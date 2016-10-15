import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC205: https://leetcode.com/problems/isomorphic-strings/
//
// Given two strings s and t, determine if they are isomorphic.
public class IsomorphicStr {
    // beats 87.35%(13 ms)
    public boolean isIsomorphic(String s, String t) {
        Map<Character, Character> mapping = new HashMap<>();
        Set<Character> mapped = new HashSet<>();
        for (int i = s.length() - 1; i >= 0; i--) {
            char firstCh = s.charAt(i);
            char secondCh = t.charAt(i);
            if (!mapping.containsKey(firstCh)) {
                mapping.put(firstCh, secondCh);
                mapped.add(secondCh);
            } else if (mapping.get(firstCh) != secondCh) return false;
        }
        return mapping.size() == mapped.size();
    }

    // beats 88.80%(12 ms)
    public boolean isIsomorphic2(String s, String t) {
        Map<Character, Character> mapping = new HashMap<>();
        Set<Character> mapped = new HashSet<>();
        for (int i = s.length() - 1; i >= 0; i--) {
            char firstCh = s.charAt(i);
            char secondCh = t.charAt(i);
            if (!mapping.containsKey(firstCh)) {
                if (mapped.contains(secondCh)) return false;

                mapping.put(firstCh, secondCh);
                mapped.add(secondCh);
            } else if (mapping.get(firstCh) != secondCh) return false;
        }
        return true;
    }

    // beats 97.74%(4 ms)
    public boolean isIsomorphic3(String s, String t) {
        char[] mapping = new char[256];
        boolean[] mapped = new boolean[256];
        for (int i = s.length() - 1; i >= 0; i--) {
            char firstCh = s.charAt(i);
            char secondCh = t.charAt(i);
            if (mapping[firstCh] == 0) {
                if (mapped[secondCh]) return false;

                mapping[firstCh] = secondCh;
                mapped[secondCh] = true;
            } else if (mapping[firstCh] != secondCh) return false;
        }
        return true;
    }

    // one mapping data structure
    // beats 87.35%(13 ms)
    public boolean isIsomorphic4(String s, String t) {
        Map<Character, Character> map = new HashMap<>();
        for (int i = s.length() - 1; i >= 0; i--) {
            char firstCh = s.charAt(i);
            char secondCh = t.charAt(i);
            if (map.containsKey(firstCh)) {
                if (map.get(firstCh) != secondCh) return false;
            } else { // containsValue is slow: O(N)
                if (map.containsValue(secondCh)) return false;

                map.put(firstCh, secondCh);
            }
        }
        return true;
    }

    // Solution of Choice
    // beats 99.34%(3 ms for 30 tests)
    public boolean isIsomorphic5(String s, String t) {
        int[] map = new int[512];
        for (int i = s.length() - 1; i >= 0; i--) {
            int first = s.charAt(i);
            int second = t.charAt(i) + 256;
            if (map[first] != map[second]) return false;

            map[first] = map[second] = i;
      }
      return true;
    }

    void test(String s, String t, boolean expected) {
        assertEquals(expected, isIsomorphic(s, t));
        assertEquals(expected, isIsomorphic2(s, t));
        assertEquals(expected, isIsomorphic3(s, t));
        assertEquals(expected, isIsomorphic4(s, t));
        assertEquals(expected, isIsomorphic5(s, t));
    }

    @Test
    public void test1() {
        test("e", "a", true);
        test("egg", "add", true);
        test("paper", "title", true);
        test("foo", "bar", false);
        test("ab", "aa", false);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("IsomorphicStr");
    }
}
