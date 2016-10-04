import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC114: https://leetcode.com/problems/flatten-binary-tree-to-linked-list/
//
// Given a binary tree, flatten it to a linked list in-place.
public class FlattenTree {
    // Recursion
    // beats 29.33%(1 ms)
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
    // beats 21.58%(2 ms)
    public void flatten2(TreeNode root) {
        if (root == null) return;

        Queue<TreeNode> queue = new LinkedList<>();
        for (TreeNode cur = root; cur != null; ) {
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

    // Morris Traversal(space complexity is O(1))
    // beats 29.33%(1 ms)
    public void flatten3(TreeNode root) {
        for (TreeNode cur = root, last = null, prev; cur != null; ) {
            if (cur.left == null) {
                if (last != null) {
                    last.left = cur;
                }
                last = cur;
                cur = cur.right;
                continue;
            }

            for (prev = cur.left; prev.right != null && prev.right != cur;
                 prev = prev.right) {}
            if (prev.right == null) {
                if (last != null) {
                    last.left = cur;
                }
                last = cur;
                prev.right = cur;
                cur = cur.left;
            } else {
                cur = cur.right;
                prev.right = null;
            }
        }
        // put all left nodes to the right
        for (TreeNode n = root; n != null; n = n.right) {
            n.right = n.left;
            n.left = null;
        }
    }

    // Stack
    // http://www.programcreek.com/2013/01/leetcode-flatten-binary-tree-to-linked-list/
    // beats 4.53%(3 ms)
    public void flatten4(TreeNode root) {
        Stack<TreeNode> stack = new Stack<>();
        for (TreeNode n = root; n != null || !stack.empty(); n = n.right) {
            if (n.right != null) {
                stack.push(n.right);
            }
            if (n.left != null) {
                n.right = n.left;
                n.left = null;
            } else if (!stack.empty()) {
                n.right = stack.pop();
            }
        }
    }

    // Solution of Choice
    // space complexity: O(1)
    // beats 29.33%(1 ms)
    public void flatten5(TreeNode root) {
        for (TreeNode cur = root, prev; cur != null; ) {
            if (cur.left == null) {
                cur = cur.right;
            } else {
                for (prev = cur.left; prev.right != null; prev = prev.right) {}
                prev.right = cur.right;
                cur.right = cur.left;
                cur.left = null;
            }
        }
    }

    // Stack
    // beats 4.53%(3 ms)
    public void flatten6(TreeNode root) {
        if (root == null) return;

        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()){
            TreeNode cur = stack.pop();
            if (cur.right != null) {
                stack.push(cur.right);
            }
            if (cur.left !=null) {
                stack.push(cur.left);
            }
            if (!stack.isEmpty()) {
                cur.right = stack.peek();
            }
            cur.left = null;
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
        test(f::flatten3, s, expected);
        test(f::flatten4, s, expected);
        test(f::flatten5, s, expected);
        test(f::flatten6, s, expected);
    }

    @Test
    public void test1() {
        test("1", "{1}");
        test("1,#,2,3", "{1,#,2,#,3}");
        test("1,2,#,3,4,5", "{1,#,2,#,3,#,5,#,4}");
        test("1,2,5,3,4,#,6", "{1,#,2,#,3,#,4,#,5,#,6}");
        test("1,2", "{1,#,2}");
        test("1,2,#,3", "{1,#,2,#,3}");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("FlattenTree");
    }
}
