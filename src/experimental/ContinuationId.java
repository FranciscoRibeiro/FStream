package experimental;

public class ContinuationId<S> extends Continuation<S, ContinuationId<S>> {
    
    public ContinuationId(){
    }

    @Override
    public Continuation execute(S value) {
        res = value;
        return null;
    }
}
