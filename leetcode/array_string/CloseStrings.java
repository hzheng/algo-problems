import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1657: https://leetcode.com/problems/determine-if-two-strings-are-close/
//
// Two strings are close if you can attain one from the other by the following operations:
// Operation 1: Swap any two existing characters.
// Operation 2: Transform every occurrence of one existing character into another existing
// character, and do the same with the other character.
// You can use the operations on either string as many times as necessary.
// Given two strings, word1 and word2, return true if they are close, and false otherwise.
//
// Constraints:
// 1 <= word1.length, word2.length <= 10^5
// word1 and word2 contain only lowercase English letters.
public class CloseStrings {
    // Sort
    // time complexity: O(N), space complexity: O(1)
    // 12 ms(100.00%), 39.6 MB(91.67%) for 144 tests
    public boolean closeStrings(String word1, String word2) {
        int[] cnt1 = count(word1);
        int[] cnt2 = count(word2);
        for (int i = 0; i < cnt1.length; i++) {
            if (cnt1[i] != cnt2[i] && (cnt1[i] == 0 || cnt2[i] == 0)) {
                return false;
            }
        }
        Arrays.sort(cnt1);
        Arrays.sort(cnt2);
        return Arrays.equals(cnt1, cnt2);
    }

    private int[] count(String word) {
        int[] res = new int[26];
        for (char c : word.toCharArray()) {
            res[c - 'a']++;
        }
        return res;
    }

    // Sort + Set
    // time complexity: O(N), space complexity: O(1)
    // 13 ms(82.35%), 39.4 MB(91.67%) for 144 tests
    public boolean closeStrings2(String word1, String word2) {
        boolean[] set1 = new boolean[26];
        int[] cnt1 = new int[26];
        for (char c : word1.toCharArray()) {
            cnt1[c - 'a']++;
            set1[c - 'a'] = true;
        }
        boolean[] set2 = new boolean[26];
        int[] cnt2 = new int[26];
        for (char c : word2.toCharArray()) {
            cnt2[c - 'a']++;
            set2[c - 'a'] = true;
        }
        if (!Arrays.equals(set1, set2)) { return false; }

        Arrays.sort(cnt1);
        Arrays.sort(cnt2);
        return Arrays.equals(cnt1, cnt2);
    }

    // Sort + Hash Table
    // time complexity: O(N), space complexity: O(1)
    // 61 ms(35.29%), 40.6 MB(25.00%) for 144 tests
    public boolean closeStrings3(String word1, String word2) {
        Map<Character, Integer> map1 = new HashMap<>();
        for (char c : word1.toCharArray()) {
            map1.put(c, map1.getOrDefault(c, 0) + 1);
        }
        Map<Character, Integer> map2 = new HashMap<>();
        for (char c : word2.toCharArray()) {
            map2.put(c, map2.getOrDefault(c, 0) + 1);
        }
        if (!map1.keySet().equals(map2.keySet())) { return false; }

        List<Integer> values1 = new ArrayList<>(map1.values());
        List<Integer> values2 = new ArrayList<>(map2.values());
        Collections.sort(values1);
        Collections.sort(values2);
        return values1.equals(values2);
    }

    // Sort
    // time complexity: O(N), space complexity: O(1)
    // 12 ms(100.00%), 39.8 MB(83.33%) for 144 tests
    public boolean closeStrings4(String word1, String word2) {
        return Arrays.equals(countArray(word1), countArray(word2));
    }

    private int[] countArray(String str) {
        final int n = 26;
        int[] count = new int[n];
        for (char c : str.toCharArray()) {
            count[c - 'a']++;
        }
        for (int i = 0; i < n; i++) {
            if (count[i] == 0) { continue; }

            int maxIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (count[j] > count[maxIndex]) {
                    maxIndex = j;
                }
            }
            int tmp = count[i];
            count[i] = count[maxIndex];
            count[maxIndex] = tmp;
        }
        return count;
    }

    private void test(String word1, String word2, boolean expected) {
        assertEquals(expected, closeStrings(word1, word2));
        assertEquals(expected, closeStrings2(word1, word2));
        assertEquals(expected, closeStrings3(word1, word2));
        assertEquals(expected, closeStrings4(word1, word2));
    }

    @Test public void test() {
        test("abc", "bca", true);
        test("a", "aa", false);
        test("cabbba", "abbccc", true);
        test("uau", "ssx", false);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
