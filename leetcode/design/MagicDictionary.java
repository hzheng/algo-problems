import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC676: https://leetcode.com/problems/implement-magic-dictionary/
//
// Implement a magic directory with buildDict, and search methods.
// For the method buildDict, you'll be given a list of non-repetitive words to
// build a dictionary.
// For the method search, you'll be given a word, and judge whether if you
// modify exactly one character into another character in this word, the
// modified word is in the dictionary you just built.
public class MagicDictionary {
    static interface IMagicDictionary {
        /** Build a dictionary through a list of words */
        public void buildDict(String[] dict);
        /** Returns if there is any word in the trie that equals to the given word after modifying exactly one character */
        public boolean search(String word);
    }

    // Hash Table + Set
    // beats 80.36%(108 ms for 32 tests)
    static class MagicDictionary1 implements IMagicDictionary {
        private Map<Integer, Set<String> > map = new HashMap<>();

        public void buildDict(String[] dict) {
            for (String word : dict) {
                int len = word.length();
                map.putIfAbsent(len, new HashSet<>());
                map.get(len).add(word);
            }
        }

        public boolean search(String word) {
            int len = word.length();
            Set<String> list = map.get(len);
            if (list == null) return false;

            for (String w : list) {
                if (oneDiff(w, word)) return true;
            }
            return false;
        }

        private boolean oneDiff(String a, String b) {
            boolean diffFound = false;
            for (int i = a.length() - 1; i >= 0; i--) {
                if (a.charAt(i) != b.charAt(i)) {
                    if (diffFound) return false;

                    diffFound = true;
                }
            }
            return diffFound;
        }
    }

    // Hash Table + List
    // beats 30.91%(123 ms for 32 tests)
    static class MagicDictionary2 implements IMagicDictionary {
        private Map<String, List<int[]> > map = new HashMap<>();

        public void buildDict(String[] dict) {
            for (String word : dict) {
                for (int i = word.length() - 1; i >= 0; i--) {
                    String key = word.substring(0, i) + word.substring(i + 1);
                    List<int[]> val = map.getOrDefault(key, new ArrayList<>());
                    val.add(new int[] {i, word.charAt(i)});
                    map.put(key, val);
                }
            }
        }

        public boolean search(String word) {
            for (int i = word.length() - 1; i >= 0; i--) {
                List<int[]> pairs = map.get(word.substring(0, i)
                                            + word.substring(i + 1));
                if (pairs == null) continue;

                for (int[] pair : pairs) {
                    if (pair[0] == i && pair[1] != word.charAt(i)) return true;
                }
            }
            return false;
        }
    }

    // Trie
    // beats 85.96%(106 ms for 32 tests)
    static class MagicDictionary3 implements IMagicDictionary {
        private static class TrieNode {
            private TrieNode[] children = new TrieNode[26];
            private boolean end;

            public boolean search(String word) {
                TrieNode cur = this;
                for (char c : word.toCharArray()) {
                    cur = cur.children[c - 'a'];
                    if (cur == null) return false;
                }
                return cur.end;
            }

            public void insert(String word) {
                TrieNode cur = this;
                for (char c : word.toCharArray()) {
                    TrieNode node = cur.children[c - 'a'];
                    if (node == null) {
                        node = cur.children[c - 'a'] = new TrieNode();
                    }
                    cur = node;
                }
                cur.end = true;
            }
        }

        private TrieNode root = new TrieNode();

        public void buildDict(String[] dict) {
            for (String word : dict) {
                root.insert(word);
            }
        }

        public boolean search(String word) {
            for (int i = word.length() - 1; i >= 0; i--) {
                if (search(word, i)) return true;
            }
            return false;
        }

        private boolean search(String word, int mutation) {
            TrieNode cur = root;
            TrieNode child = null;
            for (int i = 0; cur != null; i++, cur = child) {
                child = cur.children[word.charAt(i) - 'a'];
                if (i == mutation) break;
            }
            if (cur == null) return false;

            String subword = word.substring(mutation + 1);
            for (TrieNode node : cur.children) {
                if (node != null && node != child
                    && node.search(subword)) return true;
            }
            return false;
        }
    }

    void test1(IMagicDictionary obj) {
        obj.buildDict(new String[] {"hello", "leetcode"});
        assertEquals(false, obj.search("hello"));
        assertEquals(true, obj.search("hhllo"));
        assertEquals(false, obj.search("hell"));
        assertEquals(false, obj.search("leetcode"));
        assertEquals(false, obj.search("leetcoded"));
    }

    void test2(IMagicDictionary obj) {
        obj.buildDict(new String[] {"hello", "hallo", "hell", "leetcode"});
        assertEquals(true, obj.search("hhllo"));
        assertEquals(false, obj.search("hell"));
        assertEquals(true, obj.search("hello"));
        assertEquals(true, obj.search("helo"));
        assertEquals(true, obj.search("hallo"));
        assertEquals(false, obj.search("leetcoded"));
    }

    void test3(IMagicDictionary obj) {
        obj.buildDict(new String[] {"a", "b", "ab", "abc"});
        assertEquals(true, obj.search("a"));
        assertEquals(true, obj.search("b"));
        assertEquals(true, obj.search("c"));
        assertEquals(true, obj.search("d"));
        assertEquals(true, obj.search("e"));
        assertEquals(true, obj.search("f"));
        assertEquals(false, obj.search("ab"));
        assertEquals(false, obj.search("ba"));
        assertEquals(false, obj.search("abc"));
        assertEquals(false, obj.search("cba"));
        assertEquals(true, obj.search("abb"));
        assertEquals(true, obj.search("bb"));
        assertEquals(true, obj.search("aa"));
        assertEquals(true, obj.search("bbc"));
    }

    @Test
    public void test1() {
        test1(new MagicDictionary1());
        test1(new MagicDictionary2());
        test1(new MagicDictionary3());
        test2(new MagicDictionary1());
        test2(new MagicDictionary2());
        test2(new MagicDictionary3());
        test3(new MagicDictionary1());
        test3(new MagicDictionary2());
        test3(new MagicDictionary3());
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
