import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1361: https://leetcode.com/problems/validate-binary-tree-nodes/
//
// You have n binary tree nodes numbered from 0 to n - 1 where node i has two children leftChild[i] and rightChild[i],
// return true if and only if all the given nodes form exactly one valid binary tree.
// If node i has no left child then leftChild[i] will equal -1, similarly for the right child.
// Note that the nodes have no values and that we only use the node numbers in this problem.
//
// 1 <= n <= 10^4
// leftChild.length == rightChild.length == n
// -1 <= leftChild[i], rightChild[i] <= n - 1
public class ValidateBinaryTreeNodes {
    // Queue
    // time complexity: O(N), space complexity: O(N)
    // 184 ms(5.15%), 41.3 MB(100%) for 33 tests
    public boolean validateBinaryTreeNodes(int n, int[] leftChild, int[] rightChild) {
        for (int i = 0; i < n; i++) {
            if (validateBinaryTreeNodes(i, n, leftChild, rightChild)) return true;
        }
        return false;
    }

    private boolean validateBinaryTreeNodes(int root, int n, int[] leftChild, int[] rightChild) {
        boolean[] visited = new boolean[n];
        Queue<Integer> queue = new LinkedList<>();
        for (queue.offer(root); !queue.isEmpty(); ) {
            int cur = queue.poll();
            if (visited[cur]) return false;

            visited[cur] = true;
            int left = leftChild[cur];
            if (left >= 0) {
                queue.offer(left);
            }
            int right = rightChild[cur];
            if (right >= 0) {
                queue.offer(right);
            }
        }
        for (boolean v : visited) {
            if (!v) return false;
        }
        return true;
    }

    // Set
    // time complexity: O(N), space complexity: O(N)
    // 5 ms(52.44%), 41.7 MB(100%) for 33 tests
    public boolean validateBinaryTreeNodes2(int n, int[] leftChild, int[] rightChild) {
        if (n == 1) return true;

        Set<Integer> linked = new HashSet<>();
        for (int i = n - 1; i >= 0; i--) {
            if (leftChild[i] >= 0) {
                if (!linked.add(leftChild[i])) return false;
            }
            if (rightChild[i] >= 0) {
                if (!linked.add(rightChild[i])) return false;
            }
        }
        for (int i = n - 1; i >= 0; i--) {
            if (leftChild[i] < 0 && rightChild[i] < 0 && !linked.contains(i)) return false;
        }
        return linked.size() == n - 1;
    }

    // Set
    // time complexity: O(N), space complexity: O(N)
    // 12 ms(22.57%), 41.5 MB(100%) for 33 tests
    public boolean validateBinaryTreeNodes3(int n, int[] leftChild, int[] rightChild) {
        if (n == 1) return true;

        Set<Integer> nodes = IntStream.range(0, n).boxed().collect(Collectors.toCollection(HashSet::new));
        int[] inDegree = new int[n];
        for (int i = 0; i < n; i++) {
            for (int child : new int[]{leftChild[i], rightChild[i]}) {
                if (child < 0) continue;
                if (++inDegree[child] > 1) return false;

                nodes.remove(child);
            }
        }
        if (nodes.size() != 1) return false;

        int root = nodes.iterator().next(); // check for cycle
        return (leftChild[root] >= 0) || (rightChild[root] >= 0); // shouldn't be single root
    }

    // Union Find
    // time complexity: O(N), space complexity: O(N)
    // 2 ms(87.97%), 41.5 MB(100%) for 33 tests
    public boolean validateBinaryTreeNodes4(int n, int[] leftChild, int[] rightChild) {
        int[] parent = new int[n];
        Arrays.fill(parent, -1);
        for (int i = n - 1; i >= 0; i--) {
            if (leftChild[i] >= 0) {
                if (parent[leftChild[i]] >= 0) return false;

                parent[leftChild[i]] = i;
            }
            if (rightChild[i] >= 0) {
                if (parent[rightChild[i]] >= 0) return false;

                parent[rightChild[i]] = i;
            }
        }
        int root = -1;
        for (int i = 0; i < n; i++) {
            int p = parent[i];
            if (p < 0) {
                if (root >= 0) return false;

                root = i;
            }
        }
        return (root >= 0) && (n == 1 || leftChild[root] >= 0 || rightChild[root] >= 0);
    }

    private void test(int n, int[] leftChild, int[] rightChild, boolean expected) {
        assertEquals(expected, validateBinaryTreeNodes(n, leftChild, rightChild));
        assertEquals(expected, validateBinaryTreeNodes2(n, leftChild, rightChild));
        assertEquals(expected, validateBinaryTreeNodes3(n, leftChild, rightChild));
        assertEquals(expected, validateBinaryTreeNodes4(n, leftChild, rightChild));
    }

    @Test
    public void test() {
        test(6, new int[]{1, -1, -1, 4, -1, -1}, new int[]{2, -1, -1, 5, -1, -1}, false); // not connected
        test(2, new int[]{-1, -1}, new int[]{-1, -1}, false);
        test(4, new int[]{1, 2, 0, -1}, new int[]{-1, -1, 3, -1}, false);
        test(2, new int[]{-1, 0}, new int[]{-1, -1}, true); // root is not 0
        test(4, new int[]{1, -1, 3, -1}, new int[]{2, -1, -1, -1}, true);
        test(4, new int[]{1, -1, 3, -1}, new int[]{2, 3, -1, -1}, false);
        test(2, new int[]{1, 0}, new int[]{-1, -1}, false);
        test(4, new int[]{1, 2, 0, -1}, new int[]{-1, -1, -1, -1}, false); // cycle
        test(1, new int[]{-1}, new int[]{-1}, true);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
