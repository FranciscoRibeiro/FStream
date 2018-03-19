package optimizations;

import datatypes.FStream;

import java.util.function.BiFunction;

public class OriginalFStream extends MasterBenchmark{
    public static void main(String[] args) {
        System.out.println("OriginalFStream...");
        /*ArrayList<Integer> xs = new ArrayList<>(Arrays.asList(new Integer[]{1, 2, 3, 4, 5}));
        ArrayList<Integer> ys = new ArrayList<>(Arrays.asList(new Integer[]{6, 7, 8, 9, 10}));*/

        OriginalFStream ofs = new OriginalFStream();

        ofs.populate();

        ofs.warmUp();

        ofs.measure();

        ofs.end();
    }

    @Override
    public void work() {
        BiFunction<Integer, Integer, Integer> f = (a, b) -> a+b;

        FStream<Integer> xsFs = FStream.fstream(xs);
        FStream<Integer> ysFs = FStream.fstream(ys);
        Integer res = xsFs.appendfs(ysFs).foldl(f, 0);


        System.out.println(res);
    }
}
