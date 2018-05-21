package optimizations.optimizations_filter_foldrasfoldl;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public class NoFunctions {
    public static void main(String[] args) {
        List<Integer> l = Arrays.asList(2,1,5,4,6,7,2,9,3,2,1,4);

        BiFunction<Function<Integer,Integer>, Integer, Function<Integer,Integer>> reducer = (g, x) -> (a -> g.apply(((BiFunction<Integer, Integer, Integer>) (x1, y) -> x1 - y).apply(x,a)));
        Function<Integer, Integer> value = Function.identity();
        List auxState = l;
        boolean over = false;

        while (!over) {
            if (auxState.isEmpty()) {
                over = true;
            } else {
                List<Integer> sub = auxState.subList(1, auxState.size());

                if (((Predicate) (Predicate<Integer>) x1 -> x1 <= 2).test(auxState.get(0))) {
                    value = reducer.apply(value, (Integer) auxState.get(0));
                    auxState = sub;
                } else {
                    auxState = sub;
                }
            }
        }

        Function<Integer,Integer> finalAcc = value;
        Integer res = finalAcc.apply(0);

        System.out.println(res);
    }
}
