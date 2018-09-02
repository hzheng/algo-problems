import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC897: https://leetcode.com/problems/increasing-order-search-tree/
//
// Given a tree, rearrange the tree in in-order so that the leftmost node in the
// tree is now the root of the tree, and every node has no left child and only 
// 1 right child.
public class IncreasingBST {
    // Recursion + List
    // flawed: should modify the original tree
    // beats %(48 ms for 2156 tests)
    public TreeNode increasingBST(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        inorder(root, res);
        TreeNode dummy = new TreeNode(0);
        TreeNode cur = dummy;
        for (int x : res) {
            cur = cur.right = new TreeNode(x);
        }
        return dummy.right;
    }

    private void inorder(TreeNode root, List<Integer> res) {
        if (root == null) return;
        
        inorder(root.left, res);
        res.add(root.val);
        inorder(root.right, res);
    }

    // Recursion
    // flawed: should modify the original tree
    // beats %(32 ms for 2156 tests)
    public TreeNode increasingBST2(TreeNode root) {
        TreeNode dummy = new TreeNode(0);
        inorder(root, dummy);
        return dummy.right;
    }

    private TreeNode inorder(TreeNode root, TreeNode cur) {
        if (root == null) return cur;
        
        cur = inorder(root.left, cur);
        cur.right = new TreeNode(root.val);
        return inorder(root.right, cur.right);
    }

    // Recursion
    // beats %(32 ms for 2156 tests)
    public TreeNode increasingBST3(TreeNode root) {
        TreeNode dummy = new TreeNode(0);
        inorder(root, new TreeNode[]{dummy});
        return dummy.right;
    }

    private void inorder(TreeNode root, TreeNode[] cur) {
        if (root == null) return;
        
        inorder(root.left, cur);
        root.left = null;
        cur[0] = cur[0].right = root;
        inorder(root.right, cur);
    }

    // Solution of Choice
    // Recursion
    // beats %(31 ms for 2156 tests)
    public TreeNode increasingBST4(TreeNode root) {
        return increasing(root, null);
    }

    private TreeNode increasing(TreeNode root, TreeNode tail) {
        if (root == null) return tail;

        TreeNode res = increasing(root.left, root);
        root.left = null;
        root.right = increasing(root.right, tail);
        return res;
    }

    // Solution of Choice
    // Stack
    // beats %(34 ms for 2156 tests)
    public TreeNode increasingBST5(TreeNode root) {
        TreeNode res = new TreeNode(0);
        ArrayDeque<TreeNode> stack = new ArrayDeque<>();
        for (TreeNode in = root, out = res; !stack.isEmpty() || in != null; ) {
            if (in != null) {
                stack.push(in);
                in = in.left;
            } else {
                in = stack.pop();
                in.left = null;
                out = out.right = in;
                in = in.right;
            }
        }
        return res.right;
    }

    void test(String root, String expected) {
        assertEquals(TreeNode.of(expected), increasingBST(TreeNode.of(root)));
        assertEquals(TreeNode.of(expected), increasingBST2(TreeNode.of(root)));
        assertEquals(TreeNode.of(expected), increasingBST3(TreeNode.of(root)));
        assertEquals(TreeNode.of(expected), increasingBST4(TreeNode.of(root)));
        assertEquals(TreeNode.of(expected), increasingBST5(TreeNode.of(root)));
    }

    @Test
    public void test() {
        test("5,3,6,2,4,#,8,1,#,#,#,7,9", "1,#,2,#,3,#,4,#,5,#,6,#,7,#,8,#,9");
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
