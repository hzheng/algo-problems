import java.util.*;

import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/palindrome-partitioning/
//
// Given a string s, partition s such that every substring of the partition is a
// palindrome. Return all possible palindrome partitioning of s.
public class PalindromePartition {
    // beats 21.86%
    public List<List<String> > partition(String s) {
        int len = s.length();
        char[] chars = s.toCharArray();
        Boolean[][] table = new Boolean[len][len];
        for (int i = 0; i < len; i++) {
            table[i][i] = true;
            for (int j = i + 1; j < len; j++) {
                fillPalindromeTable(chars, i, j, table);
            }
        }
        return partition(chars, 0, len - 1, table);
    }

    private List<List<String> > partition(char[] chars, int start, int end,
                                          Boolean[][] table) {
        List<List<String> > partitions = new LinkedList<>();
        if (start >= end) {
            List<String> list = new LinkedList<>();
            if (start == end) {
                list.add(String.valueOf(chars[start]));
            }
            partitions.add(list);
            return partitions;
        }

        for (int i = start; i <= end; i++) {
            if (table[start][i]) {
                String first = new String(chars, start, i - start + 1);
                for (List<String> list : partition(chars, i + 1, end, table)) {
                    list.add(0, first);
                    partitions.add(list);
                };
            }
        }
        return partitions;
    }

    private void fillPalindromeTable(char[] chars, int start, int end,
                                     Boolean[][] table) {
        if (table[start][end] != null) return;

        if (chars[start] != chars[end]) {
            table[start][end] = false;
        } else if (start + 2 >= end) {
            table[start][end] = true;
        } else {
            fillPalindromeTable(chars, start + 1, end - 1, table);
            table[start][end] = table[start + 1][end - 1];
        }
    }

    String[][] sort(String[][] lists) {
        Arrays.sort(lists, (a, b) -> {
            int len = Math.min(a.length, b.length);
            int i = 0;
            for (; i < len && (a[i].equals(b[i])); i++);
            return (i == len) ? (a.length - b.length) : a[i].compareTo(b[i]);
        });
        return lists;
    }

    void test(Function<String, List<List<String>>> partition,
              String s, String[][] expected) {
        List<List<String> > res = partition.apply(s);
        // System.out.println(res);
        String[][] resArray = res.stream().map(
            a -> a.toArray(new String[0])).toArray(String[][]::new);
        resArray = sort(resArray);
        expected = sort(expected);
        assertArrayEquals(expected, resArray);
    }

    void test(String s, String[][] expected) {
        PalindromePartition p = new PalindromePartition();
        test(p::partition, s, expected);
    }

    @Test
    public void test1() {
        test("aab", new String[][] {{"a", "a", "b"}, {"aa", "b"}});
        test("aababa", new String[][] {
            {"a", "a", "b", "a", "b", "a"}, {"a", "a", "b", "aba"},
            {"a", "a", "bab", "a"}, {"a", "aba", "b", "a"}, {"a", "ababa"},
            {"aa", "b", "a", "b", "a"}, {"aa", "b", "aba"}, {"aa", "bab", "a"}
        });
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("PalindromePartition");
    }
}
