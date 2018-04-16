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

public class CaseOfCaseInUnfold {
    public static void main(String[] args) {
        System.out.println(MethodHandles.lookup().lookupClass().getSimpleName() + "...");

        ArrayList<Integer> xs = new ArrayList<>(Arrays.asList(new Integer[]{1, 2, 3, 4, 5}));
        ArrayList<String> ys = new ArrayList<>(Arrays.asList(new String[]{"ola", "hello", "hallo", "hola", "ciao"}));

        Predicate<Integer> p = x -> x < 5;

        ArrayList<Pair<Integer, String>> res1 = new ArrayList<>();
        final Object[] auxState = {new Triple<>(xs, ys, Optional.empty())};
        final boolean[] over = {false};

        while (!over[0]) {
            Step step = ((Function<Object, Step>) x -> {
                if (!(((Triple) x).getElem()).isPresent()) {
                    Step aux = ((Function<Object, Step>) x1 -> {
                        Step aux1 = ((Function<Object, Step>) x2 -> {
                            List lAux = (List) x2;

                            if (lAux.isEmpty()) {
                                Step innerAux = new Done();

                                if (innerAux instanceof Done) {
                                    over[0] = true;
                                } else if (innerAux instanceof Skip) {
                                    auxState[0] = innerAux.state;
                                } else if (innerAux instanceof Yield) {
                                    res1.add((Pair<Integer, String>) innerAux.elem);
                                    auxState[0] = innerAux.state;
                                }
                            } else {
                                List<Integer> sub = lAux.subList(1, lAux.size());
                                Step innerAux = new Yield<Integer, List<Integer>>((Integer) lAux.get(0), sub);

                                if (((Predicate) p).test(innerAux.elem)) {
                                    innerAux = new Skip<>(new Triple(innerAux.state, ((Triple) x).getStateB(), Optional.of(innerAux.elem)));

                                    if (innerAux instanceof Done) {
                                        over[0] = true;
                                    } else if (innerAux instanceof Skip) {
                                        auxState[0] = innerAux.state;
                                    } else if (innerAux instanceof Yield) {
                                        res1.add((Pair<Integer, String>) innerAux.elem);
                                        auxState[0] = innerAux.state;
                                    }
                                } else {
                                    innerAux = new Skip<>(new Triple(innerAux.state, ((Triple) x).getStateB(), Optional.empty()));

                                    if (innerAux instanceof Done) {
                                        over[0] = true;
                                    } else if (innerAux instanceof Skip) {
                                        auxState[0] = innerAux.state;
                                    } else if (innerAux instanceof Yield) {
                                        res1.add((Pair<Integer, String>) innerAux.elem);
                                        auxState[0] = innerAux.state;
                                    }
                                }
                            }

                            return null;
                        }).apply(x1);

                        return aux1;
                    }).apply(((Triple) x).getStateA());

                    return aux;
                } else { //There is a value present in Optional
                    Step aux = ((Function<Object, Step>) x1 -> {
                        List aux1 = (List) x1;

                        if (aux1.isEmpty()) {
                            Step innerAux = new Done();

                            if (innerAux instanceof Done) {
                                over[0] = true;
                            } else if (innerAux instanceof Skip) {
                                auxState[0] = innerAux.state;
                            } else if (innerAux instanceof Yield) {
                                res1.add((Pair<Integer, String>) innerAux.elem);
                                auxState[0] = innerAux.state;
                            }
                        } else {
                            List<String> sub = aux1.subList(1, aux1.size());

                            Step innerAux = new Yield<>(new Pair<>(((Triple) x).getElem().get(), aux1.get(0)), new Triple(((Triple) x).getStateA(), sub, Optional.empty()));

                            if (innerAux instanceof Done) {
                                over[0] = true;
                            } else if (innerAux instanceof Skip) {
                                auxState[0] = innerAux.state;
                            } else if (innerAux instanceof Yield) {
                                res1.add((Pair<Integer, String>) innerAux.elem);
                                auxState[0] = innerAux.state;
                            }
                        }

                        return null;
                    }).apply(((Triple) x).getStateB());

                    return aux;
                }
            }).apply(auxState[0]);
        }

        ArrayList<Pair<Integer,String>> res = (ArrayList<Pair<Integer, String>>)
                res1;

        System.out.println(res);
    }
}
