package optimizations.optimizations_foldl_map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class ForEach {
    public static void main(String[] args) {
        List<Integer> ints = new ArrayList<>(Arrays.asList(3, 4, 5, 6, 5, 7, 8, 9, 0));

        Function<Integer, Boolean> p = x -> x > 2;

        Boolean value = true;

        for(Integer i: ints){
            value = value && p.apply(i);
        }

        boolean res = value;

        System.out.println(res);
    }
}
