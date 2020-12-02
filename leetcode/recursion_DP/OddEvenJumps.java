import java.util.*;
import java.util.stream.IntStream;

import org.junit.Test;

import static org.junit.Assert.*;

// LC975: https://leetcode.com/problems/odd-even-jump/
//
// You are given an integer array A. From some starting index, you can make a series of jumps. The
// (1st, 3rd, 5th, ...) jumps in the series are called odd-numbered jumps, and the
// (2nd, 4th, 6th, ...) jumps in the series are called even-numbered jumps. Note that the jumps are
// numbered, not the indices. You may jump forward from index i to j (i < j) in the following way:
// During odd-numbered jumps, you jump to the index j such that A[i] <= A[j] and A[j] is the
// smallest possible value. If there are multiple such indices j, you can only jump
// to the smallest such index j.
// During even-numbered jumps, you jump to the index j such that A[i] >= A[j] and A[j] is the
// largest possible value. If there are multiple such indices j, you can only jump to the smallest
// such index j.
// It may be the case that for some index i, there are no legal jumps.
// A starting index is good if, starting from that index, you can reach the end of the array
// (index A.length - 1) by jumping some number of times (possibly 0 or more than once).
// Return the number of good starting indices.
//
// Constraints:
// 1 <= A.length <= 2 * 10^4
// 0 <= A[i] < 10^5
public class OddEvenJumps {
    // SortedSet + Bit Manipulation
    // time complexity: O(N*log(N)), space complexity: O(N)
    // 111 ms(9.16%), 46.8 MB(19.74%) for 64 tests
    public int oddEvenJumps(int[] A) {
        NavigableSet<Integer> stops1 = new TreeSet<>();
        NavigableSet<Integer> stops2 = new TreeSet<>();
        NavigableSet<Integer> oddStops = new TreeSet<>();
        NavigableSet<Integer> evenStops = new TreeSet<>();
        final int shift = 14;
        for (int n = A.length, i = n - 1; i >= 0; i--) {
            int val1 = (A[i] << shift) | i;
            int val2 = (A[i] << shift) | (n - i);
            Integer oddJump = stops1.ceiling(val1);
            if (i == n - 1 || oddJump != null && evenStops.contains(oddJump)) {
                oddStops.add(val2);
            }
            stops1.add(val1);

            Integer evenJump = stops2.floor(val2);
            if (i == n - 1 || evenJump != null && oddStops.contains(evenJump)) {
                evenStops.add(val1);
            }
            stops2.add(val2);
        }
        return oddStops.size();
    }

    // SortedMap + Dynamic Programming
    // time complexity: O(N*log(N)), space complexity: O(N)
    // 59 ms(70.45%), 46.2 MB(37.81%) for 64 tests
    public int oddEvenJumps2(int[] A) {
        int n = A.length;
        boolean[] odd = new boolean[n];
        boolean[] even = new boolean[n];
        odd[n - 1] = even[n - 1] = true;
        NavigableMap<Integer, Integer> vals = new TreeMap<>();
        vals.put(A[n - 1], n - 1);
        int res = 1;
        for (int i = n - 2; i >= 0; i--) {
            int a = A[i];
            Map.Entry<Integer, Integer> lower = vals.floorEntry(a);
            Map.Entry<Integer, Integer> higher = vals.ceilingEntry(a);
            if (lower != null) {
                even[i] = odd[lower.getValue()];
            }
            if (higher != null) {
                odd[i] = even[higher.getValue()];
            }
            vals.put(a, i);
            res += odd[i] ? 1 : 0;
        }
        return res;
    }

    // Monotonic Stack + Dynamic Programming
    // time complexity: O(N*log(N)), space complexity: O(N)
    // 82 ms(27.87%), 42 MB(98.32%) for 64 tests
    public int oddEvenJumps3(int[] A) {
        int n = A.length;
        int[] indices = IntStream.range(0, n).boxed().sorted(Comparator.comparingInt(i -> A[i]))
                                 .mapToInt(i -> i).toArray();
        Integer[] oddNext = nextIndices(indices);
        indices = IntStream.range(0, n).boxed().sorted(Comparator.comparingInt(i -> -A[i]))
                           .mapToInt(i -> i).toArray();
        Integer[] evenNext = nextIndices(indices);
        int[] odd = new int[n];
        int[] even = new int[n];
        odd[n - 1] = even[n - 1] = 1;
        for (int i = n - 2; i >= 0; i--) {
            if (oddNext[i] != null) {
                odd[i] = even[oddNext[i]];
            }
            if (evenNext[i] != null) {
                even[i] = odd[evenNext[i]];
            }
        }
        return Arrays.stream(odd).sum();
    }

    private Integer[] nextIndices(int[] indices) {
        Integer[] res = new Integer[indices.length];
        Stack<Integer> stack = new Stack<>();
        for (int i : indices) {
            while (!stack.isEmpty() && i > stack.peek()) {
                res[stack.pop()] = i; // jump to i
            }
            stack.push(i);
        }
        return res;
    }

    private void test(int[] A, int expected) {
        assertEquals(expected, oddEvenJumps(A));
        assertEquals(expected, oddEvenJumps2(A));
        assertEquals(expected, oddEvenJumps3(A));
    }

    @Test public void test() {
        test(new int[] {2, 3, 1, 1, 4}, 3);
        test(new int[] {5, 1, 3, 4, 2}, 3);
        test(new int[] {10, 13, 12, 14, 15}, 2);
        test(new int[] {2}, 1);
        test(new int[] {10, 13, 8, 17, 7, 12, 14, 8, 15, 23, 12, 9, 3, 20, 1, 7, 12}, 12);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
