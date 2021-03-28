import java.util.*;
import java.util.stream.Collectors;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1805: https://leetcode.com/problems/number-of-different-integers-in-a-string/
//
// You are given a string word that consists of digits and lowercase English letters.
// You will replace every non-digit character with a space. For example, "a123bc34d8ef34" will
// become " 123  34 8  34". Notice that you are left with some integers that are separated by at
// least one space: "123", "34", "8", and "34".
// Return the number of different integers after performing the replacement operations on word.
// Two integers are considered different if their decimal representations without any leading zeros
// are different.
// Constraints:
//
// 1 <= word.length <= 1000
// word consists of digits and lowercase English letters.
public class NumDifferentIntegers {
    // time complexity: O(N), space complexity: O(N)
    // 1 ms(%), 36.8 MB(%) for 68 tests
    public int numDifferentIntegers(String word) {
        Set<String> set = new HashSet<>();
        for (int n = word.length(), i = 0, prev = -1; i <= n; i++) {
            char c = (i == n) ? 0 : word.charAt(i);
            if (!Character.isDigit(c)) {
                if (i > 0 && Character.isDigit(word.charAt(i - 1))) {
                    set.add(word.substring(prev, i));
                    prev = -1;
                }
            } else if (prev < 0 || word.charAt(prev) == '0') {
                prev = i;
            }
        }
        return set.size();
    }

    // Two Pointers
    // time complexity: O(N), space complexity: O(N)
    // 2 ms(%), 37.7 MB(%) for 95 tests
    public int numDifferentIntegers2(String word) {
        Set<String> set = new HashSet<>(Collections.singletonList(""));
        int i = 0;
        for (int j = 0, n = word.length(); j < n; j++) {
            if (Character.isDigit(word.charAt(j))) {
                i += (i < j && word.charAt(i) == '0') ? 1 : 0;
            } else {
                set.add(word.substring(i, j));
                i = j + 1;
            }
        }
        set.add(word.substring(i));
        return set.size() - 1;
    }

    // RegEx
    // time complexity: O(N), space complexity: O(N)
    // 12 ms(%), 39.3 MB(%) for 95 tests
    public int numDifferentIntegers3(String word) {
        String[] arr = word.split("\\D");
        Set<String> set = new HashSet<>();
        for (String s : arr) {
            s = s.trim();
            if (!s.isEmpty()) {
                s = s.replaceAll("^0*", "");
                set.add(s);
            }
        }
        return set.size();
    }

    // Stream
    // time complexity: O(N), space complexity: O(N)
    // 14 ms(%), 39.1 MB(%) for 95 tests
    public int numDifferentIntegers4(String word) {
        return Arrays.stream(word.replaceAll("[a-z]", " ").split(" "))
                     .filter(x -> !x.isEmpty() && x.charAt(0) != ' ')
                     .map(x -> x.replaceFirst("^0+(?!$)", "")).collect(Collectors.toSet()).size();
    }

    private void test(String word, int expected) {
        assertEquals(expected, numDifferentIntegers(word));
        assertEquals(expected, numDifferentIntegers2(word));
        assertEquals(expected, numDifferentIntegers3(word));
        assertEquals(expected, numDifferentIntegers4(word));
    }

    @Test public void test() {
        test("a123bc34d8ef34", 3);
        test("leet1234code234", 2);
        test("a1b01c001", 1);
        test("adgva1x167278959591294a001", 2);
        test("035985750011523523129774573439111590559325a1554234973", 2);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
