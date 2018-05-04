package optimizations.optimizations_unfoldr_foldl;

import datatypes.Step;
import util.Pair;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public class TrivialRewriteInFoldl {
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
                    over[0] = true;
                } else {
                    auxState[0] = aux.get().getY();
                    value[0] = ((BiFunction<Integer, Integer, Integer>) (x, y) -> x * y).apply(value[0], (Integer) aux.get().getX());
                }

                return null;
            }).apply(auxState[0]);
        }

        Integer res = value[0];
        System.out.println(res);
    }
}
