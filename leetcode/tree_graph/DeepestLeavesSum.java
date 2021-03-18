import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

import common.TreeNode;

// LC1302: https://leetcode.com/problems/deepest-leaves-sum/
//
// Given the root of a binary tree, return the sum of values of its deepest leaves.
//
// Constraints:
// The number of nodes in the tree is in the range [1, 10^4].
// 1 <= Node.val <= 100
public class DeepestLeavesSum {
    // DFS + Recursion
    // time complexity: O(N), space complexity: O(H)
    // 1 ms(80.88%), 40.7 MB(56.95%) for 15 tests
    public int deepestLeavesSum(TreeNode root) {
        return getSum(root, getDepth(root) - 1);
    }

    private int getSum(TreeNode root, int depth) {
        if (root == null) { return 0; }

        int res = (depth == 0) ? root.val : 0;
        return res + getSum(root.left, depth - 1) + getSum(root.right, depth - 1);
    }

    private int getDepth(TreeNode root) {
        if (root == null) { return 0; }

        return Math.max(getDepth(root.left), getDepth(root.right)) + 1;
    }

    // BFS + Queue
    // time complexity: O(N), space complexity: O(H)
    // 3 ms(46.18%), 39.7 MB(97.48%) for 15 tests
    public int deepestLeavesSum2(TreeNode root) {
        int res = 0;
        Queue<TreeNode> queue = new LinkedList<>();
        for (queue.offer(root); !queue.isEmpty(); ) {
            res = 0;
            for (int i = queue.size(); i > 0; i--) {
                TreeNode node = queue.poll();
                res += node.val;
                if (node.right != null) {
                    queue.offer(node.right);
                }
                if (node.left  != null) {
                    queue.offer(node.left);
                }
            }
        }
        return res;
    }

    private void test(String s, int expected) {
        TreeNode root = TreeNode.of(s);
        assertEquals(expected, deepestLeavesSum(root));
        assertEquals(expected, deepestLeavesSum2(root));
    }

    @Test public void test() {
        test("1,2,3,4,5,#,6,7,#,#,#,#,8", 15);
        test("6,7,8,2,7,1,3,9,#,1,4,#,#,#,5", 19);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
