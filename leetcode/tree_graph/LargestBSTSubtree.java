import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC333: https://leetcode.com/problems/largest-bst-subtree
//
// Given a binary tree, find the largest subtree which is a Binary Search Tree,
// where largest means subtree with largest number of nodes in it.
// Note:
// A subtree must include all of its descendants.
public class LargestBSTSubtree {
    private static class Result {
        int min;
        int max;
        int count;

        static final Result EMPTY = new Result(Integer.MAX_VALUE, Integer.MIN_VALUE, 0);
        static final Result NULL = new Result(Integer.MIN_VALUE, Integer.MAX_VALUE, 0);

        Result(int min, int max, int count) {
            this.min = min;
            this.max = max;
            this.count = count;
        }
    }

    // Recursion + Postorder
    // time complexity: O(N)
    // beats 78.54%(7 ms for 73 tests)
    public int largestBSTSubtree(TreeNode root) {
        // Result res = new Result(0, 0, 0);
        int[] max = new int[1];
        postorder(root, max);
        return max[0];
    }

    private Result postorder(TreeNode root, int[] max) {
        if (root == null) return null;

        Result left = postorder(root.left, max);
        Result right = postorder(root.right, max);
        if (left == null && root.left != null || left != null && left.max >= root.val) return null;
        if (right == null && root.right != null || right != null && right.min <= root.val) return null;

        int count = (left == null ? 0 : left.count) + (right == null ? 0 : right.count) + 1;
        Result res = new Result(left == null ? root.val : left.min,
                                right == null ? root.val : right.max, count);
        max[0] = Math.max(max[0], count);
        return res;
    }

    // Recursion + Postorder
    // time complexity: O(N)
    // beats 47.64%(8 ms for 73 tests)
    public int largestBSTSubtree2(TreeNode root) {
        int[] max = new int[1];
        postorder2(root, max);
        return max[0];
    }

    private Result postorder2(TreeNode root, int[] max) {
        if (root == null) return Result.EMPTY;

        Result left = postorder2(root.left, max);
        Result right = postorder2(root.right, max);
        if (left.max >= root.val || right.min <= root.val) return Result.NULL;

        int count = left.count + right.count + 1;
        max[0] = Math.max(max[0], count);
        return new Result(Math.min(root.val, left.min), Math.max(root.val, right.max), count);
    }

    // Recursion
    // time complexity: average - O(N ^ log(N)), worst - O(N ^ 2)
    // beats 99.14%(6 ms for 73 tests)
    public int largestBSTSubtree3(TreeNode root) {
        if (root == null) return 0;

        int count = checkValidBST(root, Integer.MIN_VALUE, Integer.MAX_VALUE);
        if (count >= 0) return count;
        
        return Math.max(largestBSTSubtree3(root.left), largestBSTSubtree3(root.right));
    }

    private int checkValidBST(TreeNode root, long min, long max) {
        if (root == null) return 0;

        if (root.val <= min || root.val >= max) return -1;

        int left = checkValidBST(root.left, min, root.val);
        int right = checkValidBST(root.right, root.val, max);
        return (left < 0 || right < 0) ? -1 : left + right + 1;
    }

    void test(String s, int expected) {
        assertEquals(expected, largestBSTSubtree(TreeNode.of(s)));
        assertEquals(expected, largestBSTSubtree2(TreeNode.of(s)));
        assertEquals(expected, largestBSTSubtree3(TreeNode.of(s)));
    }

    @Test
    public void test1() {
        test("1,2", 1);
        test("1,3,2,4,#,#,5", 2);
        test("10,5,15,1,8,#,7", 3);
        test("4,2,7,2,3,5,#,2,#,#,#,#,#,1", 2);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("LargestBSTSubtree");
    }
}
