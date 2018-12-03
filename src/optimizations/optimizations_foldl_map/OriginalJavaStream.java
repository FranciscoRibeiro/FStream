package optimizations.optimizations_foldl_map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;

public class OriginalJavaStream {
    public static void main(String[] args) {
        ArrayList<Integer> ints = new ArrayList<>(Arrays.asList(3,4,5,6,5,7,8,9,0));

        Function<Integer, Boolean> p = x -> x > 2;
        System.out.println(ints.stream()
                                .map(p)
                                .reduce(true, (x,y) -> x && y));
    }
}
