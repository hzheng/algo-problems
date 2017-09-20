import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;
import java.util.function.Function;

// LC129: https://leetcode.com/problems/sum-root-to-leaf-numbers/
//
// Given a binary tree containing digits from 0-9 only, each root-to-leaf path
// could represent a number. Find the total sum of all root-to-leaf numbers.
public class SumRootToLeaf {
    // Divide & Conquer/Recursion
    // beats 27.08%(1 ms)
    public int sumNumbers(TreeNode root) {
        return sumNumbers(root, 0);
    }

    private int sumNumbers(TreeNode root, int sum) {
        if (root == null) return 0;

        sum = sum * 10 + root.val;
        if (root.left == null && root.right == null) return sum;

        return sumNumbers(root.left, sum) + sumNumbers(root.right, sum);
    }

    // 2 Stacks(postorder)
    // beats 9.05%(3 ms)
    public int sumNumbers2(TreeNode root) {
        Stack<TreeNode> nodeStack = new Stack<>();
        Stack<Integer> valStack = new Stack<>();
        int sum = 0;
        valStack.push(0);
        for (TreeNode cur = root, prev = null; cur != null || !nodeStack.isEmpty(); ) {
            if (cur != null) {
                nodeStack.push(cur);
                valStack.push(valStack.peek() * 10 + cur.val);
                cur = cur.left;
            } else {
                cur = nodeStack.peek();
                if (cur.right != null && cur.right != prev) {
                    cur = cur.right;
                } else {
                    nodeStack.pop();
                    int val = valStack.pop();
                    if (cur.right == null && cur.left == null) {
                        sum += val;
                    }
                    prev = cur;
                    cur = null;
                }
            }
        }
        return sum;
    }

    // Solution of Choice
    // 1 Stack(postorder)
    // beats 15.93%(2 ms)
    public int sumNumbers3(TreeNode root) {
        Stack<TreeNode> nodeStack = new Stack<>();
        int total = 0;
        int sum = 0;
        for (TreeNode cur = root, prev = null; cur != null || !nodeStack.isEmpty(); ) {
            if (cur != null) {
                nodeStack.push(cur);
                sum = sum * 10 + cur.val;
                cur = cur.left;
            } else {
                cur = nodeStack.peek();
                if (cur.right != null && cur.right != prev) {
                    cur = cur.right;
                } else {
                    nodeStack.pop();
                    if (cur.right == null && cur.left == null) {
                        total += sum;
                    }
                    sum /= 10;
                    prev = cur;
                    cur = null;
                }
            }
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
        test(sum::sumNumbers2, s, expected);
        test(sum::sumNumbers3, s, expected);
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
