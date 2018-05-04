package optimizations.optimizations_unfoldr_foldl;

import datatypes.Done;
import datatypes.Skip;
import datatypes.Step;
import datatypes.Yield;
import util.Pair;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public class CaseOfCaseInFoldl {
    public static void main(String[] args) {
        Function<Integer, Optional<Pair<Integer,Integer>>> f = x -> {
            if (x > 0) {
                return Optional.of(new Pair<>(x, x - 1));
            } else {
                return Optional.empty();
            }
        };

        final Integer[] value = {1};
        final Object[] auxState = {5};
        final boolean[] over = {false};

        while (!over[0]) {
            Step step = ((Function<Object, Step>) x1 -> {
                Optional<Pair<Integer, Integer>> aux = f.apply((Integer) x1);

                if (!aux.isPresent()) {
                    Step innerAux = new Done();

                    if (innerAux instanceof Done) {
                        over[0] = true;
                    } else if (innerAux instanceof Skip) {
                        auxState[0] = innerAux.state;
                    } else if (innerAux instanceof Yield) {
                        auxState[0] = innerAux.state;
                        value[0] = ((BiFunction<Integer, Integer, Integer>) (x, y) -> x * y).apply(value[0], (Integer) innerAux.elem);
                    }
                } else {
                    Step innerAux = new Yield<>(aux.get().getX(), aux.get().getY());

                    if (innerAux instanceof Done) {
                        over[0] = true;
                    } else if (innerAux instanceof Skip) {
                        auxState[0] = innerAux.state;
                    } else if (innerAux instanceof Yield) {
                        auxState[0] = innerAux.state;
                        value[0] = ((BiFunction<Integer, Integer, Integer>) (x, y) -> x * y).apply(value[0], (Integer) innerAux.elem);
                    }
                }

                return null;
            }).apply(auxState[0]);
        }

        Integer res = value[0];
        System.out.println(res);
    }
}
