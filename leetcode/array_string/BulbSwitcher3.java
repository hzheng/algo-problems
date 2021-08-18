import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1375: https://leetcode.com/problems/bulb-switcher-iii/
//
// There is a room with n bulbs, numbered from 1 to n, arranged in a row from left to right. Initially, all the bulbs
// are turned off. At moment k (for k from 0 to n - 1), we turn on the light[k] bulb. A bulb change color to blue only
// if it is on and all the previous bulbs (to the left) are turned on too.
//
// Return the number of moments in which all turned on bulbs are blue.
// Constraints:
// n == light.length
// 1 <= n <= 5 * 10^4
// light is a permutation of  [1, 2, ..., n]
public class BulbSwitcher3 {
    // SortedMap
    // time complexity: O(N*log(N)), space complexity: O(N)
    // 51 ms(13.72%), 46.9 MB(98.01%) for 63 tests
    public int numTimesAllBlue(int[] light) {
        TreeMap<Integer, Integer> map = new TreeMap<>();
        int res = 0;
        for (int cur : light) {
            Integer prev = map.floorKey(cur);
            int key = cur;
            if (prev != null) {
                int end = map.get(prev);
                if (cur == end + 1) {
                    map.put(prev, cur);
                    key = prev;
                }
            }
            Integer next = map.ceilingKey(cur);
            if (next != null && cur + 1 == next) {
                map.put(key, map.remove(next));
            } else if (key == cur) {
                map.put(key, cur);
            }
            res += (key == 1 && map.size() == 1) ? 1 : 0;
        }
        return res;
    }

    // Hash Table
    // time complexity: O(N), space complexity: O(N)
    // 39 ms(66.67%), 51.6 MB(100%) for 63 tests
    public int numTimesAllBlue2(int[] light) {
        int res = 0;
        Map<Integer, Integer> map = new HashMap<>();
        Map<Integer, Integer> revMap = new HashMap<>();
        for (int cur : light) {
            int max = cur;
            Integer prev = map.get(cur - 1);
            if (prev != null) {
                map.remove(cur - 1);
                map.put(cur, prev);
                revMap.put(prev, cur);
            } else {
                map.put(cur, cur);
                revMap.put(cur, cur);
            }
            Integer next = revMap.get(cur + 1);
            if (next != null) {
                map.put(next, map.get(cur));
                revMap.put(map.remove(cur), next);
                max = next;
            }
            if (map.size() == 1 && map.getOrDefault(max, 0) == 1) {
                res++;
            }
        }
        return res;
    }

    // Solution of Choice
    // time complexity: O(N), space complexity: O(1)
    // 1 ms(100%), 51.2 MB(100%) for 63 tests
    public int numTimesAllBlue3(int[] light) {
        int res = 0;
        for (int i = 0, n = light.length, rightmost = 0; i < n; i++) {
            rightmost = Math.max(rightmost, light[i]);
            if (rightmost == i + 1) {
                res++;
            }
        }
        return res;
    }

    private void test(int[] light, int expected) {
        assertEquals(expected, numTimesAllBlue(light));
        assertEquals(expected, numTimesAllBlue2(light));
        assertEquals(expected, numTimesAllBlue3(light));
    }

    @Test
    public void test() {
        test(new int[]{2, 1, 3, 5, 4}, 3);
        test(new int[]{3, 2, 4, 1, 5}, 2);
        test(new int[]{4, 1, 2, 3}, 1);
        test(new int[]{2, 1, 4, 3, 6, 5}, 3);
        test(new int[]{1, 2, 3, 4, 5, 6}, 6);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
