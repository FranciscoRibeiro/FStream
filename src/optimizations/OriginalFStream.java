package optimizations;

import datatypes.FStream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.BiFunction;

public class OriginalFStream {
    public static void main(String[] args) {
        ArrayList<Integer> xs = new ArrayList<>(Arrays.asList(new Integer[]{1, 2, 3, 4, 5}));
        ArrayList<Integer> ys = new ArrayList<>(Arrays.asList(new Integer[]{6, 7, 8, 9, 10}));
        BiFunction<Integer, Integer, Integer> f = (a, b) -> a+b;


        FStream<Integer> xsFs = FStream.fstream(xs);
        FStream<Integer> ysFs = FStream.fstream(ys);
        Integer res = xsFs.appendfs(ysFs).foldl(f, 0);


        System.out.println(res);
    }
}
