import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC298: https://leetcode.com/problems/binary-tree-longest-consecutive-sequence
//
// Given a binary tree, find the length of the longest consecutive sequence path.
// The path refers to any sequence of nodes from some starting node to any node
// in the tree along the parent-child connections. The longest consecutive path
// need to be from parent to child (cannot be the reverse).
public class LongestConsecutivePath {
    // DFS(Top-Down) + Backtracking + Recursion
    // beats 65.91%(2 ms for 54 tests)
    public int longestConsecutive(TreeNode root) {
        int[] res = new int[2];
        dfs(null, root, res);
        return res[0];
    }

    private void dfs(TreeNode prev, TreeNode cur, int[] res) {
        if (cur == null) return;

        if (prev == null || prev.val + 1 != cur.val) {
            res[1] = 1;
        } else {
            res[1]++;
        }
        int old = res[1];
        res[0] = Math.max(res[0], res[1]);
        dfs(cur, cur.left, res);
        res[1] = old;
        dfs(cur, cur.right, res);
    }

    // DFS(Top-Down) + Recursion
    // beats 65.91%(2 ms for 54 tests)
    public int longestConsecutive2(TreeNode root) {
        return dfs2(null, root, 0);
    }

    private int dfs2(TreeNode prev, TreeNode cur, int count) {
        if (cur == null) return count;

        if (prev == null || prev.val + 1 != cur.val) {
            count = 1;
        } else {
            count++;
        }
        return Math.max(count, Math.max(dfs2(cur, cur.left, count), dfs2(cur, cur.right, count)));
    }

    // DFS(Top-Down) + Recursion
    // beats 65.91%(2 ms for 54 tests)
    public int longestConsecutive3(TreeNode root) {
        return root == null ? 0 : dfs3(root, root.val, 0);
    }

    private int dfs3(TreeNode root, int target, int count) {
        if (root == null) return count;

        if (root.val == target) {
            count++;
        } else {
            count = 1;
        }
        return Math.max(count, Math.max(dfs3(root.left, root.val + 1, count),
                                        dfs3(root.right, root.val + 1, count)));
    }

    // DFS(Bottom-Up) + Recursion
    // beats 31.06%(3 ms for 54 tests)
    public int longestConsecutive4(TreeNode root) {
        int[] max = new int[1];
        dfs4(root, max);
        return max[0];
    }

    private int dfs4(TreeNode root, int[] max) {
        if (root == null) return 0;

        int lMax = dfs4(root.left, max) + 1;
        int rMax = dfs4(root.right, max) + 1;
        if (root.left != null && root.val + 1 != root.left.val) {
            lMax = 1;
        }
        if (root.right != null && root.val + 1 != root.right.val) {
            rMax = 1;
        }
        int res = Math.max(lMax, rMax);
        max[0] = Math.max(max[0], res);
        return res;
    }

    static class CountedNode {
        TreeNode node;
        int count;
        CountedNode(TreeNode node, int count) {
            this.node = node;
            this.count = count;
        }
    }

    // DFS + Stack
    // beats 4.61%(19 ms for 54 tests)
    public int longestConsecutive5(TreeNode root) {
        if (root == null) return 0;

        ArrayDeque<CountedNode> stack = new ArrayDeque<>();
        int max = 1;
        stack.push(new CountedNode(root, 1));
        while (!stack.isEmpty()) {
            CountedNode top = stack.pop();
            max = Math.max(max, top.count);
            if (top.node.right != null) {
                int count = (top.node.val + 1 == top.node.right.val) ? top.count + 1 : 1;
                stack.push(new CountedNode(top.node.right, count));
            }
            if (top.node.left != null) {
                int count = (top.node.val + 1 == top.node.left.val) ? top.count + 1 : 1;
                stack.push(new CountedNode(top.node.left, count));
            }
        }
        return max;
    }

    // BFS + Queue
    // beats 4.89%(18 ms for 54 tests)
    public int longestConsecutive6(TreeNode root) {
        if (root == null) return 0;

        Queue<CountedNode> queue = new LinkedList<>();
        int max = 1;
        queue.offer(new CountedNode(root, 1));
        while (!queue.isEmpty()) {
            CountedNode head = queue.poll();
            max = Math.max(max, head.count);
            if (head.node.left != null) {
                int count = (head.node.val + 1 == head.node.left.val) ? head.count + 1 : 1;
                queue.offer(new CountedNode(head.node.left, count));
            }
            if (head.node.right != null) {
                int count = (head.node.val + 1 == head.node.right.val) ? head.count + 1 : 1;
                queue.offer(new CountedNode(head.node.right, count));
            }
        }
        return max;
    }

    void test(String s, int expected) {
        assertEquals(expected, longestConsecutive(TreeNode.of(s)));
        assertEquals(expected, longestConsecutive2(TreeNode.of(s)));
        assertEquals(expected, longestConsecutive3(TreeNode.of(s)));
        assertEquals(expected, longestConsecutive4(TreeNode.of(s)));
        assertEquals(expected, longestConsecutive5(TreeNode.of(s)));
        assertEquals(expected, longestConsecutive6(TreeNode.of(s)));
    }

    @Test
    public void test() {
        test("1,2,2", 2);
        test("1,2,3,4,5", 2);
        test("1,#,3,2,4,#,#,#,5", 3);
        test("2,#,3,2,#,1", 2);
        test("1,2,6,3,#,7,8,4,#,#,#,#,#,5", 5);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("LongestConsecutivePath");
    }
}
