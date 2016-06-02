import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// Given a binary tree, return the bottom-up level order traversal of its nodes'
// values. (ie, from left to right, level by level from leaf to root).
public class TreeLevelOrderTraversal2 {
    // beats 31.93%
    public List<List<Integer>> levelOrderBottom(TreeNode root) {
        List<List<Integer> > res = new ArrayList<>();
        if (root == null) return res;

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        List<Integer> level = null;
        while (!queue.isEmpty()) {
            TreeNode n = queue.poll();
            if (n == null) { // finished one level
                res.add(0, level); // or reverse the res at the end
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

    // beats 31.93%
    public List<List<Integer> > levelOrderBottom2(TreeNode root) {
        List<List<Integer> > res = new ArrayList<>();
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
            res.add(0, level);
        }
        return res;
    }

    // beats 3.33%
    public List<List<Integer> > levelOrderBottom3(TreeNode root) {
        List<List<Integer> > res = new ArrayList<>();
        if (root != null) {
            levelOrderBottom3(Arrays.asList(root), res);
        }
        return res;
    }

    private void levelOrderBottom3(List<TreeNode> level, List<List<Integer> > res) {
        List<Integer> levelVal = new LinkedList<>();
        List<TreeNode> nextLevel = new LinkedList<>();
        for (TreeNode node : level) {
            levelVal.add(node.val);
            if (node.left != null) {
                nextLevel.add(node.left);
            }
            if (node.right != null) {
                nextLevel.add(node.right);
            }
        }
        res.add(0, levelVal);
        if (!nextLevel.isEmpty()) {
            levelOrderBottom3(nextLevel, res);
        }
    }

    void test(Function<TreeNode, List<List<Integer>>> traversal,
              String s, int[] ... expected) {
        TreeNode root = TreeNode.of(s);
        Integer[][] res = traversal.apply(root).stream().map(
            l -> l.toArray(new Integer[0])).toArray(Integer[][]::new);
        assertArrayEquals(expected, res);
    }

    void test(String s, int[] ... expected) {
        TreeLevelOrderTraversal2 t = new TreeLevelOrderTraversal2();
        test(t::levelOrderBottom, s, expected);
        test(t::levelOrderBottom2, s, expected);
        test(t::levelOrderBottom3, s, expected);
    }

    @Test
    public void test1() {
        test("1,#,2,3", new int[][] {{3}, {2}, {1}});
        test("3,9,20,#,#,15,7", new int[][] {{15, 7}, {9, 20}, {3}});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("TreeLevelOrderTraversal2");
    }
}
