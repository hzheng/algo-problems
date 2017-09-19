import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC250: https://leetcode.com/problems/count-univalue-subtrees/?tab=Description
//
// Given a binary tree, count the number of uni-value subtrees.
// A Uni-value subtree means all nodes of the subtree have the same value.
public class CountUnivalSubtrees {
    // Recursion + DFS
    // beats 20.62%(1 ms for 197 tests)
    public int countUnivalSubtrees(TreeNode root) {
        int[] res = new int[1];
        dfs(root, res);
        return res[0];
    }

    private boolean dfs(TreeNode root, int[] count) {
        if (root == null) return true;

        boolean isUnival = dfs(root.left, count);
        if (isUnival &= dfs(root.right, count)) { // avoid short circuit
            if ((root.left == null || root.left.val == root.val)
                && (root.right == null || root.right.val == root.val)) {
                count[0]++;
                return true;
            }
        }
        return false;
    }

    // Recursion + DFS
    // beats 20.62%(1 ms for 197 tests)
    public int countUnivalSubtrees2(TreeNode root) {
        if (root == null) return 0;

        int[] res = new int[1];
        dfs2(root, res);
        return res[0];
    }

    private boolean dfs2(TreeNode root, int[] count) {
        boolean leftUni = (root.left == null)
                          || dfs2(root.left, count) && (root.val == root.left.val);
        boolean rightUni = (root.right == null)
                           || dfs2(root.right, count) && (root.val == root.right.val);
        return leftUni && rightUni && ++count[0] > 0;
    }

    // TODO: Iterative solution

    void test(String s, int expected) {
        assertEquals(expected, countUnivalSubtrees(TreeNode.of(s)));
        assertEquals(expected, countUnivalSubtrees2(TreeNode.of(s)));
    }

    @Test
    public void test1() {
        test("0,#,1", 1);
        test("5,1,5,5,5,#,5", 4);
        test("5,1,5,5,5,5,5,5,#,6,5", 7);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("CountUnivalSubtrees");
    }
}
