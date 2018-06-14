package util;

public class Leaf<T> extends BTree<T> {
    private T value;

    public Leaf(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "L " + value;
    }
}
