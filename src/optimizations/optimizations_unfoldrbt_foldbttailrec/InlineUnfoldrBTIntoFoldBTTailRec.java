package optimizations.optimizations_unfoldrbt_foldbttailrec;

import datatypes.BranchBT;
import datatypes.LeafBT;
import datatypes.Step;
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

public class InlineUnfoldrBTIntoFoldBTTailRec extends MasterBenchmarkFibonacci {
    public static void main(String[] args) {
        System.out.println(MethodHandles.lookup().lookupClass().getSimpleName() + "...");

        InlineUnfoldrBTIntoFoldBTTailRec iuf = new InlineUnfoldrBTIntoFoldBTTailRec();

        /*iuf.populate();

        iuf.warmUp();*/

        iuf.measure();

        iuf.end();
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
            Step step = ((Function<Object, Step>) x -> {
                Either<BigInteger, Pair<Integer, Integer>> aux = g.apply((Integer) x);

                if (aux instanceof Left) {
                    return new LeafBT<>(((Left) aux).fromLeft());
                } else if (aux instanceof Right) {
                    Pair p = (Pair) ((Right) aux).fromRight();
                    return new BranchBT(p.getX(), p.getY());
                }

                return null;
            }).apply(Continuation.globalState);

            if(step instanceof LeafBT){
                cont = cont.execute(Function.<BigInteger>identity().apply((BigInteger) step.elem));

                if(cont == null){
                    over = true;
                }
            }
            else if(step instanceof BranchBT){
                Continuation.globalState = ((BranchBT) step).state1;

                Continuation<Integer, ContinuationBranchOp<Integer>> nextCont = new ContinuationBranchOp<>(cont);
                cont = new ContinuationFold(((BranchBT) step).state2, nextCont);
            }
        }

        BigInteger res = (BigInteger) Continuation.res;

        System.out.println(res);
    }
}
