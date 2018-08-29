import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC863: https://leetcode.com/problems/all-nodes-distance-k-in-binary-tree/
//
// We are given a binary tree, a target node, and an integer value K. Return a 
// list of the values of all nodes that have a distance K from the target node.
public class DistanceKInBinaryTree {
    // Stack/Postorder + Recursion
    // beats 50.53%(4 ms for 57 tests)
    public List<Integer> distanceK(TreeNode root, TreeNode target, int K) {
        Stack<TreeNode> stack = postorder(root, target);
        List<Integer> res = new ArrayList<>();
        distanceK(target, K, res);
        for (TreeNode cur = target; K >= 0 && !stack.isEmpty(); ) {
            TreeNode parent = stack.pop();
            if (parent.left != cur && parent.right != cur) continue;

            if (--K == 0) {
                res.add(parent.val);
            } else if (parent.left == cur) {
                distanceK(parent.right, K - 1, res);
            } else if (parent.right == cur) {
                distanceK(parent.left, K - 1, res);
            }
            cur = parent;
        }
        return res;
    }

    private Stack<TreeNode> postorder(TreeNode root, TreeNode target) {
        Stack<TreeNode> stack = new Stack<>();
        for (TreeNode cur = root, prev = null; ; ) {
            if (cur != null) {
                stack.push(cur);
                cur = cur.left;
            } else {
                TreeNode top = stack.peek();
                if (top.right != null && top.right != prev) {
                    cur = top.right;
                } else {
                    if (top == target) break;
                    prev = stack.pop();
                }
            }
        }
        return stack;
    }
 
    private void distanceK(TreeNode cur, int k, List<Integer> res) {
        if (cur == null) return;

        if (k == 0) {
            res.add(cur.val);
        } else {
            distanceK(cur.left, k - 1, res);
            distanceK(cur.right, k - 1, res);
        }
    }

    // Queue/BFS + Recursion + Hash Table
    // beats 76.22%(2 ms for 57 tests)
    public List<Integer> distanceK2(TreeNode root, TreeNode target, int K) {
        Queue<TreeNode> queue = new LinkedList<>();
        Map<TreeNode, TreeNode> parents = new HashMap<>();
        for (queue.offer(root); ; ) {
            TreeNode cur = queue.poll();
            if (cur == target) break;
            
            if (cur.left != null) {
                parents.put(cur.left, cur);
                queue.offer(cur.left);
            }
            if (cur.right != null) {
                parents.put(cur.right, cur);
                queue.offer(cur.right);
            }
        }
        List<Integer> res = new ArrayList<>();
        distanceK(target, K, res);
        for (TreeNode cur = target, parent; K > 0; cur = parent) {
            parent = parents.get(cur);
            if (parent == null) break;

            if (--K == 0) {
                res.add(parent.val);
            } else if (parent.left == cur) {
                distanceK(parent.right, K - 1, res);
            } else {
                distanceK(parent.left, K - 1, res);
            }
        }
        return res;
    }

    // Queue/BFS + Recursion/DFS + Hash Table
    // beats 50.53%(4 ms for 57 tests)
    public List<Integer> distanceK3(TreeNode root, TreeNode target, int K) {
        Map<TreeNode, TreeNode> parents = new HashMap<>();
        dfs(root, null, parents);
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(target);
        Set<TreeNode> visited = new HashSet<>();
        visited.add(target);
        for (int dist = 0; dist < K; dist++) {
            for (int i = queue.size(); i > 0; i--) {
                TreeNode node = queue.poll();
                if (node.left != null && visited.add(node.left)) {
                    queue.offer(node.left);
                }
                if (node.right != null && visited.add(node.right)) {
                    queue.offer(node.right);
                }
                TreeNode parent = parents.get(node);
                if (parent != null && visited.add(parent)) {
                    queue.offer(parent);
                }
            }
        }
        List<Integer> res = new ArrayList<>();
        for (TreeNode node : queue) {
            res.add(node.val);
        }
        return res;
    }

    private void dfs(TreeNode node, TreeNode parent, Map<TreeNode, TreeNode> parents) {
        if (node == null) return;

        parents.put(node, parent);
        dfs(node.left, node, parents);
        dfs(node.right, node, parents);
    }

    // Recursion/DFS + Hash Table
    // beats 33.20%(5 ms for 57 tests)
    public List<Integer> distanceK4(TreeNode root, TreeNode target, int K) {
        Map<TreeNode, TreeNode> parents = new HashMap<>();
        dfs(root, null, parents);
        List<Integer> res = new ArrayList<>();
        dfs2(target, null, K, parents, res);
        return res;
    }

    private void dfs2(TreeNode node, TreeNode prev, int remain, 
                      Map<TreeNode, TreeNode> parents, List<Integer> res) {
        if (node == null) return;

        if (remain == 0) {
            res.add(node.val);
            return;
        }
        if (node.left != prev) {
            dfs2(node.left, node, remain - 1, parents, res);
        }
        if (node.right != prev) {
            dfs2(node.right, node, remain - 1, parents, res);
        }
        TreeNode parent = parents.get(node);
        if (parent != prev) {
            dfs2(parent, node, remain - 1, parents, res);
        }
    }

    // Recursion/DFS
    // beats 76.22%(2 ms for 57 tests)
    public List<Integer> distanceK5(TreeNode root, TreeNode target, int K) {
        List<Integer> res = new LinkedList<>();
        dfs(root, target, K, res);
        return res;
    }

    // Return distance from node to target if exists, else -1
    private int dfs(TreeNode node, TreeNode target, int K, List<Integer> res) {
        if (node == null) return -1;

        if (node == target) {
            distanceK(node, K, res);
            return 1;
        } 
        int L = dfs(node.left, target, K, res);
        if (L >= 0) {
            if (L == K) {
                res.add(node.val);
            } else {
                distanceK(node.right, K - L - 1, res);
            }
            return L + 1;
        } 
        int R = dfs(node.right, target, K, res);
        if (R < 0) return -1;

        if (R == K) {
            res.add(node.val);
        } else {
            distanceK(node.left, K - R - 1, res);
        }
        return R + 1;
    }

    private TreeNode find(TreeNode root, int val) {
        if (root == null) return null;
        if (root.val == val) return root;
        TreeNode res = find(root.left, val);
        return (res != null) ? res : find(root.right, val);
    }

    void test(String tree, int targetVal, int K, int[] expected) {
        DistanceKInBinaryTree d = new DistanceKInBinaryTree();
        test(tree, targetVal, K, expected, d::distanceK);
        test(tree, targetVal, K, expected, d::distanceK2);
        test(tree, targetVal, K, expected, d::distanceK3);
        test(tree, targetVal, K, expected, d::distanceK4);
        test(tree, targetVal, K, expected, d::distanceK5);
    }

    @FunctionalInterface
    interface Function<A, B, C, D> {
        public D apply(A a, B b, C c);
    }
    
    void test(String tree, int targetVal, int K, int[] expected,
              Function<TreeNode, TreeNode, Integer, List<Integer>> distanceK) {
        TreeNode root = TreeNode.of(tree);
        TreeNode target = find(root, targetVal);
        List<Integer> res = distanceK.apply(root, target, K);
        Collections.sort(res);
        List<Integer> exp = new ArrayList<>();
        for (int e : expected) {
            exp.add(e);
        }
        Collections.sort(exp);
        assertEquals(exp, res);
    }

    @Test
    public void test() {
        test("0,1,#,3,2", 2, 1, new int[]{1});
        test("3,5,1,6,2,0,8,#,#,7,4", 5, 2, new int[]{7, 4, 1});
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
