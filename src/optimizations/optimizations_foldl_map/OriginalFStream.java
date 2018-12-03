package optimizations.optimizations_foldl_map;

import datatypes.FStream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;

public class OriginalFStream {
    public static void main(String[] args) {
        ArrayList<Integer> ints = new ArrayList<>(Arrays.asList(3,4,5,6,5,7,8,9,4));

        Function<Integer, Boolean> p = x -> x > 2;
        System.out.println(FStream.fstream(ints)
                                    .mapfs(p)
                                    .foldl((x,y) -> x && y, true));
    }
}
