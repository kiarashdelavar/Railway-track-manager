package nl.saxion.cds.collection;

/**
 * A node used in the SaxBinarySearchTree (and AVL variant).
 * Each node has a value and optional left and right child nodes.
 * It also tracks its height for AVL balancing.
 *
 * @param <T> the type of element stored in this node
 */
public class TreeNode<T> {
    T value;
    TreeNode<T> left;
    TreeNode<T> right;
    int height;

    /**
     * Creating a new tree node with the given value.
     * the initial height of a leaf node is 0.
     *
     * @param value the value stored in this node
     */
    public TreeNode(T value) {
        this.value = value;
        this.height = 0;
    }
}
