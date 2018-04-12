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

public class CaseOfCaseInUnfold extends MasterBenchmarkFilterMap{
    public static void main(String[] args) {
        CaseOfCaseInUnfold ccu = new CaseOfCaseInUnfold();

        ccu.populate();

        ccu.warmUp();

        ccu.measure();

        ccu.end();
    }

    @Override
    public ArrayList<Student> work() {
        Predicate<Student> p = s -> s.grade >= 95;
        Function<Student, Student> f = s -> new Student(s.name, Math.round((double) s.grade / 10));


        ArrayList<Student> res1 = new ArrayList<>();
        final Object[] auxState = {l};
        final boolean[] over = {false};

        while (!over[0]) {
            Step innerAux = ((Function<Object, Step>) x -> {
                Step aux = ((Function<Object, Step>) x1 -> {
                    Step aux1 = ((Function<Object, Step>) x2 -> {
                        List lAux = (List) x2;

                        if (lAux.isEmpty()) {
                            Step innerAux2 = new Done();

                            if (innerAux2 instanceof Done) {
                                over[0] = true;
                            } else if (innerAux2 instanceof Skip) {
                                auxState[0] = innerAux2.state;
                            } else if (innerAux2 instanceof Yield) {
                                res1.add((Student) innerAux2.elem);
                                auxState[0] = innerAux2.state;
                            }
                        } else {
                            List<Student> sub = lAux.subList(1, lAux.size());

                            if (((Predicate) p).test(lAux.get(0))) {
                                Step innerAux2 = new Yield<>(f.apply((Student) lAux.get(0)), sub);

                                if (innerAux2 instanceof Done) {
                                    over[0] = true;
                                } else if (innerAux2 instanceof Skip) {
                                    auxState[0] = innerAux2.state;
                                } else if (innerAux2 instanceof Yield) {
                                    res1.add((Student) innerAux2.elem);
                                    auxState[0] = innerAux2.state;
                                }
                            } else {
                                Step innerAux2 = new Skip<>(sub);

                                if (innerAux2 instanceof Done) {
                                    over[0] = true;
                                } else if (innerAux2 instanceof Skip) {
                                    auxState[0] = innerAux2.state;
                                } else if (innerAux2 instanceof Yield) {
                                    res1.add((Student) innerAux2.elem);
                                    auxState[0] = innerAux2.state;
                                }
                            }
                        }

                        return null;
                    }).apply(x1);

                    return aux1;
                }).apply(x);

                return aux;
            }).apply(auxState[0]);
        }

        ArrayList<Student> res = (ArrayList<Student>) res1;


        System.out.println(res.size());
        return res;
    }
}
