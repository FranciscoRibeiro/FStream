package demos.fibonacci;

import java.math.BigInteger;

public class FibonacciIterative {
    private static BigInteger fib(int n){
        if(n <= 1){
            return BigInteger.valueOf(n);
        }
        BigInteger fib = BigInteger.ONE;
        BigInteger previous = BigInteger.ONE;

        for(int i = 2; i < n; i++){
            BigInteger aux = fib;
            fib = fib.add(previous);
            previous = aux;
        }

        return fib;
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        System.out.println(fib(100000));
        System.out.println("Time taken: " + (System.currentTimeMillis() - start));
    }
}
