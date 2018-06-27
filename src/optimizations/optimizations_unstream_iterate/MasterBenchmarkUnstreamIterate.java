package optimizations.optimizations_unstream_iterate;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

public abstract class MasterBenchmarkUnstreamIterate {
    public final int NLINES = 3000;
    private final int MEASUREMENTS = 10;
    private long[] times = new long[MEASUREMENTS];

    public List<BigInteger> l = Arrays.asList(BigInteger.ONE);
    public List<List<BigInteger>> res1;

    public static void print(List<List<BigInteger>> l, String fileName){
        FileWriter fw = null;
        try {
            fw = new FileWriter(fileName);

            for(List<BigInteger> li: l){
                for(BigInteger i: li){
                    fw.write(i + "| ");
                }
                fw.write("\n");
            }

            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void end(){
        l = null;
        res1 = null;

        try {
            System.gc();
            Thread.sleep(200);
            System.runFinalization();
            Thread.sleep(200);
            System.gc();
            Thread.sleep(200);
            System.runFinalization();
            Thread.sleep(1000);
            System.gc();
            Thread.sleep(200);
            System.runFinalization();
            Thread.sleep(200);
            System.gc();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void measure(){
        for(int i = 0; i < MEASUREMENTS; i++){
            long start = System.currentTimeMillis();

            this.work();

            times[i] = System.currentTimeMillis() - start;
            System.out.println("Iteration " + i + ": " + times[i]);
            print(res1, "res1.txt");
            res1 = null;

            try {
                System.gc();
                Thread.sleep(1000);
                System.runFinalization();
                Thread.sleep(1000);
                System.gc();
                Thread.sleep(1000);
                System.runFinalization();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long sumTimes = 0;
        for(int i = 0; i < times.length; i++){
            sumTimes += times[i];
        }

        System.out.println("Time to complete: " + (sumTimes/(float) times.length)/1000);
    }

    public abstract void work();

    /*public void warmUp(){
        System.out.println(xs.size()); //Print collection's size
        System.out.println(ys.size()); //Print collection's size

        Random r = new Random(5);
        System.out.println(xs.get(r.nextInt(POPSIZE/2))); //Print random element
        System.out.println(ys.get(r.nextInt(POPSIZE/2))); //Print random element

        //Calculate sum of even numbers through the use of an iterator for list xs
        long totalSum = 0;
        Iterator<Integer> it = xs.iterator();
        while(it.hasNext()){
            Integer i = it.next();
            if(i % 2 == 0){
                totalSum += i;
            }
        }
        System.out.println("Even number sum for xs: " + totalSum);

        //Calculate sum of odd numbers through the use of an iterator for list ys
        totalSum = 0;
        it = ys.iterator();
        while(it.hasNext()){
            Integer i = it.next();
            if(i % 2 != 0){
                totalSum += i;
            }
        }
        System.out.println("Odd number sum for ys: " + totalSum);
    }*/

    /*public void populate(){
        for(int n = 1; n <= POPSIZE/2; n++){
            xs.add(n);
            ys.add(n + POPSIZE/2);
        }
    }*/
}
