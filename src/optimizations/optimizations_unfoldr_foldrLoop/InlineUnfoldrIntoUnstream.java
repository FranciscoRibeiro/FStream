package optimizations.optimizations_unfoldr_foldrLoop;

import datatypes.*;
import util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public class InlineUnfoldrIntoUnstream {
    public static void main(String[] args) {
        Function<Integer, Optional<Pair<Integer,Integer>>> f = x -> {
            if (x > 0) {
                return Optional.of(new Pair<>(x, x - 1));
            } else {
                return Optional.empty();
            }
        };

        Integer value = 1;
        ArrayList<Integer> res1 = new ArrayList<>();
        Object auxState = 5;
        boolean over = false;

        while (!over) {
            Step step = ((Function<Object, Step>) x -> {
                Optional<Pair<Integer, Integer>> aux = f.apply((Integer) x);

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
                res1.add((Integer) step.elem);
                auxState = step.state;
            }
        }

        List<Integer> l = res1;

        for(int i = l.size() - 1; i >= 0; i--){
            Integer t = l.get(i);
            value = ((BiFunction<Integer, Integer, Integer>) (x, y) -> x * y).apply(t, value);
        }

        Integer res = value;
        System.out.println(res);
    }
}
