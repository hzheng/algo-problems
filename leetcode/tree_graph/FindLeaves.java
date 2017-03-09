import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;
import common.Utils;

// LC366: https://leetcode.com/problems/find-leaves-of-binary-tree
//
// Given a binary tree, collect a tree's nodes as if you were doing this:
// Collect and remove all leaves, repeat until the tree is empty.
public class FindLeaves {
    // Recursion + DFS
    // beats 26.35%(1 ms for 68 tests)
    public List<List<Integer>> findLeaves(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        dfs(root, res);
        return res;
    }

    private int dfs(TreeNode root, List<List<Integer>> res) {
        if (root == null) return 0;

        int depth = 1 + Math.max(dfs(root.left, res), dfs(root.right, res));
        if (res.size() < depth) {
            res.add(new ArrayList<>());
        }
        res.get(depth - 1).add(root.val);
        return depth;
    }

    // TODO: Topological Sort

    void test(Function<TreeNode, List<List<Integer>>> find, String s, int[][] expected) {
        List<List<Integer>> res = find.apply(TreeNode.of(s));
        assertArrayEquals(expected, Utils.toInts(res));
    }

    void test(String s, int[][] expected) {
        FindLeaves f = new FindLeaves();
        test(f::findLeaves, s, expected);
    }

    @Test
    public void test1() {
        test("1,2,3,4,5", new int[][] {{4, 5, 3}, {2}, {1}});
        test("1,2,3,#,4,#,5,#,#,6,7,#,#,8", new int[][] {{4, 6, 8}, {2, 7}, {5}, {3}, {1}});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("FindLeaves");
    }
}
