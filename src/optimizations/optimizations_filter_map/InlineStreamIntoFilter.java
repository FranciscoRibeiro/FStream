package optimizations.optimizations_filter_map;

import datatypes.*;
import experimental.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class InlineStreamIntoFilter extends MasterBenchmarkFilterMap{
    public static void main(String[] args) {
        InlineStreamIntoFilter isf = new InlineStreamIntoFilter();

        isf.populate();

        isf.warmUp();

        isf.measure();

        isf.end();
    }

    @Override
    public ArrayList<Student> work() {
        Predicate<Student> p = s -> s.grade >= 95;
        Function<Student, Student> f = s -> new Student(s.name, Math.round((double) s.grade / 10));


        Function<Object, Step> nextFilter = x -> {
            Step aux = ((Function<Object, Step>) x1 -> {
                List aux1 = (List) x1;

                if (aux1.isEmpty()) {
                    return new Done();
                } else {
                    List<Student> sub = aux1.subList(1, aux1.size());
                    return new Yield<Student, List<Student>>((Student) aux1.get(0), sub);
                }
            }).apply(x);

            if(aux instanceof Done){
                return new Done();
            }
            else if(aux instanceof Skip){
                return new Skip<>(aux.state); //Need to change this later
            }
            else if(aux instanceof Yield){
                if(((Predicate) p).test(aux.elem)){
                    return new Yield<>((Student) aux.elem, aux.state);
                }
                else{
                    return new Skip<>(aux.state);
                }
            }

            return null;
        };

        Function<Object, Step> nextMap = x -> {
            Step aux = nextFilter.apply(x);

            if(aux instanceof Done){
                return new Done();
            }
            else if(aux instanceof Skip){
                return new Skip<>(aux.state); //Need to change this later
            }
            else if(aux instanceof Yield){
                return new Yield<>(f.apply((Student) aux.elem), aux.state);
            }

            return null;
        };

        ArrayList<Student> res1 = new ArrayList<>();
        Object auxState = l;
        boolean over = false;

        while (!over) {
            Step step = nextMap.apply(auxState);

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
