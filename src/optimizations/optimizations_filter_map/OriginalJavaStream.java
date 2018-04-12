package optimizations.optimizations_filter_map;

import experimental.Student;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class OriginalJavaStream extends MasterBenchmarkFilterMap{
    public static void main(String[] args) {
        OriginalJavaStream ojs = new OriginalJavaStream();

        ojs.populate();

        ojs.warmUp();

        ojs.measure();

        ojs.end();
    }

    @Override
    public ArrayList<Student> work() {
        Predicate<Student> p = s -> s.grade >= 95;
        Function<Student, Student> f = s -> new Student(s.name, Math.round((double) s.grade / 10));


        ArrayList<Student> res = l.stream()
                .filter(p)
                .map(f)
                .collect(Collectors.toCollection(ArrayList::new));


        System.out.println(res.size());
        return res;
    }
}
