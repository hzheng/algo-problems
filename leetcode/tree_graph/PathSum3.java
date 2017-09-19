import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC437: https://leetcode.com/problems/path-sum-iii/
//
// You are given a binary tree in which each node contains an integer value.
// Find the number of paths that sum to a given value.
// The path does not need to start or end at the root or a leaf, but it must go
// downwards (traveling only from parent nodes to child nodes).
// The tree has no more than 1,000 nodes and the values are in the range
// -1,000,000 to 1,000,000.
public class PathSum3 {
    // beats N/A(32 ms for 126 tests)
    public int pathSum(TreeNode root, int sum) {
        return pathSum(root, sum, 0, true);
    }

    private int pathSum(TreeNode root, int sum, int cur, boolean reset) {
        if (root == null) return 0;

        int count = 0;
        if ((cur += root.val) == sum) {
            count++;
        }
        count += pathSum(root.left, sum, cur, false);
        count += pathSum(root.right, sum, cur, false);
        if (reset) {
            count += pathSum(root.left, sum, 0, true);
            count += pathSum(root.right, sum, 0, true);
        }
        return count;
    }

    // beats N/A(30 ms for 126 tests)
    public int pathSum2(TreeNode root, int sum) {
        if (root == null) return 0;

        return countPath(root, sum) + pathSum(root.left, sum) + pathSum(root.right, sum);
    }

    private int countPath(TreeNode root, int sum) {
        if (root == null) return 0;

        sum -= root.val;
        return (sum == 0 ? 1 : 0) + countPath(root.left, sum) + countPath(root.right, sum);
    }

    void test(String s, int sum, int expected) {
        assertEquals(expected, pathSum(TreeNode.of(s), sum));
        assertEquals(expected, pathSum2(TreeNode.of(s), sum));
    }

    @Test
    public void test() {
        test("1,-2,-3,1,3,-2,#,-1", 1, 3);
        test("0,1,1", 1, 4);
        test("1,2,3,90", 90, 1);
        test("1,-2,-3,1,3,-2,#,-1", 3, 1);
        test("1,#,2,#,3,#,4,#,5", 3, 2);
        test("1,2,3", 4, 1);
        test("10,5,-3,3,2,#,11,3,-2,#,1", 8, 3);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("PathSum3");
    }
}
