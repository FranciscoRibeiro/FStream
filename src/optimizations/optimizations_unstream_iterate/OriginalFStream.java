package optimizations.optimizations_unstream_iterate;

import java.lang.invoke.MethodHandles;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static datatypes.FStream.fstream;
import static datatypes.FStream.iterate;

public class OriginalFStream extends MasterBenchmarkUnstreamIterate{
    public static void main(String[] args) {
        System.out.println(MethodHandles.lookup().lookupClass().getSimpleName() + "...");

        OriginalFStream ofs = new OriginalFStream();

        /*ofs.populate();

        ofs.warmUp();*/

        ofs.measure();

        ofs.end();
    }

    @Override
    public void work() {
        Function<List<BigInteger>,List<BigInteger>> f1 =
                row -> fstream(Arrays.asList(BigInteger.ZERO)).appendfs(fstream(row)).zipfs(fstream(row).appendfs(fstream(Arrays.asList(BigInteger.ZERO)))).mapfs(p -> p.getX().add(p.getY())).unfstream();

        res1 = iterate(f1, l).take(NLINES).unfstream();
    }
}
