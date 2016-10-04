import java.util.*;
import java.util.stream.Collectors;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC113: https://leetcode.com/problems/path-sum-ii/
//
// Given a binary tree and a sum, find all root-to-leaf paths where each path's
// sum equals the given sum.
public class PathSum2 {
    // Solution of Choice
    // DFS/Backtracking
    // beats 45.83%(3 ms)
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
        if (root.left != null || root.right != null) {
            pathSum(root.left, sum, path, res);
            pathSum(root.right, sum, path, res);
        } else if (sum == 0) {
            res.add(new ArrayList<>(path));
        }
        path.remove(path.size() - 1);
    }

    // Solution of Choice
    // Stack(Postorder traversal)
    // beats 18.14%(6 ms)
    public List<List<Integer> > pathSum2(TreeNode root, int sum) {
        List<List<Integer> > res = new ArrayList<>();
        List<Integer> path = new ArrayList<>();
        Stack<TreeNode> stack = new Stack<>();
        int curSum = 0;
        for (TreeNode cur = root, prev = null; cur != null || !stack.isEmpty(); ) {
            if (cur != null) {
                stack.push(cur);
                path.add(cur.val);
                curSum += cur.val;
                cur = cur.left;
                continue;
            }

            cur = stack.peek();
            if (cur.right != null && prev != cur.right) {
                cur = cur.right;
                continue;
            }
            if (cur.left == null && cur.right == null && curSum == sum) {
                res.add(new ArrayList<>(path));
            }
            curSum -= cur.val;
            path.remove(path.size() - 1);
            prev = cur;
            cur = null;
            stack.pop();
        }
        return res;
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
        test(p::pathSum2, s, sum, expected);
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
