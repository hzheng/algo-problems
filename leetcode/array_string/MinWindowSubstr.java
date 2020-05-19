import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC076: https://leetcode.com/problems/minimum-window-substring/
//
// Given a string S and a string T, find the minimum window in S which will
// contain all the characters in T in complexity O(n).
public class MinWindowSubstr {
    // Hashtable
    // beats 11.68%(73 ms)
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

    // Solution of Choice
    // Hashtable + Two Pointers
    // simpler than last solution, but need rescan partial source string.
    // time complexity: O(N), space complexity: O(N)
    // 3 ms(97.06%), 40 MB(100%) for 268 tests
    public String minWindow2(String s, String t) {
        int[] map = new int[128];
        for (char c : t.toCharArray()) {
            map[c]++;
        }
        int minStart = 0;
        int minLen = Integer.MAX_VALUE;
        for (int start = 0, end = 0, toMatch = t.length(), len = s.length(); end < len; end++) {
            if (map[s.charAt(end)]-- > 0) {
                toMatch--;
            }
            if (toMatch > 0) continue;

            for ( ; map[s.charAt(start)] < 0; start++) { // rescan from start
                map[s.charAt(start)]++;
            }
            if (end - start < minLen) {
                minStart = start;
                minLen = end - start;
            }
        }
        return minLen == Integer.MAX_VALUE ?
               "" : s.substring(minStart, minStart + minLen + 1);
    }

    // Hashtable + Two Pointers + Queue
    // one pass(no rescan source string)
    // beats 68.03%(14 ms)
    public String minWindow3(String s, String t) {
        int[] target = new int[128];
        for (char c : t.toCharArray()) {
            target[c]++;
        }
        int[] freq = new int[128];
        Queue<Integer> queue = new LinkedList<>();
        int minStart = 0;
        int minEnd = Integer.MAX_VALUE;
        for (int i = 0, toMatch = t.length(), len = s.length(); i < len; i++) {
            char c = s.charAt(i);
            int tgtCount = target[c];
            if (tgtCount == 0) continue;

            queue.offer(i);
            if (freq[c]++ >= tgtCount || --toMatch > 0) continue;

            while (!queue.isEmpty()) {
                int j = queue.poll();
                if (i - j < minEnd - minStart) {
                    minStart = j;
                    minEnd = i;
                }
                char startCh = s.charAt(j);
                if (--freq[startCh] < target[startCh]) {
                    toMatch++;
                    break;
                }
            }
        }
        return minEnd == Integer.MAX_VALUE ?
               "" : s.substring(minStart, minEnd + 1);
    }

    // Hashtable + Set + Two Pointers
    // time complexity: O(N), space complexity: O(N)
    // 14 ms(40.07%), 41.4 MB(100%) for 268 tests
    public String minWindow4(String s, String t) {
        int n = s.length();
        Map<Character, Integer> missingMap = new HashMap<>();
        Set<Character> missing = new HashSet<>();
        for (char c : t.toCharArray()) {
            missingMap.put(c, missingMap.getOrDefault(c, 0) + 1);
            missing.add(c);
        }
        int min = n + 1;
        int minStart = 0;
        for (int start = 0, end = 0; end < n; end++) {
            char c = s.charAt(end);
            if (!missingMap.containsKey(c)) continue;

            int count = missingMap.get(c)  - 1;
            missingMap.put(c, count);
            if (count == 0) {
                missing.remove(c);
            }
            for ( ; missing.size() == 0; start++) {
                if (end - start + 1 < min) {
                    min = end - start + 1;
                    minStart = start;
                }
                char toDel = s.charAt(start);
                if (missingMap.containsKey(toDel)) {
                    count = missingMap.get(toDel) + 1;
                    missingMap.put(toDel, count);
                    if (count > 0) {
                        missing.add(toDel);
                    }
                }
            }
        }
        return min > n ? "" : s.substring(minStart, minStart + min);
    }

    void test(String s, String t, String expected) {
        assertEquals(expected, minWindow(s, t));
        assertEquals(expected, minWindow2(s, t));
        assertEquals(expected, minWindow3(s, t));
        assertEquals(expected, minWindow4(s, t));
    }

    @Test
    public void test1() {
        test("bba", "ab", "ba");
        test("ADOBECODEBANC", "ABC", "BANC");
        test("cabwefgewcwaefgcf", "cae", "cwae");
        test("a", "aa", "");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MinWindowSubstr");
    }
}
