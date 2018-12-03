package demos.fibonacci;

import util.Pair;

import java.lang.invoke.MethodHandles;
import java.math.BigInteger;

public class FibonacciTuple extends MasterBenchmarkFibonacci{

    private static BigInteger fib(int n){
        //return second element of tuple
        return fibAux(n).getY();
    }

    private static Pair<BigInteger,BigInteger> fibAux(int n){
        if(n == 1 || n == 2){
            return new Pair<>(BigInteger.valueOf(1), BigInteger.valueOf(1));
        }
        else{
            Pair<BigInteger, BigInteger> p = fibAux(n-1);
            BigInteger n2 = p.getX();
            BigInteger n1 = p.getY();
            return new Pair<>(n1, n2.add(n1));
        }
    }

    public static void main(String[] args) {
        System.out.println(MethodHandles.lookup().lookupClass().getSimpleName() + "...");

        FibonacciTuple fibT = new FibonacciTuple();

        /*fibT.populate();

        fibT.warmUp();*/

        fibT.measure();

        fibT.end();
    }

    @Override
    public void work() {
        BigInteger fib = fib(700000);
        System.out.println(fib);
    }
}
