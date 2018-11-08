import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;
import java.nio.file.Files;
import java.nio.file.Paths;

// LC126: https://leetcode.com/problems/word-ladder-ii/
//
// Given two words and a word list, find all shortest transformation sequence(s)
// from beginWord to endWord, such that:
// 1. Only one letter can be changed at a time
// 2. Each intermediate word must exist in the word list
public class WordLadder2 {
    static class WordPath {
        String word;
        List<String> path;

        WordPath(String word, List<String> path) {
            this.word = word;
            this.path = path;
        }
    }

    // BFS + Queue + Set
    // Time Limit Exceeded
    public List<List<String>> findLadders(String beginWord, String endWord, Set<String> wordList) {
        List<List<String>> res = new ArrayList<>();
        if (beginWord.equals(endWord)) {
            res.add(Arrays.asList(beginWord, endWord));
            return res;
        }

        Queue<WordPath> queue = new LinkedList<>();
        Map<String, Set<String>> cache = new HashMap<>();
        queue.add(new WordPath(beginWord, new ArrayList<>()));
        wordList.add(endWord);
        int maxLevel = Integer.MAX_VALUE;
        boolean found = false;
        while (!queue.isEmpty()) {
            WordPath wordPath = queue.poll();
            String word = wordPath.word;
            List<String> path = wordPath.path;
            path.add(word);
            if (found && path.size() >= maxLevel) break;

            Set<String> adjacency;
            if (cache.containsKey(word)) {
                adjacency = cache.get(word);
            } else {
                adjacency = oneEditSets(word, wordList);
                cache.put(word, adjacency);
            }
            for (String w : adjacency) {
                if (w.equals(endWord)) {
                    path.add(endWord);
                    res.add(new ArrayList<>(path));
                    maxLevel = path.size();
                    found = true;
                }
                if (!found && !path.contains(w)) {
                    queue.add(new WordPath(w, new ArrayList<>(path)));
                }
            }
        }
        return res;
    }

    private Set<String> oneEditSets(String word, Set<String> dict) {
        Set<String> words = new HashSet<>();
        char[] chars = word.toCharArray();
        for (int i = word.length() - 1; i >= 0; i--) {
            char ch = word.charAt(i);
            for (char c = 'a'; c <= 'z'; c++) {
                if (c != ch) {
                    chars[i] = c;
                    String str = new String(chars);
                    if (dict.contains(str)) {
                        words.add(str);
                    }
                }
            }
            chars[i] = ch;
        }
        return words;
    }

    // BFS + Queue + Hashtable + Set
    // beats 23.63%(372 ms)
    public List<List<String>> findLadders2(String beginWord, String endWord, Set<String> wordList) {
        List<List<String>> res = new ArrayList<>();
        if (beginWord.equals(endWord)) {
            res.add(Arrays.asList(beginWord, endWord));
            return res;
        }

        Queue<WordNode> queue = new LinkedList<>();
        queue.offer(new WordNode(beginWord, null));
        wordList.add(endWord);
        Set<String> visited = new HashSet<>();
        Map<String, Set<String>> cache = new HashMap<>();
        boolean found = false;
        for (int maxLevel = Integer.MAX_VALUE; !queue.isEmpty();) {
            WordNode node = queue.poll();
            String word = node.word;
            visited.add(word);
            if (found && node.level >= maxLevel) break;

            Set<String> adjacency;
            if (cache.containsKey(word)) {
                adjacency = cache.get(word);
            } else {
                adjacency = oneEditSets(word, wordList);
                cache.put(word, adjacency);
            }
            for (String w : adjacency) {
                if (w.equals(endWord)) {
                    List<String> path = new LinkedList<>();
                    for (; node != null; node = node.prev) {
                        path.add(0, node.word);
                    }
                    path.add(endWord);
                    res.add(path);
                    maxLevel = path.size();
                    found = true;
                    break;
                }
                if (!found && !visited.contains(w)) {
                    queue.offer(new WordNode(w, node));
                }
            }
        }
        return res;
    }

    static class WordNode {
        String word;
        WordNode prev;
        int level;

        WordNode(String word, WordNode prev) {
            this.word = word;
            this.prev = prev;
            level = (prev == null) ? 1 : prev.level + 1;
        }
    }

    // Time Limit Exceeded
    // DFS + BFS
    public List<List<String>> findLadders3(String beginWord, String endWord, Set<String> wordList) {
        List<List<String>> res = new ArrayList<>();
        wordList.add(endWord);
        for (int i = 0; res.size() == 0 && i < wordList.size(); i++) {
            List<String> path = new ArrayList<>();
            path.add(beginWord);
            depthSearch(beginWord, endWord, wordList, new HashMap<>(), path, i, res);
        }
        return res;
    }

    private void depthSearch(String beginWord, String endWord, Set<String> dict,
                             Map<String, Set<String>> cache, List<String> path, int len,
                             List<List<String>> res) {
        if (len == 0) {
            if (beginWord.equals(endWord)) {
                res.add(new ArrayList<>(path));
            }
            return;
        }

        Set<String> adjacency;
        if (cache.containsKey(beginWord)) {
            adjacency = cache.get(beginWord);
        } else {
            adjacency = oneEditSets(beginWord, dict);
            cache.put(beginWord, adjacency);
        }
        for (String w : adjacency) {
            if (!path.contains(w)) {
                path.add(w);
                depthSearch(w, endWord, dict, cache, path, len - 1, res);
                path.remove(path.size() - 1);
            }
        }
    }

    // Time Limit Exceeded
    // DFS + BFS
    public List<List<String>> findLadders4(String beginWord, String endWord, Set<String> wordList) {
        int level = ladderLength(beginWord, endWord, wordList) - 1;
        List<List<String>> res = new ArrayList<>();
        if (level < 0) return res;

        wordList.add(endWord);
        List<String> path = new ArrayList<>();
        path.add(beginWord);
        depthSearch(beginWord, endWord, wordList, new HashMap<>(), path, level, res);
        return res;
    }

    private int ladderLength(String beginWord, String endWord, Set<String> wordList) {
        if (beginWord.equals(endWord)) return 1;

        Set<String> visited = new HashSet<>();
        Set<String> beginSet = new HashSet<>();
        beginSet.add(beginWord);
        Set<String> endSet = new HashSet<>();
        endSet.add(endWord);
        for (int distance = 2; !beginSet.isEmpty() && !endSet.isEmpty(); distance++) {
            if (beginSet.size() > endSet.size()) {
                Set<String> tmp = beginSet;
                beginSet = endSet;
                endSet = tmp;
            }

            Set<String> nextSet = new HashSet<>();
            for (String word : beginSet) {
                char[] cs = word.toCharArray();
                for (int i = cs.length - 1; i >= 0; i--) {
                    for (char c = 'a'; c <= 'z'; c++) {
                        char old = cs[i];
                        cs[i] = c;
                        String target = String.valueOf(cs);
                        if (endSet.contains(target)) return distance;

                        if (!visited.contains(target) && wordList.contains(target)) {
                            nextSet.add(target);
                            visited.add(target);
                        }
                        cs[i] = old;
                    }
                }
            }
            beginSet = nextSet;
        }
        return 0;
    }

    // Solution of Choice
    // Two-end BFS + Recursion + Backtracking + Hashtable + Set
    // https://discuss.leetcode.com/topic/17975/super-fast-java-solution-two-end-bfs
    // beats 92.88%(42 ms)
    public List<List<String>> findLadders5(String beginWord, String endWord, Set<String> wordList) {
        Set<String> set1 = new HashSet<>();
        set1.add(beginWord);
        Set<String> set2 = new HashSet<>();
        set2.add(endWord);
        Map<String, List<String>> map = new HashMap<>();
        findPath(set1, set2, wordList, map, false);
        List<List<String>> res = new ArrayList<>();
        findLadders5(beginWord, endWord, map, new ArrayList<>(Arrays.asList(beginWord)), res);
        return res;
    }

    private boolean findPath(Set<String> set1, Set<String> set2, Set<String> dict,
                             Map<String, List<String>> map, boolean direction) {
        if (set1.size() > set2.size()) return findPath(set2, set1, dict, map, !direction);

        if (set1.isEmpty()) return false;

        dict.removeAll(set1);
        dict.removeAll(set2);
        boolean done = false;
        Set<String> nextSet = new HashSet<>();
        for (String word : set1) {
            char[] chars = word.toCharArray();
            for (int i = word.length() - 1; i >= 0; i--) {
                for (char c = 'a'; c <= 'z'; c++) {
                    chars[i] = c;
                    String newWord = new String(chars);
                    String key = direction ? newWord : word;
                    String val = direction ? word : newWord;
                    List<String> path = map.getOrDefault(key, new ArrayList<>());
                    if (set2.contains(newWord)) {
                        done = true;
                        path.add(val);
                        map.put(key, path);
                    } else if (!done && dict.contains(newWord)) {
                        nextSet.add(newWord);
                        path.add(val);
                        map.put(key, path);
                    }
                }
                chars[i] = word.charAt(i);
            }
        }
        return done || findPath(set2, nextSet, dict, map, !direction);
    }

    private void findLadders5(String start, String end, Map<String, List<String>> map,
                              List<String> path, List<List<String>> res) {
        if (start.equals(end)) {
            res.add(new ArrayList<>(path));
            return;
        }
        if (!map.containsKey(start)) return;

        for (String word : map.get(start)) {
            path.add(word);
            findLadders5(word, end, map, path, res);
            path.remove(path.size() - 1);
        }
    }

    interface Function<A, B, C, D> {
        public D apply(A a, B b, C c);
    }

    void test(Function<String, String, Set<String>, List<List<String>>> find, String name,
              String begin, String end, String[][] expected, String... words) throws Exception {
        Set<String> wordList;
        if (words.length == 0) {
            wordList = new HashSet<>(Files.readAllLines(Paths.get("/usr/share/dict/words")));
        } else {
            wordList = new HashSet<>(Arrays.asList(words));
        }
        long t1 = System.nanoTime();
        List<List<String>> res = find.apply(begin, end, wordList);
        res.sort((p, q) -> {
            for (int i = 0; i < p.size(); i++) {
                int diff = p.get(i).compareTo(q.get(i));
                if (diff != 0) return diff;
            }
            return 0;
        });
        Arrays.sort(expected, (p, q) -> {
            for (int i = 0; i < p.length; i++) {
                int diff = p[i].compareTo(q[i]);
                if (diff != 0) return diff;
            }
            return 0;
        });
        assertArrayEquals(expected,
                          res.stream().map(a -> a.toArray(new String[0])).toArray(String[][]::new));
        System.out.format("%s: %.3f ms\n", name, (System.nanoTime() - t1) * 1e-6);
    }

    public void test(String begin, String end, String[][] expected, String... words) {
        WordLadder2 w = new WordLadder2();
        try {
            test(w::findLadders, "findLadders", begin, end, expected, words);
            test(w::findLadders2, "findLadders2", begin, end, expected, words);
            test(w::findLadders3, "findLadders3", begin, end, expected, words);
            test(w::findLadders4, "findLadders4", begin, end, expected, words);
            test(w::findLadders5, "findLadders5", begin, end, expected, words);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test1() {
        test("talk", "tail", new String[][] {}, "talk", "tons", "fall", "tail", "gale", "hall",
             "negs");
        test("red", "tax",
             new String[][] {{"red", "rex", "tex", "tax"}, {"red", "ted", "tad", "tax"},
                             {"red", "ted", "tex", "tax"}},
             "ted", "tex", "red", "tax", "tad", "den", "rex", "pee");
        test("a", "c", new String[][] {{"a", "c"}}, "a", "b", "c");
        test("hit", "cog",
             new String[][] {{"hit", "hot", "dot", "dog", "cog"},
                             {"hit", "hot", "lot", "log", "cog"}},
             "hot", "dot", "dog", "lot", "log");
        test("qa", "sq",
             new String[][] {{"qa", "ba", "be", "se", "sq"}, {"qa", "ba", "bi", "si", "sq"},
                             {"qa", "ba", "br", "sr", "sq"}, {"qa", "ca", "cm", "sm", "sq"},
                             {"qa", "ca", "co", "so", "sq"}, {"qa", "la", "ln", "sn", "sq"},
                             {"qa", "la", "lt", "st", "sq"}, {"qa", "ma", "mb", "sb", "sq"},
                             {"qa", "pa", "ph", "sh", "sq"}, {"qa", "ta", "tc", "sc", "sq"},
                             {"qa", "fa", "fe", "se", "sq"}, {"qa", "ga", "ge", "se", "sq"},
                             {"qa", "ha", "he", "se", "sq"}, {"qa", "la", "le", "se", "sq"},
                             {"qa", "ma", "me", "se", "sq"}, {"qa", "na", "ne", "se", "sq"},
                             {"qa", "ra", "re", "se", "sq"}, {"qa", "ya", "ye", "se", "sq"},
                             {"qa", "ca", "ci", "si", "sq"}, {"qa", "ha", "hi", "si", "sq"},
                             {"qa", "la", "li", "si", "sq"}, {"qa", "ma", "mi", "si", "sq"},
                             {"qa", "na", "ni", "si", "sq"}, {"qa", "pa", "pi", "si", "sq"},
                             {"qa", "ta", "ti", "si", "sq"}, {"qa", "ca", "cr", "sr", "sq"},
                             {"qa", "fa", "fr", "sr", "sq"}, {"qa", "la", "lr", "sr", "sq"},
                             {"qa", "ma", "mr", "sr", "sq"}, {"qa", "fa", "fm", "sm", "sq"},
                             {"qa", "pa", "pm", "sm", "sq"}, {"qa", "ta", "tm", "sm", "sq"},
                             {"qa", "ga", "go", "so", "sq"}, {"qa", "ha", "ho", "so", "sq"},
                             {"qa", "la", "lo", "so", "sq"}, {"qa", "ma", "mo", "so", "sq"},
                             {"qa", "na", "no", "so", "sq"}, {"qa", "pa", "po", "so", "sq"},
                             {"qa", "ta", "to", "so", "sq"}, {"qa", "ya", "yo", "so", "sq"},
                             {"qa", "ma", "mn", "sn", "sq"}, {"qa", "ra", "rn", "sn", "sq"},
                             {"qa", "ma", "mt", "st", "sq"}, {"qa", "pa", "pt", "st", "sq"},
                             {"qa", "na", "nb", "sb", "sq"}, {"qa", "pa", "pb", "sb", "sq"},
                             {"qa", "ra", "rb", "sb", "sq"}, {"qa", "ta", "tb", "sb", "sq"},
                             {"qa", "ya", "yb", "sb", "sq"}, {"qa", "ra", "rh", "sh", "sq"},
                             {"qa", "ta", "th", "sh", "sq"}},
             "si", "go", "se", "cm", "so", "ph", "mt", "db", "mb", "sb", "kr", "ln", "tm", "le",
             "av", "sm", "ar", "ci", "ca", "br", "ti", "ba", "to", "ra", "fa", "yo", "ow", "sn",
             "ya", "cr", "po", "fe", "ho", "ma", "re", "or", "rn", "au", "ur", "rh", "sr", "tc",
             "lt", "lo", "as", "fr", "nb", "yb", "if", "pb", "ge", "th", "pm", "rb", "sh", "co",
             "ga", "li", "ha", "hz", "no", "bi", "di", "hi", "qa", "pi", "os", "uh", "wm", "an",
             "me", "mo", "na", "la", "st", "er", "sc", "ne", "mn", "mi", "am", "ex", "pt", "io",
             "be", "fm", "ta", "tb", "ni", "mr", "pa", "he", "lr", "sq", "ye");
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
