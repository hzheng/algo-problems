import java.util.*;
import java.math.*;
import java.io.*;

import org.junit.Test;
import static org.junit.Assert.*;

// http://poj.org/problem?id=3276
//
// John has arranged his N(1 ≤ N ≤ 5,000) cows in a row and many of them are facing forward,
// some are facing backward. He needs them all to face forward. Given an turning machine,
// which must be preset to turn K(1 ≤ K ≤ N) cows at once. Each time it reverses the facing
// direction of a contiguous K cows in the line. Each cow remains in the same location as
// before, but ends up facing the opposite direction. A cow that starts out facing forward
// will be turned backward by the machine and vice-versa.
// Determine the minimum value of K that minimizes the number of operations required by
// the machine to make all the cows face forward. Also determine M, the minimum number of
// machine operations required to get all the cows facing forward using that value of K.
// Input
// Line 1: A single integer: N
// Lines 2..N+1: Line i+1 contains a single character, F or B, indicating whether cow i is
// facing forward or backward.
// Output
// Line 1: Two space-separated integers: K and M
public class FaceTheRightWay {
    // time complexity: O(N ^ 2)
    // Time Limit Exceeded
    public static int flip(char[] S, int K) {
        int n = S.length;
        int[] flips = new int[n];
        int res = 0;
        for (int i = 0; i < n; i++) {
            if ((flips[i] % 2 == 0) ^ (S[i] == 'B')) continue;

            res++;
            for (int j = i + 1; j < i + K; flips[j++]++) {
                if (j >= n) return -1;
            }
        }
        return res;
    }

    // time complexity: O(N)
    // Time: 2844 ms Memory: 5236 K
    public static int flip2(char[] S, int K) {
        int n = S.length;
        int[] flips = new int[n];
        int res = 0;
        int sum = 0;
        for (int i = 0; i <= n - K; i++) {
            if (((S[i] == 'B' ? 1 : 0) + sum) % 2 != 0) {
                res++;
                flips[i] = 1;
            }
            sum += flips[i];
            if (i - K + 1 >= 0) {
                sum -= flips[i - K + 1];
            }
        }
        for (int i = n - K + 1; i < n; i++) {
            if (((S[i] == 'B' ? 1 : 0) + sum) % 2 != 0) return -1;

            if (i - K + 1 >= 0) {
                sum -= flips[i - K + 1];
            }
        }
        return res;
    }

    void test(String s, int k, int expected) {
        char[] cs = s.toCharArray();
        assertEquals(expected, flip(cs, k));
        assertEquals(expected, flip2(cs, k));
    }

    @Test
    public void test() {
        test("BBFBFBB", 3, 3);
        test("FFFFF", 4, 0);
        test("BBFFFFFFB", 3, 5);
        test("BBFFFFFB", 3, -1);
    }

    public static void main(String[] args) throws IOException {
        if (System.getProperty("ONLINE_JUDGE") == null) {
            String clazz = new Object(){}.getClass().getEnclosingClass().getSimpleName();
            org.junit.runner.JUnitCore.main(clazz);
            return;
        }

        Scanner in = new Scanner(System.in);
        int N = in.nextInt();
        char[] S = new char[N];
        for (int i = 0; i < N; i++) {
            S[i] = in.next().charAt(0);
        }
        int M = N;
        int K = 1;
        for (int k = 1; k <= N; k++) {
            int res = flip2(S, k);
            if (res >= 0 && res < M) {
                M = res;
                K = k;
            }
        }
        System.out.println(K + " " + M);
    }
}
