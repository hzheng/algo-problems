import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC932: https://leetcode.com/problems/beautiful-array/
//
// For some fixed N, an array A is beautiful if it is a permutation of the 
// integers 1, 2, ..., N, such that:
// For each i < j, there is no k with i < k < j s.t A[k] * 2 = A[i] + A[j].
// Given N, return any beautiful array A. 
public class BeautifulArray {
    // Divide & Conquer/Recursion
    // time complexity: O(N * log(N)), space complexity: O(N * log(N))
    // beats %(4 ms for 38 tests)
    public int[] beautifulArray(int N) {
        int[] res = new int[N];
        if (N == 1) {
            res[0] = 1;
            return res;
        }

        int[] left = beautifulArray(N / 2);
        int[] right = beautifulArray(N - N / 2);
        for (int i = 0; i < N / 2; i++) {
            res[i] = left[i] * 2;
        }
        for (int i = N / 2; i < N; i++) {
            res[i] = right[i - N / 2] * 2 - 1;
        }
        return res;
    }

    // Divide & Conquer/Recursion + Memoization
    // time complexity: O(N), space complexity: O(N)
    // beats %(2 ms for 38 tests)
    public int[] beautifulArray2(int N) {
        return generate(N, new HashMap<>());
    }

    private int[] generate(int N, Map<Integer, int[]> memo) {
        if (memo.containsKey(N)) return memo.get(N);

        int[] res = new int[N];
        if (N == 1) {
            res[0] = 1;
        } else {
            int i = 0;
            for (int x : generate((N + 1) / 2, memo)) {
                res[i++] = 2 * x - 1;
            }
            for (int x : generate(N / 2, memo)) {
                res[i++] = 2 * x;
            }
        }
        memo.put(N, res);
        return res;
    }

    // Divide & Conquer/Iteration
    // time complexity: O(N), space complexity: O(N)
    // beats %(62 ms for 38 tests)
    public int[] beautifulArray3(int N) {
        List<Integer> res = new ArrayList<>();
        res.add(1);
        for (List<Integer> tmp; res.size() < N; res = tmp) {
            tmp = new ArrayList<>();
            for (int i : res) {
                if (i * 2 - 1 <= N) {
                    tmp.add(i * 2 - 1);
                }
            }
            for (int i : res) {
                if (i * 2 <= N) {
                    tmp.add(i * 2);
                }
            }
        }
        return res.stream().mapToInt(i -> i).toArray();
    }

    void test(int[] array) {
        // System.out.println(Arrays.toString(array));
        Set<Integer> numbers = new HashSet<>();
        Set<Integer> set = new HashSet<>();
        for (int i = 0, n = array.length; i < n; i++) {
            numbers.add(i + 1);
            set.add(array[i]);
            for (int j = i + 2; j < n; j++) {
                int a = array[i];
                int b = array[j];
                if ((a + b) % 2 != 0) continue;

                int mid = (a + b) / 2;
                for (int k = i + 1; k < j; k++) {
                    assertTrue(array[k] + "*2=" + a + "+" + b, array[k] != mid);
                }
            }
        }
        assertEquals(numbers, set);
    }

    void test(int N) {
        test(beautifulArray(N));
        test(beautifulArray2(N));
        test(beautifulArray3(N));
    }

    @Test
    public void test() {
        test(4);
        test(5);
        test(10);
        test(100);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
