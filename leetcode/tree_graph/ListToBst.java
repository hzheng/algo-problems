import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;
import common.ListNode;

// LC109: https://leetcode.com/problems/convert-sorted-list-to-binary-search-tree/
//
// Given a singly linked list where elements are sorted in ascending order,
// convert it to a height balanced BST.
public class ListToBst {
    // Recursion + Fast Pointer
    // time complexity: O(N * Log(N)), space complexity: O(1)
    // beats 45.93%(1 ms)
    public TreeNode sortedListToBST(ListNode head) {
        return sortedListToBST(head, null);
    }

    private TreeNode sortedListToBST(ListNode start, ListNode end) {
        if (start == end) return null;

        ListNode slow = start;
        for (ListNode fast = start; fast != end && fast.next != end;
             fast = fast.next.next, slow = slow.next) {}
        TreeNode parent = new TreeNode(slow.val);
        parent.left = sortedListToBST(start, slow);
        parent.right = sortedListToBST(slow.next, end);
        return parent;
    }

    // Divide & Conquer/Recursion(Top-down)
    // beats 4.77%(3 ms)
    // time complexity: O(N), space complexity: O(N)
    public TreeNode sortedListToBST2(ListNode head) {
        List<Integer> nums = new ArrayList<>();
        for (ListNode n = head; n != null; n = n.next) {
            nums.add(n.val);
        }
        return sortedListToBST2(nums, 0, nums.size());
    }

    private TreeNode sortedListToBST2(List<Integer> nums, int start, int end) {
        if (start >= end) return null;

        int mid = (start + end) >>> 1;
        TreeNode parent = new TreeNode(nums.get(mid));
        parent.left = sortedListToBST2(nums, start, mid);
        parent.right = sortedListToBST2(nums, mid + 1, end);
        return parent;
    }

    // Solution of Choice
    // http://articles.leetcode.com/convert-sorted-list-to-balanced-binary
    // Divide & Conquer/Recursion(Bottom-up) + Delimiter
    // time complexity: O(N), space complexity: O(1)
    // beats 45.93%(1 ms)
    public TreeNode sortedListToBST3(ListNode head) {
        int len = 0;
        for (ListNode n = head; n != null; n = n.next) {
            len++;
        }
        return sortedListToBST3(len, new ListNode[] {head});
    }

    private TreeNode sortedListToBST3(int size, ListNode[] current) {
        if (size <= 0) return null;

        TreeNode left = sortedListToBST3(size / 2, current);
        TreeNode root = new TreeNode(current[0].val);
        current[0] = current[0].next;
        root.right = sortedListToBST3(size - 1 - size / 2, current);
        root.left = left;
        return root;
    }

    // TODO: iterative solution
    // convert to array and apply iterative way in ArrayToBst.java

    void test(Function<ListNode, TreeNode> convert, int[] nums, String expected) {
        TreeNode expectedTree = TreeNode.of(expected);
        TreeNode res = convert.apply(ListNode.of(nums));
        // System.out.println(res);
        assertArrayEquals(expectedTree.toArray(), res.toArray());
    }

    void test(int[] nums, String ... expected) {
        ListToBst l = new ListToBst();
        test(l::sortedListToBST, nums, expected[0]);
        test(l::sortedListToBST2, nums, expected[0]);
        test(l::sortedListToBST3, nums, expected[0]);
    }

    @Test
    public void test1() {
        test(new int[] {1, 2, 3}, "2,1,3", "2,1,3");
        test(new int[] {1, 2, 3, 4}, "3,2,4,1", "2,1,3,#,#,#,4");
        test(new int[] {1, 2, 3, 4, 5}, "3,2,5,1,#,4", "3,1,4,#,2,#,5");
        test(new int[] {1, 2, 3, 4, 5, 6}, "4,2,6,1,3,5", "3,1,5,#,2,4,6");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ListToBst");
    }
}
