import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// https://leetcode.com/problems/binary-tree-postorder-traversal/
//
// Given a binary tree, return the postorder traversal of its nodes' values.
public class TreePostorderTraversal {
    // beats 71.07%
    public List<Integer> postorderTraversal(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        postorderTraversal(root, res);
        return res;
    }

    private void postorderTraversal(TreeNode root, List<Integer> res) {
        if (root == null) return;

        postorderTraversal(root.left, res);
        postorderTraversal(root.right, res);
        res.add(root.val);
    }

    // beats 7.51%
    public List<Integer> postorderTraversal2(TreeNode root) {
        Stack<TreeNode> stack = new Stack<>();
        List<Integer> res = new ArrayList<>();
        for (TreeNode n = root; n != null || !stack.isEmpty(); ) {
            if (n != null) {
                stack.push(n);
                n = n.left;
            } else {
                n = stack.peek();
                if (n.right != null) {
                    n = n.right;
                    continue;
                }
                res.add(n.val);
                stack.pop();
                while (!stack.isEmpty()) {
                    if (stack.peek().right != n) {
                        n = stack.peek().right;
                        break;
                    }
                    n = stack.pop();
                    res.add(n.val);
                }
                n = null;
            }
        }
        return res;
    }

    // TODO: Morris traversal

    void test(Function<TreeNode, List<Integer> > traversal,
              String s, Integer ... expected) {
        TreeNode root = TreeNode.of(s);
        assertArrayEquals(expected,
                          traversal.apply(root).toArray(new Integer[0]));
    }

    void test(String s, Integer ... expected) {
        TreePostorderTraversal t = new TreePostorderTraversal();
        test(t::postorderTraversal, s, expected);
        test(t::postorderTraversal2, s, expected);
    }

    @Test
    public void test1() {
        test("1,2,3,4,5,6,7", 4, 5, 2, 6, 7, 3, 1);
        test("1,#,2,3", 3, 2, 1);
        test("1", 1);
        test("1,2,3,#,#,4,#,#,5", 2, 5, 4, 3, 1);
        test("1,2,3,4,5,6,7,8,9,10,11,12,13,14,15",
             8, 9, 4, 10, 11, 5, 2, 12, 13, 6, 14, 15, 7, 3, 1);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("TreePostorderTraversal");
    }
}
