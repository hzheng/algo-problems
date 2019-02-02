import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC971: https://leetcode.com/problems/flip-binary-tree-to-match-preorder-traversal/
//
// Given a binary tree with N nodes, each node has a different value from 1 to 
// N. A node in this tree can be flipped by swapping the left child and the 
// right child of that node. Consider the sequence of N values reported by a 
// preorder traversal starting from the root.  Call such a sequence of N values
// the voyage of the tree. Our goal is to flip the least number of nodes in the
// tree so that the voyage of the tree matches the voyage we are given.
// If we can do so, then return a list of the values of all nodes flipped. You 
// may return the answer in any order. Otherwise, return the list [-1].
// Note:
// 1 <= N <= 100
public class FlipMatchVoyage {
    // Stack
    // time complexity: O(N), space complexity: O(N)
    // beats 55.88%(5 ms for 96 tests)
    public List<Integer> flipMatchVoyage(TreeNode root, int[] voyage) {
        List<Integer> res = new ArrayList<>();
        Stack<TreeNode> stack = new Stack<>();
        int i = 0;
        for (TreeNode cur = root; !stack.isEmpty() || cur != null;) {
            if (cur != null) {
                stack.push(cur);
                if (voyage[i++] != cur.val) return Arrays.asList(-1);

                if (cur.left != null && cur.left.val != voyage[i]) {
                    res.add(cur.val);
                    TreeNode tmp = cur.left;
                    cur.left = cur.right;
                    cur.right = tmp;
                }
                cur = cur.left;
            } else {
                cur = stack.pop().right;
            }
        }
        return res;
    }

    // Stack
    // time complexity: O(N), space complexity: O(N)
    // beats 55.88%(5 ms for 96 tests)
    public List<Integer> flipMatchVoyage2(TreeNode root, int[] voyage) {
        List<Integer> res = new ArrayList<>();
        int i = 0;
        Stack<TreeNode> stack = new Stack<>();
        for (stack.push(root); !stack.isEmpty();) {
            TreeNode node = stack.pop();
            if (node == null) continue;

            if (node.val != voyage[i++]) return Arrays.asList(-1);

            if (node.right != null && node.right.val == voyage[i]) {
                if (node.left != null) {
                    res.add(node.val);
                }
                stack.push(node.left);
                stack.push(node.right);
            } else {
                stack.push(node.right);
                stack.push(node.left);
            }
        }
        return res;
    }

    // DFS + Recursion
    // time complexity: O(N), space complexity: O(N)
    // beats 100.00%(4 ms for 96 tests)
    public List<Integer> flipMatchVoyage3(TreeNode root, int[] voyage) {
        List<Integer> res = new ArrayList<>();
        return dfs(root, voyage, new int[1], res) ? res : Arrays.asList(-1);
    }

    private boolean dfs(TreeNode node, int[] voyage, int[] index, List<Integer> res) {
        if (node == null) return true;

        if (node.val != voyage[index[0]++]) return false;

        if (node.left != null && node.left.val != voyage[index[0]]) {
            res.add(node.val);
            return dfs(node.right, voyage, index, res) && dfs(node.left, voyage, index, res);
        }
        return dfs(node.left, voyage, index, res) && dfs(node.right, voyage, index, res);
    }

    // DFS + Recursion
    // time complexity: O(N), space complexity: O(N)
    // beats 100.00%(4 ms for 96 tests)
    public List<Integer> flipMatchVoyage4(TreeNode root, int[] voyage) {
        List<Integer> res = new ArrayList<>();
        return (dfs(root, voyage, 0, res) >= 0) ? res : Arrays.asList(-1);
    }

    private int dfs(TreeNode root, int[] voyage, int i, List<Integer> res) {
        if (root == null) return i;
        if (root.val != voyage[i]) return -1;

        int left = dfs(root.left, voyage, i + 1, res);
        if (left >= 0) return dfs(root.right, voyage, left, res);

        res.add(root.val);
        int right = dfs(root.right, voyage, i + 1, res);
        return (right >= 0) ? dfs(root.left, voyage, right, res) : -1;
    }

    void test(String tree, int[] voyage, Integer[] expected) {
        List<Integer> exp = Arrays.asList(expected);
        assertEquals(exp, flipMatchVoyage(TreeNode.of(tree), voyage));
        assertEquals(exp, flipMatchVoyage2(TreeNode.of(tree), voyage));
        assertEquals(exp, flipMatchVoyage3(TreeNode.of(tree), voyage));
        assertEquals(exp, flipMatchVoyage4(TreeNode.of(tree), voyage));
    }

    @Test
    public void test() {
        test("1,2", new int[] {2, 1}, new Integer[] {-1});
        test("1,2,3", new int[] {1, 3, 2}, new Integer[] {1});
        test("1,2,3", new int[] {1, 2, 3}, new Integer[] {});
        test("1,2,3,4,#,#,5", new int[] {1, 3, 5, 2, 4}, new Integer[] {1});
        test("1,2,3,4,#,6,5", new int[] {1, 3, 5, 6, 2, 4}, new Integer[] {1, 3});
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
