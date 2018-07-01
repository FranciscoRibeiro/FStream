package optimizations.optimizations_unfoldrbt_foldlbt;

import datatypes.Step;
import util.Either;
import util.Left;
import util.Pair;
import util.Right;

import java.util.Optional;
import java.util.Stack;
import java.util.function.BiFunction;
import java.util.function.Function;

public class TrivialRewriteInFoldlBT {
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

        Function<Integer, Integer> l = Function.identity();
        Integer res1 = null;
        boolean over = false;
        Stack<Object> states = new Stack<>();
        states.push(20);
        final Optional<Integer>[] opAux = new Optional[]{Optional.empty()};

        while(!over){
            if(states.empty()){
                res1 = opAux[0].get();
                over = true;
            }

            else{
                Step step = ((Function<Object, Step>) x -> {
                    Either<Integer, Pair<Integer, Integer>> aux = g.apply((Integer) x);

                    if (aux instanceof Left) {
                        if(!opAux[0].isPresent()){
                            opAux[0] = Optional.of(l.apply((Integer) ((Left) aux).fromLeft()));
                        }
                        else{
                            opAux[0] = Optional.of(sum.apply(opAux[0].get(), l.apply((Integer) ((Left) aux).fromLeft())));
                        }
                    } else if (aux instanceof Right) {
                        Pair p = (Pair) ((Right) aux).fromRight();

                        states.push(p.getY());
                        states.push(p.getX());
                    }

                    return null;
                }).apply(states.pop());
            }
        }

        Integer res = res1;
        System.out.println(res);
    }
}
