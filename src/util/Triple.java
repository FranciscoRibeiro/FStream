package util;

import java.util.Optional;

public class Triple<T,S> {
    T stateA;
    S stateB;
    Optional elem;

    public Triple(T stateA, S stateB, Optional elem) {
        this.stateA = stateA;
        this.stateB = stateB;
        this.elem = elem;
    }

    public T getStateA() {
        return stateA;
    }

    public S getStateB() {
        return stateB;
    }

    public Optional getElem() {
        return elem;
    }
}
