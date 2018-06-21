package experimental;

public class ContinuationListFold<S> extends Continuation<S, ContinuationListFold<S>>{

    public ContinuationListFold(Continuation nextCont) {
        this.nextCont = nextCont;
    }

    @Override
    public Continuation execute(S value) {
        return this.nextCont;
    }
}
