import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

import common.TreeNode;

// LC1305: https://leetcode.com/problems/all-elements-in-two-binary-search-trees/
//
// Given two binary search trees root1 and root2.
// Return a list containing all the integers from both trees sorted in ascending order.
//
// Constraints:
// Each tree has at most 5000 nodes.
// Each node's value is between [-10^5, 10^5].
public class GetAllElements {
    // DFS + Recursion + Sort
    // time complexity: O(N), space complexity: O(N)
    // 15 ms(51.64%), 41.8 MB(82.33%) for 48 tests
    public List<Integer> getAllElements(TreeNode root1, TreeNode root2) {
        List<Integer> res = new ArrayList<>();
        traverse(root1, res);
        traverse(root2, res);
        Collections.sort(res);
        return res;
    }

    private void traverse(TreeNode root, List<Integer> list) {
        if (root == null) { return; }

        list.add(root.val);
        traverse(root.left, list);
        traverse(root.right, list);
    }

    // Stack
    // time complexity: O(N), space complexity: O(N)
    // 13 ms(83.43%), 42 MB(61.65%) for 48 tests
    public List<Integer> getAllElements2(TreeNode root1, TreeNode root2) {
        List<Integer> res = new ArrayList<>();
        Stack<TreeNode> stack1 = new Stack<>();
        Stack<TreeNode> stack2 = new Stack<>();
        for (TreeNode p1 = root1, p2 = root2; p1 != null || p2 != null
                                              || !stack1.empty() || !stack2.empty(); ) {
            for (; p1 != null; p1 = p1.left) {
                stack1.push(p1);
            }
            for (; p2 != null; p2 = p2.left) {
                stack2.push(p2);
            }
            if (stack2.isEmpty() || !stack1.isEmpty() && stack1.peek().val <= stack2.peek().val) {
                p1 = stack1.pop();
                res.add(p1.val);
                p1 = p1.right;
            } else {
                p2 = stack2.pop();
                res.add(p2.val);
                p2 = p2.right;
            }
        }
        return res;
    }

    @FunctionalInterface interface Function<A, B, C> {
        C apply(A a, B b);
    }

    void test(Function<TreeNode, TreeNode, List<Integer>> getAllNodes, String s1, String s2,
              Integer[] expected) {
        List<Integer> expectedList = Arrays.asList(expected);
        assertEquals(expectedList, getAllNodes.apply(TreeNode.of(s1), TreeNode.of(s2)));
    }

    private void test(String s1, String s2, Integer[] expected) {
        GetAllElements g = new GetAllElements();
        test(g::getAllElements, s1, s2, expected);
        test(g::getAllElements2, s1, s2, expected);
    }

    @Test public void test() {
        test("2,1,4", "1,0,3", new Integer[] {0, 1, 1, 2, 3, 4});
        test("0,-10,10", "5,1,7,0,2", new Integer[] {-10, 0, 0, 1, 2, 5, 7, 10});
        test("", "5,1,7,0,2", new Integer[] {0, 1, 2, 5, 7});
        test("0,-10,10", "", new Integer[] {-10, 0, 10});
        test("1,#,8", "8,1", new Integer[] {1, 1, 8, 8});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
