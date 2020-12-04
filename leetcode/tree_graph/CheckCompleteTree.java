import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

import common.TreeNode;

// LC958: https://leetcode.com/problems/check-completeness-of-a-binary-tree/
//
// Given the root of a binary tree, determine if it is a complete binary tree.
// In a complete binary tree, every level, except possibly the last, is completely filled, and all
// nodes in the last level are as far left as possible. It can have between 1 and 2^h nodes
// inclusive at the last level h.
//
// The number of nodes in the tree is in the range [1, 100].
// 1 <= Node.val <= 1000
public class CheckCompleteTree {
    // BFS + Queue
    // time complexity: O(N), space complexity: O(N)
    // 0 ms(100.00%), 38.5 MB(23.38%) for 119 tests
    public boolean isCompleteTree(TreeNode root) {
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        for (boolean done = false; !queue.isEmpty(); ) {
            for (int size = queue.size(); size > 0; size--) {
                TreeNode cur = queue.poll();
                if (cur.left == null) {
                    done = true;
                } else if (done) {
                    return false;
                } else {
                    queue.offer(cur.left);
                }
                if (cur.right == null) {
                    done = true;
                } else if (done) {
                    return false;
                } else {
                    queue.offer(cur.right);
                }
            }
            if (done) {
                while (!queue.isEmpty()) {
                    TreeNode cur = queue.poll();
                    if (cur.left != null || cur.right != null) { return false; }
                }
            }
        }
        return true;
    }

    // DFS + Recursion
    // time complexity: O(N), space complexity: O(N)
    // 0 ms(100.00%), 38 MB(97.11%) for 119 tests
    public boolean isCompleteTree2(TreeNode root) {
        int h = getHeight(root);
        int[] count = new int[h];
        if (!check(root, 0, count, new boolean[] {false})) {
            return false;
        }
        for (int i = 1; i < h - 1; i++) {
            if (count[i] != count[i - 1] * 2) { return false; }
        }
        return true;
    }

    private boolean check(TreeNode root, int h, int[] count, boolean[] done) {
        if (root == null) { return true; }

        count[h]++;
        if (h == count.length - 2) { // second to last row
            if (root.left == null) {
                done[0] = true;
            } else if (done[0]) {
                return false;
            }
            if (root.right == null) {
                done[0] = true;
            } else { return !done[0]; }
            return true;
        }
        return check(root.left, h + 1, count, done) && check(root.right, h + 1, count, done);
    }

    private int getHeight(TreeNode root) {
        if (root == null) { return 0; }

        return 1 + Math.max(getHeight(root.left), getHeight(root.right));
    }

    // BFS + List
    // time complexity: O(N), space complexity: O(N)
    // 1 ms(64.41%), 38.8 MB(8.77%) for 119 tests
    public boolean isCompleteTree3(TreeNode root) {
        List<CodedNode> nodes = new ArrayList<>();
        nodes.add(new CodedNode(root, 1));
        int i = 0;
        for (; i < nodes.size(); i++) {
            CodedNode cn = nodes.get(i);
            if (cn.node != null) {
                nodes.add(new CodedNode(cn.node.left, cn.code * 2));
                nodes.add(new CodedNode(cn.node.right, cn.code * 2 + 1));
            }
        }
        return nodes.get(i - 1).code == nodes.size();
    }

    private static class CodedNode {
        TreeNode node;
        int code;

        CodedNode(TreeNode node, int code) {
            this.node = node;
            this.code = code;
        }
    }

    // BFS + Queue
    // time complexity: O(N), space complexity: O(N)
    // 0 ms(100.00%), 38.4 MB(48.31%) for 119 tests
    public boolean isCompleteTree4(TreeNode root) {
        Queue<TreeNode> queue = new LinkedList<>();
        for (queue.offer(root); ; ) {
            TreeNode cur = queue.poll();
            if (cur == null) { break; }

            queue.offer(cur.left);
            queue.offer(cur.right);
        }
        while (!queue.isEmpty()) {
            if (queue.poll() != null) { return false; }
        }
        return true;
    }

    // BFS + Queue
    // time complexity: O(N), space complexity: O(N)
    // 0 ms(100.00%), 38.4 MB(48.31%) for 119 tests
    public boolean isCompleteTree5(TreeNode root) {
        Queue<TreeNode> queue = new LinkedList<>();
        for (queue.offer(root); ; ) {
            TreeNode cur = queue.poll();
            if (cur.left == null) {
                if (cur.right != null) { return false; }

                break;
            }
            queue.offer(cur.left);
            if (cur.right == null) { break; }

            queue.offer(cur.right);
        }
        while (!queue.isEmpty()) {
            TreeNode cur = queue.poll();
            if (cur.left != null || cur.right != null) {
                return false;
            }
        }
        return true;
    }

    // Solution of Choice
    // BFS + Queue
    // time complexity: O(N), space complexity: O(N)
    // 0 ms(100.00%), 38.5 MB(23.38%) for 119 tests
    public boolean isCompleteTree6(TreeNode root) {
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        for (boolean done = false; !queue.isEmpty(); ) {
            TreeNode cur = queue.poll();
            if (cur == null) {
                done = true;
            } else {
                if (done) { return false; }

                queue.offer(cur.left);
                queue.offer(cur.right);
            }
        }
        return true;
    }

    // DFS + Recursion
    // time complexity: O(N), space complexity: O(N)
    // 0 ms(100.00%), 38 MB(97.11%) for 119 tests
    public boolean isCompleteTree7(TreeNode root) {
        return count(root) >= 0;
    }

    private int count(TreeNode root) {
        if (root == null) { return 0; }

        int lc = count(root.left);
        if (lc < 0) { return lc; }

        int rc = count(root.right);
        if (rc < 0) { return rc; }

        if (((lc & (lc + 1)) == 0 && lc / 2 <= rc && rc <= lc) || ((rc & (rc + 1)) == 0 && rc <= lc
                                                                   && lc <= rc * 2 + 1)) {
            return lc + rc + 1;
        }
        return -1;
    }

    // DFS + Recursion
    // time complexity: O(N), space complexity: O(N)
    // 0 ms(100.00%), 38 MB(97.11%) for 119 tests
    public boolean isCompleteTree8(TreeNode root) {
        return isComplete(root, 0, countNodes(root));
    }

    private boolean isComplete(TreeNode root, int i, int n) {
        if (root == null) { return true; }
        if (i >= n) { return false; }

        return isComplete(root.left, 2 * i + 1, n) && isComplete(root.right, 2 * i + 2, n);
    }

    private int countNodes(TreeNode root) {
        if (root == null) { return 0; }

        return 1 + countNodes(root.left) + countNodes(root.right);
    }

    private void test(String s, boolean expected) {
        TreeNode root = TreeNode.of(s);
        assertEquals(expected, isCompleteTree(root));
        assertEquals(expected, isCompleteTree2(root));
        assertEquals(expected, isCompleteTree3(root));
        assertEquals(expected, isCompleteTree4(root));
        assertEquals(expected, isCompleteTree5(root));
        assertEquals(expected, isCompleteTree6(root));
        assertEquals(expected, isCompleteTree7(root));
        assertEquals(expected, isCompleteTree8(root));
    }

    @Test public void test() {
        test("1,#,2", false);
        test("1", true);
        test("1,2,3,4,5,6", true);
        test("1,2,3,4,5,#,7", false);
        test("1,2,3,4,5,6,#,7", false);
        test("1,2,3,4,5,6,7,8,9,10", true);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
