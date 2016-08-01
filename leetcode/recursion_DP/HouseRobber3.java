import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// https://leetcode.com/problems/house-robber-iii/
//
// There is only one entrance to this area, called the "root." Besides the root,
// each house has one and only one parent house. After a tour, the smart thief
// realized that "all houses in this place forms a binary tree". It will
// automatically contact the police if two directly-linked houses were broken
// into on the same night.
// Determine the maximum amount of money the thief can rob tonight without
// alerting the police.
public class HouseRobber3 {
    // Recursion
    // beats 14.76%(1062 ms)
    public int rob(TreeNode root) {
        if (root == null) return 0;

        int max1 = rob(root.left) + rob(root.right);
        int max2 = root.val + robUnder(root.left) + robUnder(root.right);
        return Math.max(max1, max2);
    }

    private int robUnder(TreeNode root) {
        return root == null ? 0 : rob(root.left) + rob(root.right);
    }

    // Recursion + Memoization
    // beats 30.27%(3 ms)
    public int rob2(TreeNode root) {
        if (root == null) return 0;

        return rob(root, new HashMap<>());
    }

    private int rob(TreeNode root, Map<TreeNode, Integer> memo) {
        if (root == null) return 0;

        if (memo.containsKey(root)) return memo.get(root);

        int max1 = rob(root.left, memo) + rob(root.right, memo);
        int max2 = root.val + robUnder(root.left, memo)
                   + robUnder(root.right, memo);
        int max = Math.max(max1, max2);
        memo.put(root, max);
        return max;
    }

    private int robUnder(TreeNode root, Map<TreeNode, Integer> memo) {
        return root == null ? 0 : rob(root.left, memo) + rob(root.right, memo);
    }

    // Recursion + Memoization
    // beats 51.19%(1 ms)
    public int rob3(TreeNode root) {
        int[] res = doRob(root);
        return Math.max(res[0], res[1]);
    }

    private int[] doRob(TreeNode root) {
        if (root == null) return new int[2];

        int[] left = doRob(root.left);
        int[] right = doRob(root.right);
        int[] res = new int[2];
        res[0] = Math.max(left[0], left[1]) + Math.max(right[0], right[1]);
        res[1] = root.val + left[0] + right[0];
        return res;
    }

    // Recursion + Memoization
    // beats 51.19%(1 ms)
    public int rob4(TreeNode root) {
        return doRob4(root)[1];
    }

    private int[] doRob4(TreeNode root){
        int[] res = new int[2];
        if (root != null) {
            int[] left = doRob4(root.left);
            int[] right = doRob4(root.right);
            res[0] = left[1] + right[1];
            res[1] = Math.max(res[0], left[0] + right[0] + root.val);
        }
        return res;
    }

    void test(Function<TreeNode, Integer> rob, String name,
              String s, int expected) {
        long t1 = System.nanoTime();
        assertEquals(expected, (int)rob.apply(TreeNode.of(s)));
        System.out.format("%s: %.3f ms\n", name, (System.nanoTime() - t1) * 1e-6);
    }

    void test(String s, int expected) {
        HouseRobber3 h = new HouseRobber3();
        test(h::rob, "rob", s, expected);
        test(h::rob2, "rob2", s, expected);
        test(h::rob3, "rob3", s, expected);
        test(h::rob4, "rob4", s, expected);
    }

    @Test
    public void test1() {
        test("3,2,3,#,3,#,1", 7);
        test("3,4,5,1,3,#,1", 9);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("HouseRobber3");
    }
}
