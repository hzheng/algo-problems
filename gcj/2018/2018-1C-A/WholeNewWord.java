import java.util.*;
import java.io.*;

// https://codejam.withgoogle.com/2018/challenges/0000000000007765/dashboard
// Round 1C 2018: Problem A - A Whole New Word
//
// Vincent is showing N distinct L-letter words to Desta by using some letter
// tiles. Each tile contains one uppercase English alphabet letter, and one 
// number between 1 and L. A word consists of the letters spelled out by L tiles
// with numbers from 1 through L, in order. Desta wants to create a new word 
// that obeys the rules above and is not one of Vincent's existing words. He
// must use some of Vincent's tiles. Help Desta to choose a word that he can 
// show to Vincent using only the tiles used by Vincent, or indicate that it is
// impossible to do so.
// Input
// The first line of the input gives the number of test cases, T. T test cases 
// follow. Each begins with one line with two integers N and L: the number of 
// Vincent's words, and the length of each word. Then, N more lines follow. The 
// i-th of these lines contains a string of L uppercase English letters
// representing the i-th of Vincent's words.
// Output
// For each test case, output one line containing Case #x: y, where x is the
// test case number and y is a valid word to be chosen by Desta, or "-"" if 
// there is no valid. If there is more than one valid word that Desta can make,
// you can output any of them.
// Limits
// 1 ≤ T ≤ 100.
// Time limit: 15 seconds per test set.
// Memory limit: 1GB.
// No two of Vincent's words are the same.
// Test set 1 (Visible)
// 1 ≤ N ≤ 26^2.
// 1 ≤ L ≤ 2.
// Test set 2 (Hidden)
// 1 ≤ N ≤ 2000.
// 1 ≤ L ≤ 10.
public class WholeNewWord {
    // Trie
    public static String solve(String[] words, int N, int L) {
        Trie trie = new Trie();
        for (String w : words) {
            trie.insert(w);
        }
        Character[][] cand = new Character[L][];
        for (int i = 0; i < L; i++) {
            Set<Character> set = new HashSet<>();
            for (String w : words) {
                set.add(w.charAt(i));
            }
            cand[i] = set.toArray(new Character[0]);
        }
        int[] indices = new int[L];
        for (int len = 1; len <= L; ) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < len; i++)  {
                sb.append(cand[i][indices[i]]);
            }
            if (!trie.search(sb.toString())) {
                for (int j = len; j < L; j++) {
                    sb.append(cand[j][0]);
                }
                return sb.toString();
            }
            for (int j = len - 1;; j--) {
                if (j < 0) {
                    len++;
                    break;
                }
                if (++indices[j] < cand[j].length) break;

                indices[j] = 0;
            }
        }
        return "-";
    }

    private static class Trie {
        private TrieNode root = new TrieNode();

        private void insert(String word) {
            TrieNode cur = root;
            for (char c : word.toCharArray()) {
                if (cur.getChild(c) == null) {
                    cur.setChild(c, new TrieNode());
                }
                cur = cur.getChild(c);
            }
        }

        private boolean search(String word) {
            TrieNode cur = root;
            for (char c : word.toCharArray()) {
                if (cur == null) return false;

                cur = cur.getChild(c);
            }
            return cur != null;
        }
    }

    private static class TrieNode {
        private static final int SIZE = 26;

        private TrieNode[] children;

        public TrieNode() {
            children = new TrieNode[SIZE + 1];
        }

        public TrieNode getChild(char c) {
            return children[c - 'A'];
        }

        public void setChild(char c, TrieNode child) {
            children[c - 'A'] = child;
        }
    }

    // HashSet
    public static String solve2(String[] words, int N, int L) {
        Set<String> wordSet = new HashSet<>();
        for (String w : words) {
            wordSet.add(w);
        }
        Character[][] cand = new Character[L][];
        for (int i = 0; i < L; i++) {
            Set<Character> set = new HashSet<>();
            for (String w : words) {
                set.add(w.charAt(i));
            }
            cand[i] = set.toArray(new Character[0]);
        }
        for (int[] indices = new int[L]; ; ) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < L; i++)  {
                sb.append(cand[i][indices[i]]);
            }
            String newWord = sb.toString();
            if (!wordSet.contains(newWord)) return newWord;

            for (int i = L - 1;; i--) {
                if (++indices[i] < cand[i].length) break;

                if (i == 0) return "-";

                indices[i] = 0;
            }
        }
    }

    public static void main(String[] args) {
        Scanner in =
            new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        PrintStream out = System.out;
        int t = in.nextInt();
        for (int i = 1; i <= t; i++) {
            out.format("Case #%d: ", i);
            printResult(in, out);
        }
    }

    private static void printResult(Scanner in, PrintStream out) {
        int N = in.nextInt();
        int L = in.nextInt();
        String[] words = new String[N];
        for (int i = 0; i < N; i++) {
            words[i] = in.next();
        }
        out.println(solve2(words, N, L));
    }
}
