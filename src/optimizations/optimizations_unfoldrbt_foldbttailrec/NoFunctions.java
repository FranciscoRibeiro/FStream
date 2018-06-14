
package optimizations.optimizations_unfoldrbt_foldbttailrec;

import demos.fibonacci.MasterBenchmarkFibonacci;
import experimental.Continuation;
import experimental.ContinuationBranchOp;
import experimental.ContinuationFold;
import experimental.ContinuationId;
import util.Either;
import util.Left;
import util.Pair;
import util.Right;

import java.lang.invoke.MethodHandles;
import java.math.BigInteger;
import java.util.function.BiFunction;
import java.util.function.Function;

public class NoFunctions extends MasterBenchmarkFibonacci {
    public static void main(String[] args) {
        System.out.println(MethodHandles.lookup().lookupClass().getSimpleName() + "...");

        NoFunctions nf = new NoFunctions();

        /*nf.populate();

        nf.warmUp();*/

        nf.measure();

        nf.end();
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

        Continuation.b = sum;
        Continuation cont = new ContinuationId();
        boolean over = false;
        Continuation.globalState = 38;

        while(!over){
            Either<BigInteger, Pair<Integer, Integer>> aux = g.apply((Integer) Continuation.globalState);

            if (aux instanceof Left) {
                cont = cont.execute(Function.<BigInteger>identity().apply((BigInteger) ((Left) aux).fromLeft()));

                if(cont == null){
                    over = true;
                }
            } else if (aux instanceof Right) {
                Pair p = (Pair) ((Right) aux).fromRight();

                Continuation.globalState = p.getX();

                Continuation<Integer, ContinuationBranchOp<Integer>> nextCont = new ContinuationBranchOp<>(cont);
                cont = new ContinuationFold(p.getY(), nextCont);
            }
        }

        BigInteger res = (BigInteger) Continuation.res;

        System.out.println(res);
    }
}
