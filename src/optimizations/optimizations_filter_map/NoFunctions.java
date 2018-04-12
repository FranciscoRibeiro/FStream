package optimizations.optimizations_filter_map;

import datatypes.Step;
import experimental.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class NoFunctions extends MasterBenchmarkFilterMap{
    public static void main(String[] args) {
        NoFunctions nf = new NoFunctions();

        nf.populate();

        nf.warmUp();

        nf.measure();

        nf.end();
    }

    @Override
    public ArrayList<Student> work() {
        Predicate<Student> p = s -> s.grade >= 95;
        Function<Student, Student> f = s -> new Student(s.name, Math.round((double) s.grade / 10));


        ArrayList<Student> res1 = new ArrayList<>();
        List auxState = l;
        boolean over = false;

        while (!over) {
            if (auxState.isEmpty()) {
                over = true;
            } else {
                List<Student> sub = auxState.subList(1, auxState.size());

                if (((Predicate) p).test(auxState.get(0))) {
                    res1.add(f.apply((Student) auxState.get(0)));
                    auxState = sub;
                } else {
                    auxState = sub;
                }
            }
        }

        ArrayList<Student> res = (ArrayList<Student>) res1;


        System.out.println(res.size());
        return res;
    }
}
