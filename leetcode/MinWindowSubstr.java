import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// Given a string S and a string T, find the minimum window in S which will
// contain all the characters in T in complexity O(n).
public class MinWindowSubstr {
    // beats 38.04%%(or 15.47%)
    static class CharSet {
        int count;
        // beat rate will drop to 25.94% if use ArrayDeque
        Queue<Integer> indices = new LinkedList<>();

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
            if (left > 0) continue;

            int start = Integer.MAX_VALUE;
            for (CharSet charSet : map.values()) {
                start = Math.min(start, charSet.indices.peek());
            }
            if (minEnd < 0 || (i - start < minEnd - minStart)) {
                minStart = start;
                minEnd = i;
            }
        }
        return minEnd < 0 ? "" : s.substring(minStart, minEnd + 1);
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
