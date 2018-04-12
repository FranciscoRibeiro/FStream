package optimizations.optimizations_foldl_append;

import java.util.List;
import java.util.function.BiFunction;

public class NoFunctions extends MasterBenchmarkFoldlAppend {
    public static void main(String[] args) {
        System.out.println("NoFunctions...");
        /*ArrayList<Integer> xs = new ArrayList<>(Arrays.asList(new Integer[]{1, 2, 3, 4, 5}));
        ArrayList<Integer> ys = new ArrayList<>(Arrays.asList(new Integer[]{6, 7, 8, 9, 10}));*/

        NoFunctions nf = new NoFunctions();

        nf.populate();

        nf.warmUp();

        nf.measure();

        nf.end();
    }
    @Override
    public void work() {
        BiFunction<Long, Integer, Long> f = (a, b) -> a + b;


        Long value = (long) 0;
        List auxState = xs;
        boolean over = false;

        while (!over) {
            if (auxState.isEmpty()) {
                auxState = ys;
                over = true;
            } else {
                List<Integer> sub = auxState.subList(1, auxState.size());
                value = f.apply(value, (Integer) auxState.get(0));
                auxState = sub;
            }
        }

        over = false;
        while (!over){
            if (auxState.isEmpty()) {
                over = true;
            } else {
                List<Integer> sub = auxState.subList(1, auxState.size());
                value = f.apply(value, (Integer) auxState.get(0));
                auxState = sub;
            }
        }

        Long res = value;

        System.out.println(res);
    }
}
