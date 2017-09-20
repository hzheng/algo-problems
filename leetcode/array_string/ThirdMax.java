import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC414: https://leetcode.com/problems/third-maximum-number/
//
// Given a non-empty array of integers, return the third maximum number in this
// array. If it does not exist, return the maximum number.
public class ThirdMax {
    // Sorted Set
    // beats 5.36%(24 ms for 26 tests)
    public int thirdMax(int[] nums) {
        SortedSet<Integer> candidates = new TreeSet<>();
        for (int num : nums) {
            candidates.add(num);
            if (candidates.size() > 3) {
                candidates.remove(candidates.first());
            }
        }
        return (candidates.size() == 3) ? candidates.first() : candidates.last();
    }

    // beats 30.78%(7 ms for 26 tests)
    public int thirdMax2(int[] nums) {
        int[] max = new int[3];
        int count = 0;
        for (int num : nums) {
            if (count == 0) {
                max[0] = num;
                count++;
            } else if (count == 1) {
                if (num != max[0]) {
                    if (num > max[0]) {
                        max[1] = num;
                    } else {
                        max[1] = max[0];
                        max[0] = num;
                    }
                    count++;
                }
            } else if (count == 2) {
                if (num != max[0] && num != max[1]) {
                    max[2] = num;
                    count++;
                    Arrays.sort(max);
                }
            } else if (num > max[0] && num != max[1] && num != max[2]) {
                max[0] = num;
                Arrays.sort(max);
            }
        }
        return count == 2 ? max[1] : max[0];
    }

    // beats 47.70%(4 ms for 26 tests)
    public int thirdMax3(int[] nums) {
        long max1 = Long.MIN_VALUE;
        long max2 = max1;
        long max3 = max1;
        for (int num : nums) {
            if (num > max1) {
                max3 = max2;
                max2 = max1;
                max1 = num;
            } else if (max1 > num && num > max2) {
                max3 = max2;
                max2 = num;
            } else if (max2 > num && num > max3) {
                max3 = num;
            }
        }
        return (int)(max3 != Long.MIN_VALUE ? max3 : max1);
    }

    void test(int[] nums, int expected) {
        assertEquals(expected, thirdMax(nums));
        assertEquals(expected, thirdMax2(nums));
        assertEquals(expected, thirdMax3(nums));
    }

    @Test
    public void test1() {
        test(new int[] {1, 2}, 2);
        test(new int[] {3, 2, 1}, 1);
        test(new int[] {5, 2, 2}, 5);
        test(new int[] {2, 2, 3, 1}, 1);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ThirdMax");
    }
}
