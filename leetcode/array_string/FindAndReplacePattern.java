import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC890: https://leetcode.com/problems/find-and-replace-pattern/
//
// You have a list of words and a pattern, and you want to know which words in
// words matches the pattern. A word matches the pattern if there exists a
// permutation of letters p so that after replacing every letter x in the
// pattern with p(x), we get the desired word.
// Note:
// 1 <= words.length <= 50
// 1 <= pattern.length = words[i].length <= 20
public class FindAndReplacePattern {
    // Hash Table
    // beats %(6 ms for 46 tests)
    public List<String> findAndReplacePattern(String[] words, String pattern) {
        List<String> res = new ArrayList<>();
        for (String w : words) {
            if (match(w, pattern)) {
                res.add(w);
            }
        }
        return res;
    }

    private boolean match(String word, String pattern) {
        Map<Character, Character> map = new HashMap<>();
        Map<Character, Character> rmap = new HashMap<>();
        for (int i = word.length() - 1; i >= 0; i--) {
            char w = word.charAt(i);
            char p = pattern.charAt(i);
            if (map.containsKey(w)) {
                if (map.get(w) != p || rmap.get(p) != w) return false;
            } else if (!rmap.containsKey(p)) {
                map.put(w, p);
                rmap.put(p, w);
            } else return false;
        }
        return true;
    }

    // Hash Table
    // beats %(8 ms for 46 tests)
    public List<String> findAndReplacePattern2(String[] words, String pattern) {
        List<String> res = new ArrayList<>();
        for (String w : words) {
            if (match2(w, pattern)) {
                res.add(w);
            }
        }
        return res;
    }

    private boolean match2(String word, String pattern) {
        Map<Character, Character> map = new HashMap<>();
        for (int i = 0; i < word.length(); i++) {
            char w = word.charAt(i);
            char p = pattern.charAt(i);
            if (!map.containsKey(w)) {
                map.put(w, p);
            }
            if (map.get(w) != p) return false;
        }
        boolean[] seen = new boolean[26];
        for (char p : map.values()) {
            if (seen[p - 'a']) return false;

            seen[p - 'a'] = true;
        }
        return true;
    }

    // Hash Table
    // beats %(8 ms for 46 tests)
    public List<String> findAndReplacePattern3(String[] words, String pattern) {
        int[] p = normalize(pattern);
        List<String> res = new ArrayList<String>();
        for (String w : words) {
            if (Arrays.equals(normalize(w), p)) {
                res.add(w);
            }
        }
        return res;
    }

    private int[] normalize(String w) {
        Map<Character, Integer> map = new HashMap<>();
        int n = w.length();
        int[] res = new int[n];
        int i = 0;
        for (char c : w.toCharArray()) {
            map.putIfAbsent(c, map.size());
            res[i++] = map.get(c);
        }
        return res;
    }

    // Hash Table
    // beats %(5 ms for 46 tests)
    public List<String> findAndReplacePattern4(String[] words, String pattern) {
        Map<Character, Integer> pMap = new HashMap<>();
        int n = pattern.length();
        int[] pat = new int[n];
        int i = 0;
        for (char c : pattern.toCharArray()) {
            pMap.putIfAbsent(c, pMap.size());
            pat[i++] = pMap.get(c);
        }
        List<String> res = new ArrayList<String>();
        outer : for (String w : words) {
            Map<Character, Integer> map = new HashMap<>();
            i = 0;
            for (char c : w.toCharArray()) {
                map.putIfAbsent(c, map.size());
                if (map.get(c) != pat[i++]) continue outer;
            }
            res.add(w);
        }
        return res;
    }

    // Hash Table
    // beats %(4 ms for 46 tests)
    public List<String> findAndReplacePattern5(String[] words, String pattern) {
        List<String> res = new LinkedList<>();
        outer : for (String w : words) {
            int[] p = new int[26];
            int[] s = new int[26];
            for (int i = w.length() - 1; i >= 0; i--) {
                if (s[w.charAt(i) - 'a'] != p[pattern.charAt(i) - 'a']) {
                    continue outer;
                } else {
                    s[w.charAt(i) - 'a'] = p[pattern.charAt(i) - 'a'] = i + 1;
                }
            }
            res.add(w);
        }
        return res;
    }

    void test(String[] words, String pattern, String[] expected) {
        assertEquals(Arrays.asList(expected),
                     findAndReplacePattern(words, pattern));
        assertEquals(Arrays.asList(expected),
                     findAndReplacePattern2(words, pattern));
        assertEquals(Arrays.asList(expected),
                     findAndReplacePattern3(words, pattern));
        assertEquals(Arrays.asList(expected),
                     findAndReplacePattern4(words, pattern));
        assertEquals(Arrays.asList(expected),
                     findAndReplacePattern5(words, pattern));
    }

    @Test
    public void test() {
        test(new String[] { "abc", "deq", "mee", "aqq", "dkd", "ccc" }, "abb",
             new String[] { "mee", "aqq" });
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
