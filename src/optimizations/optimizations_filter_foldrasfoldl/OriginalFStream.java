package optimizations.optimizations_filter_foldrasfoldl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import static datatypes.FStream.fstream;

public class OriginalFStream {
    public static void main(String[] args) {
        List<Integer> l = new ArrayList<>(Arrays.asList(2,1,5,4,6,7,2,9,3,2,1,4));

        for(int i = 0; i < 40000000; i++){
            l.add(i);
        }

        //Predicate<Integer> p = x -> x <= 2;
        Predicate<Long> p = x -> x <= 2;
        //BiFunction<Integer, Integer, Integer> f = (x, y) -> x-y;
        BiFunction<Long, Long, Long> f = (x, y) -> x-y;

        long start = System.currentTimeMillis();
        Long res = fstream(l).mapfs(Long::valueOf).filterfs(p).foldrAsFoldl(f, (long) 0);
        System.out.println(System.currentTimeMillis() - start);

        System.out.println(res);
    }
}
