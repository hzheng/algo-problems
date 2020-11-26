import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC736: https://leetcode.com/problems/parse-lisp-expression/
//
// You are given a string expression representing a Lisp-like expression to return the integer value
// of. The syntax for these expressions is given as follows.
// An expression is either an integer, a let-expression, an add-expression, a mult-expression, or an
// assigned variable. Expressions always evaluate to a single integer.
// A let-expression takes the form (let v1 e1 v2 e2 ... vn en expr), where let is always the string
// "let", then there are 1 or more pairs of alternating variables and expressions, meaning that the
// first variable v1 is assigned the value of the expression e1, the second variable v2 is assigned
// the value of the expression e2, and so on sequentially; and then the value of this let-expression
// is the value of the expression expr.
// An add-expression takes the form (add e1 e2) where add is always the string "add", there are
// always two expressions e1, e2, and this expression evaluates to the addition of the evaluation of
// e1 and the evaluation of e2.
// A mult-expression takes the form (mult e1 e2) where mult is always the string "mult", there are
// always two expressions e1, e2, and this expression evaluates to the multiplication of the
// evaluation of e1 and the evaluation of e2.
// For the purposes of this question, we will use a smaller subset of variable names. A variable
// starts with a lowercase letter, then zero or more lowercase letters or digits. Additionally for
// your convenience, the names "add", "let", or "mult" are protected and will never be used as
// variable names.
// Finally, there is the concept of scope. When an expression of a variable name is evaluated,
// within the context of that evaluation, the innermost scope (in terms of parentheses) is checked
// first for the value of that variable, and then outer scopes are checked sequentially. It is
// guaranteed that every expression is legal. Please see the examples for more details on scope.
//
// Note:
// The given string expression is well formatted: There are no leading or trailing spaces, there is
// only a single space separating different components of the string, and no space between adjacent
// parentheses. The expression is guaranteed to be legal and evaluate to an integer.
// The length of expression is at most 2000. (It is also non-empty)
// The answer and all intermediate calculations of that answer fit in a 32-bit integer.
public class ParseLispExpression {
    private static final String LET = "(let";
    private static final String ADD = "(add";
    private static final String MULT = "(mult";

    // Recursion + Hash Table + Stack + Queue
    // time complexity: O(N), space complexity: O(N)
    // 4 ms(66.13%), 39.2 MB(11.94%) for 64 tests
    public int evaluate(String expression) {
        Queue<String> tokens = new LinkedList<>();
        for (String token : expression.split(" ")) {
            tokens.offer(token);
        }
        return evaluate(tokens, new HashMap<>());
    }

    private int evaluate(Queue<String> tokens, Map<String, Stack<Integer>> variables) {
        String firstToken = tokens.poll();
        switch (firstToken) {
        case LET:
            for (Map<String, Integer> newVars = new HashMap<>(); ; ) {
                String var = tokens.peek();
                if (isVariable(var)) {
                    tokens.poll();
                    int val = evaluate(tokens, variables);
                    variables.computeIfAbsent(var, x -> new Stack<>()).push(val);
                    newVars.put(var, newVars.getOrDefault(var, 0) + 1);
                } else { // last expression
                    int res = evaluate(tokens, variables);
                    for (String x : newVars.keySet()) {
                        Stack<Integer> v = variables.get(x);
                        for (int i = newVars.get(x); i > 0; i--) {
                            v.pop();
                        }
                    }
                    return res;
                }
            }
        case ADD:
        case MULT:
            int a = evaluate(tokens, variables);
            int b = evaluate(tokens, variables);
            return firstToken.equals(ADD) ? a + b : a * b;
        default: // number or a variable expression
            int rightParan = firstToken.indexOf(")");
            if (rightParan > 0) {
                firstToken = firstToken.substring(0, rightParan);
            }
            return isNumber(firstToken) ? Integer.parseInt(firstToken) :
                   variables.get(firstToken).peek();
        }
    }

    private boolean isNumber(String s) {
        char first = s.charAt(0);
        return first == '-' || Character.isDigit(first);
    }

    private boolean isVariable(String s) {
        return Character.isLowerCase(s.charAt(0)) && !s.endsWith(")");
    }

    // Recursion + Hash Table
    // time complexity: O(N^2), space complexity: O(N^2)
    // 4 ms(66.13%), 39.4 MB(11.94%) for 64 tests
    public int evaluate2(String expression) {
        return eval(expression, new HashMap<>());
    }

    private int eval(String expression, Map<String, Integer> context) {
        if (expression.charAt(0) != '(') {
            return isNumber(expression) ? Integer.parseInt(expression) : context.get(expression);
        }

        Map<String, Integer> variables = new HashMap<>(context); // clone outer variables
        List<String> subExpr = split(expression.substring(expression.charAt(1) == 'm' ? 6 : 5,
                                                          expression.length() - 1));
        if (expression.startsWith(LET)) {
            for (int i = 0; i < subExpr.size() - 2; i += 2) {
                variables.put(subExpr.get(i), eval(subExpr.get(i + 1), variables));
            }
            return eval(subExpr.get(subExpr.size() - 1), variables);
        }

        int a = eval(subExpr.get(0), variables);
        int b = eval(subExpr.get(1), variables);
        return expression.startsWith(ADD) ? a + b : a * b;
    }

    private List<String> split(String str) {
        List<String> res = new ArrayList<>();
        int parentheses = 0;
        StringBuilder sb = new StringBuilder();
        for (char c : str.toCharArray()) {
            if (c == '(') {
                parentheses++;
            } else if (c == ')') {
                parentheses--;
            }
            if (parentheses == 0 && c == ' ') {
                res.add(sb.toString());
                sb = new StringBuilder();
            } else {
                sb.append(c);
            }
        }
        if (sb.length() > 0) {
            res.add(sb.toString());
        }
        return res;
    }

    private void test(String expression, int expected) {
        assertEquals(expected, evaluate(expression));
        assertEquals(expected, evaluate2(expression));
    }

    @Test public void test() {
        test("(add 1 2)", 3);
        test("(mult 3 (add 2 3))", 15);
        test("(let x 2 (mult x 5))", 10);
        test("(let x 2 (mult x (let x 3 y 4 (add x y))))", 14);
        test("(let x 3 x 2 x)", 2);
        test("(let x 1 y 2 x (add x y) (add x y))", 5);
        test("(let x 2 (add (let x 3 (let x 4 x)) x))", 6);
        test("(let a1 3 b2 (add a1 1) b2)", 4);
        test("(let x 7 -12)", -12);
        test("(let x 12 x 3 x 2 y 5 (add x y))", 7);
        test("(let x 7 y (let x 12 x 3 x 2 y 5 (add x y)) x)", 7);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
