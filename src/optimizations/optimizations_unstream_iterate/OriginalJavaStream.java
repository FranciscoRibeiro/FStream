package optimizations.optimizations_unstream_iterate;

import util.Pair;

import java.lang.invoke.MethodHandles;
import java.math.BigInteger;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class OriginalJavaStream extends MasterBenchmarkUnstreamIterate{
    public static void main(String[] args) {
        System.out.println(MethodHandles.lookup().lookupClass().getSimpleName() + "...");

        OriginalJavaStream ojs = new OriginalJavaStream();

        /*ojs.populate();

        ojs.warmUp();*/

        ojs.measure();

        ojs.end();
    }

    @Override
    public void work() {
        UnaryOperator<List<BigInteger>> u1 =
                row -> {
                    List<BigInteger> zeroBegin = Stream.concat(Stream.of(BigInteger.ZERO), row.stream()).collect(Collectors.toList());
                    List<BigInteger> zeroEnd = Stream.concat(row.stream(), Stream.of(BigInteger.ZERO)).collect(Collectors.toList());

                    return IntStream
                            .range(0, Math.min(zeroBegin.size(), zeroEnd.size()))
                            .mapToObj(index -> new Pair<>(zeroBegin.get(index), zeroEnd.get(index)))
                            .map(p -> p.getX().add(p.getY()))
                            .collect(Collectors.toList());
                };

        res1 = Stream.iterate(l, u1).limit(NLINES).collect(Collectors.toList());
    }
}
