import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC889: https://leetcode.com/problems/construct-binary-tree-from-preorder-and-postorder-traversal/
//
// Return any binary tree that matches the given preorder and postorder
// traversals. Values in the traversals pre and post are distinct positive.
// Note:
// 1 <= pre.length == post.length <= 30
// pre[] and post[] are both permutations of 1, 2, ..., pre.length.
// It is guaranteed an answer exists. If there exists multiple answers, you can
// return any of them.
public class TreeFromPreorderPostorder {
    // Divide & Conquer/Recursion + Hash Table
    // beats %(14 ms for 306 tests)
    public TreeNode constructFromPrePost(int[] pre, int[] post) {
        Map<Integer, Integer> indices = new HashMap<>();
        for (int i = 0; i < pre.length; i++) {
            indices.put(pre[i], i);
        }
        return construct(pre, pre.length - 1, post, post.length - 1, indices);
    }

    private TreeNode construct(int[] pre, int preEnd, int[] post, int postEnd,
                               Map<Integer, Integer> indices) {
        int preStart = indices.get(post[postEnd]);
        TreeNode root = new TreeNode(post[postEnd--]);
        if (++preStart > preEnd) return root;

        int index = indices.get(post[postEnd]);
        root.left = construct(pre, preEnd, post, postEnd, indices);
        if (index > preStart) {
            root.right = root.left;
            root.left = construct(pre, index - 1, post,
                                  postEnd - preEnd + index - 1, indices);
        }
        return root;
    }

    // Divide & Conquer/Recursion
    // beats %(28 ms for 306 tests)
    public TreeNode constructFromPrePost2(int[] pre, int[] post) {
        int n = pre.length;
        if (n == 0) return null;

        TreeNode root = new TreeNode(pre[0]);
        if (n == 1) return root;

        int m = 1;
        for (; post[m - 1] != pre[1]; m++) {}
        root.left = constructFromPrePost2(Arrays.copyOfRange(pre, 1,
                                                             m + 1),
                                          Arrays.copyOfRange(post, 0, m));
        root.right = constructFromPrePost2(Arrays.copyOfRange(pre, m + 1,
                                                              n),
                                           Arrays.copyOfRange(post, m, n - 1));
        return root;
    }

    // Stack + Hash Table
    // beats %(24 ms for 306 tests)
    public TreeNode constructFromPrePost3(int[] pre, int[] post) {
        Map<Integer, Integer> indices = new HashMap<>();
        for (int i = post.length - 1; i >= 0; i--) {
            indices.put(post[i], i);
        }
        indices.put(0, post.length); // dummy
        TreeNode root = new TreeNode(0); // dummy
        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);
        for (int p : pre) {
            TreeNode next = new TreeNode(p);
            // parent should be after in postorder
            for (int index = indices.get(next.val);
                 indices.get(stack.peek().val) < index; stack.pop()) {}
            TreeNode cur = stack.peek();
            if (cur.left == null) {
                cur.left = next;
            } else {
                cur.right = next;
                stack.pop();
            }
            stack.push(next);
        }
        return root.left;
    }

    // Stack
    // beats %(28 ms for 306 tests)
    public TreeNode constructFromPrePost4(int[] pre, int[] post) {
        Deque<TreeNode> stack = new ArrayDeque<>();
        TreeNode dummy = new TreeNode(0);
        stack.offer(dummy);
        int i = 0;
        for (int p : pre) {
            TreeNode next = new TreeNode(p);
            for (; stack.peek().val == post[i]; stack.pop(), i++) {}
            if (stack.peek().left == null) {
                stack.peek().left = next;
            } else {
                stack.peek().right = next;
            }
            stack.push(next);
        }
        return dummy.left;
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(Function<int[], int[], TreeNode> build, int[] preorder,
              int[] inorder, String expected) {
        assertEquals(TreeNode.of(expected), build.apply(preorder, inorder));
    }

    void test(int[] pre, int[] post, String expected) {
        TreeFromPreorderPostorder t = new TreeFromPreorderPostorder();
        test(t::constructFromPrePost, pre, post, expected);
        test(t::constructFromPrePost2, pre, post, expected);
        test(t::constructFromPrePost3, pre, post, expected);
        test(t::constructFromPrePost4, pre, post, expected);
    }

    @Test
    public void test1() {
        test(new int[] { 2, 1 }, new int[] { 1, 2 }, "2,1");
        test(new int[] { 1, 2, 4, 5, 3, 6, 7 }, 
             new int[] { 4, 5, 2, 6, 7, 3, 1 }, "1,2,3,4,5,6,7");
        test(new int[] { 1, 2, 3, 4, 5, 6, 7 }, 
             new int[] { 3, 5, 7, 6, 4, 2, 1 }, "1,2,#,3,4,#,#,5,6,#,#,7");
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
