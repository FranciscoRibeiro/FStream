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
import java.lang.invoke.MethodHandles;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class CaseOfCaseInMap extends MasterBenchmarkUnstreamIterate{
    public static void main(String[] args) {
        System.out.println(MethodHandles.lookup().lookupClass().getSimpleName() + "...");

        CaseOfCaseInMap ccm = new CaseOfCaseInMap();

        /*ccm.populate();

        ccm.warmUp();*/

        ccm.measure();

        ccm.end();
    }

    @Override
    public void work() {
        Function<List<BigInteger>, List<BigInteger>> f1 =
                row -> {

                    Function<Object, Step> nextMap = x -> {
                        Step aux = ((Function<Object, Step>) x1 -> {
                            if (!(((Triple) x1).getElem()).isPresent()) {
                                Step aux1 = ((Function<Object, Step>) x11 -> {
                                    if (x11 instanceof Left) {
                                        Step aux2 = ((Function<Object, Step>) x2 -> {
                                            List lAux = (List) x2;

                                            if (lAux.isEmpty()) {
                                                Step innerAux =  new Skip<>(new Triple(new Right(row), ((Triple) x1).getStateB(), Optional.empty()));

                                                if (innerAux instanceof Done) {
                                                    return new Done();
                                                } else if (innerAux instanceof Skip) {
                                                    return new Skip<>(innerAux.state);
                                                } else if (innerAux instanceof Yield) {
                                                    return new Yield<>(((Function<Pair<BigInteger, BigInteger>, BigInteger>) p -> p.getX().add(p.getY())).apply((Pair<BigInteger, BigInteger>) innerAux.elem), innerAux.state);
                                                }
                                            } else {
                                                List<BigInteger> sub = lAux.subList(1, lAux.size());
                                                Step innerAux = new Skip<>(new Triple(new Left(sub), ((Triple) x1).getStateB(), Optional.of((BigInteger) lAux.get(0))));

                                                if (innerAux instanceof Done) {
                                                    return new Done();
                                                } else if (innerAux instanceof Skip) {
                                                    return new Skip<>(innerAux.state);
                                                } else if (innerAux instanceof Yield) {
                                                    return new Yield<>(((Function<Pair<BigInteger, BigInteger>, BigInteger>) p -> p.getX().add(p.getY())).apply((Pair<BigInteger, BigInteger>) innerAux.elem), innerAux.state);
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
                                                    return new Done();
                                                } else if (innerAux instanceof Skip) {
                                                    return new Skip<>(innerAux.state);
                                                } else if (innerAux instanceof Yield) {
                                                    return new Yield<>(((Function<Pair<BigInteger, BigInteger>, BigInteger>) p -> p.getX().add(p.getY())).apply((Pair<BigInteger, BigInteger>) innerAux.elem), innerAux.state);
                                                }
                                            } else {
                                                List<BigInteger> sub = lAux.subList(1, lAux.size());
                                                Step innerAux = new Skip<>(new Triple(new Right(sub), ((Triple) x1).getStateB(), Optional.of((BigInteger) lAux.get(0))));

                                                if (innerAux instanceof Done) {
                                                    return new Done();
                                                } else if (innerAux instanceof Skip) {
                                                    return new Skip<>(innerAux.state);
                                                } else if (innerAux instanceof Yield) {
                                                    return new Yield<>(((Function<Pair<BigInteger, BigInteger>, BigInteger>) p -> p.getX().add(p.getY())).apply((Pair<BigInteger, BigInteger>) innerAux.elem), innerAux.state);
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
                                                    return new Done();
                                                } else if (innerAux instanceof Skip) {
                                                    return new Skip<>(innerAux.state);
                                                } else if (innerAux instanceof Yield) {
                                                    return new Yield<>(((Function<Pair<BigInteger, BigInteger>, BigInteger>) p -> p.getX().add(p.getY())).apply((Pair<BigInteger, BigInteger>) innerAux.elem), innerAux.state);
                                                }
                                            } else {
                                                List<BigInteger> sub = lAux.subList(1, lAux.size());
                                                Step innerAux = new Yield<>(new Pair<>(((Triple) x1).getElem().get(), (BigInteger) lAux.get(0)), new Triple(((Triple) x1).getStateA(), new Left(sub), Optional.empty()));

                                                if (innerAux instanceof Done) {
                                                    return new Done();
                                                } else if (innerAux instanceof Skip) {
                                                    return new Skip<>(innerAux.state);
                                                } else if (innerAux instanceof Yield) {
                                                    return new Yield<>(((Function<Pair<BigInteger, BigInteger>, BigInteger>) p -> p.getX().add(p.getY())).apply((Pair<BigInteger, BigInteger>) innerAux.elem), innerAux.state);
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
                                                    return new Done();
                                                } else if (innerAux instanceof Skip) {
                                                    return new Skip<>(innerAux.state);
                                                } else if (innerAux instanceof Yield) {
                                                    return new Yield<>(((Function<Pair<BigInteger, BigInteger>, BigInteger>) p -> p.getX().add(p.getY())).apply((Pair<BigInteger, BigInteger>) innerAux.elem), innerAux.state);
                                                }
                                            } else {
                                                List<BigInteger> sub = lAux.subList(1, lAux.size());
                                                Step innerAux = new Yield<>(new Pair<>(((Triple) x1).getElem().get(), (BigInteger) lAux.get(0)), new Triple(((Triple) x1).getStateA(), new Right(sub), Optional.empty()));

                                                if (innerAux instanceof Done) {
                                                    return new Done();
                                                } else if (innerAux instanceof Skip) {
                                                    return new Skip<>(innerAux.state);
                                                } else if (innerAux instanceof Yield) {
                                                    return new Yield<>(((Function<Pair<BigInteger, BigInteger>, BigInteger>) p -> p.getX().add(p.getY())).apply((Pair<BigInteger, BigInteger>) innerAux.elem), innerAux.state);
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
                    };

                    ArrayList<BigInteger> res = new ArrayList<>();
                    Object auxState = new Triple<>(new Left(Arrays.asList(BigInteger.ZERO)), new Left(row), Optional.empty());
                    boolean over = false;

                    while (!over) {
                        Step step = nextMap.apply(auxState);

                        if (step instanceof Done) {
                            over = true;
                        } else if (step instanceof Skip) {
                            auxState = step.state;
                        } else if (step instanceof Yield) {
                            res.add((BigInteger) step.elem);
                            auxState = step.state;
                        }
                    }

                    return res;
                };

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
        Object auxState = new Pair<>(NLINES, l);
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

        res1 = res;
    }
}
