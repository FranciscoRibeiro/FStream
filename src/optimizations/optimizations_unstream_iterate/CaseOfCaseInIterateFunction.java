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
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class CaseOfCaseInIterateFunction {
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
                    final Object[] auxState = {new Triple<>(new Left(Arrays.asList(BigInteger.ZERO)), new Left(row), Optional.empty())};
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
                                                    Step innerAux = new Skip<>(new Triple(new Right(row), ((Triple) x1).getStateB(), Optional.empty()));

                                                    if (innerAux instanceof Done) {
                                                        over[0] = true;
                                                    } else if (innerAux instanceof Skip) {
                                                        auxState[0] = innerAux.state;
                                                    } else if (innerAux instanceof Yield) {
                                                        res.add((BigInteger) innerAux.elem);
                                                        auxState[0] = innerAux.state;
                                                    }
                                                } else {
                                                    List<BigInteger> sub = lAux.subList(1, lAux.size());
                                                    Step innerAux = new Skip<>(new Triple(new Left(sub), ((Triple) x1).getStateB(), Optional.of((BigInteger) lAux.get(0))));

                                                    if (innerAux instanceof Done) {
                                                        over[0] = true;
                                                    } else if (innerAux instanceof Skip) {
                                                        auxState[0] = innerAux.state;
                                                    } else if (innerAux instanceof Yield) {
                                                        res.add((BigInteger) innerAux.elem);
                                                        auxState[0] = innerAux.state;
                                                    }
                                                }

                                                return null;

                                            }).apply(((Left) x11).fromLeft());

                                            return aux2;
                                        } else if (x11 instanceof Right) {
                                            Step aux2 = ((Function<Object, Step>) x2 -> {
                                                List lAux = (List) x2;

                                                if (lAux.isEmpty()) {
                                                    Step innerAux = new Done();

                                                    if (innerAux instanceof Done) {
                                                        over[0] = true;
                                                    } else if (innerAux instanceof Skip) {
                                                        auxState[0] = innerAux.state;
                                                    } else if (innerAux instanceof Yield) {
                                                        res.add((BigInteger) innerAux.elem);
                                                        auxState[0] = innerAux.state;
                                                    }
                                                } else {
                                                    List<BigInteger> sub = lAux.subList(1, lAux.size());
                                                    Step innerAux = new Skip<>(new Triple(new Right(sub), ((Triple) x1).getStateB(), Optional.of((BigInteger) lAux.get(0))));

                                                    if (innerAux instanceof Done) {
                                                        over[0] = true;
                                                    } else if (innerAux instanceof Skip) {
                                                        auxState[0] = innerAux.state;
                                                    } else if (innerAux instanceof Yield) {
                                                        res.add((BigInteger) innerAux.elem);
                                                        auxState[0] = innerAux.state;
                                                    }
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
                                                    Step innerAux = new Skip<>(new Triple(((Triple) x1).getStateA(), new Right(Arrays.asList(BigInteger.ZERO)), ((Triple) x1).getElem()));

                                                    if (innerAux instanceof Done) {
                                                        over[0] = true;
                                                    } else if (innerAux instanceof Skip) {
                                                        auxState[0] = innerAux.state;
                                                    } else if (innerAux instanceof Yield) {
                                                        res.add((BigInteger) innerAux.elem);
                                                        auxState[0] = innerAux.state;
                                                    }
                                                } else {
                                                    List<BigInteger> sub = lAux.subList(1, lAux.size());
                                                    Step innerAux = new Yield<>(((Function<Pair<BigInteger, BigInteger>, BigInteger>) p -> p.getX().add(p.getY())).apply(new Pair<>((BigInteger) ((Triple) x1).getElem().get(), (BigInteger) lAux.get(0))), new Triple(((Triple) x1).getStateA(), new Left(sub), Optional.empty()));

                                                    if (innerAux instanceof Done) {
                                                        over[0] = true;
                                                    } else if (innerAux instanceof Skip) {
                                                        auxState[0] = innerAux.state;
                                                    } else if (innerAux instanceof Yield) {
                                                        res.add((BigInteger) innerAux.elem);
                                                        auxState[0] = innerAux.state;
                                                    }
                                                }

                                                return null;

                                            }).apply(((Left) x11).fromLeft());

                                            return aux2;
                                        } else if (x11 instanceof Right) {
                                            Step aux2 = ((Function<Object, Step>) x2 -> {
                                                List lAux = (List) x2;

                                                if (lAux.isEmpty()) {
                                                    Step innerAux = new Done();

                                                    if (innerAux instanceof Done) {
                                                        over[0] = true;
                                                    } else if (innerAux instanceof Skip) {
                                                        auxState[0] = innerAux.state;
                                                    } else if (innerAux instanceof Yield) {
                                                        res.add((BigInteger) innerAux.elem);
                                                        auxState[0] = innerAux.state;
                                                    }
                                                } else {
                                                    List<BigInteger> sub = lAux.subList(1, lAux.size());
                                                    Step innerAux = new Yield<>(((Function<Pair<BigInteger, BigInteger>, BigInteger>) p -> p.getX().add(p.getY())).apply(new Pair<>((BigInteger) ((Triple) x1).getElem().get(), (BigInteger) lAux.get(0))), new Triple(((Triple) x1).getStateA(), new Right(sub), Optional.empty()));

                                                    if (innerAux instanceof Done) {
                                                        over[0] = true;
                                                    } else if (innerAux instanceof Skip) {
                                                        auxState[0] = innerAux.state;
                                                    } else if (innerAux instanceof Yield) {
                                                        res.add((BigInteger) innerAux.elem);
                                                        auxState[0] = innerAux.state;
                                                    }
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


        Function<Object, Step> nextIterate = x -> new Yield(x, f1.apply((List<BigInteger>) x));

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

        ArrayList<List<BigInteger>> res = new ArrayList<>();
        Object auxState = new Pair<>(2000, l);
        boolean over = false;

        while (!over) {
            Step step = nextTake.apply(auxState);

            if (step instanceof Done) {
                over = true;
            } else if (step instanceof Skip) {
                auxState = step.state;
            } else if (step instanceof Yield) {
                res.add((List<BigInteger>) step.elem);
                auxState = step.state;
            }
        }

        List<List<BigInteger>> res1 = res;


        System.out.println(System.currentTimeMillis() - start);

        print(res1, "res1.txt");
    }
}
