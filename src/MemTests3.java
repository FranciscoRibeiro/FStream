import java.util.ArrayList;
import java.util.function.Function;

public class MemTests3 {
    public static void main(String[] args) {
        final int SIZE = 200000;
        ArrayList<Integer> li = new ArrayList<>();
        for(int i = 0; i < SIZE; i++){
            li.add(i);
        }
        Function<Integer, String> f = x -> String.valueOf(x);
        Function<String, Long> g = x -> Long.parseLong(x);
        FStream<Long> sl = FStream.fstream(li).mapfs(f.andThen(g));
        ArrayList<Long> ll = sl.unfstream();
        //System.out.println(li);
        System.out.println(ll);
    }
}
