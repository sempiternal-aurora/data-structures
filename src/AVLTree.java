import java.util.ArrayList;
import java.util.Collection;

public class AVLTree<E extends Comparable<? super E>> {
    private class Node {
        E value;
        Node parent;
        Node left;
        Node right;
        int height;
        Node(E value) {
            this.value = value;
        }

        void recalcHeight() {
            height = Math.max(getHeight(left), getHeight(right)) + 1;
        }

        @Override
        public String toString() {
            return "Node (" + left + ") " + value.toString() + " (" + right + ')';
        }
    }

    boolean isNullInTree;
    // The root node of the tree
    Node root;
    int numElements;

    AVLTree(Collection<E> collection) {
        addAll(collection);
    }

    AVLTree() {

    }

    /**
     * Adds a value to the tree, returning true if it can be added, false otherwise.
     * Balances the tree after the item is added by rotating, given it is a standard AVL Tree
     * Assumes that the equality and Comparable implementations are consistent
     * @param value the value to be added to the tree
     * @return whether the value could be added to the tree
     */
    boolean add(E value) {
        // Handling for the null value which doesn't work with the comparing in the tree
        if (value == null) {
            if (isNullInTree) {
                return false;
            } else {
                isNullInTree = true;
                numElements++;
                return true;
            }
        }

        Node parent = findParent(value);
        Node thisNode;
        if (parent == null) { // Only occurs at root node
            if (this.root == null) {
                thisNode = new Node(value);
                thisNode.parent = null;
                thisNode.height = 1;
                this.root = thisNode;
                numElements++;
                return true;
            } else {
                return false;
            }
        } else if (parent.value.compareTo(value) > 0) {
            thisNode = parent.left;
            if (thisNode == null) {
                thisNode = new Node(value);
                parent.left = thisNode;
                thisNode.parent = parent;
                thisNode.height = 1;
            } else {
                return false;
            }
        } else { // Can only be right side, as parent explicitly means that it isn't the value.
            thisNode = parent.right;
            if (thisNode == null) {
                thisNode = new Node(value);
                parent.right = thisNode;
                thisNode.parent = parent;
                thisNode.height = 1;
            } else {
                return false;
            }
        }

        balanceUp(thisNode.parent, thisNode);
        numElements++;
        return true;
    }

    boolean addAll(Collection<E> collection) {
        boolean isChanged = false;
        for (E elem : collection) {
            isChanged |= add(elem);
        }
        return isChanged;
    }

    /**
     *
     * @param parent
     * @param child
     */
    private void balanceUp(Node parent, Node child) {
        while (parent != null) {
            parent.recalcHeight();
            Node nextParent = parent.parent;
            int parentBalanceFactor = getBalanceFactor(parent);
            int childBalanceFactor = getBalanceFactor(child);
            if (child == parent.left) {
                if (parentBalanceFactor > 1) {
                    Node other = parent.right;
                    int otherBalanceFactor = getBalanceFactor(other);
                    if (otherBalanceFactor < 0) {
                        child = doubleRotate(parent, other);
                    } else {
                        child = singleRotate(parent, other);
                    }
                } else if (parentBalanceFactor < -1) {
                    if (childBalanceFactor > 0) {
                        child = doubleRotate(parent, child);
                    } else {
                        child = singleRotate(parent, child);
                    }
                } else {
                    child = parent;
                }
            } else {
                if (parentBalanceFactor > 1) {
                    if (childBalanceFactor < 0) {
                        child = doubleRotate(parent, child);
                    } else {
                        child = singleRotate(parent, child);
                    }
                } else if (parentBalanceFactor < -1) {
                    Node other = parent.left;
                    int otherBalanceFactor = getBalanceFactor(other);
                    if (otherBalanceFactor > 0) {
                        child = doubleRotate(parent, other);
                    } else {
                        child = singleRotate(parent, other);
                    }
                } else {
                    child = parent;
                }
            }
            parent = nextParent;
        }
    }

    /**
     * A double rotation, moving a grandchild of the node up to the top of the subtree, and moving the node and child
     * to be children of it
     * @param node the node at the top of the subtree initially
     * @param child the child of this node that will be moved to balance the subtree
     * @return the new top of the subtree
     */
    private Node doubleRotate(Node node, Node child) {
        boolean isLeftRight = node.left == child;
        Node grandChild;
        if (isLeftRight) {
            grandChild = child.right;
        } else {
            grandChild = child.left;
        }
        Node leftGreatChild = grandChild.left;
        Node rightGreatChild = grandChild.right;
        Node parent = node.parent;

        // Position the grand child at the top of the new subtree
        if (parent != null) {
            if (parent.left == node) {
                parent.left = grandChild;
            } else {
                parent.right = grandChild;
            }
        } else {
            root = grandChild;
        }
        grandChild.parent = parent;

        // Make the node it's appropriate child, and the child it's other
        if (isLeftRight) {
            grandChild.left = child;
            grandChild.right = node;
        } else {
            grandChild.left = node;
            grandChild.right = child;
        }
        node.parent = grandChild;
        child.parent = grandChild;

        // And finally replace the children of grandChild in the new correct place
        if (isLeftRight) {
            child.right = leftGreatChild;
            node.left = rightGreatChild;
            if (leftGreatChild != null) {
                leftGreatChild.parent = child;
            }
            if (rightGreatChild != null) {
                rightGreatChild.parent = node;
            }
        } else {
            child.left = rightGreatChild;
            node.right = leftGreatChild;
            if (leftGreatChild != null) {
                leftGreatChild.parent = node;
            }
            if (rightGreatChild != null) {
                rightGreatChild.parent = child;
            }
        }

        // recalculate the heights
        child.recalcHeight();
        node.recalcHeight();
        grandChild.recalcHeight();
        return grandChild;
    }

    /**
     * A single rotation, moving the child into place of the node, demoting the node to a child.
     * @param node the node that will become a child
     * @param child the node that will become the new top of the subtree
     * @return the new top of the subtree
     */
    private Node singleRotate(Node node, Node child) {
        Node parent = node.parent;
        Node movingGrandChild;
        boolean isRightRotate = node.left == child;
        if (isRightRotate) {
            movingGrandChild = child.right;
        } else {
            movingGrandChild = child.left;
        }

        // Change the movingGrandChild to be the correct child of node
        if (movingGrandChild != null) {
            movingGrandChild.parent = node;
        }
        if (isRightRotate) {
            node.left = movingGrandChild;
        } else {
            node.right = movingGrandChild;
        }

        // Change the node to be the correct child of child
        node.parent = child;
        if (isRightRotate) {
            child.right = node;
        } else {
            child.left = node;
        }

        // Change node to be the whichever side child of parent
        child.parent = parent;
        if (parent != null) {
            if (parent.left == node) {
                parent.left = child;
            } else {
                parent.right = child;
            }
        } else {
            root = child;
        }

        // Recalculate heights
        node.recalcHeight();
        child.recalcHeight();
        return child;
    }

    /**
     * Determines the balancing factor of a node
     * used to determine if a rotation needs to be performed on a node to balance the tree
     * @param node a nullable node
     * @return the Node's right height minus it's left, 0 if the node is null
     */
    private int getBalanceFactor(Node node) {
        if (node == null) {
            return 0;
        } else {
            return getHeight(node.right) - getHeight(node.left);
        }
    }

    /**
     * Wrapper for getting the height of an abstract node, returning 0 for null heights
     * @param node the node to get the height of, can be null
     * @return the node's cached height value if it is non-null, 0 otherwise
     */
    private int getHeight(Node node) {
        if (node == null) {
            return 0;
        } else {
            return node.height;
        }
    }

    /**
     * Find the parent of a value if it were to exist in the current AVL tree
     * @param value a value to find the parent of
     * @return the parent of the given value
     */
    private Node findParent(E value) {
        Node currRoot = root;
        Node parent = null;
        while (currRoot != null) {
            int comparison = currRoot.value.compareTo(value);
            if (comparison == 0) {
                break;
            } else if (comparison > 0) {
                parent = currRoot;
                currRoot = currRoot.left;
            } else {
                parent = currRoot;
                currRoot = currRoot.right;
            }
        }
        return parent;
    }

    /**
     * Checks whether the tree is balanced, using cached heights of the nodes
     * Balanced here means that every node's subtrees differ in height by at most 1
     * @return whether the tree is balanced by AVL Standards
     */
    public boolean isBalanced() {
        ArrayList<Node> nodesToCheck = new ArrayList<>();
        if (root != null) {
            nodesToCheck.add(root);
        }
        while (!nodesToCheck.isEmpty()) {
            Node node = nodesToCheck.get(0);
            int bal = getBalanceFactor(node);
            if (bal < -1 | bal > 1) {
                return false;
            }
            if (node.left != null) {
                nodesToCheck.add(node.left);
            }
            if (node.right != null) {
                nodesToCheck.add(node.right);
            }
            nodesToCheck.remove(0);
        }
        return true;
    }

    @Override
    public String toString() {
        return "AVLTree (has null : " + isNullInTree + ") (" + root + ')';
    }

    /**
     * Attempt to find the successor to a given node in the tree
     * Travels down the right sub-tree to find it
     * @param node a non-null node with a right subtree
     * @return the next largest value in the right subtree
     */
    private Node successor(Node node) {
        Node successor = node.right;
        while (successor.left != null) {
            successor = successor.left;
        }
        return successor;
    }

    /**
     *
     * @param node
     * @return
     */
    private Node deleteInternal(Node node) {
        Node parent = node.parent;
        Node newChild;
        Node balanceUpParent;
        Node balanceUpChild;
        if (node.left == null) { // node might have a right child, but left is empty
            newChild = node.right;
            balanceUpParent = parent;
            balanceUpChild = newChild;
        } else if (node.right == null) { // the right node is empty, but the left isn't
            newChild = node.left;
            balanceUpParent = parent;
            balanceUpChild = newChild;
        } else {
            // Both children of the node are not empty
            // Need to go down subtree until an option is found
            // Right by choice
            newChild = successor(node);
            balanceUpParent = newChild.parent;
            balanceUpChild = newChild.right;
            balanceUpParent.left = balanceUpChild;
            if (balanceUpChild != null) {
                balanceUpChild.parent = newChild.parent;
            }
            newChild.right = node.right;
            node.right.parent = newChild;
            newChild.left = node.left;
            node.left.parent = newChild;
        }

        if (newChild != null) {
            newChild.parent = parent;
        }
        if (parent == null) {
            root = newChild;
        } else if (parent.left == node) {
            parent.left = newChild;
        } else {
            parent.right = newChild;
        }

        balanceUp(balanceUpParent, balanceUpChild);
        return newChild;
    }

    /**
     *
     * @param value
     * @return
     */
    public boolean remove(E value) {
        // Handle the null case
        if (value == null) {
            if (isNullInTree) {
                isNullInTree = false;
                numElements--;
                return true;
            } else {
                return false;
            }
        }

        // Find the value to be deleted
        Node parent = findParent(value);
        Node node;
        if (parent == null) {
            node = root;
        } else if (parent.value.compareTo(value) > 0) {
            node = parent.left;
        } else {
            node = parent.right;
        }

        if (node == null) {
            return false;
        }

        Node newChild = deleteInternal(node);
        numElements--;
        return true;
    }

    boolean contains(E value) {
        if (value == null) {
            return isNullInTree;
        }

        Node parent = findParent(value);
        if (parent == null) {
            return root != null;
        } else if (parent.value.compareTo(value) > 0) {
            return parent.left != null;
        } else {
            return parent.right != null;
        }
    }

    public int size() {
        return numElements;
    }
}
