import java.util.*;
import java.util.stream.*;

import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.Utils;

// LC249: https://leetcode.com/problems/group-shifted-strings/?tab=Description
//
// Given a string, we can "shift" each of its letter to its successive letter,
// Given a list of strings which contains only lowercase alphabets, group all
// strings that belong to the same shifting sequence.
public class GroupShiftedStrings {
    // beats 93.09%(3 ms for 23 tests)
    public List<List<String> > groupStrings(String[] strings) {
        Map<String, List<String> > map = new HashMap<>();
        for (String s : strings) {
            char[] buf = new char[s.length()];
            char base = s.charAt(0);
            for (int i = s.length() - 1; i >= 0; i--) {
                buf[i] = (char)((s.charAt(i) - base + 26) % 26 + 'a');
            }
            String key = new String(buf);
            List<String> group = map.get(key);
            if (group == null) {
                map.put(key, group = new ArrayList<>());
            }
            group.add(s);
        }
        return new ArrayList<>(map.values());
    }

    // beats 7.90%(68 ms for 23 tests)
    public List<List<String> > groupStrings2(String[] strings) {
        Map<String, List<String> > map = new HashMap<>();
        for (String s : strings) {
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < s.length(); i++) {
                sb.append(String.format("%2d", (s.charAt(i) - s.charAt(i - 1) + 26) % 26));
            }
            String key = sb.toString();
            List<String> group = map.get(key);
            if (group == null) {
                map.put(key, group = new ArrayList<>());
            }
            group.add(s);
        }
        return new ArrayList<>(map.values());
    }

    // beats 0.99%(108 ms for 23 tests)
    public List<List<String> > groupStrings3(String[] strings) {
        return new ArrayList<>(
            Stream.of(strings).collect(
                Collectors.groupingBy(s -> s.chars().mapToObj(c -> (c - s.charAt(0) + 26) % 26)
                                      .collect(Collectors.toList()))).values());
    }

    void test(Function<String[], List<List<String>>> groupStrings,
              String[] strings, String[][] expected) {
        List<List<String> > res = groupStrings.apply(strings);
        Arrays.sort(expected, new Utils.StrArrayComparator());
        assertArrayEquals(expected, Utils.toSortedStrs(res));
    }

    void test(String[] strings, String[][] expected) {
        GroupShiftedStrings g = new GroupShiftedStrings();
        test(g::groupStrings, strings, expected);
        test(g::groupStrings2, strings, expected);
        test(g::groupStrings3, strings, expected);
    }

    @Test
    public void test() {
        test(new String[] {"abc", "bcd", "acef", "xyz", "az", "ba", "a", "z"},
             new String[][] {{"abc","bcd","xyz"}, {"az","ba"}, {"acef"}, {"a","z"}});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("GroupShiftedStrings");
    }
}
