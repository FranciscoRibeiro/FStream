package optimizations.optimizations_foldl_map;

import datatypes.Step;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class TrivialRewritingInFoldl {
    public static void main(String[] args) {
        ArrayList<Integer> ints = new ArrayList<>(Arrays.asList(3, 4, 5, 6, 5, 7, 8, 9, 0));

        Function<Integer, Boolean> p = x -> x > 2;

        final Boolean[] value = {true};
        final Object[] auxState = {ints};
        final boolean[] over = {false};

        while (!over[0]) {
            Step step = ((Function<Object, Step>) x1 -> {
                Step aux = ((Function<Object, Step>) x2 -> {
                    List aux1 = (List) x2;

                    if (aux1.isEmpty()) {
                        over[0] = true;
                    } else {
                        List<Integer> sub = aux1.subList(1, aux1.size());

                        value[0] = ((BiFunction<Boolean, Boolean, Boolean>) (x, y) -> x && y).apply(value[0], (Boolean) p.apply((Integer) aux1.get(0)));
                        auxState[0] = sub;
                    }

                    return null;
                }).apply(x1);

                return aux;
            }).apply(auxState[0]);
        }

        boolean res = value[0];

        System.out.println(res);
    }
}
