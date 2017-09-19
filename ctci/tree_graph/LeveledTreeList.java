import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode2;
import static common.TreeUtils.createTree;

/**
 * Cracking the Coding Interview(5ed) Problem 4.4:
 * Given a binary tree, creates a linked list of all the nodes at each depth
 * (e.g., if you have a tree with depth D, you'll have D linked lists).
 */
public class LeveledTreeList {
    static class LeveledTreeNode {
        TreeNode2 node;
        int level;
        LeveledTreeNode(TreeNode2 node, int level) {
            this.node = node;
            this.level = level;
        }
    }

    public static List<LinkedList<TreeNode2> > createList(TreeNode2 root) {
        if (root == null) return null;

        List<LinkedList<TreeNode2> > lists = new ArrayList<>();
        LinkedList<LeveledTreeNode> queue = new LinkedList<>();
        queue.add(new LeveledTreeNode(root, 0));
        while (!queue.isEmpty()) {
            LeveledTreeNode current = queue.removeFirst();
            int level = current.level;
            TreeNode2 node = current.node;
            if (lists.size() <= level) {
                lists.add(new LinkedList<TreeNode2>());
            }
            lists.get(level).add(node);
            ++level;
            if (node.left != null) {
                queue.add(new LeveledTreeNode(node.left, level));
            }
            if (node.right != null) {
                queue.add(new LeveledTreeNode(node.right, level));
            }
        }

        return lists;
    }

    public static List<LinkedList<TreeNode2> > createList2(TreeNode2 root) {
        if (root == null) return null;

        List<LinkedList<TreeNode2> > lists
            = new ArrayList<>();
        LinkedList<TreeNode2> current
            = new LinkedList<>(Collections.singletonList(root));
        while (current.size() > 0) {
            lists.add(current);
            LinkedList<TreeNode2> parents = current;
            current = new LinkedList<>();
            for (TreeNode2 parent : parents) {
                if (parent.left != null) {
                    current.add(parent.left);
                }
                if (parent.right != null) {
                    current.add(parent.right);
                }
            }
        }
        return lists;
    }

    public static List<LinkedList<TreeNode2> > createListRecursive(TreeNode2 root) {
        if (root == null) return null;

        List<LinkedList<TreeNode2> > lists = new ArrayList<>();
        createListRecursive(lists, root, 0);
        return lists;
    }

    private static void createListRecursive(List<LinkedList<TreeNode2> > lists,
                                            TreeNode2 node, int level) {
        if (node == null) return;

        if (lists.size() <= level) {
            lists.add(new LinkedList<>());
        }
        lists.get(level++).add(node);

        createListRecursive(lists, node.left, level);
        createListRecursive(lists, node.right, level);
    }

    void test(Integer[] array, int[][] expected) {
        test(array, expected, LeveledTreeList::createList);
        test(array, expected, LeveledTreeList::createList2);
        test(array, expected, LeveledTreeList::createListRecursive);
    }

    void test(Integer[] array, int[][] expected,
              Function<TreeNode2, List<LinkedList<TreeNode2>>> createList) {
        int i = 0;
        for (LinkedList<TreeNode2> list : createList.apply(createTree(array))) {
            assertArrayEquals(expected[i],
                              list.stream().mapToInt(n -> n.data).toArray());
            i++;
        }
        assertEquals(i, expected.length);
    }

    @Test
    public void test1() {
        test(new Integer[] {2, 7, 5, 2, 6, 3, 6, 5, 8, 4, 5, 8, 4, 5, 8},
             new int[][] {{2}, {7, 5}, {2, 6, 3, 6}, {5, 8, 4, 5, 8, 4, 5, 8}});
    }

    @Test
    public void test2() {
        test(new Integer[] {2, 7, 5, 2, 6, null, 9,
                            null, null, 5, 8, null, null, 4},
             new int[][] {{2}, {7, 5}, {2, 6, 9}, {5, 8, 4}});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("LeveledTreeList");
    }
}
