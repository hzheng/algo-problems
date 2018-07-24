import org.junit.Test;
import static org.junit.Assert.*;

// LC866: https://leetcode.com/problems/prime-palindrome/
//
// Find the smallest prime palindrome greater than or equal to N.
// Note: 1 <= N <= 10^8
public class PrimePalindrome {
    // beats 82.78%(9 ms for 58 tests)
    public int primePalindrome(int N) {
        for (int i = N;; i++) {
            if (isPalindrome(i) && isPrime(i)) return i;

            if (10_000_000 <= i && i < 100_000_000) { // multiple of 11
                i = 100_000_000;
            }
        }
    }

    private boolean isPrime(int n) {
        if (n < 2) return false;

        for (int i = 2; i * i <= n; i++) {
            if ((n % i) == 0) return false;
        }
        return true;
    }

    private boolean isPalindrome(int x) {
        if (x < 0 || (x != 0 && x % 10 == 0)) return false;

        int reversed = 0;
        int y = x;
        for (; y > reversed; y /= 10) {
            reversed = reversed * 10 + y % 10;
        }
        return (y == reversed || y == reversed / 10);
    }

    // beats 33.57%(51 ms for 58 tests)
    public int primePalindrome2(int N) {
        for (int len = 1;; len++) { // only check for odd-length palindromes
            for (int root = (int) Math.pow(10, len - 1);
                 root < (int) Math.pow(10, len); root++) {
                StringBuilder sb = new StringBuilder(Integer.toString(root));
                for (int k = len - 2; k >= 0; k--) {
                    sb.append(sb.charAt(k));
                }
                int x = Integer.valueOf(sb.toString());
                if (x >= N && isPrime(x)) return N <= 11 ? Math.min(x, 11) : x;
            }
        }
    }

    // beats 36.94%(46 ms for 58 tests)
    public int primePalindrome3(int N) {
        if (8 <= N && N <= 11) return 11;

        for (int x = 1; ; x++) {
            String s = Integer.toString(x);
            String r = new StringBuilder(s).reverse().toString().substring(1);
            int y = Integer.valueOf(s + r);
            if (y >= N && isPrime(y)) return y;
        }
    }

    void test(int N, int expected) {
        assertEquals(expected, primePalindrome(N));
        assertEquals(expected, primePalindrome2(N));
        assertEquals(expected, primePalindrome3(N));
    }

    @Test
    public void test() {
        test(6, 7);
        test(8, 11);
        test(11, 11);
        test(13, 101);
        test(9989900, 100030001);
        test(61023998, 100030001);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
