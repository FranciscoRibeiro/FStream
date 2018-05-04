package optimizations.optimizations_unfoldr_foldl;

import java.util.function.BiFunction;

public class SimpleLoop {
    public static void main(String[] args) {
        Integer v = 1;
        for(Integer x = 5; x > 0; x = x-1){
            v = ((BiFunction<Integer, Integer, Integer>) (a, b) -> a * b).apply(v, (Integer) x);
        }
        Integer r = v;
        System.out.println(r);
    }
}
