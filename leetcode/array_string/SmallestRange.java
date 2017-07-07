import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC632: https://leetcode.com/problems/smallest-range/
//
// You have k lists of sorted integers in ascending order. Find the smallest range
// that includes at least one number from each of the k lists.
// The range [a,b] is smaller than range [c,d] if b-a < d-c or a < c if b-a == d-c.
// Note:
// The given list may contain duplicates, so ascending order means >= here.
// 1 <= k <= 3500
// -10 ^ 5 <= value of elements <= 10 ^ 5.
public class SmallestRange {
    // Sort
    // time complexity: O(N * K * log(K))
    // Time Limit Exceeded
    public int[] smallestRange0(List<List<Integer> > nums) {
        int k = nums.size();
        final int LIMIT = 100000;
        int left = -LIMIT;
        int right = LIMIT;
        int minRange = right - left;
        int[][] first = new int[k][2];
        for (int i = 0; i < k; i++) {
            first[i] = new int[] {i, 0};
        }
        Comparator<int[]> sorter = new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return nums.get(a[0]).get(a[1]) - nums.get(b[0]).get(b[1]);
            }
        };
        while (true) {
            Arrays.sort(first, sorter);
            int[] a = first[0];
            int[] b = first[k - 1];
            int curLeft = nums.get(a[0]).get(a[1]);
            int curRight = nums.get(b[0]).get(b[1]);
            int range = curRight - curLeft;
            if (range < minRange) {
                minRange = range;
                left = curLeft;
                right = curRight;
            }
            if (++a[1] == nums.get(a[0]).size()) break;
        }
        return new int[] {left, right};
    }

    // Heap
    // time complexity: O(N * log(K))
    // beats 67.60%(82 ms for 86 tests)
    public int[] smallestRange(List<List<Integer> > nums) {
        int k = nums.size();
        int[] res = {Integer.MIN_VALUE / 2, Integer.MAX_VALUE / 2};
        int minRange = Integer.MAX_VALUE;
        PriorityQueue<int[]> pq = new PriorityQueue<>(new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return nums.get(a[0]).get(a[1]) - nums.get(b[0]).get(b[1]);
            }
        });
        int right = Integer.MIN_VALUE;
        for (int i = 0; i < k; i++) {
            pq.offer(new int[] {i, 0});
            right = Math.max(right, nums.get(i).get(0));
        }
        while (true) {
            int[] min = pq.poll();
            int left = nums.get(min[0]).get(min[1]);
            if (right - left < res[1] - res[0]) {
                res = new int[] {left, right};
            }
            if (++min[1] == nums.get(min[0]).size()) break;

            pq.offer(min);
            right = Math.max(right, nums.get(min[0]).get(min[1]));
        }
        return res;
    }

    // Heap
    // time complexity: O(N * log(K))
    // beats 13.52%(181 ms for 86 tests)
    public int[] smallestRange2(List<List<Integer> > nums) {
        int minX = 0;
        int minY = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        int k = nums.size();
        int[] next = new int[k];
        PriorityQueue<Integer> pq = new PriorityQueue<>(
            (i, j) -> nums.get(i).get(next[i]) - nums.get(j).get(next[j]));
        for (int i = 0; i < k; i++) {
            pq.offer(i);
            max = Math.max(max, nums.get(i).get(0));
        }
        outer: for (int i = 0; i < k; i++) {
            for (int j = 0; j < nums.get(i).size(); j++) {
                int curMin = pq.poll();
                if (minY - minX > max - nums.get(curMin).get(next[curMin])) {
                    minX = nums.get(curMin).get(next[curMin]);
                    minY = max;
                }
                if (++next[curMin] == nums.get(curMin).size()) break outer;

                pq.offer(curMin);
                max = Math.max(max, nums.get(curMin).get(next[curMin]));
            }
        }
        return new int[] {minX, minY};
    }

    void test(Integer[][] nums, int[] expected) {
        List<List<Integer> > lists = new ArrayList<>();
        for (Integer[] num : nums) {
            List<Integer> list = Arrays.asList(num);
            lists.add(list);
        }
        assertArrayEquals(expected, smallestRange0(lists));
        assertArrayEquals(expected, smallestRange(lists));
        assertArrayEquals(expected, smallestRange2(lists));
    }

    @Test
    public void test() {
        test(new Integer[][] {{4, 10, 15, 24, 26}, {0, 9, 12, 20}, {5, 18, 22, 30}},
             new int[] {20, 24});
    }

    public static void main(String[] args) {
        String clazz = new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
