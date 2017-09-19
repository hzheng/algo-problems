import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// LC096: https://leetcode.com/problems/unique-binary-search-trees/
//
// Given n, how many structurally unique BST's that store values 1...n?
public class UniqueBst {
    // Recursion
    // Time Limit Exceeded
    public int numTrees(int n) {
        if (n == 0) return 1;

        int count = 0;
        for (int i = 1; i <= n; i++) {
            count += numTrees(i - 1) * numTrees(n - i);
        }
        return count;
    }

    // Dynamic Programming
    // time complexity: O(N), space complexity: O(N)
    // beats 14.61%(0 ms)
    public int numTrees2(int n) {
        int[] cache = new int[n + 1];
        cache[0] = 1;
        return numTrees2(n, cache);
    }

    private int numTrees2(int n, int[] cache) {
        if (cache[n] != 0) return cache[n];

        int count = 0;
        for (int i = 1; i <= n; i++) {
            count += numTrees2(i - 1, cache) * numTrees2(n - i, cache);
        }
        cache[n] = count;
        return count;
    }

    // Solution of Choice
    // Dynamic Programming
    // beats 14.61%(0 ms)
    public int numTrees3(int n) {
        int[] count = new int[n + 1];
        count[0] = 1;
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j < i; j++) {
                count[i] += count[j] * count[i - j - 1];
            }
        }
        return count[n];
    }

    // Solution of Choice
    // Math (Catalan Number: C(2n,n)/(n+1)= II(2..n) (n+k)/k)
    // time complexity: O(N), space complexity: O(1)
    // beats 14.61%(0 ms)
    public int numTrees4(int n) {
        long res = 1;
        for (int i = 1; i <= n; i++) {
            res *= (n + i);
            res /= i;
        }
        return (int)(res / (n + 1));
    }

    void test(Function<Integer, Integer> count, String name,
              int n, int expected) {
        long t1 = System.nanoTime();
        assertEquals(expected, (int)count.apply(n));
        System.out.format("%s: %.3f ms\n", name, (System.nanoTime() - t1) * 1e-6);
    }

    void test(int n, int expected) {
        UniqueBst u = new UniqueBst();
        if (n < 10) {
            test(u::numTrees, "numTrees", n, expected);
        }
        test(u::numTrees2, "numTrees2", n, expected);
        test(u::numTrees3, "numTrees3", n, expected);
        test(u::numTrees4, "numTrees4", n, expected);
    }

    @Test
    public void test1() {
        test(1, 1);
        test(2, 2);
        test(3, 5);
        test(4, 14);
        test(19, 1767263190);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("UniqueBst");
    }
}
