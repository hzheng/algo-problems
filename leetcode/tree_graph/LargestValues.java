import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;
import common.Utils;

// LC515: https://leetcode.com/problems/find-largest-value-in-each-tree-row/
//
// You need to find the largest value in each row of a binary tree.
public class LargestValues {
    // BFS + Queue
    // beats 63.00%(12 ms for 78 tests)
    public List<Integer> largestValues(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) return res;

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            int max = Integer.MIN_VALUE;
            for (int i = queue.size(); i > 0; i--) {
                TreeNode head = queue.poll();
                max = Math.max(max, head.val);
                if (head.left != null) {
                    queue.offer(head.left);
                }
                if (head.right != null) {
                    queue.offer(head.right);
                }
            }
            res.add(max);
        }
        return res;
    }

    // DFS + Recursion
    // beats 78.56%(11 ms for 78 tests)
    public List<Integer> largestValues2(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        dfs(root, 0, res);
        return res;
    }

    private void dfs(TreeNode root, int level, List<Integer> res) {
        if (root == null) return;

        if (res.size() == level) {
            res.add(root.val);
        } else {
            res.set(level, Math.max(root.val, res.get(level)));
        }
        dfs(root.left, level + 1, res);
        dfs(root.right, level + 1, res);
    }

    void test(Function<TreeNode, List<Integer>> largestValues,
              String s, int... expected) {
        int[] res = Utils.toArray(largestValues.apply(TreeNode.of(s)));
        assertArrayEquals(expected, res);
    }

    void test(String s, int... expected) {
        LargestValues l = new LargestValues();
        test(l::largestValues, s, expected);
        test(l::largestValues2, s, expected);
    }

    @Test
    public void test() {
        test("1", 1);
        test("1,2,3,5,3,#,9", 1, 3, 9);
        test("1,2,3,5,3,10,9", 1, 3, 10);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("LargestValues");
    }
}
