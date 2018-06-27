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

public class FirstInline extends MasterBenchmarkUnstreamIterate{
    public static void main(String[] args) {
        System.out.println(MethodHandles.lookup().lookupClass().getSimpleName() + "...");

        FirstInline fi = new FirstInline();

        /*fi.populate();

        fi.warmUp();*/

        fi.measure();

        fi.end();
    }

    @Override
    public void work() {
        Function<List<BigInteger>,List<BigInteger>> f1 =
                row -> {
                    Function<Object, Step> nextStream = x -> {
                        List aux = (List) x;

                        if(aux.isEmpty()){
                            return new Done();
                        }
                        else{
                            List<BigInteger> sub = aux.subList(1, aux.size());
                            return new Yield<BigInteger, List<BigInteger>>((BigInteger) aux.get(0), sub);
                        }
                    };

                    Function<Object, Step> nextStream1 = x -> {
                        List aux = (List) x;

                        if(aux.isEmpty()){
                            return new Done();
                        }
                        else{
                            List<BigInteger> sub = aux.subList(1, aux.size());
                            return new Yield<BigInteger, List<BigInteger>>((BigInteger) aux.get(0), sub);
                        }
                    };

                    Function<Object, Step> nextStream2 = x -> {
                        List aux = (List) x;

                        if(aux.isEmpty()){
                            return new Done();
                        }
                        else{
                            List<BigInteger> sub = aux.subList(1, aux.size());
                            return new Yield<BigInteger, List<BigInteger>>((BigInteger) aux.get(0), sub);
                        }
                    };

                    Function<Object, Step> nextStream3 = x -> {
                        List aux = (List) x;

                        if(aux.isEmpty()){
                            return new Done();
                        }
                        else{
                            List<BigInteger> sub = aux.subList(1, aux.size());
                            return new Yield<BigInteger, List<BigInteger>>((BigInteger) aux.get(0), sub);
                        }
                    };

                    Function<Object, Step> nextAppend = x -> {
                        if(x instanceof Left){
                            Step aux = nextStream.apply(((Left) x).fromLeft());

                            if(aux instanceof Done){
                                return new Skip<Either>(new Right(row));
                            }
                            else if(aux instanceof Skip){
                                return new Skip<Either>(new Left(aux.state));
                            }
                            else if(aux instanceof Yield){
                                return new Yield<BigInteger, Either>((BigInteger) aux.elem, new Left(aux.state));
                            }
                        }
                        else if(x instanceof Right){
                            Step aux = nextStream1.apply(((Right) x).fromRight());

                            if(aux instanceof Done){
                                return new Done();
                            }
                            else if(aux instanceof Skip){
                                return new Skip<Either>(new Right(aux.state));
                            }
                            else if(aux instanceof Yield){
                                return new Yield<BigInteger, Either>((BigInteger) aux.elem, new Right(aux.state));
                            }
                        }

                        return null;
                    };

                    Function<Object, Step> nextAppend1 = x -> {
                        if(x instanceof Left){
                            Step aux = nextStream2.apply(((Left) x).fromLeft());

                            if(aux instanceof Done){
                                return new Skip<Either>(new Right(Arrays.asList(BigInteger.ZERO)));
                            }
                            else if(aux instanceof Skip){
                                return new Skip<Either>(new Left(aux.state));
                            }
                            else if(aux instanceof Yield){
                                return new Yield<BigInteger, Either>((BigInteger) aux.elem, new Left(aux.state));
                            }
                        }
                        else if(x instanceof Right){
                            Step aux = nextStream3.apply(((Right) x).fromRight());

                            if(aux instanceof Done){
                                return new Done();
                            }
                            else if(aux instanceof Skip){
                                return new Skip<Either>(new Right(aux.state));
                            }
                            else if(aux instanceof Yield){
                                return new Yield<BigInteger, Either>((BigInteger) aux.elem, new Right(aux.state));
                            }
                        }

                        return null;
                    };

                    Function<Object, Step> nextZip = x -> {
                        if(!(((Triple) x).getElem()).isPresent()){
                            Step aux = nextAppend.apply(((Triple) x).getStateA());

                            if(aux instanceof Done){
                                return new Done();
                            }
                            else if(aux instanceof Skip){
                                return new Skip<>(new Triple(aux.state, ((Triple) x).getStateB(), Optional.empty()));
                            }
                            else if(aux instanceof Yield){
                                return new Skip<>(new Triple(aux.state, ((Triple) x).getStateB(), Optional.of(aux.elem)));
                            }
                        }
                        else{ //There is a value present in Optional
                            Step aux = nextAppend1.apply(((Triple) x).getStateB());

                            if(aux instanceof Done){
                                return new Done();
                            }
                            else if(aux instanceof Skip){
                                return new Skip<>(new Triple(((Triple) x).getStateA(), aux.state, ((Triple) x).getElem()));
                            }
                            else if(aux instanceof Yield){
                                return new Yield<>(new Pair<>(((Triple) x).getElem().get(), aux.elem), new Triple(((Triple) x).getStateA(), aux.state, Optional.empty()));
                            }
                        }

                        return null;
                    };

                    Function<Object, Step> nextMap = x -> {
                        Step aux = nextZip.apply(x);

                        if(aux instanceof Done){
                            return new Done();
                        }
                        else if(aux instanceof Skip){
                            return new Skip<>(aux.state);
                        }
                        else if(aux instanceof Yield){
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
            Pair<Integer,Object> p = (Pair) x;
            if(p.getX() == 0){
                return new Done();
            }
            else{
                Step aux = nextIterate.apply(p.getY());

                if(aux instanceof Done){
                    return new Done();
                }
                else if(aux instanceof Skip){
                    return new Skip<>(new Pair<>(p.getX(), aux.state));
                }
                else if(aux instanceof Yield){
                    return new Yield<>(aux.elem, new Pair<>(p.getX()-1, aux.state));
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
