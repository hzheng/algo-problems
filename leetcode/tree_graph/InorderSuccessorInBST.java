import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC285: https://leetcode.com/problems/inorder-successor-in-bst/
//
// Given a binary search tree and a node in it, find the in-order successor.
public class InorderSuccessorInBST {
    // Stack
    // time complexity: O(N), space complexity: O(N)
    // beats 7.09%(8 ms for 29 tests)
    public TreeNode inorderSuccessor(TreeNode root, TreeNode p) {
        ArrayDeque<TreeNode> stack = new ArrayDeque<>();
        boolean found = false;
        for (TreeNode cur = root; cur != null || !stack.isEmpty(); ) {
            if (cur != null) {
                stack.push(cur);
                cur = cur.left;
            } else {
                if (found) return stack.peek();

                cur = stack.pop();
                found = (cur == p);
                cur = cur.right;
            }
        }
        return null;
    }

    // time complexity: O(log(N)), space complexity: O(1)
    // beats 51.97%(4 ms for 29 tests)
    public TreeNode inorderSuccessor2(TreeNode root, TreeNode p) {
        boolean found = false;
        for (TreeNode cur = root, prev = null; cur != null || prev != null; ) {
            if (cur != null && cur.val > p.val) {
                prev = cur;
                cur = cur.left;
            } else if (cur != null) {
                found = (cur == p);
                cur = cur.right;
            } else {
                if (found) return prev;

                cur = prev.right;
            }
        }
        return null;
    }

    // time complexity: O(log(N)), space complexity: O(1)
    // beats 51.97%(4 ms for 29 tests)
    public TreeNode inorderSuccessor3(TreeNode root, TreeNode p) {
        TreeNode res = null;
        for (TreeNode cur = root; cur != null; ) {
            if (cur.val > p.val) {
                res = cur;
                cur = cur.left;
            } else {
                cur = cur.right;
            }
        }
        return res;
    }

    // DFS + Recursion
    // time complexity: O(N), space complexity: O(N)
    // beats 23.10%(5 ms for 29 tests)
    public TreeNode inorderSuccessor4(TreeNode root, TreeNode p) {
        return dfs(root, p, new TreeNode[1]);
    }

    private TreeNode dfs(TreeNode root, TreeNode p, TreeNode[] prev) {
        if (root == null) return null;

        TreeNode res = dfs(root.left, p, prev);
        if (res != null) return res;

        if (prev[0] == p) return root;

        prev[0] = root;
        return dfs(root.right, p, prev);
    }

    // DFS(prune) + Recursion
    // time complexity: O(log(N)), space complexity: O(N)
    // beats 51.97%(4 ms for 29 tests)
    public TreeNode inorderSuccessor5(TreeNode root, TreeNode p) {
        return dfs2(root, p, new TreeNode[1]);
    }

    private TreeNode dfs2(TreeNode root, TreeNode p, TreeNode[] prev) {
        if (root == null) return null;

        if (root.val <= p.val) {
            prev[0] = root;
            return dfs2(root.right, p, prev);
        }

        TreeNode res = dfs2(root.left, p, prev);
        if (res != null) return res;

        return (prev[0] == p) ? root : null;
    }

    // DFS(prune) + Recursion
    // time complexity: O(log(N)), space complexity: O(N)
    // beats 51.97%(4 ms for 29 tests)
    public TreeNode inorderSuccessor5_2(TreeNode root, TreeNode p) {
        if (root == null) return null;

        if (root.val <= p.val) return inorderSuccessor5_2(root.right, p);

        TreeNode res = inorderSuccessor5_2(root.left, p);
        return (res != null) ? res : root; // assuming p must be root
    }

    // DFS(prune) + Recursion
    // time complexity: O(log(N)), space complexity: O(N)
    // beats 51.97%(4 ms for 29 tests)
    public TreeNode inorderSuccessor5_3(TreeNode root, TreeNode p) {
        while (root != null && root.val <= p.val) {
            root = root.right;
        }
        if (root == null) return null;

        TreeNode res = inorderSuccessor5_3(root.left, p);
        return (res != null) ? res : root;
    }

    private TreeNode find(TreeNode root, int val) {
        if (root == null) return null;

        if (root.val == val) return root;

        TreeNode res = find(root.left, val);
        if (res != null) return res;

        return find(root.right, val);
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(Function<TreeNode, TreeNode, TreeNode> inorderSuccessor,
              String s, int val, Integer expected) {
        TreeNode root = TreeNode.of(s);
        TreeNode p = find(root, val);
        if (expected == null) {
            assertNull(inorderSuccessor.apply(root, p));
        } else {
            assertEquals((int)expected, inorderSuccessor.apply(root, p).val);
        }
    }

    void test(String s, int val, Integer expected) {
        InorderSuccessorInBST i = new InorderSuccessorInBST();
        test(i::inorderSuccessor, s, val, expected);
        test(i::inorderSuccessor2, s, val, expected);
        test(i::inorderSuccessor3, s, val, expected);
        test(i::inorderSuccessor4, s, val, expected);
        test(i::inorderSuccessor5, s, val, expected);
        test(i::inorderSuccessor5_2, s, val, expected);
    }

    @Test
    public void test() {
        test("2,1,4,#,#,3,5", 3, 4);
        test("1,#,8,6", 6, 8);
        test("2,1,4,#,#,3,5", 5, null);
        test("1,#,8,6", 8, null);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("InorderSuccessorInBST");
    }
}
