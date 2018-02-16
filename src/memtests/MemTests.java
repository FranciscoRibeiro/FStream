package memtests;

import datatypes.FStream;

import java.util.ArrayList;
import java.util.function.Function;

public class MemTests {
    public static void main(String[] args) {
        final int SIZE = 10000;
        ArrayList<Integer> l_first = new ArrayList<>();
        for(int i = 0; i < SIZE; i++){
            l_first.add(i);
        }

        ArrayList<Integer> l_last = new ArrayList<>();
        for(int i = SIZE; i < SIZE * 2; i++){
            l_last.add(i);
        }

        //Function<Integer, Integer> inc = x -> x + 1;
        Function<Integer, Long> intToLong = x -> new Long(x);
        Function<Long, Double> longToDouble = x -> new Double(x);

        FStream<Integer> fs_first = FStream.fstream(l_first);
        FStream<Integer> fs_last = FStream.fstream(l_last);
        System.out.println("Mapping merged...");
        FStream<Double> fs2 = fs_last.mapfs(intToLong.andThen(longToDouble));
        ArrayList<Double> l2 = fs2.unfstream();

        System.out.println("Mapping chained...");
        FStream<Double> fs1 = fs_first.mapfs(intToLong)
                .mapfs(longToDouble);
        ArrayList<Double> l1 = fs1.unfstream();

        System.out.println(l1);
        System.out.println(l2);


        System.out.println("Mapping chained...");
        ArrayList<Double> l3 = fs1.unfstream();
        System.gc();
        System.out.println("Mapping merged...");
        ArrayList<Double> l5 = fs2.unfstream();
        System.gc();
        System.out.println("Mapping chained...");
        ArrayList<Double> l6 = fs1.unfstream();
        System.gc();
        System.out.println("Mapping merged...");
        ArrayList<Double> l4 = fs2.unfstream();
        System.gc();


        System.out.println(l3);
        System.out.println(l4);
        System.out.println(l5);
        System.out.println(l6);
    }
}
