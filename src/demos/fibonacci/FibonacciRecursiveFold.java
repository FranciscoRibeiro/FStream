package demos.fibonacci;

import util.Either;
import util.Left;
import util.Pair;
import util.Right;

import java.lang.invoke.MethodHandles;
import java.math.BigInteger;
import java.util.function.BiFunction;
import java.util.function.Function;

import static datatypes.FStream.unfoldrBT;

public class FibonacciRecursiveFold extends MasterBenchmarkFibonacci{
    public static void main(String[] args) {
        System.out.println(MethodHandles.lookup().lookupClass().getSimpleName() + "...");

        FibonacciRecursiveFold fibRF = new FibonacciRecursiveFold();

        /*fibRF.populate();

        fibRF.warmUp();*/

        fibRF.measure();

        fibRF.end();
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

        System.out.println(unfoldrBT(g, 38).foldBT(sum, Function.identity()));
    }
}
