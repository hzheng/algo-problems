import java.util.*;

import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.*;

// LC748: https://leetcode.com/problems/shortest-completing-word/
//
// Given a string licensePlate and an array of strings words, find the shortest completing word in
// words. A completing word is a word that contains all the letters in licensePlate. Ignore numbers
// and spaces in licensePlate, and treat letters as case insensitive. If a letter appears more than
// once in licensePlate, then it must appear in the word the same number of times or more.
// Return the shortest completing word in words. It is guaranteed an answer exists. If there are
// multiple shortest completing words, return the first one that occurs in words.
// Constraints:
// 1 <= licensePlate.length <= 7
// licensePlate contains digits, letters (uppercase or lowercase), or space ' '.
// 1 <= words.length <= 1000
// 1 <= words[i].length <= 15
//words[i] consists of lower case English letters.
public class ShortestCompletingWord {
    // Hash Table
    // time complexity: O(N), space complexity: O(1)
    // 9 ms(29.08%), 39.6 MB(8.15%) for 102 tests
    public String shortestCompletingWord(String licensePlate, String[] words) {
        Map<Character, Integer> chars = new HashMap<>();
        for (char c : licensePlate.toCharArray()) {
            if (Character.isLetter(c)) {
                c = Character.toLowerCase(c);
                chars.put(c, chars.getOrDefault(c, 0) + 1);
            }
        }
        String res = null;
        for (String word : words) {
            Map<Character, Integer> cs = new HashMap<>(chars);
            for (char c : word.toCharArray()) {
                if (cs.containsKey(c)) {
                    int count = cs.get(c) - 1;
                    if (count == 0) {
                        cs.remove(c);
                    } else {
                        cs.put(c, count);
                    }
                }
            }
            if (cs.isEmpty() && (res == null || word.length() < res.length())) {
                res = word;
            }
        }
        return res;
    }

    // Array
    // time complexity: O(N), space complexity: O(1)
    // 4 ms(55.16%), 39.6 MB(8.15%) for 102 tests
    public String shortestCompletingWord2(String licensePlate, String[] words) {
        int[] chars = new int[26];
        for (char c : licensePlate.toCharArray()) {
            if (Character.isLetter(c)) {
                chars[Character.toLowerCase(c) - 'a']++;
            }
        }
        String res = null;
        outer:
        for (String word : words) {
            int[] cs = chars.clone();
            for (char c : word.toCharArray()) {
                cs[c - 'a']--;
            }
            for (int count : cs) {
                if (count > 0) { continue outer; }
            }
            if (res == null || word.length() < res.length()) {
                res = word;
            }
        }
        return res;
    }

    private static final int[] PRIMES =
            {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83,
             89, 97, 101, 103};

    // Math
    // time complexity: O(N), space complexity: O(1)
    // 1 ms(100.00%), 39.4 MB(8.15%) for 102 tests
    public String shortestCompletingWord3(String licensePlate, String[] words) {
        long charProduct = product(licensePlate.toLowerCase()); // caution: may overflow
        String res = "aaaaaaaaaaaaaaaaaaaa"; // 16 a's
        for (String word : words) {
            if (word.length() < res.length() && product(word) % charProduct == 0) {
                res = word;
            }
        }
        return res;
    }

    private long product(String s) {
        long res = 1L;
        for (char c : s.toCharArray()) {
            int index = c - 'a';
            if (index >= 0 && index < 26) {
                res *= PRIMES[index];
            }
        }
        return res;
    }

    private void test(String licencePlate, String[] words, String expected) {
        assertEquals(expected, shortestCompletingWord(licencePlate, words));
        assertEquals(expected, shortestCompletingWord2(licencePlate, words));
        assertEquals(expected, shortestCompletingWord3(licencePlate, words));
    }

    @Test public void test() {
        test("1s3 PSt", new String[] {"step", "steps", "stripe", "stepple"}, "steps");
        test("1s3 456", new String[] {"looks", "pest", "stew", "show"}, "pest");
        test("Ah71752",
             new String[] {"suggest", "letter", "of", "husband", "easy", "education", "drug",
                           "prevent", "writer", "old"}, "husband");
        test("OgEu755",
             new String[] {"enough", "these", "play", "wide", "wonder", "box", "arrive", "money",
                           "tax", "thus"}, "enough");
        test("iMSlpe4",
             new String[] {"claim", "consumer", "student", "camera", "public", "never", "wonder",
                           "simple", "thought", "use"}, "simple");
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
