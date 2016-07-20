import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/count-of-smaller-numbers-after-self/
//
// You are given an integer array nums and you have to return a new counts array.
// The counts array has the property where counts[i] is the number of smaller
// elements to the right of nums[i].
public class CountSmaller {
    // Binary Indexed Tree
    // time complexity: O(N * log(N)), space complexity: O(N)
    // beats 46.27%(20 ms)
    public List<Integer> countSmaller(int[] nums) {
        int n = nums.length;
        if (n == 0) return Collections.emptyList();

        int[] buffer = nums.clone();
        Arrays.sort(buffer);
        // change comparison from numbers themselves to their places
        Map<Integer, Integer> place = new HashMap<>();
        for (int i = 0; i < n; i++) {
            place.put(buffer[i], i + 1);
        }

        buffer[n - 1] = 0; // buffer is now reused to save result
        int[] bit = new int[n + 1];
        for (int i = n - 2; i >= 0; i--) {
            for (int j = place.get(nums[i + 1]); j <= n; j += (j & -j)) {
                bit[j]++;
            }
            int count = 0;
            for (int j = place.get(nums[i]) - 1; j > 0; j -= (j & -j)) {
                count += bit[j]; // "-1" to skip all equal values
            }
            buffer[i] = count;
        }

        List<Integer> res = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            res.add(buffer[i]);
        }
        return res;
    }

    // Binary Search Tree
    // time complexity: average: O(N * log(N)) Worse case: O(N  ^ 2)
    // https://discuss.leetcode.com/topic/31422/easiest-java-solution
    // beats 58.96%(13 ms)
    public List<Integer> countSmaller2(int[] nums) {
        int n = nums.length;
        List<Integer> res = new LinkedList<>();
        if (n == 0) return res;

        TreeNode root = new TreeNode(nums[n - 1]);
        res.add(0);
        for (int i = n - 2; i >= 0; i--) {
            res.add(0, root.insertNode(nums[i]));
        }
        return res;
    }

    static class TreeNode {
        TreeNode left;
        TreeNode right;
        int val;
        int count = 1;

        TreeNode(int val) {
            this.val = val;
        }

        int insertNode(int v) {
            int total = 0;
            for (TreeNode root = this;; ) {
                if (v <= root.val) {
                    root.count++;
                    if (root.left == null) {
                        root.left = new TreeNode(v);
                        break;
                    } else {
                        root = root.left;
                    }
                } else {
                    total += root.count;
                    if (root.right == null) {
                        root.right = new TreeNode(v);
                        break;
                    } else {
                        root = root.right;
                    }
                }
            }
            return total;
        }
    }

    // Binary Search Tree
    // https://discuss.leetcode.com/topic/31405/9ms-short-java-bst-solution-get-answer-when-building-bst/2
    // time complexity: average: O(N * log(N)) Worse case: O(N  ^ 2)
    // beats 89.05(9 ms)
    public List<Integer> countSmaller3(int[] nums) {
        Integer[] res = new Integer[nums.length];
        Node root = null;
        for (int i = nums.length - 1; i >= 0; i--) {
            root = insert(nums[i], root, res, i, 0);
        }
        return Arrays.asList(res);
    }

    private Node insert(int num, Node node, Integer[] res, int i, int preCount) {
        if (node == null) {
            node = new Node(num, 0);
            res[i] = preCount;
        } else if (node.val == num) {
            node.dup++;
            res[i] = preCount + node.count;
        } else if (node.val > num) {
            node.count++;
            node.left = insert(num, node.left, res, i, preCount);
        } else {
            node.right = insert(num, node.right, res, i,
                                preCount + node.dup + node.count);
        }
        return node;
    }

    static class Node {
        Node left, right;
        int val;
        int count;
        int dup = 1;

        Node(int val, int count) {
            this.val = val;
            this.count = count;
        }
    }

    // Merge Sort
    // https://discuss.leetcode.com/topic/31162/mergesort-solution/9
    // time complexity: O(N * log(N)), space complexity: O(N)
    // beats 31.22%(47 ms)
    public List<Integer> countSmaller4(int[] nums) {
        int n = nums.length;
        List<Integer> res = new ArrayList<>(Collections.nCopies(n, 0));
        List<NumWithPos> numWithPos = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            numWithPos.add(new NumWithPos(nums[i], i));
        }
        numWithPos = sort(numWithPos);
        for (NumWithPos num : numWithPos) {
            res.set(num.pos, num.shift);
        }
        return res;
    }

    private List<NumWithPos> sort(List<NumWithPos> unsorted) {
        int size = unsorted.size();
        if (size <= 1) return unsorted;

        List<NumWithPos> l1 = sort(unsorted.subList(0, size / 2));
        List<NumWithPos> l2 = sort(unsorted.subList(size / 2, size));
        List<NumWithPos> sorted = new ArrayList<>();
        int i = 0;
        int j = 0;
        while (i < l1.size() && j < l2.size()) {
            if (l1.get(i).val <= l2.get(j).val) {
                sorted.add((i++) + j, l1.get(i - 1).shift(j));
            } else {
                sorted.add((j++) + i, l2.get(j - 1));
            }
        }
        while (i < l1.size()) {
            sorted.add(l1.get(i++).shift(j));
        }
        while (j < l2.size()) {
            sorted.add(l2.get(j++));
        }
        return sorted;
    }

    static class NumWithPos {
        int val;
        int pos;
        int shift;

        NumWithPos(int val, int pos) {
            this.val = val;
            this.pos = pos;
        }

        NumWithPos shift(int val){
            this.shift += val;
            return this;
        }
    }

    // Binary Search
    // time complexity: O(N * log(N)), space complexity: O(N)
    // beats 28.23%(52 ms)
    public List<Integer> countSmaller5(int[] nums) {
        Integer[] res = new Integer[nums.length];
        List<Integer> sorted = new ArrayList<>();
        for (int i = nums.length - 1; i >= 0; i--) {
            int index = find(sorted, nums[i]);
            res[i] = index;
            sorted.add(index, nums[i]);
        }
        return Arrays.asList(res);
    }

    private int find(List<Integer> sorted, int target) {
        if (sorted.size() == 0) return 0;

        int low = 0;
        int high = sorted.size() - 1;
        if (sorted.get(high) < target) return high + 1;

        if (sorted.get(low) >= target) return 0;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (sorted.get(mid) < target) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return (sorted.get(low) >= target) ? low : high;
    }

    void test(Function<int[], List<Integer> > count, String name,
              int[] nums, Integer ... expected) {
        long t1 = System.nanoTime();
        assertArrayEquals(expected, count.apply(nums).toArray(new Integer[0]));
        System.out.format("%s: %.3f ms\n", name, (System.nanoTime() - t1) * 1e-6);
    }

    void test(int[] nums, Integer ... expected) {
        CountSmaller c = new CountSmaller();
        test(c::countSmaller, "countSmaller", nums, expected);
        test(c::countSmaller2, "countSmaller2", nums, expected);
        test(c::countSmaller3, "countSmaller3", nums, expected);
        test(c::countSmaller4, "countSmaller4", nums, expected);
        test(c::countSmaller5, "countSmaller5", nums, expected);
    }

    @Test
    public void test1() {
        test(new int[] {5, 2, 6, 1}, 2, 1, 1, 0);
        test(new int[] {5, 2, 8, 9, 7, 4, 3, 6, 1}, 4, 1, 5, 5, 4, 2, 1, 1, 0);
        test(new int[] {5, 2, 6, 3, 7, 4, 3, 6, 1}, 5, 1, 4, 1, 4, 2, 1, 1, 0);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("CountSmaller");
    }
}
