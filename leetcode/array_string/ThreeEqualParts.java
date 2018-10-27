import org.junit.Test;
import static org.junit.Assert.*;

// LC927: https://leetcode.com/problems/three-equal-parts/
//
// Given an array A of 0s and 1s, divide the array into 3 non-empty parts such 
// that all of these parts represent the same binary value. 
// If it is not possible, return [-1, -1].
// Note:
// 3 <= A.length <= 30000
// A[i] == 0 or A[i] == 1
public class ThreeEqualParts {
    static final int[] NONE = new int[] {-1, -1};

    // time complexity: O(N), space complexity: O(1)
    // beats 89.68%(10 ms for 104 tests)
    public int[] threeEqualParts(int[] A) {
        int ones = 0;
        for (int a : A) {
            ones += a;
        }
        if (ones % 3 != 0) return NONE;

        int one = ones / 3;
        int n = A.length;
        if (one == 0) return new int[] {0, n - 1};

        int i0 = -1; // first nonzero in the 1st partition
        int i1 = -1; // first nonzero in the 2nd partition
        int i2 = -1; // first nonzero in the 3rd partition
        for (int i = 0, count = 0;; i++) {
            count += A[i];
            if (count == 1 && i0 < 0) {
                i0 = i;
            } else if (count == one + 1 && i1 < 0) {
                i1 = i;
            } else if (count == one * 2 + 1 && i2 < 0) {
                i2 = i;
                break;
            }
        }
        int len = n - i2;
        if ((i1 - i0 < len) || (i2 - i1 < len)) return NONE;

        for (int i = i0, j = i2; j < n; i++, j++) {
            if (A[i] != A[j]) return NONE;
        }
        for (int i = i1, j = i2; j < n; i++, j++) {
            if (A[i] != A[j]) return NONE;
        }
        return new int[] {i0 + len - 1, i1 + len};
    }

    void test(int[] A, int[] expected) {
        assertArrayEquals(expected, threeEqualParts(A));
    }

    @Test
    public void test() {
        test(new int[] {1, 0, 1, 0, 1}, new int[] {0, 3});
        test(new int[] {1, 1, 0, 1, 1}, new int[] {-1, -1});
        test(new int[] {0, 0, 0, 0, 0}, new int[] {0, 4});
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
