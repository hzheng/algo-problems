import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Cracking the Coding Interview(5ed) Problem 18.8:
 * Given a string s and an array of smaller strings T, design a method to
 * search s for each small string in T
 */
class SuffixTree {
    static class Node {
        // private char value;
        private Map<Character, Node> children = new HashMap<Character, Node>();
        private List<Integer> indexes = new ArrayList<Integer>();

        public void insert(String s, int index) {
            indexes.add(index);
            if (s == null || s.isEmpty()) return;

            char value = s.charAt(0);
            if (!children.containsKey(value)) {
                children.put(value, new Node());
            }
            children.get(value).insert(s.substring(1), index);
        }

        public List<Integer> search(String s) {
            if (s == null || s.isEmpty()) return indexes;

            char value = s.charAt(0);
            if (!children.containsKey(value)) return Collections.emptyList();

            return children.get(value).search(s.substring(1));
        }
    } // class Node

    private Node suffixTree = new Node();

    public SuffixTree(String s) {
        for (int i = 0; i < s.length(); i++) {
            suffixTree.insert(s.substring(i), i);
        }
    }

    public List<Integer> search(String target) {
        return suffixTree.search(target);
    }
} // class SuffixTree

public class StringSearch {
    public static List<List<Integer> > search(String text, String[] keywords) {
        SuffixTree tree = new SuffixTree(text);
        int len = keywords.length;
        List<List<Integer> > results = new ArrayList<List<Integer> >(len);
        for (int i = 0; i < len; i++) {
            results.add(tree.search(keywords[i]));
        }
        return results;
    }

    private void test(String text, String[] keywords, Integer[][] expected) {
        List<List<Integer> > results = search(text, keywords);
        assertEquals(expected.length, results.size());
        for (int i = 0; i < expected.length; i++) {
            Integer[] result = results.get(i).toArray(new Integer[0]);
            assertArrayEquals(expected[i], result);
        }
    }

    @Test
    public void test1() {
        test("mississippi",
             new String[] {"is", "sip", "hi", "sis", "ss", "s"},
             new Integer[][] {{1, 4}, {6}, {}, {3}, {2, 5}, {2, 3, 5, 6}});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("StringSearch");
    }
}
