import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/range-sum-query-immutable/
//
// Given an integer array nums, find the sum of the elements between indices
// i and j (i <= j), inclusive. The update(i, val) function modifies nums by
// updating the element at index i to val.
public class RangeSumQueryMutable {
    interface INumArray {
        void update(int i, int val);
        int sumRange(int i, int j);
    }

    //  Time Limit Exceeded
    class NumArray implements INumArray {
        private int[] sums;
        private int[] nums;
        private SortedMap<Integer, Integer> diffMap = new TreeMap<>();

        // time complexity: O(N), space complexity: O(N)
        public NumArray(int[] nums) {
            this.nums = nums;
            int n = nums.length;
            sums = new int[n + 1];
            for (int i = 0; i < n; i++) {
                sums[i + 1] = sums[i] + nums[i];
            }
        }

        // time complexity: O(N)
        public int sumRange(int i, int j) {
            int sum = sums[j + 1] - sums[i];
            for (int diff : diffMap.subMap(i, j + 1).values()) {
                sum += diff;
            }
            return sum;
        }

        // time complexity: O(1)
        public void update(int i, int val) {
            int diff = val - nums[i];
            if (diff != 0) {
                nums[i] = val;
                if (diffMap.containsKey(i)) {
                    diff += diffMap.get(i);
                }
                diffMap.put(i, diff);
            }
        }
    }

    //  Time Limit Exceeded
    class NumArray2 implements INumArray {
        private int[] sums;
        private int[] nums;

        // time complexity: O(N), space complexity: O(N)
        public NumArray2(int[] nums) {
            this.nums = nums;
            int n = nums.length;
            sums = new int[n + 1];
            for (int i = 0; i < n; i++) {
                sums[i + 1] = sums[i] + nums[i];
            }
        }

        // time complexity: O(1)
        public int sumRange(int i, int j) {
            return sums[j + 1] - sums[i];
        }

        // time complexity: O(N)
        public void update(int i, int val) {
            int diff = val - nums[i];
            if (diff != 0) {
                nums[i] = val;
                for (int j = i; j < nums.length; j++) {
                    sums[j + 1] += diff;
                }
            }
        }
    }

    // Binary Indexed Tree
    // beats 68.67%(7 ms)
    class NumArray3 implements INumArray {
        private int[] bit;
        private int[] nums;

        // time complexity: O(N * log(N)), space complexity: O(N)
        public NumArray3(int[] nums) {
            int n = nums.length;
            bit = new int[n + 1];
            this.nums = nums;
            for (int i = 0; i < n; i++) {
                add(i, nums[i]);
            }
        }

        // time complexity: O(log(N))
        public int sumRange(int i, int j) {
            return sum(j + 1) - sum(i);
        }

        private int sum(int i) {
            int sum = 0;
            for (int j = i; j > 0; j -= (j & -j)) {
                sum += bit[j];
            }
            return sum;
        }

        // time complexity: O(log(N))
        public void update(int i, int val) {
            add(i, val - nums[i]);
            nums[i] = val;
        }

        private void add(int i, int diff) {
            for (int j = i + 1; j < bit.length; j += (j & -j)) {
                bit[j] += diff;
            }
        }
    }

    // from leetcode
    // Segment Tree
    // beats 38.11%(14 ms)
    class NumArray4 implements INumArray {
        private int[] tree;
        private int n;

        // time complexity: O(N), space complexity: O(N)
        public NumArray4(int[] nums) {
            n = nums.length;
            if (n > 0) {
                tree = new int[n * 2];
                buildTree(nums);
            }
        }

        private void buildTree(int[] nums) {
            for (int i = n, j = 0; i < 2 * n; i++, j++) {
                tree[i] = nums[j];
            }
            for (int i = n - 1; i > 0; i--) {
                tree[i] = tree[i * 2] + tree[i * 2 + 1];
            }
        }

        // time complexity: O(log(N)), space complexity: O(1)
        public void update(int pos, int val) {
            tree[pos += n] = val;
            for (; pos > 0; pos /= 2) {
                int left = pos;
                int right = pos;
                if (pos % 2 == 0) {
                    right = pos + 1;
                } else {
                    left = pos - 1;
                }
                tree[pos / 2] = tree[left] + tree[right];
            }
        }

        // time complexity: O(log(N)), space complexity: O(1)
        public int sumRange(int i, int j) {
            int sum = 0;
            for (int l = i + n, r = j + n; l <= r; l /= 2, r /= 2) {
                if ((l % 2) == 1) {
                    sum += tree[l++];
                }
                if ((r % 2) == 0) {
                    sum += tree[r--];
                }
            }
            return sum;
        }
    }

    // from leetcode
    // Sqrt decomposition
    // beats 38.11%(14 ms)
    class NumArray5 implements INumArray {
        private int[] block;
        private int len;
        private int[] nums;

        // time complexity: O(N), space complexity: O(N ^ (1/2))
        public NumArray5(int[] nums) {
            this.nums = nums;
            int n = nums.length;
            len = (int) Math.ceil(nums.length / Math.sqrt(n));
            block = new int[len];
            for (int i = 0; i < n; i++) {
                block[i / len] += nums[i];
            }
        }

        // time complexity: O(N ^ (1/2))
        public int sumRange(int i, int j) {
            int sum = 0;
            int startBlock = i / len;
            int endBlock = j / len;
            if (startBlock == endBlock) {
                for (int k = i; k <= j; k++) {
                    sum += nums[k];
                }
            } else {
                for (int k = i; k <= (startBlock + 1) * len - 1; k++) {
                    sum += nums[k];
                }
                for (int k = startBlock + 1; k <= endBlock - 1; k++) {
                    sum += block[k];
                }
                for (int k = endBlock * len; k <= j; k++) {
                    sum += nums[k];
                }
            }
            return sum;
        }

        // time complexity: O(1)
        public void update(int i, int val) {
            block[i / len] += val - nums[i];
            nums[i] = val;
        }
    }

    // https://discuss.leetcode.com/topic/29918/17-ms-java-solution-with-segment-tree
    // beats 17.33(16 ms)
    class NumArray6 implements INumArray {
        class SegmentTreeNode {
            int start, end;
            SegmentTreeNode left, right;
            int sum;

            SegmentTreeNode(int start, int end) {
                this.start = start;
                this.end = end;
            }
        }

        SegmentTreeNode root = null;

        public NumArray6(int[] nums) {
            root = buildTree(nums, 0, nums.length - 1);
        }

        private SegmentTreeNode buildTree(int[] nums, int start, int end) {
            if (start > end) return null;

            SegmentTreeNode node = new SegmentTreeNode(start, end);
            if (start == end) {
                node.sum = nums[start];
            } else {
                int mid = start  + (end - start) / 2;
                node.left = buildTree(nums, start, mid);
                node.right = buildTree(nums, mid + 1, end);
                node.sum = node.left.sum + node.right.sum;
            }
            return node;
        }

        public void update(int i, int val) {
            update(root, i, val);
        }

        private void update(SegmentTreeNode root, int pos, int val) {
            if (root.start == root.end) {
                root.sum = val;
            } else {
                int mid = root.start + (root.end - root.start) / 2;
                if (pos <= mid) {
                    update(root.left, pos, val);
                } else {
                    update(root.right, pos, val);
                }
                root.sum = root.left.sum + root.right.sum;
            }
        }

        public int sumRange(int i, int j) {
            return sumRange(root, i, j);
        }

        private int sumRange(SegmentTreeNode root, int start, int end) {
            if (root.end == end && root.start == start) return root.sum;

            int mid = root.start + (root.end - root.start) / 2;
            if (end <= mid) return sumRange(root.left, start, end);

            if (start > mid) return sumRange(root.right, start, end);

            return sumRange(root.right, mid + 1, end)
                   + sumRange(root.left, start, mid);
        }
    }

    void test(INumArray obj, int[] query1, int[] update, int[] query2) {
        assertEquals(query1[2], obj.sumRange(query1[0], query1[1]));
        for (int i = 0; i < update.length - 1; i++) {
            obj.update(update[i], update[++i]);
        }
        assertEquals(query2[2], obj.sumRange(query2[0], query2[1]));
    }

    void test(int[] nums, int[] query1, int[] update, int[] query2) {
        test(new NumArray(nums.clone()), query1, update, query2);
        test(new NumArray2(nums.clone()), query1, update, query2);
        test(new NumArray3(nums.clone()), query1, update, query2);
        test(new NumArray4(nums.clone()), query1, update, query2);
        test(new NumArray5(nums.clone()), query1, update, query2);
        test(new NumArray6(nums.clone()), query1, update, query2);
    }

    @Test
    public void test1() {
        test(new int[] {1, 3, 5},
             new int[] {0, 2, 9}, new int[] {1, 2, 2, 3}, new int[] {0, 2, 6});
        test(new int[] {1, 2, 3, 4, 5},
             new int[] {1, 3, 9}, new int[] {1, 3, 2, 4, 3, 5},
             new int[] {1, 3, 12});
        test(new int[] {7, 2, 7, 2, 0},
             new int[] {0, 2, 16}, new int[] {4, 6, 0, 2, 0, 9, 3, 8},
             new int[] {0, 4, 32});
        test(new int[] {-28, -39, 53, 65, 11, -56, -65, -39, -43, 97},
             new int[] {5, 6, -121}, new int[] {9, 27, 1, -82, 3, -72},
             new int[] {1, 8, -293});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("RangeSumQueryMutable");
    }
}
