import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC563: https://leetcode.com/problems/binary-tree-tilt/
//
// Given a binary tree, return the tilt of the whole tree.
// The tilt of a tree node is defined as the absolute difference between the sum
// of all left subtree node values and the sum of all right subtree node values.
// Null node has tilt 0.
// The tilt of the whole tree is defined as the sum of all nodes' tilt.
public class TreeTilt {
    // beats 0%(28 ms for 75 tests)
    public int findTilt(TreeNode root) {
        if (root == null) return 0;

        Map<TreeNode, Integer> map = new HashMap<>();
        sum(root, map);
        int res = 0;
        for (TreeNode node : map.keySet()) {
            res += Math.abs(map.getOrDefault(node.left, 0) - map.getOrDefault(node.right, 0));
        }
        return res;
    }

    private int sum(TreeNode root, Map<TreeNode, Integer> map) {
        if (root == null) return 0;

        int leftSum = sum(root.left, map);
        int rightSum = sum(root.right, map);
        int sum = leftSum + rightSum + root.val;
        map.put(root, sum);
        return sum;
    }

    // beats 25.00%(8 ms for 75 tests)
    public int findTilt2(TreeNode root) {
        int[] tilt = new int[1];
        postOrder(root, tilt);
        return tilt[0];
    }

    private int postOrder(TreeNode root, int[] tilt) {
        if (root == null) return 0;

        int left = postOrder(root.left, tilt);
        int right = postOrder(root.right, tilt);
        tilt[0] += Math.abs(left - right);
        return left + right + root.val;
    }

    void test(String s, int expected) {
        assertEquals(expected, findTilt(TreeNode.of(s)));
        assertEquals(expected, findTilt2(TreeNode.of(s)));
    }

    @Test
    public void test() {
        test("1,2,3", 1);
        test("1,2,3,4,#,5", 11);
        test("1,2,3,4,#,5,6", 13);
    }

    public static void main(String[] args) {
        String clazz = new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
