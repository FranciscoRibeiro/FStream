package util;

public class Branch<T> extends BTree<T>{
    private BTree<T> left;
    private BTree<T> right;

    public Branch(BTree<T> left, BTree<T> right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "B " + "(" + left.toString() + ", " + right.toString() + ")";
    }
}
