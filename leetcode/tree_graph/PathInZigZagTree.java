import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1104: https://leetcode.com/problems/path-in-zigzag-labelled-binary-tree/
//
// In an infinite binary tree where every node has two children, the nodes are labelled in row
// order. In the odd numbered rows (ie., the first, third, fifth,...), the labelling is left to
// right, while in the even numbered rows, the labelling is right to left. Given the label of a node
// in the tree, return the labels in the path from the root of the tree to the node with that label.
//
// Constraints:
// 1 <= label <= 10^6
public class PathInZigZagTree {
    // Math
    // time complexity: O(log(L)), space complexity: O(log(L))
    // 0 ms(100.00%), 36.2 MB(97.66%) for 44 tests
    public List<Integer> pathInZigZagTree(int label) {
        List<Integer> res = new ArrayList<>();
        int level = (int)(Math.log(label) / Math.log(2));
        // or: int level = Integer.toBinaryString(label).length() - 1;
        for(int i = level, k = label; i >= 0; k >>= 1, i--) {
            res.add(k);
            k = (1 << i) * 3 - k - 1; // min = 2^i; max = 2^(i+1)-1; k = min+max-k
        }
        Collections.reverse(res);
        return res;
    }

    private void test(int label, Integer[] expected) {
        List<Integer> expectedList = Arrays.asList(expected);
        assertEquals(expectedList, pathInZigZagTree(label));
//        assertEquals(expectedList, pathInZigZagTree2(label));
    }

    @Test public void test() {
        test(16, new Integer[] {1, 3, 4, 15, 16});
        test(14, new Integer[] {1, 3, 4, 14});
        test(26, new Integer[] {1, 2, 6, 10, 26});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
