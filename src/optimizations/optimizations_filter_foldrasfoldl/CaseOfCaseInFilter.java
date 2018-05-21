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

public class CaseOfCaseInFilter {
    public static void main(String[] args) {
        List<Integer> l = Arrays.asList(2,1,5,4,6,7,2,9,3,2,1,4);

        Function<Object, Step> nextFilter = x -> {
            Step aux = ((Function<Object, Step>) x2 -> {
                List aux1 = (List) x2;

                if (aux1.isEmpty()) {
                    Step innerAux = new Done();

                    if(innerAux instanceof Done){
                        return new Done();
                    }
                    else if(innerAux instanceof Skip){
                        return new Skip<>(innerAux.state);
                    }
                    else if(innerAux instanceof Yield){
                        if(((Predicate) (Predicate<Integer>) x1 -> x1 <= 2).test(innerAux.elem)){
                            return new Yield<>((Integer) innerAux.elem, innerAux.state);
                        }
                        else{
                            return new Skip<>(innerAux.state);
                        }
                    }
                } else {
                    List<Integer> sub = aux1.subList(1, aux1.size());
                    Step innerAux = new Yield<Integer, List<Integer>>((Integer) aux1.get(0), sub);

                    if(innerAux instanceof Done){
                        return new Done();
                    }
                    else if(innerAux instanceof Skip){
                        return new Skip<>(innerAux.state);
                    }
                    else if(innerAux instanceof Yield){
                        if(((Predicate) (Predicate<Integer>) x1 -> x1 <= 2).test(innerAux.elem)){
                            return new Yield<>((Integer) innerAux.elem, innerAux.state);
                        }
                        else{
                            return new Skip<>(innerAux.state);
                        }
                    }
                }

                return null;
            }).apply(x);

            return aux;
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
