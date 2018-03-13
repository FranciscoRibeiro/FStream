package optimizations;

import util.RecursiveLambda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

public class ConstructorSpecialisation {
    private static <S,T> S foldlAppend(BiFunction<S,T,S> f, S value, List<T> xs, List<T> ys){
        RecursiveLambda<BiFunction<S, Object, S>> go1 = new RecursiveLambda<>();
        RecursiveLambda<BiFunction<S, Object, S>> go2 = new RecursiveLambda<>();

        go1.function = (z, x) -> {
            List lAux = (List) x;

            if (lAux.isEmpty()) {
                return go2.function.apply(z, ys);
            } else {
                List<T> sub = lAux.subList(1, lAux.size());
                return go1.function.apply(f.apply(z, (T) lAux.get(0)), sub);
            }
        };

        go2.function = (z, x) -> {
            List lAux = (List) x;

            if(lAux.isEmpty()){
                return z;
            }
            else{
                List<T> sub = lAux.subList(1, lAux.size());
                return go2.function.apply(f.apply(z, (T) lAux.get(0)), sub);
            }
        };

        return go1.function.apply(value, xs);
    }

    public static void main(String[] args) {
        System.out.println("GHC optimizations...");
        ArrayList<Integer> xs = new ArrayList<>(Arrays.asList(new Integer[]{1, 2, 3, 4, 5}));
        ArrayList<Integer> ys = new ArrayList<>(Arrays.asList(new Integer[]{6, 7, 8, 9, 10}));
        BiFunction<Integer, Integer, Integer> f = (a, b) -> a+b;
        System.out.println(foldlAppend(f, 0, xs, ys));
    }
}
