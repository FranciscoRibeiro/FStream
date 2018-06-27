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

public class InlineStreamIntoAppend {
    public static void print(List<List<Integer>> l, String fileName){
        FileWriter fw = null;
        try {
            fw = new FileWriter(fileName);

            for(List<Integer> li: l){
                for(Integer i: li){
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

        Function<List<Integer>,List<Integer>> f1 =
                row -> {

                    Function<Object, Step> nextAppend = x -> {
                        if(x instanceof Left){
                            Step aux = ((Function<Object, Step>) x1 -> {
                                List aux1 = (List) x1;

                                if (aux1.isEmpty()) {
                                    return new Done();
                                } else {
                                    List<Integer> sub = aux1.subList(1, aux1.size());
                                    return new Yield<Integer, List<Integer>>((Integer) aux1.get(0), sub);
                                }
                            }).apply(((Left) x).fromLeft());

                            if(aux instanceof Done){
                                return new Skip<Either>(new Right(row));
                            }
                            else if(aux instanceof Skip){
                                return new Skip<Either>(new Left(aux.state));
                            }
                            else if(aux instanceof Yield){
                                return new Yield<Integer, Either>((Integer) aux.elem, new Left(aux.state));
                            }
                        }
                        else if(x instanceof Right){
                            Step aux = ((Function<Object, Step>) x1 -> {
                                List aux1 = (List) x1;

                                if (aux1.isEmpty()) {
                                    return new Done();
                                } else {
                                    List<Integer> sub = aux1.subList(1, aux1.size());
                                    return new Yield<Integer, List<Integer>>((Integer) aux1.get(0), sub);
                                }
                            }).apply(((Right) x).fromRight());

                            if(aux instanceof Done){
                                return new Done();
                            }
                            else if(aux instanceof Skip){
                                return new Skip<Either>(new Right(aux.state));
                            }
                            else if(aux instanceof Yield){
                                return new Yield<Integer, Either>((Integer) aux.elem, new Right(aux.state));
                            }
                        }

                        return null;
                    };

                    Function<Object, Step> nextAppend1 = x -> {
                        if(x instanceof Left){
                            Step aux = ((Function<Object, Step>) x1 -> {
                                List aux1 = (List) x1;

                                if (aux1.isEmpty()) {
                                    return new Done();
                                } else {
                                    List<Integer> sub = aux1.subList(1, aux1.size());
                                    return new Yield<Integer, List<Integer>>((Integer) aux1.get(0), sub);
                                }
                            }).apply(((Left) x).fromLeft());

                            if(aux instanceof Done){
                                return new Skip<Either>(new Right(Arrays.asList(0)));
                            }
                            else if(aux instanceof Skip){
                                return new Skip<Either>(new Left(aux.state));
                            }
                            else if(aux instanceof Yield){
                                return new Yield<Integer, Either>((Integer) aux.elem, new Left(aux.state));
                            }
                        }
                        else if(x instanceof Right){
                            Step aux = ((Function<Object, Step>) x1 -> {
                                List aux1 = (List) x1;

                                if (aux1.isEmpty()) {
                                    return new Done();
                                } else {
                                    List<Integer> sub = aux1.subList(1, aux1.size());
                                    return new Yield<Integer, List<Integer>>((Integer) aux1.get(0), sub);
                                }
                            }).apply(((Right) x).fromRight());

                            if(aux instanceof Done){
                                return new Done();
                            }
                            else if(aux instanceof Skip){
                                return new Skip<Either>(new Right(aux.state));
                            }
                            else if(aux instanceof Yield){
                                return new Yield<Integer, Either>((Integer) aux.elem, new Right(aux.state));
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
