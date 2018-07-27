import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

import common.TreeNode;

// LC872: https://leetcode.com/problems/leaf-similar-trees/
// Consider all the leaves of a binary tree.  From left to right order, the
// values of those leaves form a leaf value sequence. Two binary trees are
// considered leaf-similar if their leaf value sequence is the same.
// Return true if and only if the two given trees with head nodes root1 and
// root2 are leaf-similar.
public class LeafSimilar {
    // Recursion
    // time complexity: O(N), space complexity: O(N)
    // beats 44.44%(3 ms for 36 tests)
    public boolean leafSimilar(TreeNode root1, TreeNode root2) {
        List<Integer> l1 = new ArrayList<>();
        getLeaves(root1, l1);
        List<Integer> l2 = new ArrayList<>();
        getLeaves(root2, l2);
        return l1.equals(l2);
    }

    private void getLeaves(TreeNode root, List<Integer> leaves) {
        if (root == null) return;

        if (root.left == null && root.right == null) {
            leaves.add(root.val);
        } else {
            getLeaves(root.left, leaves);
            getLeaves(root.right, leaves);
        }
    }

    // Recursion + Stack
    // time complexity: O(N), space complexity: O(log(N))
    // beats 44.44%(3 ms for 36 tests)
    public boolean leafSimilar2(TreeNode root1, TreeNode root2) {
        Stack<TreeNode> s1 = new Stack<>();
        Stack<TreeNode> s2 = new Stack<>();
        s1.push(root1);
        s2.push(root2);
        while (!s1.empty() && !s2.empty()) {
            if (dfs(s1) != dfs(s2)) return false;
        }
        return s1.empty() && s2.empty();
    }

    private int dfs(Stack<TreeNode> s) {
        while (true) {
            TreeNode node = s.pop();
            if (node.right != null) {
                s.push(node.right);
            }
            if (node.left != null) {
                s.push(node.left);
            }
            if (node.left == null && node.right == null) return node.val;
        }
    }

    void test(String root1, String root2, boolean expected) {
        assertEquals(expected,
                     leafSimilar(TreeNode.of(root1), TreeNode.of(root2)));
    }

    @Test
    public void test() {
        test("3,5,1,6,2,9,8,#,#,7,4",
             "3,5,1,6,7,4,2,#,#,#,#,#,#,9,8", true);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
