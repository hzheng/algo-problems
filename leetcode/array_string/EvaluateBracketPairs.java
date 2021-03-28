import java.util.*;
import java.util.stream.Collectors;

import common.Utils;
import org.junit.Test;

import static org.junit.Assert.*;

// LC1807: https://leetcode.com/problems/evaluate-the-bracket-pairs-of-a-string/
//
// You are given a string s that contains some bracket pairs, with each pair containing a non-empty
// key. For example, in the string "(name)is(age)yearsold", there are two bracket pairs that contain
// the keys "name" and "age". You know the values of a wide range of keys. This is represented by a
// 2D string array knowledge where each knowledge[i] = [keyi, valuei] indicates that key keyi has a
// value of valuei. You are tasked to evaluate all of the bracket pairs. When you evaluate a bracket
// pair that contains some key keyi, you will:
// Replace keyi and the bracket pair with the key's corresponding valuei.
// If you do not know the value of the key, you will replace keyi and the bracket pair with a
// question mark "?" (without the quotation marks).
// Each key will appear at most once in your knowledge. There will not be any nested brackets in s.
// Return the resulting string after evaluating all of the bracket pairs.
//
// Constraints:
// 1 <= s.length <= 10^5
// 0 <= knowledge.length <= 10^5
// knowledge[i].length == 2
// 1 <= keyi.length, valuei.length <= 10
// s consists of lowercase English letters and round brackets '(' and ')'.
// Every open bracket '(' in s will have a corresponding close bracket ')'.
// The key in each bracket pair of s will be non-empty.
// There will not be any nested bracket pairs in s.
// keyi and valuei consist of lowercase English letters.
// Each keyi in knowledge is unique.
public class EvaluateBracketPairs {
    // Hash Table
    // time complexity: O(N+M), space complexity: O(N+M)
    // 31 ms(%), 79 MB(%) for 105 tests
    public String evaluate(String s, List<List<String>> knowledge) {
        Map<String, String> map = new HashMap<>();
        for (List<String> k : knowledge) {
            map.put(k.get(0), k.get(1));
        }
        StringBuilder sb = new StringBuilder();
        for (int n = s.length(), i = 0; i < n; i++) {
            char c = s.charAt(i);
            if (c != '(') {
                sb.append(c);
            } else {
                int j = i + 1;
                for (; s.charAt(i) != ')'; i++) {}
                sb.append(map.getOrDefault(s.substring(j, i), "?"));
            }
        }
        return sb.toString();
    }

    // Hash Table
    // time complexity: O(N+M), space complexity: O(N+M)
    // 38 ms(%), 79.6 MB(%) for 105 tests
    public String evaluate2(String s, List<List<String>> knowledge) {
        var map = knowledge.stream()
                           .collect(Collectors.toMap(k -> k.get(0), k -> k.get(1)));
        StringBuilder sb = new StringBuilder();
        for (int i = 0, n = s.length(); i < n; i++) {
            char c = s.charAt(i);
            if (c == '(') {
                int j = s.indexOf(")", i);
                sb.append(map.getOrDefault(s.substring(i + 1, j), "?"));
                i = j;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private void test(String s, String[][] knowledge, String expected) {
        List<List<String>> knowledgeList = Utils.toList(knowledge);
        assertEquals(expected, evaluate(s, knowledgeList));
        assertEquals(expected, evaluate2(s, knowledgeList));
    }

    @Test public void test() {
        test("(name)is(age)yearsold", new String[][] {{"name", "bob"}, {"age", "two"}},
             "bobistwoyearsold");
        test("hi(name)", new String[][] {{"a", "b"}}, "hi?");
        test("(a)(a)(a)aaa", new String[][] {{"a", "yes"}}, "yesyesyesaaa");
        test("(a)(b)", new String[][] {{"a", "b"}, {"b", "a"}}, "ba");
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
