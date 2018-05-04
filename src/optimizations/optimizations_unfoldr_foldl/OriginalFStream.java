package optimizations.optimizations_unfoldr_foldl;

import datatypes.FStream;
import util.Pair;

import java.util.Optional;
import java.util.function.Function;

public class OriginalFStream {
    public static void main(String[] args) {
        Function<Integer, Optional<Pair<Integer,Integer>>> f = x -> {
            if (x > 0) {
                return Optional.of(new Pair<>(x, x - 1));
            } else {
                return Optional.empty();
            }
        };

        Integer res = FStream.unfoldr(f, 5).foldl((x, y) -> x * y, 1);
        System.out.println(res);
    }
}
