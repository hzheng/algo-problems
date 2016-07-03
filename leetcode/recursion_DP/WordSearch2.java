import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/word-search-ii/
//
// Given a 2D board and a list of words from the dictionary, find all words.
// Each word must be constructed from letters of sequentially adjacent cell,
// where "adjacent" cells are those horizontally or vertically neighboring.
// The same letter cell may not be used more than once in a word.
public class WordSearch2 {
    static final int SIZE = 26;

    //  Time Limit Exceeded
    public List<String> findWords(char[][] board, String[] words) {
        List<String> res = new ArrayList<>();
        int m = board.length;
        if (m == 0) return res;

        Set<String> wordSet = new HashSet<>();
        for (String word : words) {
            if (wordSet.contains(word)) continue;

            wordSet.add(word);
            if (exist(board, word)) {
                res.add(word);
            }
        }
        return res;
    }

    private boolean exist(char[][] board, String word) {
        if (word.isEmpty()) return true;

        int m = board.length;
        int n = board[0].length;
        char c = word.charAt(0);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == c) {
                    if (exist(board, word, i, j, 0)) return true;
                }
            }
        }
        return false;
    }

    private boolean exist(char[][] board, String word,
                          int row, int col, int start) {
        if (start >= word.length()) return true;

        if (row < 0 || row >= board.length || col < 0 || col >= board[0].length
            || board[row][col] != word.charAt(start)) {
            return false;
        }

        board[row][col] = '\0';
        boolean res = exist(board, word, row - 1, col, start + 1)
                      || exist(board, word, row + 1, col, start + 1)
                      || exist(board, word, row, col - 1, start + 1)
                      || exist(board, word, row, col + 1, start + 1);
        board[row][col] = word.charAt(start);
        return res;
    }

    //  Time Limit Exceeded
    public List<String> findWords2(char[][] board, String[] words) {
        int m = board.length;
        if (m == 0) return Collections.emptyList();

        Map<Character, List<int[]> > map = new HashMap<>();
        int n = board[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                char c = board[i][j];
                if (!map.containsKey(c)) {
                    map.put(c, new ArrayList<>());
                }
                map.get(c).add(new int[] {i, j});
            }
        }
        Trie trie = new Trie();
        Set<String> res = new HashSet<>();
        for (String word : words) {
            if (exist(board, word, map, trie)) {
                res.add(word);
            }
        }
        return new ArrayList<>(res);
    }

    private static class Trie {
        class TrieNode {
            boolean matched = true;

            TrieNode[] children;

            public TrieNode getChild(char c) {
                return children == null ? null : children[c - 'a'];
            }

            public void setChild(char c, TrieNode child) {
                if (children == null) {
                    children = new TrieNode[SIZE];
                }
                children[c - 'a'] = child;
            }
        }

        private TrieNode root = new TrieNode();

        private void insert(String word, boolean matched) {
            TrieNode cur = root;
            for (char c : word.toCharArray()) {
                if (cur.getChild(c) == null) {
                    cur.setChild(c, new TrieNode());
                }
                cur = cur.getChild(c);
            }
            cur.matched = matched;
        }

        private Boolean search(String word) {
            TrieNode cur = root;
            for (char c : word.toCharArray()) {
                if (cur == null || cur.children == null) return null;

                cur = cur.getChild(c);
            }
            return cur == null ? null : Boolean.valueOf(cur.matched);
        }
    }

    private boolean exist(char[][] board, String word,
                          Map<Character, List<int[]> > map, Trie trie) {
        if (word.isEmpty()) return true;

        Boolean searchRes = trie.search(word);
        if (searchRes != null) return Boolean.valueOf(searchRes);

        char c = word.charAt(0);
        if (!map.containsKey(c)) return false;

        int matched = 0;
        int len = word.length();
        for (int[] pos : map.get(c)) {
            matched = Math.max(matched, maxMatch(board, word, pos[0], pos[1], 0));
            if (matched == len) {
                trie.insert(word, true);
                return true;
            }
        }
        trie.insert(word.substring(0, matched + 1), false);
        return false;
    }

    private int maxMatch(char[][] board, String word,
                         int row, int col, int start) {
        if (board[row][col] != word.charAt(start)) return start;

        int len = word.length();
        if (start + 1 >= len) return len;

        board[row][col] = '\0';
        int res = 0;
        if (row > 0) {
            res = maxMatch(board, word, row - 1, col, start + 1);
        }
        if (res < len && row + 1 < board.length) {
            res = Math.max(res, maxMatch(board, word, row + 1, col, start + 1));
        }
        if (res < len && col > 0) {
            res = Math.max(res, maxMatch(board, word, row, col - 1, start + 1));
        }
        if (res < len && col + 1 < board[0].length) {
            res = Math.max(res, maxMatch(board, word, row, col + 1, start + 1));
        }
        board[row][col] = word.charAt(start);
        return res;
    }

    // build all possible words from board, very slow
    public List<String> findWords3(char[][] board, String[] words) {
        int m = board.length;
        if (m == 0) return Collections.emptyList();

        int n = board[0].length;
        Trie3 trie = new Trie3();

        Set<String> wordSet = new HashSet<>();
        int maxWord = 0;
        for (String word : words) {
            if (wordSet.contains(word)) continue;

            wordSet.add(word);
            maxWord = Math.max(maxWord, word.length());
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                buildTrie(board, i, j, trie, maxWord);
            }
        }

        List<String> res = new LinkedList<>();
        for (String word : wordSet) {
            if (trie.search(word)) {
                res.add(word);
            }
        }
        return res;
    }

    private void buildTrie(char[][] board, int row, int col, Trie3 trie, int len) {
        if (len == 0) return;

        char c = board[row][col];
        if (c == 0) return;

        Trie3.TrieNode cur = trie.cur;
        trie.insert(c);
        board[row][col] = 0;

        if (row > 0) {
            buildTrie(board, row - 1, col, trie, len - 1);
        }
        if (row + 1 < board.length) {
            buildTrie(board, row + 1, col, trie, len - 1);
        }
        if (col > 0) {
            buildTrie(board, row, col - 1, trie, len - 1);
        }
        if (col + 1 < board[0].length) {
            buildTrie(board, row, col + 1, trie, len - 1);
        }
        board[row][col] = c;
        trie.cur = cur;
    }

    private static class Trie3 {
        class TrieNode {
            TrieNode[] children = new TrieNode[SIZE];

            public TrieNode getChild(char c) {
                return children[c - 'a'];
            }

            public void setChild(char c, TrieNode child) {
                children[c - 'a'] = child;
            }
        }

        private TrieNode root = new TrieNode();

        private TrieNode cur = root;

        private boolean search(String word) {
            TrieNode cur = root;
            for (char c : word.toCharArray()) {
                cur = cur.getChild(c);
                if (cur == null) return false;
            }
            return true;
        }

        private void insert(char c) {
            if (cur.getChild(c) == null) {
                cur.setChild(c, new TrieNode());
            }
            cur = cur.getChild(c);
        }
    }

    // DFS with pruning, reverse Trie(build from target instead of source)
    // beats 37.47%(68 ms)
    public List<String> findWords4(char[][] board, String[] words) {
        int m = board.length;
        if (m == 0) return Collections.emptyList();

        int n = board[0].length;
        boolean[][] visited = new boolean[m][n];
        Set<String> res = new HashSet<>();
        Trie4 trie = new Trie4();
        for (String word : words) {
            trie.insert(word);
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                findWords(board, visited, "", i, j, trie, res);
            }
        }
        return new ArrayList<>(res);
    }

    public void findWords(char[][] board, boolean[][] visited, String word,
                          int x, int y, Trie4 trie, Set<String> res) {
        if (x < 0 || x >= board.length
           || y < 0 || y >= board[0].length || visited[x][y]) return;

        word += board[x][y];
        if (!trie.startsWith(word)) return;

        if (trie.search(word)) {
            res.add(word);
        }

        visited[x][y] = true;
        findWords(board, visited, word, x - 1, y, trie, res);
        findWords(board, visited, word, x + 1, y, trie, res);
        findWords(board, visited, word, x, y - 1, trie, res);
        findWords(board, visited, word, x, y + 1, trie, res);
        visited[x][y] = false;
    }

    private static class Trie4 {
        class TrieNode {
            boolean isEnd;

            TrieNode[] children = new TrieNode[SIZE];

            public TrieNode getChild(char c) {
                return children == null ? null : children[c - 'a'];
            }

            public void setChild(char c, TrieNode child) {
                if (children == null) {
                    children = new TrieNode[SIZE];
                }
                children[c - 'a'] = child;
            }
        }

        private TrieNode root = new TrieNode();

        public void insert(String word) {
            TrieNode cur = root;
            for (char c : word.toCharArray()) {
                if (cur.getChild(c) == null) {
                    cur.setChild(c, new TrieNode());
                }
                cur = cur.getChild(c);
            }
            cur.isEnd = true;
        }

        public boolean search(String word) {
            TrieNode res = doSearch(word);
            return res != null && res.isEnd;
        }

        public boolean startsWith(String prefix) {
            return doSearch(prefix) != null;
        }

        private TrieNode doSearch(String word) {
            TrieNode cur = root;
            for (char c : word.toCharArray()) {
                if (cur == null || cur.children == null) return null;

                cur = cur.getChild(c);
            }
            return cur;
        }
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(Function<char[][], String[], List<String> > find, String name,
              String[] boardStr, String[] words, String[] expected) {
        char[][] board = new char[boardStr.length][];
        for (int i = 0; i < boardStr.length; i++) {
            board[i] = boardStr[i].toCharArray();
        }
        long t1 = System.nanoTime();
        List<String> res = find.apply(board, words);
        if (words.length > 10) {
            System.out.format("%s: %.3f ms\n", name,
                              (System.nanoTime() - t1) * 1e-6);
        }
        Collections.sort(res);
        assertArrayEquals(expected, res.toArray(new String[0]));
    }

    void test(String[] boardStr, String[] words, String ... expected) {
        WordSearch2 w = new WordSearch2();
        test(w::findWords, "findWords", boardStr, words, expected);
        test(w::findWords2, "findWords2", boardStr, words, expected);
        if (words.length < 10) {
            test(w::findWords3, "findWords3", boardStr, words, expected);
        }
        test(w::findWords4, "findWords4", boardStr, words, expected);
    }

    @Test
    public void test1() {
        test(new String[] {"a"}, new String[] {"a", "a"}, "a");
        test(new String[] {"oaan", "etae", "ihkr", "iflv"},
             new String[] {"oath", "pea", "eat", "rain"}, "eat","oath");
        test(new String[] {"ab"}, new String[] {"a", "b"}, "a", "b");
        test(new String[] {"oaan", "etae", "ihkr", "iflv"},
             new String[] {"oath", "oat", "pea", "eat", "rain", "p"},
             "eat", "oat", "oath");
        test(new String[] {"aaaa", "aaaa", "aaaa", "aaaa", "bcde", "fghi",
                           "jklm", "nopq", "rstu", "vwxy", "zzzz"},
             new String[] {
            "aaaaaaaaaaaaaaaa", "aaaaaaaaaaaaaaab", "aaaaaaaaaaaaaaac",
            "aaaaaaaaaaaaaaad", "aaaaaaaaaaaaaaae","aaaaaaaaaaaaaaaf",
            "aaaaaaaaaaaaaaag", "aaaaaaaaaaaaaaah", "aaaaaaaaaaaaaaai",
            "aaaaaaaaaaaaaaaj", "aaaaaaaaaaaaaaak", "aaaaaaaaaaaaaaal",
            "aaaaaaaaaaaaaaam", "aaaaaaaaaaaaaaan", "aaaaaaaaaaaaaaao",
            "aaaaaaaaaaaaaaap", "aaaaaaaaaaaaaaaq", "aaaaaaaaaaaaaaar",
            "aaaaaaaaaaaaaaas", "aaaaaaaaaaaaaaat", "aaaaaaaaaaaaaaau",
            "aaaaaaaaaaaaaaav", "aaaaaaaaaaaaaaaw", "aaaaaaaaaaaaaaax",
            "aaaaaaaaaaaaaaay", "aaaaaaaaaaaaaaaz", "aaaaaaaaaaaaaaae",
            "aaaaaaaaaaaaaaaf", "aaaaaaaaaaaaaaag", "aaaaaaaaaaaaaaah",
            "aaaaaaaaaaaaaaai", "aaaaaaaaaaaaaaaj", "aaaaaaaaaaaaaaak",
            "aaaaaaaaaaaaaaal", "aaaaaaaaaaaaaaam", "aaaaaaaaaaaaaaan",
            "aaaaaaaaaaaaaaao", "aaaaaaaaaaaaaaap", "aaaaaaaaaaaaaaaq",
            "aaaaaaaaaaaaaaar", "aaaaaaaaaaaaaaas", "aaaaaaaaaaaaaaat",
            "aaaaaaaaaaaaaaau", "aaaaaaaaaaaaaaav", "aaaaaaaaaaaaaaaw",
            "aaaaaaaaaaaaaaax", "aaaaaaaaaaaaaaay", "aaaaaaaaaaaaaaaz",
            "aaaaaaaaaaaaaabb", "aaaaaaaaaaaaaabc", "aaaaaaaaaaaaaabd",
            "aaaaaaaaaaaaaabe", "aaaaaaaaaaaaaabf", "aaaaaaaaaaaaaabg",
            "aaaaaaaaaaaaaabh", "aaaaaaaaaaaaaabi", "aaaaaaaaaaaaaabj",
            "aaaaaaaaaaaaaabk", "aaaaaaaaaaaaaabl", "aaaaaaaaaaaaaabm",
            "aaaaaaaaaaaaaabn", "aaaaaaaaaaaaaabo", "aaaaaaaaaaaaaabp",
            "aaaaaaaaaaaaaabq", "aaaaaaaaaaaaaabr", "aaaaaaaaaaaaaabs",
            "aaaaaaaaaaaaaabt", "aaaaaaaaaaaaaabu", "aaaaaaaaaaaaaabv",
            "aaaaaaaaaaaaaabw", "aaaaaaaaaaaaaabx", "aaaaaaaaaaaaaaby",
            "aaaaaaaaaaaaaabz", "aaaaaaaaaaaaaaca", "aaaaaaaaaaaaaacb",
            "aaaaaaaaaaaaaacc", "aaaaaaaaaaaaaacd", "aaaaaaaaaaaaaace",
            "aaaaaaaaaaaaaafg", "aaaaaaaaaaaaaafh", "aaaaaaaaaaaaaafi",
            "aaaaaaaaaaaaaafj", "aaaaaaaaaaaaaafk", "aaaaaaaaaaaaaafl"
        },
             "aaaaaaaaaaaaaaaa", "aaaaaaaaaaaaaaab", "aaaaaaaaaaaaaaac",
             "aaaaaaaaaaaaaaad", "aaaaaaaaaaaaaaae", "aaaaaaaaaaaaaabc",
             "aaaaaaaaaaaaaabf", "aaaaaaaaaaaaaacb", "aaaaaaaaaaaaaacd");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("WordSearch2");
    }
}
