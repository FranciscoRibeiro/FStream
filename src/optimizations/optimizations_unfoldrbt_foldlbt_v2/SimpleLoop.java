package optimizations.optimizations_unfoldrbt_foldlbt_v2;

import util.Either;
import util.Left;
import util.Pair;
import util.Right;

import java.util.Optional;
import java.util.Stack;
import java.util.function.BiFunction;
import java.util.function.Function;

public class SimpleLoop {
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
        //states.push(20);
        Optional<Integer> opAux = Optional.empty();

        Either<Integer, Pair<Integer, Integer>> aux;

        /*for(states.push(20); !states.empty(); aux = g.apply((Integer) states.pop())){
            if (aux instanceof Left) {
                if(!opAux.isPresent()){
                    opAux = Optional.of(l.apply((Integer) ((Left) aux).fromLeft()));
                }
                else{
                    opAux = Optional.of(sum.apply(opAux.get(), l.apply((Integer) ((Left) aux).fromLeft())));
                }
            } else if (aux instanceof Right) {
                Pair p = (Pair) ((Right) aux).fromRight();

                states.push(p.getY());
                states.push(p.getX());
            }
        }*/

        res1 = opAux.get();

        /*while(!over){
            if(states.empty()){
                res1 = opAux.get();
                over = true;
            }

            else{
                Either<Integer, Pair<Integer, Integer>> aux = g.apply((Integer) states.pop());

                if (aux instanceof Left) {
                    if(!opAux.isPresent()){
                        opAux = Optional.of(l.apply((Integer) ((Left) aux).fromLeft()));
                    }
                    else{
                        opAux = Optional.of(sum.apply(opAux.get(), l.apply((Integer) ((Left) aux).fromLeft())));
                    }
                } else if (aux instanceof Right) {
                    Pair p = (Pair) ((Right) aux).fromRight();

                    states.push(p.getY());
                    states.push(p.getX());
                }
            }
        }*/

        Integer res = res1;
        System.out.println(res);
    }
}
