import org.junit.Test;
import static org.junit.Assert.*;

// LC852: https://leetcode.com/problems/peak-index-in-a-mountain-array/
//
// Let's call an array A a mountain if the following properties hold:
// A.length >= 3
// There exists some 0 < i < A.length - 1 such that A[0] < A[1] < ... A[i-1] <
// A[i] > A[i+1] > ... > A[A.length - 1]
// Given an array that is definitely a mountain, return such i.
public class PeakIndexInMountainArra {
    // time complexity: O(N), space complexity: O(1)
    // beats 35.46%(3 ms for 32 tests)
    public int peakIndexInMountainArray(int[] A) {
        for (int i = 0; ; i++) {
            if (A[i + 1] < A[i]) return i;
        }
    }

    // Binary Search
    // time complexity: O(log(N)), space complexity: O(1)
    // beats 99.80%(2 ms for 32 tests)
    public int peakIndexInMountainArray2(int[] A) {
        int low = 0;
        for (int high = A.length - 1; low < high; ) {
            int mid = (low + high) >>> 1;
            if (A[mid] < A[mid + 1]) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low;
    }

    void test(int[] A, int expected) {
        assertEquals(expected, peakIndexInMountainArray(A));
        assertEquals(expected, peakIndexInMountainArray2(A));
    }

    @Test
    public void test() {
        test(new int[] {0, 1, 0}, 1);
        test(new int[] {0, 2, 1, 0}, 1);
        test(new int[] {1, 2, 3, 4, 1, 0}, 3);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
