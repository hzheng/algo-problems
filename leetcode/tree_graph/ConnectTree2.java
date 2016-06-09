import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeLinkNode;

// Same problem as ConnectTree except binary tree could be inperfect.
public class ConnectTree2 {
    // beats 32.04%
    public void connect(TreeLinkNode root) {
        for (TreeLinkNode prevCur = root; prevCur != null; ) {
            TreeLinkNode nextHead = null;
            TreeLinkNode last = null;
            for (; prevCur != null; prevCur = prevCur.next) {
                if (prevCur.left != null) {
                    if (nextHead == null) {
                        nextHead = prevCur.left;
                    }
                    if (last != null) {
                        last.next = prevCur.left;
                    }
                    last = prevCur.left;
                }
                if (prevCur.right != null) {
                    if (nextHead == null) {
                        nextHead = prevCur.right;
                    }
                    if (last != null) {
                        last.next = prevCur.right;
                    }
                    last = prevCur.right;
                }
            }
            prevCur = nextHead;
        }
    }

    // recursion
    // beats 75.61%
    public void connect2(TreeLinkNode root) {
        if (root == null || (root.left == null && root.right == null)) return;

        TreeLinkNode node = root.left;
        if (node == null) {
            node = root.right;
        } else if (root.right != null) {
            node.next = root.right;
            node = root.right;
        }
        for (TreeLinkNode cur = root.next; cur != null; cur = cur.next) {
            if (cur.left != null) {
                node.next = cur.left;
                break;
            } else if (cur.right != null) {
                node.next = cur.right;
                break;
            }
        }
        connect2(root.right); // must connect right first!
        connect2(root.left);
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
        ConnectTree2 c = new ConnectTree2();
        test(c::connect, s, expected);
    }

    @Test
    public void test1() {
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ConnectTree2");
    }
}
