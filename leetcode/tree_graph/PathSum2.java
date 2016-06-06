import java.util.*;
import java.util.stream.Collectors;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// Given a binary tree and a sum, find all root-to-leaf paths where each path's
// sum equals the given sum.
public class PathSum2 {
    // beats 40.22%
    public List<List<Integer> > pathSum(TreeNode root, int sum) {
        List<List<Integer> > res = new ArrayList<>();
        pathSum(root, sum, new ArrayList<>(), res);
        return res;
    }

    private void pathSum(TreeNode root, int sum, List<Integer> path,
                         List<List<Integer> > res) {
        if (root == null) return;

        path.add(root.val);
        sum -= root.val;
        if (root.left == null && root.right == null) {
            if (sum == 0) {
                res.add(new ArrayList<>(path));
            }
        } else {
            pathSum(root.left, sum, path, res);
            pathSum(root.right, sum, path, res);
        }
        path.remove(path.size() - 1);
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(Function<TreeNode, Integer, List<List<Integer>>> pathSum,
              String s, int sum, int[] ... expected) {
        TreeNode root = TreeNode.of(s);
        List<List<Integer> > res = pathSum.apply(root, sum);
        Integer[][] resArray = res.stream().map(
            a -> a.toArray(new Integer[0])).toArray(Integer[][]::new);
        assertArrayEquals(expected, resArray);
    }

    void test(String s, int sum, int[] ... expected) {
        PathSum2 p = new PathSum2();
        test(p::pathSum, s, sum, expected);
    }

    @Test
    public void test1() {
        test("1,2", 1, new int[0][0]);
        test("5,4,8,11,#,13,4,7,2,#,#,5,1", 22,
             new int[] {5, 4, 11, 2}, new int[] {5, 8, 4, 5});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("PathSum2");
    }
}
