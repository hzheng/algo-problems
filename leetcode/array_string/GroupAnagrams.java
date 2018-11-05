import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// LC049: https://leetcode.com/problems/anagrams/
//
// Given an array of strings, group anagrams together.
// For the return value, each inner list's elements must follow the
// lexicographic order. All inputs will be in lower-case.
public class GroupAnagrams {
    // beats 27.25%(34 ms)
    public List<List<String>> groupAnagrams(String[] strs) {
        Map<Integer, PriorityQueue<String>> map = new HashMap<>();
        for (String s : strs) {
            char[] chars = s.toCharArray();
            Arrays.sort(chars);
            int code = Arrays.hashCode(chars);
            PriorityQueue<String> words = null;
            if (!map.containsKey(code)) {
                words = new PriorityQueue<>();
                map.put(code, words);
            } else {
                words = map.get(code);
            }
            words.add(s);
        }
        List<List<String>> res = new ArrayList<>();
        for (int code : map.keySet()) {
            List<String> group = new ArrayList<>();
            PriorityQueue<String> groupQueue = map.get(code);
            while (!groupQueue.isEmpty()) {
                group.add(groupQueue.poll());
            }
            res.add(group);
        }
        return res;
    }

    // beats 89.45%(24 ms)
    public List<List<String>> groupAnagrams2(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();
        for (String s : strs) {
            char[] chars = s.toCharArray();
            Arrays.sort(chars);
            String key = new String(chars);
            if (map.containsKey(key)) {
                map.get(key).add(s);
            } else {
                List<String> list = new ArrayList<>();
                list.add(s);
                map.put(key, list);
            }
        }
        List<List<String>> res = new ArrayList<>();
        for (List<String> list : map.values()) {
            Collections.sort(list);
            res.add(list);
        }
        return res;
    }

    // Hash Table + Sort
    // time complexity: O(N * K * log(K)), space complexity: O(N * K)
    // beats 17.65%(37 ms)
    public List<List<String>> groupAnagrams3(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();
        for (String s : strs) {
            char[] chars = s.toCharArray();
            Arrays.sort(chars);
            String key = new String(chars);
            if (map.containsKey(key)) {
                map.get(key).add(s);
            } else {
                List<String> list = new ArrayList<>();
                list.add(s);
                map.put(key, list);
            }
        }
        return new ArrayList<>(map.values());
    }

    // Solution of Choice
    // Hash Table + Sort
    // time complexity: O(N * K * log(K)), space complexity: O(N * K)
    // beats 12.63%(59 ms for 101 tests)
    public List<List<String>> groupAnagrams3_2(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();
        for (String s : strs) {
            char[] cs = s.toCharArray();
            Arrays.sort(cs);
            map.computeIfAbsent(String.valueOf(cs), a -> new ArrayList<>()).add(s);
        }
        return new ArrayList<>(map.values());
    }

    // Solution of Choice
    // Hash Table + Sort
    // time complexity: O(N * K), space complexity: O(N * K)
    // beats 22.08%(33 ms for 101 tests)
    public List<List<String>> groupAnagrams4(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();
        int[] count = new int[26];
        for (String s : strs) {
            Arrays.fill(count, 0);
            for (char c : s.toCharArray()) {
                count[c - 'a']++;
            }
            StringBuilder sb = new StringBuilder();
            for (int c : count) {
                sb.append('#').append(c);
            }
            String key = sb.toString();
            if (!map.containsKey(key)) {
                map.put(key, new ArrayList<>());
            }
            map.get(key).add(s);
        }
        return new ArrayList<>(map.values());
    }

    // Hash Table + Math (may overflow)
    // time complexity: O(N * K), space complexity: O(N * K)
    // beats 99.68%(13 ms for 101 tests)
    public List<List<String>> groupAnagrams5(String[] strs) {
        final int[] primes = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 41, 43, 47, 53, 59, 61, 67,
                              71, 73, 79, 83, 89, 97, 101, 103};
        List<List<String>> res = new ArrayList<>();
        Map<Integer, Integer> map = new HashMap<>();
        for (String s : strs) {
            int key = 1;
            for (char c : s.toCharArray()) {
                key *= primes[c - 'a'];
            }
            List<String> list;
            if (map.containsKey(key)) {
                list = res.get(map.get(key));
            } else {
                res.add(list = new ArrayList<>());
                map.put(key, res.size() - 1);
            }
            list.add(s);
        }
        return res;
    }

    void test(Function<String[], List<List<String>>> group, String[][] expected, String... strs) {
        String[][] anagrams = group.apply(strs).stream().map(s -> s.toArray(new String[0]))
                                   .toArray(String[][]::new);
        sort(anagrams);
        sort(expected);
        assertArrayEquals(expected, anagrams);
    }

    String[][] sort(String[][] lists) {
        for (String[] list : lists) {
            Arrays.sort(list);
        }
        Arrays.sort(lists, (a, b) -> {
            int len = Math.min(a.length, b.length);
            int i = 0;
            for (; i < len && (a[i] == b[i]); i++) {}
            if (i == len) return a.length - b.length;
            return a[i].compareTo(b[i]);
        });
        return lists;
    }

    void test(String[][] expected, String... strs) {
        GroupAnagrams g = new GroupAnagrams();
        test(g::groupAnagrams, expected, strs);
        test(g::groupAnagrams2, expected, strs);
        test(g::groupAnagrams3, expected, strs);
        test(g::groupAnagrams3_2, expected, strs);
        test(g::groupAnagrams4, expected, strs);
        test(g::groupAnagrams5, expected, strs);
    }

    @Test
    public void test1() {
        test(new String[0][0]);
        test(new String[][] {{"ate", "eat", "tea"}, {"nat", "tan"}, {"bat"}}, "eat", "tea", "tan",
             "ate", "nat", "bat");
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
