package optimizations.optimizations3;

import datatypes.*;
import util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class FirstInline {
    public static void main(String[] args) {
        ArrayList<Integer> lInts = new ArrayList<>(Arrays.asList(new Integer[]{1, 2, 3}));


        Function<Object, Step> stepper1 = x -> {
            Optional opAux = (Optional) ((Pair) x).getY();

            if(!(opAux.isPresent())){
                Step aux = ((Function<Object, Step>) x1 -> {
                    List aux1 = (List) x1;

                    if (aux1.isEmpty()) {
                        return new Done();
                    } else {
                        List<Integer> sub = aux1.subList(1, aux1.size());
                        Yield y = new Yield<Integer, List<Integer>>((Integer) aux1.get(0), sub);
                        return y;
                    }
                }).apply(((Pair) x).getX());

                if(aux instanceof Done){
                    return new Done();
                }
                else if(aux instanceof Skip){
                    return new Skip(new Pair<>(aux.state, Optional.empty()));
                }
                else if(aux instanceof Yield){
                    return new Skip(new Pair<>(aux.state, Optional.of(((Function<Integer, FStream<Object>>) FStream::until).apply((Integer) aux.elem))));
                }
            }
            else{
                FStream fAux = (FStream) opAux.get();
                Step aux = (Step) fAux.getStepper().apply(fAux.getState());

                if(aux instanceof Done){
                    return new Skip(new Pair(((Pair) x).getX(), Optional.empty()));
                }
                else if(aux instanceof Skip){
                    return new Skip(new Pair(((Pair) x).getX(), Optional.of(new FStream<>(fAux.getStepper(), aux.state))));
                }
                else if(aux instanceof Yield){
                    return new Yield(aux.elem, new Pair(((Pair) x).getX(), Optional.of(new FStream<>(fAux.getStepper(), aux.state))));
                }
            }

            return null;
        };

        ArrayList<Object> res1 = new ArrayList<>();
        Object auxState = new Pair(lInts, Optional.empty());

        while (true) {
            Step step = stepper1.apply(auxState);

            if (step instanceof Done) {
                break;
            } else if (step instanceof Skip) {
                auxState = step.state;
                continue;
            } else if (step instanceof Yield) {
                res1.add((Object) step.elem);
                auxState = step.state;
                continue;
            }
        }

        List res = res1;


        System.out.println(res);
    }
}
