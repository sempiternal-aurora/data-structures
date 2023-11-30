import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class AVLTreeTest {
    @Test
    void add() {
        // test adding to tree
        AVLTree<Integer> tree = new AVLTree<>();
        Assertions.assertTrue(tree.add(10));
        Assertions.assertTrue(tree.add(5));
        Assertions.assertTrue(tree.add(2));
        Assertions.assertTrue(tree.add(1));
        Assertions.assertTrue(tree.add(15));
        Assertions.assertTrue(tree.add(16));
        Assertions.assertTrue(tree.add(23));
        Assertions.assertTrue(tree.add(24));
        Assertions.assertTrue(tree.add(30));
        Assertions.assertTrue(tree.add(40));

        // Test balancing
        Assertions.assertTrue(tree.isBalanced());

        // Test adding
        Assertions.assertFalse(tree.add(15));
        Assertions.assertFalse(tree.add(1));
        Assertions.assertFalse(tree.add(23));

        // Test null handling
        tree = new AVLTree<>();
        Assertions.assertTrue(tree.add(null));
        Assertions.assertFalse(tree.add(null));
        Assertions.assertEquals("AVLTree (has null : true) (null)", tree.toString());

        // Test single rotate
        tree = new AVLTree<>();
        Assertions.assertTrue(tree.add(1));
        Assertions.assertTrue(tree.add(2));
        Assertions.assertTrue(tree.add(3));
        Assertions.assertEquals("AVLTree (has null : false) (Node (Node (null) 1 (null)) 2 (Node (null) 3 (null)))", tree.toString());

        // Test double rotate
        tree = new AVLTree<>();
        Assertions.assertTrue(tree.add(3));
        Assertions.assertTrue(tree.add(1));
        Assertions.assertTrue(tree.add(2));
        Assertions.assertEquals("AVLTree (has null : false) (Node (Node (null) 1 (null)) 2 (Node (null) 3 (null)))", tree.toString());

        // Test the extreme case
        tree = new AVLTree<>();
        for (int i = 0; i < 10000; i++) {
            Assertions.assertTrue(tree.add(i));
            Assertions.assertTrue(tree.isBalanced());
        }
    }

    @Test
    void size() {
        // test size by adding
        AVLTree<Integer> tree = new AVLTree<>();
        tree.add(10);
        tree.add(5);
        tree.add(2);
        tree.add(1);
        tree.add(15);
        tree.add(16);
        tree.add(23);
        tree.add(24);
        tree.add(30);
        tree.add(40);
        Assertions.assertEquals(10, tree.size());

        // Test after adding duplicate elements
        tree.add(10);
        tree.add(5);
        tree.add(2);
        tree.add(1);
        tree.add(15);
        tree.add(16);
        tree.add(23);
        tree.add(24);
        tree.add(30);
        tree.add(40);
        Assertions.assertEquals(10, tree.size());

        // test extreme case
        IntStream.range(0, 10000).forEach(tree::add);
        Assertions.assertEquals(10000, tree.size());
    }

    @Test
    void contains() {
        // Set up example tree
        AVLTree<Integer> tree = new AVLTree<>();
        tree.add(10);
        tree.add(5);
        tree.add(2);
        tree.add(1);
        tree.add(15);
        tree.add(16);
        tree.add(23);
        tree.add(24);
        tree.add(30);
        tree.add(40);

        // Test elements in tree
        Assertions.assertTrue(tree.contains(10));
        Assertions.assertTrue(tree.contains(5));
        Assertions.assertTrue(tree.contains(2));
        Assertions.assertTrue(tree.contains(1));
        Assertions.assertTrue(tree.contains(15));
        Assertions.assertTrue(tree.contains(16));
        Assertions.assertTrue(tree.contains(23));
        Assertions.assertTrue(tree.contains(24));
        Assertions.assertTrue(tree.contains(30));
        Assertions.assertTrue(tree.contains(40));

        // Test elements not in tree
        Assertions.assertFalse(tree.contains(-1));
        Assertions.assertFalse(tree.contains(1003));
        Assertions.assertFalse(tree.contains(37));
        Assertions.assertFalse(tree.contains(9));
        Assertions.assertFalse(tree.contains(7));
    }

    @Test
    void delete() {
        // Create a new tree and a collection of elements to add to it
        Collection<Integer> collection = new ArrayList<>();
        collection.add(10);
        collection.add(5);
        collection.add(2);
        collection.add(1);
        collection.add(15);
        collection.add(16);
        collection.add(23);
        collection.add(24);
        collection.add(30);
        collection.add(40);
        AVLTree<Integer> tree = new AVLTree<>(collection);

        int size = 10;
        // Remove each, checking that it is balanced after each removal
        // And that size is correct
        for (Integer i : collection) {
            Assertions.assertTrue(tree.remove(i));
            size--;
            Assertions.assertTrue(tree.isBalanced());
            Assertions.assertEquals(size, tree.size());
        }

        // Test the null handling
        tree.add(null);
        Assertions.assertTrue(tree.remove(null));
        Assertions.assertEquals(0, tree.size());
        Assertions.assertFalse(tree.remove(null));
        Assertions.assertEquals(0, tree.size());

        // Test element not in the tree
        tree.addAll(collection);
        Assertions.assertFalse(tree.remove(36));
        Assertions.assertFalse(tree.remove(0));


        // Test the extreme case
        IntStream.range(0, 10000).forEach(tree::add);
        for (int i = 0; i < 10000; i++) {
            if (true) {
                Assertions.assertTrue(tree.isBalanced());
                Assertions.assertEquals(10000-i, tree.size());
                Assertions.assertTrue(tree.remove(i));
                Assertions.assertTrue(tree.isBalanced());
                Assertions.assertEquals(9999-i, tree.size());
            } else {
                tree.remove(i);
            }
        }
    }

    @Test
    void addAll() {
        AVLTree<Integer> tree = new AVLTree<>();
        Collection<Integer> collection = new ArrayList<>();

        // Test empty collection
        Assertions.assertFalse(tree.addAll(collection));

        // Create multi element collection to add to tree
        collection.add(10);
        collection.add(5);
        collection.add(2);
        collection.add(1);
        collection.add(15);
        collection.add(16);
        collection.add(23);
        collection.add(24);
        collection.add(30);
        collection.add(40);

        // Add all to tree
        Assertions.assertTrue(tree.addAll(collection));
        Assertions.assertFalse(tree.addAll(collection));

        // Add new element to test not all elements new and add to tree
        collection.add(35);
        Assertions.assertTrue(tree.addAll(collection));
        Assertions.assertFalse(tree.addAll(collection));

        // Check null handling
        collection.add(null);
        Assertions.assertTrue(tree.addAll(collection));
        Assertions.assertFalse(tree.addAll(collection));

        // Check all items added correctly (size is correct)
        Assertions.assertEquals(12, tree.size());

        // test extreme case
        collection = IntStream.range(0, 10000).boxed().collect(Collectors.toList());
        tree = new AVLTree<>();
        Assertions.assertTrue(tree.addAll(collection));
        Assertions.assertEquals(10000, tree.size());
    }


}