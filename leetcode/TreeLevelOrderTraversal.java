import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// Given a binary tree, return the level order traversal of its nodes' values.
public class TreeLevelOrderTraversal {
    // beats 14.03%
    public List<List<Integer> > levelOrder(TreeNode root) {
        List<List<Integer> > res = new ArrayList<>();
        if (root == null) return res;

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        List<Integer> level = null;
        while (!queue.isEmpty()) {
            TreeNode n = queue.poll();
            if (n == null) { // finished one level
                res.add(level);
                level = null;
                continue;
            }

            if (level == null) { // new level begins
                level = new ArrayList<>();
                queue.offer(null);
            }
            level.add(n.val);
            if (n.left != null) {
                queue.offer(n.left);
            }
            if (n.right != null) {
                queue.offer(n.right);
            }
        }
        return res;
    }

    // beats 58.26%
    public List<List<Integer> > levelOrder2(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) return res;

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            List<Integer> level = new ArrayList<>();
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                TreeNode head = queue.poll();
                level.add(head.val);
                if (head.left != null) {
                    queue.offer(head.left);
                }
                if (head.right != null) {
                    queue.offer(head.right);
                }
            }
            res.add(level);
        }
        return res;
    }

    // beats 6.32%
    public List<List<Integer> > levelOrder3(TreeNode root) {
        List<List<Integer> > res = new ArrayList<>();

        for (int max = 0; ; max++) {
            List<Integer> level = new ArrayList<>();
            dfs(root, level, 0, max);
            if (level.size() == 0) break;

            res.add(level);
        }
        return res;
    }

    private void dfs(TreeNode root, List<Integer> level, int cur, int max) {
        if (root == null || cur > max) return;

        if (cur == max) {
            level.add(root.val);
            return;
        }

        dfs(root.left, level, cur + 1, max);
        dfs(root.right, level, cur + 1, max);
    }

    void test(Function<TreeNode, List<List<Integer>>> traversal,
              String s, int[] ... expected) {
        TreeNode root = TreeNode.of(s);
        Integer[][] res = traversal.apply(root).stream().map(
            l -> l.toArray(new Integer[0])).toArray(Integer[][]::new);
        assertArrayEquals(expected, res);
    }

    void test(String s, int[] ... expected) {
        TreeLevelOrderTraversal t = new TreeLevelOrderTraversal();
        test(t::levelOrder, s, expected);
        test(t::levelOrder2, s, expected);
        test(t::levelOrder3, s, expected);
    }

    @Test
    public void test1() {
        test("1,#,2,3", new int[][] {{1}, {2}, {3}});
        test("3,9,20,#,#,15,7", new int[][] {{3}, {9, 20}, {15, 7}});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("TreeLevelOrderTraversal");
    }
}
