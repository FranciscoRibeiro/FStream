package demos.fibonacci;

import java.lang.invoke.MethodHandles;
import java.math.BigInteger;

public class FibonacciIterative extends MasterBenchmarkFibonacci{
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
        System.out.println(MethodHandles.lookup().lookupClass().getSimpleName() + "...");

        FibonacciIterative fibI = new FibonacciIterative();

        /*fibI.populate();

        fibI.warmUp();*/

        fibI.measure();

        fibI.end();
    }

    @Override
    public void work() {
        BigInteger fib = fib(100000);
        System.out.println(fib);
    }
}
