import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// Given a string S and a string T, find the minimum window in S which will
// contain all the characters in T in complexity O(n).
public class MinWindowSubstr {
    // beats 25.94%
    static class CharSet {
        int count;
        Queue<Integer> indices = new ArrayDeque<>();

        public void add() {
            count++;
        }

        public int consume(int index) {
            indices.add(index);
            if (indices.size() > count) {
                return indices.remove();
            }

            return -1;
        }
    }

    public String minWindow(String s, String t) {
        Map<Character, CharSet> map = new HashMap<>();
        for (char c : t.toCharArray()) {
            if (!map.containsKey(c)) {
                map.put(c, new CharSet());
            }
            map.get(c).add();
        }

        int minStart = 0;
        int minEnd = -1;
        int left = t.length();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (!map.containsKey(c)) continue;

            if (map.get(c).consume(i) < 0) {
                left--;
            }
            if (left <= 0) {
                int start = getStart(map);
                if (minEnd < 0 || (i - start < minEnd - minStart)) {
                    minStart = start;
                    minEnd = i;
                }
            }
        }
        return minEnd < 0 ? "" : s.substring(minStart, minEnd + 1);
    }

    private int getStart(Map<Character, CharSet> map) {
        int min = Integer.MAX_VALUE;
        for (CharSet charSet : map.values()) {
            min = Math.min(min, charSet.indices.peek());
        }
        return min;
    }

    void test(String s, String t, String expected) {
        assertEquals(expected, minWindow(s, t));
    }

    @Test
    public void test1() {
        test("bba", "ab", "ba");
        test("ADOBECODEBANC", "ABC", "BANC");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MinWindowSubstr");
    }
}
