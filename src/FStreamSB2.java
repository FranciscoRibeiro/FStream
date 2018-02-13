import java.util.ArrayList;

public class FStreamSB2 {
    public static void main(String[] args){
        ArrayList<String> as = new ArrayList<>();
        for(int i = 0; i < 3000000; i++){
            as.add("string" + i);
        }

        ArrayList<String> as4 = new ArrayList<>();
        int i = 0;
        for(String s: as){
            StringBuilder sb = new StringBuilder(s);
            sb.append(i);
            sb.append(i);
            as4.add(sb.toString());
            i++;
        }

        System.out.println(as4);
    }
}
