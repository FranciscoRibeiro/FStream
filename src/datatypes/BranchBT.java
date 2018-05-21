package datatypes;

public class BranchBT<S> extends Step{
    public S state1;
    public S state2;

    public BranchBT(S state1, S state2) {
        this.state1 = state1;
        this.state2 = state2;
    }
}
