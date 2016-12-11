import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC475: https://leetcode.com/problems/heaters/
//
// You are given positions of houses and heaters on a horizontal line, find out
// minimum radius of heaters so that all houses could be covered by those heaters.
// So, your input will be the positions of houses and heaters seperately, and
// your expected output will be the minimum radius standard of heaters.
// Note:
// Numbers of houses and heaters you are given are non-negative and will not exceed 25000.
// Positions of houses and heaters you are given are non-negative and will not exceed 10^9.
// As long as a house is in the heaters' warm radius range, it can be warmed.
// All the heaters follow your radius standard and the warm radius will the same.
public class Heaters {
    // beats N/A(28 ms for 27 tests)
    public int findRadius(int[] houses, int[] heaters) {
        Arrays.sort(houses);
        Arrays.sort(heaters);
        int start = 0;
        int radius = 0;
        int n = heaters.length;
        for (int house : houses) {
            int pos = Arrays.binarySearch(heaters, start, n, house);
            if (pos >= 0) continue;

            pos = -pos - 1;
            if (pos == n) return Math.max(radius, houses[houses.length - 1] - heaters[n - 1]);

            if (pos == start) {
                radius = Math.max(radius, heaters[start] - house);
            } else {
                radius = Math.max(radius,
                                  Math.min(house - heaters[pos - 1], heaters[pos] - house));
                start = pos - 1;
            }
        }
        return radius;
    }

    // beats N/A(44 ms for 27 tests)
    public int findRadius2(int[] houses, int[] heaters) {
        int m = houses.length;
        if (m == 0) return 0;

        int n = heaters.length;
        Arrays.sort(houses);
        Arrays.sort(heaters);
        int low = 0;
        // int high = 1000_000_000;
        int high = Math.max(Math.abs(houses[0] - heaters[n - 1]),
                            Math.abs(houses[m - 1] - heaters[0]));
        while (low <= high) {
            int i = 0;
            int j = 0;
            int mid = (low + high) >>> 1;
            while (i < m && j < n) {
                if (Math.abs(houses[i] - heaters[j]) <= mid) {
                    i++;
                } else {
                    j++;
                }
            }
            if (i >= m) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return low;
    }

    void test(int[] houses, int[] heaters, int expected) {
        assertEquals(expected, findRadius(houses.clone(), heaters.clone()));
        assertEquals(expected, findRadius2(houses.clone(), heaters.clone()));
    }

    @Test
    public void test() {
        test(new int[] {}, new int[] {1}, 0);
        test(new int[] {282475249, 622650073, 984943658, 144108930, 470211272,
                        101027544, 457850878, 458777923},
             new int[] {823564440, 115438165, 784484492, 74243042, 114807987,
                        137522503, 441282327, 16531729, 823378840, 143542612}, 161834419);
        test(new int[] {474833169, 264817709, 998097157, 817129560},
             new int[] {197493099, 404280278, 893351816, 505795335}, 104745341);
        test(new int[] {8}, new int[] {1}, 7);
        test(new int[] {1, 2, 3}, new int[] {2}, 1);
        test(new int[] {1, 2, 3, 4}, new int[] {1, 4}, 1);
        test(new int[] {1, 2, 3, 4}, new int[] {1, 4}, 1);
        test(new int[] {3}, new int[] {2, 6, 9}, 1);
        test(new int[] {2, 6, 9}, new int[] {3}, 6);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("Heaters");
    }
}
