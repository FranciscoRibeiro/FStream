package optimizations.optimizations_unstream_iterate;

import util.Pair;
import util.Triple;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class InvertLoops {
    public static void print(List<List<BigInteger>> l, String fileName) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(fileName);

            for (List<BigInteger> li : l) {
                for (BigInteger i : li) {
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
        List<BigInteger> l = Arrays.asList(BigInteger.ONE);

        Function<List<BigInteger>, List<BigInteger>> f1 =
                row -> {
                    ArrayList<BigInteger> res = new ArrayList<>();
                    Triple auxState = new Triple<>(Arrays.asList(BigInteger.ZERO), row, Optional.empty());
                    boolean over = false;

                    while (!over) {
                        if (!(auxState.getElem()).isPresent()) {
                            List lAux = (List) auxState.getStateA();

                            if (lAux.isEmpty()) {
                                auxState = new Triple(row, auxState.getStateB(), Optional.empty());
                            } else {
                                List<BigInteger> sub = lAux.subList(1, lAux.size());
                                auxState = new Triple(sub, auxState.getStateB(), Optional.of((BigInteger) lAux.get(0)));
                            }
                        } else {
                            List lAux = (List) auxState.getStateB();

                            if (lAux.isEmpty()) {
                                auxState = new Triple(auxState.getStateA(), Arrays.asList(BigInteger.ZERO), auxState.getElem());
                                over = true;
                            } else {
                                List<BigInteger> sub = lAux.subList(1, lAux.size());
                                res.add(((Function<Pair<BigInteger, BigInteger>, BigInteger>) p -> p.getX().add(p.getY())).apply(new Pair<>((BigInteger) auxState.getElem().get(), (BigInteger) lAux.get(0))));
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
                                List<BigInteger> sub = lAux.subList(1, lAux.size());
                                auxState = new Triple(sub, auxState.getStateB(), Optional.of((BigInteger) lAux.get(0)));
                            }

                        } else { //There is a value present in Optional
                            List lAux = (List) auxState.getStateB();

                            if (lAux.isEmpty()) {
                                over = true;
                            } else {
                                List<BigInteger> sub = lAux.subList(1, lAux.size());
                                res.add(((Function<Pair<BigInteger, BigInteger>, BigInteger>) p -> p.getX().add(p.getY())).apply(new Pair<>((BigInteger) auxState.getElem().get(), (BigInteger) lAux.get(0))));
                                auxState = new Triple(auxState.getStateA(), sub, Optional.empty());
                            }
                        }
                    }

                    return res;
                };

        long start = System.currentTimeMillis();


        ArrayList<List<BigInteger>> res = new ArrayList<>();
        Pair auxState = new Pair<>(3000, l);
        boolean over = false;

        while (!over) {
            Pair<Integer, List<BigInteger>> p = (Pair) auxState;
            if (p.getX() == 0) {
                over = true;
            } else {
                res.add(p.getY());
                auxState = new Pair<>(p.getX() - 1, f1.apply(p.getY()));
            }
        }

        List<List<BigInteger>> res1 = res;


        System.out.println(System.currentTimeMillis() - start);

        print(res1, "res1.txt");
    }
}
