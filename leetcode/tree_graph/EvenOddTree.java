import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

import common.TreeNode;

// LC1609: https://leetcode.com/problems/even-odd-tree/
//
// A binary tree is named Even-Odd if it meets the following conditions:
//
//The root of the binary tree is at level index 0, its children are at level index 1, their children
// are at level index 2, etc. For every even-indexed level, all nodes at the level have odd integer
// values in strictly increasing order (from left to right). For every odd-indexed level, all nodes
// at the level have even integer values in strictly decreasing order (from left to right).
// Given a binary tree, return true if the binary tree is Even-Odd, otherwise return false.
// Constraints:
// The number of nodes in the tree is in the range [1, 10^5].
// 1 <= Node.val <= 10^6
public class EvenOddTree {
    // Queue + BFS
    // time complexity: O(N), space complexity: O(N)
    // 8 ms(99.31%), 55.9 MB(89.79%) for 105 tests
    public boolean isEvenOddTree(TreeNode root) {
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);
        for (int level = 0; !q.isEmpty(); level++) {
            boolean levelEven = (level % 2 == 0);
            int prev = levelEven ? -1 : Integer.MAX_VALUE - 1; // MAX_VALUE is odd
            for (int i = q.size(); i > 0; i--) {
                TreeNode cur = q.poll();
                if ((cur.val - prev) % 2 != 0) { return false; }
                if (levelEven) {
                    if (cur.val <= prev) { return false; }
                } else if (cur.val >= prev) { return false; }

                prev = cur.val;
                if (cur.left != null) {
                    q.offer(cur.left);
                }
                if (cur.right != null) {
                    q.offer(cur.right);
                }
            }
        }
        return true;
    }

    // DFS + Recursion
    // time complexity: O(N), space complexity: O(N)
    // 16 ms(64.08%), 316.9 MB(5.06%) for 105 tests
    public boolean isEvenOddTree2(TreeNode root) {
        return dfs(root, 0, new HashMap<>());
    }

    private boolean dfs(TreeNode root, int level, Map<Integer, Integer> map) {
        if (root == null) { return true; }

        int cur = root.val;
        Integer prev = map.put(level, cur);
        boolean levelEven = (level % 2) == 0;
        if (prev == null) {
            prev = levelEven ? -1 : Integer.MAX_VALUE - 1; // MAX_VALUE is odd
        }
        if ((cur - prev) % 2 != 0) { return false; }

        if (levelEven) {
            if (cur <= prev) { return false; }
        } else if (cur >= prev) { return false; }
        return dfs(root.left, level + 1, map) && dfs(root.right, level + 1, map);
    }

    void test(String tree, boolean expected) {
        TreeNode root = TreeNode.of(tree);
        assertEquals(expected, isEvenOddTree(root));
        assertEquals(expected, isEvenOddTree2(root));
    }

    @Test public void test1() {
        test("1,10,4,3,#,7,9,12,8,6,#,#,2", true);
        test("5,4,2,3,3,7", false);
        test("5,9,1,3,5,7", false);
        test("1", true);
        test("11,8,6,1,3,9,11,30,20,18,16,12,10,4,2,17", true);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
