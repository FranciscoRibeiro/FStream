package experimental;

import datatypes.FStream;
import util.Either;
import util.Left;
import util.Pair;
import util.Right;

import java.util.function.Function;

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

        System.out.println(unfoldrBT(g, 3).unfstreamBT());
    }
}
