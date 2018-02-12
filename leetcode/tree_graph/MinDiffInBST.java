import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC783: https://leetcode.com/problems/minimum-distance-between-bst-nodes/
//
// Given a Binary Search Tree, return the minimum difference between the values
// of any two different nodes in the tree.
// Note:
// The size of the BST will be between 2 and 100.
// The BST is always valid, each node's value is an integer, and each node's
// value is different.
public class MinDiffInBST {
    // DFS + Stack
    // beats %(6 ms for 45 tests)
    public int minDiffInBST(TreeNode root) {
        int last = Integer.MAX_VALUE;
        int min = Integer.MAX_VALUE;
        Stack<TreeNode> stack = new Stack<>();
        for (TreeNode n = root; n != null || !stack.empty(); ) {
            if (n != null) {
                stack.push(n);
                n = n.left;
            } else {
                TreeNode top = stack.pop();
                if (last != Integer.MAX_VALUE) {
                    min = Math.min(min, top.val - last);
                }
                last = top.val;
                n = top.right;
            }
        }
        return min;
    }

    // DFS + Recursion
    // beats %(4 ms for 45 tests)
    public int minDiffInBST2(TreeNode root) {
        int[] res = new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE};
        dfs(root, res);
        return res[0];
    }

    private void dfs(TreeNode root, int[] res) {
        if (root == null) return;

        dfs(root.left, res);
        if (res[1] != Integer.MAX_VALUE) {
            res[0] = Math.min(res[0], root.val - res[1]);
        }
        res[1] = root.val;
        dfs(root.right, res);
    }

    void test(String root, int expected) {
        assertEquals(expected, minDiffInBST(TreeNode.of(root)));
        assertEquals(expected, minDiffInBST2(TreeNode.of(root)));
    }

    @Test
    public void test() {
        test("4,2,6,1,3", 1);
        test("8,6,12,2,#,10", 2);
        test("90,69,#,49,89,#,52,#,#,#,#", 1);
        test("96,12,#,#,13,#,52,29,#,#,#", 1);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
