package optimizations.optimizations_foldl_append;

import datatypes.Step;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ConstructorSpecialisation extends MasterBenchmarkFoldlAppend {

    public static void main(String[] args) {
        System.out.println("ConstructorSpecialisation...");
        /*ArrayList<Integer> xs = new ArrayList<>(Arrays.asList(new Integer[]{1, 2, 3, 4, 5}));
        ArrayList<Integer> ys = new ArrayList<>(Arrays.asList(new Integer[]{6, 7, 8, 9, 10}));*/

        ConstructorSpecialisation cs = new ConstructorSpecialisation();

        cs.populate();

        cs.warmUp();

        cs.measure();

        cs.end();
    }

    @Override
    public void work() {
        BiFunction<Long, Integer, Long> f = (a, b) -> a + b;


        final Long[] value = {(long) 0};
        final Object[] auxState = {xs};
        final boolean[] over = {false};

        while (!over[0]) {
            Step aux = ((Function<Object, Step>) x1 -> {
                List aux1 = (List) x1;

                if (aux1.isEmpty()) {
                    auxState[0] = ys;
                    over[0] = true;
                } else {
                    List<Integer> sub = aux1.subList(1, aux1.size());
                    auxState[0] = sub;
                    value[0] = f.apply(value[0], (Integer) aux1.get(0));
                }

                return null;
            }).apply(auxState[0]);
        }

        over[0] = false;
        while (!over[0]){
            Step aux = ((Function<Object, Step>) x1 -> {
                List aux1 = (List) x1;

                if (aux1.isEmpty()) {
                    over[0] = true;
                } else {
                    List<Integer> sub = aux1.subList(1, aux1.size());
                    auxState[0] = sub;
                    value[0] = f.apply(value[0], (Integer) aux1.get(0));
                }

                return null;
            }).apply(auxState[0]);
        }

        Long res = value[0];

        System.out.println(res);
    }
}
