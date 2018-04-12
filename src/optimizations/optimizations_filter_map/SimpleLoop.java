package optimizations.optimizations_filter_map;

import experimental.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class SimpleLoop extends MasterBenchmarkFilterMap{
    public static void main(String[] args) {
        SimpleLoop sl = new SimpleLoop();

        sl.populate();

        sl.warmUp();

        sl.measure();

        sl.end();
    }

    @Override
    public ArrayList<Student> work() {
        Predicate<Student> p = s -> s.grade >= 95;
        Function<Student, Student> f = s -> new Student(s.name, Math.round((double) s.grade / 10));


        ArrayList<Student> res1 = new ArrayList<>();

        for(Student s: l){
            if (((Predicate) p).test(s)){
                res1.add(f.apply(s));
            }
        }

        ArrayList<Student> res = (ArrayList<Student>) res1;


        System.out.println(res.size());
        return res;
    }
}
