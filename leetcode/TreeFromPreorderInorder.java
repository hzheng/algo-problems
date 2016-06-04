import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// Given preorder and inorder traversal of a tree, construct the binary tree.
public class TreeFromPreorderInorder {
    // beats 72.17%
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        int len = preorder.length;
        if (len == 0) return null;

        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < len; i++) {
            map.put(preorder[i], i);
        }

        int rootVal = preorder[0];
        int rootIndex = len - 1;
        for (; rootIndex >= 0; rootIndex--) {
            if (inorder[rootIndex] == rootVal) break;
        }

        TreeNode root = new TreeNode(rootVal);
        root.left = buildTree(map, inorder, 0, rootIndex);
        root.right = buildTree(map, inorder, rootIndex + 1, len);
        return root;
    }

    private TreeNode buildTree(Map<Integer, Integer> map, int[] nums,
                               int start, int end) {
        if (start >= end) return null;

        Stack<TreeNode> stack = new Stack<>();
        stack.push(new TreeNode(nums[start]));
        for (int i = start + 1; i < end; i++) {
            TreeNode cur = stack.peek();
            TreeNode node = new TreeNode(nums[i]);
            if (preorderAfter(nums[i], cur, map)) {
                cur.right = node;
            } else {
                while (!stack.isEmpty()) {
                    cur = stack.pop();
                    if (preorderAfter(nums[i], cur, map)) {
                        stack.push(cur);
                        TreeNode tmp = cur.right;
                        cur.right = node;
                        cur = tmp;
                        break;
                    }
                }
                node.left = cur;
            }
            stack.push(node);
        }
        return stack.elementAt(0);
    }

    private boolean preorderAfter(int n, TreeNode node, Map<Integer, Integer> map) {
        return map.get(n) > map.get(node.val);
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(Function<int[], int[], TreeNode> build, int[] preorder,
              int[] inorder, String expected) {
        assertEquals(expected, build.apply(preorder, inorder).toString());
    }

    void test(int[] preorder, int[] inorder, String expected) {
        TreeFromPreorderInorder t = new TreeFromPreorderInorder();
        test(t::buildTree, preorder, inorder, expected);
    }

    @Test
    public void test1() {
        test(new int[] {1, 2, 4, 3}, new int[] {1, 2, 3, 4}, "{1,#,2,#,4,3}");
        test(new int[] {1, 2, 3}, new int[] {1, 3, 2}, "{1,#,2,3}");
        test(new int[] {3, 9, 20, 15, 7}, new int[] {9, 3, 15, 20, 7},
             "{3,9,20,#,#,15,7}");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("TreeFromPreorderInorder");
    }
}
