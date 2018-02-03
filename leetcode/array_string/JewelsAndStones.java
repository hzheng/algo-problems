import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC771: https://leetcode.com/problems/jewels-and-stones/
//
// You're given strings J representing the types of stones that are jewels, and 
// S representing the stones you have.  Each character in S is a type of stone 
// you have.  You want to know how many of the stones you have are also jewels.
// The letters in J are guaranteed distinct, and all characters in J and S are
// letters.
public class JewelsAndStones {
    // Hash Table
    // beats %(17 ms for 254 tests)
    public int numJewelsInStones(String J, String S) {
        boolean[] jeweries = new boolean[128];
        for (char c : J.toCharArray()) {
            jeweries[c] = true;
        }
        int res = 0;
        for (char c : S.toCharArray()) {
            if (jeweries[c]) {
                res++;
            }
        }
        return res;
    }

    // Regex
    // beats %(30 ms for 254 tests)
    public int numJewelsInStones2(String J, String S) {
        return S.replaceAll("[^" + J + "]", "").length();
    }

    void test(String J, String S, int expected) {
        assertEquals(expected, numJewelsInStones(J, S));
        assertEquals(expected, numJewelsInStones2(J, S));
    }

    @Test
    public void test() {
        test("aA", "aAAbbbb", 3);
        test("z", "ZZ", 0);
    }

    public static void main(String[] args) {
        String clazz = new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
