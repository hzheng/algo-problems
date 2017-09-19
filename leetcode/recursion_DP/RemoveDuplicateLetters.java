// https://leetcode.com/problems/remove-duplicate-letters/

import java.util.*;
import java.util.PriorityQueue;
import java.util.function.Function;
import org.junit.Test;
import static org.junit.Assert.*;

// LC316: https://leetcode.com/problems/remove-duplicate-letters/
//
// Given a string which contains only lowercase letters, remove
// duplicate letters so that every letter appears once and only once.
// You must make sure your result is the smallest in lexicographical order
// among all possible results.
public class RemoveDuplicateLetters {
    // Hash Table + Heap + Recursion + DFS/Backtracking
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

    // Hash Table + Deque
    // time complexity: O(N), space complexity: O(1)
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

    // Hash Table + Stack
    // beats 81.45%(6 ms)
    public String removeDuplicateLetters3(String s) {
        boolean[] visited = new boolean[26];
        int[] counts = new int[26];
        for (char c : s.toCharArray()) {
            counts[c - 'a']++;
        }
        Stack<Character> stack = new Stack<>();
        for (char c : s.toCharArray()) {
            int index = c - 'a';
            counts[index]--;
            if (visited[index]) continue;

            while (!stack.isEmpty()) {
                char d = stack.peek();
                if (d > c && counts[d - 'a'] > 0) {
                    visited[stack.pop() - 'a'] = false;
                } else break;
            }
            stack.push(c);
            visited[index] = true;
        }
        StringBuilder sb = new StringBuilder();
        for (char c : stack) {
            sb.append(c);
        }
        return sb.toString();
    }

    // Hash Table + Recursion + Greedy
    // beats 10.73%(36 ms for 286 tests)
    public String removeDuplicateLetters4(String s) {
        int[] counts = new int[26];
        for (char c : s.toCharArray()) {
            counts[c - 'a']++;
        }
        int min = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) < s.charAt(min)) {
                min = i;
            }
            if (--counts[s.charAt(i) - 'a'] == 0) break;
        }
        return s.isEmpty() ? "" : s.charAt(min)
               + removeDuplicateLetters4(s.substring(min + 1)
                                         .replaceAll("" + s.charAt(min), ""));
    }

    // Hash Table
    // https://discuss.leetcode.com/topic/31413/easy-to-understand-iterative-java-solution
    // beats 35.09%(21 ms)
    public String removeDuplicateLetters5(String s) {
        if (s.length() <= 1) return s;

        Map<Character, Integer> lastPosMap = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            lastPosMap.put(s.charAt(i), i);
        }
        char[] res = new char[lastPosMap.size()];
        int begin = 0;
        int end = findMinLastPos(lastPosMap);
        for (int i = 0;; i++) {
            char minChar = 'z' + 1;
            for (int j = begin; j <= end; j++) {
                char c = s.charAt(j);
                if (lastPosMap.containsKey(c) && c < minChar) {
                    minChar = c;
                    begin = j + 1;
                }
            }
            res[i] = minChar;
            if (i == res.length - 1) return new String(res);

            lastPosMap.remove(minChar);
            if (s.charAt(end) == minChar) {
                end = findMinLastPos(lastPosMap);
            }
        }
    }

    private int findMinLastPos(Map<Character, Integer> lastPosMap) {
        if (lastPosMap.isEmpty()) return -1;

        int minLastPos = Integer.MAX_VALUE;
        for (int lastPos : lastPosMap.values()) {
            minLastPos = Math.min(minLastPos, lastPos);
        }
        return minLastPos;
    }

    // Solution of Choice
    // Hash Table
    // https://discuss.leetcode.com/topic/32172/c-simple-solution-easy-understanding/
    // beats 95.04%(4 ms for 286 tests)
    public String removeDuplicateLetters6(String s) {
        boolean[] visited = new boolean[26];
        int[] counts = new int[26];
        for (char c : s.toCharArray()) {
            counts[c - 'a']++;
        }
        StringBuilder sb = new StringBuilder("0"); // less than any letter
        for (char c : s.toCharArray()) {
            int index = c - 'a';
            counts[index]--;
            if (visited[index]) continue;

            while (true) {
                char end = sb.charAt(sb.length() - 1);
                if (c < end && counts[end - 'a'] > 0) {
                    visited[end - 'a'] = false;
                    sb.deleteCharAt(sb.length() - 1);
                } else break;
            }
            sb.append(c);
            visited[index] = true;
        }
        return sb.substring(1);
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
        test(r::removeDuplicateLetters3, "removeDuplicateLetters3", s, expected);
        test(r::removeDuplicateLetters4, "removeDuplicateLetters4", s, expected);
        test(r::removeDuplicateLetters5, "removeDuplicateLetters5", s, expected);
        test(r::removeDuplicateLetters6, "removeDuplicateLetters6", s, expected);
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
