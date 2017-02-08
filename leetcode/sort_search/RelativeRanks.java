import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC506: https://leetcode.com/problems/relative-ranks/
//
// Given scores of N athletes, find their relative ranks and the people with the
// top three highest scores, who will be awarded medals: "Gold Medal",
// "Silver Medal" and "Bronze Medal".
public class RelativeRanks {
    private static final String[] TOPS = {"Gold Medal", "Silver Medal", "Bronze Medal"};

    // Heap
    // time complexity: O(N * log(N))
    // beats 61.32%(21 ms for 17 tests)
    public String[] findRelativeRanks(int[] nums) {
        int n = nums.length;
        PriorityQueue<int[]> pq = new PriorityQueue<>(new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return b[0] - a[0];
            }
        });
        for (int i = 0; i < n; i++) {
            pq.offer(new int[] {nums[i], i});
        }
        String[] res = new String[n];
        for (int i = 0; i < n; i++) {
            res[pq.poll()[1]] = i < 3 ? TOPS[i] : String.valueOf(i + 1);
        }
        return res;
    }

    // Sort
    // time complexity: O(N * log(N))
    // beats 67.11%(20 ms for 17 tests)
    public String[] findRelativeRanks2(int[] nums) {
        int n = nums.length;
        Integer[] indices = new Integer[n];
        for (int i = 0; i < n; i++) {
            indices[i] = i;
        }
        Arrays.sort(indices, new Comparator<Integer>() {
            public int compare(Integer i, Integer j) {
                return nums[j] - nums[i];
            }
        });
        String[] res = new String[n];
        for (int i = 0; i < nums.length; i++) {
            res[indices[i]] = i < 3 ? TOPS[i] : String.valueOf(i + 1);
        }
        return res;
    }

    // Sort + Hash Table
    // time complexity: O(N * log(N))
    // beats 92.37%(16 ms for 17 tests)
    public String[] findRelativeRanks3(int[] nums) {
        int[] ranks = nums.clone();
        Arrays.sort(ranks);
        Map<Integer, Integer> map = new HashMap<>();
        int n = nums.length;
        for (int i = 0; i < n; i++) {
            map.put(ranks[i], n - i);
        }
        String[] res = new String[n];
        for (int i = 0; i < n; i++) {
            int rank = map.get(nums[i]);
            res[i] = rank < 4 ? TOPS[rank - 1] : String.valueOf(rank);
        }
        return res;
    }

    // brute-force
    // time complexity: O(N ^ 2)
    // beats 5.00%(142 ms for 17 tests)
    public String[] findRelativeRanks4(int[] nums) {
        int n = nums.length;
        int[] ranks = new int[n];
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                ranks[nums[i] > nums[j] ? j : i]++;
            }
        }
        String[] res = new String[n];
        for (int i = 0; i < n; i++) {
            int rank = ranks[i];
            res[i] = rank < 3 ? TOPS[rank] : String.valueOf(rank + 1);
        }
        return res;
    }

    void test(int[] nums, String ... expected) {
        assertArrayEquals(expected, findRelativeRanks(nums));
        assertArrayEquals(expected, findRelativeRanks2(nums));
        assertArrayEquals(expected, findRelativeRanks3(nums));
        assertArrayEquals(expected, findRelativeRanks4(nums));
    }

    @Test
    public void test() {
        test(new int[] {10, 3, 8, 9, 4}, "Gold Medal","5","Bronze Medal","Silver Medal","4");
        test(new int[] {5, 4, 3, 2, 1}, "Gold Medal", "Silver Medal", "Bronze Medal", "4", "5");
        test(new int[] {4, 5, 3, 2, 1}, "Silver Medal", "Gold Medal", "Bronze Medal", "4", "5");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("RelativeRanks");
    }
}
