import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// https://leetcode.com/problems/binary-tree-right-side-view/
//
// Given a binary tree, imagine yourself standing on the right side of it,
// return the values of the nodes you can see ordered from top to bottom.
public class TreeRightView {
    // beats 1.48%(6 ms)
    public List<Integer> rightSideView(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) return res;

        Stack<TreeNode> stack = new Stack<>();
        Set<TreeNode> visited = new HashSet<>();
        stack.push(root);
        res.add(root.val);

        while (!stack.isEmpty()) {
            TreeNode top = stack.peek();
            if (res.size() < stack.size()) {
                res.add(top.val);
            }

            if (top.right != null && !visited.contains(top.right)) {
                stack.push(top.right);
                continue;
            }

            if (top.left != null && !visited.contains(top.left)) {
                stack.push(top.left);
                continue;
            }

            stack.pop();
            visited.add(top);
        }
        return res;
    }

    // beats 10.84%(3 ms)
    public List<Integer> rightSideView2(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) return res;

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        int curCount = 1;
        int nextCount = 0;
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            if (node.left != null) {
                queue.offer(node.left);
                nextCount++;
            }
            if (node.right != null) {
                queue.offer(node.right);
                nextCount++;
            }
            if (--curCount == 0) {
                res.add(node.val);
                curCount = nextCount;
                nextCount = 0;
            }
        }
        return res;
    }

    void test(Function<TreeNode, List<Integer> > rightSideView,
              String s, Integer ... expected) {
        List<Integer> res = rightSideView.apply(TreeNode.of(s));
        assertArrayEquals(expected, res.toArray(new Integer[0]));
    }

    void test(String s, Integer ... expected) {
        TreeRightView t = new TreeRightView();
        test(t::rightSideView, s, expected);
        test(t::rightSideView2, s, expected);
    }

    @Test
    public void test1() {
        test("1,2,3,#,4", 1, 3, 4);
        test("1,2,3,#,#,4,5", 1, 3, 5);
        test("1,2,3,#,#,4,5,6", 1, 3, 5, 6);
        test("1,2,3,#,#,4,5,6,7,8,9,#,10,11,12,#,13,14", 1, 3, 5, 9, 14);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("TreeRightView");
    }
}
