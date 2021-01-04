import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1710: https://leetcode.com/problems/maximum-units-on-a-truck/
//
// You are assigned to put some amount of boxes onto one truck. You are given a 2D array boxTypes,
// where boxTypes[i] = [numberOfBoxesi, numberOfUnitsPerBoxi]:
// numberOfBoxesi is the number of boxes of type i.
// numberOfUnitsPerBoxi is the number of units in each box of the type i.
// You are also given an integer truckSize, which is the maximum number of boxes that can be put on
// the truck. You can choose any boxes to put on the truck as long as the number of boxes does not
// exceed truckSize.
// Return the maximum total number of units that can be put on the truck.
//
// Constraints:
// 1 <= boxTypes.length <= 1000
// 1 <= numberOfBoxesi, numberOfUnitsPerBoxi <= 1000
// 1 <= truckSize <= 10^6
public class MaximumUnits {
    // Greedy + Sort
    // time complexity: O(N*log(N)), space complexity: O(N)
    // 7 ms(98.54%), 39.5 MB(78.75%) for 75 tests
    public int maximumUnits(int[][] boxTypes, int truckSize) {
        Arrays.sort(boxTypes, (a, b) -> (b[1] - a[1]));
        int res = 0;
        for (int[] box : boxTypes) {
            int num = Math.min(truckSize, box[0]);
            res += num * box[1];
            truckSize -= num;
            if (truckSize == 0) { break; }
        }
        return res;
    }

    private void test(int[][] boxTypes, int truckSize, int expected) {
        assertEquals(expected, maximumUnits(boxTypes, truckSize));
    }

    @Test public void test() {
        test(new int[][] {{1, 3}, {2, 2}, {3, 1}}, 4, 8);
        test(new int[][] {{5, 10}, {2, 5}, {4, 7}, {3, 9}}, 10, 91);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
