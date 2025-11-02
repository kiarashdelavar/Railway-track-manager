package nl.saxion.cds.collection;

import nl.saxion.cds.collection.exceptions.ElementNotFoundException;
import nl.saxion.cds.collection.exceptions.NullNotAllowedException;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Stack;

/**
 * A self-balancing Binary Search Tree (AVL Tree) implementation.
 * <p>
 * The tree keeps itself balanced after adding or removing elements
 * using rotations (Left, Right, Left-Right, Right-Left).
 * </p>
 * <p>
 * Every node stores a height value. After each operation, we check
 * if the balance factor (left height - right height) is between -1 and +1.
 * If not, I fix it by rotating the nodes.
 * </p>
 *
 * @param <T> the type of elements stored in the tree
 */
public class SaxBinarySearchTree<T> implements SaxCollection<T> {
    private final Comparator<T> comparator;
    private TreeNode<T> root;
    private int size;

    /**
     * Creating a new (empty) AVL Tree using a given comparator.
     *
     * @param comparator the comparator used to compare elements
     */
    public SaxBinarySearchTree(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    /**
     * Returning how many elements are currently in the tree.
     */
    @Override
    public int getSize() {
        return size;
    }

    /**
     * Removes all elements from the tree (makes it empty).
     */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * Returns a GraphViz string to visualize the tree.
     * (This part is not required for test coverage.)
     */
    @IgnoreCoverage
    @Override
    public String graphViz(String name) {
        StringBuilder sb = new StringBuilder("digraph ").append(name).append(" {\n");
        Stack<TreeNode<T>> nodeStack = new Stack<>();
        Stack<String> idStack = new Stack<>();

        if (root != null) {
            nodeStack.push(root);
            idStack.push("\"" + root.value + "\"");
        }

        while (!nodeStack.isEmpty()) {
            TreeNode<T> current = nodeStack.pop();
            String currentId = idStack.pop();

            if (current.left != null) {
                String leftId = "\"" + current.left.value + "\"";
                sb.append(currentId).append(" -> ").append(leftId).append(";\n");
                nodeStack.push(current.left);
                idStack.push(leftId);
            } else {
                String nullLeft = "\"nullL_" + currentId + "\"";
                sb.append(nullLeft).append(" [shape=point];\n");
                sb.append(currentId).append(" -> ").append(nullLeft).append(";\n");
            }

            if (current.right != null) {
                String rightId = "\"" + current.right.value + "\"";
                sb.append(currentId).append(" -> ").append(rightId).append(";\n");
                nodeStack.push(current.right);
                idStack.push(rightId);
            } else {
                String nullRight = "\"nullR_" + currentId + "\"";
                sb.append(nullRight).append(" [shape=point];\n");
                sb.append(currentId).append(" -> ").append(nullRight).append(";\n");
            }
        }

        sb.append("}");
        return sb.toString();
    }

    /**
     * Uses the default SaxCollection toString format.
     */
    @Override
    public String toString() {
        return defaultToString();
    }

    /**
     * In-order iterator (left → node → right).
     */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            Stack<TreeNode<T>> stack = new Stack<>();
            TreeNode<T> current = root;

            @Override
            public boolean hasNext() {
                return !stack.isEmpty() || current != null;
            }

            @Override
            public T next() {
                while (current != null) {
                    stack.push(current);
                    current = current.left;
                }
                TreeNode<T> node = stack.pop();
                current = node.right;
                return node.value;
            }
        };
    }


    // avl tree implementation
    /**
     * Adds a new element to the tree.
     *
     * @throws NullNotAllowedException if element is null
     */
    public void add(T element) throws NullNotAllowedException {
        if (element == null)
            throw new NullNotAllowedException();
        root = addRecursive(root, element);
    }

    /**
     * Recursive insert with AVL rebalancing.
     */
    private TreeNode<T> addRecursive(TreeNode<T> node, T value) {
        if (node == null) {
            size++;
            return new TreeNode<>(value);
        }

        int cmp = comparator.compare(value, node.value);
        if (cmp < 0) {
            node.left = addRecursive(node.left, value);
        }
        else if (cmp > 0) {
            node.right = addRecursive(node.right, value);
        }
        else
            return node; // no duplicates

        // Update height
        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));

        // Check balance
        int balance = getBalance(node);

        // Left Left (rotate right)
        if (balance > 1 && comparator.compare(value, node.left.value) < 0) {
            return rotateRight(node);
        }
        // Right Right (rotate left)
        if (balance < -1 && comparator.compare(value, node.right.value) > 0) {
            return rotateLeft(node);
        }

        // Left Right (double rotation)
        if (balance > 1 && comparator.compare(value, node.left.value) > 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        // Right Left (double rotation)
        if (balance < -1 && comparator.compare(value, node.right.value) < 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node; // balanced
    }

    /**
     * Removes an element and rebalances the tree.
     */
    public void remove(T element) throws ElementNotFoundException, NullNotAllowedException {
        if (element == null)
            throw new NullNotAllowedException();
        root = removeRecursive(root, element);
    }

    /**
     * Recursive remove with AVL rebalancing.
     */
    private TreeNode<T> removeRecursive(TreeNode<T> node, T element) throws ElementNotFoundException {
        if (node == null)
            throw new ElementNotFoundException("Element not found: " + element);

        int cmp = comparator.compare(element, node.value);

        if (cmp < 0)
            node.left = removeRecursive(node.left, element);
        else if (cmp > 0)
            node.right = removeRecursive(node.right, element);
        else {
            // found node
            if (node.left == null || node.right == null) {
                node = (node.left != null) ? node.left : node.right;
                size--;
            } else {
                // Two children: replace with smallest in right subtree
                TreeNode<T> successor = getMin(node.right);
                node.value = successor.value;
                node.right = removeRecursive(node.right, successor.value);
            }
        }

        if (node == null) {
            return null;
        }

        // update height
        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));

        // rebalance
        int balance = getBalance(node);

        // Left Left
        if (balance > 1 && getBalance(node.left) >= 0)
            return rotateRight(node);

        // Left right
        if (balance > 1 && getBalance(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        // right right
        if (balance < -1 && getBalance(node.right) <= 0) {
            return rotateLeft(node);
        }

        // right Left
        if (balance < -1 && getBalance(node.right) > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    /**
     * Helper to find the smallest value node in a subtree.
     */
    public TreeNode<T> getMin(TreeNode<T> node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    /**
     * Checks if an element exists in the tree.
     */
    public boolean contains(T element) {
        TreeNode<T> current = root;
        while (current != null) {
            int cmp = comparator.compare(element, current.value);
            if (cmp == 0) {
                return true;
            }
            current = (cmp < 0) ? current.left : current.right;
        }
        return false;
    }



    //helper methods (height & balance)
    private int getHeight(TreeNode<T> node) {
        return (node == null) ? -1 : node.height;
    }

    private int getBalance(TreeNode<T> node) {
        return (node == null) ? 0 : getHeight(node.left) - getHeight(node.right);
    }

    private TreeNode<T> rotateRight(TreeNode<T> y) {
        TreeNode<T> x = y.left;
        TreeNode<T> T2 = x.right;

        // Performing rotation
        x.right = y;
        y.left = T2;

        // Updating heights
        y.height = 1 + Math.max(getHeight(y.left), getHeight(y.right));
        x.height = 1 + Math.max(getHeight(x.left), getHeight(x.right));

        return x;
    }

    private TreeNode<T> rotateLeft(TreeNode<T> x) {
        TreeNode<T> y = x.right;
        TreeNode<T> T2 = y.left;

        // Perform rotation
        y.left = x;
        x.right = T2;

        // Updating heights
        x.height = 1 + Math.max(getHeight(x.left), getHeight(x.right));
        y.height = 1 + Math.max(getHeight(y.left), getHeight(y.right));

        return y;
    }

    /**
     * Calculates the total height (for external checks).
     */
    public int getHeight() {
        return getHeight(root);
    }

    /**
     * Checks if the whole tree is balanced (AVL property).
     */
    public boolean isBalanced() {
        return checkBalanced(root);
    }

    private boolean checkBalanced(TreeNode<T> node) {
        if (node == null) {
            return true;
        }

        int lh = getHeight(node.left);
        int rh = getHeight(node.right);

        if (Math.abs(lh - rh) > 1) {
            return false;
        }

        return checkBalanced(node.left) && checkBalanced(node.right);
    }
}
