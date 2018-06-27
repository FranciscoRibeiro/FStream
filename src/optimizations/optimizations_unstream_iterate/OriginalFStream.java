package optimizations.optimizations_unstream_iterate;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static datatypes.FStream.fstream;
import static datatypes.FStream.iterate;

public class OriginalFStream {
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
        List<Integer> l = Arrays.asList(1);

        Function<List<Integer>,List<Integer>> f1 =
                row -> fstream(Arrays.asList(0)).appendfs(fstream(row)).zipfs(fstream(row).appendfs(fstream(Arrays.asList(0)))).mapfs(p -> p.getX() + p.getY()).unfstream();

        long start = System.currentTimeMillis();
        List<List<Integer>> res1 = iterate(f1, l).take(2000).unfstream();
        System.out.println(System.currentTimeMillis() - start);

        print(res1, "res1.txt");
    }
}
