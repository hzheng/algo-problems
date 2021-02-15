import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

import common.TreeNode;

// LC1145: https://leetcode.com/problems/binary-tree-coloring-game/
//
// Two players play a turn based game on a binary tree.  We are given the root of this binary tree,
// and the number of nodes n in the tree.  n is odd, and each node has a distinct value from 1 to n.
// Initially, the first player names a value x with 1 <= x <= n, and the second player names a value
// y with 1 <= y <= n and y != x.  The first player colors the node with value x red, and the second
// player colors the node with value y blue. Then, the players take turns starting with the first
// player. In each turn, that player chooses a node of their color (red if player 1, blue if player
// 2) and colors an uncolored neighbor of the chosen node (either the left child, right child, or
// parent of the chosen node.) If (and only if) a player cannot choose such a node in this way, they
// must pass their turn. If both players pass their turn, the game ends, and the winner is the
// player that colored more nodes. You are the second player.  If it is possible to choose such a y
// to ensure you win the game, return true. If it is not possible, return false.
//
// Constraints:
// root is the root of a binary tree with n nodes and distinct node values from 1 to n.
// n is odd.
// 1 <= x <= n <= 100
public class BinaryTreeColoringGame {
    // DFS + Recursion
    // time complexity: O(N), space complexity: O(H)
    // 0 ms(100.00%), 37 MB(27.81%) for 65 tests
    public boolean btreeGameWinningMove(TreeNode root, int n, int x) {
        TreeNode node = find(root, x);
        int left = count(node.left);
        int right = count(node.right);
        return ((left + right + 1) * 2 < n) || (left * 2 > n) || (right * 2 > n);
    }

    private TreeNode find(TreeNode root, int x) {
        if (root == null || root.val == x) { return root; }

        TreeNode left = find(root.left, x);
        return (left != null) ? left : find(root.right, x);
    }

    private int count(TreeNode cur) {
        if (cur == null) { return 0; }
        return 1 + count(cur.left) + count(cur.right);
    }

    // DFS + Recursion
    // time complexity: O(N), space complexity: O(H)
    // 0 ms(100.00%), 37 MB(27.81%) for 65 tests
    public boolean btreeGameWinningMove2(TreeNode root, int n, int x) {
        int[] count = new int[2];
        count(root, x, count);
        int left = count[0];
        int right = count[1];
        return Math.max(Math.max(left, right), n - left - right - 1) > n / 2;
    }

    private int count(TreeNode root, int x, int[] count) {
        if (root == null) { return 0; }

        int left = count(root.left, x, count);
        int right = count(root.right, x, count);
        if (root.val == x) {
            count[0] = left;
            count[1] = right;
        }
        return left + right + 1;
    }

    @FunctionalInterface interface Function<A, B, C, D> {
        D apply(A a, B b, C c);
    }

    void test(Function<TreeNode, Integer, Integer, Boolean> btreeGameWinningMove, String s, int n,
              int x, boolean expected) {
        assertEquals(expected, btreeGameWinningMove.apply(TreeNode.of(s), n, x));
    }

    private void test(String root, int n, int x, boolean expected) {
        BinaryTreeColoringGame b = new BinaryTreeColoringGame();
        test(b::btreeGameWinningMove, root, n, x, expected);
        test(b::btreeGameWinningMove2, root, n, x, expected);
    }

    @Test public void test() {
        test("1,2,3,4,5,6,7,8,9,10,11", 11, 3, true);
        test("1,2,3", 3, 1, false);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
