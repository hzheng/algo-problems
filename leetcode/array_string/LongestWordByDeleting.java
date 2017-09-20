import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC524: https://leetcode.com/problems/longest-word-in-dictionary-through-deleting/
//
// Given a string and a string dictionary, find the longest string in the
// dictionary that can be formed by deleting some characters of the given string.
// If there are more than one possible results, return the longest word with the
// smallest lexicographical order. If there is no possible result, return the empty string.
public class LongestWordByDeleting {
    // Heap
    // beats 80.54%(41 ms for 31 tests)
    public String findLongestWord(String s, List<String> d) {
        PriorityQueue<String> pq = new PriorityQueue<>(
            new Comparator<String>() {
            public int compare(String a, String b) {
                int diff = b.length() - a.length();
                return diff != 0 ? diff : a.compareTo(b);
            }
        });
        char[] cs = s.toCharArray();
        for (String word : d) {
            if (word.length() <= cs.length) {
                pq.offer(word);
            }
        }
        while (!pq.isEmpty()) {
            String word = pq.poll();
            if (isSubword(word, cs)) return word;
        }
        return "";
    }

    private boolean isSubword(String word, char[] cs) {
        int len = word.length();
        int i = 0;
        for (char c : cs) {
            if (word.charAt(i) == c) {
                i++;
            }
            if (i == len) return true;
        }
        return false;
    }

    // beats 73.31%(48 ms for 31 tests)
    public String findLongestWord_2(String s, List<String> d) {
        Collections.sort(d, new Comparator<String>() {
            public int compare(String a, String b) {
                int diff = b.length() - a.length();
                return diff != 0 ? diff : a.compareTo(b);
            }
        });
        char[] cs = s.toCharArray();
        for (String word : d) {
            if (isSubword(word, cs)) return word;
        }
        return "";
    }

    // beats 95.82%(23 ms for 31 tests)
    public String findLongestWord2(String s, List<String> d) {
        String res = "";
        int n = s.length();
        int[][] lists = compile(s);
        for (String word : d) {
            int len = word.length();
            if (len <= n && len >= res.length() && isSubword(word, lists)) {
                if (len > res.length() || word.compareTo(res) < 0) {
                    res = word;
                }
            }
        }
        return res;
    }

    private int[][] compile(String s) {
        char[] cs = s.toCharArray();
        List<List<Integer> > lists = new ArrayList<>();
        for (int i = 0; i < 26; i++) {
            lists.add(new ArrayList<>());
        }
        for (int i = 0; i < cs.length; i++) {
            lists.get(cs[i] - 'a').add(i);
        }
        int[][] arrays = new int[26][];
        int i = 0;
        for (List<Integer> list : lists) {
            int[] array = new int[list.size()];
            int j = 0;
            for (int k : list) {
                array[j++] = k;
            }
            arrays[i++] = array;
        }
        return arrays;
    }

    private boolean isSubword(String word, int[][] lists) {
        int[] indices = new int[26];
        int prev = -1;
outer:
        for (char c : word.toCharArray()) {
            int index = c - 'a';
            int[] list = lists[index];
            for (int j = indices[index]; j < list.length; j++) { // Binary Search?
                if (list[j] > prev) {
                    prev = list[j];
                    indices[index] = j + 1;
                    continue outer;
                }
            }
            return false;
        }
        return true;
    }

    void test(String s, String[] list, String expected) {
        List<String> d = Arrays.asList(list);
        assertEquals(expected, findLongestWord(s, d));
        assertEquals(expected, findLongestWord_2(s, d));
        assertEquals(expected, findLongestWord2(s, d));
    }

    @Test
    public void test() {
        test("a", new String[] {}, "");
        test("aaa", new String[] {"aaa", "aa", "a"}, "aaa");
        test("abpcplea", new String[] {"ale", "aab", "apple","monkey","plea"}, "apple");
        test("abpcplea", new String[] {"b", "a", "c"}, "a");
        test("aewfafwafjlwajflwajflwafj", new String[] {
            "apple", "ewaf", "awefawfwaf", "awef", "awefe", "ewafeffewafewf"
        }, "ewaf");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("LongestWordByDeleting");
    }
}
