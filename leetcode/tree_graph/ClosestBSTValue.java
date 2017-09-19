import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC270: https://leetcode.com/problems/closest-binary-search-tree-value
//
// Given a non-empty binary search tree and a target value, find the value in
// the BST that is closest to the target.
public class ClosestBSTValue {
    // DFS + Recursion
    // beats 8.51%(1 ms for 66 tests)
    public int closestValue(TreeNode root, double target) {
        double[] res = new double[] {Double.MAX_VALUE, 0};
        dfs(root, target, res);
        return (int)res[1];
    }

    private void dfs(TreeNode root, double target, double[] res) {
        if (root == null) return;

        dfs(root.left, target, res);
        double diff = Math.abs(root.val - target);
        if (res[0] > diff) {
            res[0] = diff;
            res[1] = root.val;
        }
        dfs(root.right, target, res);
    }

    // DFS(prune) + Recursion
    // beats 8.51%(1 ms for 66 tests)
    public int closestValue2(TreeNode root, double target) {
        double[] res = new double[] {Double.MAX_VALUE, 0};
        dfs2(root, target, res);
        return (int)res[1];
    }

    private void dfs2(TreeNode root, double target, double[] res) {
        if (root == null) return;

        dfs2(root.left, target, res);
        double diff = root.val - target;
        boolean changeSign = (res[0] != Double.MAX_VALUE) && ((diff > 0) ^ (res[0] > 0));
        if (Math.abs(res[0]) > Math.abs(diff)) {
            res[0] = diff;
            res[1] = root.val;
        }
        if (diff != 0 && !changeSign) { // prune
            dfs2(root.right, target, res);
        }
    }

    // DFS(prune) + Recursion
    // beats 8.51%(1 ms for 66 tests)
    public int closestValue3(TreeNode root, double target) {
        int a = root.val;
        TreeNode child = (a > target) ? root.left : root.right;
        if (child == null) return a;

        int b = closestValue3(child, target);
        return Math.abs(a - target) < Math.abs(b - target) ? a : b;
    }

    // beats 8.51%(1 ms for 66 tests)
    public int closestValue4(TreeNode root, double target) {
        int res = root.val;
        for (TreeNode cur = root; cur != null; ) {
            if (Math.abs(target - cur.val) < Math.abs(target - res)) {
                res = cur.val;
            }
            cur = (cur.val > target) ? cur.left : cur.right;
        }
        return res;
    }

    void test(String s, double target, int expected) {
        assertEquals(expected, closestValue(TreeNode.of(s), target));
        assertEquals(expected, closestValue2(TreeNode.of(s), target));
        assertEquals(expected, closestValue3(TreeNode.of(s), target));
        assertEquals(expected, closestValue4(TreeNode.of(s), target));
    }

    @Test
    public void test1() {
        test("1,#,2,#,3", 1.1, 1);
        test("1,#,2,#,3,#,4,#,5", 1.1, 1);
        test("1500000000,1400000000", -1500000000.0, 1400000000);
        test("2147483647", 0.0, 2147483647);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ClosestBSTValue");
    }
}
