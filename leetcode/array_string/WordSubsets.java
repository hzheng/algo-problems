import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC916: https://leetcode.com/problems/word-subsets/
//
// We are given two arrays A and B of words.  Each word is a string of lowercase
// letters. Now, say that word b is a subset of word a if every letter in b
// occurs in a, including multiplicity.  For example, "wrr" is a subset of
// "warrior", but is not a subset of "world".
// Now say a word a from A is universal if for every b in B, b is a subset of a.
// Return a list of all universal words in A.  You can return the words in any
// order.
public class WordSubsets {
    // beats 39.13%(48 ms for 55 tests)
    public List<String> wordSubsets(String[] A, String[] B) {
        int[] counts = new int[26];
        for (String b : B) {
            int[] cnt = count(b);
            for (int i = 0; i < 26; i++) {
                counts[i] = Math.max(counts[i], cnt[i]);
            }
        }
        List<String> res = new ArrayList<>();
        for (String a : A) {
            if (isSubset(a, counts)) {
                res.add(a);
            }
        }
        return res;
    }

    private int[] count(String A) {
        int[] cnt = new int[26];
        for (char a : A.toCharArray()) {
            cnt[a - 'a']++;
        }
        return cnt;
    }

    private boolean isSubset(String a, int[] counts) {
        int[] cnt = count(a);
        for (int i = 0; i < 26; i++) {
            if (cnt[i] < counts[i]) return false;
        }
        return true;
    }

    void test(String[] A, String[] B, String[] expected) {
        assertArrayEquals(expected, wordSubsets(A, B).toArray());
    }

    @Test
    public void test() {
        test(
                new String[] {"amazon", "apple", "facebook", "google", "leetcode"},
                new String[] {"e", "o"},
                new String[] {"facebook", "google", "leetcode"});
        test(
                new String[] {"amazon", "apple", "facebook", "google", "leetcode"},
                new String[] {"l", "e"},
                new String[] {"apple", "google", "leetcode"});
        test(
                new String[] {"amazon", "apple", "facebook", "google", "leetcode"},
                new String[] {"e", "oo"},
                new String[] {"facebook", "google"});
        test(
                new String[] {"amazon", "apple", "facebook", "google", "leetcode"},
                new String[] {"lo", "eo"},
                new String[] {"google", "leetcode"});
        test(
                new String[] {"amazon", "apple", "facebook", "google", "leetcode"},
                new String[] {"ec", "oc", "ceo"},
                new String[] {"facebook", "leetcode"});
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
