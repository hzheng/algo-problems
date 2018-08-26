import java.util.*;

import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC894: https://leetcode.com/problems/all-possible-full-binary-trees/
//
// A full binary tree is a binary tree where each node has exactly 0 or 2
// children. Return a list of all possible full binary trees with N nodes.
public class AllPossibleFullBinaryTrees {
    // Recursion
    // beats %(20 ms for 53 tests)
    public List<TreeNode> allPossibleFBT(int N) {
        List<TreeNode> res = new ArrayList<>();
        if (N == 0) return res;

        if (N == 1) {
            res.add(new TreeNode(0));
            return res;
        }
        for (int i = 1; i < N - 1; i++) {
            for (TreeNode l : allPossibleFBT(i)) {
                for (TreeNode r : allPossibleFBT(N - 1 - i)) {
                    TreeNode root = new TreeNode(0);
                    root.left = l;
                    root.right = r;
                    res.add(root);
                }
            }
        }
        return res;
    }

    // Recursion + Memo
    // beats %(4 ms for 53 tests)
    public List<TreeNode> allPossibleFBT2(int N) {
        return allPossibleFBT(N, new HashMap<>());
    }

    public List<TreeNode> allPossibleFBT(int N, Map<Integer, List<TreeNode>> memo) {
        List<TreeNode> res = memo.get(N);
        if (res != null) return res;

        res = new ArrayList<>();
        if (N == 1) {
            res.add(new TreeNode(0));
        } else {
            for (int i = 1; i < N - 1; i++) {
                for (TreeNode l : allPossibleFBT(i, memo)) {
                    for (TreeNode r : allPossibleFBT(N - 1 - i, memo)) {
                        TreeNode root = new TreeNode(0);
                        root.left = l;
                        root.right = r;
                        res.add(root);
                    }
                }
            }
        }
        memo.put(N, res);
        return res;
    }

    // Iteration
    // beats %(6 ms for 53 tests)
    public List<TreeNode> allPossibleFBT3(int N) {
        List<List<TreeNode>> res = new ArrayList<>();
        for (int i = 0; i <= N; i++) {
            res.add(new ArrayList<>());
        }
        res.get(0).add(null);
        res.get(1).add(new TreeNode(0));
        for (int i = 1; i <= N; i += 2) {
            for (int j = 1; j < i - 1; j++) {
                for (TreeNode l : res.get(j)) {
                    for (TreeNode r : res.get(i - 1 - j)) {
                        TreeNode root = new TreeNode(0);
                        root.left = l;
                        root.right = r;
                        res.get(i).add(root);
                    }
                }
            }
        }
        return res.get(N);
    }

    void test(int N, String[] expected) {
        AllPossibleFullBinaryTrees a = new AllPossibleFullBinaryTrees();
        test(N, expected, a::allPossibleFBT);
        test(N, expected, a::allPossibleFBT2);
        test(N, expected, a::allPossibleFBT3);
    }

    void test(int N, String[] expected, 
              Function<Integer, List<TreeNode>> allPossibleFBT) {
        List<TreeNode> res = allPossibleFBT.apply(N);
        assertEquals(expected.length, res.size());
        String[] resArray = new String[expected.length];
        for (int i = 0; i < expected.length; i++) {
            resArray[i] = res.get(i).toString(false);
        }
        Arrays.sort(expected);
        Arrays.sort(resArray);
        assertArrayEquals(expected, resArray);
    }

    @Test
    public void test() {
        test(7, new String[] { "0,0,0,#,#,0,0,#,#,0,0", "0,0,0,#,#,0,0,0,0",
                               "0,0,0,0,0,0,0", "0,0,0,0,0,#,#,#,#,0,0",
                               "0,0,0,0,0,#,#,0,0" });
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
