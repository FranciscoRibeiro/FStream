package optimizations.optimizations_foldl_append;

import datatypes.Step;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

public class SimpleLoop extends MasterBenchmarkFoldlAppend {
    public static void main(String[] args) {
        System.out.println("SimpleLoop...");
        SimpleLoop sl = new SimpleLoop();

        sl.populate();

        sl.warmUp();

        sl.measure();

        sl.end();
    }

    @Override
    public void work() {

        long res = 0L;
        for (Integer x : xs) {
            long l = x;
            res += l;
        }
        long sum = 0L;
        for (Integer i : ys) {
            long l = i;
            sum += l;
        }
        res += sum;

        System.out.println(res);
    }
}
