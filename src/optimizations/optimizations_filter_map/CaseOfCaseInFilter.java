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

public class CaseOfCaseInFilter extends MasterBenchmarkFilterMap{
    public static void main(String[] args) {
        CaseOfCaseInFilter ccf = new CaseOfCaseInFilter();

        ccf.populate();

        ccf.warmUp();

        ccf.measure();

        ccf.end();
    }
    @Override
    public ArrayList<Student> work() {
        Predicate<Student> p = s -> s.grade >= 95;
        Function<Student, Student> f = s -> new Student(s.name, Math.round((double) s.grade / 10));


        Function<Object, Step> nextFilter = x -> {
            Step aux = ((Function<Object, Step>) x1 -> {
                List aux1 = (List) x1;

                if (aux1.isEmpty()) {
                    Step innerAux = new Done();

                    if(innerAux instanceof Done){
                        return new Done();
                    }
                    else if(innerAux instanceof Skip){
                        return new Skip<>(innerAux.state); //Need to change this later
                    }
                    else if(innerAux instanceof Yield){
                        if(((Predicate) p).test(innerAux.elem)){
                            return new Yield<>((Student) innerAux.elem, innerAux.state);
                        }
                        else{
                            return new Skip<>(innerAux.state);
                        }
                    }
                } else {
                    List<Student> sub = aux1.subList(1, aux1.size());
                    Step innerAux = new Yield<Student, List<Student>>((Student) aux1.get(0), sub);

                    if(innerAux instanceof Done){
                        return new Done();
                    }
                    else if(innerAux instanceof Skip){
                        return new Skip<>(innerAux.state); //Need to change this later
                    }
                    else if(innerAux instanceof Yield){
                        if(((Predicate) p).test(innerAux.elem)){
                            return new Yield<>((Student) innerAux.elem, innerAux.state);
                        }
                        else{
                            return new Skip<>(innerAux.state);
                        }
                    }
                }

                return null;
            }).apply(x);

            return aux;
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
