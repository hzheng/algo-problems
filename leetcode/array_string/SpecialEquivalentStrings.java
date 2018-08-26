import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC893: https://leetcode.com/problems/groups-of-special-equivalent-strings/
//
// Given an array A of strings. Two strings S and T are special-equivalent if
// after any number of moves, S == T. A move consists of choosing two indices i
// and j with i % 2 == j % 2, and swapping S[i] with S[j]. Now, a group of
// special-equivalent strings from A is a non-empty subset S of A such that any
//string not in S is not special-equivalent with any string in S.
// Return the number of groups of special-equivalent strings from A.
public class SpecialEquivalentStrings {
    // Set + Sort
    // beats %(8 ms for 34 tests)
    public int numSpecialEquivGroups(String[] A) {
        Set<String> set = new HashSet<>();
        for (String a : A) {
            int len = a.length();
            char[] even = new char[(len + 1) / 2];
            for (int i = 0, j = 0; j < len; i++, j += 2) {
                even[i] = a.charAt(j);
            }
            Arrays.sort(even);
            char[] odd = new char[len / 2];
            for (int i = 0, j = 1; j < len; i++, j += 2) {
                odd[i] = a.charAt(j);
            }
            Arrays.sort(odd);
            set.add(String.valueOf(even) + String.valueOf(odd));
        }
        return set.size();
    }

    // Set + Count
    // beats %(18 ms for 34 tests)
    public int numSpecialEquivGroups2(String[] A) {
        Set<String> set = new HashSet<>();
        for (String a : A) {
            int[] count = new int[52];
            for (int i = 0; i < a.length(); i++) {
                count[a.charAt(i) - 'a' + 26 * (i % 2)]++;
            }
            set.add(Arrays.toString(count));
        }
        return set.size();
    }

    void test(String[] A, int expected) {
        assertEquals(expected, numSpecialEquivGroups(A));
        assertEquals(expected, numSpecialEquivGroups2(A));
    }

    @Test
    public void test() {
        test(new String[] { "a", "b", "c", "a", "c", "c" }, 3);
        test(new String[] { "aa", "bb", "ab", "ba" }, 4);
        test(new String[] { "abc", "acb", "bac", "bca", "cab", "cba" }, 3);
        test(new String[] { "abcd", "cdab", "adcb", "cbad" }, 1);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
