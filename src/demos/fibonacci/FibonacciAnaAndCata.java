package demos.fibonacci;

import util.*;

import java.lang.invoke.MethodHandles;
import java.math.BigInteger;
import java.util.function.BiFunction;
import java.util.function.Function;

import static datatypes.FStream.fstreamBT;
import static datatypes.FStream.unfoldrBT;

public class FibonacciAnaAndCata extends MasterBenchmarkFibonacci{
    public static void main(String[] args) {
        System.out.println(MethodHandles.lookup().lookupClass().getSimpleName() + "...");

        FibonacciAnaAndCata fibAC = new FibonacciAnaAndCata();

        /*fibAC.populate();

        fibAC.warmUp();*/

        fibAC.measure();

        fibAC.end();
    }

    @Override
    public void work() {
        Function<Integer, Either<BigInteger, Pair<Integer,Integer>>> g = x -> {
            if (x == 0){
                return new Left<>(BigInteger.ZERO);
            }
            else if(x == 1){
                return new Left<>(BigInteger.ONE);
            }
            else{
                return new Right<>(new Pair<>(x-1, x-2));
            }
        };

        BiFunction<BigInteger, BigInteger, BigInteger> sum = (x, y) -> x.add(y);

        BTree<BigInteger> tree = unfoldrBT(g, 38).unfstreamBT();

        System.out.println(fstreamBT(tree).foldBTTailRec(sum, Function.identity()));
    }
}
