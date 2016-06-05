import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// Given two binary trees, write a function to check if they are equal or not.
public class SameTree {
    // beats 10.47%
    public boolean isSameTree(TreeNode p, TreeNode q) {
        if (p == null)  return q == null;
        if (q == null)  return p == null;

        return (p.val == q.val)
               && isSameTree(p.left, q.left) && isSameTree(p.right, q.right);
    }

    // beats 1.18%
    public boolean isSameTree2(TreeNode p, TreeNode q) {
        if (p == null)  return q == null;
        if (q == null)  return p == null;

        Queue<TreeNode> pq = new LinkedList<>();
        Queue<TreeNode> qq = new LinkedList<>();
        pq.add(p);
        qq.add(q);
        while (!pq.isEmpty() && !qq.isEmpty()) {
            TreeNode pn = pq.poll();
            TreeNode qn = qq.poll();
            if (pn.val != qn.val) return false;

            if (pn.left == null) {
                if (qn.left != null) return false;
            } else {
                if (qn.left == null) return false;

                pq.offer(pn.left);
                qq.offer(qn.left);
            }
            if (pn.right == null) {
                if (qn.right != null) return false;
            } else {
                if (qn.right == null) return false;

                pq.offer(pn.right);
                qq.offer(qn.right);
            }
        }
        return pq.isEmpty() && qq.isEmpty();
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
