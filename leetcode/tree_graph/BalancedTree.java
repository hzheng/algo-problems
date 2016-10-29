import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC110: https://leetcode.com/problems/balanced-binary-tree/
//
// Given a binary tree, determine if it is height-balanced.
public class BalancedTree {
    // Solution of Choice
    // Recursion
    // beats 70.26%(1 ms)
    public boolean isBalanced(TreeNode root) {
        return depth(root) >= 0;
    }

    private int depth(TreeNode root) {
        if (root == null) return 0;

        int leftDepth = depth(root.left);
        if (leftDepth < 0) return -1;

        int rightDepth = depth(root.right);
        if (rightDepth < 0) return -1;

        if (Math.abs(leftDepth - rightDepth) > 1) return -1;

        return Math.max(leftDepth, rightDepth) + 1;
    }

    // beats 3.28%(10 ms)
    // Stack + Hashtable(Postorder traversal)
    public boolean isBalanced2(TreeNode root) {
        if (root == null) return true;

        Map<TreeNode, Integer> map = new HashMap<>();
        map.put(null, 0);
        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            TreeNode node = stack.peek();
            boolean visitedLeft = map.containsKey(node.left);
            if (visitedLeft && map.containsKey(node.right)) {
                int leftDepth = map.get(stack.pop().left);
                int rightDepth = map.get(node.right);
                if (Math.abs(leftDepth - rightDepth) > 1) return false;

                map.put(node, Math.max(leftDepth, rightDepth) + 1);
            } else {
                stack.push(visitedLeft ? node.right : node.left);
            }
        }
        return true;
    }

    void test(Function<TreeNode, Boolean> balanced, String s, boolean expected) {
        TreeNode root = TreeNode.of(s);
        assertEquals(expected, balanced.apply(root));
    }

    void test(String s, boolean expected) {
        BalancedTree b = new BalancedTree();
        test(b::isBalanced, s, expected);
        test(b::isBalanced2, s, expected);
    }

    @Test
    public void test1() {
        test("1,#,2,3", false);
        test("3,9,20,#,#,15,7", true);
        test("1,2,3,4,5,6,7", true);
        test("1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17", true);
        test("1,2,3,4,5,#,#,6,7", false);
        test("1,2,3,4,5,6,7,8,9,10,11,12,13,#,#,14,15", true);
        test("1,2,2,3,3,3,3,4,4,4,4,4,4,#,#,5,5", true);
        test("1,2,3,4,#,5,6,7,#,8,#,9,#,10,#,11,#,#,#,12", false);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("BalancedTree");
    }
}
