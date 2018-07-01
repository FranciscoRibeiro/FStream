package experimental;

import util.Either;
import util.Left;
import util.Pair;
import util.Right;

import java.util.function.BiFunction;
import java.util.function.Function;

import static datatypes.FStream.unfoldrBT;

public class FibTest {
    public static void main(String[] args) {
        Function<Integer, Either<Integer, Pair<Integer,Integer>>> g = x -> {
            if (x == 0){
                return new Left<>(0);
            }
            else if(x == 1){
                return new Left<>(1);
            }
            else{
                return new Right<>(new Pair<>(x-1, x-2));
            }
        };

        BiFunction<Integer, Integer, Integer> sum = (x,y) -> x + y;

        final int NUMBER = 45;

        long start = System.currentTimeMillis();
        //System.out.println(unfoldrBT(g, NUMBER).foldBTTailRec(sum, Function.identity()));
        System.out.println(unfoldrBT(g, NUMBER).foldlBT(sum, Function.identity()));
        //System.out.println(unfoldrBT(g, NUMBER).foldBT(sum, Function.identity()));
        System.out.println(System.currentTimeMillis() - start);
    }
}
