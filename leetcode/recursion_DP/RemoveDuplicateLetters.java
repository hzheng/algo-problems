// https://leetcode.com/problems/remove-duplicate-letters/

import java.util.*;
import java.util.Collection;
import java.util.PriorityQueue;
import java.util.function.Function;
import org.junit.Test;
import static org.junit.Assert.*;

//
// Given a string which contains only lowercase letters, remove
// duplicate letters so that every letter appears once and only once.
// You must make sure your result is the smallest in lexicographical order
// among all possible results.
public class RemoveDuplicateLetters {
    // DFS + backtracking
    // Time Limit Exceeded
    public String removeDuplicateLetters(String s) {
        int[] count = new int[26];
        for (char c : s.toCharArray()) {
            count[c - 'a']++;
        }
        int toBeRemoved = 0;
        for (int c : count) {
            if (c > 1) {
                toBeRemoved += (c - 1);
            }
        }
        if (toBeRemoved == 0) return s;

        Queue<String> res = new PriorityQueue<>();
        removeDuplicateLetters(s, count, toBeRemoved, new StringBuilder(s), res);
        return res.poll();
    }

    private void removeDuplicateLetters(String s, int[] count, int toBeRemoved,
                                        StringBuilder sb, Queue<String> res) {
        if (toBeRemoved == 0) {
            res.add(sb.toString());
            return;
        }

        int len = sb.length();
        for (int i = 0; i < len; i++) {
            char c = sb.charAt(i);
            int index = c - 'a';
            if (count[index] > 1) {
                count[index]--;
                sb.deleteCharAt(i);
                removeDuplicateLetters(s, count, toBeRemoved - 1, sb, res);
                sb.insert(i, c);
                count[index]++;
            }
        }
    }

    // Deque
    // beats 57.50%(9 ms)
    public String removeDuplicateLetters2(String s) {
        int[] counts = new int[26];
        for (char c : s.toCharArray()) {
            counts[c - 'a']++;
        }

        StringBuilder sb = new StringBuilder();
        Deque<Character> deque = new LinkedList<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            int index = c - 'a';
            int count = counts[index];
            if (count == 0) continue;

            if (deque.isEmpty()) {
                if (counts[index] == 1) {
                    sb.append(c);
                } else {
                    deque.offerLast(c);
                }
                continue;
            }
            // deque is not empty
            char d = deque.peekLast();
            if (count == 1 || (count == 2 && c == d)
                || (c < d && !deque.contains(c))) {
                while (!deque.isEmpty()) {
                    d = deque.peekLast();
                    if (c <= d) {
                        counts[d - 'a']--;
                        deque.pollLast();
                    } else break;
                }
                if (count != 1) {
                    i--;
                    continue;
                }

                while (!deque.isEmpty()) {
                    d = deque.pollFirst();
                    sb.append(d);
                    counts[d - 'a'] = 0;
                }
                sb.append(c);
            } else if (c > d) {
                deque.offerLast(c);
            } else if (c == d) {
                counts[c - 'a']--;
            } else if (--counts[c - 'a'] == 1) { // deque contains c
                while (c != d) {
                    d = deque.pollFirst();
                    sb.append(d);
                    counts[d - 'a'] = 0;
                }
            }
        }
        return sb.toString();
    }

    void test(Function<String, String> remove, String name,
              String s, String expected) {
        long t1 = System.nanoTime();
        assertEquals(expected, remove.apply(s));
        if (s.length() > 10) {
            System.out.format("%s: %.3f ms\n", name, (System.nanoTime() - t1) * 1e-6);
        }
    }

    void test(String s, String expected) {
        RemoveDuplicateLetters r = new RemoveDuplicateLetters();
        if (s.length() < 15) {
            test(r::removeDuplicateLetters, "removeDuplicateLetters", s, expected);
        }
        test(r::removeDuplicateLetters2, "removeDuplicateLetters2", s, expected);
    }

    @Test
    public void test1() {
        test("bccc", "bc");
        test("bcbac", "bac");
        test("cbacdcbc", "acdb");
        test("abacb", "abc");
        test("bcabc", "abc");
        test("bcabcabcdeab", "abcde");
        test("bcabcabcdeabcdefabccabc", "abcdef");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("RemoveDuplicateLetters");
    }
}
