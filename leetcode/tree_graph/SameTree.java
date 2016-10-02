import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC100: https://leetcode.com/problems/same-tree/
//
// Given two binary trees, write a function to check if they are equal or not.
public class SameTree {
    // Recursion
    // beats 10.47%(0 ms)
    public boolean isSameTree(TreeNode p, TreeNode q) {
        if (p == null || q == null)  return p == q;

        return (p.val == q.val)
               && isSameTree(p.left, q.left) && isSameTree(p.right, q.right);
    }

    // Solution of Choice
    // Queue
    // beats 2.18%(1 ms)
    public boolean isSameTree2(TreeNode p, TreeNode q) {
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(p);
        queue.offer(q);
        while (!queue.isEmpty()) {
            TreeNode pHead = queue.poll();
            TreeNode qHead = queue.poll();
            if (pHead == null && qHead == null) continue;

            if (pHead == null || qHead == null) return false;

            if (pHead.val != qHead.val) return false;

            queue.offer(pHead.left);
            queue.offer(qHead.left);
            queue.offer(pHead.right);
            queue.offer(qHead.right);
        }
        return true;
    }

    // Solution of Choice
    // Stack
    // beats 2.18%(1 ms)
    public boolean isSameTree3(TreeNode p, TreeNode q) {
        Stack<TreeNode> stack = new Stack<> ();
        stack.push(p);
        stack.push(q);
        while (!stack.isEmpty()) {
            TreeNode pTop = stack.pop();
            TreeNode qTop = stack.pop();
            if (pTop == null && qTop == null) continue;

            if (pTop == null || qTop == null) return false;

            if (pTop.val != qTop.val) return false;

            stack.push(pTop.right);
            stack.push(qTop.right);
            stack.push(pTop.left);
            stack.push(qTop.left);
        }
        return true;
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(Function<TreeNode, TreeNode, Boolean> same,
              String s1, String s2, boolean expected) {
        TreeNode root1 = TreeNode.of(s1);
        TreeNode root2 = TreeNode.of(s2);
        assertEquals(expected, same.apply(root1, root2));
    }

    void test(String s1, String s2, boolean expected) {
        SameTree s = new SameTree();
        test(s::isSameTree, s1, s2, expected);
        test(s::isSameTree2, s1, s2, expected);
        test(s::isSameTree3, s1, s2, expected);
    }

    @Test
    public void test1() {
        test("1,#,2,3", "1,#,2,3", true);
        test("1,#,2,3", "1,2,#,3", false);
        test("1", "1,2", false);
        test("1", "1,#,2", false);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("SameTree");
    }
}
