import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC399: https://leetcode.com/problems/evaluate-division/
//
// Equations are given in the format A / B = k, where A and B are variables
// represented as strings, and k is a real number (floating point number). Given
// some queries, return the answers. If the answer does not exist, return -1.0.
//
// Example:
// Given a / b = 2.0, b / c = 3.0.
// queries are: a / c = ?, b / a = ?, a / e = ?, a / a = ?, x / x = ? .
// return [6.0, 0.5, -1.0, 1.0, -1.0 ].
// The input is: vector<pair<string, string>> equations, vector<double>& values,
// vector<pair<string, string>> queries , where equations.size() == values.size(),
// and the values are positive. This represents the equations. Return vector<double>.
// The input is always valid. You may assume that evaluating the queries will
// result in no division by zero and there is no contradiction.
public class EvaluateDivision {
    // Solution of Choice
    // Hash Table + Recursion + DFS
    // beats 77.38%(3 ms for 11 tests)
    public double[] calcEquation(String[][] equations, double[] values,
                                 String[][] queries) {
        Map<String, Map<String, Double> > relations = new HashMap<>();
        for (int i = 0; i < equations.length; i++) {
            String[] equation = equations[i];
            double value = values[i];
            save(relations, equation[0], equation[1], value);
            save(relations, equation[1], equation[0], 1.0 / value);
        }
        double[] res = new double[queries.length];
        for (int i = 0; i < queries.length; i++) {
            String[] query = queries[i];
            res[i] = calc(relations, query[0], query[1]);
        }
        return res;
    }

    private void save(Map<String, Map<String, Double> > relations,
                      String x, String y, double value) {
        Map<String, Double> map = relations.get(x);
        if (map == null) {
            relations.put(x, map = new HashMap<>());
        }
        map.put(y, value);
    }

    private double calc(Map<String, Map<String, Double> > relations,
                        String x, String y) {
        if (!relations.containsKey(x)) return -1.0;

        return x.equals(y) ? 1.0 : calc(relations, x, y, new HashSet<>());
    }

    private double calc(Map<String, Map<String, Double> > relations,
                        String x, String y, Set<String> visited) {
        visited.add(x);
        Map<String, Double> values = relations.get(x);
        Double val = values.get(y);
        if (val != null) return val;

        for (String var : values.keySet()) {
            if (!visited.contains(var)) {
                double res = calc(relations, var, y, visited);
                if (res >= 0) return values.get(var) * res;
            }
        }
        return -1.0;
    }

    // Hash Table
    // beats 14.77%(6 ms for 11 tests)
    public double[] calcEquation2(String[][] equations, double[] values,
                                  String[][] queries) {
        Map<String, Map<String, Double> > relations = new HashMap<>();
        for (int i = 0; i < equations.length; i++) {
            String[] equation = equations[i];
            double value = values[i];
            save(relations, equation[0], equation[1], value);
            save(relations, equation[1], equation[0], 1.0 / value);
        }
        for (String z : relations.keySet()) {
            Map<String, Double> map1 = relations.get(z);
            Set<String> relation = map1.keySet();
            for (String x : relation) {
                Map<String, Double> map2 = relations.get(x);
                for (String y : relation) {
                    save(relations, x, y, map1.get(y) * map2.get(z));
                }
            }
        }
        double[] res = new double[queries.length];
        for (int i = 0; i < queries.length; i++) {
            String[] query = queries[i];
            Map<String, Double> map = relations.get(query[0]);
            res[i] = (map == null) ? -1.0 : map.getOrDefault(query[1], -1.0);
        }
        return res;
    }

    // Hash Table + Queue + BFS
    // beats 41.57%(4 ms for 11 tests)
    public double[] calcEquation3(String[][] equations, double[] values,
                                  String[][] queries) {
        Map<String, Map<String, Double> > relations = new HashMap<>();
        for (int i = 0; i < equations.length; i++) {
            String[] equation = equations[i];
            double value = values[i];
            save(relations, equation[0], equation[1], value);
            save(relations, equation[1], equation[0], 1.0 / value);
        }
        double[] res = new double[queries.length];
        for (int i = 0; i < queries.length; i++) {
            String[] query = queries[i];
            res[i] = calc3(relations, query[0], query[1]);
        }
        return res;
    }

    private double calc3(Map<String, Map<String, Double> > relations,
                         String x, String y) {
        Map<String, Double> values = relations.get(x);
        if (values == null) return -1.0;

        Queue<Value> queue = new LinkedList<>();
        queue.offer(new Value(x, 1));
        Set<String> visited = new HashSet<>();
        visited.add(x);
        while (!queue.isEmpty()) {
            Value v = queue.poll();
            Map<String, Double> map = relations.get(v.x);
            Double val = map.get(y);
            if (val != null) return v.val * val;

            for (String key : map.keySet()) {
                if (!visited.contains(key)) {
                    visited.add(key);
                    double newVal = v.val * map.get(key);
                    queue.offer(new Value(key, newVal));
                    values.put(key, newVal); // improve performance or not?
                }
            }
        }
        return -1.0;
    }

    private static class Value {
        String x;
        double val;
        Value(String x, double val) {
            this.x = x;
            this.val = val;
        }
    }

    // Hash Table
    // beats 77.38%(3 ms for 11 tests)
    public double[] calcEquation4(String[][] equations, double[] values,
                                  String[][] queries) {
        Map<String, Integer> nodes = new HashMap<>();
        int n = 0;
        for (String[] equation : equations) { // indexing nodes
            String src = equation[0];
            String dest = equation[1];
            if (!nodes.containsKey(src)) {
                nodes.put(src, n++);
            }
            if (!nodes.containsKey(dest)) {
                nodes.put(dest, n++);
            }
        }
        double[][] relations = new double[n][n];
        for (int i = equations.length - 1; i >= 0; i--) {
            int src = nodes.get(equations[i][0]);
            int dest = nodes.get(equations[i][1]);
            relations[src][src] = relations[dest][dest] = 1.0;
            relations[src][dest] = values[i];
            relations[dest][src] = 1.0 / values[i];
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    if (relations[i][j] != 0 && relations[j][k] != 0) {
                        relations[i][k] = relations[i][j] * relations[j][k];
                    }
                }
            }
        }
        double res[] = new double[queries.length];
        for (int i = queries.length - 1; i >= 0; i--) {
            res[i] = -1.0;
            String[] query = queries[i];
            Integer src = nodes.get(query[0]);
            Integer dest = nodes.get(query[1]);
            if (src != null && dest != null) {
                double val = relations[src][dest];
                if (val != 0) {
                    res[i] = val;
                }
            }
        }
        return res;
    }

    // Union Find + Hash Table
    // https://discuss.leetcode.com/topic/58577/0ms-c-union-find-solution-easy-to-understand/2
    public double[] calcEquation5(String[][] equations, double[] values,
                                  String[][] queries) {
        Map<String, Integer> sets = new HashMap<>();
        Map<String, Double> vals = new HashMap<>();
        List<List<String> > list = new ArrayList<>();
        for (int i = 0, n = 0; i < equations.length; i++) {
            String a = equations[i][0];
            Integer setA = sets.get(a);
            String b = equations[i][1];
            Integer setB = sets.get(b);
            if (setA == null && setB == null) {
                sets.put(a, n);
                sets.put(b, n++);
                vals.put(a, values[i]);
                vals.put(b, 1.0);
                list.add(new LinkedList<>(Arrays.asList(a, b)));
            } else if (setB == null) {
                sets.put(b, setA);
                vals.put(b, vals.get(a) / values[i]);
                list.get(setA).add(b);
            } else if (setA == null) {
                sets.put(a, setB);
                vals.put(a, vals.get(b) * values[i]);
                list.get(setB).add(a);
            } else if (!setA.equals(setB)) {
                double factor = vals.get(a) / values[i] / vals.get(b);
                for (String x : list.get(setB)) {
                    sets.put(x, setA);
                    vals.put(x, vals.get(x) * factor);
                }
            }
        }
        double[] res = new double[queries.length];
        for (int i = 0; i < queries.length; i++) {
            Integer setA = sets.get(queries[i][0]);
            Integer setB = sets.get(queries[i][1]);
            res[i] = (setA != null && setA.equals(setB))
                     ? vals.get(queries[i][0]) / vals.get(queries[i][1]) : -1;
        }
        return res;
    }

    @FunctionalInterface
    interface Function<A, B, C, D> {
        public D apply(A a, B b, C c);
    }

    void test(Function<String[][], double[], String[][], double[]> calc,
              String[][] equations, double[] values, String[][] queries,
              double ... expected) {
        double[] res = calc.apply(equations, values, queries);
        assertArrayEquals(expected, res, 1E-8);
    }

    void test(String[][] equations, double[] values, String[][] queries,
              double ... expected) {
        EvaluateDivision e = new EvaluateDivision();
        test(e::calcEquation, equations, values, queries, expected);
        test(e::calcEquation2, equations, values, queries, expected);
        test(e::calcEquation3, equations, values, queries, expected);
        test(e::calcEquation4, equations, values, queries, expected);
        test(e::calcEquation5, equations, values, queries, expected);
    }

    @Test
    public void test1() {
        test(new String[][] {{"a", "b"}, {"b", "c"}}, new double[] {2.0, 3.0},
             new String[][] {{"a", "c"}, {"b", "a"}, {"a", "e"}, {"a", "a"},
                             {"x", "x"}},
             6.0, 0.5, -1.0, 1.0, -1.0);
        test(new String[][] {{"x1","x2"},{"x2","x3"},{"x3","x4"},{"x4","x5"}},
             new double[] {3.0,4.0,5.0,6.0},
             new String[][] {{"x1","x5"}, {"x5","x2"}, {"x2","x4"}, {"x2","x2"},
                             {"x2","x9"}, {"x9","x9"}},
             360, 0.00833333, 20, 1, -1, -1);
        test(new String[][] {{"a", "b"}, {"e", "f"}, {"b", "e"}},
             new double[] {3.4, 1.4, 2.3},
             new String[][] {{"b", "a"}, {"a", "f"}, {"f", "f"}, {"e", "e"},
                             {"c", "c"}, {"a", "c"}, {"f", "e"}},
             0.294117647, 10.948, 1, 1, -1, -1, 0.714285714);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("EvaluateDivision");
    }
}
