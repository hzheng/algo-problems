import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// LC315: https://leetcode.com/problems/count-of-smaller-numbers-after-self/
//
// You are given an integer array nums and you have to return a new counts array.
// The counts array has the property where counts[i] is the number of smaller
// elements to the right of nums[i].
public class CountSmaller {
    // Solution of Choice
    // Sort + Binary Indexed Tree
    // time complexity: O(N * log(N)), space complexity: O(N)
    // beats 44.89%(23 ms for 16 tests)
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
        int[] bit = new int[n + 1];
        List<Integer> res = new LinkedList<>();
        res.add(0);
        for (int i = n - 2; i >= 0; i--) {
            for (int j = place.get(nums[i + 1]); j <= n; j += (j & -j)) {
                bit[j]++;
            }
            int count = 0;
            for (int j = place.get(nums[i]) - 1; j > 0; j -= (j & -j)) {
                count += bit[j]; // "-1" to skip all equal values
            }
            res.add(0, count);
        }
        return res;
    }

    // Binary Indexed Tree
    // https://discuss.leetcode.com/topic/39656/short-java-binary-index-tree-beat-97-33-with-detailed-explanation/
    // time complexity: O(N * log(N)), space complexity: O(N)
    // although it's fastest(no sorting process like above solution), but if
    // range of numbers is large, it will take much memory
    // beats 99.94%(6 ms for 16 tests)
    public List<Integer> countSmaller1(int[] nums) {
        List<Integer> res = new LinkedList<>();
        int n = nums.length;
        if (n == 0) return res;

        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < n; i++) {
            min = Math.min(min, nums[i]);
        }
        int[] nums2 = new int[n];
        for (int i = 0; i < n; i++) {
            nums2[i] = nums[i] - min + 1;
            max = Math.max(nums2[i], max);
        }
        int[] bit = new int[max + 1];
        for (int i = n - 1; i >= 0; i--) {
            int count = 0;
            for (int j = nums2[i] - 1; j > 0; j -= (j & -j)) {
                count += bit[j];
            }
            res.add(0, count);
            for (int j = nums2[i]; j < bit.length; j += (j & -j)) {
                bit[j]++;
            }
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
    // https://discuss.leetcode.com/topic/31405/9ms-short-java-bst-solution-get-answer-when-building-bst/
    // time complexity: average: O(N * log(N)) Worse case: O(N  ^ 2)
    // beats 89.05%(9 ms)
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

    // Recursion + Divide & Conquer + Merge Sort
    // https://discuss.leetcode.com/topic/31162/mergesort-solution/
    // time complexity: O(N * log(N)), space complexity: O(N)
    // beats 31.22%(47 ms)
    public List<Integer> countSmaller4(int[] nums) {
        int n = nums.length;
        List<Integer> res = new ArrayList<>(Collections.nCopies(n, 0));
        List<NumWithPos> numWithPos = new ArrayList<>(n);
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
    // beats 25.78%(55 ms for 16 tests)
    public List<Integer> countSmaller5(int[] nums) {
        List<Integer> res = new LinkedList<>();
        List<Integer> sorted = new ArrayList<>();
        for (int i = nums.length - 1; i >= 0; i--) {
            int index = find(sorted, nums[i]);
            res.add(0, index);
            sorted.add(index, nums[i]);
        }
        return res;
    }

    private int find(List<Integer> sorted, int target) {
        if (sorted.size() == 0) return 0;

        int low = 0;
        int high = sorted.size() - 1;
        if (sorted.get(high) < target) return high + 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            if (sorted.get(mid) < target) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return (sorted.get(low) >= target) ? low : high;
    }

    // slower than <code>find</code>
    private int find2(List<Integer> sorted, int target) {
        int index = Collections.binarySearch(sorted, target);
        if (index < 0) {
            index = -index - 1;
        } else {
            for (; index > 0 && sorted.get(index - 1) == target; index--) {}
        }
        return index;
    }

    // Recursion + Divide and Conquer & Bit Manipulation
    // https://discuss.leetcode.com/topic/31924/o-nlogn-divide-and-conquer-java-solution-based-on-bit-by-bit-comparison
    // time complexity: O(N), space complexity: O(N)
    // beats 18.03%(62 ms)
    public List<Integer> countSmaller6(int[] nums) {
        int n = nums.length;
        List<Integer> res = new ArrayList<>(n);
        List<Integer> index = new ArrayList<>(n);
        for (int i = n - 1; i >= 0; i--) {
            res.add(0);
            index.add(i);
        }
        countSmaller(nums, index, 1 << 31, res);
        return res;
    }

    private void countSmaller(int[] nums, List<Integer> index, int mask,
                              List<Integer> res) {
        if (mask == 0 || index.size() <= 1) return;

        int n = index.size();
        List<Integer> highGroup = new ArrayList<>(n);
        List<Integer> lowGroup = new ArrayList<>(n);
        int highBit = (mask < 0 ? 0 : mask);
        for (int j = 0; j < n; j++) {
            int i = index.get(j);
            if ((nums[i] & mask) == highBit) {
                res.set(i, res.get(i) + lowGroup.size());
                highGroup.add(i);
            } else {
                lowGroup.add(i);
            }
        }
        countSmaller(nums, lowGroup, mask >>> 1, res);
        countSmaller(nums, highGroup, mask >>> 1, res);
    }

    // Segment Tree
    // beats 38.16%(34 ms for 16 tests)
    public List<Integer> countSmaller7(int[] nums) {
        if (nums.length == 0) return Collections.emptyList();

        int min = Integer.MAX_VALUE;
        for (int i : nums) {
            min = Math.min(min, i);
        }
        int max = Integer.MIN_VALUE;
        for (int i : nums) {
            max = Math.max(max, i);
        }
        SegmentTreeNode root = SegmentTreeNode.build(min, max);
        for (int num : nums) {
            root.update(num, 1);
        }
        List<Integer> res = new ArrayList<>();
        for (int num : nums) {
            root.update(num, -1);
            res.add(root.query(min, num - 1));
        }
        return res;
    }

    private static class SegmentTreeNode {
        int start, end, count;
        SegmentTreeNode left, right;
        SegmentTreeNode(int start, int end) {
            this.start = start;
            this.end = end;
        }

        public static SegmentTreeNode build(int start, int end) {
            SegmentTreeNode root = new SegmentTreeNode(start, end);
            if (start == end) return root;

            int mid = start + (end - start) / 2; // NOT (start + end) >>> 1;
            root.left = build(start, mid);
            root.right = build(mid + 1, end);
            return root;
        }

        public void update(int val, int change) {
            if (start > val || end < val) return;

            if (start == val && end == val) {
                count += change;
                return;
            }
            int mid = start + (end - start) / 2;
            if (val <= mid) {
                left.update(val, change);
            } else {
                right.update(val, change);
            }
            count = left.count + right.count;
        }

        public int query(int start, int end) {
            if (this.start == start && this.end == end) return count;

            int mid = this.start + (this.end - this.start) / 2;
            if (end < mid) return left == null ? 0 : left.query(start, end);

            return left.query(start, mid) + right.query(mid + 1, end);
        }
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
        test(c::countSmaller1, "countSmaller1", nums, expected);
        test(c::countSmaller2, "countSmaller2", nums, expected);
        test(c::countSmaller3, "countSmaller3", nums, expected);
        test(c::countSmaller4, "countSmaller4", nums, expected);
        test(c::countSmaller5, "countSmaller5", nums, expected);
        test(c::countSmaller6, "countSmaller6", nums, expected);
        test(c::countSmaller7, "countSmaller7", nums, expected);
    }

    @Test
    public void test1() {
        test(new int[] {});
        test(new int[] {-1, -2}, 1, 0);
        test(new int[] {5, 2, 6, 1}, 2, 1, 1, 0);
        test(new int[] {5, 2, 8, 9, 7, 4, 3, 6, 1}, 4, 1, 5, 5, 4, 2, 1, 1, 0);
        test(new int[] {5, 2, 6, 3, 7, 4, 3, 6, 1}, 5, 1, 4, 1, 4, 2, 1, 1, 0);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("CountSmaller");
    }
}
