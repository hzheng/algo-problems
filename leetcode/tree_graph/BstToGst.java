import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

import common.TreeNode;

// LC1038: https://leetcode.com/problems/binary-search-tree-to-greater-sum-tree/
//
// Given the root of a Binary Search Tree (BST), convert it to a Greater Tree such that every key of
// the original BST is changed to the original key plus sum of all keys greater than the original
// key in BST.
// Note: This question is the same as 538: https://leetcode.com/problems/convert-bst-to-greater-tree/
//
// Constraints:
//
// The number of nodes in the tree is in the range [1, 100].
// 0 <= Node.val <= 100
// All the values in the tree are unique.
// root is guaranteed to be a valid binary search tree.
public class BstToGst {
    // DFS + Recursion
    // time complexity: O(N), space complexity: O(H)
    // 0 ms(100.00%), 36.6 MB(48.73%) for 28 tests
    public TreeNode bstToGst(TreeNode root) {
        toGst(root, new TreeNode[]{new TreeNode(0)});
        return root;
    }

    private void toGst(TreeNode root, TreeNode[] prev) {
        if (root == null) { return; }

        toGst(root.right, prev);
        root.val += prev[0].val;
        prev[0] = root;
        toGst(root.left, prev);
    }

    // Solution of Choice
    // DFS + Recursion
    // time complexity: O(N), space complexity: O(H)
    // 0 ms(100.00%), 36.4 MB(81.03%) for 28 tests
    public TreeNode bstToGst2(TreeNode root) {
        toGst(root, 0);
        return root;
    }

    private int toGst(TreeNode root, int sum) {
        if (root == null) { return sum; }

        root.val += toGst(root.right, sum);
        return toGst(root.left, root.val);
    }

    // Solution of Choice
    // Stack
    // time complexity: O(N), space complexity: O(H)
    // 0 ms(100.00%), 36.8 MB(12.02%) for 28 tests
    public TreeNode bstToGst3(TreeNode root) {
        int sum = 0;
        Stack<TreeNode> stack = new Stack<>();
        for (TreeNode cur = root; cur != null || !stack.isEmpty(); ) {
            if (cur != null) {
                stack.push(cur);
                cur = cur.right;
            } else {
                TreeNode top = stack.pop();
                cur = top.left;
                sum += top.val;
                top.val = sum;
            }
        }
        return root;
    }

    private void test(String s, String expected) {
        assertEquals(TreeNode.of(expected), bstToGst(TreeNode.of(s)));
        assertEquals(TreeNode.of(expected), bstToGst2(TreeNode.of(s)));
        assertEquals(TreeNode.of(expected), bstToGst3(TreeNode.of(s)));
    }

    @Test public void test() {
        test("4,1,6,0,2,5,7,#,#,#,3,#,#,#,8", "30,36,21,36,35,26,15,#,#,#,33,#,#,#,8");
        test("0,#,1", "1,#,1");
        test("1,0,2", "3,3,2");
        test("3,2,4,1", "7,9,4,10");
        test("5,2,13", "18,20,13");
        test("5,2,13,1,4,10,20", "48,54,33,55,52,43,20");
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
