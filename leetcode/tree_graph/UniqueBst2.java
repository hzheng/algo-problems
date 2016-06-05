import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// Given n, generate all structurally unique BST's that store values 1...n.
public class UniqueBst2 {
    // beats 39.17%
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

    void test(Function<Integer, List<TreeNode>> generate, String name,
              int n, String ... expected) {
        long t1 = System.nanoTime();
        List<TreeNode> res = generate.apply(n);
        System.out.format("%s: %.3f ms\n", name, (System.nanoTime() - t1) * 1e-6);
        String[] resStr = res.stream().map(
            t -> t.toString()).toArray(String[]::new);
        Arrays.sort(resStr);
        Arrays.sort(expected);
        assertArrayEquals(expected, resStr);
    }

    void test(int n, String ... expected) {
        UniqueBst2 u = new UniqueBst2();
        test(u::generateTrees, "generateTrees", n, expected);
    }

    @Test
    public void test1() {
        test(1, "{1}");
        test(2, "{1,#,2}", "{2,1,#}");
        test(3, "{1,#,3,2,#}", "{3,2,#,1,#}", "{3,1,#,#,2}",
                "{2,1,3}", "{1,#,2,#,3}");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("UniqueBst2");
    }
}
