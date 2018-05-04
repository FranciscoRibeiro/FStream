package optimizations.optimizations_unfoldr_foldl;

import datatypes.Done;
import datatypes.Skip;
import datatypes.Step;
import datatypes.Yield;
import util.Pair;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public class InlineUnfoldrIntoFoldl {
    public static void main(String[] args) {
        Function<Integer, Optional<Pair<Integer,Integer>>> f = x -> {
            if (x > 0) {
                return Optional.of(new Pair<>(x, x - 1));
            } else {
                return Optional.empty();
            }
        };

        Integer value = 1;
        Object auxState = 5;
        boolean over = false;

        while (!over) {
            Step step = ((Function<Object, Step>) x1 -> {
                Optional<Pair<Integer, Integer>> aux = f.apply((Integer) x1);

                if (!aux.isPresent()) {
                    return new Done();
                } else {
                    return new Yield<>(aux.get().getX(), aux.get().getY());
                }
            }).apply(auxState);

            if (step instanceof Done) {
                over = true;
            } else if (step instanceof Skip) {
                auxState = step.state;
            } else if (step instanceof Yield) {
                auxState = step.state;
                value = ((BiFunction<Integer, Integer, Integer>) (x, y) -> x * y).apply(value, (Integer) step.elem);
            }
        }

        Integer res = value;
        System.out.println(res);
    }
}
