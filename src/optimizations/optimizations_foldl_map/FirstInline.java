package optimizations.optimizations_foldl_map;

import datatypes.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class FirstInline {
    public static void main(String[] args) {
        ArrayList<Integer> ints = new ArrayList<>(Arrays.asList(3,4,5,6,5,7,8,9,0));

        Function<Integer, Boolean> p = x -> x > 2;

        Function<Object, Step> nextStream = x -> {
            List aux = (List) x;

            if(aux.isEmpty()){
                return new Done();
            }
            else{
                List<Integer> sub = aux.subList(1, aux.size());
                return new Yield<Integer, List<Integer>>((Integer) aux.get(0), sub);
            }
        };

        Function<Object, Step> nextMap = x -> {
            Step aux = new FStream<Integer>(nextStream, ints).stepper.apply(x);

            if(aux instanceof Done){
                return new Done();
            }
            else if(aux instanceof Skip){
                return new Skip<>(aux.state);
            }
            else if(aux instanceof Yield){
                return new Yield<>(p.apply((Integer) aux.elem), aux.state);
            }

            return null;
        };

        Boolean value = true;
        Object auxState = new FStream<Boolean>(nextMap, new FStream<Integer>(nextStream, ints).state).state;
        boolean over = false;

        while (!over) {
            Step step = new FStream<Boolean>(nextMap, new FStream<Integer>(nextStream, ints).state).stepper.apply(auxState);

            if (step instanceof Done) {
                over = true;
            } else if (step instanceof Skip) {
                auxState = step.state;
            } else if (step instanceof Yield) {
                value = ((BiFunction<Boolean, Boolean, Boolean>) (x, y) -> x && y).apply(value, (Boolean) step.elem);
                auxState = step.state;
            }
        }

        boolean res = value;

        System.out.println(res);
    }
}
