package experimental;

import util.Pair;

public class ContinuationFold<S> extends Continuation<S,ContinuationFold<S>> {
    public Object state;


    public ContinuationFold(Object state, Continuation nextCont) {
        this.state = state;
        this.nextCont = nextCont;
    }

    @Override
    public Continuation execute(S value) {
        globalState = this.state;
        ((ContinuationBranchOp) this.nextCont).pendingElem = value;
        return this.nextCont;
    }

    /*@Override
    public Continuation execute2(Object value) {
        if(value instanceof Pair){
            Continuation<Integer, ContinuationBranchOp<Integer>> nextCont = new ContinuationBranchOp<>(cont);
            return new ContinuationFold(value.getY(), nextCont);
        }
    }*/

}
