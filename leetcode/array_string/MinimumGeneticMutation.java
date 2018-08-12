import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC433: https://leetcode.com/problems/minimum-genetic-mutation/
//
// A gene string can be represented by an 8-character long string, with choices
// from "A", "C", "G", "T". Suppose we need to investigate about a mutation,
// where ONE mutation is defined as ONE single character changed in the string.
// There is a given gene "bank", which records all the valid gene mutations. A
// gene must be in the bank to make it a valid gene string.
// Determine what is the minimum number of mutations needed to mutate from
// "start" to "end". If there is no such a mutation, return -1.
public class MinimumGeneticMutation {
    // BFS + Queue
    // beats 68.70%(2 ms for 14 tests)
    public int minMutation(String start, String end, String[] bank) {
        Set<String> validSet = new HashSet<>(Arrays.asList(bank));
        Set<String> visited = new HashSet<>();
        Queue<String> q = new LinkedList<>();
        q.offer(start);
        char[] chars = new char[] { 'A', 'C', 'G', 'T' };
        for (int level = 0; !q.isEmpty(); level++) {
            for (int i = q.size(); i > 0; i--) {
                String mutation = q.poll();
                if (!visited.add(mutation)) continue;

                if (mutation.equals(end)) return level;

                for (int j = mutation.length() - 1; j >= 0; j--) {
                    char old = mutation.charAt(j);
                    for (char c : chars) {
                        if (c == old) continue;

                        String next = mutation.substring(0, j) + c
                                      + mutation.substring(j + 1);
                        if (validSet.contains(next)) {
                            q.offer(next);
                        }
                    }
                }
            }
        }
        return -1;
    }

    // BFS + Queue
    // beats 68.70%(2 ms for 14 tests)
    public int minMutation_2(String start, String end, String[] bank) {
        Set<String> validSet = new HashSet<>(Arrays.asList(bank));
        Set<String> visited = new HashSet<>();
        Queue<String> q = new LinkedList<>();
        q.offer(start);
        char[] chars = new char[] { 'A', 'C', 'G', 'T' };
        for (int level = 0; !q.isEmpty(); level++) {
            for (int i = q.size(); i > 0; i--) {
                String mutation = q.poll();
                if (!visited.add(mutation)) continue;

                if (mutation.equals(end)) return level;

                char[] m = mutation.toCharArray();
                for (int j = m.length - 1; j >= 0; j--) {
                    char old = m[j];
                    for (char c : chars) {
                        m[j] = c;
                        String next = String.valueOf(m);
                        if (validSet.contains(next)) {
                            q.offer(next);
                        }
                    }
                    m[j] = old;
                }
            }
        }
        return -1;
    }

    // BFS + Queue
    // beats 68.70%(2 ms for 14 tests)
    public int minMutation_3(String start, String end, String[] bank) {
        Set<String> validSet = new HashSet<>(Arrays.asList(bank));
        Queue<String> q = new LinkedList<>();
        q.offer(start);
        char[] chars = new char[] { 'A', 'C', 'G', 'T' };
        for (int level = 0; !q.isEmpty(); level++) {
            for (int i = q.size(); i > 0; i--) {
                String mutation = q.poll();
                if (mutation.equals(end)) return level;

                char[] m = mutation.toCharArray();
                for (int j = m.length - 1; j >= 0; j--) {
                    char old = m[j];
                    for (char c : chars) {
                        m[j] = c;
                        String next = String.valueOf(m);
                        if (validSet.contains(next)) {
                            q.offer(next);
                            validSet.remove(next); // avoid revisted!
                        }
                    }
                    m[j] = old;
                }
            }
        }
        return -1;
    }

    // BFS + Queue + Bit Manipulation
    // beats 68.70%(2 ms for 14 tests)
    public int minMutation2(String start, String end, String[] bank) {
        Set<Integer> validSet = new HashSet<>();
        for (String s : bank) {
            validSet.add(encode(s));
        }
        Queue<Integer> q = new LinkedList<>();
        q.offer(encode(start));
        int target = encode(end);
        for (int level = 0, length = start.length(); !q.isEmpty(); level++) {
            for (int i = q.size(); i > 0; i--) {
                int mutation = q.poll();
                if (mutation == target) return level;

                for (int j = 0, mask = 3; j < length; j++, mask <<= 2) {
                    int next = mutation & ~mask;
                    for (int k = 0; k < 4; k++, next += mask / 3) {
                        if (validSet.contains(next)) {
                            q.offer(next);
                            validSet.remove(next);
                        }
                    }
                }
            }
        }
        return -1;
    }

    private int encode(String seq) {
        int res = 0;
        for (char c : seq.toCharArray()) {
            res <<= 2;
            switch (c) {
                case 'A': break;
                case 'C': res += 1; break;
                case 'G': res += 2; break;
                case 'T': res += 3; break;
            }
        }
        return res;
    }

    void test(String start, String end, String[] bank, int expected) {
        assertEquals(expected, minMutation(start, end, bank));
        assertEquals(expected, minMutation_2(start, end, bank));
        assertEquals(expected, minMutation_3(start, end, bank));
        assertEquals(expected, minMutation2(start, end, bank));
    }

    @Test
    public void test() {
        test("AACCGGTT", "AACCGGTA", new String[] { "AACCGGTA" }, 1);
        test("AACCGGTT", "AAACGGTA", new String[] { "AACCGGTA", "AACCGCTA",
                                                    "AAACGGTA" }, 2);
        test("AAAAACCC", "AACCCCCC", new String[] { "AAAACCCC", "AAACCCCC",
                                                    "AACCCCCC" }, 3);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
