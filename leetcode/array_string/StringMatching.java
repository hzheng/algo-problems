import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

// LC1408: https://leetcode.com/problems/string-matching-in-an-array/
//
// Given an array of string words. Return all strings in words which is substring of another word
// in any order. String words[i] is substring of words[j], if can be obtained removing some
// characters to left and/or right side of words[j].
// Constraints:
//
// 1 <= words.length <= 100
// 1 <= words[i].length <= 30
// words[i] contains only lowercase English letters.
// It's guaranteed that words[i] will be unique.
public class StringMatching {
    // Sort
    // time complexity: O(N ^ 2), space complexity: O(log(N))
    // 4 ms(72.29%), 39.9 MB(100%) for 66 tests
    public List<String> stringMatching(String[] words) {
        Arrays.sort(words, Comparator.comparingInt(String::length));
        Set<String> res = new HashSet<>();
        for (int i = 0, n = words.length; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (words[j].contains(words[i])) {
                    res.add(words[i]);
                }
            }
        }
        return new ArrayList<>(res);
    }

    // time complexity: O(N ^ 2), space complexity: O(1)
    // 4 ms(72.29%), 39.9 MB(100%) for 66 tests
    public List<String> stringMatching2(String[] words) {
        Set<String> res = new HashSet<>();
        for (String w1 : words) {
            for (String w2 : words) {
                if (w1 != w2 && w1.contains(w2)) { // compare string reference on purpose
                    res.add(w2);
                }
            }
        }
        return new ArrayList<>(res);
    }

    // TODO: KMP
    // TODO: Tries

    @Test public void test() {
        test(new String[] {"leetcoder", "leetcode", "od", "hamlet", "am"},
             new String[] {"leetcode", "od", "am"});
        test(new String[] {"mass", "as", "hero", "superhero"}, new String[] {"as", "hero"});
        test(new String[] {"leetcode", "et", "code"}, new String[] {"et", "code"});
        test(new String[] {"blue", "green", "bu"}, new String[] {});
    }

    private void test(String[] words, String[] expected) {
        Set<String> expectedSet = new HashSet<>(Arrays.asList(expected));
        assertEquals(expectedSet, new HashSet<>(stringMatching2(words)));
        assertEquals(expectedSet, new HashSet<>(stringMatching(words)));
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
