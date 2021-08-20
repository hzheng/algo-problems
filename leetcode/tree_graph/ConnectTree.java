import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeLinkNode;

// LC116: https://leetcode.com/problems/populating-next-right-pointers-in-each-node/
//
// Populate each next pointer to point to its next right node. If there is no
// next right node, the next pointer should be set to NULL.
// Note:
// You may only use constant extra space.
// You may assume that it is a perfect binary tree (ie, all leaves are at the
// same level, and every parent has two children).
public class ConnectTree {
    // BFS + Queue
    // beats 10.93%(5 ms)
    // space complexity: O(Log(N))
    public void connect(TreeLinkNode root) {
        if (root == null) return;

        Queue<TreeLinkNode> queue = new LinkedList<>();
        queue.offer(root);
        TreeLinkNode last = null;
        int curCount = 1;
        int nextCount = 0;
        while (!queue.isEmpty()) {
            TreeLinkNode node = queue.poll();
            if (--curCount < 0) {
                last = null;
                curCount = nextCount - 1;
                nextCount = 0;
            }
            node.next = last;
            last = node;
            if (node.right != null) {
                queue.offer(node.right);
                nextCount++;
            }
            if (node.left != null) {
                queue.offer(node.left);
                nextCount++;
            }
        }
    }

    // BFS + Queue
    // beats 10.93%(5 ms)
    // space complexity: O(Log(N))
    public void connect2(TreeLinkNode root) {
        if (root == null) return;

        Queue<TreeLinkNode> queue = new LinkedList<>();
        queue.offer(root);
        queue.offer(null);
        TreeLinkNode last = null;
        while (!queue.isEmpty()) {
            TreeLinkNode node = queue.poll();
            if (node == null) {
                if (queue.isEmpty()) return;

                last = null;
                queue.offer(null);
                continue;
            }

            node.next = last;
            last = node;
            if (node.right != null) {
                queue.offer(node.right);
            }
            if (node.left != null) {
                queue.offer(node.left);
            }
        }
    }

    // space complexity: O(1)
    // beats 86.69%(0 ms)
    public void connect3(TreeLinkNode root) {
        for (TreeLinkNode prevHead = root, nextHead; prevHead != null; prevHead = nextHead) {
            nextHead = prevHead.left;
            if (nextHead == null) return;

            TreeLinkNode last = nextHead.next = prevHead.right;
            for (TreeLinkNode prevCur = prevHead.next; prevCur != null;
                 prevCur = prevCur.next) {
                last.next = prevCur.left;
                last = prevCur.left.next = prevCur.right;
            }
        }
    }

    // Recursion
    // beats 31.38%(1 ms)
    public void connect4(TreeLinkNode root) {
        if (root == null || root.left == null) return;

        root.left.next = root.right;
        if (root.next != null) {
            root.right.next = root.next.left;
        }
        connect4(root.left);
        connect4(root.right);
    }

    // Solution of Choice
    // Recursion
    // 0 ms(100.00%), 39.5 MB(18.54%) for 58 tests
    public void connect5(TreeLinkNode root) {
        dfs(root, null);
    }

    private void dfs(TreeLinkNode cur, TreeLinkNode next) {
        if (cur == null) { return; }

        cur.next = next;
        dfs(cur.left, cur.right);
        dfs(cur.right, cur.next == null ? null : cur.next.left);
    }

    // Solution of Choice
    // space complexity: O(1)
    // beats 31.38%(1 ms)
    public void connect6(TreeLinkNode root) {
        if (root == null) return;

        for (TreeLinkNode prev = root; prev.left != null; prev = prev.left) {
            for (TreeLinkNode cur = prev; cur != null; cur = cur.next) {
                cur.left.next = cur.right;
                if (cur.next != null) {
                    cur.right.next = cur.next.left;
                }
            }
        }
    }

    @FunctionalInterface
    interface Function<A> {
        void apply(A a);
    }

    void test(Function<TreeLinkNode> connect, String s, String expected) {
        TreeLinkNode root = TreeLinkNode.of(s);
        connect.apply(root);
        // assertEquals(expected, root.toString());
        assertEquals(TreeLinkNode.of(expected), root);
    }

    void test(String s, String expected) {
        ConnectTree c = new ConnectTree();
        test(c::connect, s, expected);
        test(c::connect5, s, expected);
    }

    @Test
    public void test1() {
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
