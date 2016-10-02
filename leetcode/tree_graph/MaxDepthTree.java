import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC104: https://leetcode.com/problems/maximum-depth-of-binary-tree/
//
// Given a binary tree, find its maximum depth.
public class MaxDepthTree {
    // DFS/Recursion
    // beats 15.15%(1 ms)
    public int maxDepth(TreeNode root) {
        if (root == null) return 0;

        return Math.max(maxDepth(root.left), maxDepth(root.right)) + 1;
    }

    // BFS/Queue
    // beats 4.99%(3 ms)
    public int maxDepth2(TreeNode root) {
        if (root == null) return 0;

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        int depth = 0;
        for (int curCount = 1, nextCount = 0; !queue.isEmpty(); ) {
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

    // Solution of Choice
    // BFS/Queue
    // beats 4.99%(3 ms)
    public int maxDepth3(TreeNode root) {
        if (root == null) return 0;

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        int depth = 0;
        for (; !queue.isEmpty(); depth++) {
            for (int i = queue.size(); i > 0; i--) {
                TreeNode head = queue.poll();
                if (head.left != null) {
                    queue.offer(head.left);
                }
                if (head.right != null) {
                    queue.offer(head.right);
                }
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
        test(m::maxDepth3, s, expected);
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
