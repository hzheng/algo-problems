import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.Utils;
import common.TreeNode;

// LC272: https://leetcode.com/problems/closest-binary-search-tree-value-ii
//
// Given a non-empty binary search tree and a target value, find k values in the
// BST that are closest to the target.
// Note:
// You may assume k is always valid, that is: k ≤ total nodes.
// You are guaranteed to have only one unique answer
// Follow up:
// Assume that the BST is balanced, could you solve it in less than O(n) runtime?
public class ClosestBSTValue2 {
    // Heap + DFS + Recursion
    // time complexity: O(N), space complexity: O(N)
    // beats 9.71%(12 ms for 68 tests)
    public List<Integer> closestKValues(TreeNode root, double target, int k) {
        PriorityQueue<Integer> pq = new PriorityQueue<>(k + 1, new Comparator<Integer>() {
            public int compare(Integer a, Integer b) {
                return Math.abs(a - target) > Math.abs(b - target) ? -1 : 1;
            }
        });
        dfs(root, target, k, pq);
        List<Integer> res = new LinkedList<>();
        while (!pq.isEmpty()) {
            res.add(0, pq.poll());
        }
        return res;
    }

    private void dfs(TreeNode root, double target, int k, PriorityQueue<Integer> pq) {
        if (root == null) return;

        dfs(root.left, target, k, pq);
        pq.offer(root.val);
        if (pq.size() > k) {
            pq.poll();
        }
        dfs(root.right, target, k, pq);
    }

    // Stack + Heap
    // time complexity: O(N), space complexity: O(N)
    // beats 9.71%(12 ms for 68 tests)
    public List<Integer> closestKValues2(TreeNode root, double target, int k) {
        ArrayDeque<TreeNode> path = new ArrayDeque<>();
        int closest = root.val;
        for (TreeNode cur = root; cur != null; ) {
            path.push(cur);
            if (Math.abs(target - cur.val) < Math.abs(target - closest)) {
                closest = cur.val;
            }
            cur = (cur.val > target) ? cur.left : cur.right;
        }
        while (path.peek().val != closest) {
            path.pop();
        }
        PriorityQueue<Integer> pq = new PriorityQueue<>(k + 1, new Comparator<Integer>() {
            public int compare(Integer a, Integer b) {
                return Math.abs(a - target) > Math.abs(b - target) ? -1 : 1;
            }
        });
        TreeNode top = path.pop();
        pq.offer(top.val);
        fill(top, k, true, pq);
        fill(top, k, false, pq);
        for (TreeNode prev = top; !path.isEmpty(); prev = top) {
            top = path.pop();
            pq.offer(top.val);
            if (pq.size() <= k || pq.poll() != top.val) {
                fill(top, k, top.left != prev, pq);
            }
        }
        List<Integer> res = new LinkedList<>();
        while (!pq.isEmpty()) {
            res.add(0, pq.poll());
        }
        return res;
    }

    private void fill(TreeNode root, int k, boolean left, PriorityQueue<Integer> pq) {
        ArrayDeque<TreeNode> stack = new ArrayDeque<>();
        for (TreeNode cur = left ? root.left : root.right; cur != null || !stack.isEmpty(); ) {
            if (cur != null) {
                stack.push(cur);
                cur = left ? cur.right : cur.left;
            } else {
                cur = stack.pop();
                pq.offer(cur.val);
                if (pq.size() > k && pq.poll() == cur.val) return;

                cur = left ? cur.left : cur.right;
            }
        }
    }

    // 2 Stacks
    // time complexity: O(K * log(N)), space complexity: O(K * log(N))
    // beats 77.24%(4 ms for 68 tests)
    public List<Integer> closestKValues3(TreeNode root, double target, int k) {
        ArrayDeque<TreeNode> predecessors = new ArrayDeque<>();
        ArrayDeque<TreeNode> successors = new ArrayDeque<>();
        for (TreeNode cur = root; cur != null; ) {
            if (cur.val > target) {
                successors.push(cur);
                cur = cur.left;
            } else {
                predecessors.push(cur);
                cur = cur.right;
            }
        }
        List<Integer> res = new ArrayList<>();
        for (int i = k; (!predecessors.isEmpty() || !successors.isEmpty()) && i > 0; i--) {
            if (successors.isEmpty() || !predecessors.isEmpty() &&
                Math.abs(target - predecessors.peek().val) < Math.abs(target - successors.peek().val)) {
                res.add(next(predecessors, true));
            } else {
                res.add(next(successors, false));
            }
        }
        return res;
    }

    private int next(ArrayDeque<TreeNode> stack, boolean left) {
        TreeNode res = stack.pop();
        for (TreeNode n = left ? res.left : res.right; n != null; n = left ? n.right : n.left) {
            stack.push(n);
        }
        return res.val;
    }

    // Queue + DFS + Recursion
    // time complexity: O(N), space complexity: O(N)
    // beats 89.68%(2 ms for 68 tests)
    public List<Integer> closestKValues4(TreeNode root, double target, int k) {
        LinkedList<Integer> queue = new LinkedList<>();
        traverse(root, target, k, queue);
        return queue;
    }

    private boolean traverse(TreeNode root, double target, int k, Queue<Integer> queue) {
        if (root == null) return false;
        if (traverse(root.left, target, k, queue)) return true;

        if (queue.size() == k) {
            if (Math.abs(queue.peek() - target) < Math.abs(root.val - target)) return true;

            queue.poll();
        }
        queue.offer(root.val);
        return traverse(root.right, target, k, queue);
    }

    // Stack + Queue
    // time complexity: O(N), space complexity: O(N)
    // beats 50.83%(5 ms for 68 tests)
    public List<Integer> closestKValues5(TreeNode root, double target, int k) {
        ArrayDeque<TreeNode> stack = new ArrayDeque<>();
        LinkedList<Integer> res = new LinkedList<>();
        for (TreeNode cur = root; cur != null || !stack.isEmpty(); ) {
            if (cur != null) {
                stack.push(cur);
                cur = cur.left;
            } else {
                cur = stack.pop();
                if (res.size() >= k) {
                    if (Math.abs(res.getFirst() - target) < Math.abs(cur.val - target)) break;

                    res.removeFirst();
                }
                res.addLast(cur.val);
                cur = cur.right;
            }
        }
        return res;
    }

    @FunctionalInterface
    interface Function<A, B, C, D> {
        public D apply(A a, B b, C c);
    }

    void test(Function<TreeNode, Double, Integer, List<Integer> > closestValue,
              String s, double target, int k, int[] expected) {
        List<Integer> res = closestValue.apply(TreeNode.of(s), target, k);
        Arrays.sort(expected);
        int[] resArray = Utils.toArray(res);
        Arrays.sort(resArray);
        assertArrayEquals(expected, resArray);
    }

    void test(String s, double target, int k, int[] expected) {
        ClosestBSTValue2 c = new ClosestBSTValue2();
        test(c::closestKValues, s, target, k, expected);
        test(c::closestKValues2, s, target, k, expected);
        test(c::closestKValues3, s, target, k, expected);
        test(c::closestKValues4, s, target, k, expected);
        test(c::closestKValues5, s, target, k, expected);
    }

    @Test
    public void test1() {
        test("1,#,2", 3.4, 1, new int[] {2});
        test("2,1,3", 5.571429, 2, new int[] {3, 2});
        test("1,#,2,#,3", 1.1, 2, new int[] {1, 2});
        test("1,#,2,#,3,#,4,#,5", 1.1, 3, new int[] {1, 2, 3});
        test("31,30,48,3,#,38,49,0,16,35,47,#,#,#,2,15,27,33,37,39,#,1,#,5,#,22,28,32,34,36,#,#,43,#,#,4,11,19,23,#,29,#,#,#,#,#,#,40,46,#,#,7,14,17,21,#,26,#,#,#,41,44,#,6,10,13,#,#,18,20,#,25,#,#,42,#,45,#,#,8,#,12,#,#,#,#,#,24,#,#,#,#,#,#,9",
             4.000000, 10, new int[] {4, 3, 5, 2, 6, 1, 7, 0, 8, 9});
        test("18,0,40,#,2,22,49,1,17,21,32,45,#,#,#,9,#,19,#,29,37,44,47,8,13,#,20,26,30,33,39,42,#,46,48,3,#,10,16,#,#,24,27,#,31,#,35,38,#,41,43,#,#,#,#,#,4,#,12,14,#,23,25,#,28,#,#,34,36,#,#,#,#,#,#,#,7,11,#,#,15,#,#,#,#,#,#,#,#,#,#,5,#,#,#,#,#,#,6",
             5.142857, 3, new int[] {5, 6, 4});
        test("2,0,33,#,1,25,40,#,#,11,31,34,45,10,18,29,32,#,36,43,46,4,#,12,24,26,30,#,#,35,39,42,44,#,48,3,9,#,14,22,#,#,27,#,#,#,#,38,#,41,#,#,#,47,49,#,#,5,#,13,15,21,23,#,28,37,#,#,#,#,#,#,#,#,8,#,#,#,17,19,#,#,#,#,#,#,#,7,#,16,#,#,20,6",
             2.428571, 14, new int[] {2, 3, 1, 4, 0, 5, 6, 7, 8, 9, 10, 11, 12, 13});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ClosestBSTValue2");
    }
}
