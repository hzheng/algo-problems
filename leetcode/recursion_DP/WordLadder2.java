import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;
import java.nio.file.Files;
import java.nio.file.Paths;

// https://leetcode.com/problems/word-ladder-ii/
//
// Given two words and a word list, find all shortest transformation sequence(s)
// from beginWord to endWord, such that:
// 1. Only one letter can be changed at a time
// 2. Each intermediate word must exist in the word list
public class WordLadder2 {
    // Time Limit Exceeded
    static class WordPath {
        String word;
        List<String> path;

        WordPath(String word, List<String> path) {
            this.word = word;
            this.path = path;
        }
    }

    public List<List<String> > findLadders(String beginWord, String endWord,
                                           Set<String> wordList) {
        List<List<String> > res = new ArrayList<>();
        if (beginWord.equals(endWord)) {
            res.add(Arrays.asList(beginWord, endWord));
            return res;
        }

        Queue<WordPath> queue = new LinkedList<>();
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

            for (String w : oneEditSets(word, wordList)) {
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
        for (int i = 0; i < word.length(); i++) {
            char[] chars = word.toCharArray();
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
        }
        return words;
    }

    // Time Limit Exceeded
    public List<List<String> > findLadders2(String beginWord, String endWord,
                                            Set<String> wordList) {
        List<List<String> > res = new ArrayList<>();
        if (beginWord.equals(endWord)) {
            res.add(Arrays.asList(beginWord, endWord));
            return res;
        }

        Queue<WordNode> queue = new LinkedList<>();
        queue.add(new WordNode(beginWord, null));
        wordList.add(endWord);
        int maxLevel = Integer.MAX_VALUE;
        boolean found = false;
        Set<String> visited = new HashSet<>();
        while (!queue.isEmpty()) {
            WordNode node = queue.poll();
            String word = node.word;
            int level = 0;
            for (WordNode n = node; n != null; n = n.prev) {
                visited.add(n.word);
                level++;
            }
            if (found && level >= maxLevel) break;

            for (String w : oneEditSets(word, wordList)) {
                if (w.equals(endWord)) {
                    List<String> path = new LinkedList<>();
                    while (node != null) {
                        path.add(0, node.word);
                        node = node.prev;
                    }
                    path.add(endWord);
                    res.add(path);
                    maxLevel = path.size();
                    found = true;
                    break;
                }
                if (!found && !visited.contains(w)) {
                    queue.add(new WordNode(w, node));
                }
            }
        }
        return res;
    }

    static class WordNode {
        String word;
        WordNode prev;

        WordNode(String word, WordNode prev) {
            this.word = word;
            this.prev = prev;
        }
    }

    // DFS + BFS
    public List<List<String> > findLadders3(String beginWord, String endWord,
                                            Set<String> wordList) {
        List<List<String> > res = new ArrayList<>();
        wordList.add(endWord);
        for (int i = 0; res.size() == 0 && i < wordList.size(); i++) {
            List<String> path = new ArrayList<>();
            path.add(beginWord);
            depthSearch(beginWord, endWord, wordList, path, i, res);
        }
        return res;
    }

    private void depthSearch(String beginWord, String endWord, Set<String> dict,
                             List<String> path, int len, List<List<String>> res) {
        if (len == 0) {
            if (beginWord.equals(endWord)) {
                path = new ArrayList<>(path);
                res.add(path);
            }
            return;
        }

        for (String w : oneEditSets(beginWord, dict)) {
            if (!path.contains(w)) {
                path.add(w);
                depthSearch(w, endWord, dict, path, len - 1, res);
                path.remove(path.size() - 1);
            }
        }
    }

    interface Function<A, B, C, D> {
        public D apply(A a, B b, C c);
    }

    void test(Function<String, String, Set<String>, List<List<String>>> find,
              String name, String begin, String end, String[][] expected,
              String ... words) throws Exception {
        Set<String> wordList;
        if (words.length == 0) {
            wordList = new HashSet<>(
                Files.readAllLines(Paths.get("/usr/share/dict/words")));
        } else {
            wordList = new HashSet<>(Arrays.asList(words));
        }
        long t1 = System.nanoTime();
        List<List<String> > res = find.apply(begin, end, wordList);
        res.sort((p, q) -> {
            for (int i = 0; i < p.size(); i++) {
                int diff = p.get(i).compareTo(q.get(i));
                if (diff != 0) return diff;
            }
            return 0;
        });
        assertArrayEquals(expected,
                          res.stream().map(a -> a.toArray(new String[0]))
                          .toArray(String[][]::new));
        System.out.format("%s: %.3f ms\n", name, (System.nanoTime() - t1) * 1e-6);
    }

    public void test(String begin, String end, String[][] expected,
                     String ... words) {
        WordLadder2 w = new WordLadder2();
        try {
            test(w::findLadders, "findLadders", begin, end, expected, words);
            test(w::findLadders2, "findLadders2", begin, end, expected, words);
            test(w::findLadders3, "findLadders3", begin, end, expected, words);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test1() {
        test("red", "tax", new String[][] {{"red", "rex", "tex", "tax"},
                                           {"red", "ted", "tad", "tax"},
                                           {"red", "ted", "tex", "tax"}},
             "ted", "tex", "red", "tax", "tad", "den", "rex", "pee");
        test("a", "c", new String[][] {{"a", "c"}}, "a", "b", "c");
        test("hit", "cog", new String[][] {{"hit", "hot", "dot", "dog", "cog"},
                                           {"hit", "hot", "lot", "log", "cog"}},
             "hot", "dot", "dog", "lot", "log");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("WordLadder2");
    }
}
