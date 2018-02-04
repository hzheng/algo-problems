import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC779:
public class KthGrammar {
    // Recursion
    // beats %(5 ms for 55 tests)
    public int kthGrammar(int N, int K) {
        if (N == 1) return 0;

        // if last is 0: odd K yields 0, even yields 1
        // if last is 1: odd K yields 1, even yields 0
        return (kthGrammar(N - 1, (K + 1) >> 1) + K + 1) & 1;
        // or: 
        // return kthGrammar(N - 1, (K + 1) >> 1) ^ (K + 1 & 1);
    }

    // Recursion
    // beats %(5 ms for 55 tests)
    public int kthGrammar2(int N, int K) {
        if (N == 1) return 0;

        if (K <= (1 << N - 2)) return kthGrammar2(N - 1, K);

        return 1 - kthGrammar2(N - 1, K - (1 << N - 2));
    }

    // Bit Manipulation
    // beats %(5 ms for 55 tests)
    public int kthGrammar3(int N, int K) {
        return Integer.bitCount(K - 1) & 1;
    }

    // Bit Manipulation
    // beats %(4 ms for 55 tests)
    public int kthGrammar4(int N, int K) {
        int res = 0;
        for (int k = K - 1; k > 0; k -= k & -k, res ^= 1) {}
        return res;
    }

    void test(int N, int K, int expected) {
        assertEquals(expected, kthGrammar(N, K));
        assertEquals(expected, kthGrammar2(N, K));
        assertEquals(expected, kthGrammar3(N, K));
        assertEquals(expected, kthGrammar4(N, K));
    }

    @Test
    public void test() {
        test(1, 1, 0);
        test(2, 1, 0);
        test(2, 2, 1);
        test(4, 5, 1);
        test(5, 8, 1);
        test(5, 9, 1);
        test(5, 10, 0);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
