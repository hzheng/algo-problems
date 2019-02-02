import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC979: https://leetcode.com/problems/distribute-coins-in-binary-tree/
//
// Given the root of a binary tree with N nodes, each node in the tree has
// node.val coins, and there are N coins total.
// In one move, we may choose two adjacent nodes and move one coin from one node
// to another.  (The move may be from parent to child, or from child to parent.)
// Return the number of moves required to make every node have exactly one coin.
public class DistributeCoins {
    // DFS + Recursion
    // time complexity: O(N), space complexity: O(H)
    // 5 ms(100.00%), 26.4 MB(100.00%) for tests
    public int distributeCoins(TreeNode root) {
        return distribute(root)[0];
    }

    private int[] distribute(TreeNode root) {
        int[] res = new int[2];
        if (root == null) return res;

        int[] left = distribute(root.left);
        int[] right = distribute(root.right);
        res[1] = left[1] + right[1] + root.val - 1; // sum diff
        res[0] = left[0] + right[0] + Math.abs(res[1]); // moves
        return res;
    }

    // DFS + Recursion
    // time complexity: O(N), space complexity: O(H)
    // 5 ms(100.00%), 26.4 MB(100.00%) for tests
    public int distributeCoins2(TreeNode root) {
        int[] res = new int[1];
        dfs(root, res);
        return res[0];
    }

    private int dfs(TreeNode node, int[] res) {
        if (node == null) return 0;

        int L = dfs(node.left, res);
        int R = dfs(node.right, res);
        res[0] += Math.abs(L) + Math.abs(R);
        return L + R + node.val - 1;
    }

    void test(String tree, int expected) {
        assertEquals(expected, distributeCoins(TreeNode.of(tree)));
        assertEquals(expected, distributeCoins2(TreeNode.of(tree)));
    }

    @Test
    public void test() {
        test("3,0,0", 2);
        test("0,3,0", 3);
        test("1,0,2", 2);
        test("1,0,0,#,3", 4);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
