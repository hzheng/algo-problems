import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC230: https://leetcode.com/problems/kth-smallest-element-in-a-bst/
//
// Given a binary search tree, write a function kthSmallest to find the kth
// smallest element in it.

// Follow up:
// What if the BST is modified (insert/delete operations) often and you need to
// find the kth smallest frequently?
public class KthSmallestBst {
    // Recursion
    // time complexity: O(N * log(N)) for balanced tree, space complexity: O(N)
    // beats 54.10%(1 ms)
    public int kthSmallest(TreeNode root, int k) {
        int count = count(root.left);
        if (count + 1 == k) return root.val;

        if (count + 1 > k) return kthSmallest(root.left, k);

        return kthSmallest(root.right, k - count - 1);
    }

    private int count(TreeNode node) {
        if (node == null) return 0;

        return 1 + count(node.left) + count(node.right);
    }

    // time complexity: O(N), space complexity: O(N)
    // beats 7.61%(6 ms)
    public int kthSmallest2(TreeNode root, int k) {
        return kthSmallest2(root, k, new HashMap<>());
    }

    // Recursion + Hash Table
    private int kthSmallest2(TreeNode root, int k, Map<TreeNode, Integer> cache) {
        int count = count(root.left, cache);
        if (count + 1 == k) return root.val;

        if (count + 1 > k) return kthSmallest2(root.left, k, cache);

        return kthSmallest2(root.right, k - count - 1, cache);
    }

    private int count(TreeNode node, Map<TreeNode, Integer> cache) {
        if (node == null) return 0;

        if (cache.containsKey(node)) return cache.get(node);

        int res = 1 + count(node.left, cache) + count(node.right, cache);
        cache.put(node, res);
        return res;
    }

    // Recursion + Inorder Traversal
    // time complexity: O(N), space complexity: O(N)
    // beats 54.10%(1 ms)
    public int kthSmallest3(TreeNode root, int k) {
        return traverse(root, k, new int[1]).val;
    }

    private TreeNode traverse(TreeNode root, int k, int[] count) {
        if (root == null) return null;

        TreeNode res = traverse(root.left, k, count);
        if (res != null) return res;

        if (++count[0] == k) return root;

        return traverse(root.right, k, count);
    }

    // Solution of Choice
    // Stack + Inorder Traversal
    // time complexity: O(N), space complexity: O(N)
    // beats 25.43%(2 ms)
    public int kthSmallest4(TreeNode root, int k) {
        Stack<TreeNode> stack = new Stack<>();
        int count = k;
        TreeNode n = root;
        while (true) {
            if (n != null) {
                stack.push(n);
                n = n.left;
            } else {
                n = stack.pop();
                if (--count == 0) return n.val;

                n = n.right;
            }
        }
    }

    // Morris algorithm + Inorder Traversal
    // time complexity: O(N), space complexity: O(1)
    // beats 51.16%(1 ms for 91 tests)
    public int kthSmallest5(TreeNode root, int k) {
        int count = k;
        for (TreeNode cur = root, prev; ; ) {
            if (cur.left == null) {
                if (--count == 0) return cur.val;

                cur = cur.right;
                continue;
            }
            for (prev = cur.left; prev.right != null && prev.right != cur; prev = prev.right) {}
            if (prev.right == null) {
                prev.right = cur;
                cur = cur.left;
            } else {
                prev.right = null;
                if (--count == 0) return cur.val; //prev.right.val;

                cur = cur.right;
            }
        }
    }

    // if we could modify BST structure, we can improve time complexity to O(H)
    // (height of tree). ref: https://en.wikipedia.org/wiki/Order_statistic_tree

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(Function<TreeNode, Integer, Integer> kthSmallest, String s,
              int k, int expected) {
        TreeNode root = TreeNode.of(s);
        assertEquals(expected, (int)kthSmallest.apply(root, k));
    }

    void test(String s, int k, int expected) {
        KthSmallestBst smallest = new KthSmallestBst();
        test(smallest::kthSmallest, s, k, expected);
        test(smallest::kthSmallest2, s, k, expected);
        test(smallest::kthSmallest3, s, k, expected);
        test(smallest::kthSmallest4, s, k, expected);
        test(smallest::kthSmallest5, s, k, expected);
    }

    @Test
    public void test1() {
        test("1", 1, 1);
        test("1,#,6,5", 2, 5);
        test("1,#,6,5", 2, 5);
        test("6,4,8,3,5,7,9,1", 2, 3);
        test("6,4,8,3,5,7,9,2,#,#,#,#,#,#,#,1", 1, 1);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("KthSmallestBst");
    }
}
