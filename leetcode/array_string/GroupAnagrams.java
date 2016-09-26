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
    public List<List<String> > groupAnagrams(String[] strs) {
        Map<Integer, PriorityQueue<String> > map = new HashMap<>();
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
        List<List<String> > res = new ArrayList<>();
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
    public List<List<String> > groupAnagrams2(String[] strs) {
        Map<String, List<String> > map = new HashMap<>();
        for(String s : strs) {
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
        List<List<String> > res = new ArrayList<>();
        for (List<String> list : map.values()) {
            Collections.sort(list);
            res.add(list);
        }
        return res;
    }

    // Solution of Choice
    // beats 17.65%(37 ms)
    public List<List<String> > groupAnagrams3(String[] strs) {
        Map<String, List<String> > map = new HashMap<>();
        Arrays.sort(strs);
        for(String s : strs) {
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

    void test(Function<String[], List<List<String>>> group,
              String[][] expected, String ... strs) {
        String[][] anagrams = group.apply(strs).stream().map(
            s -> s.toArray(new String[0])).toArray(String[][]::new);
        sort(anagrams);
        assertArrayEquals(expected, anagrams);
    }

    String[][] sort(String[][] lists) {
        Arrays.sort(lists, (a, b) -> {
            int len = Math.min(a.length, b.length);
            int i = 0;
            for (; i < len && (a[i] == b[i]); i++);
            if (i == len) return a.length - b.length;
            return a[i].compareTo(b[i]);
        });
        return lists;
    }

    void test(String[][] expected, String ... strs) {
        GroupAnagrams g = new GroupAnagrams();
        sort(expected);
        test(g::groupAnagrams, expected, strs);
        test(g::groupAnagrams2, expected, strs);
        test(g::groupAnagrams3, expected, strs);
    }

    @Test
    public void test1() {
        test(new String[][] { {"ate", "eat","tea"}, {"nat", "tan"}, {"bat"}},
             "eat", "tea", "tan", "ate", "nat", "bat");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("GroupAnagrams");
    }
}
