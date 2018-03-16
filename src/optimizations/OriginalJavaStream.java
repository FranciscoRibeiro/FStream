package optimizations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.stream.Stream;

/*
 * The reduce version with 2 args takes a BinaryOperator instead of BiFunction
 * But, BinaryOperator<T> == BiFunction<T,T,T>, so (a, b) -> a+b is placed directly
 * inside the second parameter of reduce.
 *     */
public class OriginalJavaStream {
    public static void main(String[] args) {
        ArrayList<Integer> xs = new ArrayList<>(Arrays.asList(new Integer[]{1, 2, 3, 4, 5}));
        ArrayList<Integer> ys = new ArrayList<>(Arrays.asList(new Integer[]{6, 7, 8, 9, 10}));
        //BiFunction<Integer, Integer, Integer> f = (a, b) -> a+b;

        Stream<Integer> xsS = xs.stream();
        Stream<Integer> ysS = ys.stream();
        Integer res = Stream.concat(xsS, ysS).reduce(0, (a, b) -> a+b);
        System.out.println(res);

    }
}
