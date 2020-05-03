import common.ListNode;
import common.TreeNode;

import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1367: https://leetcode.com/problems/linked-list-in-binary-tree/
//
// Given a binary tree root and a linked list with head as the first node. Return True if all the
// elements in the linked list starting from the head correspond to some downward path connected in
// the binary tree otherwise return False.
// In this context downward path means a path that starts at some node and goes downwards.
// Constraints:
// 1 <= node.val <= 100 for each node in the linked list and binary tree.
// The given linked list will contain between 1 and 100 nodes.
// The given binary tree will contain between 1 and 2500 nodes.
public class LinkedListInTree {
    // Recursion
    // time complexity: O(N * min(H, L)), space complexity: O(H) (N/L: tree/list size H: tree height)
    // 1 ms(97.94%), 39.4 MB(100%) for 61 tests
    public boolean isSubPath(ListNode head, TreeNode root) {
        if (root == null) { return false; }

        return pathStarts(head, root) || isSubPath(head, root.left) || isSubPath(head, root.right);
    }

    private boolean pathStarts(ListNode head, TreeNode root) {
        if (head == null) { return true; }
        if (root == null || head.val != root.val) { return false; }

        return pathStarts(head.next, root.left) || pathStarts(head.next, root.right);
    }

    // Dynamic Programming (KMP algorithm)
    // time complexity: O(N), space complexity: O(L+H)
    // 1 ms(97.94%), 47.3 MB(100%) for 61 tests
    public boolean isSubPath2(ListNode head, TreeNode root) {
        List<int[]> kmp = new ArrayList<>();
        kmp.add(new int[] {0, head.val});
        int i = 0;
        for (ListNode cur = head.next; cur != null; cur = cur.next) {
            for (; i > 0 && cur.val != kmp.get(i)[1]; i = kmp.get(i - 1)[0]) {}
            if (cur.val == kmp.get(i)[1]) {
                i++;
            }
            kmp.add(new int[] {i, cur.val});
        }
        return dfs(root, 0, kmp);
    }

    private boolean dfs(TreeNode root, int cur, List<int[]> kmp) {
        if (root == null) { return false; }

        for (; cur > 0 && root.val != kmp.get(cur)[1]; cur = kmp.get(cur - 1)[0]) {}
        if (root.val == kmp.get(cur)[1]) {
            cur++;
        }
        return cur == kmp.size() || dfs(root.left, cur, kmp) || dfs(root.right, cur, kmp);
    }

    private void test(int[] list, String tree, boolean expected) {
        ListNode head = ListNode.of(list);
        TreeNode root = TreeNode.of(tree);
        assertEquals(expected, isSubPath(head, root));
        assertEquals(expected, isSubPath2(head, root));
    }

    @Test public void test() {
        test(new int[] {4, 2, 8}, "1,4,4,#,2,2,#,1,#,6,8,#,#,#,#,1,3", true);
        test(new int[] {1, 4, 2, 6}, "1,4,4,#,2,2,#,1,#,6,8,#,#,#,#,1,3", true);
        test(new int[] {1, 4, 2, 6, 8}, "1,4,4,#,2,2,#,1,#,6,8,#,#,#,#,1,3", false);
        test(new int[] {1, 4, 1}, "1,4,4,1,2,2,#,1,#,6,8,#,#,#,#,1,3", true);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
