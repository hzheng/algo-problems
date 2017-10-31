import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC617: https://leetcode.com/problems/merge-two-binary-trees/
//
// Given two binary trees and imagine that when you put one of them to cover the
// other, some nodes of the two trees are overlapped while the others are not.
// You need to merge them into a new binary tree. The merge rule is that if two
// nodes overlap, then sum node values up as the new value of the merged node.
// Otherwise, the NOT null node will be used as the node of new tree.
// Note: The merging process must start from the root nodes of both trees.
public class MergeTrees {
    // Recursion
    // beats 72.47%(14 ms for 183 tests)
    public TreeNode mergeTrees(TreeNode t1, TreeNode t2) {
        if (t1 == null) return t2;
        if (t2 == null) return t1;

        TreeNode res = new TreeNode(t1.val + t2.val);
        res.left = mergeTrees(t1.left, t2.left);
        res.right = mergeTrees(t1.right, t2.right);
        return res;
    }

    // Stack
    // beats 18.86%(17 ms for 183 tests)
    public TreeNode mergeTrees2(TreeNode t1, TreeNode t2) {
        if (t1 == null) return t2;

        ArrayDeque<TreeNode[]> stack = new ArrayDeque<>();
        stack.push(new TreeNode[] {t1, t2});
        while (!stack.isEmpty()) {
            TreeNode[] t = stack.pop();
            if (t[0] == null || t[1] == null) continue;

            t[0].val += t[1].val;
            if (t[0].left == null) {
                t[0].left = t[1].left;
            } else {
                stack.push(new TreeNode[] {t[0].left, t[1].left});
            }
            if (t[0].right == null) {
                t[0].right = t[1].right;
            } else {
                stack.push(new TreeNode[] {t[0].right, t[1].right});
            }
        }
        return t1;
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(String t1, String t2, String expected,
              Function<TreeNode, TreeNode, TreeNode> mergeTrees) {
        assertEquals ("{" + expected + "}",
                      mergeTrees.apply(TreeNode.of(t1),
                                       TreeNode.of(t2)).toString());
    }

    void test(String t1, String t2, String expected) {
        MergeTrees m = new MergeTrees();
        test(t1, t2, expected, m::mergeTrees);
        test(t1, t2, expected, m::mergeTrees2);
    }

    @Test
    public void test() {
        test("1,2,#,3", "1,#,2,#,3", "2,2,2,3,#,#,3");
        test("1,3,2,5", "2,1,3,#,4,#,7", "3,4,5,5,4,#,7");
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
