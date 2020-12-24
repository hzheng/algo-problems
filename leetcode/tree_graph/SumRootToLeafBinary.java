import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.function.Function;

import common.TreeNode;

// LC1022: https://leetcode.com/problems/sum-of-root-to-leaf-binary-numbers/
//
// You are given the root of a binary tree where each node has a value 0 or 1.  Each root-to-leaf
// path represents a binary number starting with the most significant bit.  For example, if the path
// is 0 -> 1 -> 1 -> 0 -> 1, then this could represent 01101 in binary, which is 13. For all leaves
// in the tree, consider the numbers represented by the path from the root to that leaf.
// Return the sum of these numbers. The answer is guaranteed to fit in a 32-bits integer.
//
// Constraints:
// The number of nodes in the tree is in the range [1, 1000].
// Node.val is 0 or 1.
public class SumRootToLeafBinary {
    // DFS + Recursion
    // time complexity: O(N), space complexity: O(H)
    // 0 ms(100.00%), 38.1 MB(89.73%) for 63 tests
    public int sumRootToLeaf(TreeNode root) {
        int[] res = new int[1];
        dfs(root, 0, res);
        return res[0];
    }

    private void dfs(TreeNode root, int cur, int[] res) {
        if (root == null) { return; }

        cur <<= 1;
        cur += root.val;
        if (root.left == null && root.right == null) {
            res[0] += cur;
        }
        dfs(root.left, cur, res);
        dfs(root.right, cur, res);
    }

    // DFS + Recursion
    // time complexity: O(N), space complexity: O(H)
    // 0 ms(100.00%), 38.3 MB(69.01%) for 63 tests
    public int sumRootToLeaf2(TreeNode root) {
        return dfs(root, 0);
    }

    private int dfs(TreeNode root, int cur) {
        if (root == null) { return 0; }

        cur <<= 1;
        cur += root.val;
        if (root.left == null && root.right == null) { return cur; }

        return dfs(root.left, cur) + dfs(root.right, cur);
    }

    // Stack (preorder traversal)
    // time complexity: O(N), space complexity: O(H)
    // 2 ms(30.02%), 38.3 MB(69.01%) for 63 tests
    public int sumRootToLeaf3(TreeNode root) {
        Stack<Object[]> stack = new Stack<>();
        int res = 0;
        for (Object[] cur = {root, 0}; cur != null || !stack.isEmpty(); ) {
            if (cur != null) {
                stack.push(cur);
                TreeNode node = (TreeNode)cur[0];
                int val = (((int)cur[1]) << 1) | node.val;
                if (node.left == null) {
                    cur = null;
                    if (node.right == null) {
                        res += val;
                    }
                } else {
                    cur = new Object[] {node.left, val};
                }
            } else {
                Object[] top = stack.pop();
                TreeNode node = (TreeNode)top[0];
                TreeNode right = node.right;
                int val = (((int)top[1]) << 1) | node.val;
                cur = (right == null) ? null : new Object[] {right, val};
            }
        }
        return res;
    }

    // Stack
    // time complexity: O(N), space complexity: O(H)
    // 2 ms(30.02%), 38.2 MB(89.73%) for 63 tests
    public int sumRootToLeaf4(TreeNode root) {
        Stack<Object[]> stack = new Stack<>();
        int res = 0;
        for (stack.push(new Object[] {root, 0}); !stack.isEmpty(); ) {
            Object[] cur = stack.pop();
            TreeNode node = (TreeNode)cur[0];
            if (node == null) { continue; }

            int val = (int)cur[1];
            val = (val << 1) | node.val;
            if (node.left == null && node.right == null) {
                res += val;
            } else {
                stack.push(new Object[] {node.left, val});
                stack.push(new Object[] {node.right, val});
            }
        }
        return res;
    }

    // BFS + Queue
    // time complexity: O(N), space complexity: O(H)
    // 2 ms(30.02%), 38.1 MB(89.73%) for 63 tests
    public int sumRootToLeaf5(TreeNode root) {
        Queue<Object[]> queue = new LinkedList<>();
        int res = 0;
        for (queue.offer(new Object[] {root, 0}); !queue.isEmpty(); ) {
            Object[] cur = queue.poll();
            TreeNode node = (TreeNode)cur[0];
            if (node == null) { continue; }

            int val = (int)cur[1];
            val = (val << 1) | node.val;
            if (node.left == null && node.right == null) {
                res += val;
            } else {
                queue.offer(new Object[] {node.left, val});
                queue.offer(new Object[] {node.right, val});
            }
        }
        return res;
    }

    void test(Function<TreeNode, Integer> sumRootToLeaf, String s, int expected) {
        assertEquals(expected, (int)sumRootToLeaf.apply(TreeNode.of(s)));
    }

    private void test(String root, int expected) {
        SumRootToLeafBinary s = new SumRootToLeafBinary();
        test(s::sumRootToLeaf, root, expected);
        test(s::sumRootToLeaf2, root, expected);
        test(s::sumRootToLeaf3, root, expected);
        test(s::sumRootToLeaf4, root, expected);
        test(s::sumRootToLeaf5, root, expected);
    }

    @Test public void test() {
        test("0,1,0,0,#,0,0,#,#,#,1,#,#,#,1", 5);
        test("1,0,1,0,1,0,1", 22);
        test("0", 0);
        test("1", 1);
        test("1,1", 3);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
