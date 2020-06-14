package interview.ds.avl;

import java.util.LinkedList;
import java.util.Queue;

/**
 * <p>
 *    二叉树实现，参考：https://www.cnblogs.com/lishanlei/p/10707794.html
 *
 * </p>
 *
 * @author liaoyl
 * @version 1.0 2020/05/12 1:57 PM
 **/
public class AVLTree {

    /**
     * 右旋，并且返回旋转后的子树
     */
    static AVLNode rotateRight(AVLNode node) {
        AVLNode leftChild = node.leftChild;
        // 如果左子树的右节点不为空，则将其置位 node 的左节点
        if (leftChild.rightChild != null) {
            node.leftChild = leftChild.rightChild;
            node.leftChild.parent = node;
        }

        // node 设置为其 leftChild 的右子节点
        leftChild.rightChild = node;
        leftChild.parent = node.parent;
        node.leftChild = null;
        node.parent = leftChild;
        return leftChild;
    }

    /**
     * 右旋-左旋，并且返回旋转后的子树
     */
    static AVLNode rotateRightLeft(AVLNode node) {
        // 先变成需要右旋前的样子
        AVLNode leftChild = node.leftChild;
        AVLNode leftRightChild = leftChild.rightChild;

        node.leftChild = leftRightChild;
        leftRightChild.parent = node;
        leftChild.rightChild = null;

        leftChild.parent = leftRightChild;
        leftRightChild.leftChild = leftChild;
        return rotateRight(node);
    }

    /**
     * 左旋，并且返回旋转后的子树
     */
    static AVLNode rotateLeft(AVLNode node) {
        AVLNode rightChild = node.rightChild;
        // 如果右子树的左节点不为空，则将其置位 node 的右节点
        if (rightChild.leftChild != null) {
            node.rightChild = rightChild.leftChild;
            node.rightChild.parent = node;
        }

        // node 设置为其 rightChild 的左子节点
        rightChild.leftChild = node;
        rightChild.parent = node.parent;
        node.rightChild = null;
        node.parent = rightChild;
        return rightChild;
    }

    /**
     * 左旋-右旋，并且返回旋转后的子树
     */
    static AVLNode rotateLeftRight(AVLNode node) {
        // 先变成需要左旋前的样子
        AVLNode rightChild = node.rightChild;
        AVLNode rightLeftChild = rightChild.leftChild;

        node.rightChild = rightLeftChild;
        rightLeftChild.parent = node;
        rightChild.leftChild = null;

        rightChild.parent = rightLeftChild;
        rightLeftChild.rightChild = rightChild;
        return rotateLeft(node);
    }

    /**
     * 插入数据
     *
     * @param root 根结点
     * @param data 数据
     * @return 新增的节点
     */
    static AVLNode putData(AVLNode root, int data) {
        // 如果大于
        if (data > root.data) {
            // 如果没有右节点
            if (root.rightChild == null) {
                root.rightChild = new AVLNode(data, root);
                return root.rightChild;
            } else {
                return putData(root.rightChild, data);
            }
        } else if (data < root.data) {
            // 如果没有左节点
            if (root.leftChild == null) {
                root.leftChild = new AVLNode(data, root);
                return root.leftChild;
            } else {
                return putData(root.leftChild, data);
            }
        } else {
            return null;
        }
    }

    /**
     * 插入并且检查
     */
    static AVLNode putAndCheck(AVLNode node, int data) {
        return rebuild(getRoot(putData(node, data)));
    }

    /**
     * 移除数据
     *
     * @param root 根结点
     * @param data 数据
     * @return 移除后的 root
     */
    static AVLNode removeData(AVLNode root, int data) {
        // 如果移除的就是 root
        if (root.data == data) {
            // 没有左子树
            if (root.leftChild == null) {
                // 没有右子树
                if (root.rightChild == null) {
                    return null;
                } else {
                    return root.rightChild;
                }
            } else {
                // 没有右子树
                if (root.rightChild == null) {
                    return root.leftChild;
                }else {
                    // 左右子树都有
                    // 如果右子树的左子树为空
                    if (root.rightChild.leftChild==null) {
                        root.rightChild.leftChild = root.leftChild;
                        root.leftChild.parent = root.rightChild;

                        root.rightChild.parent = null;
                        return root.rightChild;
                    }else {
                        // 找到右子树中值最小的节点放在当前 node 的位置
                        AVLNode minNode = findMinNode(root.rightChild);
                        minNode.parent.leftChild = null;
                        minNode.parent = null;
                        minNode.rightChild = root.rightChild;
                        minNode.leftChild = root.leftChild;
                        return minNode;
                    }
                }
            }

        }else {
            // 找到对应的 node
            AVLNode node = findNode(root, data);

            AVLNode parent = node.parent;
            // 确定 node 是左节点还是右节点
            boolean isRight = parent.rightChild == node;

            // 没有左子树
            if (node.leftChild == null) {
                // 没有右子树
                if (node.rightChild == null) {
                    // 断开 node
                    if (parent.rightChild == node) {
                        parent.rightChild = null;
                    } else {
                        parent.leftChild = null;
                    }
                } else {
                    if (isRight) {
                        parent.rightChild = node.rightChild;
                        node.rightChild.parent = parent;
                    } else {
                        parent.leftChild = node.rightChild;
                        node.rightChild.parent = parent;
                    }
                }
            } else {
                // 没有右子树
                if (node.rightChild == null) {
                    if (isRight) {
                        parent.rightChild = node.leftChild;
                        node.leftChild.parent = parent;
                    } else {
                        parent.leftChild = node.leftChild;
                        node.leftChild.parent = parent;
                    }
                } else {
                    // 左右子树都有
                    // 找到右子树中值最小的节点放在当前 node 的位置
                    AVLNode minNode = findMinNode(node.rightChild);
                    minNode.parent.leftChild = null;
                    minNode.parent = node.parent;
                    minNode.rightChild = node.rightChild;
                }
            }
        }


        return root;
    }

    /**
     * 移除并且检查
     */
    static AVLNode removeAndCheck(AVLNode node, int data) {
        return rebuild(getRoot(removeData(node, data)));
    }

    /**
     * 找到最小的节点
     */
    static AVLNode findMinNode(AVLNode node) {
        if (node.leftChild != null) {
            return findMinNode(node.leftChild);
        } else {
            return node;
        }
    }

    /**
     * 查找 node
     *
     * @param root 根结点
     * @param data 数据
     * @return 对应的节点
     */
    static AVLNode findNode(AVLNode root, int data) {
        // 如果大于
        if (data > root.data) {
            // 如果没有右节点
            if (root.rightChild == null) {
                return null;
            } else {
                return findNode(root.rightChild, data);
            }
        } else if (data < root.data) {
            // 如果没有左节点
            if (root.leftChild == null) {
                return null;
            } else {
                return findNode(root.leftChild, data);
            }
        } else {
            return root;
        }
    }


    /**
     * 从当前节点重新构建
     */
    static AVLNode rebuild(AVLNode node) {
        // 获取平衡因子
        int balanceFactor = (node.leftChild == null ? 0 : getNodeHeight(node.leftChild)) - (node.rightChild == null ? 0 : getNodeHeight(node.rightChild));
        if (balanceFactor == 2) {
            // 右旋
            // 是否要双旋
            if (node.leftChild.leftChild == null && node.leftChild.rightChild != null) {
                return rotateRightLeft(node);
            } else {
                return rotateRight(node);
            }
        } else if (balanceFactor == -2) {
            // 左旋
            // 是否要双旋
            if (node.rightChild.rightChild == null && node.rightChild.leftChild != null) {
                return rotateLeftRight(node);
            } else {
                return rotateLeft(node);
            }
        } else {
            // 不需要自旋，向上继续追溯
            if (node.parent == null) {
                return node;
            } else {
                return rebuild(node.parent);
            }
        }
    }

    /**
     * 获取 node 高度，root 高度为 0
     */
    static int getNodeHeight(AVLNode node) {
        // 节点高度等于左右子树高度最大值+1
        int leftHeight, rightHeight;
        if (node.leftChild != null) {
            leftHeight = getNodeHeight(node.leftChild);
        } else {
            leftHeight = 0;
        }

        if (node.rightChild != null) {
            rightHeight = getNodeHeight(node.rightChild);
        } else {
            rightHeight = 0;
        }
        return Math.max(leftHeight, rightHeight) + 1;
    }

    /**
     * @param root 树根节点
     *             层序遍历二叉树，用队列实现，先将根节点入队列，只要队列不为空，然后出队列，并访问，接着讲访问节点的左右子树依次入队列
     */
    public static void levelTravel(AVLNode root) {
        if (root == null) {
            return;
        }
        Queue<AVLNode> q = new LinkedList<>();
        q.add(root);
        while (!q.isEmpty()) {
            AVLNode temp = q.poll();
            System.out.println(temp.data);
            if (temp.leftChild != null) {
                q.add(temp.leftChild);
            }
            if (temp.rightChild != null) {
                q.add(temp.rightChild);
            }
        }
    }

    static AVLNode getRoot(AVLNode node) {
        if (node.parent != null) {
            return getRoot(node.parent);
        }else {
            return node;
        }
    }

    


}
