package optimizations.optimizations_foldl_append;

import java.util.function.BiFunction;

public class SimpleLoopFApply extends MasterBenchmarkFoldlAppend {
    public static void main(String[] args) {
        System.out.println("SimpleLoopFApply...");
        SimpleLoopFApply slfa = new SimpleLoopFApply();

        slfa.populate();

        slfa.warmUp();

        slfa.measure();

        slfa.end();
    }

    @Override
    public void work() {
        BiFunction<Long, Integer, Long> f = (a, b) -> a + b;

        long res = 0L;
        for (Integer i : xs) {
            res = f.apply(res, i);
        }

        for (Integer i : ys) {
            res = f.apply(res, i);
        }

        System.out.println(res);
    }
}
