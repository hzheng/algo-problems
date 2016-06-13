import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/longest-consecutive-sequence/
//
// Given an unsorted array of integers, find the length of the longest
// consecutive elements sequence.
public class  LongestConsecutiveSeq {
    // beats 58.43%
    public int longestConsecutive(int[] nums) {
        Map<Integer, int[]> map = new HashMap<>();
        for (int num : nums) {
            map.put(num, new int[] {num, num});
        }
        Set<Integer> keys = map.keySet();
        int maxLen = 1;
        for (int num : nums) {
            if (!keys.contains(num)) continue;

            int[] range = map.get(num);
            for (int i = num + 1; keys.contains(i); i++) {
                range[1]++;
                map.remove(i);
            }
            for (int i = num - 1; keys.contains(i); i--) {
                range[0]--;
                map.remove(i);
            }
            map.remove(num);
            maxLen = Math.max(maxLen, range[1] - range[0] + 1);
        }
        return maxLen;
    }

    // beats 68.29%
    public int longestConsecutive2(int[] nums) {
        Set<Integer> set = new HashSet<>();
        for (int num : nums) {
            set.add(num);
        }
    
        int maxLen = 1;
        for (int num : nums) {
            if (!set.contains(num)) continue;

            int count = 1;
            for (int i = num + 1; set.contains(i); i++) {
                count++;
                set.remove(i);
            }
            for (int i = num - 1; set.contains(i); i--) {
                count++;
                set.remove(i);
            }
            set.remove(num);
            maxLen = Math.max(maxLen, count);
        }
        return maxLen;
    }

    void test(int expected, int ... nums) {
        assertEquals(expected, longestConsecutive(nums));
        assertEquals(expected, longestConsecutive2(nums));
    }

    @Test
    public void test1() {
        test(4, 100, 4, 200, 1, 3, 2);
        test(5, 100, 4, 101, 98, 1, 99, 3, 2, 102);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("LongestConsecutiveSeq");
    }
}
