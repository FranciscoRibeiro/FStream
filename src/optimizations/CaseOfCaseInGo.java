package optimizations;

import datatypes.Done;
import datatypes.Skip;
import datatypes.Step;
import datatypes.Yield;
import util.Either;
import util.Left;
import util.RecursiveLambda;
import util.Right;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class CaseOfCaseInGo {

    public static void main(String[] args) {
        System.out.println("GHC optimizations...");
        ArrayList<Integer> xs = new ArrayList<>(Arrays.asList(new Integer[]{1, 2, 3, 4, 5}));
        ArrayList<Integer> ys = new ArrayList<>(Arrays.asList(new Integer[]{6, 7, 8, 9, 10}));
        BiFunction<Integer, Integer, Integer> f = (a, b) -> a + b;


        final Integer[] value = {0};
        final Object[] auxState = {new Left(xs)};
        final boolean[] over = {false};

        while (!over[0]) {
            Step step = ((Function<Object, Step>) x -> {
                if (x instanceof Left) {
                    Step aux = ((Function<Object, Step>) x1 -> {
                        List aux1 = (List) x1;

                        if (aux1.isEmpty()) {
                            Step innerAux = new Skip<Either>(new Right(ys));

                            if (innerAux instanceof Done) {
                                over[0] = true;
                            } else if (innerAux instanceof Skip) {
                                auxState[0] = innerAux.state;
                            } else if (innerAux instanceof Yield) {
                                auxState[0] = innerAux.state;
                                value[0] = f.apply(value[0], (Integer) innerAux.elem);
                            }

                        } else {
                            List<Integer> sub = aux1.subList(1, aux1.size());
                            Step innerAux = new Yield<Integer, Either>((Integer) aux1.get(0), new Left(sub));

                            if (innerAux instanceof Done) {
                                over[0] = true;
                            } else if (innerAux instanceof Skip) {
                                auxState[0] = innerAux.state;
                            } else if (innerAux instanceof Yield) {
                                auxState[0] = innerAux.state;
                                value[0] = f.apply(value[0], (Integer) innerAux.elem);
                            }
                        }

                        return null;
                    }).apply(((Left) x).fromLeft());

                    return aux;
                } else if (x instanceof Right) {
                    Step aux = ((Function<Object, Step>) x1 -> {
                        List aux1 = (List) x1;

                        if (aux1.isEmpty()) {
                            Step innerAux = new Done();

                            if (innerAux instanceof Done) {
                                over[0] = true;
                            } else if (innerAux instanceof Skip) {
                                auxState[0] = innerAux.state;
                            } else if (innerAux instanceof Yield) {
                                auxState[0] = innerAux.state;
                                value[0] = f.apply(value[0], (Integer) innerAux.elem);
                            }
                        } else {
                            List<Integer> sub = aux1.subList(1, aux1.size());
                            Step innerAux = new Yield<Integer, Either>((Integer) aux1.get(0), new Right(sub));

                            if (innerAux instanceof Done) {
                                over[0] = true;
                            } else if (innerAux instanceof Skip) {
                                auxState[0] = innerAux.state;
                            } else if (innerAux instanceof Yield) {
                                auxState[0] = innerAux.state;
                                value[0] = f.apply(value[0], (Integer) innerAux.elem);
                            }
                        }

                        return null;
                    }).apply(((Right) x).fromRight());

                    return aux;
                }

                return null;
            }).apply(auxState[0]);
        }

        Integer res = value[0];

        System.out.println(res);
    }
}
