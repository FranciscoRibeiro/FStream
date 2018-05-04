package experimental;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class FactorialTest {
    static int currentNumber = 5;

    public static int numbers(){
        return currentNumber--;
    }
    public static void main(String[] args) {
        //Alternative using generate
        Integer res = Stream.generate(FactorialTest::numbers).takeWhile(x -> x != 0).reduce(1, (x,y) -> x*y);
        System.out.println(res);

        //Alternative using range
        int acc = IntStream.range(1, 6).reduce(1, (a, b) -> a * b);
        Integer res1 = acc;
        System.out.println(res1);
    }
}
