import common.TreeNode;

import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1382: https://leetcode.com/problems/balance-a-binary-search-tree/
//
// Given a binary search tree, return a balanced binary search tree with the same node values.
// A binary search tree is balanced if and only if the depth of the two subtrees of every node never
// differ by more than 1.
// If there is more than one answer, return any of them.
public class BalanceBST {
    // Stack + Recursion
    // time complexity: O(N), space complexity: O(N)
    // 7 ms(19.44%), 43.1 MB(100%) for 16 tests
    public TreeNode balanceBST(TreeNode root) {
        List<TreeNode> list = new ArrayList<>();
        Stack<TreeNode> stack = new Stack<>();
        for (TreeNode cur = root; cur != null || !stack.empty(); ) {
            if (cur != null) {
                stack.push(cur);
                cur = cur.left;
            } else {
                cur = stack.pop();
                list.add(cur);
                cur = cur.right;
            }
        }
        return createTree(list, 0, list.size());
    }

    private TreeNode createTree(List<TreeNode> list, int start, int end) {
        if (start == end) { return null; }

        int mid = (start + end) / 2;
        TreeNode root = list.get(mid);
        root.left = createTree(list, start, mid);
        root.right = createTree(list, mid + 1, end);
        return root;
    }

    // Recursion
    // time complexity: O(N), space complexity: O(N)
    // 2 ms(99.64%), 41.8 MB(100%) for 16 tests
    public TreeNode balanceBST2(TreeNode root) {
        List<TreeNode> list = new ArrayList<>();
        traverse(root, list);
        return createTree(list, 0, list.size());
    }

    private void traverse(TreeNode root, List<TreeNode> res) {
        if (root != null) {
            traverse(root.left, res);
            res.add(root);
            traverse(root.right, res);
        }
    }

    // DSW algorithm (https://csactor.blogspot.com/2018/08/dsw-day-stout-warren-algorithm-dsw.html)
    // time complexity: O(N), space complexity: O(1)
    // 3 ms(48.82%), 42.4 MB(100%) for 16 tests
    public TreeNode balanceBST3(TreeNode root) {
        TreeNode dummy = new TreeNode(0);
        dummy.right = root;
        int len = makeVine(dummy);
        int height = (int)(Math.log(len + 1) / Math.log(2));
        int balanceCount = (int)Math.pow(2, height) - 1;
        compress(dummy, len - balanceCount);
        for (int extra = balanceCount / 2; extra > 0; extra /= 2) {
            compress(dummy, extra);
        }
        return dummy.right;
    }

    private int makeVine(TreeNode dummy) {
        int len = 0;
        for (TreeNode node = dummy.right; node != null; ) {
            if (node.left != null) {
                TreeNode oldNode = node;
                node = node.left;
                oldNode.left = node.right;
                node.right = oldNode;
                dummy.right = node;
            } else {
                len++;
                dummy = node;
                node = node.right;
            }
        }
        return len;
    }

    private void compress(TreeNode dummy, int extra) {
        for (TreeNode node = dummy.right; extra > 0; extra--, node = node.right) {
            TreeNode oldNode = node;
            node = node.right;
            dummy.right = node;
            oldNode.right = node.left;
            node.left = oldNode;
            dummy = node;
        }
    }

    @Test public void test() {
        //        test("1,#,2,#,3,#,4,#,#", "2,1,3,#,#,#,4");
    }

    private void test(String tree, String expected) {
        TreeNode root = TreeNode.of(tree);
        TreeNode expectedTree = TreeNode.of(expected);
        assertEquals(expectedTree, balanceBST(root));
        assertEquals(expectedTree, balanceBST2(root));
        assertEquals(expectedTree, balanceBST3(root));
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
