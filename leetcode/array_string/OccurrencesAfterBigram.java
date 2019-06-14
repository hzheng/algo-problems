import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1078: https://leetcode.com/problems/occurrences-after-bigram/
//
// Given words first and second, consider occurrences in some text of the form "first second third",
// where second comes immediately after first, and third comes immediately after second.
// For each such occurrence, add "third" to the answer, and return the answer.
public class OccurrencesAfterBigram {
    // time complexity: O(N), space complexity: O(N)
    // 3 ms(22.44%), 35 MB(100%) for 28 tests
    public String[] findOcurrences(String text, String first, String second) {
        List<String> res = new ArrayList<>();
        String[] words = text.split("\\s+");
        for (int i = 1; i < words.length - 1; i++) {
            if (words[i].equals(second) && words[i - 1].equals(first)) {
                res.add(words[i + 1]);
            }
        }
        return res.toArray(new String[0]);
    }

    // time complexity: O(N), space complexity: O(N)
    // 1 ms(95.57%), 34.9 MB(100%) for 28 tests
    public String[] findOcurrences2(String text, String first, String second) {
        String[] words = text.split(" ");
        String[] res = new String[words.length];
        int p = 0;
        for (int i = 0; i < words.length - 2; i++) {
            if (words[i].equals(first) && words[i + 1].equals(second)) {
                res[p++] = words[i + 2];
            }
        }
        return Arrays.copyOf(res, p);
    }

    // time complexity: O(N), space complexity: O(N)
    // 0 ms(100.00%), 34.8 MB(100%) for 28 tests
    public String[] findOcurrences3(String text, String first, String second) {
        List<String> res = new ArrayList<>();
        String bigram = first + " " + second + " ";
        for (int i = 0, pos = 0; ; i = pos + 1) {
            pos = text.indexOf(bigram, i);
            if (pos < 0) {
                break;
            }
            int start = pos + bigram.length();
            int j = start;
            for (; j < text.length() && text.charAt(j) != ' '; j++) {}
            res.add(text.substring(start, j));
        }
        return res.toArray(new String[0]);
    }

    // Regex
    // time complexity: O(N), space complexity: O(N)
    // 3 ms(22.44%), 35 MB(100%) for 28 tests
    public String[] findOcurrences4(String text, String first, String second) {
        List<String> res = new ArrayList<>();
        Matcher m = Pattern.compile("(?=" + first + "\\s+" + second + "\\s+(\\S+))").matcher(text);
        while (m.find()) {
            res.add(m.group(1));
        }
        return res.toArray(new String[0]);
    }

    void test(String text, String first, String second, String[] expected) {
        assertArrayEquals(expected, findOcurrences(text, first, second));
        assertArrayEquals(expected, findOcurrences2(text, first, second));
        assertArrayEquals(expected, findOcurrences3(text, first, second));
        assertArrayEquals(expected, findOcurrences4(text, first, second));
    }

    @Test
    public void test() {
        test("alice is a good girl she is a good student", "a", "good",
             new String[]{"girl", "student"});
        test("we will we will rock you", "we", "will", new String[]{"we", "rock"});
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
