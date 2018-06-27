package optimizations.optimizations_unstream_iterate;

import util.Pair;
import util.Triple;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class InvertLoops {
    public static void print(List<List<Integer>> l, String fileName) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(fileName);

            for (List<Integer> li : l) {
                for (Integer i : li) {
                    fw.write(i + "| ");
                }
                fw.write("\n");
            }

            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        List<Integer> l = Arrays.asList(1);

        Function<List<Integer>, List<Integer>> f1 =
                row -> {
                    ArrayList<Integer> res = new ArrayList<>();
                    Triple auxState = new Triple<>(Arrays.asList(0), row, Optional.empty());
                    boolean over = false;

                    while (!over) {
                        if (!(auxState.getElem()).isPresent()) {
                            List lAux = (List) auxState.getStateA();

                            if (lAux.isEmpty()) {
                                auxState = new Triple(row, auxState.getStateB(), Optional.empty());
                            } else {
                                List<Integer> sub = lAux.subList(1, lAux.size());
                                auxState = new Triple(sub, auxState.getStateB(), Optional.of((Integer) lAux.get(0)));
                            }
                        } else {
                            List lAux = (List) auxState.getStateB();

                            if (lAux.isEmpty()) {
                                auxState = new Triple(auxState.getStateA(), Arrays.asList(0), auxState.getElem());
                                over = true;
                            } else {
                                List<Integer> sub = lAux.subList(1, lAux.size());
                                res.add(((Function<Pair<Integer, Integer>, Integer>) p -> p.getX() + p.getY()).apply(new Pair<>((Integer) auxState.getElem().get(), (Integer) lAux.get(0))));
                                auxState = new Triple(auxState.getStateA(), sub, Optional.empty());
                            }
                        }
                    }

                    over = false;

                    while(!over){
                        if (!(auxState.getElem()).isPresent()) {
                            List lAux = (List) auxState.getStateA();

                            if (lAux.isEmpty()) {
                                over = true;
                            } else {
                                List<Integer> sub = lAux.subList(1, lAux.size());
                                auxState = new Triple(sub, auxState.getStateB(), Optional.of((Integer) lAux.get(0)));
                            }

                        } else { //There is a value present in Optional
                            List lAux = (List) auxState.getStateB();

                            if (lAux.isEmpty()) {
                                over = true;
                            } else {
                                List<Integer> sub = lAux.subList(1, lAux.size());
                                res.add(((Function<Pair<Integer, Integer>, Integer>) p -> p.getX() + p.getY()).apply(new Pair<>((Integer) auxState.getElem().get(), (Integer) lAux.get(0))));
                                auxState = new Triple(auxState.getStateA(), sub, Optional.empty());
                            }
                        }
                    }

                    return res;
                };

        long start = System.currentTimeMillis();


        ArrayList<List<Integer>> res = new ArrayList<>();
        Pair auxState = new Pair<>(2000, l);
        boolean over = false;

        while (!over) {
            Pair<Integer, List<Integer>> p = (Pair) auxState;
            if (p.getX() == 0) {
                over = true;
            } else {
                res.add(p.getY());
                auxState = new Pair<>(p.getX() - 1, f1.apply(p.getY()));
            }
        }

        List<List<Integer>> res1 = res;


        System.out.println(System.currentTimeMillis() - start);

        print(res1, "res1.txt");
    }
}
