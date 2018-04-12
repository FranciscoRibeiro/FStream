package experimental;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RefactoringTests1 {
    public static void main(String[] args) {
        List<Integer> l = Arrays.asList(1,2,3);
        ArrayList<String> res = l.stream().filter(i -> i <= 2)
                .map(i -> i + 1)
                .map(a -> a + "_word")
                .collect(Collectors.toCollection(ArrayList::new));

        System.out.println(res);
    }
}
