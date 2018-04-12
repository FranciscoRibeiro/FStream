package optimizations.optimizations_foldl_append;

import java.util.stream.Stream;

public class OriginalJavaStreamSum extends MasterBenchmarkFoldlAppend{
    public static void main(String[] args) {
        OriginalJavaStreamSum ojss = new OriginalJavaStreamSum();

        ojss.populate();

        ojss.warmUp();

        ojss.measure();

        ojss.end();
    }

    @Override
    public void work() {
        Stream<Integer> xsS = xs.stream();
        Stream<Integer> ysS = ys.stream();

        long res = Stream.concat(xsS, ysS).mapToLong(i -> i).sum();

        System.out.println(res);
    }
}
