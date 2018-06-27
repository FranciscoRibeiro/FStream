package experimental;

import datatypes.FStream;
import util.Pair;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static datatypes.FStream.fstream;
import static datatypes.FStream.iterate;

public class PascalTriangleTest {
    public static void print(List<List<Integer>> l, String fileName){
        FileWriter fw = null;
        try {
            fw = new FileWriter(fileName);

            for(List<Integer> li: l){
                for(Integer i: li){
                    fw.write(i + "| ");
                }
                fw.write("\n");
            }

            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        /*FStream<Integer> fs = fstream(Arrays.asList(1));

        Function<FStream<Integer>,FStream<Integer>> f =
                row -> fstream(Arrays.asList(0)).appendfs(row).zipfs(row.appendfs(fstream(Arrays.asList(0)))).mapfs(p -> p.getX() + p.getY());

        long start = System.currentTimeMillis();
        List<List<Integer>> res = iterate(f, fs).take(20).mapfs(FStream::unfstream).unfstream();
        System.out.println(System.currentTimeMillis() - start);

        System.out.println(res);*/



        List<Integer> l = Arrays.asList(1);

        Function<List<Integer>,List<Integer>> f1 =
                row -> fstream(Arrays.asList(0)).appendfs(fstream(row)).zipfs(fstream(row).appendfs(fstream(Arrays.asList(0)))).mapfs(p -> p.getX() + p.getY()).unfstream();

        long start = System.currentTimeMillis();
        List<List<Integer>> res1 = iterate(f1, l).take(2000).unfstream();
        long end = System.currentTimeMillis() - start;

        print(res1, "res1.txt");



        UnaryOperator<List<Integer>> u1 =
                row -> {
                    List<Integer> zeroBegin = Stream.concat(Stream.of(0), row.stream()).collect(Collectors.toList());
                    List<Integer> zeroEnd = Stream.concat(row.stream(), Stream.of(0)).collect(Collectors.toList());

                    return IntStream
                            .range(0, Math.min(zeroBegin.size(), zeroEnd.size()))
                            .mapToObj(index -> new Pair<>(zeroBegin.get(index), zeroEnd.get(index)))
                            .map(p -> p.getX() + p.getY())
                            .collect(Collectors.toList());
                };

        start = System.currentTimeMillis();
        List<List<Integer>> res2 = Stream.iterate(l, u1).limit(2000).collect(Collectors.toList());
        long end1 = System.currentTimeMillis() - start;

        print(res2, "res2.txt");
        System.out.println(end);
        System.out.println(end1);
    }
}
