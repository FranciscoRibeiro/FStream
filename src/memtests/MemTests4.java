package memtests;

import datatypes.FStream;

import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.Predicate;

public class MemTests4 {
    public static void main(String[] args) {
        //build list
        long start = System.currentTimeMillis();
        final int SIZE = 20000000;
        ArrayList<Integer> li = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            li.add(i);
        }

        Function<Integer, String> f = x -> String.valueOf(x); //function to convert an Integer to a String
        Function<String, Long> g = x -> Long.parseLong(x); //function to convert a String to a Long
        Predicate<String> p = x -> x.length() > 5;

        FStream<Long> sl = FStream.fstream(li).mapfs(f).filterfs(p).mapfs(g); //convert list to stream and build functions
        System.out.println("Unstreaming!");
        ArrayList<Long> ll = sl.unfstream(); //Note that functions are only applied when unfstream is called!

        //print lists to avoid garbage collection
        System.out.println(li);
        System.out.println(ll);
        System.out.println("Time to run: " + (float) (System.currentTimeMillis() - start)/1000);
    }
}
