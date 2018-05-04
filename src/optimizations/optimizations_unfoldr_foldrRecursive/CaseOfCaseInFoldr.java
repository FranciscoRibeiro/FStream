package optimizations.optimizations_unfoldr_foldrRecursive;

import datatypes.Done;
import datatypes.Skip;
import datatypes.Step;
import datatypes.Yield;
import util.Pair;
import util.RecursiveLambda;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public class CaseOfCaseInFoldr {
    public static void main(String[] args) {
        Function<Integer, Optional<Pair<Integer,Integer>>> f = x -> {
            if (x > 0) {
                return Optional.of(new Pair<>(x, x - 1));
            } else {
                return Optional.empty();
            }
        };

        RecursiveLambda<Function<Object,Integer>> go = new RecursiveLambda<>();
        go.function = x -> {
            final Integer[] res1 = {null};
            Step step = ((Function<Object, Step>) x2 -> {
                Optional<Pair<Integer, Integer>> aux = f.apply((Integer) x2);

                if (!aux.isPresent()) {
                    Step innerAux = new Done();

                    if (innerAux instanceof Done) {
                        res1[0] = 1;
                    } else if (innerAux instanceof Skip) {
                        res1[0] = go.function.apply(innerAux.state);
                    } else if (innerAux instanceof Yield) {
                        res1[0] = ((BiFunction<Integer, Integer, Integer>) (x1, y) -> x1 * y).apply((Integer) innerAux.elem, go.function.apply(innerAux.state));
                    }
                } else {
                    Step innerAux = new Yield<>(aux.get().getX(), aux.get().getY());

                    if (innerAux instanceof Done) {
                        res1[0] = 1;
                    } else if (innerAux instanceof Skip) {
                        res1[0] = go.function.apply(innerAux.state);
                    } else if (innerAux instanceof Yield) {
                        res1[0] = ((BiFunction<Integer, Integer, Integer>) (x1, y) -> x1 * y).apply((Integer) innerAux.elem, go.function.apply(innerAux.state));
                    }
                }

                return null;
            }).apply(x);

            return res1[0];
        };

        Integer res = go.function.apply(5);
        System.out.println(res);
    }
}
