import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.function.Function;

import common.TreeNode;

// LC1028: https://leetcode.com/problems/recover-a-tree-from-preorder-traversal/
//
// We run a preorder depth-first search (DFS) on the root of a binary tree. At each node in this
// traversal, we output D dashes (where D is the depth of this node), then we output the value of
// this node.  If the depth of a node is D, the depth of its immediate child is D + 1.  The depth of
// the root node is 0. If a node has only one child, that child is guaranteed to be the left child.
// Given the output S of this traversal, recover the tree and return its root.
//
// Constraints:
// The number of nodes in the original tree is in the range [1, 1000].
// 1 <= Node.val <= 10^9
public class RecoverFromPreorder {
    // Stack
    // time complexity: O(N), space complexity: O(N)
    // 4 ms(68.73%), 39.3 MB(92.51%) for 104 tests
    public TreeNode recoverFromPreorder(String S) {
        Stack<Object[]> stack = new Stack<>();
        char[] s = S.toCharArray();
        TreeNode res = null;
        for (int n = s.length, i = 0, level; i < n; ) {
            for (level = 0; s[i] == '-'; i++, level++) {}
            int j = i;
            for (; i < n && s[i] != '-'; i++) {}
            TreeNode cur = new TreeNode(Integer.parseInt(S.substring(j, i)));
            if (level == 0) {
                res = cur;
            } else {
                for (; ; stack.pop()) {
                    Object[] top = stack.peek();
                    if (level <= (int)top[1]) { continue; }

                    TreeNode node = (TreeNode)top[0];
                    if (node.left == null) {
                        node.left = cur;
                    } else {
                        node.right = cur;
                    }
                    break;
                }
            }
            stack.push(new Object[] {cur, level});
        }
        return res;
    }

    // Solution of Choice
    // time complexity: O(N), space complexity: O(N)
    // 3 ms(84.24%), 39.7 MB(44.96%) for 104 tests
    public TreeNode recoverFromPreorder2(String S) {
        Deque<TreeNode> deque = new LinkedList<>();
        for (int i = 0, n = S.length(), level, val; i < n; ) {
            for (level = 0; S.charAt(i) == '-'; level++, i++) {}
            for (val = 0; i < n && S.charAt(i) != '-'; i++) {
                val = val * 10 + (S.charAt(i) - '0');
            }
            TreeNode node = new TreeNode(val);
            for (; deque.size() > level; deque.pop()) {}
            TreeNode parent = deque.peek();
            if (parent != null) {
                if (parent.left == null) {
                    parent.left = node;
                } else {
                    parent.right = node;
                }
            }
            deque.push(node);
        }
        return deque.peekLast();
    }

    // Solution of Choice
    // DFS + Recursion
    // time complexity: O(N), space complexity: O(N)
    // 1 ms(100.00%), 39.4 MB(82.87%) for 104 tests
    public TreeNode recoverFromPreorder3(String S) {
        return dfs(S.toCharArray(), 0, new int[1]);
    }

    private TreeNode dfs(char[] s, int depth, int[] index) {
        int level = 0;
        int nextIndex = index[0];
        for (;  nextIndex < s.length && s[nextIndex] == '-'; level++, nextIndex++) {}
        if (level != depth) { return null; }

        int val = 0;
        for (; nextIndex < s.length && s[nextIndex] != '-'; nextIndex++) {
            val = val * 10 + (s[nextIndex] - '0');
        }
        index[0] = nextIndex;
        TreeNode root = new TreeNode(val);
        root.left = dfs(s, depth + 1, index);
        root.right = dfs(s, depth + 1, index);
        return root;
    }

    void test(Function<String, TreeNode> recoverFromPreorder, String S, String expected) {
        assertEquals(TreeNode.of(expected), recoverFromPreorder.apply(S));
    }

    private void test(String S, String expected) {
        RecoverFromPreorder r = new RecoverFromPreorder();
        test(r::recoverFromPreorder, S, expected);
        test(r::recoverFromPreorder2, S, expected);
        test(r::recoverFromPreorder3, S, expected);
    }

    @Test public void test() {
        test("1-2--3--4-5--6--7", "1,2,5,3,4,6,7");
        test("1-2--3---4-5--6---7", "1,2,5,3,#,6,#,4,#,7");
        test("1-401--349---90--88", "1,401,#,349,88,90");
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
