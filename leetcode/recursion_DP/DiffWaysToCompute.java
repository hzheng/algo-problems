import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/different-ways-to-add-parentheses/
//
// Given a string of numbers and operators, return all possible results
// from computing all the different possible ways to group numbers and
// operators. The valid operators are +, - and *.
public class DiffWaysToCompute {
    // beats 12.15%(10 ms)
    public List<Integer> diffWaysToCompute(String input) {
        int len = input.length();
        char[] cs = input.toCharArray();
        int operatorCount = 0;
        for (char c : cs) {
            switch (c) {
            case '+': case '-': case '*':
                operatorCount++;
            }
        }
        char[] operators = new char[operatorCount];
        int[] operands = new int[operatorCount + 1];
        for (int i = 0, j = 0; i <= operatorCount; i++) {
            int n = 0;
            while (j < len && Character.isDigit(cs[j])) {
                n *= 10;
                n += cs[j++] - '0';
            }
            operands[i] = n;
            if (j < len) {
                operators[i] = cs[j++];
            }
        }
        if (operatorCount == 0) return Arrays.asList(operands[0]);

        List<Integer> res = new ArrayList<>();
        groupWays(operands, operators, 0, operatorCount + 1, operatorCount,
                  new int[operatorCount * 2 + 1], res);
        return res;
    }

    private void groupWays(int[] operands, char[] operators, int index,
                           int operandLeft, int operatorLeft,
                           int[] cur, List<Integer> res) {
        if (operatorLeft == 0 && operandLeft == 0) {
            res.add(evaluate(cur, operands, operators));
            return;
        }

        if (operandLeft > 0) {
            cur[index] = operands.length - operandLeft;
            groupWays(operands, operators, index + 1, operandLeft - 1,
                      operatorLeft, cur, res);
        }
        if (operatorLeft > operandLeft) {
            cur[index] = -1; // operator
            groupWays(operands, operators, index + 1, operandLeft,
                      operatorLeft - 1, cur, res);
        }
    }

    private int evaluate(int[] polish, int[] operands, char[] operators) {
        Stack<int[]> stack = new Stack<>();
        for (int index : polish) {
            if (index >= 0) {
                stack.push(new int[] {operands[index], index, index});
            } else {
                int[] operator2 = stack.pop();
                int[] operator1 = stack.pop();
                int res = 0;
                switch (operators[operator1[2]]) {
                case '+':
                    res = operator1[0] + operator2[0];
                    break;
                case '-':
                    res = operator1[0] - operator2[0];
                    break;
                case '*':
                    res = operator1[0] * operator2[0];
                    break;
                }
                stack.push(new int[] {res, operator1[1], operator2[2]});
            }
        }
        return stack.peek()[0];
    }

    // beats 20.77%(9 ms)
    public List<Integer> diffWaysToCompute2(String input) {
        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c >= '0' && c <= '9') continue;

            List<Integer> leftRes = diffWaysToCompute(input.substring(0, i));
            List<Integer> rightRes = diffWaysToCompute(input.substring(i + 1));
            for (Integer m : leftRes) {
                for (Integer n : rightRes) {
                    if (c == '+') {
                        res.add(m + n);
                    } else if (c == '-') {
                        res.add(m - n);
                    } else if (c == '*') {
                        res.add(m * n);
                    }
                }
            }
        }
        if (res.size() == 0) {
            res.add(Integer.parseInt(input));
        }
        return res;
    }

    // beats 20.77%(9 ms)
    public List<Integer> diffWaysToCompute3(String input) {
        int len = input.length();
        char[] cs = input.toCharArray();
        List<Object> expression = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            int n = 0;
            while (i < len && Character.isDigit(cs[i])) {
                n *= 10;
                n += cs[i++] - '0';
            }
            expression.add(n);
            if (i < len) {
                expression.add(cs[i]);
            }
        }
        return diffWaysToCompute3(expression, 0, expression.size() - 1);
    }

    private List<Integer> diffWaysToCompute3(List<Object> expression, int start, int end) {
        if (start == end) return Arrays.asList((Integer)expression.get(start));

        List<Integer> res = new ArrayList<>();
        for (int i = start; i <= end; i++) {
            Object oper = expression.get(i);
            if (!(oper instanceof Character)) continue;

            char c = ((Character)oper).charValue();
            List<Integer> leftRes = diffWaysToCompute3(expression, start, i - 1);
            List<Integer> rightRes = diffWaysToCompute3(expression, i + 1, end);
            for (Integer m : leftRes) {
                for (Integer n : rightRes) {
                    if (c == '+') {
                        res.add(m + n);
                    } else if (c == '-') {
                        res.add(m - n);
                    } else if (c == '*') {
                        res.add(m * n);
                    }
                }
            }
        }
        return res;
    }

    // beats 84.68%(4 ms)
    public List<Integer> diffWaysToCompute4(String input) {
        return diffWaysToCompute4(input, new HashMap<>());
    }

    private List<Integer> diffWaysToCompute4(String s,
                                             Map<String, List<Integer>> cache) {
        if (cache.containsKey(s)) return cache.get(s);

        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= '0' && c <= '9') continue;

            List<Integer> leftRes = diffWaysToCompute4(s.substring(0, i), cache);
            List<Integer> rightRes = diffWaysToCompute4(s.substring(i + 1), cache);
            for (Integer m : leftRes) {
                for (Integer n : rightRes) {
                    if (c == '+') {
                        res.add(m + n);
                    } else if (c == '-') {
                        res.add(m - n);
                    } else if (c == '*') {
                        res.add(m * n);
                    }
                }
            }
        }
        if (res.size() == 0) {
            res.add(Integer.parseInt(s));
        }
        cache.put(s, res);
        return res;
    }

    void test(Function<String, List<Integer> > compute,
              String s, int ... expected) {
        List<Integer> res = compute.apply(s);
        Collections.sort(res);
        assertArrayEquals(expected, res.stream().mapToInt(i->i).toArray());
    }

    void test(String s, int ... expected) {
        DiffWaysToCompute d = new DiffWaysToCompute();
        test(d::diffWaysToCompute, s, expected);
        test(d::diffWaysToCompute2, s, expected);
        test(d::diffWaysToCompute3, s, expected);
        test(d::diffWaysToCompute4, s, expected);
    }

    @Test
    public void test1() {
        test("2-1-1", 0, 2);
        test("2*3-4*5", -34, -14, -10, -10, 10);
        test("2*31-4*5", 22, 42, 270, 270, 290);
        test("1-2+3*4-5*6+7", -312,-273,-234,-194,-184,-182,-137,-122,-119,
             -116,-116,-104,-104,-101,-100,-96,-92,-84,-82,-81,-78,-77,-72,-72,
             -66,-60,-60,-58,-57,-56,-54,-54,-53,-52,-50,-50,-50,-46,-46,-45,
             -42,-41,-41,-40,-40,-39,-38,-36,-36,-29,-26,-26,-26,-26,-17,-15,
             -12,-12,-12,-12,-12,-12,-12,-5,-5,-4,-4,-4,0,2,2,4,10,10,10,10,10,
             10,10,14,14,18,18,19,24,24,24,24,24,24,24,24,25,26,31,31,38,38,38,
             38,39,43,43,43,46,48,48,52,52,52,52,56,66,66,70,70,76,78,78,78,
             84,90,96,98,111,124,132,138,166,182,244,306);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("DiffWaysToCompute");
    }
}
