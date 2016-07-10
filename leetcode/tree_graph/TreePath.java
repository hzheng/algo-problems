import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// https://leetcode.com/problems/binary-tree-paths/
//
// Given a binary tree, return all root-to-leaf paths.
public class TreePath {
    // beats 6.28%(5 ms)
    public List<String> binaryTreePaths(TreeNode root) {
        List<String> res = new ArrayList<>();
        if (root == null) return res;

        binaryTreePaths(root, new LinkedList<>(), res);
        return res;
    }

    private void binaryTreePaths(TreeNode root, List<Integer> path, List<String> res) {
        path.add(root.val);
        if (root.left == null && root.right == null) {
            res.add(pathString(path));
            return;
        }

        if (root.left != null) {
            binaryTreePaths(root.left, path, res);
            path.remove(path.size() - 1);
        }
        if (root.right != null) {
            binaryTreePaths(root.right, path, res);
            path.remove(path.size() - 1);
        }
    }

    // beats 6.28%(5 ms)
    public List<String> binaryTreePaths2(TreeNode root) {
        List<String> res = new ArrayList<>();
        if (root == null) return res;

        binaryTreePaths2(root, new LinkedList<>(), res);
        return res;
    }

    private void binaryTreePaths2(TreeNode root, List<Integer> path, List<String> res) {
        path.add(root.val);
        if (root.left == null && root.right == null) {
            res.add(pathString(path));
            return;
        }

        if (root.left != null) {
            binaryTreePaths2(root.left, new ArrayList<>(path), res);
        }
        if (root.right != null) {
            binaryTreePaths2(root.right, new ArrayList<>(path), res);
        }
    }

    private String pathString(List<Integer> path) {
        StringBuilder sb = new StringBuilder();
        sb.append(path.get(0));
        for (int i = 1; i < path.size(); i++) {
            sb.append("->");
            sb.append(path.get(i));
        }
        return sb.toString();
    }

    // beats 28.45%(3 ms)
    public List<String> binaryTreePaths3(TreeNode root) {
        List<String> res = new ArrayList<>();
        if (root == null) return res;

        binaryTreePaths3(root, String.valueOf(root.val), res);
        return res;
    }

    private void binaryTreePaths3(TreeNode root, String path, List<String> res) {
        if (root == null) return;

        if (root.left == null && root.right == null) {
            res.add(path);
            return;
        }

        if (root.left != null) {
            binaryTreePaths3(root.left, path + "->" + root.left.val, res);
        }
        if (root.right != null) {
            binaryTreePaths3(root.right, path + "->" + root.right.val, res);
        }
    }

    // beats 75.42%(2 ms)
    public List<String> binaryTreePaths4(TreeNode root) {
        List<String> res = new ArrayList<>();
        binaryTreePaths4(root, new StringBuilder(), res);
        return res;
    }

    private void binaryTreePaths4(TreeNode root, StringBuilder sb, List<String> res) {
        if (root == null) return;

        int len = sb.length();
        sb.append(root.val);
        if (root.left == null && root.right == null) {
            res.add(sb.toString());
        } else {
            sb.append("->");
            binaryTreePaths4(root.left, sb, res);
            binaryTreePaths4(root.right, sb, res);
        }
        sb.setLength(len); // backtracking
    }

    // TODO: non-recursion solution by stack(DFS)

    void test(Function<TreeNode, List<String> > paths,
              String s, String ... expected) {
        TreeNode root = TreeNode.of(s);
        assertArrayEquals(expected,
                          paths.apply(root).toArray(new String[0]));
    }

    void test(String s, String ... expected) {
        TreePath t = new TreePath();
        test(t::binaryTreePaths, s, expected);
        test(t::binaryTreePaths2, s, expected);
        test(t::binaryTreePaths3, s, expected);
        test(t::binaryTreePaths4, s, expected);
    }

    @Test
    public void test1() {
        test("1,2,3,#,5", "1->2->5", "1->3");
        test("1,2,3,4", "1->2->4", "1->3");
        test("1,2,3,#,#,4,5,#,6", "1->2", "1->3->4->6", "1->3->5");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("TreePath");
    }
}
