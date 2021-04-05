import org.junit.Test;

import static org.junit.Assert.*;

// LC1813: https://leetcode.com/problems/sentence-similarity-iii/
//
// A sentence is a list of words that are separated by a single space with no leading or trailing
// spaces. For example, "Hello World", "HELLO", "hello world hello world" are all sentences. Words
// consist of only uppercase and lowercase English letters.
// Two sentences sentence1 and sentence2 are similar if it is possible to insert an arbitrary
// sentence (possibly empty) inside one of these sentences such that the two sentences become equal.
// For example, sentence1 = "Hello my name is Jane" and sentence2 = "Hello Jane" can be made equal
// by inserting "my name is" between "Hello" and "Jane" in sentence2.
// Given two sentences sentence1 and sentence2, return true if sentence1 and sentence2 are similar.
// Otherwise, return false.
//
// Constraints:
// 1 <= sentence1.length, sentence2.length <= 100
// sentence1 and sentence2 consist of lowercase and uppercase English letters and spaces.
// The words in sentence1 and sentence2 are separated by a single space.
public class SentenceSimilarityIII {
    // time complexity: O(N+M), space complexity: O(1)
    // 0 ms(100.00%), 37.4 MB(%) for 112 tests
    public boolean areSentencesSimilar(String s1, String s2) {
        if (s1.length() < s2.length()) {
            String tmp = s1;
            s1 = s2;
            s2 = tmp;
        }
        int l1 = s1.length();
        int l2 = s2.length();
        int diff = l1 - l2;
        if (diff == 0) { return s1.equals(s2); }

        for (int i = -1; i + diff <= l1; i++) {
            if (i >= 0 && s1.charAt(i) != ' ') { continue; }
            if (i + diff < l1 && s1.charAt(i + diff) != ' ') { continue; }

            String a = (i >= 0) ? s1.substring(0, i) : "";
            String b = (i + diff == l1) ? "" : s1.substring(i + diff + (i >= 0 ? 0 : 1));
            if (s2.equals((a + b))) { return true; }
        }
        return false;
    }

    // time complexity: O(N+M), space complexity: O(N+M)
    // 1 ms(%), 37.4 MB(%) for 112 tests
    public boolean areSentencesSimilar2(String s1, String s2) {
        if (s1.length() > s2.length()) {
            String tmp = s1;
            s1 = s2;
            s2 = tmp;
        }
        String[] words1 = s1.split(" ");
        String[] words2 = s2.split(" ");
        int i = 0;
        int n1 = words1.length;
        int n2 = words2.length;
        if (n1 > n2) { return false; }

        for (; i < n1 && words1[i].equals(words2[i]); i++) {}
        for (; i < n1 && words1[i].equals(words2[n2 - n1 + i]); i++) {}
        return i == n1;
    }

    private void test(String s1, String s2, boolean expected) {
        assertEquals(expected, areSentencesSimilar(s1, s2));
        assertEquals(expected, areSentencesSimilar2(s1, s2));
    }

    @Test public void test() {
        test("My name is Haley", "My Haley", true);
        test("of", "A lot of words", false);
        test("Eating right now", "Eating", true);
        test("Are You Okay", "are you okay", false);
        test("xD iP tqchblXgqvNVdi", "FmtdCzv Gp YZf UYJ xD iP tqchblXgqvNVdi", true);
        test("UI wqhw Lf", "ezzXqqEQcS", false);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
