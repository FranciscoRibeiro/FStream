package experimental;

import datatypes.FStream;
import util.*;

import java.util.function.BiFunction;
import java.util.function.Function;

import static datatypes.FStream.fstreamBT;
import static datatypes.FStream.unfoldrBT;

public class BTreeUnfstreamTest {
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

        BiFunction<Integer, Integer, Integer> sum = (x, y) -> x + y;

        BTree<Integer> tree = unfoldrBT(g, 15).unfstreamBT();
        System.out.println(fstreamBT(tree).foldBT(sum, Function.identity()));

    }
}
