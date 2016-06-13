import java.util.*;
import java.util.stream.Collectors;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;
import java.util.function.Function;

// https://leetcode.com/problems/sum-root-to-leaf-numbers/
//
// Given a binary tree containing digits from 0-9 only, each root-to-leaf path
// could represent a number. Find the total sum of all root-to-leaf numbers.
public class SumRootToLeaf {
    // beats 73.64%
    public int sumNumbers(TreeNode root) {
        return root == null ? 0 : sumNumbers(root, 0);
    }

    private int sumNumbers(TreeNode root, int sum) {
        sum *= 10;
        sum += root.val;
        if (root.left == null && root.right == null) {
            return sum;
        }

        int total = 0;
        if (root.left != null) {
            total += sumNumbers(root.left, sum);
        }
        if (root.right != null) {
            total += sumNumbers(root.right, sum);
        }
        return total;
    }

    void test(Function<TreeNode, Integer> sum, String s, int expected) {
        TreeNode root = TreeNode.of(s);
        assertEquals(expected, (int)sum.apply(root));
    }

    void test(String s, int expected) {
        SumRootToLeaf sum = new SumRootToLeaf();
        test(sum::sumNumbers, s, expected);
    }

    @Test
    public void test1() {
        test("1,2,3", 25);
        test("1,2,#,3", 123);
        test("1,2,3,5,0,#,1", 376);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("SumRootToLeaf");
    }
}
