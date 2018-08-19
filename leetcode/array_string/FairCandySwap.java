import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC888: https://leetcode.com/problems/fair-candy-swap/
//
// Alice and Bob have candy bars of different sizes: A[i] is the size of the 
// i-th bar of candy that Alice has, and B[j] is the size of the j-th bar of
// candy that Bob has. They would like to exchange one candy bar each so that 
// they both have the same total amount of candy. Return an integer array ans 
// where ans[0] is the size of the candy bar that Alice must exchange, and 
// ans[1] is the size of the candy bar that Bob must exchange.
// If there are multiple answers, you may return any one of them.  It is 
// guaranteed an answer exists.
public class FairCandySwap {
    // beats %(27 ms for 75 tests)
    public int[] fairCandySwap(int[] A, int[] B) {
        int diff = 0;
        Set<Integer> set = new HashSet<>();
        for (int b : B) {
            diff += b;
            set.add(b);
        }
        for (int a : A) {
            diff -= a;
        }
        diff /= 2;
        for (int a : A) {
            if (set.contains(a + diff)) return new int[]{a, a + diff};
        }
        return null;
    }

    void test(int[] A, int[] B, int[] expected) {
        assertArrayEquals(expected, fairCandySwap(A, B));
    }

    @Test
    public void test() {
        test(new int[]{1, 1}, new int[]{2, 2}, new int[]{1, 2});
        test(new int[]{2}, new int[]{1,3}, new int[]{2, 3});
        test(new int[]{1, 2, 5}, new int[]{2, 4}, new int[]{5, 4});
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
