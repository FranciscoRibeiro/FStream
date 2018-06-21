package demos.fold;

import util.Pair;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import static datatypes.FStream.fstream;

public class AverageFoldrTailRec {
    public static void main(String[] args) {
        //Random r = new Random(3);
        List<Integer> lInts = new ArrayList<>();

        for(int i = 0; i < 3000; i++){
            //lInts.add(r.nextInt(500));
            lInts.add(i);
        }

        BiFunction<Integer, Pair<Integer,Integer>, Pair<Integer,Integer>> f =
                (x, p) -> new Pair<>(x+p.getX(),p.getY()+1);

        Predicate<Integer> p = x -> x%2 == 0;
        //Function<Integer,Integer> g = x -> x*x;
        long start = System.currentTimeMillis();
        Pair<Integer, Integer> resPair = fstream(lInts).filterfs(p).foldrTailRec(f, new Pair<>(0,0));
        //Pair<Integer, Integer> resPair = fstream(lInts).foldrTailRec(f, new Pair<>(0,0));
        Double res = (double) resPair.getX() / resPair.getY();
        System.out.println("Time taken: " + (System.currentTimeMillis() - start));
        System.out.println(res);
        //System.out.println(fstream(lInts).filterfs(p).unfstream());
    }
}
