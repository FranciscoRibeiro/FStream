package demos.matrices;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

public class Multiply {
    public static Jama.Matrix magic (int n) {

        double[][] M = new double[n][n];

        // Odd order

        if ((n % 2) == 1) {
            int a = (n+1)/2;
            int b = (n+1);
            for (int j = 0; j < n; j++) {
                for (int i = 0; i < n; i++) {
                    M[i][j] = n*((i+j+a) % n) + ((i+2*j+b) % n) + 1;
                }
            }

            // Doubly Even Order

        } else if ((n % 4) == 0) {
            for (int j = 0; j < n; j++) {
                for (int i = 0; i < n; i++) {
                    if (((i+1)/2)%2 == ((j+1)/2)%2) {
                        M[i][j] = n*n-n*i-j;
                    } else {
                        M[i][j] = n*i+j+1;
                    }
                }
            }

            // Singly Even Order

        } else {
            int p = n/2;
            int k = (n-2)/4;
            Jama.Matrix A = magic(p);
            for (int j = 0; j < p; j++) {
                for (int i = 0; i < p; i++) {
                    double aij = A.get(i,j);
                    M[i][j] = aij;
                    M[i][j+p] = aij + 2*p*p;
                    M[i+p][j] = aij + 3*p*p;
                    M[i+p][j+p] = aij + p*p;
                }
            }
            for (int i = 0; i < p; i++) {
                for (int j = 0; j < k; j++) {
                    double t = M[i][j]; M[i][j] = M[i+p][j]; M[i+p][j] = t;
                }
                for (int j = n-k+1; j < n; j++) {
                    double t = M[i][j]; M[i][j] = M[i+p][j]; M[i+p][j] = t;
                }
            }
            double t = M[k][0]; M[k][0] = M[k+p][0]; M[k+p][0] = t;
            t = M[k][k]; M[k][k] = M[k+p][k]; M[k+p][k] = t;
        }
        return new Jama.Matrix(M);
    }

    public static void main(String[] args) {
        List<Jama.Matrix> mList = new ArrayList<Jama.Matrix>();



        List auxState = mList;
        boolean foundFirst = false;
        boolean over = false;
        Jama.Matrix value = null;

        while (!foundFirst) {
                if (auxState.isEmpty()) {
                    over = foundFirst = true;
                } else {
                    List<Jama.Matrix> sub = auxState.subList(1, auxState.size());

                    foundFirst = true;
                    value = (Jama.Matrix) auxState.get(0);
                    auxState = sub;
                }
        }

        while (!over) {
                if (auxState.isEmpty()) {
                    over = true;
                } else {
                    List<Jama.Matrix> sub = auxState.subList(1, auxState.size());

                    value = ((BiFunction<Jama.Matrix, Jama.Matrix, Jama.Matrix>) (a, b) -> a.times(b)).apply(value, (Jama.Matrix) auxState.get(0));
                    auxState = sub;
                }


        }

        Jama.Matrix res = ((Optional<Jama.Matrix>) (value == null ? Optional.empty() : Optional.of(value)))
                                    .get();

        System.out.println(res);
    }
}
