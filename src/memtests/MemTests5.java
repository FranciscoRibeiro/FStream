package memtests;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MemTests5 {
    public static void main(String[] args) throws InterruptedException {
        //build list
        final int SIZE = 20000000;
        ArrayList<Integer> li = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            li.add(i);
        }

        Function<Integer, String> f = x -> String.valueOf(x); //function to convert an Integer to a String
        Function<String, Long> g = x -> Long.parseLong(x); //function to convert a String to a Long
        Predicate<String> p = x -> x.length() > 5;

        long start = System.currentTimeMillis();
        Stream<Long> sl = li.stream().map(f).filter(p).map(g);
        System.out.println("Unstreaming!");
        Thread.sleep(10000); // Small delay to allow the profiler to attach
        List<Long> ll = sl.collect(Collectors.toList());

        System.out.println("Time to run: " + (float) (System.currentTimeMillis() - start)/1000);
        //print lists to avoid garbage collection
        System.out.println(li);
        System.out.println(ll);
    }
}
