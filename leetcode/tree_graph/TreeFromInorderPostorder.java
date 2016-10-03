import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC106: https://leetcode.com/problems/construct-binary-tree-from-inorder-and-postorder-traversal/
//
// Given inorder and postorder traversal of a tree, construct the binary tree.
// You may assume that duplicates do not exist in the tree.
public class TreeFromInorderPostorder {
    // Solution of Choice
    // Divide & Conquer/Recursion + Hashtable
    // beats 68.48%(6 ms)
    public TreeNode buildTree(int[] inorder, int[] postorder) {
        int len = inorder.length;
        if (len == 0) return null;

        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < inorder.length; i++) {
            map.put(inorder[i], i);
        }
        return buildTree(map, postorder, new int[] {len}, inorder, 0, len);
    }

    private TreeNode buildTree(Map<Integer, Integer> map, int[] postorder,
                               int[] last, int[] inorder, int start, int end) {
        if (start >= end) return null;

        TreeNode root = new TreeNode(postorder[--last[0]]);
        int rootIndex = map.get(root.val);
        root.right = buildTree(map, postorder, last, inorder, rootIndex + 1, end);
        root.left = buildTree(map, postorder, last, inorder, start, rootIndex);
        return root;
    }

    // Divide & Conquer/Recursion + Hashtable
    // beats 77.83%(5 ms)
    public TreeNode buildTree2(int[] inorder, int[] postorder) {
        int len = inorder.length;
        if (len == 0) return null;

        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < inorder.length; i++) {
            map.put(inorder[i], i);
        }
        return buildTree2(map, inorder, 0, len - 1, postorder, 0, len - 1);
    }

    private TreeNode buildTree2(Map<Integer, Integer> map,
                                int[] inorder, int inStart, int inEnd,
                                int[] postorder, int postStart, int postEnd) {
        if (inStart > inEnd) return null;

        TreeNode root = new TreeNode(postorder[postEnd]);
        int index = map.get(postorder[postEnd]);
        root.left = buildTree2(map, inorder, inStart, index - 1,
                               postorder, postStart, postStart + index - inStart - 1);
        root.right = buildTree2(map, inorder, index + 1, inEnd,
                                postorder, postStart + index - inStart, postEnd - 1);
        return root;
    }

    // Solution of Choice
    // Stack
    // beats 77.83%(5 ms)
    public TreeNode buildTree3(int[] inorder, int[] postorder) {
        TreeNode dummy = new TreeNode(Integer.MIN_VALUE);
        Stack<TreeNode> stack = new Stack<>();
        stack.push(dummy);
        TreeNode node = null;
        for (int i = postorder.length - 1, j = i; j >= 0; ) {
            if (stack.peek().val == inorder[j]) {
                node = stack.pop();
                j--;
            } else {
                TreeNode child = new TreeNode(postorder[i--]);
                if (node == null) {
                    stack.peek().right = child;
                } else {
                    node.left = child;
                    node = null;
                }
                stack.push(child);
            }
        }
        return dummy.right;
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(Function<int[], int[], TreeNode> build, int[] inorder,
              int[] postorder, String expected) {
        assertEquals(expected, build.apply(inorder, postorder).toString());
    }

    void test(int[] inorder, int[] postorder, String expected) {
        TreeFromInorderPostorder t = new TreeFromInorderPostorder();
        test(t::buildTree, inorder, postorder, expected);
        test(t::buildTree2, inorder, postorder, expected);
        test(t::buildTree3, inorder, postorder, expected);
    }

    @Test
    public void test1() {
        test(new int[] {1, 2, 3}, new int[] {2, 3, 1}, "{1,#,3,2}");
        test(new int[] {1, 2, 3, 4}, new int[] {1, 3, 2, 4}, "{4,2,#,1,3}");
        test(new int[] {1, 2, 3, 4}, new int[] {3, 2, 1, 4}, "{4,1,#,#,2,#,3}");
        test(new int[] {1, 2, 3, 4}, new int[] {2, 1, 4, 3}, "{3,1,4,#,2}");
        test(new int[] {1, 2, 3, 4}, new int[] {3, 4, 2, 1}, "{1,#,2,#,4,3}");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("TreeFromInorderPostorder");
    }
}
