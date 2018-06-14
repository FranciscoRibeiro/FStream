package util;

public class Leaf<T> extends BTree<T> {
    public T value;

    public Leaf(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "L " + value;
    }
}
