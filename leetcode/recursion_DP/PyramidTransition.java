import java.util.*;
import java.util.stream.Collectors;

import org.junit.Test;

import static org.junit.Assert.*;

// LC756: https://leetcode.com/problems/pyramid-transition-matrix/
//
// We are stacking blocks to form a pyramid. Each block has a color which is a one letter string.
// We are allowed to place any color block C on top of two adjacent blocks of colors A and B, if and
// only if ABC is an allowed triple. We start with a bottom row of bottom, represented as a single
// string. We also start with a list of allowed triples allowed. Each allowed triple is represented
// as a string of length 3. Return true if we can build the pyramid all the way to the top,
// otherwise false.
//
// Constraints:
// bottom will be a string with length in range [2, 8].
// allowed will have length in range [0, 200].
// Letters in all strings will be chosen from the set {'A', 'B', 'C', 'D', 'E', 'F', 'G'}.
public class PyramidTransition {
    // DFS + Recursion + Backtracking
    // time complexity: O(A^N), space complexity: O(N^2)
    // 6 ms(47.01%), 39.7 MB(5.22%) for 65 tests
    public boolean pyramidTransition(String bottom, List<String> allowed) {
        Map<String, Set<Character>> map = new HashMap<>();
        for (String a : allowed) {
            map.computeIfAbsent(a.substring(0, 2), x -> new HashSet<>()).add(a.charAt(2));
        }
        return processRow(bottom, map);
    }

    private boolean processRow(String bottom, Map<String, Set<Character>> map) {
        if (bottom.length() == 1) { return true; }

        List<String> nextBottoms = new ArrayList<>();
        processColumn(nextBottoms, bottom, 0, new StringBuilder(), map);
        for (String next : nextBottoms) {
            if (processRow(next, map)) { return true; }
        }
        return false;
    }

    private void processColumn(List<String> nextBottoms, String bottom, int cur, StringBuilder sb,
                               Map<String, Set<Character>> map) {
        if (cur == bottom.length() - 1) {
            nextBottoms.add(sb.toString());
            return;
        }
        for (char c : map.getOrDefault(bottom.substring(cur, cur + 2), Collections.emptySet())) {
            sb.append(c);
            processColumn(nextBottoms, bottom, cur + 1, sb, map);
            sb.setLength(sb.length() - 1); // backtracking
        }
    }

    // DFS + Recursion + Backtracking
    // time complexity: O(A^N), space complexity: O(N^2)
    // 2 ms(92.91%), 39.3 MB(5.22%) for 65 tests
    public boolean pyramidTransition2(String bottom, List<String> allowed) {
        Map<String, Set<Character>> map = new HashMap<>();
        for (String a : allowed) {
            map.computeIfAbsent(a.substring(0, 2), x -> new HashSet<>()).add(a.charAt(2));
        }
        return dfs(bottom, new StringBuilder(), 0, map);
    }

    private boolean dfs(String bottom, StringBuilder next, int cur,
                        Map<String, Set<Character>> map) {
        if (bottom.length() == 1) { return true; }

        if (cur == bottom.length() - 1) { // start next level
            return dfs(next.toString(), new StringBuilder(), 0, map);
        }

        for (char c : map.getOrDefault(bottom.substring(cur, cur + 2), Collections.emptySet())) {
            if (dfs(bottom, next.append(c), cur + 1, map)) { return true; }

            next.setLength(next.length() - 1); // backtracking
        }
        return false;
    }

    // DFS + Recursion + Bit Manipulation
    // time complexity: O(A^N), space complexity: O(N^2)
    // 0 ms(100.00%), 37.4 MB(5.28%) for 65 tests
    public boolean pyramidTransition3(String bottom, List<String> allowed) {
        int[][] map = new int[7][7];
        for (String a : allowed) {
            map[a.charAt(0) - 'A'][a.charAt(1) - 'A'] |= 1 << (a.charAt(2) - 'A');
        }
        int n = bottom.length();
        int[][] grid = new int[n][n];
        int i = 0;
        for (char c : bottom.toCharArray()) {
            grid[n - 1][i++] = c - 'A';
        }
        return dfs(grid, n - 1, 0, map);
    }

    public boolean dfs(int[][] grid, int row, int col, int[][] map) {
        if (row == 1 && col == 1) { return true; }

        if (col == row) { // last column (note that row is decreasing, and column is increasing)
            return dfs(grid, row - 1, 0, map);
        }

        int mask = map[grid[row][col]][grid[row][col + 1]];
        for (int i = 0; i < 7; i++) {
            if (((mask >> i) & 1) != 0) {
                grid[row - 1][col] = i;
                if (dfs(grid, row, col + 1, map)) { return true; }
            }
        }
        return false;
    }

    private void test(String bottom, String[] allowed, boolean expected) {
        List<String> allowedList = Arrays.asList(allowed);
        assertEquals(expected, pyramidTransition(bottom, allowedList));
        assertEquals(expected, pyramidTransition2(bottom, allowedList));
        assertEquals(expected, pyramidTransition3(bottom, allowedList));
    }

    @Test public void test() {
        test("BCD", new String[] {"BCG", "CDE", "GEA", "FFF"}, true);
        test("AABA", new String[] {"AAA", "AAB", "ABA", "ABB", "BAC"}, false);
        test("AAAA", new String[] {"AAB", "AAC", "BCD", "BBE", "DEF"}, false);
        test("ABCD", new String[] {"BCE", "BCF", "ABA", "CDA", "AEG", "FAG", "GGG"}, false);
        test("CCC",
             new String[] {"CBB", "ACB", "ABD", "CDB", "BDC", "CBC", "DBA", "DBB", "CAB", "BCB",
                           "BCC", "BAA", "CCD", "BDD", "DDD", "CCA", "CAA", "CCC", "CCB"}, true);
        test("BDBBAA",
             new String[] {"ACB", "ACA", "AAA", "ACD", "BCD", "BAA", "BCB", "BCC", "BAD", "BAB",
                           "BAC", "CAB", "CCD", "CAA", "CCA", "CCC", "CAD", "DAD", "DAA", "DAC",
                           "DCD", "DCC", "DCA", "DDD", "BDD", "ABB", "ABC", "ABD", "BDB", "BBD",
                           "BBC", "BBA", "ADD", "ADC", "ADA", "DDC", "DDB", "DDA", "CDA", "CDD",
                           "CBA", "CDB", "CBD", "DBA", "DBC", "DBD", "BDA"}, true);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
