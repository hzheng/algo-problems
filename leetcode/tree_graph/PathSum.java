import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC112: https://leetcode.com/problems/path-sum/
//
// Given a binary tree and a sum, determine if the tree has a root-to-leaf path
// such that adding up all the values along the path equals the given sum.
public class PathSum {
    // Solution of Choice
    // DFS + Recursion
    // beats 14.31%(1 ms)
    public boolean hasPathSum(TreeNode root, int sum) {
        if (root == null) return false;

        if (root.left == null && root.right == null) return sum == root.val;

        sum -= root.val;
        return hasPathSum(root.left, sum) || hasPathSum(root.right, sum);
    }

    // BFS + Queue
    // beats 7.44%(3 ms)
    public boolean hasPathSum2(TreeNode root, int sum) {
        if (root == null) return false;

        Queue<TreeNode> nodes = new LinkedList<>();
        Queue<Integer> values = new LinkedList<>();
        nodes.add(root);
        values.add(root.val);
        while (!nodes.isEmpty()) {
            TreeNode cur = nodes.poll();
            int val = values.poll();
            if (cur.left == null && cur.right == null && val == sum) return true;

            if (cur.left != null) {
                nodes.offer(cur.left);
                values.offer(val + cur.left.val);
            }
            if (cur.right != null) {
                nodes.offer(cur.right);
                values.offer(val + cur.right.val);
            }
        }
        return false;
    }

    // Solution of Choice
    // Stack(Postorder traversal)
    // beats 3.83%(5 ms)
    public boolean hasPathSum3(TreeNode root, int sum) {
        Stack<TreeNode> stack = new Stack<>();
        int curSum = 0;
        for (TreeNode cur = root, prev = null; cur != null || !stack.isEmpty(); ) {
            if (cur != null) {
                stack.push(cur);
                curSum += cur.val;
                cur = cur.left;
            } else {
                cur = stack.peek();
                if (cur.left == null && cur.right == null && curSum == sum) return true;

                if (cur.right != null && prev != cur.right) {
                    cur = cur.right;
                } else {
                    curSum -= cur.val;
                    prev = cur;
                    cur = null;
                    stack.pop();
                }
            }
        }
        return false;
    }

    void test(String s, int sum, boolean expected) {
        TreeNode root = TreeNode.of(s);
        assertEquals(expected, hasPathSum(root, sum));
        assertEquals(expected, hasPathSum2(root, sum));
        assertEquals(expected, hasPathSum3(root, sum));
    }

    @Test
    public void test1() {
        test("1,2", 1, false);
        test("5,4,8,11,#,13,4,7,2,#,1", 22, true);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("PathSum");
    }
}
