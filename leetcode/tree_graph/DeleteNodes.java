import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

import common.TreeNode;

// LC1110: https://leetcode.com/problems/delete-nodes-and-return-forest/
//
// Given the root of a binary tree, each node in the tree has a distinct value. After deleting all
// nodes with a value in to_delete, we are left with a forest (a disjoint union of trees).
// Return the roots of the trees in the remaining forest.  You may return the result in any order.
//
// Constraints:
// The number of nodes in the given tree is at most 1000.
// Each node has a distinct value between 1 and 1000.
// to_delete.length <= 1000
// to_delete contains distinct values between 1 and 1000.
public class DeleteNodes {
    // DFS + Recursion (Postorder) + Set
    // time complexity: O(N), space complexity: O(H+D)
    // 2 ms(31.61%), 39.3 MB(92.14%) for 111 tests
    public List<TreeNode> delNodes(TreeNode root, int[] to_delete) {
        Set<Integer> toDelete = new HashSet<>();
        for (int d : to_delete) {
            toDelete.add(d);
        }
        Set<TreeNode> res = new HashSet<>();
        del(res, root, root, toDelete);
        return new ArrayList<>(res);
    }

    private TreeNode del(Set<TreeNode> res, TreeNode cur, TreeNode root, Set<Integer> toDelete) {
        if (cur == null) { return null; }

        boolean deleted = toDelete.remove(cur.val);
        cur.left = del(res, cur.left, deleted ? cur.left : root, toDelete);
        cur.right = del(res, cur.right, deleted ? cur.right : root, toDelete);
        if (!deleted && cur.left == null && cur.right == null) {
            res.add(root);
        }
        return deleted ? null : cur;
    }

    // Solution of Choice
    // DFS + Recursion (Postorder) + Set
    // time complexity: O(N), space complexity: O(H+D)
    // 1 ms(99.12%), 39.9 MB(23.05%) for 111 tests
    public List<TreeNode> delNodes2(TreeNode root, int[] to_delete) {
        Set<Integer> toDelete = new HashSet<>();
        for (int d : to_delete) {
            toDelete.add(d);
        }
        List<TreeNode> res = new ArrayList<>();
        del(res, root, true, toDelete);
        return res;
    }

    private TreeNode del(List<TreeNode> res, TreeNode cur, boolean isRoot, Set<Integer> toDelete) {
        if (cur == null) { return null; }

        boolean deleted = toDelete.remove(cur.val);
        if (!deleted && isRoot) {
            res.add(cur);
        }
        cur.left = del(res, cur.left, deleted, toDelete);
        cur.right = del(res, cur.right, deleted, toDelete);
        return deleted ? null : cur;
    }

    // DFS + Recursion (Postorder) + Set
    // time complexity: O(N), space complexity: O(H+D)
    // 1 ms(99.12%), 39.5 MB(72.67%) for 111 tests
    public List<TreeNode> delNodes3(TreeNode root, int[] to_delete) {
        Set<Integer> toDelete = new HashSet<>();
        for (int d : to_delete) {
            toDelete.add(d);
        }
        toDelete.add(0);
        List<TreeNode> res = new ArrayList<>();
        TreeNode dummy = new TreeNode(0);
        dummy.left = root;
        deleteNodes(res, dummy, toDelete);
        return res;
    }

    private TreeNode deleteNodes(List<TreeNode> res, TreeNode cur, Set<Integer> toDelete) {
        if (cur == null) { return null; }

        cur.left = deleteNodes(res, cur.left, toDelete);
        cur.right = deleteNodes(res, cur.right, toDelete);
        if (!toDelete.remove(cur.val)) { return cur; }

        if (cur.left != null) {
            res.add(cur.left);
        }
        if (cur.right != null) {
            res.add(cur.right);
        }
        return null;
    }

    // DFS + Stack (Postorder) + Set
    // time complexity: O(N), space complexity: O(H+D)
    // 3 ms(18.23%), 39.7 MB(44.02%) for 111 tests
    public List<TreeNode> delNodes4(TreeNode root, int[] to_delete) {
        Set<Integer> toDelete = new HashSet<>();
        for (int d : to_delete) {
            toDelete.add(d);
        }
        toDelete.add(0);
        List<TreeNode> res = new LinkedList<>();
        Stack<TreeNode> stack = new Stack<>();
        TreeNode dummy = new TreeNode(0);
        dummy.left = root;
        for (TreeNode cur = dummy, prev = null; cur != null || !stack.isEmpty(); ) {
            if (cur != null) {
                stack.push(cur);
                cur = cur.left;
                continue;
            }
            TreeNode top = stack.peek();
            if (top.right != null && top.right != prev) {
                cur = top.right;
                continue;
            }
            if (toDelete.remove(top.val)) {
                top.val = 0;
            }
            if (top.left == null || top.left.val == 0) {
                top.left = null;
            } else if (top.val == 0) {
                res.add(top.left);
            }
            if (top.right == null || top.right.val == 0) {
                top.right = null;
            } else if (top.val == 0) {
                res.add(top.right);
            }
            prev = stack.pop();
        }
        return res;
    }

    // BFS + Queue + Set
    // time complexity: O(N), space complexity: O(H+D)
    // 3 ms(18.23%), 39.4 MB(83.53%) for 111 tests
    public List<TreeNode> delNodes5(TreeNode root, int[] to_delete) {
        Set<Integer> toDelete = new HashSet<>();
        for (int d : to_delete) {
            toDelete.add(d);
        }
        Set<TreeNode> res = new HashSet<>();
        res.add(root);
        Queue<TreeNode> queue = new LinkedList<>();
        for (queue.offer(root); !queue.isEmpty(); ) {
            TreeNode cur = queue.poll();
            if (toDelete.remove(cur.val)) {
                res.remove(cur);
                if (cur.left != null) {
                    res.add(cur.left);
                }
                if (cur.right != null) {
                    res.add(cur.right);
                }
            }
            if (cur.left != null) {
                queue.offer(cur.left);
                if (toDelete.contains(cur.left.val)) {
                    cur.left = null;
                }
            }
            if (cur.right != null) {
                queue.offer(cur.right);
                if (toDelete.contains(cur.right.val)) {
                    cur.right = null;
                }
            }
        }
        return new ArrayList<>(res);
    }

    // BFS + Queue + Set
    // time complexity: O(N), space complexity: O(H+D)
    // 3 ms(18.23%), 39.4 MB(83.53%) for 111 tests
    public List<TreeNode> delNodes6(TreeNode root, int[] to_delete) {
        Set<Integer> toDelete = new HashSet<>();
        for (int d : to_delete) {
            toDelete.add(d);
        }
        List<TreeNode> res = new ArrayList<>();
        Queue<TreeNode[]> queue = new LinkedList<>();
        for (queue.offer(new TreeNode[]{root, null}); !queue.isEmpty(); ) {
            TreeNode[] top = queue.poll();
            TreeNode cur = top[0];
            TreeNode parent = top[1];
            boolean deleted = toDelete.remove(cur.val);
            if (!deleted && parent == null) {
                res.add(cur);
            }
            parent = deleted ? null : cur;
            if (cur.left != null) {
                queue.offer(new TreeNode[]{cur.left, parent});
                if (toDelete.contains(cur.left.val)) {
                    cur.left = null;
                }
            }
            if (cur.right != null) {
                queue.offer(new TreeNode[]{cur.right, parent});
                if (toDelete.contains(cur.right.val)) {
                    cur.right = null;
                }
            }
        }
        return res;
    }

    @FunctionalInterface interface Function<A, B, C> {
        C apply(A a, B b);
    }

    void test(Function<TreeNode, int[], List<TreeNode>> delNodes, String s, int[] to_delete,
              String[] expected) {
        Set<TreeNode> expectedSet = new HashSet<>();
        for (String e : expected) {
            expectedSet.add(TreeNode.of(e));
        }
        List<TreeNode> res = delNodes.apply(TreeNode.of(s), to_delete);
        assertEquals("result size is wrong.", expectedSet.size(), res.size());
        assertEquals(expectedSet, new HashSet<>(res));
    }

    private void test(String s, int[] to_delete, String[] expected) {
        DeleteNodes d = new DeleteNodes();
        test(d::delNodes, s, to_delete, expected);
        test(d::delNodes2, s, to_delete, expected);
        test(d::delNodes3, s, to_delete, expected);
        test(d::delNodes4, s, to_delete, expected);
        test(d::delNodes5, s, to_delete, expected);
        test(d::delNodes6, s, to_delete, expected);
    }

    @Test public void test() {
        test("1,2,3,4,5,6,7", new int[] {3, 5}, new String[] {"1,2,#,4", "6", "7"});
        test("1,#,2,#,3,#,4", new int[] {3}, new String[] {"1,#,2", "4"});
        test("1,2,4,#,3", new int[] {3}, new String[] {"1,2,4"});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
