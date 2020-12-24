import org.junit.Test;

import static org.junit.Assert.*;

import java.util.function.Function;

import common.TreeNode;

// LC1026: https://leetcode.com/problems/maximum-difference-between-node-and-ancestor/
//
// Given the root of a binary tree, find the maximum value V for which there exist different nodes A
// and B where V = |A.val - B.val| and A is an ancestor of B. A node A is an ancestor of B if
// either: any child of A is equal to B, or any child of A is an ancestor of B.
//
// Constraints:
// The number of nodes in the tree is in the range [2, 5000].
// 0 <= Node.val <= 10^5
public class MaxAncestorDiff {
    // DFS + Recursion
    // time complexity: O(N), space complexity: O(H)
    // 0 ms(100.00%), 39.7 MB(57.91%) for 27 tests
    public int maxAncestorDiff(TreeNode root) {
        return maxAncestorDiff(root, Integer.MAX_VALUE, Integer.MIN_VALUE);
    }

    private int maxAncestorDiff(TreeNode root, int min, int max) {
        if (root == null) { return max - min; }

        min = Math.min(min, root.val);
        max = Math.max(max, root.val);
        int leftRes = maxAncestorDiff(root.left, min, max);
        int rightRes = maxAncestorDiff(root.right, min, max);
        return Math.max(leftRes, rightRes);
    }

    void test(Function<TreeNode, Integer> maxAncestorDiff, String s, int expected) {
        assertEquals(expected, (int)maxAncestorDiff.apply(TreeNode.of(s)));
    }

    private void test(String root, int expected) {
        MaxAncestorDiff m = new MaxAncestorDiff();
        test(m::maxAncestorDiff, root, expected);
    }

    @Test public void test() {
        test("8,3,10,1,6,#,14,#,#,4,7,13", 7);
        test("1,#,2,#,0,3", 3);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
