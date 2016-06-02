import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// Given a binary tree, find its maximum depth.
public class MaxDepthTree {
    // beats 10.21%
    public int maxDepth(TreeNode root) {
        if (root == null) return 0;

        return Math.max(maxDepth(root.left), maxDepth(root.right)) + 1;
    }

    // beats 4.88%
    public int maxDepth2(TreeNode root) {
        if (root == null) return 0;

        Queue<TreeNode> queue = new LinkedList<>();
        int depth = 0;
        int curCount = 1;
        int nextCount = 0;
        queue.offer(root);
        while (!queue.isEmpty()) {
            TreeNode n = queue.poll();
            curCount--;
            if (n.left != null) {
                queue.offer(n.left);
                nextCount++;
            }
            if (n.right != null) {
                queue.offer(n.right);
                nextCount++;
            }
            if (curCount == 0) {
                depth++;
                curCount = nextCount;
                nextCount = 0;
            }
        }
        return depth;
    }

    void test(Function<TreeNode, Integer> maxDepth, String s, int expected) {
        TreeNode root = TreeNode.of(s);
        assertEquals(expected, (int)maxDepth.apply(root));
    }

    void test(String s, int expected) {
        MaxDepthTree m = new MaxDepthTree();
        test(m::maxDepth, s, expected);
        test(m::maxDepth2, s, expected);
    }

    @Test
    public void test1() {
        test("1,#,2,3", 3);
        test("3,9,20,#,#,15,7", 3);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MaxDepthTree");
    }
}
