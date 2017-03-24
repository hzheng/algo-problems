import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import java.util.List;
import org.junit.Test;

import static org.junit.Assert.*;
import static common.TreeUtils.*;
import common.TreeNode2;

/**
 * Cracking the Coding Interview(5ed) Problem 4.9:
 * Given a binary tree, print all paths which sum to a given value.
 */
public class FindSumTree {
    // time complexity: O(N ^ 2), space complexity: O(log(N))
    public static List<List<TreeNode2> > findSum(TreeNode2 root, int sum) {
        List<List<TreeNode2> > results = new ArrayList<>();
        LinkedList<TreeNode2> path = new LinkedList<>();
        findSum(results, path, root, sum);
        return results;
    }

    private static void findSum(List<List<TreeNode2> > results,
                                LinkedList<TreeNode2> path, TreeNode2 root, int sum) {
        if (root == null) return;

        sumStart(results, path, root, sum);
        findSum(results, path, root.left, sum);
        findSum(results, path, root.right, sum);
    }

    private static void sumStart(List<List<TreeNode2> > results,
                                 LinkedList<TreeNode2> path, TreeNode2 root, int sum) {
        if (root == null) return;

        if (root.data == sum) {
            List<TreeNode2> nodes = new ArrayList<>(path);
            nodes.add(root);
            results.add(nodes);
        }

        sum -= root.data;
        path.add(root);
        sumStart(results, path, root.left, sum);
        sumStart(results, path, root.right, sum);
        path.removeLast();
    }

    // time complexity: O(N * log(N)), space complexity: O(log(N))
    // the method from book is more simple(only one recursion) and
    // time efficient(vs. O(N ^ 2)) for two reasons:
    // 1. it only prints path value instead of get path nodes
    // 2. using extra array to store path makes it easy to look backwards
    // 3. most of all, its search strategy is "ends at" instead of "starts from"
    public static void findSum2(TreeNode2 node, int sum) {
        int[] path = new int[depth(node)];
        findSum2(node, sum, path, 0);
    }

    private static void findSum2(TreeNode2 node, int sum, int[] path, int level) {
        if (node == null) return;
        /* Insert current node into path. */
        path[level] = node.data;
        /* Look for paths with a sum that ends at this node. */
        int t = 0;
        for (int i = level; i >= 0; i--) {
            t += path[i];
            if (t == sum) {
                printPath(path, i, level);
            }
        }

        /* Search nodes beneath this one. */
        findSum2(node.left, sum, path, level + 1);
        findSum2(node.right, sum, path, level + 1);

        /* Remove current node from path. Not strictly necessary, since
         * we would ignore this value, but it's good practice. */
        path[level] = Integer.MIN_VALUE;
    }

    private static void printPath(int[] path, int start, int end){
        for(int i = start; i <= end; i++) {
            System.out.print(path[i] + " ");
        }
        System.out.println();
    }

    private static int depth(TreeNode2 node) {
        if (node == null) return 0;
        return 1 + Math.max(depth(node.left), depth(node.right));
    }

    // get idea from <tt>findSum2</tt>
    public static List<List<TreeNode2> > findSum3(TreeNode2 root, int sum) {
        List<List<TreeNode2> > results = new ArrayList<>();
        TreeNode2[] path = new TreeNode2[depth(root)];
        findSum3(results, path, root, sum, 0);
        return results;
    }

    private static void findSum3(List<List<TreeNode2> > results,
                                 TreeNode2[] path, TreeNode2 node,
                                 int sum, int level) {
        if (node == null) return;

        path[level] = node;
        int t = 0;
        for (int i = level; i >= 0; i--) {
            t += path[i].data;
            if (t == sum) {
                results.add(createPath(path, i, level));
            }
        }

        findSum3(results, path, node.left, sum, level + 1);
        findSum3(results, path, node.right, sum, level + 1);
        path[level] = null;
    }

    private static List<TreeNode2> createPath(
        TreeNode2[] path, int start, int end) {
        List<TreeNode2> result = new ArrayList<>();
        for(int i = start; i <= end; i++) {
            result.add(path[i]);
        }
        return result;
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(Integer[] array, int sum, int[][] expectedPaths) {
        TreeNode2 t = createTree(array);
        print(t);
        findSum2(t, sum);

        test(array, sum, expectedPaths, FindSumTree::findSum);
        test(array, sum, expectedPaths, FindSumTree:: findSum3);
    }

    private static int compareIntArray(int[] a1, int[] a2) {
        if (a1.length != a2.length) return a1.length - a2.length;

        for (int i = 0; i < a1.length; i++) {
            if (a1[i] != a2[i]) return a1[i] - a2[i];
        }
        return 0;
    }

    private void test(Integer[] array, int sum, int[][] expectedPaths,
                      Function<TreeNode2, Integer, List<List<TreeNode2>>> find) {
        TreeNode2 t = createTree(array);

        List<int[]> pathList = new ArrayList<int[]>();
        for (List<TreeNode2> path : find.apply(t, sum)) {
            pathList.add(path.stream().mapToInt(n -> n.data).toArray());
        }
        // pathList.forEach(p -> System.out.println(Arrays.toString(p)));
        pathList.sort(FindSumTree::compareIntArray);
        // pathList.forEach(p -> System.out.println(Arrays.toString(p)));
        assertArrayEquals(expectedPaths, pathList.toArray());
    }

    @Test
    public void test1() {
        test(new Integer[] {3, 1, 5, 6, 4, 2, 3, 5, 2, 4, 2, 5, 3, 3, 2}, 12,
             new int[][] {{1, 6, 5}, {5, 2, 5}, {3, 1, 4, 4}, {3, 1, 6, 2}});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("FindSumTree");
    }
}
