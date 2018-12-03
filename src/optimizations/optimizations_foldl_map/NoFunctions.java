package optimizations.optimizations_foldl_map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class NoFunctions {
    public static void main(String[] args) {
        List<Integer> ints = new ArrayList<>(Arrays.asList(3, 4, 5, 6, 5, 7, 8, 9, 0));

        Function<Integer, Boolean> p = x -> x > 2;

        Boolean value = true;
        boolean over = false;

        while (!over) {
            if (ints.isEmpty()) {
                over = true;
            } else {
                List<Integer> sub = ints.subList(1, ints.size());

                value = ((BiFunction<Boolean, Boolean, Boolean>) (x, y) -> x && y).apply(value, (Boolean) p.apply((Integer) ints.get(0)));
                ints = sub;
            }
        }

        boolean res = value;

        System.out.println(res);
    }
}
