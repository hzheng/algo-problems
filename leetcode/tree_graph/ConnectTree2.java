import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeLinkNode;

// LC117: https://leetcode.com/problems/populating-next-right-pointers-in-each-node-ii/
//
// Same problem as ConnectTree except binary tree could be inperfect.
public class ConnectTree2 {
    // two-layer loop
    // beats 51.63%(1 ms)
    public void connect(TreeLinkNode root) {
        for (TreeLinkNode cur = root, next = null; cur != null;
             cur = next, next = null) {
            for (TreeLinkNode prev = null; cur != null; cur = cur.next) {
                if (cur.left != null) {
                    if (next == null) {
                        next = cur.left;
                    }
                    if (prev != null) {
                        prev.next = cur.left;
                    }
                    prev = cur.left;
                }
                if (cur.right != null) {
                    if (next == null) {
                        next = cur.right;
                    }
                    if (prev != null) {
                        prev.next = cur.right;
                    }
                    prev = cur.right;
                }
            }
        }
    }

    // Recursion
    // beats 51.63%(1 ms)
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

    // one-layer loop
    // beats 32.04%(2 ms)
    public void connect3(TreeLinkNode root) {
        for (TreeLinkNode cur = root, first = null, last = null; cur != null; ) {
            if (first == null) {
                first = (cur.left != null) ? cur.left : cur.right;
            }
            if (cur.left != null) {
                if (last != null) {
                    last.next = cur.left;
                }
                last = cur.left;
            }
            if (cur.right != null) {
                if (last != null) {
                    last.next = cur.right;
                }
                last = cur.right;
            }
            if (cur.next != null) {
                cur = cur.next;
            } else {
                cur = first;
                last = null;
                first = null;
            }
        }
    }

    // Solution of Choice
    // one-layer loop
    // beats 51.63%(1 ms)
    public void connect4(TreeLinkNode root) {
        TreeLinkNode dummy = new TreeLinkNode(0);
        for (TreeLinkNode cur = root, prev = dummy; cur != null; ) {
            if (cur.left != null) {
                prev.next = cur.left;
                prev = prev.next;
            }
            if (cur.right != null) {
                prev.next = cur.right;
                prev = prev.next;
            }
            cur = cur.next;
            if (cur == null) {
                prev = dummy;
                cur = dummy.next;
                dummy.next = null;
            }
        }
    }

    @FunctionalInterface
    interface Function<A> {
        public void apply(A a);
    }

    void test(Function<TreeLinkNode> connect, String s, String expected) {
        TreeLinkNode root = TreeLinkNode.of(s);
        connect.apply(root);
        // assertEquals(expected, root.toString());
        assertEquals(TreeLinkNode.of(expected), root);
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
