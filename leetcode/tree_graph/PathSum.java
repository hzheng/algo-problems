import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// Given a binary tree and a sum, determine if the tree has a root-to-leaf path
// such that adding up all the values along the path equals the given sum.
public class PathSum {
    // beats 9.06%
    public boolean hasPathSum(TreeNode root, int sum) {
        if (root == null) return false;

        if (root.left == null && root.right == null) return sum == root.val;

        sum -= root.val;
        return hasPathSum(root.left, sum) || hasPathSum(root.right, sum);
    }

    // http://www.programcreek.com/2013/01/leetcode-path-sum/
    // beats 1.64%
    public boolean hasPathSum2(TreeNode root, int sum) {
        if (root == null) return false;

        Queue<TreeNode> nodes = new LinkedList<>();
        Queue<Integer> values = new LinkedList<>();
        nodes.add(root);
        values.add(root.val);

        while (!nodes.isEmpty()){
            TreeNode cur = nodes.poll();
            int val = values.poll();
            if (cur.left == null && cur.right == null && val == sum) {
                return true;
            }

            if (cur.left != null) {
                nodes.add(cur.left);
                values.add(val + cur.left.val);
            }
            if (cur.right != null){
                nodes.add(cur.right);
                values.add(val + cur.right.val);
            }
        }
        return false;
    }

    // TODO: DFS

    void test(String s, int sum, boolean expected) {
        TreeNode root = TreeNode.of(s);
        assertEquals(expected, hasPathSum(root, sum));
        assertEquals(expected, hasPathSum2(root, sum));
    }

    @Test
    public void test1() {
        test("1,2", 1, false);
        test("5,4,8,11,#,13,4,7,2,#,1", 22, true);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("PathSum");
    }
}
