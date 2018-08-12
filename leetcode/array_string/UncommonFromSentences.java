import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC888: https://leetcode.com/problems/uncommon-words-from-two-sentences/
//
// We are given two sentences A and B.  A word is uncommon if it appears exactly
// once in one of the sentences, and does not appear in the other sentence.
// Return a list of all uncommon words.
public class UncommonFromSentences {
    // Hash Table
    // beats %(10 ms for 53 tests)
    public String[] uncommonFromSentences(String A, String B) {
        Map<String, Integer> map = new HashMap<>();
        List<String> res = new ArrayList<>();
        for (String s : A.split("\\s+")) {
            map.put(s, map.getOrDefault(s, 0) + 1);
        }
        for (String s : B.split("\\s+")) {
            map.put(s, map.getOrDefault(s, 0) + 1);
        }
        for (String s : map.keySet()) {
            if (map.get(s) == 1) {
                res.add(s);
            }
        }
        return res.toArray(new String[0]);
    }

    // Set
    // beats %(10 ms for 53 tests)
    public String[] uncommonFromSentences2(String A, String B) {
        Set<String> pool = new HashSet<>();
        Set<String> repeats = new HashSet<>();
        for (String s : A.split("\\s+")) {
            if (!pool.add(s)) {
                repeats.add(s);
            }
        }
        for (String s : B.split("\\s+")) {
            if (!pool.add(s)) {
                repeats.add(s);
            }
        }
        String[] res = new String[pool.size() - repeats.size()];
        int i = 0;
        for (String s : pool) {
            if (!repeats.contains(s)) {
                res[i++] = s;
            }
        }
        return res;
    }

    // Set
    // beats %(6 ms for 53 tests)
    public String[] uncommonFromSentences3(String A, String B) {
        Set<String> distinct = new HashSet<>();
        Set<String> common = new HashSet<>();
        for (String s : (A + " " + B).split(" ")) {
            if (common.contains(s) || !distinct.add(s)) {
                distinct.remove(s);
                common.add(s);
            }
        }
        return distinct.toArray(new String[0]);
    }

    void test(String A, String B, String[] expected) {
        assertArrayEquals(expected, uncommonFromSentences(A, B));
        assertArrayEquals(expected, uncommonFromSentences2(A, B));
        assertArrayEquals(expected, uncommonFromSentences3(A, B));
    }

    @Test
    public void test() {
        test("this apple is sweet", "this apple is sour", 
             new String[] { "sweet", "sour" });
        test("banana", "apple apple", new String[] { "banana" });
        test("a b b", "a a c", new String[] { "c" });
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
