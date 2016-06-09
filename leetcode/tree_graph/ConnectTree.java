import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeLinkNode;

// Populate each next pointer to point to its next right node. If there is no
// next right node, the next pointer should be set to NULL.
// Note:
// You may only use constant extra space.
// You may assume that it is a perfect binary tree (ie, all leaves are at the
// same level, and every parent has two children).
public class ConnectTree {
    // beats 10.93%
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

    // beats 10.93%
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
    // beats 86.69%
    public void connect3(TreeLinkNode root) {
        for (TreeLinkNode prevHead = root; prevHead != null; ) {
            TreeLinkNode nextHead = prevHead.left;
            if (nextHead == null) return;

            TreeLinkNode last = nextHead.next = prevHead.right;
            for (TreeLinkNode prevCur = prevHead.next; prevCur != null;
                 prevCur = prevCur.next) {
                last.next = prevCur.left;
                last = prevCur.left.next = prevCur.right;
            }
            prevHead = nextHead;
        }
    }

    // recursion
    // beats 32.42%
    public void connect4(TreeLinkNode root) {
        if (root == null || root.left == null) return;

        root.left.next = root.right;
        if (root.next != null) {
            root.right.next = root.next.left;
        }

        connect4(root.left);
        connect4(root.right);
    }

    @FunctionalInterface
    interface Function<A> {
        public void apply(A a);
    }

    void test(Function<TreeLinkNode> connect, String s, String expected) {
        TreeLinkNode root = TreeLinkNode.of(s);
        connect.apply(root);
        assertEquals(expected, root.toString());
    }

    void test(String s, String expected) {
        ConnectTree c = new ConnectTree();
        test(c::connect, s, expected);
    }

    @Test
    public void test1() {
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ConnectTree");
    }
}
