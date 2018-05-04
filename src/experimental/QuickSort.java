package experimental;

import datatypes.FStream;

import java.util.ArrayList;
import java.util.Arrays;

public class QuickSort {
    public static FStream<Integer> quickSort(FStream<Integer> s){
        if(s.isEmpty()){
            return s;
        }
        else {
            Integer head = s.head();
            FStream<Integer> lesser = s.drop(1).filterfs(x -> (Integer) x < head);
            FStream<Integer> geq = s.drop(1).filterfs(x -> (Integer) x >= head);

            ArrayList<Integer> aux = new ArrayList<>();
            aux.add(head);

            return quickSort(lesser).appendfs(FStream.fstream(aux)).appendfs(quickSort(geq));
        }
    }

    public static void main(String[] args) {
        ArrayList<Integer> l = new ArrayList<>(Arrays.asList(93, 6, 2, 95, 47, 45, 68, 89, 46, 92, 19, 54, 58, 79, 85, 23, 87, 97, 86, 48, 84, 72, 37, 7, 22, 39, 1, 71, 10, 36, 60, 94, 52, 44, 75, 43, 56, 88, 11, 8, 33, 17, 64, 73, 4, 76, 59, 78, 27, 77, 49, 63, 35, 12, 32, 9, 82));

        FStream<Integer> res = quickSort(FStream.fstream(l));
        System.out.println(res.unfstream());
    }
}
