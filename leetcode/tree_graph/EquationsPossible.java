import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC990: https://leetcode.com/problems/satisfiability-of-equality-equations/
//
// Given an array equations of strings that represent relationships between variables, each string
// equations[i] has length 4 and takes one of two different forms: "a==b" or "a!=b".  Here, a and b
// are lowercase letters (not necessarily different) that represent one-letter variable names.
// Return true if and only if it is possible to assign integers to variable names so as to satisfy
// all the given equations.
//
// Note:
// 1 <= equations.length <= 500
// equations[i].length == 4
// equations[i][0] and equations[i][3] are lowercase letters
// equations[i][1] is either '=' or '!'
// equations[i][2] is '='
public class EquationsPossible {
    // Union Find + Hash Table
    // time complexity: O(N), space complexity: O(1)
    // 3 ms(19.56%), 38.9 MB(27.42%) for 181 tests
    public boolean equationsPossible(String[] equations) {
        Map<Character, Set<Character>> inequalities = new HashMap<>();
        int[] id = new int[26];
        Arrays.fill(id, -1);
        for (String e : equations) {
            char a = e.charAt(0);
            char b = e.charAt(3);
            boolean eq = (e.charAt(1) == '=');
            if (a == b) {
                if (eq) { continue; }
                return false;
            }
            if (eq) {
                union(id, a - 'a', b - 'a');
            } else {
                inequalities.computeIfAbsent((char)Math.min(a, b), x -> new HashSet<>())
                            .add((char)Math.max(a, b));
            }
        }
        for (char a : inequalities.keySet()) {
            int parent = root(id, a - 'a');
            for (char b : inequalities.get(a)) {
                if (parent == root(id, b - 'a')) { return false; }
            }
        }
        return true;
    }

    private boolean union(int[] id, int x, int y) {
        int px = root(id, x);
        int py = root(id, y);
        if (px == py) { return false; }

        if (id[py] < id[px]) {
            int tmp = px;
            px = py;
            py = tmp;
        }
        id[px] += id[py];
        id[py] = px;
        return true;
    }

    private int root(int[] id, int x) {
        for (; id[x] >= 0; x = id[x]) {}
        return x;
    }

    // DFS + Stack
    // time complexity: O(N), space complexity: O(1)
    // 2 ms(36.38%), 38.8 MB(27.42%) for 181 tests
    public boolean equationsPossible2(String[] equations) {
        List<Integer>[] graph = new ArrayList[26];
        for (int i = 0; i < 26; i++) {
            graph[i] = new ArrayList<>();
        }
        for (String e : equations) {
            if (e.charAt(1) == '=') {
                int x = e.charAt(0) - 'a';
                int y = e.charAt(3) - 'a';
                graph[x].add(y);
                graph[y].add(x);
            }
        }
        int[] color = new int[26];
        for (int cur = 0, c = 0; cur < 26; cur++) {
            if (color[cur] != 0) { continue; }

            c++;
            Stack<Integer> stack = new Stack<>();
            for (stack.push(cur); !stack.isEmpty(); ) {
                int node = stack.pop();
                for (int nei : graph[node]) {
                    if (color[nei] == 0) {
                        color[nei] = c;
                        stack.push(nei);
                    }
                }
            }
        }
        for (String e : equations) {
            if (e.charAt(1) == '!') {
                int x = e.charAt(0) - 'a';
                int y = e.charAt(3) - 'a';
                if (x == y || color[x] != 0 && color[x] == color[y]) { return false; }
            }
        }
        return true;
    }

    private void test(String[] equations, boolean expected) {
        assertEquals(expected, equationsPossible(equations));
        assertEquals(expected, equationsPossible2(equations));
    }

    @Test public void test() {
        test(new String[] {"a==b", "b!=a"}, false);
        test(new String[] {"b==a", "a==b"}, true);
        test(new String[] {"a==b", "b==c", "a==c"}, true);
        test(new String[] {"a==b", "b!=c", "c==a"}, false);
        test(new String[] {"c==c", "b==d", "x!=z"}, true);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
