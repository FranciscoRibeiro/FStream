package optimizations.optimizations_unfoldr_foldrRecursive;

import datatypes.Step;
import util.Pair;
import util.RecursiveLambda;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public class TrivialRewriteInFoldr {
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
                    res1[0] = 1;
                } else {
                    res1[0] = ((BiFunction<Integer, Integer, Integer>) (x1, y) -> x1 * y).apply((Integer) aux.get().getX(), go.function.apply(aux.get().getY()));
                }

                return null;
            }).apply(x);

            return res1[0];
        };

        Integer res = go.function.apply(5);
        System.out.println(res);
    }
}
