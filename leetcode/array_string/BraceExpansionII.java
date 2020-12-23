import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1096: https://leetcode.com/problems/brace-expansion-ii/
//
// Under a grammar given below, strings can represent a set of lowercase words.  Let's use R(expr)
// to denote the set of words the expression represents.
// Grammar can best be understood through simple examples:
// Single letters represent a singleton set containing that word.
// R("a") = {"a"}
// R("w") = {"w"}
// When we take a comma delimited list of 2 or more expressions, we take the union of possibilities.
// R("{a,b,c}") = {"a","b","c"}
// R("{{a,b},{b,c}}") = {"a","b","c"} (notice the final set only contains each word at most once)
// When we concatenate two expressions, we take the set of possible concatenations between two words
// where the first word comes from the first expression and the second word comes from the second
// expression.
// R("{a,b}{c,d}") = {"ac","ad","bc","bd"}
// R("a{b,c}{d,e}f{g,h}") = {"abdfg", "abdfh", "abefg", "abefh", "acdfg", "acdfh", "acefg", "acefh"}
// Formally, the 3 rules for our grammar:
// For every lowercase letter x, we have R(x) = {x}
// For expressions e_1, e_2, ... , e_k with k >= 2, we have R({e_1,e_2,...}) = R(e_1) ∪ R(e_2) ∪ ...
// For expressions e_1 and e_2, we have R(e_1 + e_2) = {a + b for (a, b) in R(e_1) × R(e_2)}, where
// + denotes concatenation, and × denotes the cartesian product.
// Given an expression representing a set of words under the given grammar, return the sorted list
// of words that the expression represents.
//
// Constraints:
// 1 <= expression.length <= 60
// expression[i] consists of '{', '}', ','or lowercase English letters.
// The given expression represents a set of words based on the grammar given in the description.
public class BraceExpansionII {
    // Stack + Set
    // time complexity: O(N), space complexity: O(N)
    // 10 ms(72.58%), 39.6 MB(87.10%) for 112 tests
    public List<String> braceExpansionII(String expression) {
        Stack<Token> stack = new Stack<>();
        for (int i = 0, n = expression.length(), start = 0; i <= n; i++) {
            char c = (i == n) ? 0 : expression.charAt(i);
            if (Character.isAlphabetic(c)) { continue; }

            if (i > start) {
                stack.push(new Token(expression.substring(start, i)));
            }
            start = i + 1;
            if (c == '{') {
                stack.push(Token.BRACE);
            } else {
                boolean endBrace = (c == '}');
                merge(stack, endBrace);
                stack.peek().setToConcatenate(endBrace);
            }
        }
        return stack.peek().toList();
    }

    private void merge(Stack<Token> stack, boolean endBrace) {
        while (stack.size() > 1) {
            Token token1 = stack.pop();
            Token token2 = stack.peek();
            if (token2 == Token.BRACE) {
                if (endBrace) {
                    stack.pop();
                }
                stack.push(token1);
                break;
            }
            if (token2.isToConcatenate()) {
                token2.concatenate(token1);
            } else {
                token2.union(token1);
            }
        }
    }

    private static class Token {
        final static Token BRACE = new Token(null);

        private Set<String> set;
        private boolean toConcatenate = true;

        public Token(String word) {
            if (word != null) {
                set = new HashSet<>();
                set.add(word);
            }
        }

        public void setToConcatenate(boolean toConcatenate) {
            this.toConcatenate = toConcatenate;
        }

        public boolean isToConcatenate() {
            return toConcatenate;
        }

        public void union(Token other) {
            set.addAll(other.set);
        }

        public void concatenate(Token other) {
            Set<String> newSet = new HashSet<>();
            for (String s : set) {
                for (String t : other.set) {
                    newSet.add(s + t);
                }
            }
            set = newSet;
        }

        public List<String> toList() {
            List<String> res = new ArrayList<>(set);
            Collections.sort(res);
            return res;
        }
    }

    // Recursion + Set
    // time complexity: O(N), space complexity: O(N)
    // 11 ms(66.13%), 39.2 MB(100.00%) for 112 tests
    public List<String> braceExpansionII2(String expression) {
        List<String> res = new ArrayList<>(braceExpand(expression, 0, expression.length()));
        Collections.sort(res);
        return res;
    }

    private Set<String> braceExpand(String expression, int start, int end) {
        if (start >= end) { return Collections.emptySet(); }

        Set<String> res;
        int next = start + 1;
        if (expression.charAt(start) == '{') {
            for (int level = 1; ; next++) {
                char c = expression.charAt(next);
                if (c == '{') {
                    level++;
                } else if (c == '}' && --level == 0) { break; }
            }
            res = braceExpand(expression, start + 1, next++);
        } else { // word
            for (; next < end && Character.isAlphabetic(expression.charAt(next)); next++) {}
            res = new HashSet<>();
            res.add(expression.substring(start, next));
        }
        if ((next < end) && (expression.charAt(next) != ',')) {
            int prev = next; // find next concatenation
            for (int level = 0; next < end; next++) {
                char c = expression.charAt(next);
                if (c == '{') {
                    level++;
                } else if (c == '}') {
                    level--;
                } else if (c == ',' && level == 0) { break; }
            }
            Set<String> res2 = new HashSet<>();
            for (String token : res) {
                for (String sec : braceExpand(expression, prev, next)) {
                    res2.add(token + sec);
                }
            }
            res = res2;
        }
        res.addAll(braceExpand(expression, next + 1, end));
        return res;
    }

    private void test(String expression, String[] expected) {
        List<String> expectedList = Arrays.asList(expected);
        assertEquals(expectedList, braceExpansionII(expression));
        assertEquals(expectedList, braceExpansionII2(expression));
    }

    @Test public void test() {
        test("{{a,z},a{b,c},{ab,z}}", new String[] {"a", "ab", "ac", "z"});
        test("{a,b}c{d,e}f", new String[] {"acdf", "acef", "bcdf", "bcef"});
        test("{a,b}{c,{d,e}}", new String[] {"ac", "ad", "ae", "bc", "bd", "be"});
        test("{{a,bc,z}y,ab{b,c}{f}}", new String[] {"abbf", "abcf", "ay", "bcy", "zy"});
        test("{{a,bc,z}y,ab{b,c}{f},{ab,z,{ab,d}}}",
             new String[] {"ab", "abbf", "abcf", "ay", "bcy", "d", "z", "zy"});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
