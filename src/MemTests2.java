import java.util.ArrayList;
import java.util.function.Function;

public class MemTests2 {
    public static void main(String[] args) throws InterruptedException {
        final int SIZE = 200000;
        ArrayList<Integer> li = new ArrayList<>();
        for(int i = 0; i < SIZE; i++){
            li.add(i);
        }
        FStream<Integer> si = FStream.fstream(li);
        Function<Integer, String> f = x -> String.valueOf(x);
        FStream<String> ss = si.mapfs(f);
        ArrayList<String> ls = ss.unfstream();
        Thread.sleep(5000);
        FStream<String> ss2 = FStream.fstream(ls);
        Function<String, Long> g = x -> Long.parseLong(x);
        FStream<Long> sl = ss2.mapfs(g);
        ArrayList<Long> ll = sl.unfstream();
        //System.out.println(li);
        System.out.println(ll);
    }
}
