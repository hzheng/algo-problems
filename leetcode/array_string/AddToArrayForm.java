import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC969: https://leetcode.com/problems/add-to-array-form-of-integer/
//
// For a non-negative integer X, the array-form of X is an array of its digits in left to right
// order.  For example, if X = 1231, then the array form is [1,2,3,1]. Given the array-form A of a
// non-negative integer X, return the array-form of the integer X+K.
//
// Note：
// 1 <= A.length <= 10000
// 0 <= A[i] <= 9
// 0 <= K <= 10000
// If A.length > 1, then A[0] != 0
public class AddToArrayForm {
    // Stack
    // time complexity: O(N+logK), space complexity: O(N)
    // 6 ms(32.49%), 40.8 MB(36.71%) for 156 tests
    public List<Integer> addToArrayForm(int[] A, int K) {
        Stack<Integer> stack = new Stack<>();
        for (int i = A.length - 1, k = K, carry = 0; ; i--, k /= 10) {
            int sum = (i >= 0 ? A[i] : 0) + k % 10 + carry;
            if (sum == 0 && k == 0 && (i < 0)) { break; }

            carry = sum / 10;
            stack.push(sum % 10);
        }
        List<Integer> res = new ArrayList<>(stack.size());
        while (!stack.isEmpty()) {
            res.add(stack.pop());
        }
        return res;
    }

    // time complexity: O(N+logK), space complexity: O(N)
    // 3 ms(90.28%), 40.6 MB(41.51%) for 156 tests
    public List<Integer> addToArrayForm2(int[] A, int K) {
        List<Integer> res = new ArrayList<>();
        for (int i = A.length - 1, sum = K; i >= 0 || sum > 0; i--, sum /= 10) {
            sum += (i >= 0) ? A[i] : 0;
            res.add(sum % 10);
        }
        Collections.reverse(res);
        return res;
    }

    private void test(int[] A, int K, Integer[] expected) {
        List<Integer> expectedList = Arrays.asList(expected);
        assertEquals(expectedList, addToArrayForm(A, K));
        assertEquals(expectedList, addToArrayForm2(A, K));
    }

    @Test public void test() {
        test(new int[] {0}, 10000, new Integer[] {1, 0, 0, 0, 0});
        test(new int[] {1, 2, 0, 0}, 34, new Integer[] {1, 2, 3, 4});
        test(new int[] {2, 7, 4}, 181, new Integer[] {4, 5, 5});
        test(new int[] {2, 1, 5}, 806, new Integer[] {1, 0, 2, 1});
        test(new int[] {9, 9, 9, 9, 9, 9, 9, 9, 9, 9}, 1,
             new Integer[] {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
