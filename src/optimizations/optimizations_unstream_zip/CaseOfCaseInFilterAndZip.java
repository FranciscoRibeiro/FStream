package optimizations.optimizations_unstream_zip;

import datatypes.Done;
import datatypes.Skip;
import datatypes.Step;
import datatypes.Yield;
import util.Pair;
import util.Triple;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public class CaseOfCaseInFilterAndZip {
    public static void main(String[] args) {
        System.out.println(MethodHandles.lookup().lookupClass().getSimpleName() + "...");

        ArrayList<Integer> xs = new ArrayList<>(Arrays.asList(new Integer[]{1, 2, 3, 4, 5}));
        ArrayList<String> ys = new ArrayList<>(Arrays.asList(new String[]{"ola", "hello", "hallo", "hola", "ciao"}));

        Predicate<Integer> p = x -> x < 5;

        Function<Object, Step> nextFilter = x -> {
            Step aux = ((Function<Object, Step>) x1 -> {
                List aux1 = (List) x1;

                if (aux1.isEmpty()) {
                    Step innerAux = new Done();

                    if(innerAux instanceof Done){
                        return new Done();
                    }
                    else if(innerAux instanceof Skip){
                        return new Skip<>(innerAux.state);
                    }
                    else if(innerAux instanceof Yield){
                        if(((Predicate) p).test(innerAux.elem)){
                            return new Yield<>((Integer) innerAux.elem, innerAux.state);
                        }
                        else{
                            return new Skip<>(innerAux.state);
                        }
                    }
                } else {
                    List<Integer> sub = aux1.subList(1, aux1.size());
                    Step innerAux = new Yield<Integer, List<Integer>>((Integer) aux1.get(0), sub);

                    if(innerAux instanceof Done){
                        return new Done();
                    }
                    else if(innerAux instanceof Skip){
                        return new Skip<>(innerAux.state);
                    }
                    else if(innerAux instanceof Yield){
                        if(((Predicate) p).test(innerAux.elem)){
                            return new Yield<>((Integer) innerAux.elem, innerAux.state);
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

        Function<Object, Step> nextZip = x -> {
            if(!(((Triple) x).getElem()).isPresent()){
                Step aux = nextFilter.apply(((Triple) x).getStateA());

                if(aux instanceof Done){
                    return new Done();
                }
                else if(aux instanceof Skip){
                    return new Skip<>(new Triple(aux.state, ((Triple) x).getStateB(), Optional.empty()));
                }
                else if(aux instanceof Yield){
                    return new Skip<>(new Triple(aux.state, ((Triple) x).getStateB(), Optional.of(aux.elem)));
                }
            }
            else{ //There is a value present in Optional
                Step aux = ((Function<Object, Step>) x1 -> {
                    List aux1 = (List) x1;

                    if (aux1.isEmpty()) {
                        Step innerAux = new Done();

                        if(innerAux instanceof Done){
                            return new Done();
                        }
                        else if(innerAux instanceof Skip){
                            return new Skip<>(new Triple(((Triple) x).getStateA(), innerAux.state, ((Triple) x).getElem()));
                        }
                        else if(innerAux instanceof Yield){
                            return new Yield<>(new Pair<>(((Triple) x).getElem().get(), innerAux.elem), new Triple(((Triple) x).getStateA(), innerAux.state, Optional.empty()));
                        }
                    } else {
                        List<String> sub = aux1.subList(1, aux1.size());
                        Step innerAux = new Yield<String, List<String>>((String) aux1.get(0), sub);

                        if(innerAux instanceof Done){
                            return new Done();
                        }
                        else if(innerAux instanceof Skip){
                            return new Skip<>(new Triple(((Triple) x).getStateA(), innerAux.state, ((Triple) x).getElem()));
                        }
                        else if(innerAux instanceof Yield){
                            return new Yield<>(new Pair<>(((Triple) x).getElem().get(), innerAux.elem), new Triple(((Triple) x).getStateA(), innerAux.state, Optional.empty()));
                        }
                    }

                    return null;
                }).apply(((Triple) x).getStateB());

                return aux;
            }

            return null;
        };

        ArrayList<Pair<Integer, String>> res1 = new ArrayList<>();
        Object auxState = new Triple<>(xs, ys, Optional.empty());
        boolean over = false;

        while (!over) {
            Step step = nextZip.apply(auxState);

            if (step instanceof Done) {
                over = true;
            } else if (step instanceof Skip) {
                auxState = step.state;
            } else if (step instanceof Yield) {
                res1.add((Pair<Integer, String>) step.elem);
                auxState = step.state;
            }
        }

        ArrayList<Pair<Integer,String>> res = (ArrayList<Pair<Integer, String>>)
                res1;

        System.out.println(res);
    }
}
