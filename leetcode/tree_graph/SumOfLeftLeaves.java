import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC404: https://leetcode.com/problems/sum-of-left-leaves/
//
// Find the sum of all left leaves in a given binary tree.
public class SumOfLeftLeaves {
    // BFS + Queue
    // beats 24.90%(10 ms for 102 tests)
    public int sumOfLeftLeaves(TreeNode root) {
        if (root == null) return 0;

        Queue<Node> queue = new LinkedList<>();
        queue.offer(new Node(root, false));
        int sum = 0;
        while (!queue.isEmpty()) {
            Node cur = queue.poll();
            TreeNode curNode = cur.node;
            if (curNode.left == null && curNode.right == null) {
                if (cur.isLeft) {
                    sum += curNode.val;
                }
                continue;
            }
            if (curNode.left != null) {
                queue.offer(new Node(curNode.left, true));
            }
            if (curNode.right != null) {
                queue.offer(new Node(curNode.right, false));
            }
        }
        return sum;
    }

    private static class Node {
        TreeNode node;
        boolean isLeft;
        Node(TreeNode node, boolean isLeft) {
            this.node = node;
            this.isLeft = isLeft;
        }
    }

    // BFS + Queue
    // beats 24.90%(10 ms for 102 tests)
    public int sumOfLeftLeaves2(TreeNode root) {
        if (root == null) return 0;

        int sum = 0;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            TreeNode cur = queue.poll();
            if (cur.left != null) {
                if (cur.left.left == null && cur.left.right == null) {
                    sum += cur.left.val;
                }
                queue.offer(cur.left);
            }
            if (cur.right != null) {
                queue.offer(cur.right);
            }
        }
        return sum;
    }

    // DFS + Recursion
    // beats 97.82%(7 ms for 102 tests)
    public int sumOfLeftLeaves3(TreeNode root) {
        if (root == null) return 0;

        int sum = 0;
        if (root.left != null) {
            if (root.left.left == null && root.left.right == null) {
                sum = root.left.val;
            } else {
                sum = sumOfLeftLeaves2(root.left);
            }
        }
        return sum + sumOfLeftLeaves2(root.right);
    }

    // DFS + Stack
    public int sumOfLeftLeaves4(TreeNode root) {
        if (root == null) return 0;

        int sum = 0;
        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            TreeNode cur = stack.pop();
            if (cur.left != null) {
                if (cur.left.left == null && cur.left.right == null) {
                    sum += cur.left.val;
                }
                stack.push(cur.left);
            }
            if (cur.right != null) {
                stack.push(cur.right);
            }
        }
        return sum;
    }

    void test(String s, int expected) {
        TreeNode root = TreeNode.of(s);
        assertEquals(expected, sumOfLeftLeaves(root));
        assertEquals(expected, sumOfLeftLeaves2(root));
        assertEquals(expected, sumOfLeftLeaves3(root));
        assertEquals(expected, sumOfLeftLeaves4(root));
    }

    @Test
    public void test1() {
        test("3", 0);
        test("3,9,20,#,#,15,7", 24);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("SumOfLeftLeaves");
    }
}
