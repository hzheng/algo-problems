import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

// LC411: https://leetcode.com/problems/minimum-unique-word-abbreviation/
//
// A string such as "word" contains the following abbreviations:
// ["word", "1ord", "w1rd", "wo1d", "wor1", "2rd", "w2d", "wo2", "1o1d", "1or1",
// "w1r1", "1o2", "2r1", "3d", "w3", "4"]
// Given a target string and a set of strings in a dictionary, find an
// abbreviation of this target string with the smallest possible length such
// that it does not conflict with abbreviations of the strings in the dictionary.
// Each number or letter in the abbreviation is considered length = 1. For
// example, the abbreviation "a32bc" has length = 4.
public class MinAbbreviation {
    // Recursion + Bit Manipulation + Hash Table
    // beats 77.52%%(32 ms for 46 tests)
    public String minAbbreviation(String target, String[] dictionary) {
        int len = target.length();
        Set<Integer> conflicts = new HashSet<>();
        for (String word : dictionary) {
            if (word.length() != len) continue;

            int mask = 0;
            for (int j = 0; j < len; j++) {
                if (word.charAt(j) == target.charAt(j)) {
                    mask |= (1 << j);
                }
            }
            conflicts.add(mask);
        }
        if (conflicts.isEmpty()) return String.valueOf(len);

        for (int i = 1; i < len; i++) {
            int mask = contains(i, len, conflicts);
            if (mask > 0) {
                StringBuilder sb = new StringBuilder();
                int last = -1;
                int j;
                for (j = 0; j < len; j++) {
                    if ((mask & (1 << j)) != 0) {
                        if (j - last > 1) {
                            sb.append(j - last - 1);
                        }
                        sb.append(target.charAt(j));
                        last = j;
                    }
                }
                if (j - last > 1) {
                    sb.append(j - last - 1);
                }
                return sb.toString();
            }
        }
        return target;
    }

    private int contains(int bitCount, int len, Set<Integer> conflicts) {
        // try first and last before middles
        int res = contains(bitCount - 1, 1, len, 1, conflicts);
        if (res > 0) return res;

        res = contains(bitCount - 1, 1, len - 1, 1 << (len - 1), conflicts);
        return res > 0 ? res : contains(bitCount, 1, len - 1, 0, conflicts);
    }

    private int contains(int bitCount, int start, int end, int mask,
                         Set<Integer> conflicts) {
        if (bitCount == 0) {
            for (int conflict : conflicts) {
                if ((conflict & mask) == mask) return 0;
            }
            return mask;
        }
        if (start >= end) return 0;

        int res = contains(bitCount - 1, start + 1, end, mask | (1 << start), conflicts);
        return res > 0 ? res : contains(bitCount, start + 1, end, mask, conflicts);
    }

    // Recursion + DFS + Bit Manipulation + Hash Table
    // beats 85.91%%(23 ms for 46 tests)
    public String minAbbreviation2(String target, String[] dictionary) {
        int len = target.length();
        int confined = 0;
        Set<Integer> dictFlags = new HashSet<>();
        for (String word : dictionary) {
            if (word.length() != len) continue;

            int flag = 0;
            for (int i = len - 1, bit = 1; i >= 0; i--, bit <<= 1) {
                if (target.charAt(i) != word.charAt(i)) {
                    flag |= bit;
                }
            }
            dictFlags.add(flag);
            confined |= flag;
        }
        int[] min = new int[] {0, len + 1}; // minAbbreviation and minLen
        dfs(dictFlags, 1, 0, len, min, confined);
        String res = "";
        for (int i = len - 1, last = i, abbr = min[0]; i >= 0; i--, abbr >>= 1) {
            if ((abbr & 1) != 0) {
                if (last - i > 0) {
                    res = "" + (last - i) + res;
                }
                last = i - 1;
                res = target.charAt(i) + res;
            } else if (i == 0) {
                res = "" + (last - i + 1) + res;
            }
        }
        return res;
    }

    private void dfs(Set<Integer> dict, int bit, int mask, int len, int[] min,
                     int confined) {
        int curLen = abbreviationLen(mask, len);
        if (curLen >= min[1]) return;

        for (int flag : dict) {
            if ((mask & flag) == 0) {
                for (int i = bit, max = 1 << len; i < max; i <<= 1) {
                    if ((confined & i) != 0) {
                        dfs(dict, i << 1, mask | i, len, min, confined);
                    }
                }
                return;
            }
        }
        min[0] = mask;
        min[1] = curLen;
    }

    private int abbreviationLen(int mask, int len) {
        int count = len;
        for (int i = 0b11, max = 1 << len; i < max; i <<= 1) {
            if ((mask & i) == 0) {
                count--;
            }
        }
        return count;
    }

    // Bit Manipulation + Hash Table
    // beats 49.16%%(109 ms for 46 tests)
    public String minAbbreviation3(String target, String[] dictionary) {
        int len = target.length();
        Set<Integer> dictFlags = new HashSet<>();
        for (String word : dictionary) {
            if (word.length() != len) continue;

            int flag = 0;
            for (int i = 0; i < len; i++) {
                if (target.charAt(i) == word.charAt(i)) {
                    flag |= 1 << i;
                }
            }
            dictFlags.add(flag);
        }
        int mask = 0;
outer:
        for (int i = 0, min = len + 1, maxMask = 1 << len; i < maxMask; i++) {
            for (int flag : dictFlags) {
                if ((i | flag) == flag) continue outer;
            }
            int abbrLen = abbreviationLen(i, len);
            if (min > abbrLen) {
                min = abbrLen;
                mask = i;
            }
        }
        int count = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            if ((mask & (1 << i)) != 0) {
                if (count > 0) {
                    sb.append(count);
                    count = 0;
                }
                sb.append(target.charAt(i));
            } else {
                count++;
            }
        }
        if (count > 0) {
            sb.append(count);
        }
        return sb.toString();
    }

    void test(String target, String[] dict, String ... expected) {
        MinAbbreviation m = new MinAbbreviation();
        test(m::minAbbreviation, target, dict, expected);
        test(m::minAbbreviation2, target, dict, expected);
        test(m::minAbbreviation3, target, dict, expected);
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(Function<String, String[], String> minAbbr,
              String target, String[] dict, String ... expected) {
        assertThat(Arrays.asList(expected), hasItem(minAbbr.apply(target, dict)));
    }

    @Test
    public void test1() {
        test("apple", new String[] {"kkkk"}, "5");
        test("apple", new String[] {"kkkkk"}, "a4", "4e");
        test("aabadaa", new String[] {"aabaxaa", "aaxadaa"}, "2b1d2", "2bad2");
        test("apple", new String[] {"plain", "amber", "blade"},
             "1p3", "ap3", "a3e", "2p2", "3le", "3l1");
        test("abcdef", new String[] {"ablade", "xxxxef", "abdefi", "blade"},
             "2c3", "a4f");
        test("apple", new String[] {"blade"}, "a4");
        test("usa", new String[] {"hel", "uus", "ssa", "uaa", "uss", "uua", "aaa",
                                  "uuu", "sss", "uul", "url", "uls", "uas"}, "usa");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MinAbbreviation");
    }
}
