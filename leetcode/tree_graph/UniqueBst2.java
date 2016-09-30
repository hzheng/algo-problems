import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC095: https://leetcode.com/problems/unique-binary-search-trees-ii/
//
// Given n, generate all structurally unique BST's that store values 1...n.
public class UniqueBst2 {
    // Solution of Choice
    // Recursion
    // beats 51.93%(4 ms)
    public List<TreeNode> generateTrees(int n) {
        return n == 0 ? Collections.emptyList() : generateTrees(1, n);
    }

    private List<TreeNode> generateTrees(int start, int end) {
        List<TreeNode> res = new ArrayList<>();
        if (start > end) {
            res.add(null);
            return res;
        }
        for (int i = start; i <= end; i++) {
            List<TreeNode> left = generateTrees(start, i - 1);
            List<TreeNode> right = generateTrees(i + 1, end);
            for (TreeNode l : left) {
                for (TreeNode r : right) {
                    TreeNode root = new TreeNode(i);
                    root.left = l;
                    root.right = r;
                    res.add(root);
                }
            }
        }
        return res;
    }

    // beats 84.31%(3 ms)
    // Dynamic Programming
    public List<TreeNode> generateTrees2(int n) {
        @SuppressWarnings("unchecked")
        List<TreeNode>[] result = new List[n + 1];
        result[0] = new ArrayList<>();
        if (n == 0) return result[0];

        result[0].add(null);
        for (int i = 1; i <= n; i++) {
            result[i] = new ArrayList<>();
            for (int j = 0; j < i; j++) {
                for (TreeNode leftNode : result[j]) {
                    for (TreeNode rightNode : result[i - j - 1]) {
                        TreeNode node = new TreeNode(j + 1);
                        node.left = leftNode;
                        node.right = clone(rightNode, j + 1);
                        result[i].add(node);
                    }
                }
            }
        }
        return result[n];
    }

    private TreeNode clone(TreeNode n, int offset) {
        if (n == null) return null;

        TreeNode node = new TreeNode(n.val + offset);
        node.left = clone(n.left, offset);
        node.right = clone(n.right, offset);
        return node;
    }

    void test(Function<Integer, List<TreeNode> > generate, String name,
              int n, String ... expected) {
        long t1 = System.nanoTime();
        List<TreeNode> res = generate.apply(n);
        System.out.format("%s: %.3f ms\n", name, (System.nanoTime() - t1) * 1e-6);
        String[] resStr = res.stream().map(
            t -> t.toString()).toArray(String[]::new);

        if (expected.length == 0) {
            assertEquals(0, resStr.length);
            return;
        }
        Arrays.sort(resStr);
        Arrays.sort(expected);
        assertArrayEquals(expected, resStr);
    }

    void test(int n, String ... expected) {
        UniqueBst2 u = new UniqueBst2();
        test(u::generateTrees, "generateTrees", n, expected);
        test(u::generateTrees2, "generateTrees2", n, expected);
    }

    @Test
    public void test1() {
        test(0);
        test(1, "{1}");
        test(2, "{1,#,2}", "{2,1}");
        test(3, "{1,#,3,2}", "{3,2,#,1}", "{3,1,#,#,2}",
             "{2,1,3}", "{1,#,2,#,3}");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("UniqueBst2");
    }
}
