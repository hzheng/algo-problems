import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC770: https://leetcode.com/problems/basic-calculator-iv/
//
// Given an expression such as expression = "e + 8 - a + 5" and an evaluation map such as {"e": 1}
// (given in terms of evalvars = ["e"] and evalints = [1]), return a list of tokens representing the
// simplified expression, such as ["-1*a","14"]
//
// An expression alternates chunks and symbols, with a space separating each chunk and symbol.
// A chunk is either an expression in parentheses, a variable, or a non-negative integer.
// A variable is a string of lowercase letters (not including digits.) Note that variables can be
// multiple letters, and note that variables never have a leading coefficient or unary operator like
// "2x" or "-x".
// The format of the output is as follows:
// For each term of free variables with non-zero coefficient, we write the free variables within a
// term in sorted order lexicographically. For example, we would never write a term like "b*a*c",
// only "a*b*c".
// Terms have degree equal to the number of free variables being multiplied, counting multiplicity.
// We write the largest degree terms of our answer first, breaking ties by lexicographic order
// ignoring the leading coefficient of the term.
// The leading coefficient of the term is placed directly to the left with an asterisk separating it
// from the variables (if they exist.)  A leading coefficient of 1 is still printed.
// An example of a well formatted answer is ["-2*a*a*a", "3*a*a*b", "3*b*b", "4*a", "5*c", "-6"]
// Terms (including constant terms) with coefficient 0 are not included.
//
// Note:
// expression will have length in range [1, 250].
// evalvars, evalints will have equal lengths in range [0, 100].
public class Calculator4 {
    // Stack + Hash Table + SortedSet
    // time complexity: O(2^N+M), space complexity: O(N+M)
    // 36 ms(7.41%), 41.6 MB(6.48%) for 158 tests
    public List<String> basicCalculatorIV(String expression, String[] evalvars, int[] evalints) {
        final String pattern = String.format("((?<=%1$s)|(?=%1$s))", "[\\+\\-\\(\\)]");
        String[] tokens = ("(" + expression + ")").replaceAll("\\s", "").split(pattern);
        Stack<Object> stack = new Stack<>();
        for (String token : tokens) {
            switch (token.charAt(0)) {
            case '(':
                stack.push(token);
                break;
            case ')':
                close(stack);
                Object top = stack.pop();
                stack.pop(); // '('
                stack.push(top);
                break;
            case '+':
            case '-':
                close(stack);
                stack.push(token);
                break;
            default: // term
                int start = 0;
                int end = token.length();
                if (token.charAt(0) == '*') {
                    start++;
                    stack.push("*");
                }
                if (token.endsWith("*")) {
                    end--;
                }
                if (start < end) {
                    stack.push(new Polynomial(token.substring(start, end)));
                }
                if (token.length() > 1 && token.endsWith("*")) {
                    stack.push("*");
                }
                break;
            }
        }
        Map<String, Integer> values = new HashMap<>();
        for (int i = evalints.length - 1; i >= 0; i--) {
            values.put(evalvars[i], evalints[i]);
        }
        return (((Polynomial)stack.peek()).evaluate(values)).toList();
    }

    private void close(Stack<Object> stack) {
        while (!stack.isEmpty()) {
            Polynomial top = (Polynomial)stack.pop();
            if ("(".equals(stack.peek())) {
                stack.push(top);
                return;
            }
            String operator = (String)stack.pop();
            Polynomial first = (Polynomial)stack.pop();
            stack.push(first.calculate(top, operator));
        }
    }

    private static class Term implements Comparable<Term> {
        private static final Term ZERO = new Term(0, 0);
        private int coefficient = 1;
        private int degree;
        private SortedMap<String, Integer> map = new TreeMap<>();

        public Term(String expression) {
            for (String token : expression.split("\\*")) {
                if (token.charAt(0) == '-' || Character.isDigit(token.charAt(0))) {
                    coefficient *= Integer.parseInt(token);
                } else {
                    map.put(token, map.getOrDefault(token, 0) + 1);
                    degree++;
                }
            }
        }

        private Term(int coefficient, int degree) {
            this.coefficient = coefficient;
            this.degree = degree;
        }

        public Term add(Term other) {
            Term sum = new Term(coefficient + other.coefficient, degree);
            sum.map = new TreeMap<>(map);
            return sum;
        }

        public Term multiply(Term other) {
            if (coefficient == 0 || other.coefficient == 0) { return ZERO; }

            Term product = new Term(coefficient * other.coefficient, degree + other.degree);
            SortedMap<String, Integer> pMap = product.map = new TreeMap<>(map);
            for (String var : other.map.keySet()) {
                pMap.put(var, pMap.getOrDefault(var, 0) + other.map.get(var));
            }
            return product;
        }

        public Term evaluate(Map<String, Integer> values) {
            Term res = new Term(coefficient, degree);
            for (String var : map.keySet()) {
                Integer val = values.get(var);
                int pow = map.get(var);
                if (val == null) {
                    res.map.put(var, pow);
                } else {
                    res.coefficient *= (int)Math.pow(val, pow);
                    res.degree -= pow;
                }
            }
            return res;
        }

        public String toString() {
            if (coefficient == 0) { return null; }

            StringBuilder sb = new StringBuilder();
            sb.append(coefficient);
            for (String var : map.keySet()) {
                for (int i = map.get(var); i > 0; i--) {
                    sb.append('*').append(var);
                }
            }
            return sb.toString();
        }

        @Override public int compareTo(Term o) {
            if (degree != o.degree) { return o.degree - degree; }

            Iterator<String> i1 = map.keySet().iterator();
            Iterator<String> i2 = o.map.keySet().iterator();
            while (i1.hasNext() && i2.hasNext()) {
                String v1 = i1.next();
                String v2 = i2.next();
                if (!v1.equals(v2)) { return v1.compareTo(v2); }

                int p1 = map.get(v1);
                int p2 = o.map.get(v2);
                if (p1 != p2) { return p2 - p1; }
            }
            return i1.hasNext() ? -1 : (i2.hasNext() ? 1 : 0);
        }
    }

    private static class Polynomial {
        private static final Polynomial NEGATIVE_1 = new Polynomial("-1");

        private final SortedSet<Term> terms = new TreeSet<>();

        public Polynomial(String expression) {
            terms.add(new Term(expression));
        }

        private Polynomial(List<Term> termList) {
            Collections.sort(termList);
            for (int n = termList.size(), i = 0; i < n; ) {
                Term term = termList.get(i);
                for (i++; i < n; i++) {
                    Term term2 = termList.get(i);
                    if (term2.compareTo(term) != 0) { break; }

                    term = term.add(term2);
                }
                terms.add(term);
            }
        }

        public Polynomial evaluate(Map<String, Integer> values) {
            List<Term> termList = new ArrayList<>();
            for (Term term : terms) {
                termList.add(term.evaluate(values));
            }
            return new Polynomial(termList);
        }

        public Polynomial calculate(Polynomial other, String operator) {
            switch (operator) {
            case "+":
                return add(other);
            case "-":
                return minus(other);
            case "*":
                return multiply(other);
            }
            throw new UnsupportedOperationException(operator);
        }

        public Polynomial add(Polynomial other) {
            List<Term> sumTerms = new ArrayList<>(terms);
            sumTerms.addAll(other.terms);
            return new Polynomial(sumTerms);
        }

        public Polynomial multiply(Polynomial other) {
            List<Term> productTerms = new ArrayList<>();
            for (Term term1 : terms) {
                for (Term term2 : other.terms) {
                    productTerms.add(term1.multiply(term2));
                }
            }
            return new Polynomial(productTerms);
        }

        public Polynomial minus(Polynomial other) {
            return add(other.multiply(NEGATIVE_1));
        }

        public List<String> toList() {
            List<String> res = new ArrayList<>();
            for (Term term : terms) {
                String s = term.toString();
                if (s != null) {
                    res.add(s);
                }
            }
            return res;
        }
    }

    // Recursion + Hash Table
    // time complexity: O(2^N+M), space complexity: O(N+M)
    // 12 ms(65.35%), 42.6 MB(6.93%) for 158 tests
    public List<String> basicCalculatorIV2(String expression, String[] evalvars, int[] evalints) {
        Map<String, Integer> values = new HashMap<>();
        for (int i = evalints.length - 1; i >= 0; i--) {
            values.put(evalvars[i], evalints[i]);
        }
        return parse(expression).evaluate(values).toList();
    }

    public Poly parse(String expr) {
        List<Poly> bucket = new ArrayList<>();
        List<Character> symbols = new ArrayList<>();
        for (int i = 0, n = expr.length(); i < n; i++) {
            if (expr.charAt(i) == '(') {
                int j = i;
                for (int bal = 0; j < n; j++) {
                    if (expr.charAt(j) == '(') {
                        bal++;
                    } else if (expr.charAt(j) == ')') {
                        bal--;
                    }
                    if (bal == 0) { break; }
                }
                bucket.add(parse(expr.substring(i + 1, j)));
                i = j;
            } else if (Character.isLetterOrDigit(expr.charAt(i))) {
                int j = i;
                for (; j < n && expr.charAt(j) != ' '; j++) {}
                bucket.add(new Poly(expr.substring(i, j)));
                i = j;
            } else if (expr.charAt(i) != ' ') {
                symbols.add(expr.charAt(i));
            }
        }
        for (int i = symbols.size() - 1; i >= 0; i--) {
            if (symbols.get(i) == '*') {
                bucket.set(i, bucket.get(i).calculate(bucket.remove(i + 1), symbols.remove(i)));
            }
        }
        if (bucket.isEmpty()) { return new Poly(); }

        Poly res = bucket.get(0);
        for (int i = 0; i < symbols.size(); i++) {
            res = res.calculate(bucket.get(i + 1), symbols.get(i));
        }
        return res;
    }

    private static class Poly {
        private final Map<List<String>, Integer> terms = new HashMap<>();

        public Poly(String expr) {
            List<String> key = new ArrayList<>();
            if (Character.isDigit(expr.charAt(0))) {
                update(key, Integer.parseInt(expr));
            } else {
                key.add(expr);
                update(key, 1);
            }
        }

        public Poly() {}

        public void update(List<String> key, int val) {
            terms.put(key, terms.getOrDefault(key, 0) + val);
        }

        public Poly add(Poly other, int sign) {
            Poly res = new Poly();
            for (List<String> term : terms.keySet()) {
                res.update(term, terms.get(term));
            }
            for (List<String> term : other.terms.keySet()) {
                res.update(term, sign * other.terms.get(term));
            }
            return res;
        }

        public Poly multiply(Poly other) {
            Poly res = new Poly();
            for (List<String> term1 : terms.keySet()) {
                for (List<String> term2 : other.terms.keySet()) {
                    List<String> term = new ArrayList<>(term1);
                    term.addAll(term2);
                    Collections.sort(term);
                    res.update(term, terms.get(term1) * other.terms.get(term2));
                }
            }
            return res;
        }

        public Poly calculate(Poly other, char operator) {
            switch (operator) {
            case '+':
                return add(other, 1);
            case '-':
                return add(other, -1);
            case '*':
                return multiply(other);
            }
            throw new UnsupportedOperationException("" + operator);
        }

        public Poly evaluate(Map<String, Integer> values) {
            Poly res = new Poly();
            for (List<String> term : terms.keySet()) {
                int coefficient = terms.get(term);
                List<String> newTerm = new ArrayList<>();
                for (String var : term) {
                    Integer val = values.get(var);
                    if (val != null) {
                        coefficient *= val;
                    } else {
                        newTerm.add(var);
                    }
                }
                res.update(newTerm, coefficient);
            }
            return res;
        }

        public int compare(List<String> term1, List<String> term2) {
            int i = 0;
            for (String x : term1) {
                String y = term2.get(i++);
                int diff = x.compareTo(y);
                if (diff != 0) { return diff; }
            }
            return 0;
        }

        public List<String> toList() {
            List<String> res = new ArrayList<>();
            List<List<String>> keys = new ArrayList<>(terms.keySet());
            keys.sort((a, b) -> a.size() != b.size() ? b.size() - a.size() : compare(a, b));
            for (List<String> key : keys) {
                int coefficient = terms.get(key);
                if (coefficient == 0) { continue; }

                StringBuilder word = new StringBuilder();
                word.append(coefficient);
                for (String token : key) {
                    word.append('*').append(token);
                }
                res.add(word.toString());
            }
            return res;
        }
    }

    private void test(String expression, String[] evalvars, int[] evalints, String[] expected) {
        List<String> expectedList = Arrays.asList(expected);
        assertEquals(expectedList, basicCalculatorIV(expression, evalvars, evalints));
        assertEquals(expectedList, basicCalculatorIV2(expression, evalvars, evalints));
    }

    @Test public void test() {
        test("e + 8 - a + 5", new String[] {"e"}, new int[] {1}, new String[] {"-1*a", "14"});
        test("e - 8 + temperature - pressure", new String[] {"e", "temperature"}, new int[] {1, 12},
             new String[] {"-1*pressure", "5"});
        test("(e + 8) * (e - 8)", new String[] {}, new int[] {}, new String[] {"1*e*e", "-64"});
        test("7 - 7", new String[] {}, new int[] {}, new String[] {});
        test("a * b * c + b * a * c * 4", new String[] {}, new int[] {}, new String[] {"5*a*b*c"});
        test("((a - b) * (b - c) + (c - a)) * ((a - b) + (b - c) * (c - a))", new String[] {},
             new int[] {},
             new String[] {"-1*a*a*b*b", "2*a*a*b*c", "-1*a*a*c*c", "1*a*b*b*b", "-1*a*b*b*c",
                           "-1*a*b*c*c", "1*a*c*c*c", "-1*b*b*b*c", "2*b*b*c*c", "-1*b*c*c*c",
                           "2*a*a*b", "-2*a*a*c", "-2*a*b*b", "2*a*c*c", "1*b*b*b", "-1*b*b*c",
                           "1*b*c*c", "-1*c*c*c", "-1*a*a", "1*a*b", "1*a*c", "-1*b*c"});
        test("9 * (8 - 10 + 4 * aw) - (3 * 2 + 1 * cl - 11)",
             new String[] {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n",
                           "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "aa", "ab",
                           "ac", "ad", "ae", "af", "ag", "ah", "ai", "aj", "ak", "al", "am", "an",
                           "ao", "ap", "aq", "ar", "as", "at", "au", "av", "aw", "ax", "ay", "az",
                           "ba", "bb", "bc", "bd", "be", "bf", "bg", "bh", "bi", "bj", "bk", "bl",
                           "bm", "bn", "bo", "bp", "bq", "br", "bs", "bt", "bu", "bv", "bw", "bx",
                           "by", "bz", "ca", "cb", "cc", "cd", "ce", "cf", "cg", "ch", "ci", "cj",
                           "ck", "cl", "cm", "cn", "co", "cp", "cq", "cr", "cs", "ct", "cu", "cv"},
             new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
                        11, 12, 5, 6, 7, 8, 9, 10, 11, 12, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
                        0, 1, 2, 3, 4, 10, 11, 12, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 0, 1,
                        2, 3, 4, 5, 6, 7, 8, 9, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 0, 1, 2, 3, 4,
                        5, 6, 7, 8, 9, 10}, new String[] {"23"});
        test("(12 + 2 * 12 * w) * (w + 9 * 12 + 9) * ak - (ab * 8 - 9 * bk) - 0",
             new String[] {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n",
                           "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "aa", "ab",
                           "ac", "ad", "ae", "af", "ag", "ah", "ai", "aj", "ak", "al", "am", "an",
                           "ao", "ap", "aq", "ar", "as", "at", "au", "av", "aw", "ax", "ay", "az",
                           "ba", "bb", "bc", "bd", "be", "bf", "bg", "bh", "bi", "bj", "bk", "bl",
                           "bm", "bn", "bo", "bp", "bq", "br", "bs", "bt", "bu", "bv", "bw", "bx",
                           "by", "bz", "ca", "cb", "cc", "cd", "ce", "cf", "cg", "ch", "ci", "cj",
                           "ck", "cl", "cm", "cn", "co", "cp", "cq", "cr", "cs", "ct", "cu", "cv"},
             new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
                        11, 12, 5, 6, 7, 8, 9, 10, 11, 12, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
                        0, 1, 2, 3, 4, 10, 11, 12, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 0, 1,
                        2, 3, 4, 5, 6, 7, 8, 9, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 0, 1, 2, 3, 4,
                        5, 6, 7, 8, 9, 10}, new String[] {"57471"});
        test("(ba - bd + ct + 5 * ca) * 7 * 3 * (7 + bw * cg - 7)",
             new String[] {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n",
                           "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "aa", "ab",
                           "ac", "ad", "ae", "af", "ag", "ah", "ai", "aj", "ak", "al", "am", "an",
                           "ao", "ap", "aq", "ar", "as", "at", "au", "av", "aw", "ax", "ay", "az",
                           "ba", "bb", "bc", "bd", "be", "bf", "bg", "bh", "bi", "bj", "bk", "bl",
                           "bm", "bn", "bo", "bp", "bq", "br", "bs", "bt", "bu", "bv", "bw", "bx",
                           "by", "bz", "ca", "cb", "cc", "cd", "ce", "cf", "cg", "ch", "ci", "cj",
                           "ck", "cl", "cm", "cn", "co", "cp", "cq", "cr", "cs", "ct", "cu", "cv"},
             new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
                        11, 12, 5, 6, 7, 8, 9, 10, 11, 12, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
                        0, 1, 2, 3, 4, 10, 11, 12, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 0, 1,
                        2, 3, 4, 5, 6, 7, 8, 9, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 0, 1, 2, 3, 4,
                        5, 6, 7, 8, 9, 10}, new String[] {"28224"});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
