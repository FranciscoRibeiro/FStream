package optimizations.optimizations_unfoldr_foldrLoop;

import datatypes.Done;
import datatypes.Skip;
import datatypes.Step;
import datatypes.Yield;
import util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public class CaseOfCaseInUnstream {
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
        final Object[] auxState = {5};
        final boolean[] over = {false};

        while (!over[0]) {
            Step step = ((Function<Object, Step>) x -> {
                Optional<Pair<Integer, Integer>> aux = f.apply((Integer) x);

                if (!aux.isPresent()) {
                    Step innerAux = new Done();

                    if (innerAux instanceof Done) {
                        over[0] = true;
                    } else if (innerAux instanceof Skip) {
                        auxState[0] = innerAux.state;
                    } else if (innerAux instanceof Yield) {
                        res1.add((Integer) innerAux.elem);
                        auxState[0] = innerAux.state;
                    }
                } else {
                    Step innerAux = new Yield<>(aux.get().getX(), aux.get().getY());

                    if (innerAux instanceof Done) {
                        over[0] = true;
                    } else if (innerAux instanceof Skip) {
                        auxState[0] = innerAux.state;
                    } else if (innerAux instanceof Yield) {
                        res1.add((Integer) innerAux.elem);
                        auxState[0] = innerAux.state;
                    }
                }

                return null;
            }).apply(auxState[0]);
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
