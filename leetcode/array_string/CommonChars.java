import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1002: https://leetcode.com/problems/find-common-characters/
//
// Given an array A of strings made only from lowercase letters, return a list of all characters
// that show up in all strings within the list (including duplicates).  For example, if a character
// occurs 3 times in all strings but not 4 times, you need to include that character three times in
// the final answer. You may return the answer in any order.
//
// Note:
// 1 <= A.length <= 100
// 1 <= A[i].length <= 100
// A[i][j] is a lowercase letter
public class CommonChars {
    // time complexity: O(N), space complexity: O(1)
    // 5 ms(65.25%), 40.4 MB(5.57%) for 83 tests
    public List<String> commonChars(String[] A) {
        int[] common = new int[26];
        Arrays.fill(common, Integer.MAX_VALUE);
        for (String a : A) {
            int[] count = new int[26];
            for (char c : a.toCharArray()) {
                count[c - 'a']++;
            }
            for (int i = common.length - 1; i >= 0; i--) {
                common[i] = Math.min(common[i], count[i]);
            }
        }
        List<String> res = new ArrayList<>();
        for (char c = 'a'; c <= 'z'; c++) {
            for (int i = common[c - 'a']; i > 0; i--) {
                res.add(String.valueOf(c));
            }
        }
        return res;
    }

    private void test(String[] A, String[] expected) {
        List<String> expectedList = Arrays.asList(expected);
        assertEquals(expectedList, commonChars(A));
    }

    @Test public void test() {
        test(new String[] {"bella", "label", "roller"}, new String[] {"e", "l", "l"});
        test(new String[] {"cool", "lock", "cook"}, new String[] {"c", "o"});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
