package interview.ds.rbt;

/**
 * <p>红黑树实现</p>
 *
 * @author liaoyl
 * @version 1.0 2020/05/12 10:53 AM
 **/
public class RBTreeNode<T extends Comparable<T>> {

    private T value;
    private RBTreeNode<T> left;
    private RBTreeNode<T> right;
    private RBTreeNode<T> parent;
    private boolean red;

    public RBTreeNode() {
    }

    public RBTreeNode(T value) {
        this.value = value;
    }

    public RBTreeNode(T value, boolean isRed) {
        this.value = value;
        this.red = isRed;
    }

    public T getValue() {
        return value;
    }

    void setValue(T value) {
        this.value = value;
    }

    RBTreeNode<T> getLeft() {
        return left;
    }

    void setLeft(RBTreeNode<T> left) {
        this.left = left;
    }

    RBTreeNode<T> getRight() {
        return right;
    }

    void setRight(RBTreeNode<T> right) {
        this.right = right;
    }

    RBTreeNode<T> getParent() {
        return parent;
    }

    void setParent(RBTreeNode<T> parent) {
        this.parent = parent;
    }

    boolean isRed() {
        return red;
    }

    boolean isBlack() {
        return !red;
    }

    /**
     * is leaf node
     **/
    boolean isLeaf() {
        return left == null && right == null;
    }

    void setRed(boolean red) {
        this.red = red;
    }

    void makeRed() {
        red = true;
    }

    void makeBlack() {
        red = false;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}

