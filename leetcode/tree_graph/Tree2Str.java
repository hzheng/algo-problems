import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC606: https://leetcode.com/problems/construct-string-from-binary-tree/
//
// You need to construct a string consists of parenthesis and integers from a
// binary tree with the preorder traversing way.
// The null node needs to be represented by empty parenthesis pair "()". And you
// need to omit all the empty parenthesis pairs that don't affect the one-to-one
// mapping relationship between the string and the original binary tree.
public class Tree2Str {
    // Recursion
    // time complexity: O(N), space complexity: O(N)
    // beats 42.44%(34 ms for 162 tests)
    public String tree2str(TreeNode t) {
        if (t == null) return "";

        StringBuilder sb = new StringBuilder();
        sb.append(t.val);
        if (t.left != null || t.right != null) {
            sb.append("(");
            sb.append(tree2str(t.left));
            sb.append(")");
            if (t.right != null) {
                sb.append("(");
                sb.append(tree2str(t.right));
                sb.append(")");
            }
        }
        return sb.toString();
    }

    // Recursion
    // time complexity: O(N), space complexity: O(N)
    // beats 96.34%(14 ms for 162 tests)
    public String tree2str2(TreeNode t) {
        StringBuilder sb = new StringBuilder();
        preorder(t, sb);
        return sb.toString();
    }

    private void preorder(TreeNode root, StringBuilder sb) {
        if (root == null) return;

        sb.append(root.val);
        if (root.left == null && root.right == null) return;

        sb.append("(");
        preorder(root.left, sb);
        sb.append(")");
        if (root.right != null) {
            sb.append("(");
            preorder(root.right, sb);
            sb.append(")");
        }
    }

    // Recursion
    // time complexity: O(N), space complexity: O(N)
    // beats 69.75%(29 ms for 162 tests)
    public String tree2str3(TreeNode t) {
        if (t == null) return "";

        if (t.left == null && t.right == null) return t.val + "";

        if (t.right == null) return t.val + "(" + tree2str3(t.left) + ")";

        return t.val + "(" + tree2str(t.left) + ")(" + tree2str3(t.right) + ")";
    }

    // Stack + Set
    // time complexity: O(N), space complexity: O(N)
    // beats 80.14%(22 ms for 162 tests)
    public String tree2str4(TreeNode t) {
        if (t == null) return "";

        StringBuilder sb = new StringBuilder();
        Set<TreeNode> visited = new HashSet<>();
        ArrayDeque<TreeNode> stack = new ArrayDeque<>();
        stack.push(t);
        while (!stack.isEmpty()) {
            TreeNode node = stack.peek();
            if (!visited.add(node)) {
                stack.pop();
                sb.append(")");
            } else {
                sb.append("(").append(node.val);
                if (node.right != null) {
                    if (node.left == null) {
                        sb.append("()");
                    }
                    stack.push(node.right);
                }
                if (node.left != null) {
                    stack.push(node.left);
                }
            }
        }
        return sb.substring(1, sb.length() - 1);
    }

    void test(String tree, String expected) {
        assertEquals(expected, tree2str(TreeNode.of(tree)));
        assertEquals(expected, tree2str2(TreeNode.of(tree)));
        assertEquals(expected, tree2str3(TreeNode.of(tree)));
        assertEquals(expected, tree2str4(TreeNode.of(tree)));
    }

    @Test
    public void test() {
        test("1,2,3,4", "1(2(4))(3)");
        test("1,2,3,#,4", "1(2()(4))(3)");
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
