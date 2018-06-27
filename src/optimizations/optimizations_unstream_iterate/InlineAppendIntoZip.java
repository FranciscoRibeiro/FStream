package optimizations.optimizations_unstream_iterate;

import datatypes.Done;
import datatypes.Skip;
import datatypes.Step;
import datatypes.Yield;
import util.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class InlineAppendIntoZip {
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

                    Function<Object, Step> nextZip = x -> {
                        if (!(((Triple) x).getElem()).isPresent()) {
                            Step aux = ((Function<Object, Step>) x1 -> {
                                if (x1 instanceof Left) {
                                    Step aux1 = ((Function<Object, Step>) x2 -> {
                                        List lAux = (List) x2;

                                        if (lAux.isEmpty()) {
                                            return new Skip<Either>(new Right(row));
                                        } else {
                                            List<Integer> sub = lAux.subList(1, lAux.size());
                                            return new Yield<Integer, Either>((Integer) lAux.get(0), new Left(sub));
                                        }
                                    }).apply(((Left) x1).fromLeft());

                                    return aux1;
                                } else if (x1 instanceof Right) {
                                    Step aux1 = ((Function<Object, Step>) x2 -> {
                                        List lAux = (List) x2;

                                        if (lAux.isEmpty()) {
                                            return new Done();
                                        } else {
                                            List<Integer> sub = lAux.subList(1, lAux.size());
                                            return new Yield<Integer, Either>((Integer) lAux.get(0), new Right(sub));
                                        }

                                    }).apply(((Right) x1).fromRight());

                                    return aux1;
                                }

                                return null;
                            }).apply(((Triple) x).getStateA());

                            if (aux instanceof Done) {
                                return new Done();
                            } else if (aux instanceof Skip) {
                                return new Skip<>(new Triple(aux.state, ((Triple) x).getStateB(), Optional.empty()));
                            } else if (aux instanceof Yield) {
                                return new Skip<>(new Triple(aux.state, ((Triple) x).getStateB(), Optional.of(aux.elem)));
                            }
                        } else { //There is a value present in Optional
                            Step aux = ((Function<Object, Step>) x1 -> {
                                if (x1 instanceof Left) {
                                    Step aux1 = ((Function<Object, Step>) x2 -> {
                                        List lAux = (List) x2;

                                        if (lAux.isEmpty()) {
                                            return new Skip<Either>(new Right(Arrays.asList(0)));
                                        } else {
                                            List<Integer> sub = lAux.subList(1, lAux.size());
                                            return new Yield<Integer, Either>((Integer) lAux.get(0), new Left(sub));
                                        }

                                    }).apply(((Left) x1).fromLeft());

                                    return aux1;
                                } else if (x1 instanceof Right) {
                                    Step aux1 = ((Function<Object, Step>) x2 -> {
                                        List lAux = (List) x2;

                                        if (lAux.isEmpty()) {
                                            return new Done();
                                        } else {
                                            List<Integer> sub = lAux.subList(1, lAux.size());
                                            return new Yield<Integer, Either>((Integer) lAux.get(0), new Right(sub));
                                        }

                                    }).apply(((Right) x1).fromRight());

                                    return aux1;
                                }

                                return null;
                            }).apply(((Triple) x).getStateB());

                            if (aux instanceof Done) {
                                return new Done();
                            } else if (aux instanceof Skip) {
                                return new Skip<>(new Triple(((Triple) x).getStateA(), aux.state, ((Triple) x).getElem()));
                            } else if (aux instanceof Yield) {
                                return new Yield<>(new Pair<>(((Triple) x).getElem().get(), aux.elem), new Triple(((Triple) x).getStateA(), aux.state, Optional.empty()));
                            }
                        }

                        return null;
                    };

                    Function<Object, Step> nextMap = x -> {
                        Step aux = nextZip.apply(x);

                        if (aux instanceof Done) {
                            return new Done();
                        } else if (aux instanceof Skip) {
                            return new Skip<>(aux.state);
                        } else if (aux instanceof Yield) {
                            return new Yield<>(((Function<Pair<Integer, Integer>, Integer>) p -> p.getX() + p.getY()).apply((Pair<Integer, Integer>) aux.elem), aux.state);
                        }

                        return null;
                    };

                    ArrayList<Integer> res = new ArrayList<>();
                    Object auxState = new Triple<>(new Left(Arrays.asList(0)), new Left(row), Optional.empty());
                    boolean over = false;

                    while (!over) {
                        Step step = nextMap.apply(auxState);

                        if (step instanceof Done) {
                            over = true;
                        } else if (step instanceof Skip) {
                            auxState = step.state;
                        } else if (step instanceof Yield) {
                            res.add((Integer) step.elem);
                            auxState = step.state;
                        }
                    }

                    return res;
                };

        long start = System.currentTimeMillis();


        Function<Object, Step> nextIterate = x -> new Yield(x, f1.apply((List<Integer>) x));

        Function<Object, Step> nextTake = x -> {
            Pair<Integer, Object> p = (Pair) x;
            if (p.getX() == 0) {
                return new Done();
            } else {
                Step aux = nextIterate.apply(p.getY());

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
