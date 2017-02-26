import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC266: https://leetcode.com/problems/palindrome-permutation/
//
// Given a string, determine if a permutation of the string could form a palindrome.
public class PalindromePermutation {
    // Hash Table(array)
    // beats 84.63%(0 ms for 26 tests)
    public boolean canPermutePalindrome(String s) {
        int[] count = new int[256];
        for (char c : s.toCharArray()) {
            count[c]++;
        }
        boolean hasMiddle = false;
        for (int i : count) {
            if (i % 2 == 1) {
                if (hasMiddle) return false;

                hasMiddle = true;
            }
        }
        return true;
    }

    // Hash Table(set)
    // beats 43.39%(2 ms for 26 tests)
    public boolean canPermutePalindrome2(String s) {
        Set<Character>set = new HashSet<>();
        for (char c : s.toCharArray()) {
            if (!set.add(c)) {
                set.remove(c);
            }
        }
        return set.size() <= 1;
    }

    // Hash Table(array)
    // beats 84.63%(0 ms for 26 tests)
    public boolean canPermutePalindrome3(String s) {
        int[] flags = new int[256];
        int odds = 0;
        for (char c : s.toCharArray()) {
            odds += (flags[c] ^= 1) * 2 - 1;
        }
        return odds <= 1;
    }

    void test(String s, boolean expected) {
        assertEquals(expected, canPermutePalindrome(s));
        assertEquals(expected, canPermutePalindrome2(s));
        assertEquals(expected, canPermutePalindrome3(s));
    }

    @Test
    public void test() {
        test("cd", false);
        test("code", false);
        test("aab", true);
        test("aaa", true);
        test("carerac", true);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("PalindromePermutation");
    }
}
