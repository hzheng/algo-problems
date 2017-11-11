import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC637: https://leetcode.com/problems/average-of-levels-in-binary-tree/
//
// Given a non-empty binary tree, return the average value of the nodes on each
// level in the form of an array.
public class AverageOfLevels {
    // BFS + Queue
    // beats 74.59%(10 ms for 65 tests)
    public List<Double> averageOfLevels(TreeNode root) {
        List<Double> res = new ArrayList<>();
        if (root == null) return res;

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            double sum = 0;
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                sum += node.val;
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
            res.add(sum / size);
        }
        return res;
    }

    // DFS + Recursion
    // beats 74.59%(10 ms for 65 tests)
    public List<Double> averageOfLevels2(TreeNode root) {
        List<Double> res = new ArrayList<>();
        List<Integer> count = new ArrayList<>();
        average(root, 0, res, count);
        for (int i = 0; i < res.size(); i++) {
            res.set(i, res.get(i) / count.get(i));
        }
        return res;
    }

    private void average(TreeNode root, int level, List<Double> sum,
                         List<Integer> count) {
        if (root == null) return;

        if (level < sum.size()) {
            sum.set(level, sum.get(level) + root.val);
            count.set(level, count.get(level) + 1);
        } else {
            sum.add((double)root.val);
            count.add(1);
        }
        average(root.left, level + 1, sum, count);
        average(root.right, level + 1, sum, count);
    }

    void test(String tree, Double[] expected) {
        TreeNode root = TreeNode.of(tree);
        List<Double> expectedList = Arrays.asList(expected);
        assertEquals(expectedList, averageOfLevels(root));
        assertEquals(expectedList, averageOfLevels2(root));
    }

    @Test
    public void test() {
        test("3,9,20,#,#,15,7", new Double[] {3d, 14.5, 11d});
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
