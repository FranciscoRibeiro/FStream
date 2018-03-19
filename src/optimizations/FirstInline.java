package optimizations;

/* Example being considered during manual optimizations:
foldl (+) 0 (append (stream xs) (stream ys)) */

import datatypes.Done;
import datatypes.Skip;
import datatypes.Step;
import datatypes.Yield;
import util.Either;
import util.Left;
import util.Right;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class FirstInline extends MasterBenchmark{

    public static void main(String[] args) {
        System.out.println("FirstInline...");
        /*ArrayList<Integer> xs = new ArrayList<>(Arrays.asList(new Integer[]{1, 2, 3, 4, 5}));
        ArrayList<Integer> ys = new ArrayList<>(Arrays.asList(new Integer[]{6, 7, 8, 9, 10}));*/

        FirstInline fi = new FirstInline();

        fi.populate();

        fi.warmUp();

        fi.measure();

        fi.end();
    }

    @Override
    public void work() {
        BiFunction<Integer, Integer, Integer> f = (a, b) -> a+b;


        Function<Object, Step> nextStream = x -> {
            List aux = (List) x;

            if(aux.isEmpty()){
                return new Done();
            }
            else{
                List<Integer> sub = aux.subList(1, aux.size());
                return new Yield<Integer, List<Integer>>((Integer) aux.get(0), sub);
            }
        };

        Function<Object, Step> nextStream1 = x -> {
            List aux = (List) x;

            if(aux.isEmpty()){
                return new Done();
            }
            else{
                List<Integer> sub = aux.subList(1, aux.size());
                return new Yield<Integer, List<Integer>>((Integer) aux.get(0), sub);
            }
        };

        Function<Object, Step> nextAppend = x -> {
            if(x instanceof Left){
                Step aux = nextStream.apply(((Left) x).fromLeft());

                if(aux instanceof Done){
                    return new Skip<Either>(new Right(ys));
                }
                else if(aux instanceof Skip){
                    return new Skip<Either>(new Left(aux.state));
                }
                else if(aux instanceof Yield){
                    return new Yield<Integer, Either>((Integer) aux.elem, new Left(aux.state));
                }
            }
            else if(x instanceof Right){
                Step aux = nextStream1.apply(((Right) x).fromRight());

                if(aux instanceof Done){
                    return new Done();
                }
                else if(aux instanceof Skip){
                    return new Skip<Either>(new Right(aux.state));
                }
                else if(aux instanceof Yield){
                    return new Yield<Integer, Either>((Integer) aux.elem, new Right(aux.state));
                }
            }

            return null;
        };

        Integer value = 0;
        Object auxState = new Left(xs);
        boolean over = false;

        while (!over) {
            Step step = nextAppend.apply(auxState);

            if (step instanceof Done) {
                over = true;
            } else if (step instanceof Skip) {
                auxState = step.state;
            } else if (step instanceof Yield) {
                auxState = step.state;
                value = f.apply(value, (Integer) step.elem);
            }
        }

        Integer res = value;

        System.out.println(res);
    }
}
