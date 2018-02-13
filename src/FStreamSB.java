import sun.reflect.ConstantPool;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.*;

public class FStreamSB<T>{
    private Function<ArrayList, Step> stepper; // the stepper function: (s -> Step a s)
    private ArrayList state; // the stream's state (normally a list)

    public FStreamSB(Function<ArrayList, Step> stepper, ArrayList<T> state){
        this.stepper = stepper;
        this.state = state;
    }

    public static <T> FStreamSB<T> fstream(ArrayList<T> l){
        Function<ArrayList, Step> stepper = x -> x.isEmpty() ? new Done() : new Yield<T, ArrayList<T>>((T)x.get(0), new ArrayList<T>(x.subList(1, x.size())));
        return new FStreamSB<T>(stepper, l);
    }

    public ArrayList<T> unfstream(){
        ArrayList<T> res = new ArrayList<>();
        Step step = this.stepper.apply(this.state);
        int slicer = 1;
        while(!(step instanceof Done)){
            if(step instanceof Yield){
                res.add((T)((Yield) step).elem);
            }
            step = this.stepper.apply(new ArrayList<T>(this.state.subList(slicer, this.state.size())));
            slicer++;
        }
        return res;
    }

    public <S> FStreamSB<S> mapfs(Function<T,S> funcTtoS){
        Function<ArrayList, Step> stepper = x -> {
            Step aux = this.stepper.apply(x);
            if(aux instanceof Done){
                return new Done();
            }
            else if(aux instanceof Skip){
                return new Skip<ArrayList>((ArrayList)((Skip)aux).state); //Need to change this later
            }
            else if(aux instanceof Yield){
                return new Yield<S,ArrayList>(funcTtoS.apply((T)((Yield)aux).elem), (ArrayList)((Yield)aux).state);
            }
            return null;
        };
        return new FStreamSB<S>(stepper, this.state);
    }

    public FStreamSB<T> filterfs(Predicate p){
        Function<ArrayList, Step> stepper = x -> {
            Step aux = this.stepper.apply(x);
            if(aux instanceof Done){
                return new Done();
            }
            else if(aux instanceof Skip){
                return new Skip<ArrayList>((ArrayList)((Skip)aux).state); //Need to change this later
            }
            else if(aux instanceof Yield){
                if(p.test(((Yield)aux).elem)){
                    return new Yield<T,ArrayList>((T)((Yield)aux).elem, (ArrayList)((Yield)aux).state);
                }
                else{
                    return new Skip<ArrayList>((ArrayList)((Yield)aux).state);
                }
            }
            return null;
        };
        return new FStreamSB<T>(stepper, this.state);
    }

    public static <T> ArrayList<T> map(Function f, ArrayList<T> l){
        return fstream(l).mapfs(f).unfstream();
    }



    public static void main(String[] args){
        ArrayList<String> as = new ArrayList<>();
        for(int i = 0; i < 3000000; i++){
            as.add("string" + i);
        }

        ArrayList<String> as2 = new ArrayList<>();
        int i = 0;
        for(String s: as){
            String sAux = s + i;
            sAux = sAux + i;
            as2.add(sAux);
            i++;
        }

        System.out.println(as2);
    }
}