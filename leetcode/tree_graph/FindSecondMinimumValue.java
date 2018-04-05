import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC671: https://leetcode.com/problems/second-minimum-node-in-a-binary-tree/
//
// Given a non-empty binary tree consisting of nodes with the non-negative
// value, where each node in this tree has exactly two or zero sub-node. If the
// node has two sub-nodes, then this node's value is the smaller value among its
// two sub-nodes.
// Given such a binary tree, you need to output the second minimum value in the
// set made of all the nodes' value in the whole tree.
// If no such second minimum value exists, output -1 instead.
public class FindSecondMinimumValue {
    // Recursion
    // beats 100%(3 ms for tests)
    public int findSecondMinimumValue(TreeNode root) {
        if (root.left == null) return -1;

        int left = (root.left.val > root.val)
                   ? root.left.val : findSecondMinimumValue(root.left);
        int right = (root.right.val > root.val)
                    ? root.right.val : findSecondMinimumValue(root.right);
        if (left < 0) return right;
        if (right < 0) return left;
        return Math.min(left, right);
    }

    // Recursion
    // beats 100%(3 ms for tests)
    public int findSecondMinimumValue_2(TreeNode root) {
        return secondMin(root, root.val);
    }

    private int secondMin(TreeNode root, int min) {
        if (root == null) return -1;

        if (root.val != min) return root.val;

        int left = secondMin(root.left, min);
        int right = secondMin(root.right, min);
        if (left < 0) return right;
        if (right < 0) return left;
        return Math.min(left, right);
    }


    // Recursion + Set
    // beats 38.52%(4 ms for tests)
    public int findSecondMinimumValue2(TreeNode root) {
        Set<Integer> vals = new HashSet<>();
        dfs(root, vals);
        int min = root.val;
        long res = Long.MAX_VALUE;
        for (int v : vals) {
            if (v > min && v < res) {
                res = v;
            }
        }
        return res < Long.MAX_VALUE ? (int)res : -1;
    }

    private void dfs(TreeNode root, Set<Integer> vals) {
        if (root == null) return;

        vals.add(root.val);
        dfs(root.left, vals);
        dfs(root.right, vals);
    }

    // Recursion
    // beats 100%(3 ms for tests)
    public int findSecondMinimumValue3(TreeNode root) {
        long[] res = {Long.MAX_VALUE};
        dfs(root, root.val, res);
        return res[0] < Long.MAX_VALUE ? (int)res[0] : -1;
    }

    private void dfs(TreeNode root, int min, long[] res) {
        if (root == null) return;

        if (min == root.val) {
            dfs(root.left, min, res);
            dfs(root.right, min, res);
        } else if (root.val < res[0]) {
            res[0] = root.val;
        }
    }

    // BFS + Queue
    // beats 100%(3 ms for tests)
    public int findSecondMinimumValue4(TreeNode root) {
        int res = -1;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        for (int min = root.val; !queue.isEmpty(); ) {
            TreeNode node = queue.poll();
            if (node.val != min) {
                if (res < 0 || res > node.val) {
                    res = node.val;
                }
            } else if (node.left != null) {
                queue.offer(node.left);
                queue.offer(node.right);
            }
        }
        return res;
    }

    void test(String tree, int expected) {
        assertEquals(expected, findSecondMinimumValue(TreeNode.of(tree)));
        assertEquals(expected, findSecondMinimumValue_2(TreeNode.of(tree)));
        assertEquals(expected, findSecondMinimumValue2(TreeNode.of(tree)));
        assertEquals(expected, findSecondMinimumValue3(TreeNode.of(tree)));
        assertEquals(expected, findSecondMinimumValue4(TreeNode.of(tree)));
    }

    @Test
    public void test() {
        test("1,1,3,1,1,3,4,3,1,1,1,3,8,4,8,3,3,1,6,2,1", 2);
        test("2,2,5,#,#,5,7", 5);
        test("2,2,2", -1);
        test("2,2147483647,2147483647", 2147483647);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
