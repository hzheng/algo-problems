import java.util.*;

import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC508: https://leetcode.com/problems/most-frequent-subtree-sum/
//
// Given the root of a tree, you are asked to find the most frequent subtree sum.
// The subtree sum of a node is defined as the sum of all the node values formed
// by the subtree rooted at that node (including the node itself). So what is the
// most frequent subtree sum value? If there is a tie, return all the values with
// the highest frequency in any order.
public class FrequentSubtreeSum {
    // Hash Table + Recursion
    // beats 99.55%(14 ms for 61 tests)
    public int[] findFrequentTreeSum(TreeNode root) {
        if (root == null) return new int[0];

        Map<Integer, Integer> freqs = new HashMap<>();
        int[] max = new int[1];
        countSum(root, freqs, max);
        List<Integer> freqSums = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : freqs.entrySet()) {
            if (entry.getValue() == max[0]) {
                freqSums.add(entry.getKey());
            }
        }
        int[] res = new int[freqSums.size()];
        for (int i = freqSums.size() - 1; i >= 0; i--) {
            res[i] = freqSums.get(i);
        }
        return res;
    }

    private int countSum(TreeNode root, Map<Integer, Integer> freqs, int[] max) {
        int sum = root.val;
        if (root.left != null) {
            sum += countSum(root.left, freqs, max);
        }
        if (root.right != null) {
            sum += countSum(root.right, freqs, max);
        }
        int freq = freqs.getOrDefault(sum, 0) + 1;
        max[0] = Math.max(max[0], freq);
        freqs.put(sum, freq);
        return sum;
    }

    void test(Function<TreeNode, int[]> findFrequentTreeSum,
              String s, int ... expected) {
        int[] res = findFrequentTreeSum(TreeNode.of(s));
        Arrays.sort(res);
        assertArrayEquals(expected, res);
    }

    void test(String s, int ... expected) {
        FrequentSubtreeSum f = new FrequentSubtreeSum();
        test(f::findFrequentTreeSum, s, expected);
    }

    @Test
    public void test() {
        test("5,2,-5", 2);
        test("5,2,-3,-1,0,1,-2,-1", -2, 0, 1);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("FrequentSubtreeSum");
    }
}
