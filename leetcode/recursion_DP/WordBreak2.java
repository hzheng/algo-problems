import java.lang.reflect.Array;
import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC140: https://leetcode.com/problems/word-break-ii/
//
// Given a string s and a dictionary of words dict, add spaces in s to construct
// a sentence where each word is a valid dictionary word.
// Return all such possible sentences.
public class WordBreak2 {
    // Solution of Choice
    // Dynamic Programming(Bottom-up) + DFS + Backtracking + Recursion
    // beats 89.04%(9 ms for 39 tests)
    public List<String> wordBreak(String s, Set<String> wordDict) {
        // LeetCode changed wordDict's type from Set<String> to List<String>
        // Set<String> words = new HashSet<>();
        // for (String word : wordDict) {
        // words.add(word);
        // }
        List<String> res = new LinkedList<>();
        // will TLE if not check this
        if (canBreak(s, wordDict)) {
            wordBreak(s, wordDict, 0, new StringBuilder(), res);
        }
        return res;
    }

    private void wordBreak(String s, Set<String> wordDict, int start, StringBuilder sb,
                           List<String> res) {
        int len = s.length();
        if (start == len) {
            res.add(sb.toString());
            return;
        }

        for (int i = start + 1, size = sb.length(); i <= len; i++) {
            String segment = s.substring(start, i);
            if (wordDict.contains(segment)) {
                if (size > 0) {
                    sb.append(" ");
                }
                sb.append(segment);
                wordBreak(s, wordDict, i, sb, res);
                sb.setLength(size);
            }
        }
    }

    // Dynamic Programming(Bottom-up) + DFS + Backtracking + Recursion
    // beats 96.61%(7 ms for 39 tests)
    public List<String> wordBreak2(String s, Set<String> wordDict) {
        List<String> res = new LinkedList<>();
        if (!canBreak(s, wordDict)) return res;

        wordBreak2(s, wordDict, 0, new StringBuilder(), res, new HashMap<>());
        return res;
    }

    private void wordBreak2(String s, Set<String> wordDict, int start, StringBuilder sb,
                            List<String> res, Map<Integer, List<String>> cache) {
        int len = s.length();
        if (start == len) {
            res.add(sb.toString());
            return;
        }

        List<String> candidates = cache.get(start);
        if (candidates == null) {
            candidates = new LinkedList<>();
            for (int i = start + 1; i <= len; i++) {
                String segment = s.substring(start, i);
                if (wordDict.contains(segment)) {
                    candidates.add(segment);
                }
            }
            cache.put(start, candidates);
        }
        int size = sb.length();
        for (String candidate : candidates) {
            if (size > 0) {
                sb.append(" ");
            }
            sb.append(candidate);
            wordBreak2(s, wordDict, start + candidate.length(), sb, res, cache);
            // sb.delete(size, sb.length());
            sb.setLength(size);
        }
    }

    private boolean canBreak(String s, Set<String> wordDict) {
        int len = s.length();
        boolean[] dp = new boolean[len + 1];
        dp[0] = true;
        for (int i = 0; i < len; i++) {
            for (int j = i; j >= 0; j--) {
                if (dp[j] && wordDict.contains(s.substring(j, i + 1))) {
                    dp[i + 1] = true;
                    break;
                }
            }
        }
        return dp[len];
    }

    // Dynamic Programming(Bottom-up) + DFS + Backtracking + Recursion
    // beats 61.48%(13 ms for 39 tests)
    public List<String> wordBreak3(String s, Set<String> wordDict) {
        int len = s.length();
        @SuppressWarnings("unchecked")
        List<String>[] dp = (List<String>[])Array.newInstance(List.class, len + 1);
        dp[0] = Collections.emptyList();
        for (int i = 0; i < len; i++) {
            List<String> words = null;
            for (int j = i; j >= 0; j--) {
                if (dp[j] == null) continue;

                String word = s.substring(j, i + 1);
                if (wordDict.contains(word)) {
                    if (words == null) {
                        words = new LinkedList<>();
                        dp[i + 1] = words;
                    }
                    words.add(word);
                }
            }
        }
        List<String> res = new LinkedList<>();
        if (dp[len] == null) return res;

        wordBreak3(dp, len, new LinkedList<String>(), res);
        return res;
    }

    private void wordBreak3(List<String>[] dp, int end, List<String> words, List<String> res) {
        if (end == 0) {
            res.add(String.join(" ", words));
            return;
        }

        for (String word : dp[end]) {
            words.add(0, word);
            wordBreak3(dp, end - word.length(), words, res);
            words.remove(0);
        }
    }

    // DFS + Recursion + Dynamic Programming(Top-down)
    // beats 64.77%(12 ms for 39 tests)
    public List<String> wordBreak4(String s, Set<String> wordDict) {
        return dfs(s, wordDict, new HashMap<>());
    }

    private List<String> dfs(String s, Set<String> words, Map<String, List<String>> memo) {
        if (memo.containsKey(s)) return memo.get(s);

        List<String> res = new ArrayList<>();
        if (words.contains(s)) {
            res.add(s);
        }
        for (int i = 1, len = s.length(); i < len; i++) {
            String prefix = s.substring(0, i);
            if (!words.contains(prefix)) continue;

            for (String a : dfs(s.substring(i), words, memo)) {
                res.add(prefix + " " + a);
            }
        }
        memo.put(s, res);
        return res;
    }

    // DFS + Recursion + Dynamic Programming(Top-down)
    // beats 96.61%(7 ms for 39 tests)
    public List<String> wordBreak5(String s, Set<String> wordDict) {
        return dfs(s, new ArrayList<>(wordDict), new HashMap<>());
    }

    private List<String> dfs(String s, List<String> wordDict, Map<String, List<String>> map) {
        if (map.containsKey(s)) return map.get(s);

        List<String> res = new LinkedList<>();
        if (s.length() == 0) {
            res.add("");
            return res;
        }
        for (String word : wordDict) {
            if (!s.startsWith(word)) continue;

            List<String> list = dfs(s.substring(word.length()), wordDict, map);
            for (String str : list) {
                res.add(word + (str.isEmpty() ? "" : " ") + str);
            }
        }
        map.put(s, res);
        return res;
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(Function<String, Set<String>, List<String>> wordBreak, String name, String s,
              String[] dict, String... expected) {
        Set<String> wordDict = new HashSet<>(Arrays.asList(dict));
        Arrays.sort(expected);

        long t1 = System.nanoTime();
        String[] res = wordBreak.apply(s, wordDict).toArray(new String[0]);
        System.out.format("%s: %.3f ms\n", name, (System.nanoTime() - t1) * 1e-6);
        Arrays.sort(res);
        assertArrayEquals(expected, res);
    }

    void test(String s, String[] dict, String... expected) {
        WordBreak2 w = new WordBreak2();
        test(w::wordBreak, "wordBreak", s, dict, expected);
        test(w::wordBreak2, "wordBreak2", s, dict, expected);
        test(w::wordBreak3, "wordBreak3", s, dict, expected);
        test(w::wordBreak4, "wordBreak4", s, dict, expected);
        test(w::wordBreak5, "wordBreak5", s, dict, expected);
    }

    @Test
    public void test1() {
        test("catsanddog", new String[] {"cat", "cats", "and", "sand", "dog"}, "cats and dog",
             "cat sand dog");
        test("aaaaaaa", new String[] {"aaaa", "aaa"}, "aaaa aaa", "aaa aaaa");
        // test("aaaaaaaaaaaaaaaaaaaaaaa",
        // new String[] {"a", "aa", "aaa", "aaaa", "aaaaa", "aaaaaa",
        // "aaaaaaa", "aaaaaaaa", "aaaaaaaaa", "aaaaaaaaaa"});
        test("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaabaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaa",
             new String[] {"a", "aa", "aaa", "aaaa", "aaaaa", "aaaaaa", "aaaaaaa", "aaaaaaaa",
                           "aaaaaaaaa", "aaaaaaaaaa"});
        test("aggegbnngohbggalojckbdfjakgnnjadhganfdkefeddjdnabmflabckflfljafdlmmbhijojiaaife"
             + "daihnoinedhhnolcjdam",
             new String[] {"o", "b", "gbdfgiokkfnhl", "glibjohcmd", "bblcnhelanckn",
                           "mflabckflflja", "mgda", "oheafhajjo", "cc", "cffalholojikojm",
                           "haljiamccabh", "gjkdlonmhdacd", "ee", "bc", "mjj", "fdlmmbhij", "nn",
                           "jiaaifedaihn", "nhligg", "hooaglldlei", "hajhebh", "ebijeeh", "me",
                           "eibm", "ekkobhajgkem", "ohaofonhjakc", "n", "kjjogm", "mhn",
                           "odcamjmodie", "edmagbkejiocacl", "kcbfnjialef", "lhifcohoe", "akgnn",
                           "fbgakjhjb", "belggjekmn", "oinedhhnolc", "ddekcnag", "oneoakldakalb",
                           "bodnokemafkhkhf", "dkefeddjdnab", "gflcngff", "fgnfmbcogmojgm", "ad",
                           "jadhganf", "lojckbdfj", "gadkaoe", "jdam", "ljjndlnednnombl",
                           "aggegbnngohbgga"},
             "aggegbnngohbgga lojckbdfj akgnn jadhganf dkefeddjdnab mflabckflflja fdlmmbhij o jiaaifedaihn oinedhhnolc jdam");
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
