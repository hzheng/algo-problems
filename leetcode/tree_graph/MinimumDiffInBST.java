import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC530: https://leetcode.com/problems/minimum-absolute-difference-in-bst/
//
// Given a binary search tree with non-negative values, find the minimum absolute
// difference between values of any two nodes.
public class MinimumDiffInBST {
    // DFS + Recursion
    // beats 69.03%(18 ms for 186 tests)
    public int getMinimumDifference(TreeNode root) {
        int[] min = new int[] {Integer.MAX_VALUE};
        dfs(root, -1, min);
        return min[0];
    }

    private int dfs(TreeNode root, int prev, int[] min) {
        if (root.left != null) {
            prev = dfs(root.left, prev, min);
        }
        int cur = root.val;
        if (prev >= 0 && min[0] > cur - prev) {
            min[0] = cur - prev;
        }
        return (root.right == null) ? cur : dfs(root.right, cur, min);
    }

    // DFS + Recursion
    // beats 75.66%(17 ms for 186 tests)
    public int getMinimumDifference2(TreeNode root) {
        int[] res = new int[] {-1, Integer.MAX_VALUE};
        dfs2(root, res);
        return res[1];
    }

    private void dfs2(TreeNode root, int[] res) {
        if (root == null) return;

        dfs2(root.left, res);
        int cur = root.val;
        if (res[0] >= 0 && res[1] > cur - res[0]) {
            res[1] = cur - res[0];
        }
        res[0] = cur;
        dfs2(root.right, res);
    }

    // Stack
    // beats 83.78%(16 ms for 186 tests)
    public int getMinimumDifference3(TreeNode root) {
        ArrayDeque<TreeNode> stack = new ArrayDeque<>();
        int prev = -1;
        int min = Integer.MAX_VALUE;
        for (TreeNode cur = root; !stack.isEmpty() || cur != null; ) {
            if (cur != null) {
                stack.push(cur);
                cur = cur.left;
            } else {
                cur = stack.pop();
                if (prev >= 0) {
                    min = Math.min(min, cur.val - prev);
                    if (min == 0) return 0;
                }
                prev = cur.val;
                cur = cur.right;
            }
        }
        return min;
    }

    // SortedSet + DFS + Recursion
    // time complexity: O(N * log(N)), space complexity: O(N)
    // beats 10.18%(46 ms for 186 tests)
    public int getMinimumDifference4(TreeNode root) {
        int[] min = new int[] {Integer.MAX_VALUE};
        preorder(root, new TreeSet<>(), min);
        return min[0];
    }

    private void preorder(TreeNode root, NavigableSet<Integer> set, int[] min) {
        if (root == null) return;

        if (!set.isEmpty()) {
            Integer val = set.floor(root.val);
            if (val != null) {
                min[0] = Math.min(min[0], root.val - val);
            }
            val = set.ceiling(root.val);
            if (val != null) {
                min[0] = Math.min(min[0], val - root.val);
            }
        }
        set.add(root.val);
        preorder(root.left, set, min);
        preorder(root.right, set, min);
    }

    void test(String s, int expected) {
        assertEquals(expected, getMinimumDifference(TreeNode.of(s)));
        assertEquals(expected, getMinimumDifference2(TreeNode.of(s)));
        assertEquals(expected, getMinimumDifference3(TreeNode.of(s)));
        assertEquals(expected, getMinimumDifference4(TreeNode.of(s)));
    }

    @Test
    public void test() {
        test("1,#,8,6", 2);
        test("1,#,3,2", 1);
        test("1,#,2", 1);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MinimumDiffInBST");
    }
}
