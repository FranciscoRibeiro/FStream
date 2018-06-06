package experimental;

import java.util.function.BiFunction;

public abstract class Continuation<S, R extends Continuation<S,R>> {
    public static Object globalState;
    public static Object res;
    public static BiFunction b;
    public Continuation<S,R> nextCont;

    public abstract Continuation execute(S value);

    //public abstract Continuation execute2(Object value);
}
