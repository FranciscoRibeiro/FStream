package optimizations.optimizations_unstream_intersect;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static datatypes.FStream.fstream;

public class OriginalFStream {
    public static void main(String[] args) {
        ArrayList<String> names1 = new ArrayList<>();
        ArrayList<String> names2 = new ArrayList<>();

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("/home/kiko/Engenharia Informática/Thesis/JavaHaskellStreams/random_names_list_1.txt"));
            for(String line; (line = br.readLine()) != null; ) {
                names1.add(line);
            }

            br = new BufferedReader(new FileReader("/home/kiko/Engenharia Informática/Thesis/JavaHaskellStreams/random_names_list_2.txt"));
            for(String line; (line = br.readLine()) != null; ) {
                names2.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        long start = System.currentTimeMillis();
        List<String> res = fstream(names1).intersect(fstream(names2)).unfstream();
        System.out.println("Time taken: " + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        names1.retainAll(names2);
        System.out.println("Time taken: " + (System.currentTimeMillis() - start));

        System.out.println(names1.size());
        System.out.println(names2.size());
        System.out.println(res.size());
        FileWriter fw = null;
        try {
            fw = new FileWriter("ola.txt");

            for(String s: res){
                fw.write(s + "\n");
            }

            fw.close();

            fw = new FileWriter("ola2.txt");

            for(String s: names1){
                fw.write(s + "\n");
            }

            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
