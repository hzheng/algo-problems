import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC677: https://leetcode.com/problems/map-sum-pairs/
//
// For the method insert, you'll be given a pair of (string, integer). The
// string represents the key and the integer represents the value. If the key
// already existed, then the original key-value pair will be overridden to the
// new one.
// For the method sum, you'll be given a string representing the prefix, and you
// need return the sum of all the pairs' value whose key starts with the prefix.
public class MapSum {
    static interface IMapSum {
        public void insert(String key, int val);
        public int sum(String prefix);
    }

    // Hash Table
    // beats 71.12%(112 ms for 30 tests)
    static class MapSum1 implements IMapSum {
        private Map<String, Integer> vals = new HashMap<>();
        private Map<String, Integer> sums = new HashMap<>();

        // time complexity: O(K ^ 2) (K: key length)
        public void insert(String key, int val) {
            int diff = val - vals.getOrDefault(key, 0);
            vals.put(key, val);
            for (int i = key.length(); i >= 0; i--) {
                String prefix = key.substring(0, i);
                sums.put(prefix, sums.getOrDefault(prefix, 0) + diff);
            }
        }

        // time complexity: O(1)
        public int sum(String prefix) {
            return sums.getOrDefault(prefix, 0);
        }
    }

    // Hash Table
    // beats 30.12%(126 ms for 30 tests)
    static class MapSum2 implements IMapSum {
        private Map<String, Integer> map = new HashMap<>();

        // time complexity: O(1)
        public void insert(String key, int val) {
            map.put(key, val);
        }

        // time complexity: O(N ^ P) (N: number of Map items, P: prefix length)
        public int sum(String prefix) {
            int res = 0;
            for (String key : map.keySet()) {
                if (key.startsWith(prefix)) {
                    res += map.get(key);
                }
            }
            return res;
        }
    }

    // https://leetcode.com/articles/map-sum-pairs/ Approach #3
    // Trie
    // beats 88.13%(107 ms for 30 tests)
    static class MapSum3 implements IMapSum {
        private static class TrieNode {
            Map<Character, TrieNode> children = new HashMap<>();
            int value;
        }

        private Map<String, Integer> map = new HashMap<>();

        private TrieNode root = new TrieNode();

        // time complexity: O(K) (K: key length)
        public void insert(String key, int val) {
            int delta = val - map.getOrDefault(key, 0);
            map.put(key, val);
            TrieNode cur = root;
            cur.value += delta;
            for (char c : key.toCharArray()) {
                cur.children.putIfAbsent(c, new TrieNode());
                cur = cur.children.get(c);
                cur.value += delta;
            }
        }

        // time complexity: O(P) (L: prefix length)
        public int sum(String prefix) {
            TrieNode cur = root;
            for (char c : prefix.toCharArray()) {
                cur = cur.children.get(c);
                if (cur == null) return 0;
            }
            return cur.value;
        }
    }

    void test1(IMapSum obj) {
        obj.insert("apple", 3);
        assertEquals(3, obj.sum("ap"));
        obj.insert("ap", 2);
        assertEquals(5, obj.sum("ap"));
        obj.insert("a", 4);
        assertEquals(9, obj.sum("a"));
    }

    @Test
    public void test1() {
        test1(new MapSum1());
        test1(new MapSum2());
        test1(new MapSum3());
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
