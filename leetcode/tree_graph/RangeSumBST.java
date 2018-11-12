import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC938: https://leetcode.com/problems/range-sum-of-bst/
//
// Given the root node of a binary search tree, return the sum of values of all 
// nodes with value between L and R (inclusive).
// The binary search tree is guaranteed to have unique values.
public class RangeSumBST {
    // Recursion
    // beats %(4 ms for 42 tests)
    public int rangeSumBST(TreeNode root, int L, int R) {
        if (root == null) return 0;

        if (root.val < L) return rangeSumBST(root.right, L, R);
        if (root.val > R) return rangeSumBST(root.left, L, R);

        return root.val + rangeSumBST(root.right, L, R) + rangeSumBST(root.left, L, R);
    }

    // Stack (Inorder traverse)
    // beats %(39 ms for 42 tests)
    public int rangeSumBST2(TreeNode root, int L, int R) {
        int res = 0;
        Stack<TreeNode> stack = new Stack<>();
        for (TreeNode cur = root; cur != null || !stack.isEmpty();) {
            if (cur != null) {
                stack.push(cur);
                cur = cur.left;
            } else {
                cur = stack.pop();
                if (cur.val >= L && cur.val <= R) {
                    res += cur.val;
                }
                cur = cur.right;
            }
        }
        return res;
    }

    // Stack
    // beats %(7 ms for 42 tests)
    public int rangeSumBST3(TreeNode root, int L, int R) {
        int res = 0;
        Stack<TreeNode> stack = new Stack<>();
        for (stack.push(root); !stack.isEmpty();) {
            TreeNode cur = stack.pop();
            if (cur.val < R && cur.right != null) {
                stack.push(cur.right);
            }
            if (cur.val > L && cur.left != null) {
                stack.push(cur.left);
            }
            if (cur.val >= L && cur.val <= R) {
                res += cur.val;
            }
        }
        return res;
    }

    void test(String s, int L, int R, int expected) {
        assertEquals(expected, rangeSumBST(TreeNode.of(s), L, R));
        assertEquals(expected, rangeSumBST2(TreeNode.of(s), L, R));
        assertEquals(expected, rangeSumBST3(TreeNode.of(s), L, R));
    }

    @Test
    public void test1() {
        test("10,5,15,3,7,#,18", 7, 15, 32);
        test("10,5,15,3,7,13,18,1,#,6", 6, 10, 23);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
