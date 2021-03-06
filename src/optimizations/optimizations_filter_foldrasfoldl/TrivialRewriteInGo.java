package optimizations.optimizations_filter_foldrasfoldl;

import datatypes.Done;
import datatypes.Skip;
import datatypes.Step;
import datatypes.Yield;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public class TrivialRewriteInGo {
    public static void main(String[] args) {
        List<Integer> l = Arrays.asList(2,1,5,4,6,7,2,9,3,2,1,4);

        BiFunction<Function<Integer,Integer>, Integer, Function<Integer,Integer>> reducer = (g, x) -> (a -> g.apply(((BiFunction<Integer, Integer, Integer>) (x1, y) -> x1 - y).apply(x,a)));
        final Function<Integer, Integer>[] value = new Function[]{Function.identity()};
        final Object[] auxState = {l};
        final boolean[] over = {false};

        while (!over[0]) {
            Step step = ((Function<Object, Step>) x -> {
                Step aux = ((Function<Object, Step>) x2 -> {
                    List aux1 = (List) x2;

                    if (aux1.isEmpty()) {
                        over[0] = true;
                    } else {
                        List<Integer> sub = aux1.subList(1, aux1.size());

                        if (((Predicate) (Predicate<Integer>) x1 -> x1 <= 2).test(aux1.get(0))) {
                            value[0] = reducer.apply(value[0], (Integer) aux1.get(0));
                            auxState[0] = sub;
                        } else {
                            auxState[0] = sub;
                        }
                    }

                    return null;
                }).apply(x);

                return aux;
            }).apply(auxState[0]);
        }

        Function<Integer,Integer> finalAcc = value[0];
        Integer res = finalAcc.apply(0);

        System.out.println(res);
    }
}
