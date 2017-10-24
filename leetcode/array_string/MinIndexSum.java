import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC599: https://leetcode.com/problems/minimum-index-sum-of-two-lists/
//
// Suppose Andy and Doris want to choose a restaurant for dinner, and they both
// have a list of favorite restaurants represented by strings. You need to help
// them find out their common interest with the least list index sum. If there is
// a choice tie between answers, output all of them with no order requirement. You
// could assume there always exists an answer.
// Note:
// The length of both lists will be in the range of [1, 1000].
// The length of strings in both lists will be in the range of [1, 30].
// The index is starting from 0 to the list length minus 1.
// No duplicates in both lists.
public class MinIndexSum {
    // Hash Table
    // beats 90.99%(28 ms for 133 tests)
    // time complexity: O(L1 + L2)
    public String[] findRestaurant(String[] list1, String[] list2) {
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < list1.length; i++) {
            map.put(list1[i], i);
        }
        List<String> res = new ArrayList<>();
        int min = list1.length + list2.length;
        for (int i = 0; i < list2.length; i++) {
            String restaurant = list2[i];
            Integer index = map.get(restaurant);
            if (index == null || index + i > min) continue;

            if (index + i < min) {
                min = index + i;
                res.clear();
            }
            res.add(restaurant);
        }
        return res.toArray(new String[0]);
    }

    // beats 18.02%(74 ms for 133 tests)
    // time complexity: O((L1 + L2) ^ 2 * S)
    public String[] findRestaurant2(String[] list1, String[] list2) {
        List<String> res = new ArrayList<>();
        for (int sum = 0, n1 = list1.length, n2 = list2.length;
             sum < n1 + n2 - 1; sum++) {
            for (int i = Math.max(0, sum - n2 + 1); i <= Math.min(n1 - 1, sum);
                 i++) {
                if (list1[i].equals(list2[sum - i])) {
                    res.add(list1[i]);
                }
            }
            if (res.size() > 0) break;
        }
        return res.toArray(new String[res.size()]);
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(String[] list1, String[] list2, String[] expected,
              Function<String[], String[], String[]> findRestaurant) {
        Arrays.sort(expected);
        String[] res = findRestaurant.apply(list1, list2);
        Arrays.sort(res);
        assertArrayEquals(expected, res);
    }

    void test(String[] list1, String[] list2, String[] expected) {
        MinIndexSum m = new MinIndexSum();
        test(list1, list2, expected, m::findRestaurant);
        test(list1, list2, expected, m::findRestaurant2);
    }

    @Test
    public void test() {
        test(new String[] {"Shogun", "Tapioca Express", "Burger King", "KFC"},
             new String[] {"KFC", "Shogun", "Burger King"}, 
             new String[] {"Shogun"});
        test(new String[] {"Shogun", "Tapioca", "Burger King", "KFC"},
             new String[] {"Tapioca", "Shogun", "Burger King"},
             new String[] {"Tapioca", "Shogun"});
        test(new String[] {"Shogun", "Tapioca Express", "Burger King", "KFC"},
             new String[] {"KNN", "KFC", "Burger King", "Tapioca Express",
                           "Shogun"},
             new String[] {"Shogun", "KFC", "Burger King", "Tapioca Express"});
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
