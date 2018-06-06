package optimizations.optimizations_unfoldrbt_foldbttailrec;

import datatypes.BranchBT;
import datatypes.LeafBT;
import datatypes.Step;
import experimental.Continuation;
import experimental.ContinuationBranchOp;
import experimental.ContinuationFold;
import experimental.ContinuationId;
import util.Either;
import util.Left;
import util.Pair;
import util.Right;

import java.util.function.BiFunction;
import java.util.function.Function;

public class InlineUnfoldrBTIntoFoldBTTailRec {
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

        Continuation.b = sum;
        Continuation cont = new ContinuationId();
        boolean over = false;
        Continuation.globalState = 23;

        while(!over){
            Step step = ((Function<Object, Step>) x -> {
                Either<Integer, Pair<Integer, Integer>> aux = g.apply((Integer) x);

                if (aux instanceof Left) {
                    return new LeafBT<>(((Left) aux).fromLeft());
                } else if (aux instanceof Right) {
                    Pair p = (Pair) ((Right) aux).fromRight();
                    return new BranchBT(p.getX(), p.getY());
                }

                return null;
            }).apply(Continuation.globalState);

            if(step instanceof LeafBT){
                cont = cont.execute(Function.<Integer>identity().apply((Integer) step.elem));

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

        Integer res = (Integer) Continuation.res;

        System.out.println(res);
    }
}
