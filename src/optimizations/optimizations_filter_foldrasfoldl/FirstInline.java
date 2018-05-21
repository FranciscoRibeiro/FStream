package optimizations.optimizations_filter_foldrasfoldl;

import datatypes.*;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public class FirstInline {
    public static void main(String[] args) {
        List<Integer> l = Arrays.asList(2,1,5,4,6,7,2,9,3,2,1,4);

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

        Function<Object, Step> nextFilter = x -> {
            Step aux = nextStream.apply(x);

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
