import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC513: https://leetcode.com/problems/find-bottom-left-tree-value/
//
// Given a binary tree, find the leftmost value in the last row of the tree.
public class BottomLeftTreeValue {
    // BFS + Queue
    // beats 32.37%(11 ms for 74 tests)
    public int findBottomLeftValue(TreeNode root) {
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        int res = 0;
        while (!queue.isEmpty()) {
            res = queue.peek().val;
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
        return res;
    }

    // DFS + Recursion
    // beats 86.06%(7 ms for 74 tests)
    public int findBottomLeftValue2(TreeNode root) {
        int[] res = new int[2];
        dfs(root, 1, res);
        return res[1];
    }

    private void dfs(TreeNode root, int level, int[] res) {
        if (root.left != null) {
            dfs(root.left, level + 1, res);
        } else if (level > res[0]) {
            res[0] = level;
            res[1] = root.val;
        }
        if (root.right != null) {
            dfs(root.right, level + 1, res);
        }
    }

    // DFS + Stack
    // beats 45.51%(10 ms for 74 tests)
    public int findBottomLeftValue3(TreeNode root) {
        ArrayDeque<LevelNode> stack = new ArrayDeque<>();
        int maxLevel = 0;
        int level = 0;
        int res = -1;
        for (TreeNode n = root; n != null || !stack.isEmpty(); ) {
            if (n != null) {
                stack.push(new LevelNode(n, ++level));
                n = n.left;
            } else {
                LevelNode top = stack.pop();
                level = top.level;
                if (level > maxLevel) {
                    res = top.node.val;
                    maxLevel = level;
                }
                n = top.node.right;
            }
        }
        return res;
    }

    static class LevelNode {
        TreeNode node;
        int level;
        LevelNode(TreeNode node, int level) {
            this.node = node;
            this.level = level;
        }
    }

    // DFS + Stack
    // beats 12.18%(14 ms for 74 tests)
    public int findBottomLeftValue4(TreeNode root) {
        ArrayDeque<TreeNode> nodeStack = new ArrayDeque<>();
        ArrayDeque<Integer> levelStack = new ArrayDeque<>();
        int maxLevel = 0;
        int level = 0;
        int res = -1;
        for (TreeNode n = root; n != null || !nodeStack.isEmpty(); ) {
            if (n != null) {
                nodeStack.push(n);
                levelStack.push(++level);
                n = n.left;
            } else {
                TreeNode top = nodeStack.pop();
                level = levelStack.pop();
                if (level > maxLevel) {
                    res = top.val;
                    maxLevel = level;
                }
                n = top.right;
            }
        }
        return res;
    }

    void test(String s, int expected) {
        assertEquals(expected, findBottomLeftValue(TreeNode.of(s)));
        assertEquals(expected, findBottomLeftValue2(TreeNode.of(s)));
        assertEquals(expected, findBottomLeftValue3(TreeNode.of(s)));
        assertEquals(expected, findBottomLeftValue4(TreeNode.of(s)));
    }

    @Test
    public void test() {
        test("3,1,5,0,2,4,6,#,#,#,7", 7);
        test("1,2,3,4,#,5,6,#,#,7", 7);
        test("2,1,3", 1);
        test("1", 1);
        test("1,#,2", 2);
        test("1,2,3,4,#,5,6,7", 7);
        test("1,2,3,4,#,5,6,7,8", 7);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("BottomLeftTreeValue");
    }
}
