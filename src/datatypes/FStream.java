package datatypes;

import experimental.*;
import util.*;

import java.util.*;
import java.util.function.*;

public class FStream<T>{
    public Function<Object, Step> stepper; // the stepper function: (s -> Step a s)
    public Object state; // the stream's state

    public FStream(Function<Object, Step> stepper, Object state){
        this.stepper = stepper;
        this.state = state;
    }

    public Function<Object, Step> getStepper() {
        return stepper;
    }

    public Object getState() {
        return state;
    }

    public static <T> FStream<T> fstream(List<T> l){
        Function<Object, Step> nextStream = x -> {
            List aux = (List) x;

            if(aux.isEmpty()){
                return new Done();
            }
            else{
                List<T> sub = aux.subList(1, aux.size());
                return new Yield<T, List<T>>((T) aux.get(0), sub);
            }
        };

        return new FStream<T>(nextStream, l);
    }

    public static <T> FStream<T> fstreamBT(BTree<T> b){
        Function<Object, Step> nextStream = x -> {
            BTree aux = (BTree) x;

            if(aux instanceof Leaf){
                return new LeafBT(((Leaf) aux).value);
            }
            else{
                return new BranchBT(((Branch) aux).left, ((Branch) aux).right);
            }
        };

        return new FStream<T>(nextStream, b);
    }

    public List<T> unfstream(){
        ArrayList<T> res = new ArrayList<>();
        Object auxState = this.state;
        boolean over = false;

        while (!over) {
            Step step = this.stepper.apply(auxState);

            if (step instanceof Done) {
                over = true;
            } else if (step instanceof Skip) {
                auxState = step.state;
            } else if (step instanceof Yield) {
                res.add((T) step.elem);
                auxState = step.state;
            }
        }

        return res;
    }

    public BTree<T> unfstreamBT(){
        BTree<T> res = null;
        //Object auxState = this.state;

        Step step = this.stepper.apply(this.state);

        if (step instanceof BranchBT) {
            FStream<T> f1 = new FStream<>(this.stepper, ((BranchBT) step).state1);
            FStream<T> f2 = new FStream<>(this.stepper, ((BranchBT) step).state2);
            res = new Branch<>(f1.unfstreamBT(), f2.unfstreamBT());
        } else if (step instanceof Skip) {
            //auxState = step.state;
        } else if (step instanceof LeafBT) {
            res = new Leaf<>((T) step.elem);
        }

        return res;
    }

    public <S> FStream<S> mapfs(Function<T,S> funcTtoS){
        Function<Object, Step> nextMap = x -> {
            Step aux = this.stepper.apply(x);

            if(aux instanceof Done){
                return new Done();
            }
            else if(aux instanceof Skip){
                return new Skip<>(aux.state);
            }
            else if(aux instanceof Yield){
                return new Yield<>(funcTtoS.apply((T) aux.elem), aux.state);
            }

            return null;
        };

        return new FStream<S>(nextMap, this.state);
    }

    public FStream<T> filterfs(Predicate p){
        Function<Object, Step> nextFilter = x -> {
            Step aux = this.stepper.apply(x);

            if(aux instanceof Done){
                return new Done();
            }
            else if(aux instanceof Skip){
                return new Skip<>(aux.state);
            }
            else if(aux instanceof Yield){
                if(p.test(aux.elem)){
                    return new Yield<>((T) aux.elem, aux.state);
                }
                else{
                    return new Skip<>(aux.state);
                }
            }

            return null;
        };

        return new FStream<T>(nextFilter, this.state);
    }

    public FStream<T> appendfs(FStream<T> streamB){
      Function<Object, Step> nextAppend = x -> {
          if(x instanceof Left){
              Step aux = this.stepper.apply(((Left) x).fromLeft());

              if(aux instanceof Done){
                  return new Skip<Either>(new Right(streamB.state));
              }
              else if(aux instanceof Skip){
                  return new Skip<Either>(new Left(aux.state));
              }
              else if(aux instanceof Yield){
                  return new Yield<T, Either>((T) aux.elem, new Left(aux.state));
              }
          }
          else if(x instanceof Right){
              Step aux = streamB.stepper.apply(((Right) x).fromRight());

              if(aux instanceof Done){
                  return new Done();
              }
              else if(aux instanceof Skip){
                  return new Skip<Either>(new Right(aux.state));
              }
              else if(aux instanceof Yield){
                  return new Yield<T, Either>((T) aux.elem, new Right(aux.state));
              }
          }

          return null;
      };

      return new FStream<T>(nextAppend, new Left(this.state));
    }

    public <S> FStream<Pair<T,S>> zipfs(FStream<S> streamB){
        Function<Object, Step> nextZip = x -> {
            if(!(((Triple) x).getElem()).isPresent()){
                Step aux = this.stepper.apply(((Triple) x).getStateA());

                if(aux instanceof Done){
                    return new Done();
                }
                else if(aux instanceof Skip){
                    return new Skip<>(new Triple(aux.state, ((Triple) x).getStateB(), Optional.empty()));
                }
                else if(aux instanceof Yield){
                    return new Skip<>(new Triple(aux.state, ((Triple) x).getStateB(), Optional.of(aux.elem)));
                }
            }
            else{ //There is a value present in Optional
                Step aux = streamB.stepper.apply(((Triple) x).getStateB());

                if(aux instanceof Done){
                    return new Done();
                }
                else if(aux instanceof Skip){
                    return new Skip<>(new Triple(((Triple) x).getStateA(), aux.state, ((Triple) x).getElem()));
                }
                else if(aux instanceof Yield){
                    return new Yield<>(new Pair<>(((Triple) x).getElem().get(), aux.elem), new Triple(((Triple) x).getStateA(), aux.state, Optional.empty()));
                }
            }

            return null;
        };

        return new FStream<>(nextZip, new Triple<>(this.state, streamB.state, Optional.empty()));
    }

    public <S> FStream<S> concatMap(Function<T, FStream<S>> f){
        Function<Object, Step> nextConcatMap = x -> {
            Optional opAux = (Optional) ((Pair) x).getY();

            if(!(opAux.isPresent())){
                Step aux = this.stepper.apply(((Pair) x).getX());

                if(aux instanceof Done){
                    return new Done();
                }
                else if(aux instanceof Skip){
                    return new Skip(new Pair<>(aux.state, Optional.empty()));
                }
                else if(aux instanceof Yield){
                    return new Skip(new Pair<>(aux.state, Optional.of(f.apply((T) aux.elem))));
                }
            }
            else{
                FStream fAux = (FStream) opAux.get();
                Step aux = (Step) fAux.getStepper().apply(fAux.getState());

                if(aux instanceof Done){
                    return new Skip(new Pair(((Pair) x).getX(), Optional.empty()));
                }
                else if(aux instanceof Skip){
                    return new Skip(new Pair(((Pair) x).getX(), Optional.of(new FStream<>(fAux.getStepper(), aux.state))));
                }
                else if(aux instanceof Yield){
                    return new Yield(aux.elem, new Pair(((Pair) x).getX(), Optional.of(new FStream<>(fAux.getStepper(), aux.state))));
                }
            }

            return null;
        };

        return new FStream<>(nextConcatMap, new Pair(this.state, Optional.empty()));
    }

    public FStream<T> intersect(FStream<?> streamB){
        Function<Object, Step> nextIntersect = x -> {
            if(!(((Triple) x).getElem()).isPresent()){
                Step aux = this.stepper.apply(((Triple) x).getStateA());

                if(aux instanceof Done){
                    return new Done();
                }
                else if(aux instanceof Skip){
                    return new Skip<>(new Triple<>(aux.state, ((Triple) x).getStateB(), Optional.empty()));
                }
                else if(aux instanceof Yield){
                    return new Skip<>(new Triple<>(aux.state, streamB.state, Optional.of(aux.elem)));
                }
            }
            else{
                Step aux = streamB.stepper.apply(((Triple) x).getStateB());

                if(aux instanceof Done){
                    return new Skip<>(new Triple<>(((Triple) x).getStateA(), streamB.state, Optional.empty()));
                }
                else if(aux instanceof Skip){
                    return new Skip<>(new Triple<>(((Triple) x).getStateA(), aux.state, ((Triple) x).getElem()));
                }
                else if(aux instanceof Yield){
                    if(((Triple) x).getElem().get().equals(aux.elem)){
                        return new Yield<>(((Triple) x).getElem().get(), new Triple<>(((Triple) x).getStateA(), streamB.state, Optional.empty()));
                    }
                    else{
                        return new Skip<>(new Triple<>(((Triple) x).getStateA(), aux.state, ((Triple) x).getElem()));
                    }
                }
            }

            return null;
        };

        return new FStream<>(nextIntersect, new Triple<>(this.state, streamB.state, Optional.empty()));
    }

    public FStream<T> drop(int n){
        Function<Object, Step> nextDrop = x -> {
            Pair p = (Pair) x;
            if((((Optional) p.getX()).isPresent())){
                int number = (int) ((Optional) p.getX()).get();

                if(number == 0){
                    return new Skip<>(new Pair(Optional.empty(), p.getY()));
                }
                else{
                    Step aux = this.stepper.apply(p.getY());

                    if(aux instanceof Done){
                        return new Done();
                    }
                    else if(aux instanceof Skip){
                        return new Skip<>(new Pair<>(Optional.of(number), aux.state));
                    }
                    else if(aux instanceof Yield){
                        return new Skip<>(new Pair<>(Optional.of(number - 1), aux.state));
                    }
                }
            }
            else{
                Step aux = this.stepper.apply(p.getY());

                if(aux instanceof Done){
                    return new Done();
                }
                else if(aux instanceof Skip){
                    return new Skip<>(new Pair<>(Optional.empty(), aux.state));
                }
                else if(aux instanceof Yield){
                    return new Yield<>(aux.elem, new Pair<>(Optional.empty(), aux.state));
                }
            }

            return null;
        };

        return new FStream<T>(nextDrop, new Pair<>(Optional.of(Math.max(0, n)), this.state));
    }

    public static <T,S> FStream<T> unfoldr(Function<S, Optional<Pair<T,S>>> builder, S seed){
        Function<Object, Step> nextUnfoldr = x -> {
            Optional<Pair<T, S>> aux = builder.apply((S) x);

            if(!aux.isPresent()){
                return new Done();
            }
            else{
                return new Yield<>(aux.get().getX(), aux.get().getY());
            }
        };

        return new FStream<T>(nextUnfoldr, seed);
    }

    public static <T,S> FStream<T> unfoldrBT(Function<S, Either<T, Pair<S,S>>> builder, S seed){
        Function<Object, Step> nextUnfoldrBT = x -> {
            Either<T, Pair<S,S>> aux = builder.apply((S) x);

            if(aux instanceof Left){
                return new LeafBT<>(((Left) aux).fromLeft());
            }
            else if(aux instanceof Right){
                Pair p = (Pair) ((Right) aux).fromRight();
                return new BranchBT(p.getX(), p.getY());
            }

            return null;
        };

        return new FStream<T>(nextUnfoldrBT, seed);
    }

    public static <S> FStream<S> until(int number){
        Function<Object, Step> nextUntil =  x -> {
            if((int) x > number){
                return new Done();
            }
            else{
                return new Yield("word"+x, ((int) x+1));
            }
        };

        return new FStream<S>(nextUntil, 0);
    }

    public <S> S foldr(BiFunction<T,S,S> f, S value){
        RecursiveLambda<Function<Object,S>> go = new RecursiveLambda<>();
        go.function = x -> {
            S res = null;
            Step step = this.stepper.apply(x);

            if (step instanceof Done) {
                res = value;
            } else if (step instanceof Skip) {
                res = go.function.apply(step.state);
            } else if (step instanceof Yield) {
                res = f.apply((T) step.elem, go.function.apply(step.state));
            }

            return res;
        };

        return go.function.apply(this.state);
    }

    public <S> S foldrLoop(BiFunction<T,S,S> f, S value){
        List<T> l = this.unfstream();

        for(int i = l.size() - 1; i >= 0; i--){
            T t = l.get(i);
            value = f.apply(t, value);
        }

        return value;
    }

    public <S> S foldrAsFoldl(BiFunction<T,S,S> f, S value){
        BiFunction<Function<S,S>, T, Function<S,S>> reducer = (g, x) -> (a -> g.apply(f.apply(x,a)));
        Function<S,S> finalAcc = this.foldl(reducer, Function.identity());
        return finalAcc.apply(value);
    }

    public <S> S foldrTailRec(BiFunction<T,S,S> f, S value){
        Continuation.b = f;
        Continuation cont = new ContinuationId();
        boolean over = false;
        Continuation.globalState = this.state;
        Continuation.res = value;

        while(!over){
            Step step = this.stepper.apply(Continuation.globalState);

            if(step instanceof Done){
                cont = cont.execute(Continuation.res);

                if(cont == null){
                    over = true;
                }
            }
            else if(step instanceof Skip){
                Continuation.globalState = step.state;
            }
            else if(step instanceof Yield){
                Continuation.globalState = step.state;

                Continuation<S, ContinuationListElem<T,S>> nextCont = new ContinuationListElem<T,S>((T) step.elem, cont);
                cont = new ContinuationListFold(nextCont);
            }
        }

        return (S) Continuation.res;
    }

    public <S> S foldl(BiFunction<S,T,S> f, S value) {
        Object auxState = this.state;
        boolean over = false;

        while (!over) {
            Step step = stepper.apply(auxState);

            if (step instanceof Done) {
                over = true;
            } else if (step instanceof Skip) {
                auxState = step.state;
            } else if (step instanceof Yield) {
                value = f.apply(value, (T) step.elem);
                auxState = step.state;
            }
        }

        return value;
    }

    public <S> S foldBT(BiFunction<S,S,S> b, Function<T,S> l) {
        RecursiveLambda<Function<Object,S>> go = new RecursiveLambda<>();
        go.function = x -> {
            S res = null;
            Step step = this.stepper.apply(x);

            if (step instanceof LeafBT) {
                res = l.apply((T) step.elem);
            } else if (step instanceof Skip) {
                res = go.function.apply(step.state);
            } else if (step instanceof BranchBT) {
                res = b.apply(go.function.apply(((BranchBT) step).state1), go.function.apply(((BranchBT) step).state2));
            }

            return res;
        };

        return go.function.apply(this.state);
    }

    public <S> S foldlBT(BiFunction<S,S,S> b, Function<T,S> l) {
        S res = null;
        boolean over = false;
        Stack<Step> tree = new Stack<>();
        tree.push(this.stepper.apply(this.state));
        Optional<S> opAux = Optional.empty();
        Step poppedStep;

        while(!over){
            if(tree.empty()){
                res = opAux.get();
                over = true;
            }
            else if(tree.peek() instanceof LeafBT){
                if(!opAux.isPresent()){
                    poppedStep = tree.pop();
                    opAux = Optional.of(l.apply((T) poppedStep.elem));
                }
                else{
                    poppedStep = tree.pop();
                    opAux = Optional.of(b.apply(opAux.get(), l.apply((T) poppedStep.elem)));
                }
            }
            else if(tree.peek() instanceof BranchBT){
                poppedStep = tree.pop();
                tree.push(this.stepper.apply(((BranchBT) poppedStep).state2));
                tree.push(this.stepper.apply(((BranchBT) poppedStep).state1));
            }
        }

        return res;
    }

    public <S> S foldlBTv2(BiFunction<S,S,S> b, Function<T,S> l) {
        S res = null;
        boolean over = false;
        Stack<Object> states = new Stack<>();
        states.push(this.state);
        Optional<S> opAux = Optional.empty();

        while(!over){
            if(states.empty()){
                res = opAux.get();
                over = true;
            }

            else{
                Step step = this.stepper.apply(states.pop());

                if(step instanceof LeafBT){
                    if(!opAux.isPresent()){
                        opAux = Optional.of(l.apply((T) step.elem));
                    }
                    else{
                        opAux = Optional.of(b.apply(opAux.get(), l.apply((T) step.elem)));
                    }
                }
                else if(step instanceof BranchBT){
                    states.push(((BranchBT) step).state2);
                    states.push(((BranchBT) step).state1);
                }
            }
        }

        return res;
    }

    public <S> S foldBTTailRec(BiFunction<S,S,S> b, Function<T,S> l) {
        Continuation.b = b;
        Continuation cont = new ContinuationId();
        boolean over = false;
        Continuation.globalState = this.state;

        while(!over){
            Step step = this.stepper.apply(Continuation.globalState);

            if(step instanceof LeafBT){
                cont = cont.execute(l.apply((T) step.elem));

                if(cont == null){
                    over = true;
                }
            }
            else if(step instanceof BranchBT){
                Continuation.globalState = ((BranchBT) step).state1;

                Continuation<S,ContinuationBranchOp<S>> nextCont = new ContinuationBranchOp<>(cont);
                cont = new ContinuationFold(((BranchBT) step).state2, nextCont);
            }
        }

        return (S) Continuation.res;
    }




    public T head(){
        Object auxState = this.state;
        boolean over = false;
        T res = null;

        while(!over){
            Step step = stepper.apply(auxState);

            if (step instanceof Done) {
                over = true;
            } else if (step instanceof Skip) {
                auxState = step.state;
            } else if (step instanceof Yield) {
                res = (T) step.elem;
                over = true;
            }
        }

        return res;
    }

    public boolean isEmpty(){
        Object auxState = this.state;
        boolean empty = true, over = false;

        while(!over){
            Step step = stepper.apply(auxState);

            if (step instanceof Done) {
                over = true;
            } else if (step instanceof Skip) {
                auxState = step.state;
            } else if (step instanceof Yield) {
                empty = false;
                over = true;
            }
        }

        return empty;
    }

    public static <T> List<T> map(Function f, ArrayList<T> l){
        return fstream(l).mapfs(f).unfstream();
    }

    public static void main(String[] args){
        final int SIZE = 6;
        ArrayList<Integer> l = new ArrayList<>();
        for(int i = 1; i < SIZE; i++){
            l.add(i);
        }
        l.add(1);

        FStream<Integer> fsOrig = fstream(l);
        System.out.println(fsOrig.state);
        List<Integer> lOrig = fsOrig.unfstream();
        System.out.println(lOrig);
        System.out.println("Test for reference equality: " + (lOrig == l));

        System.out.println("Mapping...");
        Function<Integer, Integer> f = n -> n + 30;
        FStream<Integer> fsMap = fsOrig.mapfs(f);
        List<Integer> lMapped = fsMap.unfstream();
        System.out.println("Mapped: " + lMapped);
        System.out.println("Original: " + lOrig);

        System.out.println("Filtering...");
        Predicate<Integer> p = n -> n < 3;
        FStream<Integer> fsFilter = fsOrig.filterfs(p);
        List<Integer> lFiltered = fsFilter.unfstream();
        System.out.println("Filtered: " + lFiltered);
        System.out.println("Original: " + lOrig);

        Function<Integer,Integer> inc = x -> x + 1;
        System.out.println("Mapping chained...");
        FStream<Integer> fsInc = fsOrig.mapfs(inc).mapfs(inc);
        List<Integer> lInc = fsInc.unfstream();
        System.out.println(lInc);
        System.out.println("Mapping merged...");
        FStream<Integer> fsInc2 = fsOrig.mapfs(inc.andThen(inc));
        List<Integer> lInc2 = fsInc2.unfstream();
        System.out.println(lInc2);

        System.out.println("Appending...");
        ArrayList<Integer> lB = new ArrayList<>();
        for(int i = 6; i < SIZE+6; i++){
            lB.add(i);
        }
        FStream<Integer> fsOrigB = fstream(lB);
        Predicate<Integer> p2 = n -> n >= 4;
        List<Integer> lAppended = fsOrig.appendfs(fsOrigB).appendfs(fsOrigB).mapfs(inc).filterfs(p2).unfstream();
        System.out.println(lAppended);

        System.out.println("Zipping...");
        ArrayList<String> lStrings = new ArrayList<>(Arrays.asList(new String[]{"hello", "ola", "hola", "ciao", "hallo"}));
        FStream<String> fsString = fstream(lStrings);
        List<Pair<Integer,String>> lZipped = fsOrig.mapfs(inc).filterfs(p2).zipfs(fsString).unfstream();
        System.out.println(lZipped);

        System.out.println("ConcatMapping...");
        ArrayList<Integer> lInts = new ArrayList<>(Arrays.asList(new Integer[]{1, 2, 3}));
        FStream<Integer> fsInts = fstream(lInts);
        System.out.println(fsInts.concatMap(FStream::until).unfstream());

        System.out.println("Foldr...");
        System.out.println(fsInts.filterfs(x -> (int) x >= 2).foldr(((x,y) -> x-y), 0));
        System.out.println("FoldrLoop...");
        System.out.println(fsInts.filterfs(x -> (int) x >= 2).foldrLoop(((x,y) -> x-y), 0));
        System.out.println("Foldl...");
        System.out.println(fsInts.filterfs(x -> (int) x >= 2).foldl(((x,y) -> x-y), 0));

        System.out.println("GHC Optimizations...");
        ArrayList<Integer> xsList = new ArrayList<>(Arrays.asList(new Integer[]{1, 2, 3, 4, 5}));
        ArrayList<Integer> ysList = new ArrayList<>(Arrays.asList(new Integer[]{6, 7, 8, 9, 10}));
        FStream<Integer> xsFs = fstream(xsList);
        FStream<Integer> ysFs = fstream(ysList);
        System.out.println(xsFs.appendfs(ysFs).foldl((x,y) -> x + y, 0));
    }
}