import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC199: https://leetcode.com/problems/binary-tree-right-side-view/
//
// Given a binary tree, imagine yourself standing on the right side of it,
// return the values of the nodes you can see ordered from top to bottom.
public class TreeRightView {
    // DFS + Stack
    // beats 1.97%(5 ms for 210 tests)
    public List<Integer> rightSideView(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) return res;

        Stack<TreeNode> stack = new Stack<>();
        Set<TreeNode> visited = new HashSet<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            TreeNode top = stack.peek();
            if (res.size() < stack.size()) {
                res.add(top.val);
            }
            if (top.right != null && !visited.contains(top.right)) {
                stack.push(top.right);
            } else if (top.left != null && !visited.contains(top.left)) {
                stack.push(top.left);
            } else {
                visited.add(stack.pop());
            }
        }
        return res;
    }

    // BFS + Queue
    // beats 31.93%(2 ms for 210 tests)
    public List<Integer> rightSideView2(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) return res;

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        for (int curCount = 1, nextCount = 0; !queue.isEmpty();) {
            TreeNode node = queue.poll();
            if (node.left != null) {
                queue.offer(node.left);
                nextCount++;
            }
            if (node.right != null) {
                queue.offer(node.right);
                nextCount++;
            }
            if (--curCount == 0) {
                res.add(node.val);
                curCount = nextCount;
                nextCount = 0;
            }
        }
        return res;
    }

    // BFS + Queue
    // beats 10.84%(3 ms)
    public List<Integer> rightSideView3(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) return res;

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        for (TreeNode lastNode = null; !queue.isEmpty();) {
            TreeNode node = queue.poll();
            if (node == null) { // finished one level
                res.add(lastNode.val);
                lastNode = null;
                continue;
            }
            if (lastNode == null) {
                queue.offer(null);
            }
            lastNode = node;
            if (node.left != null) {
                queue.offer(node.left);
            }
            if (node.right != null) {
                queue.offer(node.right);
            }
        }
        return res;
    }

    // Solution of Choice
    // BFS + Queue
    // beats 28.06%(2 ms for 211 tests)
    public List<Integer> rightSideView4(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) return res;

        Queue<TreeNode> queue = new LinkedList<>();
        for (queue.offer(root); !queue.isEmpty();) {
            TreeNode node = null;
            for (int i = queue.size(); i > 0; i--) {
                node = queue.poll();
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
            res.add(node.val);
        }
        return res;
    }

    // DFS + Recursion
    // beats 9.02%(3 ms for 210 tests)
    public List<Integer> rightSideView5(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        Map<Integer, Integer> values = new HashMap<>();
        dfs(values, root, 1);
        // for (int depth = 1; values.containsKey(depth); depth++) {
        for (int depth = 1, maxDepth = values.size(); depth <= maxDepth; depth++) {
            res.add(values.get(depth));
        }
        return res;
    }

    private void dfs(Map<Integer, Integer> values, TreeNode node, int depth) {
        if (node == null) return;

        values.put(depth, node.val);
        dfs(values, node.left, depth + 1);
        dfs(values, node.right, depth + 1); // overwrite left
    }

    // Solution of Choice
    // DFS + Recursion
    // beats 82.20%(1 ms for 211 tests)
    public List<Integer> rightSideView6(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        rightView(root, 0, res);
        return res;
    }

    private void rightView(TreeNode node, int level, List<Integer> res) {
        if (node == null) return;

        if (level == res.size()) {
            res.add(node.val);
        }
        rightView(node.right, level + 1, res);
        rightView(node.left, level + 1, res);
    }

    void test(Function<TreeNode, List<Integer>> rightSideView, String s, Integer... expected) {
        List<Integer> res = rightSideView.apply(TreeNode.of(s));
        assertArrayEquals(expected, res.toArray(new Integer[0]));
    }

    void test(String s, Integer... expected) {
        TreeRightView t = new TreeRightView();
        test(t::rightSideView, s, expected);
        test(t::rightSideView2, s, expected);
        test(t::rightSideView3, s, expected);
        test(t::rightSideView4, s, expected);
        test(t::rightSideView5, s, expected);
        test(t::rightSideView6, s, expected);
    }

    @Test
    public void test1() {
        test("1,2,3,#,4", 1, 3, 4);
        test("1,2,3,#,#,4,5", 1, 3, 5);
        test("1,2,3,#,#,4,5,6", 1, 3, 5, 6);
        test("1,2,3,#,#,4,5,6,7,8,9,#,10,11,12,#,13,14", 1, 3, 5, 9, 14);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
