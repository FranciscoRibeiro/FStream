package optimizations.optimizations_foldl_append;

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

public class TrivialRewriteInAppend extends MasterBenchmarkFoldlAppend {

    public static void main(String[] args) {
        System.out.println("TrivialRewriteInAppend...");
        /*ArrayList<Integer> xs = new ArrayList<>(Arrays.asList(new Integer[]{1, 2, 3, 4, 5}));
        ArrayList<Integer> ys = new ArrayList<>(Arrays.asList(new Integer[]{6, 7, 8, 9, 10}));*/

        TrivialRewriteInAppend tra = new TrivialRewriteInAppend();

        tra.populate();

        tra.warmUp();

        tra.measure();

        tra.end();
    }

    @Override
    public void work() {
        BiFunction<Long, Integer, Long> f = (a, b) -> a+b;


        Function<Object, Step> nextAppend = x -> {
            if(x instanceof Left){
                Step aux = ((Function<Object, Step>) x1 -> {
                    List aux1 = (List) x1;

                    if (aux1.isEmpty()) {
                        return new Skip<Either>(new Right(ys));

                    } else {
                        List<Integer> sub = aux1.subList(1, aux1.size());
                        return new Yield<Integer, Either>((Integer) aux1.get(0), new Left(sub));
                    }
                }).apply(((Left) x).fromLeft());

                return aux;
            }
            else if(x instanceof Right){
                Step aux = ((Function<Object, Step>) x1 -> {
                    List aux1 = (List) x1;

                    if (aux1.isEmpty()) {
                        return new Done();
                    } else {
                        List<Integer> sub = aux1.subList(1, aux1.size());
                        return new Yield<Integer, Either>((Integer) aux1.get(0), new Right(sub));
                    }
                }).apply(((Right) x).fromRight());

                return aux;
            }

            return null;
        };

        Long value = (long) 0;
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

        Long res = value;

        System.out.println(res);
    }
}
