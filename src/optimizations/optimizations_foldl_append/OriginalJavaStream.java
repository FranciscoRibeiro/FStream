package optimizations.optimizations_foldl_append;

import java.util.function.BiFunction;
import java.util.stream.Stream;

/*
 * The reduce version with 2 args takes a BinaryOperator instead of BiFunction
 * But, BinaryOperator<T> == BiFunction<T,T,T>, so (a, b) -> a+b is placed directly
 * inside the second parameter of reduce.
 *     */
public class OriginalJavaStream extends MasterBenchmarkFoldlAppend {
    public static void main(String[] args) {
        System.out.println("OriginalJavaStream...");
        /*ArrayList<Integer> xs = new ArrayList<>(Arrays.asList(new Integer[]{1, 2, 3, 4, 5}));
        ArrayList<Integer> ys = new ArrayList<>(Arrays.asList(new Integer[]{6, 7, 8, 9, 10}));*/
        //BiFunction<Integer, Integer, Integer> f = (a, b) -> a+b;

        OriginalJavaStream ojs = new OriginalJavaStream();

        ojs.populate();

        ojs.warmUp();

        ojs.measure();

        ojs.end();
    }

    @Override
    public void work() {
        BiFunction<Long, Integer, Long> f = (a, b) -> a+b;

        Stream<Integer> xsS = xs.stream();
        Stream<Integer> ysS = ys.stream();
        Long res = Stream.concat(xsS, ysS).reduce((long) 0, f, (a,b) -> a+b);

        System.out.println(res);
    }
}
