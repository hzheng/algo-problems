import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC549: https://leetcode.com/problems/binary-tree-longest-consecutive-sequence-ii/
//
// Given a binary tree, you need to find the length of Longest Consecutive Path 
// in Binary Tree.
public class LongestConsecutiveInTree {
    // Recursion
    // beats 63.91%(14 ms for 159 tests)
    public int longestConsecutive(TreeNode root) {
        int[] max = {0};
        longestConsecutive(root, max);
        return max[0];
    }

    private int[] longestConsecutive(TreeNode root, int[] max) {
        if (root == null) return new int[2];

        int[] res = {1, 1};
        int[] leftRes = longestConsecutive(root.left, max);
        if (root.left != null && root.left.val + 1 == root.val) {
            res[0] = leftRes[0] + 1;
        }
        if (root.left != null && root.left.val - 1 == root.val) {
            res[1] = leftRes[1] + 1;
        }
        int[] rightRes = longestConsecutive(root.right, max);
        if (root.right != null && root.right.val + 1 == root.val) {
            res[0] = Math.max(res[0], rightRes[0] + 1);
        }
        if (root.right != null && root.right.val - 1 == root.val) {
            res[1] = Math.max(res[1], rightRes[1] + 1);
        }
        max[0] = Math.max(max[0], res[0] + res[1] - 1);
        return res;
    }

    // Recursion
    // beats 86.21%(12 ms for 159 tests)
    public int longestConsecutive2(TreeNode root) {
        if (root == null) return 0;

        int[] max = new int[3];
        longestConsecutive2(root, max);
        return max[0];
    }

    private void longestConsecutive2(TreeNode root, int[] max) {
        int ascend = 0;
        int descend = 0;
        if (root.left != null) {
            longestConsecutive2(root.left, max);
            if (root.left.val + 1 == root.val) {
                ascend = max[1];
            } else if (root.left.val - 1 == root.val) {
                descend = max[2];
            }
        }
        if (root.right != null) {
            longestConsecutive2(root.right, max);
            if (root.right.val + 1 == root.val) {
                ascend = Math.max(ascend, max[1]);
            } else if (root.right.val - 1 == root.val) {
                descend = Math.max(descend, max[2]);
            }
        }
        max[0] = Math.max(max[0], ascend + descend + 1);
        max[1] = ascend + 1;
        max[2] = descend + 1;
    }

    void test(String s, int expected) {
        assertEquals(expected, longestConsecutive(TreeNode.of(s)));
        assertEquals(expected, longestConsecutive2(TreeNode.of(s)));
    }

    @Test
    public void test() {
        test("2", 1);
        test("1,2", 2);
        test("1,2,3", 2);
        test("2,1,3", 3);
        test("2,3,1", 3);
        test("3,1,#,#,2", 2);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
