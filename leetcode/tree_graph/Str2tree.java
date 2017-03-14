import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC536: https://leetcode.com/problems/construct-binary-tree-from-string
//
// You need to construct a binary tree from a string consisting of parenthesis and integers.
// The whole input represents a binary tree. It contains an integer followed by zero,
// one or two pairs of parenthesis. The integer represents the root's value and a
// pair of parenthesis contains a child binary tree with the same structure.
// You always start to construct the left child node of the parent first if it exists.
public class Str2tree {
    // Recursion
    // beats N/A(24 ms for 86 tests)
    public TreeNode str2tree(String s) {
        int len = s.length();
        if (len == 0) return null;

        int leftParen = s.indexOf('(');
        if (leftParen < 0) return new TreeNode(Integer.parseInt(s));

        TreeNode root = new TreeNode(Integer.parseInt(s.substring(0, leftParen)));
        int rightParen = rightParenPos(s, leftParen + 2);
        root.left = str2tree(s.substring(leftParen + 1, rightParen));
        if (rightParen < len - 1) {
            root.right = str2tree(s.substring(rightParen + 2, len - 1));
        }
        return root;
    }

    private int rightParenPos(String s, int start) {
        int len = s.length();
        int i = start;
        for (int leftParens = 0; i < len; i++) {
            char c = s.charAt(i);
            if (c == '(') {
                leftParens++;
            } else if (c == ')' && leftParens-- == 0) break;
        }
        return i;
    }

    // Recursion
    // beats N/A(45 ms for 86 tests)
    public TreeNode str2tree2(String s) {
        int len = s.length();
        if (len == 0) return null;

        int leftParen = s.indexOf('(');
        if (leftParen < 0) return new TreeNode(Integer.parseInt(s));

        TreeNode root = new TreeNode(Integer.parseInt(s.substring(0, leftParen)));
        for (int i = leftParen + 1, rightParen = 0, parens = 1; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(') {
                parens++;
            } else if (c == ')' && --parens == 0) {
                if (root.left == null) {
                    root.left = str2tree2(s.substring(leftParen + 1, i));
                    rightParen = i + 1;
                } else {
                    root.right = str2tree2(s.substring(rightParen + 1, i));
                }
            }
        }
        return root;
    }

    void test(String s, String expected) {
        assertEquals(expected, str2tree(s).toString());
        assertEquals(expected, str2tree2(s).toString());
    }

    @Test
    public void test() {
        // test("", "");
        test("4(2(3)(1))(6(5))", "{4,2,6,3,1,5}");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("Str2tree");
    }
}
