import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TrieNode;

// LC208: https://leetcode.com/problems/implement-trie-prefix-tree/
//
// Implement a trie with insert, search, and startsWith methods.
public class Trie {
    static abstract class AbstractTrie {

        // Inserts a word into the trie.
        public abstract void insert(String word);

        // Returns if the word is in the trie.
        public abstract boolean search(String word);

        // Returns if there is any word in the trie
        // that starts with the given prefix.
        public abstract boolean startsWith(String prefix);

        public void insert(String ... words) {
            for (String word : words) {
                insert(word);
            }
        }
    }

    // Solution of Choice
    // beat 40.00%(28 ms for 14 tests)
    static class Trie1 extends AbstractTrie {
        private TrieNode root;

        public Trie1() {
            root = new TrieNode();
        }

        public void insert0(String word) {
            TrieNode cur = root;
            int i = 0;
            while (i < word.length()) {
                TrieNode next = cur.getChild(word.charAt(i));
                if (next == null) break;

                cur = next;
                i++;
            }
            for (; i < word.length(); i++) {
                TrieNode child = new TrieNode();
                cur.setChild(word.charAt(i), child);
                cur = child;
            }
            cur.setEnd();
        }

        public void insert(String word) {
            TrieNode cur = root;
            for (char c : word.toCharArray()) {
                if (!cur.containsKey(c)) {
                    cur.setChild(c, new TrieNode());
                }
                cur = cur.getChild(c);
            }
            cur.setEnd();
        }

        public boolean search(String word) {
            TrieNode res = doSearch(word);
            return res != null && res.isEnd();
        }

        public boolean startsWith(String prefix) {
            return doSearch(prefix) != null;
        }

        private TrieNode doSearch(String word) {
            TrieNode cur = root;
            for (char c : word.toCharArray()) {
                cur = cur.getChild(c);
                if (cur == null) return null;
            }
            return cur;
        }
    }

    void test1(AbstractTrie trie) {
        trie.insert("keys");
        assertEquals(true, trie.search("keys"));
        assertEquals(false, trie.search("key"));
        assertEquals(true, trie.startsWith("key"));
    }

    void test2(AbstractTrie trie) {
        trie.insert("app", "apple", "beer", "add", "jam", "rental");
        assertEquals(false, trie.search("apps"));
        assertEquals(true, trie.search("app"));
        assertEquals(false, trie.search("ad"));
        assertEquals(false, trie.search("applepie"));
        assertEquals(false, trie.search("rest"));
        assertEquals(false, trie.search("jan"));
        assertEquals(false, trie.search("rent"));
        assertEquals(true, trie.search("beer"));
        assertEquals(true, trie.search("jam"));
        assertEquals(false, trie.startsWith("apps"));
        assertEquals(true, trie.startsWith("app"));
        assertEquals(true, trie.startsWith("ad"));
        assertEquals(false, trie.startsWith("applepie"));
        assertEquals(false, trie.startsWith("rest"));
        assertEquals(false, trie.startsWith("jan"));
        assertEquals(true, trie.startsWith("rent"));
        assertEquals(true, trie.startsWith("beer"));
        assertEquals(true, trie.startsWith("jam"));
    }

    @Test
    public void test() {
        test1(new Trie1());
        test2(new Trie1());
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("Trie");
    }
}
