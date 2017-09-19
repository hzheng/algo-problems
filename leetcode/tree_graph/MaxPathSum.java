import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;
import java.util.function.Function;

// LC124: https://leetcode.com/problems/binary-tree-maximum-path-sum/
//
// Given a binary tree, find the maximum path sum.
// For this problem, a path is defined as any sequence of nodes from some
// starting node to any node in the tree along the parent-child connections.
// The path does not need to go through the root
public class MaxPathSum {
    // Solution of Choice
    // beats 32.06%(2 ms)
    public int maxPathSum(TreeNode root) {
        int[] max = {Integer.MIN_VALUE};
        maxPathSum(root, max);
        return max[0];
    }

    private int maxPathSum(TreeNode root, int[] max) {
        if (root == null) return 0;

        int left = Math.max(0, maxPathSum(root.left, max));
        int right = Math.max(0, maxPathSum(root.right, max));
        max[0] = Math.max(max[0], root.val + left + right);
        return root.val + Math.max(left, right);
    }

    void test(Function<TreeNode, Integer> maxPathSum, String s, int expected) {
        TreeNode root = TreeNode.of(s);
        assertEquals(expected, (int)maxPathSum.apply(root));
    }

    void test(String s, int expected) {
        MaxPathSum m = new MaxPathSum();
        test(m::maxPathSum, s, expected);
    }

    @Test
    public void test1() {
        test("1,2,3", 6);
        test("5,4,8,11,#,13,4,7,2,5,1", 53);
        test("5,4,8,11,#,13,4,7,2,#,#,5,1", 48);
        test("5,4,-8,-11,#,13,4,7,2,5,1", 19);
        test("5,2,-8,-11,#,13,4,7,2,5,-1", 18);
        test("5,4,-8,-11,#,13,4,7,2,#,#,5,1", 14);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MaxPathSum");
    }
}
