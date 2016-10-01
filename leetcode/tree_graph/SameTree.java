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

    // Queue
    // beats 2.18%(1 ms)
    public boolean isSameTree2(TreeNode p, TreeNode q) {
        if (p == null || q == null)  return p == q;

        Queue<TreeNode> queue1 = new LinkedList<>();
        Queue<TreeNode> queue2 = new LinkedList<>();
        queue1.add(p);
        queue2.add(q);
        while (!queue1.isEmpty() && !queue2.isEmpty()) {
            TreeNode pHead = queue1.poll();
            TreeNode qHead = queue2.poll();
            if (pHead.val != qHead.val) return false;

            if ((pHead.left == null) ^ (qHead.left == null)) return false;
            if (pHead.left != null) {
                queue1.offer(pHead.left);
                queue2.offer(qHead.left);
            }

            if ((pHead.right == null) ^ (qHead.right == null)) return false;
            if (pHead.right != null) {
                queue1.offer(pHead.right);
                queue2.offer(qHead.right);
            }
        }
        return queue1.isEmpty() && queue2.isEmpty();
    }

    // Stack
    // beats 2.18%(1 ms)
    public boolean isSameTree3(TreeNode p, TreeNode q) {
        if (p == null || q == null)  return p == q;

        Stack<TreeNode> stack1 = new Stack<> ();
        Stack<TreeNode> stack2 = new Stack<> ();
        stack1.push(p);
        stack2.push(q);
        while (!stack1.isEmpty() && !stack2.isEmpty()) {
            TreeNode pTop = stack1.pop();
            TreeNode qTop = stack2.pop();
            if (pTop.val != qTop.val) return false;

            if ((pTop.right == null) ^ (qTop.right == null)) return false;
            if (pTop.right != null) {
                stack1.push(pTop.right);
                stack2.push(qTop.right);
            }

            if ((pTop.left == null) ^ (qTop.left == null)) return false;
            if (pTop.left != null) {
                stack1.push(pTop.left);
                stack2.push(qTop.left);
            }
        }
        return stack1.isEmpty() == stack2.isEmpty();
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
