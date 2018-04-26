package datatypes;

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
        return goFoldr(f, value, this.stepper, this.state);
    }


    public static <S, R> S goFoldr(BiFunction<R, S, S> f, S value, Function<Object, Step> stepper, Object state) {

        Step step = stepper.apply(state);

        if (step instanceof Done) {
            return value;
        } else if (step instanceof Skip) {
            return goFoldr(f, value, stepper, step.state);
        } else if (step instanceof Yield) {
            return f.apply((R) step.elem, goFoldr(f, value, stepper, step.state));
        }

        return null;
    }

    public <S> S foldrLoop(BiFunction<T,S,S> f, S value){
        List<T> l = this.unfstream();

        for(int i = l.size() - 1; i >= 0; i--){
            T t = l.get(i);
            value = f.apply(t, value);
        }

        return value;
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
                auxState = step.state;
                value = f.apply(value, (T) step.elem);
            }
        }

        return value;
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