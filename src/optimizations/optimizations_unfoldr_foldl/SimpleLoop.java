package optimizations.optimizations_unfoldr_foldl;

import java.util.function.BiFunction;

public class SimpleLoop {
    public static void main(String[] args) {
        Integer value = 1;
        for(Integer x = 5; x > 0; x = x-1){
            value = ((BiFunction<Integer, Integer, Integer>) (a, b) -> a * b).apply(value, x);
        }
        Integer r = value;
        System.out.println(r);
    }
}
