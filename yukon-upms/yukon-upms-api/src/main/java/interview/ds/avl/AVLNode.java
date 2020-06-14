package interview.ds.avl;

/**
 * <p></p>
 *
 * @author liaoyl
 * @version 1.0 2020/05/12 1:30 PM
 **/
public class AVLNode {

    int data;
    AVLNode parent, leftChild, rightChild;

    public AVLNode(int data, AVLNode parent) {
        this.data = data;
        this.parent = parent;
        this.leftChild = null;
        this.rightChild = null;
    }



}
