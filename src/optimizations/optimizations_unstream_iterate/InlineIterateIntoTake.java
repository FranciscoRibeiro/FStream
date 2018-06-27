package optimizations.optimizations_unstream_iterate;

import datatypes.Done;
import datatypes.Skip;
import datatypes.Step;
import datatypes.Yield;
import util.Left;
import util.Pair;
import util.Right;
import util.Triple;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class InlineIterateIntoTake {
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
                    final Object[] auxState = {new Triple<>(new Left(Arrays.asList(0)), new Left(row), Optional.empty())};
                    final boolean[] over = {false};

                    while (!over[0]) {
                        Step step = ((Function<Object, Step>) x -> {
                            Step aux = ((Function<Object, Step>) x1 -> {
                                if (!(((Triple) x1).getElem()).isPresent()) {
                                    Step aux1 = ((Function<Object, Step>) x11 -> {
                                        if (x11 instanceof Left) {
                                            Step aux2 = ((Function<Object, Step>) x2 -> {
                                                List lAux = (List) x2;

                                                if (lAux.isEmpty()) {
                                                    auxState[0] = new Triple(new Right(row), ((Triple) x1).getStateB(), Optional.empty());
                                                } else {
                                                    List<Integer> sub = lAux.subList(1, lAux.size());
                                                    auxState[0] = new Triple(new Left(sub), ((Triple) x1).getStateB(), Optional.of((Integer) lAux.get(0)));
                                                }

                                                return null;

                                            }).apply(((Left) x11).fromLeft());

                                            return aux2;
                                        } else if (x11 instanceof Right) {
                                            Step aux2 = ((Function<Object, Step>) x2 -> {
                                                List lAux = (List) x2;

                                                if (lAux.isEmpty()) {
                                                    over[0] = true;
                                                } else {
                                                    List<Integer> sub = lAux.subList(1, lAux.size());
                                                    auxState[0] = new Triple(new Right(sub), ((Triple) x1).getStateB(), Optional.of((Integer) lAux.get(0)));
                                                }

                                                return null;

                                            }).apply(((Right) x11).fromRight());

                                            return aux2;
                                        }

                                        return null;
                                    }).apply(((Triple) x1).getStateA());

                                    return aux1;
                                } else { //There is a value present in Optional
                                    Step aux1 = ((Function<Object, Step>) x11 -> {
                                        if (x11 instanceof Left) {
                                            Step aux2 = ((Function<Object, Step>) x2 -> {
                                                List lAux = (List) x2;

                                                if (lAux.isEmpty()) {
                                                    auxState[0] = new Triple(((Triple) x1).getStateA(), new Right(Arrays.asList(0)), ((Triple) x1).getElem());
                                                } else {
                                                    List<Integer> sub = lAux.subList(1, lAux.size());
                                                    res.add((Integer) ((Function<Pair<Integer, Integer>, Integer>) p -> p.getX() + p.getY()).apply(new Pair<>((Integer) ((Triple) x1).getElem().get(), (Integer) lAux.get(0))));
                                                    auxState[0] = new Triple(((Triple) x1).getStateA(), new Left(sub), Optional.empty());
                                                }

                                                return null;

                                            }).apply(((Left) x11).fromLeft());

                                            return aux2;
                                        } else if (x11 instanceof Right) {
                                            Step aux2 = ((Function<Object, Step>) x2 -> {
                                                List lAux = (List) x2;

                                                if (lAux.isEmpty()) {
                                                    over[0] = true;
                                                } else {
                                                    List<Integer> sub = lAux.subList(1, lAux.size());
                                                    res.add((Integer) ((Function<Pair<Integer, Integer>, Integer>) p -> p.getX() + p.getY()).apply(new Pair<>((Integer) ((Triple) x1).getElem().get(), (Integer) lAux.get(0))));
                                                    auxState[0] = new Triple(((Triple) x1).getStateA(), new Right(sub), Optional.empty());
                                                }

                                                return null;

                                            }).apply(((Right) x11).fromRight());

                                            return aux2;
                                        }

                                        return null;
                                    }).apply(((Triple) x1).getStateB());

                                    return aux1;
                                }
                            }).apply(x);

                            return aux;
                        }).apply(auxState[0]);
                    }

                    return res;
                };

        long start = System.currentTimeMillis();


        Function<Object, Step> nextTake = x -> {
            Pair<Integer, Object> p = (Pair) x;
            if (p.getX() == 0) {
                return new Done();
            } else {
                Step aux = ((Function<Object, Step>) x1 -> new Yield(x1, f1.apply((List<Integer>) x1))).apply(p.getY());

                if (aux instanceof Done) {
                    return new Done();
                } else if (aux instanceof Skip) {
                    return new Skip<>(new Pair<>(p.getX(), aux.state));
                } else if (aux instanceof Yield) {
                    return new Yield<>(aux.elem, new Pair<>(p.getX() - 1, aux.state));
                }
            }

            return null;
        };

        ArrayList<List<Integer>> res = new ArrayList<>();
        Object auxState = new Pair<>(2000, l);
        boolean over = false;

        while (!over) {
            Step step = nextTake.apply(auxState);

            if (step instanceof Done) {
                over = true;
            } else if (step instanceof Skip) {
                auxState = step.state;
            } else if (step instanceof Yield) {
                res.add((List<Integer>) step.elem);
                auxState = step.state;
            }
        }

        List<List<Integer>> res1 = res;


        System.out.println(System.currentTimeMillis() - start);

        print(res1, "res1.txt");
    }
}
