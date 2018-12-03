import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC951: https://leetcode.com/problems/flip-equivalent-binary-trees/
//
// For a binary tree T, we can define a flip operation as follows: choose any 
// node, and swap the left and right child subtrees. A binary tree X is flip 
// equivalent to a binary tree Y if and only if we can make X equal to Y after 
// some number of flip operations. Write a function that determines whether two 
// binary trees are flip equivalent.
// Note:
// Each tree will have at most 100 nodes.
// Each value in each tree will be a unique integer in the range [0, 99].
public class FlipEquiv {
    // Recursion
    // beats %(3 ms for 72 tests)
    public boolean flipEquiv(TreeNode root1, TreeNode root2) {
        if (root1 == root2) return true;
        if (root1 == null || root2 == null || root1.val != root2.val) return false;

        return (flipEquiv(root1.left, root2.left) && flipEquiv(root1.right, root2.right))
               || (flipEquiv(root1.right, root2.left) && flipEquiv(root1.left, root2.right));
    }

    // Recursion
    // beats %(5 ms for 72 tests)
    public boolean flipEquiv2(TreeNode root1, TreeNode root2) {
        List<Integer> vals1 = new ArrayList<>();
        List<Integer> vals2 = new ArrayList<>();
        dfs(root1, vals1);
        dfs(root2, vals2);
        return vals1.equals(vals2);
    }

    private void dfs(TreeNode node, List<Integer> vals) {
        if (node == null) return;

        vals.add(node.val);
        int left = (node.left != null) ? node.left.val : -1;
        int right = (node.right != null) ? node.right.val : -1;
        if (left < right) {
            dfs(node.left, vals);
            dfs(node.right, vals);
        } else {
            dfs(node.right, vals);
            dfs(node.left, vals);
        }
    }

    void test(String tree1, String tree2, boolean expected) {
        TreeNode root1 = TreeNode.of(tree1);
        TreeNode root2 = TreeNode.of(tree2);
        assertEquals(expected, flipEquiv(root1, root2));
        assertEquals(expected, flipEquiv2(root1, root2));
    }

    @Test
    public void test1() {
        test("1,2,3,4,5,6,#,#,#,7,8", "1,3,2,#,6,4,5,#,#,#,#,8,7", true);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
