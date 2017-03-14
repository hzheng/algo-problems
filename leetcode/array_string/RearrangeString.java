import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

// LC358: https://leetcode.com/problems/rearrange-string-k-distance-apart
//
// Given a non-empty string s and an integer k, rearrange the string such that the
// same characters are at least distance k from each other.
// All input strings are given in lowercase letters. If it is not possible to
// rearrange the string, return an empty string "".
public class RearrangeString {
    // Greedy + Priority Queue + Hash Table
    // beats 88.71%(13 ms for 57 tests)
    public String rearrangeString(String s, int k) {
        int[] freqs = new int[26];
        for (char c : s.toCharArray()) {
            freqs[c - 'a']++;
        }
        PriorityQueue<int[]> pq = new PriorityQueue<>(new Comparator<int[]>(){
            public int compare(int[] a, int[] b) {
                return b[0] - a[0];
            }
        });
        for (int i = 0; i < freqs.length; i++) {
            if (freqs[i] > 0) {
                pq.offer(new int[] {freqs[i], 'a' + i});
            }
        }
        int n = s.length();
        char[] buf = new char[n];
        for (int i = 0, step = k - 1; !pq.isEmpty(); ) {
            int[] head = pq.poll();
            int count = head[0];
            for (int first = -1; count > 0; i = ++i % n) {
                if (buf[i] != 0) continue;

                if (first < 0) {
                    first = i;
                } else if (Math.abs(first - i) <= step) return "";
                buf[i] = (char)head[1];
                if (--count > 0) {
                    i = (i + step < n) ? i + step : 0; // wrong: i += k;
                }
            }
            if (count > 0) return "";
        }
        return new String(buf);
    }

    // Greedy + Hash Table
    // beats 72.23%(18 ms for 57 tests)
    public String rearrangeString2(String s, int k) {
        int n = s.length();
        int[] freqs = new int[26];
        for (int i = 0; i < n; i++) {
            freqs[s.charAt(i) - 'a']++;
        }
        int[] offsets = new int[26];
        char[] buf = new char[n];
        for (int i = 0, j = 0; i < n; i++) {
            int pos = nextVacant(i, freqs, offsets);
            if (pos < 0) return "";

            freqs[pos]--;
            offsets[pos] = i + k;
            buf[j++] = (char)('a' + pos);
        }
        return new String(buf);
    }

    private int nextVacant(int index, int[] freqs, int[] offsets) {
        int max = 0;
        int pos = -1;
        for (int i = 0; i < freqs.length; i++) {
            if (freqs[i] > max && index >= offsets[i]) {
                max = freqs[i];
                pos = i;
            }
        }
        return pos;
    }

    // Greedy + Priority Queue + Queue + Hash Table
    // beats 57.79%(34 ms for 57 tests)
    public String rearrangeString3(String s, int k) {
        int[] freqs = new int[26];
        for (char c : s.toCharArray()) {
            freqs[c - 'a']++;
        }
        PriorityQueue<int[]> pq = new PriorityQueue<>(new Comparator<int[]>(){
            public int compare(int[] a, int[] b) {
                return b[0] - a[0];
            }
        });
        for (int i = 0; i < freqs.length; i++) {
            if (freqs[i] > 0) {
                pq.offer(new int[] {freqs[i], 'a' + i});
            }
        }
        int n = s.length();
        char[] buf = new char[n];
        Queue<int[]> throttle = new LinkedList<>();
        int index = 0;
        while (!pq.isEmpty()) {
            int[] head = pq.poll();
            buf[index++] = (char)head[1];
            head[0]--;
            throttle.offer(head);
            if (throttle.size() >= k) { // release
                int[] next = throttle.poll();
                if (next[0] > 0) {
                    pq.offer(next);
                }
            }
        }
        return index == n ? new String(buf) : "";
    }

    // Greedy + Priority Queue + List + Hash Table
    // beats 50.79%(50 ms for 57 tests)
    public String rearrangeString4(String s, int k) {
        if (k < 2) return s;

        int[] freqs = new int[26];
        for (char c : s.toCharArray()) {
            freqs[c - 'a']++;
        }
        PriorityQueue<int[]> pq = new PriorityQueue<>(new Comparator<int[]>(){
            public int compare(int[] a, int[] b) {
                return a[0] != b[0] ? b[0] - a[0] : a[1] - b[1]; // stability matters
            }
        });
        for (int i = 0; i < freqs.length; i++) {
            if (freqs[i] > 0) {
                pq.offer(new int[] {freqs[i], 'a' + i});
            }
        }
        int n = s.length();
        char[] buf = new char[n];
        for (int index = 0; !pq.isEmpty(); ) {
            int count = Math.min(k, n - index);
            if (pq.size() < count) return "";

            List<int[]> next = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                int[] head = pq.poll();
                buf[index++] = (char)head[1];
                if (--head[0] > 0) {
                    next.add(head);
                }
            }
            pq.addAll(next);
        }
        return new String(buf);
    }

    void test(String s, int k, String ... expected) {
        // assertTrue(Arrays.asList(expected).contains(rearrangeString(s, k)));
        assertThat(Arrays.asList(expected), hasItem(rearrangeString(s, k)));
        assertThat(Arrays.asList(expected), hasItem(rearrangeString2(s, k)));
        assertThat(Arrays.asList(expected), hasItem(rearrangeString3(s, k)));
        assertThat(Arrays.asList(expected), hasItem(rearrangeString4(s, k)));
    }

    @Test
    public void test() {
        test("a", 0, "a");
        test("aa", 0, "aa");
        test("aa", 1, "aa");
        test("ab", 1, "ab");
        test("aa", 2, "");
        test("aaa", 2, "");
        test("aaabc", 3, "");
        test("abb", 2, "bab");
        test("aabbc", 3, "abcab"); // try to contribute Testcase for Leetcode
        test("aabbcc", 2, "abcabc", "abacbc", "acbacb", "acbcab");
        test("aabbcc", 3, "abcabc", "acbacb");
        test("aaadbbcc", 2, "abacabcd", "abcabcda", "acacabdb", "ababacdc", "abcabcad", "abcabacd");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("RearrangeString");
    }
}
