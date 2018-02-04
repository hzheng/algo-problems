import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC776: https://leetcode.com/problems/split-bst/
//
// Given a Binary Search Tree (BST) with root node root, and a target value V,
// split the tree into two subtrees where one subtree has nodes that are all 
// smaller or equal to the target value, while the other subtree has all nodes 
// that are greater than the target value. Additionally, most of the structure
// of the original tree should remain.  Formally, for any child C with parent P 
// in the original tree, if they are both in the same subtree after the split, 
// then node C should still have the parent P.
public class SplitBST {
    // Recursion
    // beats %(3 ms for 38 tests)
    public TreeNode[] splitBST(TreeNode root, int V) {
        if (root == null) return new TreeNode[2];

        if (root.val <= V) {
            TreeNode[] res = splitBST(root.right, V);
            root.right = res[1];
            res[1] = root;
            return res;
        }
        // root.val > V
        TreeNode[] res = splitBST(root.left, V);
        root.left = res[0];
        res[0] = root;
        return res;
    }

    // Recursion
    // beats %(3 ms for 38 tests)
    public TreeNode[] splitBST2(TreeNode root, int V) {
        TreeNode small = new TreeNode(0);
        TreeNode large = new TreeNode(0);
        split(root, V, small, large);
        return new TreeNode[] {large.left, small.right};
    }

    private void split(TreeNode node, int v, TreeNode small, TreeNode large) {
        if (node == null) return;

        if (node.val <= v) {
            small.right = node;
            TreeNode right = node.right;
            node.right = null;
            split(right, v, node, large);
        } else {
            large.left = node;
            TreeNode left = node.left;
            node.left = null;
            split(left, v, small, node);
        }
    }

    // beats %(3 ms for 38 tests)
    public TreeNode[] splitBST3(TreeNode root, int V) {
        TreeNode rRoot = null;
        TreeNode lRoot = null;
        for (TreeNode cur = root, lEnd = null, rEnd = null; cur != null; ) {
            if (cur.val <= V) {
                if (lRoot == null) {
                    lRoot = lEnd = cur;
                } else {
                    lEnd = lEnd.right = cur;
                }
                if (rEnd != null) {
                    rEnd.left = null;
                }
                cur = cur.right;
            } else {
                if (rRoot == null) {
                    rRoot = rEnd = cur;
                } else {
                    rEnd = rEnd.left = cur;
                }
                if (lEnd != null) {
                    lEnd.right = null;
                }
                cur = cur.left;
            }
        }
        return new TreeNode[] {rRoot, lRoot};
    }

    void test(String root, int V, String ... expected) {
        SplitBST s = new SplitBST();
        test(root, V, s::splitBST, expected);
        test(root, V, s::splitBST2, expected);
        test(root, V, s::splitBST3, expected);
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(String root, int V, Function<TreeNode, Integer, TreeNode[]> split,
              String ... expected) {
        TreeNode[] res = split.apply(TreeNode.of(root), V);
        for (int i = 0; i < 2; i++) {
            if (expected[i] == null) {
                assertNull(res[i]);
            } else {
                assertEquals("{" + expected[i] + "}", res[i].toString());
            }
        }
    }

    @Test
    public void test() {
        test("4,2,6,1,3,5,7", 2, "4,3,6,#,#,5,7", "2,1");
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
