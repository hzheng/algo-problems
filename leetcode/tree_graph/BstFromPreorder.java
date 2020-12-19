import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.function.Function;

import common.TreeNode;

// LC1008: https://leetcode.com/problems/construct-binary-search-tree-from-preorder-traversal/
//
// Return the root node of a binary search tree that matches the given preorder traversal.
// It's guaranteed that for the given test cases there is always possible to find a binary search
// tree with the given requirements.
//
// Constraints:
// 1 <= preorder.length <= 100
// 1 <= preorder[i] <= 10^8
// The values of preorder are distinct.
public class BstFromPreorder {
    // SortedMap
    // time complexity: O(N*log(N)), space complexity: O(N)
    // 4 ms(24.80%), 38.6 MB(8.35%) for 110 tests
    public TreeNode bstFromPreorder(int[] preorder) {
        TreeMap<Integer, TreeNode> map = new TreeMap<>();
        TreeNode root = create(preorder[0], map);
        int n = preorder.length;
        for (int i = 1; i < n; i++) {
            int val = preorder[i];
            Map.Entry<Integer, TreeNode> higherEntry = map.higherEntry(val);
            if (higherEntry == null) {
                TreeNode lowNode = map.lastEntry().getValue();
                lowNode.right = create(val, map);
            } else {
                TreeNode cur = higherEntry.getValue();
                TreeNode node = create(val, map);
                if (cur.left == null) {
                    cur.left = node;
                } else {
                    TreeNode prev = cur;
                    for (cur = cur.left; cur != null; prev = cur, cur = cur.right) {}
                    prev.right = node;
                }
            }
        }
        return root;
    }

    private TreeNode create(int val, TreeMap<Integer, TreeNode> map) {
        TreeNode node = new TreeNode(val);
        map.put(val, node);
        return node;
    }

    // Stack
    // time complexity: O(N), space complexity: O(N)
    // 1 ms(24.80%), 36.9 MB(74.70%) for 110 tests
    public TreeNode bstFromPreorder2(int[] preorder) {
        Stack<TreeNode> stack = new Stack<>();
        TreeNode root = new TreeNode(preorder[0]);
        stack.push(root);
        for (int i = 1, n = preorder.length; i < n; i++) {
            int val = preorder[i];
            TreeNode node = new TreeNode(val);
            TreeNode top = stack.peek();
            if (val < top.val) {
                top.left = node;
            } else {
                TreeNode prev = stack.peek();
                for (; !stack.isEmpty() && val > stack.peek().val; prev = stack.pop()) {}
                prev.right = node;
            }
            stack.push(node);
        }
        return root;
    }

    // Recursion
    // time complexity: O(N), space complexity: O(N)
    // 0 ms(100.00%), 37 MB(59.00%) for 110 tests
    public TreeNode bstFromPreorder3(int[] preorder) {
        return build(preorder, Integer.MAX_VALUE, new int[1]);
    }

    private TreeNode build(int[] preorder, int bound, int[] cur) {
        int i = cur[0];
        if (i == preorder.length || preorder[i] > bound) { return null; }

        TreeNode node = new TreeNode(preorder[i]);
        cur[0]++;
        node.left = build(preorder, node.val, cur);
        node.right = build(preorder, bound, cur);
        return node;
    }

    // Recursion
    // time complexity: O(N^2), space complexity: O(N)
    // 0 ms(100.00%), 37 MB(59.00%) for 110 tests
    public TreeNode bstFromPreorder4(int[] preorder) {
        return build(preorder, 0, preorder.length - 1);
    }

    private TreeNode build(int[] preorder, int start, int end) {
        if (start > end) { return null; }

        TreeNode node = new TreeNode(preorder[start]);
        int i;
        for (i = start; i <= end; i++) {
            if (preorder[i] > node.val) { break; }
        }
        node.left = build(preorder, start + 1, i - 1);
        node.right = build(preorder, i, end);
        return node;
    }

    // Recursion
    // time complexity: O(N*log(N)), space complexity: O(N)
    // 0 ms(100.00%), 37 MB(59.00%) for 110 tests
    public TreeNode bstFromPreorder5(int[] preorder) {
        TreeNode root = null;
        for (int val : preorder) {
            root = insert(root, val);
        }
        return root;
    }

    private TreeNode insert(TreeNode root, int val) {
        if (root == null) { return new TreeNode(val); }

        if (val < root.val) {
            root.left = insert(root.left, val);
        } else {
            root.right = insert(root.right, val);
        }
        return root;
    }

    void test(Function<int[], TreeNode> bstFromPreorder, int[] preorder, String expected) {
        assertEquals(TreeNode.of(expected), bstFromPreorder.apply(preorder));
    }

    private void test(int[] preorder, String expected) {
        BstFromPreorder b = new BstFromPreorder();
        test(b::bstFromPreorder, preorder, expected);
        test(b::bstFromPreorder2, preorder, expected);
        test(b::bstFromPreorder3, preorder, expected);
        test(b::bstFromPreorder4, preorder, expected);
        test(b::bstFromPreorder5, preorder, expected);
    }

    @Test public void test() {
        test(new int[] {8, 5, 1, 7, 10, 12}, "8,5,10,1,7,#,12");
        test(new int[] {9, 5, 2, 4, 8, 12, 15, 13}, "9,5,12,2,8,#,15,#,4,#,#,13");
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
