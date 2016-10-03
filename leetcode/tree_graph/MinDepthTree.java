import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC111: https://leetcode.com/problems/minimum-depth-of-binary-tree/
//
// Given a binary tree, find its minimum depth.
// The minimum depth is the number of nodes along the shortest path from the
// root node down to the nearest leaf node.
public class MinDepthTree {
    // DFS + Recursion
    // beats 10.71%(1 ms)
    public int minDepth(TreeNode root) {
        if (root == null) return 0;

        if (root.left == null) return minDepth(root.right) + 1;

        if (root.right == null) return minDepth(root.left) + 1;

        return Math.min(minDepth(root.left), minDepth(root.right)) + 1;
    }

    // BFS + Queue
    // beats 3.90%(2 ms)
    public int minDepth2(TreeNode root) {
        if (root == null) return 0;

        int depth = 1;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        for (int curCount = 1, nextCount = 0; !queue.isEmpty(); ) {
            TreeNode n = queue.poll();
            curCount--;
            if (n.left != null) {
                queue.offer(n.left);
                nextCount++;
            } else if (n.right == null) break;

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

    // Solution of Choice
    // BFS + Queue
    // beats 15.24%(1 ms)
    public int minDepth3(TreeNode root) {
        if (root == null) return 0;

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        for (int depth = 1; ; depth++) {
            for (int i = queue.size(); i > 0; i--) {
                TreeNode head = queue.poll();
                if (head.left == null && head.right == null) return depth;

                if (head.left != null) {
                    queue.offer(head.left);
                }
                if (head.right != null) {
                    queue.offer(head.right);
                }
            }
        }
    }

    void test(Function<TreeNode, Integer> minDepth, String s, int expected) {
        TreeNode root = TreeNode.of(s);
        assertEquals(expected, (int)minDepth.apply(root));
    }

    void test(String s, int expected) {
        MinDepthTree m = new MinDepthTree();
        test(m::minDepth, s, expected);
        test(m::minDepth2, s, expected);
        test(m::minDepth3, s, expected);
    }

    @Test
    public void test1() {
        test("1,2", 2);
        test("1,#,2,3", 3);
        test("1,2,3,4", 2);
        test("3,9,20,#,#,15,7", 2);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MinDepthTree");
    }
}
