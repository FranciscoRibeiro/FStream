package experimental;

public class ContinuationListElem<T,S> extends Continuation<S, ContinuationListElem<T,S>> {
    public T pendingElem;

    public ContinuationListElem(T elem, Continuation nextCont){
        this.pendingElem = elem;
        this.nextCont = nextCont;
    }

    @Override
    public Continuation execute(S value) {
        Continuation.res = b.apply(pendingElem, value);
        return this.nextCont;
    }
}
