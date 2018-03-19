package optimizations;

import datatypes.Done;
import datatypes.Skip;
import datatypes.Step;
import datatypes.Yield;
import util.Either;
import util.Left;
import util.Right;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class CaseOfCaseInAppend {

    public static void main(String[] args) {
        System.out.println("GHC optimizations...");
        ArrayList<Integer> xs = new ArrayList<>(Arrays.asList(new Integer[]{1, 2, 3, 4, 5}));
        ArrayList<Integer> ys = new ArrayList<>(Arrays.asList(new Integer[]{6, 7, 8, 9, 10}));
        BiFunction<Integer, Integer, Integer> f = (a, b) -> a+b;


        Function<Object, Step> nextAppend = x -> {
            if(x instanceof Left){
                Step aux = ((Function<Object, Step>) x1 -> {
                    List aux1 = (List) x1;

                    if (aux1.isEmpty()) {
                        Step innerAux = new Done();

                        if(innerAux instanceof Done){
                            return new Skip<Either>(new Right(ys));
                        }
                        else if(innerAux instanceof Skip){
                            return new Skip<Either>(new Left(innerAux.state));
                        }
                        else if(innerAux instanceof Yield){
                            return new Yield<Integer, Either>((Integer) innerAux.elem, new Left(innerAux.state));
                        }
                    } else {
                        List<Integer> sub = aux1.subList(1, aux1.size());
                        Step innerAux = new Yield<Integer, List<Integer>>((Integer) aux1.get(0), sub);

                        if(innerAux instanceof Done){
                            return new Skip<Either>(new Right(ys));
                        }
                        else if(innerAux instanceof Skip){
                            return new Skip<Either>(new Left(innerAux.state));
                        }
                        else if(innerAux instanceof Yield){
                            return new Yield<Integer, Either>((Integer) innerAux.elem, new Left(innerAux.state));
                        }
                    }

                return null;
                }).apply(((Left) x).fromLeft());

                return aux;
            }
            else if(x instanceof Right){
                Step aux = ((Function<Object, Step>) x1 -> {
                    List aux1 = (List) x1;

                    if (aux1.isEmpty()) {
                        Step innerAux = new Done();

                        if(innerAux instanceof Done){
                            return new Done();
                        }
                        else if(innerAux instanceof Skip){
                            return new Skip<Either>(new Right(innerAux.state));
                        }
                        else if(innerAux instanceof Yield){
                            return new Yield<Integer, Either>((Integer) innerAux.elem, new Right(innerAux.state));
                        }
                    } else {
                        List<Integer> sub = aux1.subList(1, aux1.size());
                        Step innerAux = new Yield<Integer, List<Integer>>((Integer) aux1.get(0), sub);

                        if(innerAux instanceof Done){
                            return new Done();
                        }
                        else if(innerAux instanceof Skip){
                            return new Skip<Either>(new Right(innerAux.state));
                        }
                        else if(innerAux instanceof Yield){
                            return new Yield<Integer, Either>((Integer) innerAux.elem, new Right(innerAux.state));
                        }
                    }

                    return null;
                }).apply(((Right) x).fromRight());

                return aux;
            }

            return null;
        };

        Integer value = 0;
        Object auxState = new Left(xs);
        boolean over = false;

        while (!over) {
            Step step = nextAppend.apply(auxState);

            if (step instanceof Done) {
                over = true;
            } else if (step instanceof Skip) {
                auxState = step.state;
            } else if (step instanceof Yield) {
                auxState = step.state;
                value = f.apply(value, (Integer) step.elem);
            }
        }

        Integer res = value;

        System.out.println(res);
    }
}
