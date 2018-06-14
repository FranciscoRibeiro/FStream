package demos.fibonacci;

import java.math.BigInteger;

public class FibonacciRecursive {
    private static BigInteger fib(int n){
        if(n <= 1){
            return BigInteger.valueOf(n);
        }
        else{
            return fib(n-1).add(fib(n-2));
        }
    }

    public static void main(String[] args) {
        System.out.println(fib(340));
    }
}
