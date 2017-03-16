import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC340: https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters
//
// Given a string, find the length of the longest substring T that contains at
// most k distinct characters.
public class LongestSubstringKDistinct {
    // Two Pointers + Hash Table
    // beats 96.25%(7 ms for 142 tests)
    // time complexity: O(N)
    public int lengthOfLongestSubstringKDistinct(String s, int k) {
        int[] freq = new int[256];
        int n = s.length();
        int max = 0;
        int start = -1;
        for (int i = 0, count = 0; i < n; i++) {
            if (freq[s.charAt(i)]++ == 0 && ++count > k) {
                max = Math.max(max, i - start - 1);
                while (--freq[s.charAt(++start)] > 0) {}
                count--;
            }
        }
        return Math.max(max, n - start - 1);
    }

    // Two Pointers + Hash Table
    // beats 88.55%(9 ms for 142 tests)
    // time complexity: O(N)
    public int lengthOfLongestSubstringKDistinct_2(String s, int k) {
        int[] freq = new int[256];
        int n = s.length();
        int max = 0;
        int start = -1;
        for (int i = 0, count = 0; i < n; i++) {
            if (freq[s.charAt(i)]++ == 0 && ++count > k) {
                while (--freq[s.charAt(++start)] > 0) {}
                count--;
            }
            max = Math.max(max, i - start);
        }
        return max;
    }

    // Hash Table + SortedMap
    // Follow up: If the string is given as a stream s.t. previous characters are not available
    // beats 16.25%(59 ms for 142 tests)
    // time complexity: O(N * log(K))
    public int lengthOfLongestSubstringKDistinct2(String s, int k) {
        int n = s.length();
        if (n == 0 || k == 0) return 0;

        Map<Character, Integer> winMap = new HashMap<>();
        SortedMap<Integer, Character> winRevMap = new TreeMap<>();
        int max = 1;
        for (int i = 0, start = 0; i < n; i++) {
            char c = s.charAt(i);
            Integer lastPos = winMap.get(c);
            if (lastPos != null) {
                winRevMap.remove(lastPos);
            } else if (winMap.size() == k) {
                start = winRevMap.firstKey();
                winMap.remove(winRevMap.remove(start++));
            }
            winMap.put(c, i);
            winRevMap.put(i, c);
            max = Math.max(max, i - start + 1);
        }
        return max;
    }

    void test(String s, int k, int expected) {
        assertEquals(expected, lengthOfLongestSubstringKDistinct(s, k));
        assertEquals(expected, lengthOfLongestSubstringKDistinct_2(s, k));
        assertEquals(expected, lengthOfLongestSubstringKDistinct2(s, k));
    }

    @Test
    public void test() {
        test("aa", 0, 0);
        test("a", 1, 1);
        test("", 3, 0);
        test("eceba", 2, 3);
        test("a@b$5!a8alskj234jasdf*()@$&%&#FJAvjjdaurNNMa8ASDF-0321jf?>{}L:fh", 10, 14);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("LongestSubstringKDistinct");
    }
}
