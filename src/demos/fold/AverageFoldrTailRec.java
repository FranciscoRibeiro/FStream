package demos.fold;

import util.Pair;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import static datatypes.FStream.fstream;

public class AverageFoldrTailRec extends MasterBenchmarkFold{
    public static void main(String[] args) {
        System.out.println(MethodHandles.lookup().lookupClass().getSimpleName() + "...");

        AverageFoldrTailRec aftr = new AverageFoldrTailRec();
        aftr.POPSIZE = Integer.parseInt(args[0]);

        aftr.populate();

        aftr.warmUp();

        aftr.measure();

        aftr.end();
    }

    @Override
    public void work() {
        BiFunction<Integer, Pair<Integer,Integer>, Pair<Integer,Integer>> f =
                (x, p) -> new Pair<>(x+p.getX(),p.getY()+1);

        Predicate<Integer> p = x -> x%2 == 0;

        Pair<Integer, Integer> resPair = fstream(lInts).filterfs(p).foldrTailRec(f, new Pair<>(0,0));

        Double res = (double) resPair.getX() / resPair.getY();
        System.out.println(res);
    }
}
