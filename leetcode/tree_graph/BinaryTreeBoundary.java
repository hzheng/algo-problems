import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC545: https://leetcode.com/problems/boundary-of-binary-tree/
//
// Given a binary tree, return the values of its boundary in anti-clockwise direction
// starting from root. Boundary includes left boundary, leaves, and right boundary
// in order without duplicate nodes.
// Left boundary is defined as the path from root to the left-most node. Right boundary
// is defined as the path from root to the right-most node. If the root doesn't have
// left subtree or right subtree, then the root itself is left boundary or right boundary.
// Note this definition only applies to the input binary tree, and not applies to any subtrees.
// The left-most node is defined as a leaf node you could reach when you always firstly
// travel to the left subtree if exists. If not, travel to the right subtree. Repeat until you reach a leaf node.
// The right-most node is also defined by the same way with left and right exchanged.
public class BinaryTreeBoundary {
    // Recursion + Set
    // beats N/A(17 ms for 117 tests)
    public List<Integer> boundaryOfBinaryTree(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) return res;

        Set<TreeNode> visited = new HashSet<>();
        visited.add(root);
        res.add(root.val);
        visitLeft(root.left, visited, res);
        visitLeaves(root, visited, res);
        List<Integer> rightBoundary = new LinkedList<>();
        visitRight(root.right, visited, rightBoundary);
        res.addAll(rightBoundary);
        return res;
    }

    private void visitLeft(TreeNode root, Set<TreeNode> visited, List<Integer> res) {
        if (root == null) return;

        if (visited.add(root)) {
            res.add(root.val);
        }
        if (root.left != null) {
            visitLeft(root.left, visited, res);
        } else if (root.right != null) {
            visitLeft(root.right, visited, res);
        }
    }

    private void visitRight(TreeNode root, Set<TreeNode> visited, List<Integer> res) {
        if (root == null) return;

        if (visited.add(root)) {
            res.add(0, root.val);
        }
        if (root.right != null) {
            visitRight(root.right, visited, res);
        } else if (root.left != null) {
            visitRight(root.left, visited, res);
        }
    }

    private void visitLeaves(TreeNode root, Set<TreeNode> visited, List<Integer> res) {
        if (root == null) return;

        if (root.left == null && root.right == null) {
            if (visited.add(root)) {
                res.add(root.val);
            }
        } else {
            visitLeaves(root.left, visited, res);
            visitLeaves(root.right, visited, res);
        }
    }

    // Recursion
    // beats N/A(13 ms for 117 tests)
    public List<Integer> boundaryOfBinaryTree2(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) return res;

        res.add(root.val);
        visitLeft(root.left, res);
        visitLeaves(root.left, res);
        visitLeaves(root.right, res);
        visitRight(root.right, res);
        return res;
    }

    private void visitLeft(TreeNode root, List<Integer> res) {
        if (root == null || (root.left == null && root.right == null)) return;

        res.add(root.val);
        if (root.left != null) {
            visitLeft(root.left, res);
        } else if (root.right != null) {
            visitLeft(root.right, res);
        }
    }

    private void visitRight(TreeNode root, List<Integer> res) {
        if (root == null || (root.left == null && root.right == null)) return;

        if (root.right != null) {
            visitRight(root.right, res);
        } else if (root.left != null) {
            visitRight(root.left, res);
        }
        res.add(root.val); // put to the end to reverse
    }

    private void visitLeaves(TreeNode root, List<Integer> res) {
        if (root == null) return;

        if (root.left == null && root.right == null) {
            res.add(root.val);
        } else {
            visitLeaves(root.left, res);
            visitLeaves(root.right, res);
        }
    }

    // Recursion + Stack
    // beats N/A(17 ms for 117 tests)
    public List<Integer> boundaryOfBinaryTree3(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) return res;

        if (!isLeaf(root)) {
            res.add(root.val);
        }
        for (TreeNode n = root.left; n != null; n = (n.left != null) ? n.left : n.right) {
            if (!isLeaf(n)) {
                res.add(n.val);
            }
        }
        addLeaves(res, root);
        ArrayDeque<Integer> stack = new ArrayDeque<>();
        for (TreeNode n = root.right; n != null; n = (n.right != null) ? n.right : n.left) {
            if (!isLeaf(n)) {
                stack.push(n.val);
            }
        }
        res.addAll(stack);
        return res;
    }

    private boolean isLeaf(TreeNode node) {
        return node.left == null && node.right == null;
    }

    private void addLeaves(List<Integer> res, TreeNode root) {
        if (isLeaf(root)) {
            res.add(root.val);
        } else {
            if (root.left != null) {
                addLeaves(res, root.left);
            }
            if (root.right != null) {
                addLeaves(res, root.right);
            }
        }
    }

    // Recursion
    // beats N/A(14 ms for 117 tests)
    public List<Integer> boundaryOfBinaryTree4(TreeNode root) {
        List<Integer> left = new ArrayList<>();
        List<Integer> right = new LinkedList<>();
        preorder(root, left, right, 0);
        left.addAll(right);
        return left;
    }

    private void preorder(TreeNode cur, List<Integer> left, List<Integer> right, int flag) {
        // 0 - root, 1 - left boundary node, 2 - right boundary node, 3 - middle node.
        if (cur == null) return;
        if (flag == 2) {
            right.add(0, cur.val);
        } else if (flag <= 1 || cur.left == null && cur.right == null) {
            left.add(cur.val);
        }
        preorder(cur.left, left, right, flag <= 1 ? 1 : (flag == 2 && cur.right == null) ? 2 : 3);
        preorder(cur.right, left, right, flag % 2 == 0 ? 2 : (flag == 1 && cur.left == null) ? 1 : 3);
    }

    void test(String s, Integer[] expected) {
        List<Integer> expectedList = Arrays.asList(expected);
        assertEquals(expectedList, boundaryOfBinaryTree(TreeNode.of(s)));
        assertEquals(expectedList, boundaryOfBinaryTree2(TreeNode.of(s)));
        assertEquals(expectedList, boundaryOfBinaryTree3(TreeNode.of(s)));
        assertEquals(expectedList, boundaryOfBinaryTree4(TreeNode.of(s)));
    }

    @Test
    public void test() {
        test("1", new Integer[] {1});
        test("1,#,2,3,4", new Integer[] {1, 3, 4, 2});
        test("1,2,3,4,5,6,#,#,#,7,8,9,10", new Integer[] {1,2,4,7,8,9,10,6,3});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("BinaryTreeBoundary");
    }
}
