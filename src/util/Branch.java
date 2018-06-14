package util;

public class Branch<T> extends BTree<T>{
    public BTree<T> left;
    public BTree<T> right;

    public Branch(BTree<T> left, BTree<T> right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "B " + "(" + left.toString() + ", " + right.toString() + ")";
    }
}
