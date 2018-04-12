package optimizations.optimizations_foldl_append;

import datatypes.Step;
import util.Left;
import util.Right;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class TrivialRewriteInGo extends MasterBenchmarkFoldlAppend {

    public static void main(String[] args) {
        System.out.println("TrivialRewriteInGo...");
        /*ArrayList<Integer> xs = new ArrayList<>(Arrays.asList(new Integer[]{1, 2, 3, 4, 5}));
        ArrayList<Integer> ys = new ArrayList<>(Arrays.asList(new Integer[]{6, 7, 8, 9, 10}));*/

        TrivialRewriteInGo trg = new TrivialRewriteInGo();

        trg.populate();

        trg.warmUp();

        trg.measure();

        trg.end();
    }

    @Override
    public void work() {
        BiFunction<Long, Integer, Long> f = (a, b) -> a + b;


        final Long[] value = {(long) 0};
        final Object[] auxState = {new Left(xs)};
        final boolean[] over = {false};

        while (!over[0]) {
            Step step = ((Function<Object, Step>) x -> {
                if (x instanceof Left) {
                    Step aux = ((Function<Object, Step>) x1 -> {
                        List aux1 = (List) x1;

                        if (aux1.isEmpty()) {
                            auxState[0] = new Right(ys);
                        } else {
                            List<Integer> sub = aux1.subList(1, aux1.size());
                            auxState[0] = new Left(sub);
                            value[0] = f.apply(value[0], (Integer) aux1.get(0));
                        }

                        return null;
                    }).apply(((Left) x).fromLeft());

                    return aux;
                } else if (x instanceof Right) {
                    Step aux = ((Function<Object, Step>) x1 -> {
                        List aux1 = (List) x1;

                        if (aux1.isEmpty()) {
                            over[0] = true;
                        } else {
                            List<Integer> sub = aux1.subList(1, aux1.size());
                            auxState[0] = new Right(sub);
                            value[0] = f.apply(value[0], (Integer) aux1.get(0));
                        }

                        return null;
                    }).apply(((Right) x).fromRight());

                    return aux;
                }

                return null;
            }).apply(auxState[0]);
        }

        Long res = value[0];

        System.out.println(res);
    }
}
