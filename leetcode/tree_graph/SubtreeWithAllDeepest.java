import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC866: https://leetcode.com/problems/smallest-subtree-with-all-the-deepest-nodes/
//
// Given a binary tree rooted at root, the depth of each node is the shortest 
// distance to the root. A node is deepest if it has the largest depth possible
// among any node in the entire tree. Return the node with the largest depth 
// such that it contains all the deepest nodes in it's subtree.
public class SubtreeWithAllDeepest {
    // BFS + Queue + Hash Table + Set
    // beats 10.64%(6 ms for 57 tests)
    public TreeNode subtreeWithAllDeepest(TreeNode root) {
        Queue<TreeNode> queue = new LinkedList<>();
        Set<TreeNode> level = new HashSet<>();
        Map<TreeNode, TreeNode> parents = new HashMap<>();
        for (queue.offer(root); !queue.isEmpty(); ) {
            level = new HashSet<>(queue);
            for (int i = queue.size(); i > 0; i--) {
                TreeNode node = queue.poll();
                if (node.left != null) {
                    queue.offer(node.left);
                    parents.put(node.left, node);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                    parents.put(node.right, node);
                }
            }
        }
        while (level.size() > 1) {
            Set<TreeNode> upperLevel = new HashSet<>();
            for (TreeNode node : level) {
                upperLevel.add(parents.get(node));
            }
            level = upperLevel;
        }
        return level.iterator().next();
    }

    // DFS + Recursion + Hash Table
    // beats 22.13%(5 ms for 57 tests)
    public TreeNode subtreeWithAllDeepest2(TreeNode root) {
        Map<TreeNode, Integer> depth = new HashMap<>();
        depth.put(null, -1);
        getDepth(root, null, depth);
        int maxDepth = -1;
        for (int d : depth.values()) {
            maxDepth = Math.max(maxDepth, d);
        }
        return getSubtree(root, depth, maxDepth);
    }

    private void getDepth(TreeNode node, TreeNode parent,
                          Map<TreeNode, Integer> depth) {
        if (node == null) return;

        depth.put(node, depth.get(parent) + 1);
        getDepth(node.left, node, depth);
        getDepth(node.right, node, depth);
    }

    private TreeNode getSubtree(TreeNode node, Map<TreeNode, Integer> depth,
                                int maxDepth) {
        if (node == null || depth.get(node) == maxDepth) return node;

        TreeNode leftTree = getSubtree(node.left, depth, maxDepth);
        TreeNode rightTree = getSubtree(node.right, depth, maxDepth);
        if (leftTree != null && rightTree != null) return node;
        return (leftTree != null) ? leftTree : rightTree;
    }

    // DFS + Recursion
    // beats 45.69%(4 ms for 57 tests)
    public TreeNode subtreeWithAllDeepest3(TreeNode root) {
        return dfs(root).node;
    }

    private DepthNode dfs(TreeNode node) {
        if (node == null) return new DepthNode(null, 0);

        DepthNode L = dfs(node.left);
        DepthNode R = dfs(node.right);
        if (L.depth > R.depth) return new DepthNode(L.node, L.depth + 1);
        if (L.depth < R.depth) return new DepthNode(R.node, R.depth + 1);
        return new DepthNode(node, L.depth + 1);
    }

    private static class DepthNode {
        TreeNode node;
        int depth;
        DepthNode(TreeNode node, int depth) {
            this.node = node;
            this.depth = depth;
        }
    }

    void test(String root, String expected) {
        assertEquals(TreeNode.of(expected), 
                     subtreeWithAllDeepest(TreeNode.of(root)));
        assertEquals(TreeNode.of(expected), 
                     subtreeWithAllDeepest2(TreeNode.of(root)));
        assertEquals(TreeNode.of(expected), 
                     subtreeWithAllDeepest3(TreeNode.of(root)));
    }

    @Test
    public void test() {
        test("3,5,1,6,2,0,8,#,#,7,4", "2,7,4");
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
