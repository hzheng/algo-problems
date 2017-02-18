import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC170: https://leetcode.com/problems/two-sum-iii-data-structure-design/
//
// Design and implement a TwoSum class. It should support the following operations:
// add - Add the number to an internal data structure.
// find - Find if there exists any pair of numbers which sum is equal to the value.
public class TwoSum3 {
    static interface ITwoSum {
        /** Add the number to an internal data structure.. */
        public void add(int number);

        /** Find if there exists any pair of numbers which sum is equal to the value. */
        public boolean find(int value);
    }

    // One Hashtable
    // beats 63.33%(323 ms for 16 tests)
    static class TwoSum implements ITwoSum {
        private Map<Integer, Boolean> map = new LinkedHashMap<>();

        // time complexity: O(1)
        public void add(int number) {
            Boolean old = map.put(number, false);
            if (old != null) {
                map.put(number, true);
            }
        }

        // time complexity: O(N)
        public boolean find(int value) {
            for (Integer key : map.keySet()) {
                Boolean pair = map.get(value - key);
                if (pair != null && (key * 2 != value || pair)) return true;
            }
            return false;
        }
    }

    // 2 Hashtables
    // Time Limit Exceeded
    static class TwoSum2 implements ITwoSum {
        private Set<Integer> nums = new HashSet<>();
        private Set<Integer> sums = new HashSet<>();

        // time complexity: O(N)
        public void add(int number) {
            for (int num : nums) {
                sums.add(num + number);
            }
            nums.add(number);
        }

        // time complexity: O(1)
        public boolean find(int value) {
            return sums.contains(value);
        }
    }

    void test1(ITwoSum obj) {
        obj.add(1);
        obj.add(3);
        obj.add(4);
        assertEquals(false, obj.find(6));
        obj.add(3);
        assertEquals(false, obj.find(8));
        assertEquals(true, obj.find(6));
        obj.add(5);
        assertEquals(true, obj.find(8));
    }

    @Test
    public void test1() {
        test1(new TwoSum());
        test1(new TwoSum2());
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("TwoSum3");
    }
}
