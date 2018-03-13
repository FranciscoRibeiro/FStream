package optimizations;

/* Example being considered during manual optimizations:
foldl (+) 0 (append (stream xs) (stream ys)) */

import datatypes.Done;
import datatypes.Skip;
import datatypes.Step;
import datatypes.Yield;
import util.Either;
import util.Left;
import util.RecursiveLambda;
import util.Right;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class FirstInline {

    private static <S,T> S foldlAppend(BiFunction<S,T,S> f, S value, List<T> xs, List<T> ys){
        Function<Object, Step> stepperStream = x -> {
            List aux = (List) x;

            if(aux.isEmpty()){
                return new Done();
            }
            else{
                List<T> sub = aux.subList(1, aux.size());
                Yield y = new Yield<T, List<T>>((T) aux.get(0), sub);
                return y;
            }
        };

        Function<Object, Step> stepperAppend = x -> {
            if(x instanceof Left){
                Step aux = stepperStream.apply(((Left) x).fromLeft());

                if(aux instanceof Done){
                    return new Skip<Either>(new Right(ys));
                }
                else if(aux instanceof Skip){
                    return new Skip<Either>(new Left(aux.state));
                }
                else if(aux instanceof Yield){
                    return new Yield<T, Either>((T) aux.elem, new Left(aux.state));
                }
            }
            else if(x instanceof Right){
                Step aux = stepperStream.apply(((Right) x).fromRight());

                if(aux instanceof Done){
                    return new Done();
                }
                else if(aux instanceof Skip){
                    return new Skip<Either>(new Right(aux.state));
                }
                else if(aux instanceof Yield){
                    return new Yield<T, Either>((T) aux.elem, new Right(aux.state));
                }
            }

            return null;
        };

        RecursiveLambda<BiFunction<S, Object, S>> go = new RecursiveLambda<>();

        go.function = (z, x) -> {
            Step aux = stepperAppend.apply(x);

            if(aux instanceof Done){
                return z;
            }
            else if(aux instanceof Skip){
                return go.function.apply(z, aux.state);
            }
            else if(aux instanceof Yield){
                return go.function.apply(f.apply(z, (T) aux.elem), aux.state);
            }

            return null;
        };

        return go.function.apply(value, new Left(xs));
    }
    public static void main(String[] args) {
        System.out.println("GHC optimizations...");
        ArrayList<Integer> xs = new ArrayList<>(Arrays.asList(new Integer[]{1, 2, 3, 4, 5}));
        ArrayList<Integer> ys = new ArrayList<>(Arrays.asList(new Integer[]{6, 7, 8, 9, 10}));
        BiFunction<Integer, Integer, Integer> f = (a, b) -> a+b;
        System.out.println(foldlAppend(f, 0, xs, ys));
    }
}
