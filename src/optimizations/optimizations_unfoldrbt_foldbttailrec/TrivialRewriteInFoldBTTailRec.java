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

public class TrivialRewriteInFoldBTTailRec {
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
        final Continuation[] cont = {new ContinuationId()};
        final boolean[] over = {false};
        Continuation.globalState = 23;

        while(!over[0]){
            Step step = ((Function<Object, Step>) x -> {
                Either<Integer, Pair<Integer, Integer>> aux = g.apply((Integer) x);

                if (aux instanceof Left) {
                    cont[0] = cont[0].execute(Function.<Integer>identity().apply((Integer) ((Left) aux).fromLeft()));

                    if(cont[0] == null){
                        over[0] = true;
                    }
                } else if (aux instanceof Right) {
                    Pair p = (Pair) ((Right) aux).fromRight();

                    Continuation.globalState = p.getX();

                    Continuation<Integer, ContinuationBranchOp<Integer>> nextCont = new ContinuationBranchOp<>(cont[0]);
                    cont[0] = new ContinuationFold(p.getY(), nextCont);
                }

                return null;
            }).apply(Continuation.globalState);
        }

        Integer res = (Integer) Continuation.res;

        System.out.println(res);
    }
}
