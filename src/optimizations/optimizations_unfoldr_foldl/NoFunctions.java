package optimizations.optimizations_unfoldr_foldl;

import util.Pair;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public class NoFunctions {
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
            Optional<Pair<Integer, Integer>> aux = f.apply((Integer) auxState);

            if (!aux.isPresent()) {
                over = true;
            } else {
                auxState = aux.get().getY();
                value = ((BiFunction<Integer, Integer, Integer>) (x, y) -> x * y).apply(value, aux.get().getX());
            }
        }

        Integer res = value;
        System.out.println(res);
    }
}
