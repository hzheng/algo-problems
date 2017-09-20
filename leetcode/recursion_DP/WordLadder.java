import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;
import java.nio.file.Files;
import java.nio.file.Paths;

// LC127: https://leetcode.com/problems/word-ladder/
// Cracking the Coding Interview(5ed) Problem 18.10
//
// Given two words and a dictionary, find the length of shortest transformation
// sequence from beginWord to endWord, such that:
// 1. Only one letter can be changed at a time
// 2. Each intermediate word must exist in the word list
public class WordLadder {
    // DFS + Memoization
    // Time Limit Exceeded
    public int ladderLength(String beginWord, String endWord,
                            Set<String> wordList) {
        int len = beginWord.length();
        if (endWord.length() != len) return 0;

        // Map<String, Integer> cache = new HashMap<>();
        return ladderLength(beginWord, endWord, len, wordList,
                            new HashSet<>(), new HashMap<>());
    }

    private int ladderLength(String w1, String w2, int len, Set<String> wordList,
                             Set<String> visited, Map<String, Integer> cache) {
        if (w1.equals(w2)) return 1;

        if (cache.containsKey(w1)) return cache.get(w1);

        if (visited.contains(w1)) return 0;

        visited.add(w1);
        char[] chars = w1.toCharArray();
        int count = Integer.MAX_VALUE;
        for (int i = 0; i < len && count != 1; i++) {
            char ch = chars[i];
            for (char c = 'a'; c <= 'z'; c++) {
                if (c == ch) continue;

                chars[i] = c;
                String nextStr = new String(chars);
                if (nextStr.equals(w2)) {
                    count = 1;
                    break;
                }
                if (wordList.contains(nextStr)) {
                    int nextCount = ladderLength(nextStr, w2, len,
                                                 wordList, visited, cache);
                    if (nextCount > 0 && nextCount < count) {
                        count = nextCount;
                    }
                }
            }
            chars[i] = ch;
        }
        if (count == Integer.MAX_VALUE || count == 0) {
            count = 0;
        } else {
            count++;
        }
        cache.put(w1, count);
        return count;
    }

    // BFS + Queue + Set
    // BFS is better that DFS since it guarantees the optimal result
    // beats 38.27%(128 ms)
    public int ladderLength2(String beginWord, String endWord,
                             Set<String> wordList) {
        if (beginWord.equals(endWord)) return 1;

        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        queue.add(beginWord);
        visited.add(beginWord);
        wordList.add(endWord);
        int level = 2;
        int curCount = 1;
        int nextCount = 0;
        while (!queue.isEmpty()) {
            String word = queue.poll();
            if (--curCount < 0) {
                level++;
                curCount = nextCount - 1;
                nextCount = 0;
            }
            for (String w : oneEditSets(word, wordList)) {
                if (w.equals(endWord)) return level;

                if (!visited.contains(w)) {
                    queue.add(w);
                    visited.add(w);
                    nextCount++;
                }
            }
        }
        return 0;
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

    // BFS + Queue + Set
    // beats 42.23%(123 ms)
    public int ladderLength3(String beginWord, String endWord,
                             Set<String> wordList) {
        if (beginWord.equals(endWord)) return 1;

        wordList.add(beginWord);
        wordList.add(endWord);
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        queue.offer(beginWord);
        visited.add(beginWord);
        for (int level = 2; !queue.isEmpty(); level++) {
            for (int i = queue.size(); i > 0; i--) {
                String word = queue.poll();
                for (String nextWord : getNextWords(word, wordList)) {
                    if (visited.contains(nextWord)) continue;

                    if (nextWord.equals(endWord)) return level;

                    visited.add(nextWord);
                    queue.offer(nextWord);
                }
            }
        }
        return 0;
    }

    private List<String> getNextWords(String word, Set<String> dict) {
        List<String> nextWords = new ArrayList<>();
        char[] chars = word.toCharArray();
        for (char c = 'a'; c <= 'z'; c++) {
            for (int i = word.length() - 1; i >= 0; i--) {
                if (c == word.charAt(i)) continue;

                chars[i] = c;
                String nextWord = new String(chars);
                if (dict.contains(nextWord)) {
                    nextWords.add(nextWord);
                }
                chars[i] = word.charAt(i);
            }
        }
        return nextWords;
    }

    // Solution of Choice
    // BFS + Queue
    // beats 82.48%(84 ms)
    public int ladderLength4(String beginWord, String endWord, Set<String> wordList) {
        Queue<String> queue = new LinkedList<>();
        queue.add(beginWord);
        wordList.add(endWord);
        for (int level = 1; !queue.isEmpty(); level++) {
            for (int i = queue.size(); i > 0; i--) {
                String cur = queue.poll();
                if (cur.equals(endWord)) return level;

                char[] word = cur.toCharArray();
                for (int j = cur.length() - 1; j >= 0; j--) {
                    char oldChar = cur.charAt(j);
                    for (char c = 'a'; c < 'z'; c++) {
                        if (c == oldChar) continue;

                        word[j] = c;
                        String newWord = new String(word);
                        if (wordList.contains(newWord)) {
                            queue.offer(newWord);
                            wordList.remove(newWord);
                        }
                    }
                    word[j] = oldChar;
                }
            }
        }
        return 0;
    }

    // Solution of Choice
    // Two-end BFS + Set
    // https://discuss.leetcode.com/topic/29303/two-end-bfs-in-java-31ms
    // beats 85.62%(45 ms)
    public int ladderLength5(String beginWord, String endWord, Set<String> wordList) {
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

    @FunctionalInterface
    interface Function<A, B, C, D> {
        public D apply(A a, B b, C c);
    }

    void test(Function<String, String, Set<String>, Integer> ladderLength,
              String name, String begin, String end, int expected, String ... words)
    throws Exception {
        Set<String> wordList;
        if (words.length == 0) {
            wordList = new HashSet<>(
                Files.readAllLines(Paths.get("/usr/share/dict/words")));
        } else {
            wordList = new HashSet<>(Arrays.asList(words));
        }
        long t1 = System.nanoTime();
        assertEquals(expected, (int)ladderLength.apply(begin, end, wordList));
        System.out.format("%s: %.3f ms\n", name, (System.nanoTime() - t1) * 1e-6);
    }

    public void test(String begin, String end, int expected, String ... words) {
        WordLadder w = new WordLadder();
        try {
            test(w::ladderLength, "ladderLength", begin, end, expected, words);
            test(w::ladderLength2, "ladderLength2", begin, end, expected, words);
            test(w::ladderLength3, "ladderLength3", begin, end, expected, words);
            test(w::ladderLength4, "ladderLength4", begin, end, expected, words);
            test(w::ladderLength5, "ladderLength5", begin, end, expected, words);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test1() {
        test("hit", "hit", 1, "dot", "dog", "lot", "log");
        test("nape", "mild", 6);
        test("hit", "cog", 0, "dot", "dog", "lot", "log");
        test("hit", "cog", 5, "hot", "dot", "dog", "lot", "log");
        test("hit", "cog", 4);
        test("nape", "mild", 6, "dose", "ends", "dine", "jars", "prow", "soap",
             "guns", "hops", "cray", "hove", "ella", "hour", "lens", "jive",
             "wiry", "earl", "mara", "part", "flue", "putt", "rory", "bull",
             "york", "ruts", "lily", "vamp", "bask", "peer", "boat", "dens",
             "lyre", "jets", "wide", "rile", "boos", "down", "path", "onyx",
             "mows", "toke", "soto", "dork", "nape", "mans", "loin", "jots",
             "male", "sits", "minn", "sale", "pets", "hugo", "woke", "suds",
             "rugs",  "vole", "warp", "mite", "pews", "lips", "pals", "nigh",
             "sulk", "vice", "clod", "iowa", "gibe", "shad", "carl", "huns",
             "coot", "sera", "mils", "rose", "orly", "ford", "void", "time",
             "eloy", "risk", "veep", "reps", "dolt", "hens", "tray", "melt",
             "rung", "rich", "saga", "lust", "yews", "rode", "many", "cods",
             "rape", "last", "tile", "nosy", "take", "nope", "toni", "bank",
             "jock", "jody", "diss", "nips", "bake", "lima", "wore", "kins",
             "cult", "hart", "wuss", "tale", "sing", "lake", "bogy", "wigs",
             "kari", "magi", "bass", "pent", "tost", "fops", "bags", "duns",
             "will", "tart", "drug", "gale", "mold", "disk", "spay", "hows",
             "naps", "puss", "gina", "kara", "zorn", "boll", "cams", "boas",
             "rave", "sets", "lego", "hays", "judy", "chap", "live", "bahs", "ohio",
             "nibs", "cuts", "pups", "data", "kate", "rump", "hews", "mary", "stow",
             "fang", "bolt", "rues", "mesh", "mice", "rise", "rant", "dune", "jell",
             "laws", "jove", "bode", "sung", "nils", "vila", "mode", "hued", "cell",
             "fies", "swat", "wags", "nate", "wist", "honk", "goth", "told", "oise",
             "wail", "tels", "sore", "hunk", "mate", "luke", "tore", "bond", "bast",
             "vows", "ripe", "fond", "benz", "firs", "zeds", "wary", "baas", "wins",
             "pair", "tags", "cost", "woes", "buns", "lend", "bops", "code", "eddy",
             "siva", "oops", "toed", "bale", "hutu", "jolt", "rife", "darn", "tape",
             "bold", "cope", "cake", "wisp", "vats", "wave", "hems", "bill", "cord",
             "pert", "type", "kroc", "ucla", "albs", "yoko", "silt", "pock", "drub",
             "puny", "fads", "mull", "pray", "mole", "talc", "east", "slay", "jamb",
             "mill", "dung", "jack", "lynx", "nome", "leos", "lade", "sana", "tike",
             "cali", "toge", "pled", "mile", "mass", "leon", "sloe", "lube", "kans",
             "cory", "burs", "race", "toss", "mild", "tops", "maze", "city", "sadr",
             "bays", "poet", "volt", "laze", "gold", "zuni", "shea", "gags", "fist",
             "ping", "pope", "cora", "yaks", "cosy", "foci", "plan", "colo", "hume",
             "yowl", "craw", "pied", "toga", "lobs", "love", "lode", "duds", "bled",
             "juts", "gabs", "fink", "rock", "pant", "wipe", "pele", "suez", "nina",
             "ring", "okra", "warm", "lyle", "gape", "bead", "lead", "jane", "oink",
             "ware", "zibo", "inns", "mope", "hang", "made", "fobs", "gamy", "fort",
             "peak", "gill", "dino", "dina", "tier");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("WordLadder");
    }
}
