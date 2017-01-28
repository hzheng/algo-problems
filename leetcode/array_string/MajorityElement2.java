import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// LC229: https://leetcode.com/problems/majority-element-ii/
//
// Given an integer array of size n, find all elements that appear more than
// ⌊ n/3 ⌋ times. The algorithm should run in linear time and in O(1) space.
public class MajorityElement2 {
    // Solution of Choice
    // Boyer-Moore Majority Vote algorithm
    // beats 37.83%(4 ms for 66 tests)
    // time complexity: O(N), space complexity: O(1)
    public List<Integer> majorityElement(int[] nums) {
        int major1 = 0;
        int count1 = 0;
        int major2 = 0;
        int count2 = 0;
        for (int num : nums) {
            if (num == major1) {
                count1++;
            } else if (num == major2) {
                count2++;
            } else if (count1 == 0) {
                major1 = num;
                count1 = 1;
            } else if (count2 == 0) {
                major2 = num;
                count2 = 1;
            } else {
                count1--;
                count2--;
            }
        }

        List<Integer> majors = new ArrayList<>();
        count1 = 0;
        count2 = 0;
        for (int num : nums) {
            if (num == major1) {
                count1++;
            } else if (num == major2) {
                count2++;
            }
        }
        if (count1 > nums.length / 3) {
            majors.add(major1);
        }
        if (count2 > nums.length / 3) {
            majors.add(major2);
        }
        return majors;
    }

    // General version
    // beats 27.19%(5 ms for 66 tests)
    public List<Integer> majorityElement2(int[] nums) {
        final int N = 3;
        int[] majors = new int[N - 1];
        int[] counts = new int[N - 1];
outerLoop:
        for (int num : nums) {
            for (int i = N - 2; i >= 0; i--) {
                if (num == majors[i]) {
                    counts[i]++;
                    continue outerLoop;
                }
            }
            for (int i = N - 2; i >= 0; i--) {
                if (counts[i] == 0) {
                    counts[i] = 1;
                    majors[i] = num;
                    continue outerLoop;
                }
            }
            for (int i = N - 2; i >= 0; i--) {
                counts[i]--;
            }
        }

        List<Integer> res = new ArrayList<>();
        Arrays.fill(counts, 0);
        for (int num : nums) {
            for (int i = N - 2; i >= 0; i--) {
                if (num == majors[i]) {
                    counts[i]++;
                    break;
                }
            }
        }
        for (int i = N - 2; i >= 0; i--) {
            if (counts[i] > nums.length / N) {
                res.add(majors[i]);
            }
        }
        return res;
    }

    void test(Function<int[], List<Integer> > major,
              Integer[] expected, int ... nums) {
        List<Integer> res = major.apply(nums);
        Collections.sort(res);
        if (expected == null) {
            assertNull(majorityElement(nums));
        } else {
            assertArrayEquals(expected, res.toArray(new Integer[0]));
        }
    }

    void test(Integer[] expected, int ... nums) {
        MajorityElement2 m = new MajorityElement2();
        test(m::majorityElement, expected, nums);
        test(m::majorityElement2, expected, nums);
    }

    @Test
    public void test1() {
        test(new Integer[0], 0, 1, 2);
        test(new Integer[] {1}, -1, 1, 1, 1, 2, 1);
        test(new Integer[] {100}, -1, 100, 2, 100, 100, 4, 100);
        test(new Integer[0], 1, 2, 3);
        test(new Integer[] {5}, 5);
        test(new Integer[] {5}, 5, 5);
        test(new Integer[] {5, 6}, 6, 5);
        test(new Integer[] {5, 6}, 6, 5, 5, 6);
        test(new Integer[] {3, 7}, 3, 7, 3, 7, 3, 7, 8, 7, 3, 7, 3, 3);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MajorityElement2");
    }
}
