package optimizations.optimizations_unstream_zip;

import datatypes.Step;
import util.Pair;
import util.Triple;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public class TrivialRewriteInUnfold {
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
                                over[0] = true;
                            } else {
                                List<Integer> sub = lAux.subList(1, lAux.size());

                                if (((Predicate) p).test(lAux.get(0))) {
                                    auxState[0] = new Triple(sub, ((Triple) x).getStateB(), Optional.of(lAux.get(0)));
                                } else {
                                    auxState[0] = new Triple(sub, ((Triple) x).getStateB(), Optional.empty());
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
                            over[0] = true;
                        } else {
                            List<String> sub = aux1.subList(1, aux1.size());

                            res1.add(new Pair<>((Integer) ((Triple) x).getElem().get(), (String) aux1.get(0)));
                            auxState[0] = new Triple(((Triple) x).getStateA(), sub, Optional.empty());
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
