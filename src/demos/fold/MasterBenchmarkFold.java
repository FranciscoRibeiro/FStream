package demos.fold;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public abstract class MasterBenchmarkFold {
    public int POPSIZE;
    private final int MEASUREMENTS = 10;
    private long[] times = new long[MEASUREMENTS];

    public List<Integer> lInts = new ArrayList<>();

    public void end(){
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

            try {
                System.gc();
                Thread.sleep(1000);
                System.gc();
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

    public void warmUp(){
        System.out.println(lInts.size()); //Print collection's size

        Random r = new Random(5);
        System.out.println(lInts.get(r.nextInt(POPSIZE))); //Print random element

        //Calculate sum of even numbers through the use of an iterator for list lInts
        long totalSum = 0;
        Iterator<Integer> it = lInts.iterator();
        while(it.hasNext()){
            Integer i = it.next();
            if(i % 2 == 0){
                totalSum += i;
            }
        }
        System.out.println("Even number sum for lInts: " + totalSum);
    }

    public void populate(){
        Random r = new Random(3);

        for(int i = 0; i < POPSIZE; i++){
            lInts.add(r.nextInt(500));
            //lInts.add(i);
        }
    }
}
