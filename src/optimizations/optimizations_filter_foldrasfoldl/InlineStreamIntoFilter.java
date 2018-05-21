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

public class InlineStreamIntoFilter {
    public static void main(String[] args) {
        List<Integer> l = Arrays.asList(2,1,5,4,6,7,2,9,3,2,1,4);

        Function<Object, Step> nextFilter = x -> {
            Step aux = ((Function<Object, Step>) x2 -> {
                List aux1 = (List) x2;

                if (aux1.isEmpty()) {
                    return new Done();
                } else {
                    List<Integer> sub = aux1.subList(1, aux1.size());
                    return new Yield<Integer, List<Integer>>((Integer) aux1.get(0), sub);
                }
            }).apply(x);

            if(aux instanceof Done){
                return new Done();
            }
            else if(aux instanceof Skip){
                return new Skip<>(aux.state);
            }
            else if(aux instanceof Yield){
                if(((Predicate) (Predicate<Integer>) x1 -> x1 <= 2).test(aux.elem)){
                    return new Yield<>((Integer) aux.elem, aux.state);
                }
                else{
                    return new Skip<>(aux.state);
                }
            }

            return null;
        };

        BiFunction<Function<Integer,Integer>, Integer, Function<Integer,Integer>> reducer = (g, x) -> (a -> g.apply(((BiFunction<Integer, Integer, Integer>) (x1, y) -> x1 - y).apply(x,a)));
        Function<Integer, Integer> value = Function.identity();
        Object auxState = l;
        boolean over = false;

        while (!over) {
            Step step = nextFilter.apply(auxState);

            if (step instanceof Done) {
                over = true;
            } else if (step instanceof Skip) {
                auxState = step.state;
            } else if (step instanceof Yield) {
                value = reducer.apply(value, (Integer) step.elem);
                auxState = step.state;
            }
        }

        Function<Integer,Integer> finalAcc = value;
        Integer res = finalAcc.apply(0);

        System.out.println(res);
    }
}
