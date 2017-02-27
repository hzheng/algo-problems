import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC269: https://leetcode.com/problems/alien-dictionary
//
// There is a new alien language which uses the latin alphabet. However, the order
// among letters are unknown to you. You receive a list of non-empty words from
// the dictionary, where words are sorted lexicographically by the rules of this
// new language. Derive the order of letters in this language.
// Note:
// You may assume all letters are in lowercase.
// You may assume that if a is a prefix of b, then a must appear before b in the
// given dictionary.
// If the order is invalid, return an empty string.
// There may be multiple valid order of letters, return any one of them is fine.
public class AlienDictionary {
    private static final int N = 26;

    // Hash Table(array) + BFS + Queue / Topological Sort
    // beats 67.64%(9 ms for 117 tests)
    public String alienOrder(String[] words) {
        @SuppressWarnings("unchecked")
        Set<Character>[] orders = new Set[N];
        int charCount = 0;
        for (String word : words) {
            for (char c : word.toCharArray()) {
                if (orders[c - 'a'] == null) {
                    orders[c - 'a'] = new HashSet<>();
                    charCount++;
                }
            }
        }
        int[] bigger = new int[N];
        for (int i = 1; i < words.length; i++) {
            String prev = words[i - 1];
            String cur = words[i];
            for (int j = 0; j < prev.length() && j < cur.length(); j++) {
                char c1 = prev.charAt(j);
                char c2 = cur.charAt(j);
                if (c1 != c2) {
                    if (orders[c1 - 'a'].add(c2)) {
                        bigger[c2 - 'a']++;
                    }
                    break;
                }
            }
        }
        Queue<Character> queue = new LinkedList<>();
        for (int i = 0; i < N; i++) {
            char c = (char)('a' + i);
            if (bigger[i] == 0 && orders[i] != null) {
                queue.offer(c);
            }
        }
        char[] buf = new char[charCount];
        for (int i = 0; !queue.isEmpty(); charCount--) {
            char c = queue.poll();
            buf[i++] = c;
            for (char smaller : orders[c - 'a']) {
                if (--bigger[smaller - 'a'] == 0) {
                    queue.offer(smaller);
                }
            }
        }
        return charCount == 0 ? new String(buf) : "";
    }

    // Hash Table(array) + DFS + Recursion + 3 Colors
    // beats 97.97%(9 ms for 117 tests)
    public String alienOrder2(String[] words) {
        boolean[][] adjacencyMatrix = new boolean[N][N];
        int[] visited = new int[N];
        buildGraph(words, adjacencyMatrix, visited);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < N; i++) {
            if (visited[i] == 0 && !dfs(i, adjacencyMatrix, visited, sb)) return "";
        }
        return sb.toString();
    }

    private boolean dfs(int cur, boolean[][] matrix, int[] visited, StringBuilder sb) {
        visited[cur] = 1; // 1 means visiting
        for (int i = 0; i < N; i++) {
            if (!matrix[cur][i]) continue;

            if ((visited[i] == 1) // cycle detected
                || (visited[i] == 0) && !dfs(i, matrix, visited, sb)) return false;
        }
        visited[cur] = 2; // 2 means visited
        sb.append((char)(cur + 'a'));
        return true;
    }

    private void buildGraph(String[] words, boolean[][] matrix, int[] visited) {
        Arrays.fill(visited, -1); // -1 means not even exist
        for (int i = 0; i < words.length; i++) {
            String cur = words[i];
            for (char c : cur.toCharArray()) {
                visited[c - 'a'] = 0; // 0 means not visited yet
            }
            if (i > 0) {
                String prev = words[i - 1];
                for (int j = 0; j < prev.length() && j < cur.length(); j++) {
                    char c1 = prev.charAt(j);
                    char c2 = cur.charAt(j);
                    if (c1 != c2) {
                        matrix[c2 - 'a'][c1 - 'a'] = true;
                        break;
                    }
                }
            }
        }
    }

    void test(String[] words, String expected) {
        assertEquals(expected, alienOrder(words));
        assertEquals(expected, alienOrder2(words));
    }

    @Test
    public void test() {
        test(new String[] {"wrt", "wrf", "er", "ett", "rftt"}, "wertf");
        test(new String[] {"z", "x"}, "zx");
        test(new String[] {"z", "x", "z"}, "");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("AlienDictionary");
    }
}
