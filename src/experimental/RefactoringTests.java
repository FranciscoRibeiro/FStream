package experimental;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RefactoringTests {
    public static void main(String[] args) {
        //List<Integer> res = Stream.generate(() -> 1).filter(x -> x >= 2).limit(3).skip(2).collect(Collectors.toList());
        List<String> l = Arrays.asList("1","2","3");
        final int[] i = {0};
        List<String> res = l.stream()
                .map(s -> s + "hey")
                .map(s -> {
                    if(i[0] == 0){
                        i[0]++;
                        return s + "!";
                    }
                    else{
                        i[0]++;
                        return s + "!!";
                    }
                })
                .collect(Collectors.toList());

        System.out.println(res);
    }
}
