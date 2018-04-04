import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC662: https://leetcode.com/problems/maximum-width-of-binary-tree/
//
// Given a binary tree, write a function to get the maximum width of the given
// tree. The width of a tree is the maximum width among all levels. The binary
// tree has the same structure as a full binary tree, but some nodes are null.
// The width of one level is defined as the length between the end-nodes (the
// leftmost and right most non-null nodes in the level, where the null nodes
// between the end-nodes are also counted into the length calculation.
public class WidthOfBinaryTree {
    // BFS + Deque
    // beats 8.16%(52 ms for 108 tests)
    public int widthOfBinaryTree(TreeNode root) {
        int width = 0;
        Deque<TreeNode> deque = new LinkedList<>(); // ArrayDeque disallows null
        deque.offer(root);
        while (!deque.isEmpty()) {
            while (!deque.isEmpty() && deque.peekFirst() == null) {
                deque.pollFirst();
            }
            while (!deque.isEmpty() && deque.peekLast() == null) {
                deque.pollLast();
            }
            int n = deque.size();
            width = Math.max(width, n);
            for (int i = 0; i < n; i++) {
                TreeNode node = deque.poll();
                deque.offer(node == null ? null : node.left);
                deque.offer(node == null ? null : node.right);
            }
        }
        return width;
    }

    // BFS + Queue
    // beats 10.35%(32 ms for 108 tests)
    public int widthOfBinaryTree2(TreeNode root) {
        int width = 0;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        for (TreeNode dummy = new TreeNode(0); !queue.isEmpty(); ) {
            int n = queue.size();
            width = Math.max(width, n);
            for (int i = 0, placeholders = -1; i < n; i++) {
                TreeNode node = queue.poll();
                if (node.left == null && node.right == null) {
                    if (placeholders >= 0) {
                        placeholders += 2;
                    }
                    continue;
                }
                if ((placeholders < 0) || (node.left == null)) {
                    placeholders++;
                }
                for (; placeholders > 0; placeholders--) {
                    queue.offer(dummy);
                }
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right == null) {
                    placeholders++;
                } else {
                    queue.offer(node.right);
                }
            }
        }
        return width;
    }

    // BFS + Queue
    // beats 81.34%(11 ms for 108 tests)
    public int widthOfBinaryTree3(TreeNode root) {
        Queue<NodeInfo> queue = new LinkedList<>();
        queue.offer(new NodeInfo(root, 0, 0));
        int width = 0;
        for (int left = 0, depth = 0; !queue.isEmpty(); ) {
            NodeInfo x = queue.poll();
            if (x.node == null) continue;

            queue.offer(new NodeInfo(x.node.left, x.depth + 1, x.pos * 2));
            queue.offer(new NodeInfo(x.node.right, x.depth + 1, x.pos * 2 + 1));
            if (depth != x.depth) {
                depth = x.depth;
                left = x.pos;
            }
            width = Math.max(width, x.pos - left + 1);
        }
        return width;
    }

    private static class NodeInfo {
        TreeNode node;
        int depth;
        int pos;
        NodeInfo(TreeNode n, int d, int p) {
            node = n;
            depth = d;
            pos = p;
        }
    }

    // DFS + Recursion + Hash Table
    // beats 81.34%(11 ms for 108 tests)
    public int widthOfBinaryTree4(TreeNode root) {
        return dfs(root, 0, 0, new HashMap<>());
    }

    private int dfs(TreeNode root, int depth, int pos,
                    Map<Integer, Integer> left) {
        if (root == null) return 0;

        left.putIfAbsent(depth, pos);
        int leftW = dfs(root.left, depth + 1, 2 * pos, left);
        int rightW = dfs(root.right, depth + 1, 2 * pos + 1, left);
        return Math.max(pos - left.get(depth) + 1, Math.max(leftW, rightW));
    }

    // DFS + Recursion + Array
    // beats 96.21%(10 ms for 108 tests)
    public int widthOfBinaryTree5(TreeNode root) {
        return dfs(root, 0, 0, new ArrayList<>());
    }

    private int dfs(TreeNode root, int depth, int pos, List<Integer> left) {
        if (root == null) return 0;

        if (depth >= left.size()) {
            left.add(pos);
        }
        int leftW = dfs(root.left, depth + 1, 2 * pos, left);
        int rightW = dfs(root.right, depth + 1, 2 * pos + 1, left);
        return Math.max(pos - left.get(depth) + 1, Math.max(leftW, rightW));
    }

    // BFS + 2 Queue
    // beats 57.43%(12 ms for 108 tests)
    public int widthOfBinaryTree6(TreeNode root) {
        Queue<TreeNode> queue = new ArrayDeque<>();
        Queue<Integer> count = new ArrayDeque<>();
        queue.offer(root);
        count.offer(0);
        int width = 1;
        while (!queue.isEmpty()) {
            int left = 0;
            int pos = 0;
            for (int i = 0, size = queue.size(); i < size; i++) {
                TreeNode node = queue.poll();
                pos = count.poll();
                if (i == 0) {
                    left = pos;
                }
                if (node.left != null) {
                    queue.offer(node.left);
                    count.offer(pos * 2);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                    count.offer(pos * 2 + 1);
                }
            }
            width = Math.max(width, pos - left + 1);
        }
        return width;
    }

    // BFS + Queue + Hash Table
    // beats 27.41%(14 ms for 108 tests)
    public int widthOfBinaryTree7(TreeNode root) {
        Queue<TreeNode> queue = new LinkedList<>();
        Map<TreeNode, Integer> map = new HashMap<>();
        queue.offer(root);
        map.put(root, 1);
        int width = 0;
        while (!queue.isEmpty()) {
            int start = 0;
            int end = 0;
            for (int i = 0, size = queue.size(); i < size; i++) {
                TreeNode node = queue.poll();
                end = map.get(node);
                if (i == 0) {
                    start = end;
                }
                if (node.left != null) {
                    map.put(node.left, map.get(node) * 2);
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    map.put(node.right, map.get(node) * 2 + 1);
                    queue.offer(node.right);
                }
            }
            width = Math.max(end - start + 1, width);
        }
        return width;
    }

    void test(String tree, int expected) {
        assertEquals(expected, widthOfBinaryTree(TreeNode.of(tree)));
        assertEquals(expected, widthOfBinaryTree2(TreeNode.of(tree)));
        assertEquals(expected, widthOfBinaryTree3(TreeNode.of(tree)));
        assertEquals(expected, widthOfBinaryTree4(TreeNode.of(tree)));
        assertEquals(expected, widthOfBinaryTree5(TreeNode.of(tree)));
        assertEquals(expected, widthOfBinaryTree6(TreeNode.of(tree)));
        assertEquals(expected, widthOfBinaryTree7(TreeNode.of(tree)));
    }

    @Test
    public void test() {
        test("1,3,2,5,3,#,9", 4);
        test("1,3,#,5,3", 2);
        test("1,3,2,5", 2);
        test("1,3,2,5,#,#,9,6,#,#,7", 8);
        test("2,1,4,3,#,5", 3);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
