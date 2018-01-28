import java.util.*;
import java.util.regex.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC726: https://leetcode.com/problems/number-of-atoms/
//
// Given a chemical formula (given as a string), return the count of each atom.
// Note:
// All atom names consist of lowercase letters, except for the first character.
// The length of formula will be in the range [1, 1000].
// formula will only consist of letters, digits, and round parentheses, and is
// a valid formula as defined in the problem.
public class AtomCount {
    // Stack + SortedMap
    // beats 60.82%(13 ms for 28 tests)
    public String countOfAtoms(String formula) {
        ArrayDeque<Map<String, Integer> > stack = new ArrayDeque<>();
        stack.push(new TreeMap<>()); // only the first map need to be sorted
        char[] cs = formula.toCharArray();
        for (int i = 0, n = cs.length; i < n; ) {
            i = parse(cs, i, stack);
        }
        return convert(stack.peek());
    }

    private int parse(char[] cs, int i,
                      ArrayDeque<Map<String, Integer> > stack) {
        int j = i + 1;
        if (Character.isUpperCase(cs[i])) {
            for (; j < cs.length && Character.isLowerCase(cs[j]); j++) {}
            String e = String.valueOf(cs, i, j - i);
            i = j;
            int count = 1;
            if (j < cs.length && Character.isDigit(cs[j])) {
                for (; j < cs.length && Character.isDigit(cs[j]); j++) {}
                count = Integer.valueOf(String.valueOf(cs, i, j - i));
            }
            stack.peek().put(e, stack.peek().getOrDefault(e, 0) + count);
            return j;
        } else if (cs[i] == '(') {
            stack.push(new HashMap<>());
            return i + 1;
        } else if (cs[i] == ')') {
            Map<String, Integer> map = stack.pop();
            int count = 1;
            if (j < cs.length && Character.isDigit(cs[j])) {
                for (; j < cs.length && Character.isDigit(cs[j]); j++) {}
                count = Integer.valueOf(String.valueOf(cs, i + 1, j - i - 1));
            }
            Map<String, Integer> prevMap = stack.peek();
            for (String e : map.keySet()) {
                prevMap.put(e, prevMap.getOrDefault(e, 0) + map.get(e) * count);
            }
            return j;
        }
        return -1;
    }

    private String convert(Map<String, Integer> res) {
        StringBuilder sb = new StringBuilder();
        for (String e : res.keySet()) {
            sb.append(e);
            int count = res.get(e);
            if (count > 1) {
                sb.append(count);
            }
        }
        return sb.toString();
    }

    // Stack + SortedMap
    // beats 40.55%(15 ms for 28 tests)
    public String countOfAtoms2(String formula) {
        Stack<Map<String, Integer> > stack = new Stack<>();
        stack.push(new TreeMap<>());
        char[] cs = formula.toCharArray();
        for (int i = 0, n = formula.length(); i < n; ) {
            if (cs[i] == '(') {
                stack.push(new HashMap<>());
                i++;
            } else if (cs[i] == ')') {
                Map<String, Integer> top = stack.pop();
                int j = ++i;
                int count = 1;
                for (; i < n && Character.isDigit(cs[i]); i++) {}
                if (i > j) {
                    count = Integer.parseInt(formula.substring(j, i));
                }
                Map<String, Integer> prevMap = stack.peek();
                for (String e : top.keySet()) {
                    prevMap.put(e, prevMap.getOrDefault(e, 0) + top.get(e) * count);
                }
            } else {
                int j = i++;
                for (; i < n && Character.isLowerCase(cs[i]); i++) {}
                String e = formula.substring(j, i);
                j = i;
                for (; i < n && Character.isDigit(cs[i]); i++) {}
                int count = i > j ? Integer.parseInt(formula.substring(j, i)) : 1;
                stack.peek().put(e, stack.peek().getOrDefault(e, 0) + count);
            }
        }
        return convert(stack.peek());
    }

    // Recursion + SortedMap
    // beats 71.71%(12 ms for 28 tests)
    public String countOfAtoms3(String formula) {
        Map<String, Integer> res = new TreeMap<>();
        parse(formula.toCharArray(), 0, res);
        return convert(res);
    }

    private int parse(char[] cs, int i, Map<String, Integer> map) {
        int j = i + 1;
        if (Character.isUpperCase(cs[i])) {
            for (; j < cs.length && Character.isLowerCase(cs[j]); j++) {}
            String e = String.valueOf(cs, i, j - i);
            i = j;
            int count = 1;
            if (j < cs.length && Character.isDigit(cs[j])) {
                for (j++; j < cs.length && Character.isDigit(cs[j]); j++) {}
                count = Integer.valueOf(String.valueOf(cs, i, j - i));
            }
            map.put(e, map.getOrDefault(e, 0) + count);
        } else if (cs[i] == '(') {
            Map<String, Integer> newMap = new HashMap<>();
            do {
                i = parse(cs, i + 1, newMap);
            } while (i >= 0);
            j = i = -i;
            int count = 1;
            if (i < cs.length && Character.isDigit(cs[i])) {
                for (j = i + 1; j < cs.length && Character.isDigit(cs[j]);
                     j++) {}
                count = Integer.valueOf(String.valueOf(cs, i, j - i));
            }
            for (String e : newMap.keySet()) {
                map.put(e, map.getOrDefault(e, 0) + newMap.get(e) * count);
            }
        }
        return (j == cs.length || cs[j] == ')') ? -(j + 1) : parse(cs, j, map);
    }

    // Recursion + SortedMap
    // beats 30.70%(16 ms for 28 tests)
    private int i;

    public String countOfAtoms4(String formula) {
        i = 0;
        Map<String, Integer> res = parse(formula.toCharArray());
        return convert(res);
    }

    public Map<String, Integer> parse(char[] cs) {
        int n = cs.length;
        Map<String, Integer> map = new TreeMap<>();
        while (i < n && cs[i] != ')') {
            if (cs[i] == '(') {
                i++;
                for (Map.Entry<String, Integer> e : parse(cs).entrySet()) {
                    map.put(e.getKey(),
                            map.getOrDefault(e.getKey(), 0) + e.getValue());
                }
            } else {
                int j = i++;
                for (; i < n && Character.isLowerCase(cs[i]); i++) {}
                String e = String.valueOf(cs, j, i - j);
                for (j = i; i < n && Character.isDigit(cs[i]); i++) {}
                int count =
                    (i > j) ? Integer.valueOf(String.valueOf(cs, j, i - j)) : 1;
                map.put(e, map.getOrDefault(e, 0) + count);
            }
        }
        int j = ++i;
        for (; i < n && Character.isDigit(cs[i]); i++) {}
        if (i > j) {
            int count = Integer.valueOf(String.valueOf(cs, j, i - j));
            for (String e : map.keySet()) {
                map.put(e, map.get(e) * count);
            }
        }
        return map;
    }

    // Stack + SortedMap + Regex
    // beats 14.66%(21 ms for 28 tests)
    public String countOfAtoms5(String formula) {
        final Stack<Map<String, Integer> > stack = new Stack<>();
        stack.push(new TreeMap<>());
        Pattern pattern = Pattern.compile(
            "([A-Z][a-z]*)(\\d*)|(\\()|(\\))(\\d*)");
        Matcher matcher = pattern.matcher(formula);
        while (matcher.find()) {
            String match = matcher.group();
            int len = match.length();
            if (match.equals("(")) {
                stack.push(new HashMap<>());
            } else if (match.startsWith(")")) {
                Map<String, Integer> top = stack.pop();
                int count = len > 1
                            ? Integer.parseInt(match.substring(1, len)) : 1;
                for (String name : top.keySet()) {
                    stack.peek().put(name, stack.peek().getOrDefault(name, 0)
                                     + top.get(name) * count);
                }
            } else {
                int i = 1;
                for (; i < len && Character.isLowerCase(match.charAt(i)); i++) {}
                String name = match.substring(0, i);
                int count = i < len
                            ? Integer.parseInt(match.substring(i, len)) : 1;
                stack.peek().put(name,
                                 stack.peek().getOrDefault(name, 0) + count);
            }
        }
        return convert(stack.peek());
    }

    void test(String formula, String expected) {
        assertEquals(expected, countOfAtoms(formula));
        assertEquals(expected, countOfAtoms2(formula));
        assertEquals(expected, countOfAtoms3(formula));
        assertEquals(expected, countOfAtoms4(formula));
        assertEquals(expected, countOfAtoms5(formula));
    }

    @Test
    public void test() {
        test("H2O", "H2O");
        test("Mg(OH)2", "H2MgO2");
        test("K4(ON(SO3)2)2", "K4N2O14S4");
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
