import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1488: https://leetcode.com/problems/avoid-flood-in-the-city/
//
// Your country has an infinite number of lakes. Initially, all the lakes are empty, but when it
// rains over the nth lake, the nth lake becomes full of water. If it rains over a lake which is
// full of water, there will be a flood. Your goal is to avoid the flood in any lake.
// Given an integer array rains where:
// rains[i] > 0 means there will be rains over the rains[i] lake.
// rains[i] == 0 means there are no rains this day and you can choose one lake this day and dry it.
// Return an array ans where:
// ans.length == rains.length
// ans[i] == -1 if rains[i] > 0.
// ans[i] is the lake you choose to dry in the ith day if rains[i] == 0.
// If there are multiple valid answers return any of them. If it is impossible to avoid flood return
// an empty array.
// Notice that if you chose to dry a full lake, it becomes empty, but if you chose to dry an empty
// lake, nothing changes. (see example 4)
// Constraints:
//
// 1 <= rains.length <= 10^5
// 0 <= rains[i] <= 10^9
public class AvoidFlood {
    // Hash Table + SortedSet
    // time complexity: O(N*log(N)), space complexity: O(N)
    // 61 ms(94.93%), 56.3 MB(5.25%) for 78 tests
    public int[] avoidFlood(int[] rains) {
        int n = rains.length;
        int[] res = new int[n];
        Map<Integer, Integer> full = new HashMap<>();
        NavigableSet<Integer> dryDays = new TreeSet<>();
        for (int day = 0; day < n; day++) {
            int lake = rains[day];
            if (lake == 0) {
                dryDays.add(day);
                res[day] = 1;
            } else {
                res[day] = -1;
                Integer lastRainDay = full.put(lake, day);
                if (lastRainDay == null) { continue; }

                Integer dryDay = dryDays.ceiling(lastRainDay);
                if (dryDay == null) { return new int[0]; }

                dryDays.remove(dryDay); // use the first available day to maximize avoid possibility
                res[dryDay] = lake;
            }
        }
        return res;
    }

    void test(int[] rains, int[] expected) {
        assertArrayEquals(expected, avoidFlood(rains));
    }

    @Test public void test() {
        test(new int[] {1, 2, 3, 4}, new int[] {-1, -1, -1, -1});
        test(new int[] {1, 2, 0, 0, 2, 1}, new int[] {-1, -1, 2, 1, -1, -1});
        test(new int[] {1, 2, 0, 1, 2}, new int[] {});
        test(new int[] {69, 0, 0, 0, 69}, new int[] {-1, 69, 1, 1, -1});
        test(new int[] {10, 20, 20}, new int[] {});
        test(new int[] {0, 1, 1}, new int[] {});
        test(new int[] {1, 1, 0, 0}, new int[] {});
        test(new int[] {1, 0, 2, 3, 0, 1, 2}, new int[] {-1, 1, -1, -1, 2, -1, -1});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
