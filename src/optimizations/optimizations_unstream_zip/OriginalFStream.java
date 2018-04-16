package optimizations.optimizations_unstream_zip;

import datatypes.FStream;
import util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

public class OriginalFStream {
    public static void main(String[] args) {
        System.out.println("OriginalFStream...");

        ArrayList<Integer> xs = new ArrayList<>(Arrays.asList(new Integer[]{1, 2, 3, 4, 5}));
        ArrayList<String> ys = new ArrayList<>(Arrays.asList(new String[]{"ola", "hello", "hallo", "hola", "ciao"}));

        Predicate<Integer> p = x -> x < 5;

        ArrayList<Pair<Integer,String>> res = (ArrayList<Pair<Integer, String>>)
                FStream.fstream(xs)
                .filterfs(p)
                .zipfs(FStream.fstream(ys))
                .unfstream();

        System.out.println(res);
    }
}
