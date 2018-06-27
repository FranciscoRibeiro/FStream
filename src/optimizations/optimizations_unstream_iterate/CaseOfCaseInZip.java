package optimizations.optimizations_unstream_iterate;

import datatypes.Done;
import datatypes.Skip;
import datatypes.Step;
import datatypes.Yield;
import util.*;

import java.lang.invoke.MethodHandles;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class CaseOfCaseInZip extends MasterBenchmarkUnstreamIterate{
    public static void main(String[] args) {
        System.out.println(MethodHandles.lookup().lookupClass().getSimpleName() + "...");

        CaseOfCaseInZip ccz = new CaseOfCaseInZip();

        /*ccz.populate();

        ccz.warmUp();*/

        ccz.measure();

        ccz.end();
    }

    @Override
    public void work() {
        Function<List<BigInteger>, List<BigInteger>> f1 =
                row -> {

                    Function<Object, Step> nextZip = x -> {
                        if (!(((Triple) x).getElem()).isPresent()) {
                            Step aux = ((Function<Object, Step>) x1 -> {
                                if (x1 instanceof Left) {
                                    Step aux1 = ((Function<Object, Step>) x2 -> {
                                        List lAux = (List) x2;

                                        if (lAux.isEmpty()) {
                                            Step innerAux = new Skip<Either>(new Right(row));

                                            if (innerAux instanceof Done) {
                                                return new Done();
                                            } else if (innerAux instanceof Skip) {
                                                return new Skip<>(new Triple(innerAux.state, ((Triple) x).getStateB(), Optional.empty()));
                                            } else if (innerAux instanceof Yield) {
                                                return new Skip<>(new Triple(innerAux.state, ((Triple) x).getStateB(), Optional.of(innerAux.elem)));
                                            }
                                        } else {
                                            List<BigInteger> sub = lAux.subList(1, lAux.size());
                                            Step innerAux = new Yield<BigInteger, Either>((BigInteger) lAux.get(0), new Left(sub));

                                            if (innerAux instanceof Done) {
                                                return new Done();
                                            } else if (innerAux instanceof Skip) {
                                                return new Skip<>(new Triple(innerAux.state, ((Triple) x).getStateB(), Optional.empty()));
                                            } else if (innerAux instanceof Yield) {
                                                return new Skip<>(new Triple(innerAux.state, ((Triple) x).getStateB(), Optional.of(innerAux.elem)));
                                            }
                                        }

                                        return null;
                                    }).apply(((Left) x1).fromLeft());

                                    return aux1;
                                } else if (x1 instanceof Right) {
                                    Step aux1 = ((Function<Object, Step>) x2 -> {
                                        List lAux = (List) x2;

                                        if (lAux.isEmpty()) {
                                            Step innerAux = new Done();

                                            if (innerAux instanceof Done) {
                                                return new Done();
                                            } else if (innerAux instanceof Skip) {
                                                return new Skip<>(new Triple(innerAux.state, ((Triple) x).getStateB(), Optional.empty()));
                                            } else if (innerAux instanceof Yield) {
                                                return new Skip<>(new Triple(innerAux.state, ((Triple) x).getStateB(), Optional.of(innerAux.elem)));
                                            }
                                        } else {
                                            List<BigInteger> sub = lAux.subList(1, lAux.size());
                                            Step innerAux = new Yield<BigInteger, Either>((BigInteger) lAux.get(0), new Right(sub));

                                            if (innerAux instanceof Done) {
                                                return new Done();
                                            } else if (innerAux instanceof Skip) {
                                                return new Skip<>(new Triple(innerAux.state, ((Triple) x).getStateB(), Optional.empty()));
                                            } else if (innerAux instanceof Yield) {
                                                return new Skip<>(new Triple(innerAux.state, ((Triple) x).getStateB(), Optional.of(innerAux.elem)));
                                            }
                                        }

                                        return null;

                                    }).apply(((Right) x1).fromRight());

                                    return aux1;
                                }

                                return null;
                            }).apply(((Triple) x).getStateA());

                            return aux;
                        } else { //There is a value present in Optional
                            Step aux = ((Function<Object, Step>) x1 -> {
                                if (x1 instanceof Left) {
                                    Step aux1 = ((Function<Object, Step>) x2 -> {
                                        List lAux = (List) x2;

                                        if (lAux.isEmpty()) {
                                            Step innerAux = new Skip<Either>(new Right(Arrays.asList(BigInteger.ZERO)));

                                            if (innerAux instanceof Done) {
                                                return new Done();
                                            } else if (innerAux instanceof Skip) {
                                                return new Skip<>(new Triple(((Triple) x).getStateA(), innerAux.state, ((Triple) x).getElem()));
                                            } else if (innerAux instanceof Yield) {
                                                return new Yield<>(new Pair<>(((Triple) x).getElem().get(), innerAux.elem), new Triple(((Triple) x).getStateA(), innerAux.state, Optional.empty()));
                                            }
                                        } else {
                                            List<BigInteger> sub = lAux.subList(1, lAux.size());
                                            Step innerAux = new Yield<BigInteger, Either>((BigInteger) lAux.get(0), new Left(sub));

                                            if (innerAux instanceof Done) {
                                                return new Done();
                                            } else if (innerAux instanceof Skip) {
                                                return new Skip<>(new Triple(((Triple) x).getStateA(), innerAux.state, ((Triple) x).getElem()));
                                            } else if (innerAux instanceof Yield) {
                                                return new Yield<>(new Pair<>(((Triple) x).getElem().get(), innerAux.elem), new Triple(((Triple) x).getStateA(), innerAux.state, Optional.empty()));
                                            }
                                        }

                                        return null;

                                    }).apply(((Left) x1).fromLeft());

                                    return aux1;
                                } else if (x1 instanceof Right) {
                                    Step aux1 = ((Function<Object, Step>) x2 -> {
                                        List lAux = (List) x2;

                                        if (lAux.isEmpty()) {
                                            Step innerAux = new Done();

                                            if (innerAux instanceof Done) {
                                                return new Done();
                                            } else if (innerAux instanceof Skip) {
                                                return new Skip<>(new Triple(((Triple) x).getStateA(), innerAux.state, ((Triple) x).getElem()));
                                            } else if (innerAux instanceof Yield) {
                                                return new Yield<>(new Pair<>(((Triple) x).getElem().get(), innerAux.elem), new Triple(((Triple) x).getStateA(), innerAux.state, Optional.empty()));
                                            }
                                        } else {
                                            List<BigInteger> sub = lAux.subList(1, lAux.size());
                                            Step innerAux = new Yield<BigInteger, Either>((BigInteger) lAux.get(0), new Right(sub));

                                            if (innerAux instanceof Done) {
                                                return new Done();
                                            } else if (innerAux instanceof Skip) {
                                                return new Skip<>(new Triple(((Triple) x).getStateA(), innerAux.state, ((Triple) x).getElem()));
                                            } else if (innerAux instanceof Yield) {
                                                return new Yield<>(new Pair<>(((Triple) x).getElem().get(), innerAux.elem), new Triple(((Triple) x).getStateA(), innerAux.state, Optional.empty()));
                                            }
                                        }

                                        return null;

                                    }).apply(((Right) x1).fromRight());

                                    return aux1;
                                }

                                return null;
                            }).apply(((Triple) x).getStateB());

                            return aux;
                        }
                    };

                    Function<Object, Step> nextMap = x -> {
                        Step aux = nextZip.apply(x);

                        if (aux instanceof Done) {
                            return new Done();
                        } else if (aux instanceof Skip) {
                            return new Skip<>(aux.state);
                        } else if (aux instanceof Yield) {
                            return new Yield<>(((Function<Pair<BigInteger, BigInteger>, BigInteger>) p -> p.getX().add(p.getY())).apply((Pair<BigInteger, BigInteger>) aux.elem), aux.state);
                        }

                        return null;
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
