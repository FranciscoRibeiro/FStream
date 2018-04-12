package experimental;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class RefactoringTests2 {
    public static void main(String[] args) {
        Function<Integer, Long> f = x -> new Long(x);
        Function<Long, String> g = x -> String.valueOf(x);
        Predicate<Long> p1 = x -> x <= 2;
        Predicate<Long> p2 = x -> x%2 == 0;
        List<Integer> l = Arrays.asList(1,2,3,4,5);
        ArrayList<String> res = new ArrayList<>();
        for (Integer t : l) {
            String s = g.apply(f.apply(t) + 1) + "_word";
            res.add(s);
        }

        System.out.println(res);
    }
}
