package optimizations.optimizations_unstream_iterate;

import util.Left;
import util.Pair;
import util.Right;
import util.Triple;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class NoFunctions {
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
                Triple auxState = new Triple<>(new Left(Arrays.asList(BigInteger.ZERO)), new Left(row), Optional.empty());
                boolean over = false;

                while (!over) {
                    if (!(auxState.getElem()).isPresent()) {
                        if (auxState.getStateA() instanceof Left) {
                            List lAux = (List) ((Left) auxState.getStateA()).fromLeft();

                            if (lAux.isEmpty()) {
                                auxState = new Triple(new Right(row), auxState.getStateB(), Optional.empty());
                            } else {
                                List<BigInteger> sub = lAux.subList(1, lAux.size());
                                auxState = new Triple(new Left(sub), auxState.getStateB(), Optional.of((BigInteger) lAux.get(0)));
                            }
                        } else if (auxState.getStateA() instanceof Right) {
                            List lAux = (List) ((Right) auxState.getStateA()).fromRight();

                            if (lAux.isEmpty()) {
                                over = true;
                            } else {
                                List<BigInteger> sub = lAux.subList(1, lAux.size());
                                auxState = new Triple(new Right(sub), auxState.getStateB(), Optional.of((BigInteger) lAux.get(0)));
                            }
                        }
                    } else { //There is a value present in Optional
                        if (auxState.getStateB() instanceof Left) {
                            List lAux = (List) ((Left) auxState.getStateB()).fromLeft();

                            if (lAux.isEmpty()) {
                                auxState = new Triple(auxState.getStateA(), new Right(Arrays.asList(BigInteger.ZERO)), auxState.getElem());
                            } else {
                                List<BigInteger> sub = lAux.subList(1, lAux.size());
                                res.add(((Function<Pair<BigInteger, BigInteger>, BigInteger>) p -> p.getX().add(p.getY())).apply(new Pair<>((BigInteger) auxState.getElem().get(), (BigInteger) lAux.get(0))));
                                auxState = new Triple(auxState.getStateA(), new Left(sub), Optional.empty());
                            }
                        } else if (auxState.getStateB() instanceof Right) {
                            List lAux = (List) ((Right) auxState.getStateB()).fromRight();

                            if (lAux.isEmpty()) {
                                over = true;
                            } else {
                                List<BigInteger> sub = lAux.subList(1, lAux.size());
                                res.add(((Function<Pair<BigInteger, BigInteger>, BigInteger>) p -> p.getX().add(p.getY())).apply(new Pair<>((BigInteger) auxState.getElem().get(), (BigInteger) lAux.get(0))));
                                auxState = new Triple(auxState.getStateA(), new Right(sub), Optional.empty());
                            }
                        }
                    }
                }

                return res;
            };

        long start = System.currentTimeMillis();


        ArrayList<List<BigInteger>> res = new ArrayList<>();
        Pair auxState = new Pair<>(2000, l);
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
