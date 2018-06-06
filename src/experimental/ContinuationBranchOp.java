package experimental;

public class ContinuationBranchOp<S> extends Continuation<S, ContinuationBranchOp<S>> {
    public S pendingElem;

    public ContinuationBranchOp(Continuation nextCont){
        this.nextCont = nextCont;
    }

    @Override
    public Continuation execute(S value) {
        return nextCont.execute((S) b.apply(pendingElem, value));
    }
}
