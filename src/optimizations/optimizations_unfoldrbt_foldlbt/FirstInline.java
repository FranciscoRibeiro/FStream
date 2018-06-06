package optimizations.optimizations_unfoldrbt_foldlbt;

import datatypes.BranchBT;
import datatypes.LeafBT;
import datatypes.Step;
import util.Either;
import util.Left;
import util.Pair;
import util.Right;

import java.util.Optional;
import java.util.Stack;
import java.util.function.BiFunction;
import java.util.function.Function;

public class FirstInline {
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

        Function<Object, Step> nextUnfoldrBT = x -> {
            Either<Integer, Pair<Integer,Integer>> aux = g.apply((Integer) x);

            if(aux instanceof Left){
                return new LeafBT<>(((Left) aux).fromLeft());
            }
            else if(aux instanceof Right){
                Pair p = (Pair) ((Right) aux).fromRight();
                return new BranchBT(p.getX(), p.getY());
            }

            return null;
        };

        Function<Integer, Integer> l = Function.identity();
        Integer res1 = null;
        boolean over = false;
        Stack<Step> tree = new Stack<>();
        tree.push(nextUnfoldrBT.apply(15));
        Optional<Integer> opAux = Optional.empty();
        Step poppedStep;

        while(!over){
            if(tree.empty()){
                res1 = opAux.get();
                over = true;
            }
            else if(tree.peek() instanceof LeafBT){
                if(!opAux.isPresent()){
                    poppedStep = tree.pop();
                    opAux = Optional.of(l.apply((Integer) poppedStep.elem));
                }
                else{
                    poppedStep = tree.pop();
                    opAux = Optional.of(sum.apply(opAux.get(), l.apply((Integer) poppedStep.elem)));
                }
            }
            else if(tree.peek() instanceof BranchBT){
                poppedStep = tree.pop();
                tree.push(nextUnfoldrBT.apply(((BranchBT) poppedStep).state2));
                tree.push(nextUnfoldrBT.apply(((BranchBT) poppedStep).state1));
            }
        }

        Integer res = res1;
        System.out.println(res);
    }
}
