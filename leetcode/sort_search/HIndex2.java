import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/h-index-ii/
//
// Follow up for H-Index: What if the citations array is sorted in ascending
// order? Could you optimize your algorithm?
public class HIndex2 {
    // time complexity: O(N), space complexity: O(1)
    // beats 22.03%(16 ms)
    public int hIndex(int[] citations) {
        int n = citations.length;
        for (int i = n - 1; i >= 0; i--) {
            int citation = citations[i];
            if (citation < (n - i)) return n - i - 1;
        }
        return n;
    }

    // time complexity: O(log(N)), space complexity: O(1)
    // beats 48.27%(12 ms)
    public int hIndex2(int[] citations) {
        int n = citations.length;
        int low = 0;
        int high = n - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            int citation = citations[mid];
            if (citation < (n - mid)) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return n - low;
    }

    void test(int expected, int ... citations) {
        assertEquals(expected, hIndex(citations));
        assertEquals(expected, hIndex2(citations));
    }

    @Test
    public void test1() {
        test(0, 0);
        test(0, 0, 0);
        test(3, 0, 1, 3, 5, 6);
        test(3, 0, 1, 3, 4, 5, 6);
        test(4, 5, 5, 5, 5);
        test(1, 0, 1);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("HIndex2");
    }
}
