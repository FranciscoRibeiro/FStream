package optimizations.optimizations_filter_foldrasfoldl;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public class SimpleLoop {
    public static void main(String[] args) {
        List<Integer> l = Arrays.asList(2,1,5,4,6,7,2,9,3,2,1,4);

        BiFunction<Function<Integer,Integer>, Integer, Function<Integer,Integer>> reducer = (g, x) ->
                (a -> g.apply(((BiFunction<Integer, Integer, Integer>) (x1, y) -> x1 - y).apply(x,a)));
        Function<Integer, Integer> value = Function.identity();

        for(Integer x : l){
            if (((Predicate<Integer>) x1 -> x1 <= 2).test(x)) {
                value = reducer.apply(value, x);
            }
        }

        Function<Integer,Integer> finalAcc = value;
        Integer res = finalAcc.apply(0);

        System.out.println(res);
    }
}
