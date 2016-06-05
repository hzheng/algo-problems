import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// Given inorder and postorder traversal of a tree, construct the binary tree.
// You may assume that duplicates do not exist in the tree.
public class TreeFromInorderPostorder {
    // beats 68.48%
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

    // beats 77.06%
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

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(Function<int[], int[], TreeNode> build, int[] preorder,
              int[] inorder, String expected) {
        assertEquals(expected, build.apply(preorder, inorder).toString());
    }

    void test(int[] inorder, int[] postorder, String expected) {
        TreeFromInorderPostorder t = new TreeFromInorderPostorder();
        test(t::buildTree, inorder, postorder, expected);
        test(t::buildTree2, inorder, postorder, expected);
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
