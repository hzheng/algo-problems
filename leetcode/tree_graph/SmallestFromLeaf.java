import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC988: https://leetcode.com/problems/smallest-string-starting-from-leaf/
//
// Given the root of a binary tree, each node has a value from 0 to 25
// representing the letters 'a' to 'z': a value of 0 represents 'a', a value of 
// 1 represents 'b', and so on.
// Find the lexicographically smallest string that starts at a leaf of this tree
// and ends at the root.
public class SmallestFromLeaf {
    // DFS + Recursion + Backtracking
    // 3 ms(100.00%), 26.1 MB(100.00%) for 62 tests
    public String smallestFromLeaf(TreeNode root) {
        String[] res = new String[1];
        res[0] = "{";
        dfs(root, new StringBuilder(), res);
        return res[0];
    }

    void dfs(TreeNode root, StringBuilder sb, String[] res) {
        if (root == null) return;

        sb.append((char)('a' + root.val));
        if (root.left == null && root.right == null) {
            sb.reverse();
            // or: String s = new StringBuilder(sb.toString()).reverse().toString();
            String s = sb.toString();
            if (s.compareTo(res[0]) < 0) {
                res[0] = s;
            }
            sb.reverse();
        } else {
            dfs(root.left, sb, res);
            dfs(root.right, sb, res);
        }
        sb.deleteCharAt(sb.length() - 1);
    }

    // DFS + Recursion
    // 3 ms(100.00%), 26.1 MB(100.00%) for 62 tests
    public String smallestFromLeaf2(TreeNode root) {
        if (root == null) return "~";

        char c = (char)(root.val + 'a');
        if (root.left == null && root.right == null) return "" + c;

        String left = smallestFromLeaf2(root.left);
        String right = smallestFromLeaf2(root.right);
        return (left.compareTo(right) < 0 ? left : right) + c;
    }

    void test(String tree, String expected) {
        assertEquals(expected, smallestFromLeaf(TreeNode.of(tree)));
        assertEquals(expected, smallestFromLeaf2(TreeNode.of(tree)));
    }

    @Test
    public void test() {
        test("0,1,2,3,4,3,4", "dba");
        test("25,1,3,1,3,0,2", "adz");
        test("2,2,1,#,1,0,#,0", "abc");
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
