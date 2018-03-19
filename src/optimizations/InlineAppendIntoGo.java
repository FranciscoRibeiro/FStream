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

public class InlineAppendIntoGo extends MasterBenchmark{

    public static void main(String[] args) {
        System.out.println("InlineAppendIntoGo...");
        /*ArrayList<Integer> xs = new ArrayList<>(Arrays.asList(new Integer[]{1, 2, 3, 4, 5}));
        ArrayList<Integer> ys = new ArrayList<>(Arrays.asList(new Integer[]{6, 7, 8, 9, 10}));*/

        InlineAppendIntoGo iag = new InlineAppendIntoGo();

        iag.populate();

        iag.warmUp();

        iag.measure();

        iag.end();
    }

    @Override
    public void work() {
        BiFunction<Integer, Integer, Integer> f = (a, b) -> a+b;


        Integer value = 0;
        Object auxState = new Left(xs);
        boolean over = false;

        while (!over) {
            Step step = ((Function<Object, Step>) x -> {
                if (x instanceof Left) {
                    Step aux = ((Function<Object, Step>) x1 -> {
                        List aux1 = (List) x1;

                        if (aux1.isEmpty()) {
                            return new Skip<Either>(new Right(ys));

                        } else {
                            List<Integer> sub = aux1.subList(1, aux1.size());
                            return new Yield<Integer, Either>((Integer) aux1.get(0), new Left(sub));
                        }
                    }).apply(((Left) x).fromLeft());

                    return aux;
                } else if (x instanceof Right) {
                    Step aux = ((Function<Object, Step>) x1 -> {
                        List aux1 = (List) x1;

                        if (aux1.isEmpty()) {
                            return new Done();
                        } else {
                            List<Integer> sub = aux1.subList(1, aux1.size());
                            return new Yield<Integer, Either>((Integer) aux1.get(0), new Right(sub));
                        }
                    }).apply(((Right) x).fromRight());

                    return aux;
                }

                return null;
            }).apply(auxState);

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
