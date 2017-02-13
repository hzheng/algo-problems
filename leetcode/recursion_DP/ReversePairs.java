import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC493: https://leetcode.com/problems/reverse-pairs/
//
// Given an array nums, we call (i, j) an important reverse pair if i < j and
// nums[i] > 2*nums[j].
// You need to return the number of important reverse pairs in the given array.
public class ReversePairs {
    // Binary Search
    // beats 0%(760 ms for 35 tests)
    // Time Limit Exceeded(for larger tests)
    public int reversePairs(int[] nums) {
        int res = 0;
        List<Integer> sorted = new ArrayList<>();
        for (int i = nums.length - 1; i >= 0; i--) {
            int target = nums[i];
            // int index = Collections.binarySearch(sorted, target);
            // if (index < 0) {
            //     index = -index - 1;
            // }
            // for (; index > 0 && target == sorted.get(index - 1); index--) {}
            // res += index;
            res += find(sorted, target);
            if (target < Integer.MIN_VALUE / 2) {
                res += i;
            } else if (target <= Integer.MAX_VALUE / 2) {
                // int index = Collections.binarySearch(sorted, target * 2);
                // if (index < 0) {
                //     index = -index - 1;
                // }
                int index = sorted.isEmpty() ? 0 : find(sorted, target * 2);
                sorted.add(index, target * 2);
            }
        }
        return res;
    }

    // Binary Search
    // beats 0%(780 ms for 35 tests)
    // Time Limit Exceeded(for larger tests)
    public int reversePairs1(int[] nums) {
        int res = 0;
        List<Integer> sorted = new ArrayList<>();
        for (int num : nums) {
            res += sorted.size() - find(sorted, (long)num * 2 + 1);
            sorted.add(find(sorted, num), num);
        }
        return res;
    }

    private int find(List<Integer> sorted, long target) {
        int high = sorted.size() - 1;
        if (high < 0 || sorted.get(high) < target) return high + 1;

        int low = 0;
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

    // Binary Search Tree
    // beats N/A(326 ms for 35 tests)
    // Time Limit Exceeded(for larger tests)
    public int reversePairs2(int[] nums) {
        int res = 0;
        TreeNode root = null;
        for (int i = nums.length - 1; i >= 0; i--) {
            int target = nums[i];
            if (root != null) {
                res += root.insertNode(target, false);
            }
            if (target < Integer.MIN_VALUE / 2) {
                res += i;
            } else if (target <= Integer.MAX_VALUE / 2) {
                if (root == null) {
                    root = new TreeNode(nums[i] * 2);
                } else {
                    root.insertNode(nums[i] * 2, true);
                }
            }
        }
        return res;
    }

    static class TreeNode {
        private TreeNode left, right;
        private int val;
        private int count = 1;

        public TreeNode(int val) {
            this.val = val;
        }

        public int insertNode(int v, boolean real) {
            int total = 0;
            for (TreeNode root = this;; ) {
                if (v <= root.val) {
                    if (real) {
                        root.count++;
                    }
                    if (root.left == null) {
                        if (real) {
                            root.left = new TreeNode(v);
                        }
                        break;
                    }
                    root = root.left;
                } else {
                    total += root.count;
                    if (root.right == null) {
                        if (real) {
                            root.right = new TreeNode(v);
                        }
                        break;
                    }
                    root = root.right;
                }
            }
            return total;
        }
    }

    // Binary Search Tree
    // beats N/A(289 ms for 35 tests)
    // Time Limit Exceeded(for larger tests)
    public int reversePairs2_2(int[] nums) {
        int res = 0;
        Node root = null;
        for (int i = 0; i < nums.length; i++) {
            int num = nums[i];
            if (num < Integer.MIN_VALUE / 2) {
                res += i;
            } else {
                if (root == null) {
                    root = new Node(num);
                } else {
                    res += root.count((long)num * 2);
                    root.add(num);
                }
            }
        }
        return res;
    }

    private static class Node {
        private Node left, right;
        private int val;
        private int count = 1; // 1 + right children count

        public Node(int val) {
            this.val = val;
        }

        public void add(int num) {
            if (num > val) {
                count++;
                if (right == null) {
                    right = new Node(num);
                } else {
                    right.add(num);
                }
            } else {
                if (left == null) {
                    left = new Node(num);
                } else {
                    left.add(num);
                }
            }
        }

        public int count(long num) {
            int count = 0;
            for (Node node = this; node != null; ) {
                if (node.val <= num) {
                    node = node.right;
                } else {
                    count += node.count;
                    node = node.left;
                }
            }
            return count;
        }
    }

    // Segement Tree
    // TLE or MLE when range is too large
    public int reversePairs3(int[] nums) {
        int min = Integer.MAX_VALUE;
        for (int i : nums) { min = Math.min(min, i); }
        int max = Integer.MIN_VALUE;
        for (int i : nums) { max = Math.max(max, i); }
        SegementTreeNode root = SegementTreeNode.build(min, max);
        for (int num : nums) { root.update(num, 1); }
        int res = 0;
        for (int num : nums) {
            root.update(num, -1);
            if (num >= Integer.MIN_VALUE / 2) {
                res += root.query(min, (num - 1) / 2);
            }
        }
        return res;
    }

    private static class SegementTreeNode {
        private int start, end;
        private int count;
        private SegementTreeNode left, right;

        private SegementTreeNode(int start, int end) {
            this.start = start;
            this.end = end;
        }

        public static SegementTreeNode build(int start, int end) {
            SegementTreeNode root = new SegementTreeNode(start, end);
            if (start < end) {
                int mid = (int)((long)start + ((long)end - start) / 2);
                root.left = build(start, mid);
                root.right = build(mid + 1, end);
            }
            return root;
        }

        public void update(int val, int change) {
            if (start > val || end < val) return;

            if (start == val && end == val) {
                count += change;
                return;
            }
            int mid = (int)((long)start + ((long)end - start) / 2);
            if (val <= mid) {
                left.update(val, change);
            } else {
                right.update(val, change);
            }
            count = left.count + right.count;
        }

        public int query(int start, int end) {
            if (this.start == start && this.end == end) return count;

            int mid = (int)((long)this.start + ((long)this.end - this.start) / 2);
            if (end < mid) return left == null ? 0 : left.query(start, end);

            return left.query(start, mid) + right.query(mid + 1, end);
        }
    }

    // Binary Indexed Tree
    // beats N/A(229 ms for 35 tests)
    // Time Limit Exceeded(for larger tests)
    public int reversePairs4(int[] nums) {
        int count = 0;
        Map<Long, Integer> bit = new HashMap<>();
        final long shift = 1L << 32;
        final long max = shift << 1;
        for (int i = nums.length - 1; i >= 0; i--) {
            for (long j = nums[i] - 1 + shift; j > 0; j -= (j & -j)) {
                count += bit.getOrDefault(j, 0);
            }
            for (long j = (long)nums[i] * 2 + shift; j < max; j += (j & -j)) {
                bit.put(j, bit.getOrDefault(j, 0) + 1);
            }
        }
        return count;
    }

    // Merge Sort
    // beats 50.00%(86 ms for 137 tests)
    public int reversePairs5(int[] nums) {
        return mergeSort(nums, 0, nums.length - 1);
    }

    private int mergeSort(int[] nums, int start, int end) {
        if (start >= end) return 0;

        int mid = (start + end) >>> 1;
        int res = mergeSort(nums, start, mid) + mergeSort(nums, mid + 1, end);
        for (int i = start, j = mid + 1; j <= end; j++) {
            for (long limit = (long)nums[j] * 2; i <= mid && nums[i] <= limit; i++) {}
            if (i <= mid) {
                res += mid - i + 1;
            } else break;
        }
        int[] buffer = new int[end - start + 1];
        for (int i = start, j = mid + 1, k = 0; k <= end - start; k++) {
            buffer[k] = (j > end || i <= mid && nums[i] <= nums[j]) ? nums[i++] : nums[j++];
        }
        System.arraycopy(buffer, 0, nums, start, end - start + 1);
        return res;
    }

    void test(int[] nums, int expected) {
        assertEquals(expected, reversePairs(nums));
        assertEquals(expected, reversePairs1(nums));
        assertEquals(expected, reversePairs2(nums));
        assertEquals(expected, reversePairs2_2(nums));
        assertEquals(expected, reversePairs3(nums));
        assertEquals(expected, reversePairs4(nums));
        assertEquals(expected, reversePairs5(nums));
    }

    @Test
    public void test() {
        test(new int[] {1, 3, 2, 3, 1}, 2);
        test(new int[] {5, 4, 3, 2, 1}, 4);
        test(new int[] {2, 4, 3, 5, 1}, 3);
        test(new int[] {1, 2, 1, 2, 1}, 0);
    }

    void test2(int[] nums, int expected) {
        assertEquals(expected, reversePairs(nums));
        assertEquals(expected, reversePairs1(nums));
        assertEquals(expected, reversePairs2(nums));
        assertEquals(expected, reversePairs2_2(nums));
        // assertEquals(expected, reversePairs3(nums));
        assertEquals(expected, reversePairs4(nums));
        assertEquals(expected, reversePairs5(nums));
    }

    @Test
    public void test2() {
        test2(new int[] {233, 2000000001, 234, 2000000006, 235, 2000000003, 236,
                         2000000007, 237, 2000000002, 2000000005, 233, 233, 233,
                         233, 233, 2000000004}, 40);
        test2(new int[] {2147483647, 2147483647, -2147483647, -2147483647, -2147483647, 2147483647}, 9);
        test2(new int[] {2147483647, 2147483647, 2147483647, 2147483647, 2147483647, 2147483647}, 0);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ReversePairs");
    }
}
