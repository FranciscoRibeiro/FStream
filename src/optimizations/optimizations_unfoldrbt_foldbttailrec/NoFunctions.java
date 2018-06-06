package optimizations.optimizations_unfoldrbt_foldbttailrec;

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

public class NoFunctions {
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
            Either<Integer, Pair<Integer, Integer>> aux = g.apply((Integer) Continuation.globalState);

            if (aux instanceof Left) {
                cont = cont.execute(Function.<Integer>identity().apply((Integer) ((Left) aux).fromLeft()));

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

        Integer res = (Integer) Continuation.res;

        System.out.println(res);
    }
}
