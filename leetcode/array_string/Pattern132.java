import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC456: https://leetcode.com/problems/132-pattern/
//
// Given a sequence of n integers a1, a2, ..., an, a 132 pattern is a subsequence
// ai, aj, ak such that i < j < k and ai < ak < aj. Design an algorithm that
// takes a list of n numbers as input and checks whether there is a 132 pattern
// in the list.
public class Pattern132 {
    // SortedMap
    // beats N/A(554 ms for 87 tests)
    public boolean find132pattern(int[] nums) {
        int n = nums.length;
        SortedMap<Integer, Integer> ranges = new TreeMap<>();
        for (int i = 0; i < n; i++) {
            ranges.put(nums[i], i);
        }
        int one = Integer.MAX_VALUE;
        int three = Integer.MIN_VALUE;
        for (int i = 1; i < n; ) {
            for (; i < n; i++) {
                if (nums[i] > one && nums[i] < three) return true;
                if (nums[i] > nums[i - 1]) break;
            }
            if (i == n) return false;

            int nextOne = nums[i - 1];
            for (i++; i < n; i++) {
                if (nums[i] < nums[i - 1]) break;
            }
            one = nextOne;
            three = nums[i - 1];
            for (Integer index : ranges.subMap(one + 1, three).values()) {
                if (index > i) return true;
            }
        }
        return false;
    }

    // SortedSet
    // beats N/A(44 ms for 87 tests)
    public boolean find132pattern2(int[] nums) {
        NavigableSet<Integer> set = new TreeSet<>();
        int n = nums.length;
        int[] min = new int[n + 1];
        min[0] = Integer.MAX_VALUE;
        for (int i = 0; i < n; i++) {
            min[i + 1] = Math.min(min[i], nums[i]);
        }
        for (int i = n - 1; i >= 0; set.add(nums[i--])) {
            if (!set.subSet(min[i + 1], false, nums[i], false).isEmpty()) return true;
        }
        return false;
    }

    // SortedMap
    // beats N/A(66 ms for 87 tests)
    public boolean find132pattern3(int[] nums) {
        NavigableMap<Integer, Integer> map = new TreeMap<>();
        for (int num : nums) {
            map.put(num, map.getOrDefault(num, 0) + 1);
        }
        int min = Integer.MAX_VALUE;
        for (int num : nums) {
            int count = map.get(num);
            if (count > 1) {
                map.put(num, count - 1);
            } else if (count == 1) {
                map.remove(num);
            }
            if (num <= min) {
                min = num;
            } else if (!map.subMap(min + 1, num).isEmpty()) return true;
        }
        return false;
    }

    // Stack
    // beats N/A(27 ms for 87 tests)
    public boolean find132pattern4(int[] nums) {
        int min = Integer.MAX_VALUE;
        Stack<int[]> stack = new Stack<>();
        for (int num : nums) {
            if (num <= min) {
                min = num; // crucial!
                continue;
            }
            for (; !stack.isEmpty(); stack.pop()) {
                if (num <= stack.peek()[0]) break;
                if (num < stack.peek()[1]) return true;
            }
            stack.push(new int[] {min, num});
        }
        return false;
    }

    // beats N/A(196 ms for 87 tests)
    public boolean find132pattern5(int[] nums) {
        int n = nums.length;
        for (int low = 0, high; low < n - 1; low = high + 1) {
            for (; low < n - 1 && nums[low] >= nums[low + 1]; low++) {}
            high = low + 1;
            for (; high < n - 1 && nums[high] <= nums[high + 1]; high++) {}
            for (int i = high + 1; i < n; i++) {
                if (nums[i] > nums[low] && nums[i] < nums[high]) return true;
            }
        }
        return false;
    }

    // Set
    // Time Limit Exceeded
    public boolean find132pattern0(int[] nums) {
        int n = nums.length;
        Set<int[]> ranges = new HashSet<>();
        for (int i = 1; i < n; ) {
            for (int[] range : ranges) {
                if (nums[i] > range[0] && nums[i] < range[1]) return true;
            }
            for (; i < n; i++) {
                if (nums[i] > nums[i - 1]) break;
            }
            int one = nums[i - 1];
            for (;; i++) {
                if (i == n) return false;
                for (int[] range : ranges) {
                    if (nums[i] > range[0] && nums[i] < range[1]) return true;
                }
                if (nums[i] < nums[i - 1]) break;
            }
            ranges.add(new int[] {one, nums[i - 1]});
        }
        return false;
    }

    void test(int[] nums, boolean expected) {
        assertEquals(expected, find132pattern0(nums));
        assertEquals(expected, find132pattern(nums));
        assertEquals(expected, find132pattern2(nums));
        assertEquals(expected, find132pattern3(nums));
        assertEquals(expected, find132pattern4(nums));
        assertEquals(expected, find132pattern5(nums));
    }

    @Test
    public void test() {
        test(new int[] {}, false);
        test(new int[] {1}, false);
        test(new int[] {1, 2}, false);
        test(new int[] {1, 0, 1, -4, -3}, false);
        test(new int[] {1, 2, 3, 4}, false);
        test(new int[] {3, 1, 4, 2}, true);
        test(new int[] {4, 1, 3, 2}, true);
        test(new int[] {3, 5, 0, 3, 4}, true);
        test(new int[] {3, 5, 0, 3, 1}, true);
        test(new int[] {-1, 10, -20, -10, -15}, true);
        test(new int[] {-2, 1, 2, -2, 1, 2}, true);
        test(new int[] {-1, 3, 2, 0}, true);
        test(new int[] {
            250000000, 250050000, 249900000, 249950000, 249800000, 249850000,
            249700000, 249750000, 249600000, 249650000, 249500000, 249550000,
            249400000, 249450000, 249300000, 249350000, 249200000, 249250000,
            249100000, 249150000, 249000000, 249050000, 248900000, 248950000,
            248800000, 248850000, 248700000, 248750000, 248600000, 248650000,
            248500000, 248550000, 248400000, 248450000, 248300000, 248350000,
            248200000, 248250000, 248100000, 248150000, 248000000, 248050000,
            247900000, 247950000, 247800000, 247850000, 247700000, 247750000,
            247600000, 247650000, 247500000, 247550000, 247400000, 247450000,
            247300000, 247350000, 247200000, 247250000, 247100000, 247150000,
            247000000,  247050000, 246900000, 246950000, 246800000, 246850000,
            246700000, 246750000, 246600000, 246650000, 246500000, 246550000,
            246400000, 246450000, 246300000, 246350000, 246200000, 246250000,
            246100000, 246150000, 246000000, 246050000, 245900000, 245950000,
            245800000, 245850000, 245700000, 245750000, 245600000, 245650000,
            245500000, 245550000, 245400000, 245450000, 245300000, 245350000,
            245200000, 245250000, 245100000, 245150000, 245000000, 245050000,
            244900000, 244950000, 244800000, 244850000, 244700000, 244750000,
            244600000, 244650000, 244500000, 244550000, 244400000, 244450000,
            244300000, 244350000, 244200000, 244250000, 244100000, 244150000,
            244000000, 244050000, 243900000, 243950000, 243800000, 243850000,
            243700000, 243750000, 243600000, 243650000, 243500000, 243550000,
            243400000, 243450000, 243300000, 243350000, 243200000, 243250000,
            243100000, 243150000, 243000000, 243050000, 242900000, 242950000,
            242800000, 242850000, 242700000, 242750000, 242600000, 242650000,
            242500000, 242550000, 242400000, 242450000, 242300000, 242350000,
            242200000, 242250000, 242100000, 242150000, 242000000, 242050000,
            241900000, 241950000, 241800000, 241850000, 241700000, 241750000,
            241600000, 241650000, 241500000, 241550000, 241400000, 241450000,
            241300000, 241350000, 241200000, 241250000, 241100000, 241150000,
            241000000, 241050000, 240900000, 240950000, 240800000, 240850000,
            240700000, 240750000, 240600000, 240650000, 240500000, 240550000,
            240400000, 240450000, 240300000, 240350000, 240200000, 240250000,
            240100000, 240150000, 240000000, 240050000, 239900000, 239950000,
            239800000, 239850000, 239700000, 239750000, 239600000, 239650000,
            239500000, 239550000, 239400000, 239450000, 239300000, 239350000,
            239200000, 239250000, 239100000, 239150000, 239000000, 239050000,
            238900000, 238950000, 238800000, 238850000, 238700000, 238750000,
            238650000, 238500000, 238550000, 238400000, 238450000, 238300000,
            238350000, 238200000, 238250000, 238100000, 238150000, 238000000,
            238050000, 237900000, 237950000, 237800000, 237850000, 237700000,
            237750000, 237600000, 237650000, 237500000, 237550000, 237400000,
            237450000, 237300000, 237350000, 237200000, 237250000, 237100000,
            237150000, 237000000, 237050000, 236900000, 236950000, 236800000,
            236850000, 236700000, 236750000, 236600000, 236650000, 236500000,
            236550000, 236400000, 236450000, 236300000, 236350000, 236200000,
            236250000, 236100000, 236150000, 236000000, 236050000, 235900000,
            235950000, 235800000, 235850000, 235700000, 235750000, 235600000
        }, false);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("Pattern132");
    }
}
