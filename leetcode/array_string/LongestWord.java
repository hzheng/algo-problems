import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC720: https://leetcode.com/problems/longest-word-in-dictionary/
//
// Given a list of strings words representing an English Dictionary, find the
// longest word in words that can be built one character at a time by other
// words in words. If there is more than one possible answer, return the longest
// word with the smallest lexicographical order.
// If there is no answer, return the empty string.
public class LongestWord {
    // SortedSet
    // time complexity: O(sum(W(i) ^ 2))
    // beats 88.63%(26 ms for 57 tests)
    public String longestWord(String[] words) {
        SortedSet<String> wordSet = new TreeSet<>(new Comparator<String>() {
            public int compare(String a, String b) {
                int l1 = a.length();
                int l2 = b.length();
                return (l1 != l2) ? (l2 - l1) : a.compareTo(b);
            }
        });
        for (String word : words) {
            wordSet.add(word);
        }
        outer : for (String word : wordSet) {
            // TODO: improve by increasingly checking to save largest one
            for (int i = word.length() - 1; i > 0; i--) {
                if (!wordSet.contains(word.substring(0, i))) continue outer;
            }
            return word;
        }
        return "";
    }

    // HashSet
    // time complexity: O(sum(W(i) ^ 2))
    // beats 93.53%(20 ms for 57 tests)
    public String longestWord2(String[] words) {
        Set<String> wordSet = new HashSet<>();
        for (String word : words) {
            wordSet.add(word);
        }
        String res = "";
        outer : for (String word : words) {
            if (word.length() > res.length() ||
                word.length() == res.length() && word.compareTo(res) < 0) {
                for (int k = 1; k < word.length(); k++) {
                    if (!wordSet.contains(word.substring(0, k))) continue outer;
                }
                res = word;
            }
        }
        return res;
    }

    // HashSet + Sort
    // time complexity: O(sum(W(i) ^ 2))
    // beats 68.02%(34 ms for 57 tests)
    public String longestWord3(String[] words) {
        Arrays.sort(words);
        Set<String> candidates = new HashSet<>();
        candidates.add("");
        String res = "";
        for (String w : words) {
            if (candidates.contains(w.substring(0, w.length() - 1))) {
                res = (w.length() > res.length()) ? w : res;
                candidates.add(w);
            }
        }
        return res;
    }

    // Trie + DFS + Stack
    // time complexity: O(sum(W(i)))
    // beats 89.70%(25 ms for 57 tests)
    public String longestWord4(String[] words) {
        Trie trie = new Trie(words);
        int index = 0;
        for (String word : words) {
            trie.insert(word, index++);
        }
        String res = "";
        Stack<Trie.Node> stack = new Stack<>();
        stack.push(trie.root);
        while (!stack.empty()) {
            Trie.Node node = stack.pop();
            for (Trie.Node child : node.children) {
                if (child == null || child.end < 0) continue;

                stack.push(child);
                String word = words[child.end];
                if (word.length() > res.length() ||
                    word.length() == res.length() && word.compareTo(res) < 0) {
                    res = word;
                }
            }
        }
        return res;
    }

    // Trie + BFS + Queue
    // time complexity: O(sum(W(i)))
    // beats 94.60%(19 ms for 57 tests)
    public String longestWord5(String[] words) {
        Trie trie = new Trie(words);
        int index = 0;
        for (String word : words) {
            trie.insert(word, index++);
        }
        String res = "";
        Queue<Trie.Node> queue = new LinkedList<>();
        queue.offer(trie.root);
        while (!queue.isEmpty()) {
            for (int i = queue.size(); i > 0; i--) {
                Trie.Node node = queue.poll();
                for (Trie.Node child : node.children) {
                    if (child != null && child.end >= 0) {
                        queue.offer(child);
                        res = words[child.end];
                    }
                }
            }
        }
        return res;
    }

    class Trie {
        class Node {
            char c;
            Node[] children = new Node[26];
            int end = -1;
            public Node(char c) {
                this.c = c;
            }
        }

        private Node root;

        public Trie(String[] words) {
            root = new Node(' ');
        }

        public void insert(String word, int index) {
            Node cur = root;
            for (char c : word.toCharArray()) {
                Node child = cur.children[25 - c + 'a']; // reverse order
                if (child == null) {
                    child = cur.children[25 - c + 'a'] = new Node(c);
                }
                cur = child;
            }
            cur.end = index;
        }
    }

    void test(String[] words, String expected) {
        assertEquals(expected, longestWord(words));
        assertEquals(expected, longestWord2(words));
        assertEquals(expected, longestWord3(words));
        assertEquals(expected, longestWord4(words));
        assertEquals(expected, longestWord5(words));
    }

    @Test
    public void test() {
        test(new String[] {"w", "wo", "wor", "worl", "world"}, "world");
        test(new String[] {"a", "banana", "app", "appl", "ap", "apply",
                           "apple"}, "apple");
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
