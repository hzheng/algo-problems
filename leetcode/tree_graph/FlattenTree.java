import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// Given a binary tree, flatten it to a linked list in-place.
public class FlattenTree {
    // beats 29.33%
    public void flatten(TreeNode root) {
        if (root != null) {
            flattenTree(root);
        }
    }

    private TreeNode flattenTree(TreeNode root) {
        if (root.left == null) {
            return root.right == null ? root : flattenTree(root.right);
        }

        TreeNode last = flattenTree(root.left);
        last.right = root.right;
        root.right = root.left;
        root.left = null;
        return last.right == null ? last : flattenTree(last.right);
    }

    // Morris Traversal(just for exercise, since space complexity is still O(N))
    // beats 21.58%
    public void flatten2(TreeNode root) {
        if (root == null) return;

        TreeNode cur = root;
        Queue<TreeNode> queue = new LinkedList<>();
        while (cur != null) {
            if (cur.left == null) {
                queue.add(cur);
                cur = cur.right;
                continue;
            }

            TreeNode prev = cur.left;
            while (prev.right != null && prev.right != cur) {
                prev = prev.right;
            }
            if (prev.right == null) {
                queue.add(cur);
                prev.right = cur;
                cur = cur.left;
            } else {
                prev.right = null;
                cur = cur.right;
            }
        }
        for (TreeNode n = queue.poll(); !queue.isEmpty(); n = n.right) {
            n.left = null;
            n.right = queue.poll();
        }
    }

    @FunctionalInterface
    interface Function<A> {
        public void apply(A a);
    }

    void test(Function<TreeNode> flatten, String s, String expected) {
        TreeNode root = TreeNode.of(s);
        flatten.apply(root);
        assertEquals(expected, root.toString());
    }

    void test(String s, String expected) {
        FlattenTree f = new FlattenTree();
        test(f::flatten, s, expected);
        test(f::flatten2, s, expected);
    }

    @Test
    public void test1() {
        test("1,2,5,3,4,#,6", "{1,#,2,#,3,#,4,#,5,#,6}");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("FlattenTree");
    }
}
