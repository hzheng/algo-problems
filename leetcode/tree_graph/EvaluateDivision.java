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
    // recursion
    // beats N/A(4 ms)
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
        if (!relations.containsKey(x)) {
            relations.put(x, new HashMap<>());
        }
        relations.get(x).put(y, value);
    }

    private double calc(Map<String, Map<String, Double> > relations,
                        String x, String y) {
        if (!relations.containsKey(x)) return -1.0;

        if (x.equals(y)) return 1.0;

        return calc(relations, x, y, new HashSet<>());
    }

    private double calc(Map<String, Map<String, Double> > relations,
                        String x, String y, Set<String> visited) {
        visited.add(x);
        Map<String, Double> values = relations.get(x);
        if (values.containsKey(y)) return values.get(y);

        for (String var : values.keySet()) {
            if (!visited.contains(var)) {
                double res = calc(relations, var, y, visited);
                if (res >= 0) return values.get(var) * res;
            }
        }
        return -1.0;
    }

    // iteration
    // beats N/A(11 ms)
    public double[] calcEquation2(String[][] equations, double[] values,
                                  String[][] queries) {
        Map<String, Map<String, Double> > relations = new HashMap<>();
        for (int i = 0; i < equations.length; i++) {
            String[] equation = equations[i];
            double value = values[i];
            save(relations, equation[0], equation[1], value);
            save(relations, equation[1], equation[0], 1.0 / value);
        }
        for (String r : relations.keySet()) {
            Set<String> relation = relations.get(r).keySet();
            for (String x : relation) {
                for (String y : relation) {
                    save(relations, x, y,
                         relations.get(x).get(r) * relations.get(r).get(y));
                }
            }
        }
        double[] res = new double[queries.length];
        for (int i = 0; i < queries.length; i++) {
            String[] query = queries[i];
            if (!relations.containsKey(query[0])) {
                res[i] = -1.0;
            } else {
                res[i] = relations.get(query[0]).getOrDefault(query[1], -1.0);
            }
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
    }

    @Test
    public void test1() {
        test(new String[][] {{"a", "b"}, {"b", "c"}}, new double[] {2.0, 3.0},
             new String[][] {{"a", "c"}, {"b", "a"}, {"a", "e"}, {"a", "a"},
                             {"x", "x"}},
             6.0, 0.5, -1.0, 1.0, -1.0);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("EvaluateDivision");
    }
}
