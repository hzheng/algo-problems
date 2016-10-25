import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC440: https://leetcode.com/problems/k-th-smallest-in-lexicographical-order/
//
// Given integers n and k, find the lexicographically k-th smallest integer in
// the range from 1 to n.
// Note: 1 ≤ k ≤ n ≤ 10 ^ 9.
public class FindKthLexiNumber {
    // Time Limit Exceeded
    public int findKthNumber(int n, int k) {
        int prefix = 1;
        for (int i = 1; i < k; i++) {
            if (prefix <= n / 10) {
                prefix *= 10;
            } else if (prefix < n) {
                for (++prefix; prefix % 10 == 0; prefix /= 10) {}
            } else {
                prefix = prefix / 10 + 1;
            }
        }
        return prefix;
    }

    // beats N/A(8 ms for 69 tests)
    public int findKthNumber2(int n, int k) {
        for (int prefix = 1, count = 0;; ) {
            int more = countPrefix(prefix, n);
            if (count + more < k) {
                count += more;
                prefix++;
            } else if (count + more > k) {
                if (++count == k) return prefix;

                prefix *= 10;
            } else return last(prefix, n);
        }
    }

    private int last(int prefix, int n) {
        if (prefix > n / 10) return prefix;

        if (prefix == n / 10) return n;

        return last(prefix * 10 + 9, n);
    }

    private int countPrefix(int prefix, int n) {
        if (prefix > n / 10) return 1;

        if (prefix == n / 10) return 2 + n % 10;

        int count = 1;
        if (!String.valueOf(n).startsWith(String.valueOf(prefix))) {
            count += 10 * countPrefix(prefix * 10, n);
        } else {
            for (int i = 0; i < 10; i++) {
                count += countPrefix(prefix * 10 + i, n);
            }
        }
        return count;
    }

    // beats N/A(6 ms for 69 tests)
    public int findKthNumber3(int n, int k) {
outerLoop:
        for (int cur = 1, left = k; ; ) {
            if (left == 1) return cur;

            int count = 1;
            for (int prefix = cur, power = 10; prefix <= n / power; power *= 10) {
                count += Math.min((prefix + 1) * power, n + 1) - prefix * power;
                if (count >= left) {
                    cur *= 10;
                    left--;
                    continue outerLoop;
                }
            }
            cur++;
            left -= count;
        }
    }

    void test(int n, int k, int expected) {
        if (n < 10000) {
            assertEquals(expected, findKthNumber(n, k));
        }
        assertEquals(expected, findKthNumber2(n, k));
        assertEquals(expected, findKthNumber3(n, k));
    }

    @Test
    public void test() {
        test(10, 3, 2);
        test(13, 2, 10);
        test(130, 20, 116);
        test(100, 90, 9);
        test(100, 100, 99);
        test(100, 50, 53);
        test(100, 10, 17);
        test(200, 87, 177);
        test(200, 99, 188);
        test(2000, 115, 1100);
        test(2000, 870, 1781);
        test(2000, 87, 1076);
        test(1692778, 1545580, 867519);
        test(681692778, 351251360, 416126219);
        test(957747794, 424238336, 481814499);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("FindKthLexiNumber");
    }
}
