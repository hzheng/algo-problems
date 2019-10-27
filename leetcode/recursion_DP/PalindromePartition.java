import java.util.*;

import java.util.function.Function;

import org.junit.Test;

import static org.junit.Assert.*;

// LC131: https://leetcode.com/problems/palindrome-partitioning/
//
// Given a string s, partition s such that every substring of the partition is a
// palindrome. Return all possible palindrome partitioning of s.
public class PalindromePartition {
    // beats 21.86%(9 ms)
    public List<List<String>> partition(String s) {
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

    private List<List<String>> partition(char[] chars, int start, int end,
                                         Boolean[][] table) {
        List<List<String>> partitions = new LinkedList<>();
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
                }
            }
        }
        return partitions;
    }

    // Backtracking
    // beats 59.09%(7 ms)
    public List<List<String>> partition2(String s) {
        int len = s.length();
        char[] chars = s.toCharArray();
        Boolean[][] table = new Boolean[len][len];
        for (int i = 0; i < len; i++) {
            table[i][i] = true;
            for (int j = i + 1; j < len; j++) {
                fillPalindromeTable(chars, i, j, table);
            }
        }

        List<List<String>> res = new ArrayList<>();
        partition2(s, 0, len, new ArrayList<>(), res, table);
        return res;
    }

    private void partition2(String s, int start, int end, List<String> partition,
                            List<List<String>> res, Boolean[][] table) {
        if (start == end) {
            res.add(new ArrayList<>(partition));
            return;
        }

        for (int i = start; i < end; i++) {
            if (table[start][i]) {
                partition.add(s.substring(start, i + 1));
                partition2(s, i + 1, end, partition, res, table);
                partition.remove(partition.size() - 1);
            }
        }
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

    // Backtracking
    // beats 68.05%(6 ms)
    public List<List<String>> partition3(String s) {
        List<List<String>> res = new ArrayList<>();
        partition3(s, 0, new ArrayList<>(), res);
        return res;
    }

    private void partition3(String s, int start, List<String> cur,
                            List<List<String>> res) {
        if (start >= s.length()) {
            res.add(new ArrayList<>(cur));
            return;
        }
        for (int i = start; i < s.length(); i++) {
            if (isPalindrome(s, start, i)) {
                cur.add(s.substring(start, i + 1));
                partition3(s, i + 1, cur, res);
                cur.remove(cur.size() - 1);
            }
        }
    }

    private boolean isPalindrome(String s, int start, int end) {
        for (int i = start, j = end; i < j; i++, j--) {
            if (s.charAt(i) != s.charAt(j)) return false;
        }
        return true;
    }

    // Solution of Choice
    // Dynamic Programming
    // beats 68.05%(6 ms)
    public List<List<String>> partition4(String s) {
        int len = s.length();
        @SuppressWarnings("unchecked")
        List<List<String>>[] res = new List[len + 1];
        res[0] = new ArrayList<>();
        res[0].add(new ArrayList<>());
        boolean[][] dp = new boolean[len][len];
        char[] cs = s.toCharArray();
        for (int i = 0; i < len; i++) {
            res[i + 1] = new ArrayList<>();
            for (int j = 0; j <= i; j++) {
                if (cs[j] == cs[i] && (i - j <= 1 || dp[j + 1][i - 1])) {
                    dp[j][i] = true;
                    String substr = s.substring(j, i + 1);
                    for (List<String> list : res[j]) {
                        List<String> cloned = new ArrayList<>(list);
                        cloned.add(substr);
                        res[i + 1].add(cloned);
                    }
                }
            }
        }
        return res[len];
    }

    // LC132: https://leetcode.com/problems/palindrome-partitioning-ii/
    // Return the minimum cuts needed for a palindrome partitioning of s.

    // Time Limit Exceeded
    public int minCut(String s) {
        int len = s.length();
        char[] chars = s.toCharArray();
        Boolean[][] table = new Boolean[len][len];
        for (int i = 0; i < len; i++) {
            table[i][i] = true;
            for (int j = i + 1; j < len; j++) {
                fillPalindromeTable(chars, i, j, table);
            }
        }
        return minCut(chars, 0, len - 1, table, new HashMap<>());
    }

    private int minCut(char[] chars, int start, int end, Boolean[][] table,
                       Map<String, Integer> cache) {
        String key = "" + start + "-" + end;
        if (cache.containsKey(key)) return cache.get(key);

        int min = Integer.MAX_VALUE;
        for (int i = end; i >= start; i--) {
            if (table[start][i]) {
                if (i == end) {
                    min = -1;
                    break;
                }

                if (i + 1 == end) {
                    min = 0;
                    break;
                }

                String k = "" + (i + 1) + "-" + end;
                int m;
                if (cache.containsKey(k)) {
                    m = cache.get(k);
                } else {
                    m = minCut(chars, i + 1, end, table, cache);
                }
                min = Math.min(min, m);
            }
        }
        cache.put(key, ++min);
        return min;
    }

    // 2D-Dynamic Programming
    // beats 41.11%(35 ms)
    public int minCut2(String s) {
        int len = s.length();
        char[] chars = s.toCharArray();
        Boolean[][] table = new Boolean[len][len];
        for (int i = 0; i < len; i++) {
            table[i][i] = true;
            for (int j = i + 1; j < len; j++) {
                fillPalindromeTable(chars, i, j, table);
            }
        }

        Integer[] table2 = new Integer[len];
        fillPalindromeTable2(0, len - 1, table, table2);
        return table2[0];
    }

    private void fillPalindromeTable2(int start, int end,
                                      Boolean[][] table, Integer[] table2) {
        if (table2[start] != null) return;

        if (table[start][end]) {
            table2[start] = 0;
            return;
        }

        int min = Integer.MAX_VALUE;
        for (int i = end - 1; i >= start; i--) {
            if (table[start][i]) {
                fillPalindromeTable2(i + 1, end, table, table2);
                min = Math.min(min, table2[i + 1]);
                if (min == 0) break;
            }
        }
        table2[start] = min + 1;
    }

    // 2D-Dynamic Programming
    // beats 93.42%(14 ms)
    public int minCut3(String s) {
        int len = s.length();
        char[] chars = s.toCharArray();
        Integer[] table2 = new Integer[len];
        fillPalindromeTable3(chars, 0, len - 1, new Boolean[len][len], table2);
        return table2[0];
    }

    private void fillPalindromeTable3(char[] chars, int start, int end,
                                      Boolean[][] table, Integer[] table2) {
        if (table2[start] != null) return;

        if (isPalindrome(chars, start, end, table)) {
            table2[start] = 0;
            return;
        }

        int min = Integer.MAX_VALUE;
        for (int i = end - 1; i >= start; i--) {
            if (isPalindrome(chars, start, i, table)) {
                fillPalindromeTable3(chars, i + 1, end, table, table2);
                min = Math.min(min, table2[i + 1]);
                if (min == 0) break;
            }
        }
        table2[start] = min + 1;
    }

    private Boolean isPalindrome(char[] chars, int start, int end,
                                 Boolean[][] table) {
        if (table[start][end] != null) return table[start][end];

        if (chars[start] != chars[end]) return table[start][end] = false;

        if (start + 2 >= end) return table[start][end] = true;

        return table[start][end] = isPalindrome(chars, start + 1, end - 1, table);
    }

    // 2D-Dynamic Programming
    // beats 35.57%(37 ms)
    public int minCut4(String s) {
        int len = s.length();
        boolean palindromes[][] = new boolean[len][len];
        int cuts[] = new int[len];
        // unlike above solution, this time table is built from small indices,
        // which makes life much easier
        for (int i = 0; i < len; i++) {
            cuts[i] = i;
            for (int j = 0; j <= i; j++) {
                if (s.charAt(i) == s.charAt(j)
                    && (i - j < 2 || palindromes[j + 1][i - 1])) {
                    palindromes[j][i] = true;
                    cuts[i] = (j == 0) ? 0 : Math.min(cuts[i], cuts[j - 1] + 1);
                }
            }
        }
        return cuts[len - 1];
    }

    // Solution of Choice
    // 1D-Dynamic Programming(Manancher algorithm)
    // https://leetcode.com/discuss/9476/solution-does-not-need-table-palindrome-right-uses-only-space
    // time complexity: O(N ^ 2), space complexity: O(N)
    // beats 94.23%(13 ms)
    public int minCut5(String s) {
        int len = s.length();
        char[] c = s.toCharArray();
        int[] cuts = new int[len + 1];
        for (int i = 0; i <= len; i++) {
            cuts[i] = i - 1;
        }
        for (int i = 0; i < len; i++) {
            // odd length palindrome
            for (int j = 0; i >= j && i + j < len && c[i - j] == c[i + j]; j++) {
                cuts[i + j + 1] = Math.min(cuts[i + j + 1], cuts[i - j] + 1);
            }
            // even length palindrome
            for (int j = 1; i + 1 >= j && i + j < len && c[i - j + 1] == c[i + j]; j++) {
                cuts[i + j + 1] = Math.min(cuts[i + j + 1], cuts[i - j + 1] + 1);
            }
        }
        return cuts[len];
    }

    // Solution of Choice
    // BFS + Queue
    // beats 97.34%(10 ms)
    public int minCut6(String s) {
        int len = s.length();
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(0);
        boolean[] visited = new boolean[len];
        for (int cuts = 0; ; cuts++) {
            for (int i = queue.size(); i > 0; i--) {
                int cur = queue.poll();
                for (int j = len - 1; j >= cur; j--) {
                    if (!visited[j] && isPalindrome(s, cur, j)) {
                        if (j == len - 1) return cuts;

                        queue.offer(j + 1);
                    }
                }
                visited[cur] = true;
            }
        }
    }

    String[][] sort(String[][] lists) {
        Arrays.sort(lists, (a, b) -> {
            int len = Math.min(a.length, b.length);
            int i = 0;
            for (; i < len && (a[i].equals(b[i])); i++) {}
            return (i == len) ? (a.length - b.length) : a[i].compareTo(b[i]);
        });
        return lists;
    }

    void test(Function<String, List<List<String>>> partition,
              String s, String[][] expected) {
        List<List<String>> res = partition.apply(s);
        String[][] resArray = res.stream().map(
                a -> a.toArray(new String[0])).toArray(String[][]::new);
        resArray = sort(resArray);
        expected = sort(expected);
        assertArrayEquals(expected, resArray);
    }

    void test(String s, String[][] expected) {
        PalindromePartition p = new PalindromePartition();
        test(p::partition, s, expected);
        test(p::partition2, s, expected);
        test(p::partition3, s, expected);
        test(p::partition4, s, expected);
    }

    @Test
    public void test1() {
        test("aab", new String[][]{{"a", "a", "b"}, {"aa", "b"}});
        test("aababa", new String[][]{
                {"a", "a", "b", "a", "b", "a"}, {"a", "a", "b", "aba"},
                {"a", "a", "bab", "a"}, {"a", "aba", "b", "a"}, {"a", "ababa"},
                {"aa", "b", "a", "b", "a"}, {"aa", "b", "aba"}, {"aa", "bab", "a"}
        });
    }

    void test(Function<String, Integer> minCut, String name,
              String s, int expected) {
        long t1 = System.nanoTime();
        assertEquals(expected, (int)minCut.apply(s));
        System.out.format("%s: %.3f ms\n", name, (System.nanoTime() - t1) * 1e-6);
    }

    void test(String s, int expected) {
        PalindromePartition p = new PalindromePartition();
        test(p::minCut, "minCut", s, expected);
        test(p::minCut2, "minCut2", s, expected);
        test(p::minCut3, "minCut3", s, expected);
        test(p::minCut4, "minCut4", s, expected);
        test(p::minCut5, "minCut5", s, expected);
        test(p::minCut6, "minCut6", s, expected);
    }

    @Test
    public void test2() {
        test("aba", 0);
        test("aab", 1);
        test("aababa", 1);
        test("aabbbaccbaaba", 4);
        test("aabbbaccbaabababac", 5);
        test("ababababababababababababcbabababababababababababa", 0);
        test("aabbaa", 0);
        test("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaabbaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaa", 1);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
