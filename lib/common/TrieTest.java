// package common;

import java.util.Map;
import java.util.HashMap;

import org.junit.Test;
import static org.junit.Assert.*;

import common.Trie;

public class TrieTest {
    private Trie<Integer> test(Map<String, Integer> map) {
        Trie<Integer> root = new Trie<Integer>();
        for (String k : map.keySet()) {
            root.insert(k, map.get(k));
        }
        return root;
    }

    @Test
    public void test1() {
        Map<String, Integer> map = new HashMap<String, Integer>() {{
            put("as", 2);
            put("ask", 1);
            put("be", 3);
            put("bee", 6);
            put("bell", 4);
            put("belly", 5);
            put("the", 7);
            put("they", 8);
            put("them", 9);
        }};

        Trie<Integer> root = test(map);
        for (String k : map.keySet()) {
            assertEquals(map.get(k), root.get(k));
        }
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("TrieTest");
    }
}
