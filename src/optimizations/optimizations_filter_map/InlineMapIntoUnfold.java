package optimizations.optimizations_filter_map;

import datatypes.Done;
import datatypes.Skip;
import datatypes.Step;
import datatypes.Yield;
import experimental.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class InlineMapIntoUnfold extends MasterBenchmarkFilterMap{
    public static void main(String[] args) {
        InlineMapIntoUnfold imu = new InlineMapIntoUnfold();

        imu.populate();

        imu.warmUp();

        imu.measure();

        imu.end();
    }

    @Override
    public ArrayList<Student> work() {
        Predicate<Student> p = s -> s.grade >= 95;
        Function<Student, Student> f = s -> new Student(s.name, Math.round((double) s.grade / 10));


        ArrayList<Student> res1 = new ArrayList<>();
        Object auxState = l;
        boolean over = false;

        while (!over) {
            Step step = ((Function<Object, Step>) x -> {
                Step aux = ((Function<Object, Step>) x1 -> {
                    Step aux1 = ((Function<Object, Step>) x2 -> {
                        List lAux = (List) x2;

                        if (lAux.isEmpty()) {
                            return new Done();
                        } else {
                            List<Student> sub = lAux.subList(1, lAux.size());

                            if (((Predicate) p).test(lAux.get(0))) {
                                return new Yield<>(f.apply((Student) lAux.get(0)), sub);
                            } else {
                                return new Skip<>(sub);
                            }
                        }
                    }).apply(x1);

                    return aux1;
                }).apply(x);

                return aux;
            }).apply(auxState);

            if (step instanceof Done) {
                over = true;
            } else if (step instanceof Skip) {
                auxState = step.state;
            } else if (step instanceof Yield) {
                res1.add((Student) step.elem);
                auxState = step.state;
            }
        }

        ArrayList<Student> res = (ArrayList<Student>) res1;


        System.out.println(res.size());
        return res;
    }
}
