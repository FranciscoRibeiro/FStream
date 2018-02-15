import java.util.*;
import java.util.function.*;

public class FStream<T>{
    private Function<Object, Step> stepper; // the stepper function: (s -> Step a s)
    private Object state; // the stream's state

    public FStream(Function<Object, Step> stepper, Object state){
        this.stepper = stepper;
        this.state = state;
    }

    public static <T> FStream<T> fstream(ArrayList<T> l){
        Function<Object, Step> stepper = x -> {
            ArrayList aux = (ArrayList) x;

            if(aux.isEmpty()){
                return new Done();
            }
            else{
                return new Yield<T, ArrayList<T>>((T) aux.get(0), new ArrayList<T>(aux.subList(1, aux.size())));
            }
        };

        return new FStream<T>(stepper, l);
    }

    public ArrayList<T> unfstream(){
        ArrayList<T> res = new ArrayList<>();
        Step step = this.stepper.apply(this.state);

        while(!(step instanceof Done)){
            if(step instanceof Yield){
                res.add((T) step.elem);
            }
            step = this.stepper.apply(step.state);
        }

        return res;
    }

    public <S> FStream<S> mapfs(Function<T,S> funcTtoS){
        Function<Object, Step> stepper = x -> {
            Step aux = this.stepper.apply(x);

            if(aux instanceof Done){
                return new Done();
            }
            else if(aux instanceof Skip){
                return new Skip<>(aux.state); //Need to change this later
            }
            else if(aux instanceof Yield){
                return new Yield<>(funcTtoS.apply((T) aux.elem), aux.state);
            }

            return null;
        };

        return new FStream<S>(stepper, this.state);
    }

    public FStream<T> filterfs(Predicate p){
        Function<Object, Step> stepper = x -> {
            Step aux = this.stepper.apply(x);

            if(aux instanceof Done){
                return new Done();
            }
            else if(aux instanceof Skip){
                return new Skip<>(aux.state); //Need to change this later
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

        return new FStream<T>(stepper, this.state);
    }

    public FStream<T> appendfs(FStream<T> streamB){
      Function<Object, Step> stepper = x -> {
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

      return new FStream<T>(stepper, new Left(this.state));
    }

    public static <T> ArrayList<T> map(Function f, ArrayList<T> l){
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
        ArrayList<Integer> lOrig = fsOrig.unfstream();
        System.out.println(lOrig);
        System.out.println("Test for reference equality: " + (lOrig == l));

        System.out.println("Mapping...");
        Function<Integer, Integer> f = n -> n + 30;
        FStream<Integer> fsMap = fsOrig.mapfs(f);
        ArrayList<Integer> lMapped = fsMap.unfstream();
        System.out.println("Mapped: " + lMapped);
        System.out.println("Original: " + lOrig);

        System.out.println("Filtering...");
        Predicate<Integer> p = n -> n < 3;
        FStream<Integer> fsFilter = fsOrig.filterfs(p);
        ArrayList<Integer> lFiltered = fsFilter.unfstream();
        System.out.println("Filtered: " + lFiltered);
        System.out.println("Original: " + lOrig);

        Function<Integer,Integer> inc = x -> x + 1;
        System.out.println("Mapping chained...");
        FStream<Integer> fsInc = fsOrig.mapfs(inc).mapfs(inc);
        ArrayList<Integer> lInc = fsInc.unfstream();
        System.out.println(lInc);
        System.out.println("Mapping merged...");
        FStream<Integer> fsInc2 = fsOrig.mapfs(inc.andThen(inc));
        ArrayList<Integer> lInc2 = fsInc2.unfstream();
        System.out.println(lInc2);

        System.out.println("Appending...");
        ArrayList<Integer> lB = new ArrayList<>();
        for(int i = 6; i < SIZE+6; i++){
            lB.add(i);
        }
        FStream<Integer> fsOrigB = fstream(lB);
        Predicate<Integer> p2 = n -> n >= 4;
        ArrayList<Integer> lAppended = fsOrig.appendfs(fsOrigB).appendfs(fsOrigB).mapfs(inc).filterfs(p2).unfstream();
        System.out.println(lAppended);
    }
}