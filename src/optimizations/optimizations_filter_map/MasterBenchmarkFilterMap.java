package optimizations.optimizations_filter_map;

import experimental.Student;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public abstract class MasterBenchmarkFilterMap {
    private final int POPSIZE = 20000000;
    private final int MEASUREMENTS = 10;
    private long[] times = new long[MEASUREMENTS];

    public ArrayList<Student> l = new ArrayList<>();

    public void end(){
        l = null;

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

            ArrayList<Student> res = this.work();

            times[i] = System.currentTimeMillis() - start;
            System.out.println("Iteration " + i + ": " + times[i]);

            //Using result list for something
            try {
                FileWriter fw = new FileWriter("printList2.txt");
                for(Student s: res){
                    fw.write(s.toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

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

    public abstract ArrayList<Student> work();

    public void warmUp(){
        System.out.println(l.size()); //Print collection's size

        Random r = new Random(5);
        System.out.println(l.get(r.nextInt(POPSIZE))); //Print random element

        //Calculate sum of student grades through the use of an iterator
        long totalSum = 0;
        Iterator<Student> it = l.iterator();
        while(it.hasNext()){
            Student s = it.next();
            totalSum += s.grade;
        }

        System.out.println("Total grade sum: " + totalSum);
    }

    public void populate(){
        Random r = new Random(3);

        for(int n = 1; n <= POPSIZE; n++){
            Student s = new Student("s_" + n, r.nextInt(201));
            l.add(s);
        }
    }
}
