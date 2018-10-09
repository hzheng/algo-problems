import org.junit.Test;
import static org.junit.Assert.*;

// LC915: https://leetcode.com/problems/partition-array-into-disjoint-intervals/
//
// Given an array A, partition it into two (contiguous) subarrays left and right
// so that:
// Every element in left is less than or equal to every element in right.
// left and right are non-empty.
// left has the smallest possible size.
// Return the length of left after such a partitioning.  It is guaranteed that
// such a partitioning exists.
public class PartitionDisjoint {
    // time complexity: O(N), space complexity: O(N)
    // beats 35.66%(9 ms for 56 tests)
    public int partitionDisjoint(int[] A) {
        int n = A.length;
        int[] left = new int[n];
        left[0] = A[0];
        for (int i = 1; i < n; i++) {
            left[i] = Math.max(left[i - 1], A[i]);
        }
        int[] right = new int[n];
        right[n - 1] = A[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            right[i] = Math.min(right[i + 1], A[i]);
        }
        for (int i = 1; ; i++) {
            if (left[i - 1] <= right[i]) return i;
        }
    }

    // time complexity: O(N), space complexity: O(N)
    // beats 35.66%(9 ms for 56 tests)
    public int partitionDisjoint2(int[] A) {
        int n = A.length;
        int[] right = new int[n];
        right[n - 1] = A[n - 1];
        for (int i = n - 2; i > 0; i--) {
            right[i] = Math.min(A[i], right[i + 1]);
        }
        for (int i = 1, maxLeft = 0; ; i++) {
            maxLeft = Math.max(maxLeft, A[i - 1]);
            if (maxLeft <= right[i]) return i;
        }
    }

    // time complexity: O(N), space complexity: O(1)
    // beats 100.00%(4 ms for 56 tests)
    public int partitionDisjoint3(int[] A) {
        int partition = 0;
        for (int i = 1, localMax = A[0], max = localMax; i < A.length; i++) {
            if (localMax > A[i]) {
                localMax = max;
                partition = i;
            } else {
                max = Math.max(max, A[i]);
            }
        }
        return partition + 1;
    }

    void test(int[] A, int expected) {
        assertEquals(expected, partitionDisjoint(A));
        assertEquals(expected, partitionDisjoint2(A));
        assertEquals(expected, partitionDisjoint3(A));
    }

    @Test
    public void test() {
        test(new int[] {5, 0, 3, 8, 6}, 3);
        test(new int[] {1, 1, 1, 0, 6, 12}, 4);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
