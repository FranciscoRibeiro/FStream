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

public class CaseOfCaseInFoldBTTailRec extends MasterBenchmarkFibonacci {
    public static void main(String[] args) {
        System.out.println(MethodHandles.lookup().lookupClass().getSimpleName() + "...");

        CaseOfCaseInFoldBTTailRec ccf = new CaseOfCaseInFoldBTTailRec();

        /*ccf.populate();

        ccf.warmUp();*/

        ccf.measure();

        ccf.end();
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
        final Continuation[] cont = {new ContinuationId()};
        final boolean[] over = {false};
        Continuation.globalState = 38;

        while(!over[0]){
            Step step = ((Function<Object, Step>) x -> {
                Either<BigInteger, Pair<Integer, Integer>> aux = g.apply((Integer) x);

                if (aux instanceof Left) {
                    Step innerAux = new LeafBT<>(((Left) aux).fromLeft());

                    if(innerAux instanceof LeafBT){
                        cont[0] = cont[0].execute(Function.<BigInteger>identity().apply((BigInteger) innerAux.elem));

                        if(cont[0] == null){
                            over[0] = true;
                        }
                    }
                    else if(innerAux instanceof BranchBT){
                        Continuation.globalState = ((BranchBT) innerAux).state1;

                        Continuation<Integer, ContinuationBranchOp<Integer>> nextCont = new ContinuationBranchOp<>(cont[0]);
                        cont[0] = new ContinuationFold(((BranchBT) innerAux).state2, nextCont);
                    }
                } else if (aux instanceof Right) {
                    Pair p = (Pair) ((Right) aux).fromRight();
                    Step innerAux = new BranchBT(p.getX(), p.getY());

                    if(innerAux instanceof LeafBT){
                        cont[0] = cont[0].execute(Function.<Integer>identity().apply((Integer) innerAux.elem));

                        if(cont[0] == null){
                            over[0] = true;
                        }
                    }
                    else if(innerAux instanceof BranchBT){
                        Continuation.globalState = ((BranchBT) innerAux).state1;

                        Continuation<Integer, ContinuationBranchOp<Integer>> nextCont = new ContinuationBranchOp<>(cont[0]);
                        cont[0] = new ContinuationFold(((BranchBT) innerAux).state2, nextCont);
                    }
                }

                return null;
            }).apply(Continuation.globalState);
        }

        BigInteger res = (BigInteger) Continuation.res;

        System.out.println(res);
    }
}
