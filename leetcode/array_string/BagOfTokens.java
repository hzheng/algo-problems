import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC948: https://leetcode.com/problems/bag-of-tokens/
//
// You have an initial power P, an initial score of 0 points, and a bag of 
// tokens. Each token can be used at most once, has a value token[i], and has 
// potentially two ways to use it. If we have at least token[i] power, we may
// play the token face up, losing token[i] power, and gaining 1 point. If we 
// have at least 1 point, we may play the token face down, gaining token[i] 
// power, and losing 1 point. Return the largest number of points we can have
// after playing any number of tokens.
public class BagOfTokens {
    // Sort + Greedy
    // beats 53.12%(9 ms for 147 tests)
    // time complexity: O(N * log(N)), space complexity: O(N)
    public int bagOfTokensScore(int[] tokens, int P) {
        Arrays.sort(tokens);
        int n = tokens.length;
        int res = 0;
        for (int i = 0, j = n - 1, p = P, pt = 0; i <= j && (p >= tokens[i] || pt > 0);) {
            for (; i <= j && p >= tokens[i]; i++, pt++) {
                p -= tokens[i];
            }
            res = Math.max(res, pt);
            if (i <= j && pt > 0) {
                p += tokens[j--];
                pt--;
            }
        }
        return res;
    }

    // Sort + Greedy
    // beats 53.12%(9 ms for 147 tests)
    // time complexity: O(N * log(N)), space complexity: O(N)
    public int bagOfTokensScore2(int[] tokens, int P) {
        Arrays.sort(tokens);
        int res = 0;
        for (int i = 0, j = tokens.length - 1, pt = 0, p = P; i <= j;) {
            if (p >= tokens[i]) {
                p -= tokens[i++];
                res = Math.max(res, ++pt);
            } else if (pt > 0) {
                pt--;
                p += tokens[j--];
            } else break;
        }
        return res;
    }

    void test(int[] tokes, int P, int expected) {
        assertEquals(expected, bagOfTokensScore(tokes, P));
        assertEquals(expected, bagOfTokensScore2(tokes, P));
    }

    @Test
    public void test() {
        test(new int[] {100}, 50, 0);
        test(new int[] {100, 200}, 150, 1);
        test(new int[] {100, 200, 300, 400}, 200, 2);
        test(new int[] {33, 4, 28, 24, 96}, 35, 3);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
