import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC653: https://leetcode.com/problems/two-sum-iv-input-is-a-bst/
//
// Given a Binary Search Tree and a target number, return true if there exist
// two elements in the BST such that their sum is equal to the given target.
public class TwoSum4 {
    // Recursion + Two Pointers
    // time complexity: O(N), space complexity: O(N)
    // beats 12.62%(25 ms for 421 tests)
    public boolean findTarget(TreeNode root, int k) {
        List<Integer> list = new ArrayList<>();
        traverse(root, list);
        for (int i = 0, j = list.size() - 1; i < j; ) {
            int sum = list.get(i) + list.get(j);
            if (sum < k) {
                i++;
            } else if (sum > k) {
                j--;
            } else return true;
        }
        return false;
    }

    private void traverse(TreeNode root, List<Integer> list) {
        if (root == null) return;

        traverse(root.left, list);
        list.add(root.val);
        traverse(root.right, list);
    }

    // Recursion + Set
    // time complexity: O(N), space complexity: O(N)
    // beats 5.48%(30 ms for 421 tests)
    public boolean findTarget2(TreeNode root, int k) {
        return find(root, k, new HashSet<>());
    }

    public boolean find(TreeNode root, int k, Set<Integer> visited) {
        if (root == null) return false;

        if (visited.contains(k - root.val)) return true;

        visited.add(root.val);
        return find(root.left, k, visited) || find(root.right, k, visited);
    }

    // BFS + Queue
    // time complexity: O(N), space complexity: O(N)
    // beats 12.62%(25 ms for 421 tests)
    public boolean findTarget3(TreeNode root, int k) {
        Set<Integer> visited = new HashSet<>();
        Queue<TreeNode> q = new LinkedList<>();
        for (q.offer(root); !q.isEmpty(); ) {
            TreeNode node = q.poll();
            if (visited.contains(k - node.val)) return true;

            visited.add(node.val);
            if (node.left != null) {
                q.offer(node.left);
            }
            if (node.right != null) {
                q.offer(node.right);
            }
        }
        return false;
    }

    // Recursion
    // time complexity: O(N * log(N)), space complexity: O(log(N))
    // beats 68.20%(18 ms for 421 tests)
    public boolean findTarget4(TreeNode root, int k) {
        return dfs(root, root, k);
    }

    public boolean dfs(TreeNode root, TreeNode cur, int k) {
        if (cur == null) return false;

        return search(root, cur, k - cur.val)
               || dfs(root, cur.left, k) || dfs(root, cur.right, k);
    }

    public boolean search(TreeNode root, TreeNode cur, int target) {
        if (root == null) return false;

        return (root.val == target) && (root != cur)
               || (root.val < target) && search(root.right, cur, target)
               || (root.val > target) && search(root.left, cur, target);
    }

    // Stack
    // time complexity: O(N), space complexity: O(log(N))
    // beats 8.19%(27 ms for 421 tests)
    public boolean findTarget5(TreeNode root, int k) {
        Stack<TreeNode> leftStack = new Stack<>();
        add(leftStack, root, true);
        Stack<TreeNode> rightStack = new Stack<>();
        add(rightStack, root, false);
        while (leftStack.peek() != rightStack.peek()) {
            int sum = leftStack.peek().val + rightStack.peek().val;
            if (sum > k) {
                next(rightStack, false);
            } else if (sum < k) {
                next(leftStack, true);
            } else return true;
        }
        return false;
    }
    
    private void add(Stack<TreeNode> stack, TreeNode root, boolean isLeft) {
    	for (TreeNode cur = root; cur != null; ) {
    	    stack.push(cur);
            cur = isLeft ? cur.left : cur.right;
    	}
    }

    private void next(Stack<TreeNode> stack, boolean isLeft) {
    	TreeNode node = stack.pop();
    	if (isLeft) {
    	    add(stack, node.right, isLeft);
    	} else {
    	    add(stack, node.left, isLeft);
    	}
    }

    void test(String root, int k, boolean expected) {
        assertEquals(expected, findTarget(TreeNode.of(root), k));
        assertEquals(expected, findTarget2(TreeNode.of(root), k));
        assertEquals(expected, findTarget3(TreeNode.of(root), k));
        assertEquals(expected, findTarget4(TreeNode.of(root), k));
        assertEquals(expected, findTarget5(TreeNode.of(root), k));
    }

    @Test
    public void test() {
        test("5,3,6,2,#,7", 9, true);
        test("5,3,6,2,4,#,7", 9, true);
        test("5,3,6,2,#,7", 14, false);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
