package optimizations.optimizations_unfoldr_foldrRecursive;

import datatypes.*;
import util.Pair;
import util.RecursiveLambda;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public class FirstInline {
    public static void main(String[] args) {
        Function<Integer, Optional<Pair<Integer,Integer>>> f = x -> {
            if (x > 0) {
                return Optional.of(new Pair<>(x, x - 1));
            } else {
                return Optional.empty();
            }
        };

        Function<Object, Step> nextUnfoldr = x -> {
            Optional<Pair<Integer, Integer>> aux = f.apply((Integer) x);

            if(!aux.isPresent()){
                return new Done();
            }
            else{
                return new Yield<>(aux.get().getX(), aux.get().getY());
            }
        };

        RecursiveLambda<Function<Object,Integer>> go = new RecursiveLambda<>();
        go.function = x -> {
            Integer res1 = null;
            Step step = nextUnfoldr.apply(x);

            if (step instanceof Done) {
                res1 = 1;
            } else if (step instanceof Skip) {
                res1 = go.function.apply(step.state);
            } else if (step instanceof Yield) {
                res1 = ((BiFunction<Integer, Integer, Integer>) (x1, y) -> x1 * y).apply((Integer) step.elem, go.function.apply(step.state));
            }

            return res1;
        };

        Integer res = go.function.apply(5);
        System.out.println(res);
    }
}
