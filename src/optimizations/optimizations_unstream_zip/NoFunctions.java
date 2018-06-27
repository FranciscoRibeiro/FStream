package optimizations.optimizations_unstream_zip;

import util.Pair;
import util.Triple;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class NoFunctions {
    public static void main(String[] args) {
        System.out.println(MethodHandles.lookup().lookupClass().getSimpleName() + "...");

        ArrayList<Integer> xs = new ArrayList<>(Arrays.asList(new Integer[]{1, 2, 3, 4, 5}));
        ArrayList<String> ys = new ArrayList<>(Arrays.asList(new String[]{"ola", "hello", "hallo", "hola", "ciao"}));

        Predicate<Integer> p = x -> x < 5;

        ArrayList<Pair<Integer, String>> res1 = new ArrayList<>();
        Triple auxState = new Triple<>(xs, ys, Optional.empty());
        boolean over = false;

        while (!over) {
            if (!(auxState.getElem()).isPresent()) {
                List lAux = (List) auxState.getStateA();

                if (lAux.isEmpty()) {
                    over = true;
                } else {
                    List<Integer> sub = lAux.subList(1, lAux.size());

                    if (((Predicate) p).test(lAux.get(0))) {
                        auxState = new Triple(sub, auxState.getStateB(), Optional.of(lAux.get(0)));
                    } else {
                        auxState = new Triple(sub, auxState.getStateB(), Optional.empty());
                    }
                }
            } else { //There is a value present in Optional
                List aux1 = (List) auxState.getStateB();

                if (aux1.isEmpty()) {
                    over = true;
                } else {
                    List<String> sub = aux1.subList(1, aux1.size());

                    res1.add(new Pair<>((Integer) auxState.getElem().get(), (String) aux1.get(0)));
                    auxState = new Triple(auxState.getStateA(), sub, Optional.empty());
                }
            }
        }

        ArrayList<Pair<Integer,String>> res = (ArrayList<Pair<Integer, String>>)
                res1;

        System.out.println(res);
    }
}
