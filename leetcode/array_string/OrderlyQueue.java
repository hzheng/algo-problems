import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC899: https://leetcode.com/problems/orderly-queue/
//
// A string S of lowercase letters is given. We may make any number of moves.
// In each move, we choose one of the first K letters, remove it, and place it 
// at the end of the string. Return the lexicographically smallest string we 
// could have after any number of moves.
public class OrderlyQueue {
    // beats %(12 ms for 106 tests)
    public String orderlyQueue(String S, int K) {
        if (K > 1) {
            // with K = 2, we can swap any 2 adjacent letters
            // so it can be viewed as bubble sort
            char[] cs = S.toCharArray(); 
            Arrays.sort(cs);
            return String.valueOf(cs);
        }
        String res = S;
        for (int i = S.length(); i > 1; i--) {
            S = S.substring(1) + S.charAt(0);
            if (res.compareTo(S) > 0) {
                res = S;
            }
        }
        return res;
    }

    void test(String S, int K, String expected) {
        assertEquals(expected, orderlyQueue(S, K));
    }

    @Test
    public void test() {
        test("nhtq", 1,  "htqn");
        test("cdba", 1,  "acdb");
        test("cba", 1,  "acb");
        test("baaca", 3, "aaabc");
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
