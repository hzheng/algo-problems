import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC501: https://leetcode.com/problems/find-mode-in-binary-search-tree/
//
// Given a binary search tree (BST) with duplicates, find all the mode(s).
public class FindModeInBST {
    // Stack + Hash Table
    // beats 12.59%(25 ms for 25 tests)
    public int[] findMode(TreeNode root) {
        if (root == null) return new int[0];

        Map<Integer, Integer> counter = new HashMap<>();
        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);
        int maxCount = 1;
        while (!stack.isEmpty()) {
            TreeNode n = stack.pop();
            int count = counter.getOrDefault(n.val, 0) + 1;
            maxCount = Math.max(maxCount, count);
            counter.put(n.val, count);
            if (n.right != null) {
                stack.push(n.right);
            }
            if (n.left != null) {
                stack.push(n.left);
            }
        }
        return report(counter, maxCount);
    }

    private int[] report(Map<Integer, Integer> counter, int maxCount) {
        List<Integer> list = new ArrayList<>();
        for (int k : counter.keySet()) {
            if (counter.get(k) == maxCount) {
                list.add(k);
            }
        }
        int[] res = new int[list.size()];
        for (int i = list.size() - 1; i >= 0; i--) {
            res[i] = list.get(i);
        }
        return res;
    }

    // Recursion + Hash Table
    // beats 18.35%(17 ms for 25 tests)
    public int[] findMode2(TreeNode root) {
        Map<Integer, Integer> counter = new HashMap<>();
        int[] maxCount = {0};
        visit(root, counter, maxCount);
        return report(counter, maxCount[0]);
    }

    private void visit(TreeNode root, Map<Integer, Integer> counter, int[] max) {
        if (root == null) return;

        int count = counter.getOrDefault(root.val, 0) + 1;
        max[0] = Math.max(max[0], count);
        counter.put(root.val, count);
        visit(root.left, counter, max);
        visit(root.right, counter, max);
    }

    // Recursion + Inorder(take use of BST property)
    // beats 100%(6 ms for 25 tests)
    public int[] findMode3(TreeNode root) {
        Counter counter = new Counter();
        inOrder(root, counter);
        int[] res = new int[counter.list.size()];
        for (int i = counter.list.size() - 1; i >= 0; i--) {
            res[i] = counter.list.get(i);
        }
        return res;
    }

    private static class Counter {
        int max;
        int cur;
        Integer prev;
        List<Integer> list = new ArrayList<>();
    }

    private void inOrder(TreeNode root, Counter counter) {
        if (root == null) return;

        inOrder(root.left, counter);
        if (counter.prev != null && root.val == counter.prev) {
            counter.cur++;
        } else {
            counter.cur = 1;
        }
        if (counter.cur >= counter.max) {
            if (counter.cur > counter.max) {
                counter.max = counter.cur;
                counter.list = new ArrayList<>(); // counter.list.clear();
            }
            counter.list.add(root.val);
        }
        counter.prev = root.val;
        inOrder(root.right, counter);
    }

    void test(String s, int ... expected) {
        TreeNode root = (s == null) ? null : TreeNode.of(s);
        assertArrayEquals(findMode(root), expected);
        assertArrayEquals(findMode2(root), expected);
        assertArrayEquals(findMode3(root), expected);
    }

    @Test
    public void test() {
        test(null);
        test("1,#,2,2", 2);
        test("6,4,7,0,4,7,9,#,#,4,5", 4);
        test("6,4,7,0,4,7,7,#,#,4,5", 4, 7);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("FindModeInBST");
    }
}
