import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// Two elements of a binary search tree (BST) are swapped by mistake.
// Recover the tree without changing its structure.
public class RecoverBst {
    private TreeNode node1;
    private TreeNode node2;
    private TreeNode lastNode = null;

    // Recursion traversal: time complexity: O(N)
    // space complexity: average - O(Log(N)) worst - O(N)
    // beats 44.17%
    public void recoverTree(TreeNode root) {
        visit(root);
        swap(node1, node2);
    }

    private void visit(TreeNode root) {
        if (root == null) return;

        visit(root.left);
        if (lastNode != null && root.val < lastNode.val) {
            if (node1 == null) {
                node1 = lastNode;
            }
            node2 = root;
        }
        lastNode = root;
        visit(root.right);
    }

    private void swap(TreeNode node1, TreeNode node2) {
        int tmp = node1.val;
        node1.val = node2.val;
        node2.val = tmp;
    }

    // Morris traversal: time complexity: O(N), space complexity: O(1)
    // beats 27.33%
    public void recoverTree2(TreeNode root) {
        TreeNode node1 = null;
        TreeNode node2 = null;
        TreeNode lastNode = null;
        TreeNode cur = root;

        while (cur != null) {
            if (cur.left == null) {
                if (lastNode != null && cur.val < lastNode.val) {
                    if (node1 == null) {
                        node1 = lastNode;
                    }
                    node2 = cur;
                }
                lastNode = cur;
                cur = cur.right;
                continue;
            }

            TreeNode pre = cur.left;
            while (pre.right != null && pre.right != cur) {
                pre = pre.right;
            }
            if (pre.right == null) {
                pre.right = cur;
                cur = cur.left;
            } else {
                pre.right = null;
                if (lastNode != null && cur.val < lastNode.val) {
                    if (node1 == null) {
                        node1 = lastNode;
                    }
                    node2 = cur;
                }
                lastNode = cur;
                cur = cur.right;
            }
        }
        swap(node1, node2);
    }

    @FunctionalInterface
    interface Function<A> {
        public void apply(A a);
    }

    void test(Function<TreeNode> recover, String s, String expected) {
        TreeNode root = TreeNode.of(s);
        recover.apply(root);
        TreeNode expectedNode = TreeNode.of(expected);
        assertArrayEquals(expectedNode.toArray(), root.toArray());
    }

    void test(String s, String expected) {
        RecoverBst r = new RecoverBst();
        test(r::recoverTree, s, expected);
        test(r::recoverTree2, s, expected);
    }

    @Test
    public void test1() {
        test("3,2,1,4", "3,2,4,1");
        test("5,3,#,2,1,4", "5,3,#,2,4,1");
        test("3,#,2,#,1", "1,#,2,#,3");
        test("0,1", "1,0");
        test("0,#,3,#,2,#,1", "0,#,1,#,2,#,3");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("RecoverBst");
    }
}
