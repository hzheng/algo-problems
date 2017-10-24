import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC591: https://leetcode.com/problems/tag-validator/
//
// Given a string representing a code snippet, you need to implement a tag validator
// to parse the code and return whether it is valid. A code is valid if all the following
// rules hold:
// The code must be wrapped in a valid closed tag. Otherwise, the code is invalid.
// A closed tag (not necessarily valid) has exactly the following format : <TAG_NAME>TAG_CONTENT</TAG_NAME>.
// Among them, <TAG_NAME> is the start tag, and </TAG_NAME> is the end tag. The TAG_NAME in
// start and end tags should be the same. A closed tag is valid if and only if the
// TAG_NAME and TAG_CONTENT are valid.
// A valid TAG_NAME only contain upper-case letters, and has length in range [1,9].
// A valid TAG_CONTENT may contain other valid closed tags, cdata and any characters(see note1)
// EXCEPT unmatched <, unmatched start and end tag, and unmatched or closed tags
// with invalid TAG_NAME. Otherwise, the TAG_CONTENT is invalid.
// A start tag is unmatched if no end tag exists with the same TAG_NAME, and vice versa.
// However, you also need to consider the issue of unbalanced when tags are nested.
// A < is unmatched if you cannot find a subsequent >. And when you find a < or </, all
// the subsequent characters until the next > should be parsed as TAG_NAME(not necessarily valid).
// The cdata has the following format : <![CDATA[CDATA_CONTENT]]>. The range of CDATA_CONTENT
// is defined as the characters between <![CDATA[ and the first subsequent ]]>.
// CDATA_CONTENT may contain any characters. The function of cdata is to forbid the validator
// to parse CDATA_CONTENT, so even it has some characters that can be parsed as tag, you
// should treat it as regular characters.
// Note:
// Assume the input code only contain letters, digits, '<','>','/','!','[',']' and ' '.
public class TagValidator {
    private static final String DATA_START = "![CDATA[";
    private static final int DATA_START_LEN = DATA_START.length();
    private static final String DATA_END = "]]>";
    private static final int DATA_END_LEN = DATA_END.length();

    // Stack
    // beats 90.38%(18 ms for 256 tests)
    public boolean isValid(String code) {
        ArrayDeque<String> stack = new ArrayDeque<>();
        boolean hasTag = false;
        for (int i = 0, len = code.length(); i < len; i++) {
            char cur = code.charAt(i);
            if (cur != '<') {
                if (!hasTag) return false;
                continue;
            }
            char next = code.charAt(++i);
            if (next == '!') {
                if (!hasTag) return false;

                String dataStart =
                    code.substring(i, Math.min(len, i + DATA_START_LEN));
                if (!dataStart.equals(DATA_START)) return false;

                int dataEnd = code.indexOf(DATA_END, i + DATA_START_LEN);
                if (dataEnd < 0) return false;

                i = dataEnd + DATA_END_LEN - 1;
                continue;
            }
            int tagEnd = code.indexOf('>', i);
            if (tagEnd < 0) return false;

            boolean openTag = (next != '/');
            String tag = getTag(code, openTag ? i : (i + 1), tagEnd);
            if (tag == null) return false;

            i = tagEnd;
            if (openTag) {
                stack.push(tag);
                hasTag = true;
            } else if (stack.isEmpty() || !tag.equals(stack.pop())
                       || stack.isEmpty() && (i != len - 1)) {
                return false;
            }
        }
        return stack.isEmpty();
    }

    private String getTag(String code, int start, int end) {
        String tag = code.substring(start, end);
        int len = tag.length();
        if (len >= 1 && len <= 9) {
            for (int i = 0; i < len; i++) {
                if (!Character.isUpperCase(tag.charAt(i))) return null;
            }
            return tag;
        }
        return null;
    }

    // Recursion
    // beats 90.38%(18 ms for 256 tests)
    public boolean isValid2(String code) {
        char[] cs = code.toCharArray();
        String tag = getTag(cs, 0, cs.length);
        if (tag == null || tag.isEmpty()
            || !code.endsWith("</" + tag + ">")) return false;

        return finishContent(cs, tag.length() + 2,
                             cs.length - tag.length() - 3, false) >= 0;
    }

    private String getTag(char[] code, int start, int end) {
        for (int i = start + 1; i < end && i <= start + 10; i++) {
            if (code[i] == '>') {
                return new String(code, start + 1, i - start - 1);
            }
            if (!Character.isUpperCase(code[i])) return "";
        }
        return "";
    }

    private int finishData(char[] code, int start, int end) {
        if (end - start <= DATA_START_LEN || !DATA_START.equals(
                new String(code, start, DATA_START_LEN))) return -1;

        start += DATA_START_LEN;
        while (start < end) {
            if (code[start++] != ']') continue;
            if (start >= end) return -1;
            if (code[start++] != ']') continue;
            if (start >= end) return -1;
            if (code[start++] == '>') return start;
        }
        return -1;
    }

    private int finishContent(char[] code, int start, int end, boolean match) {
        for (; start < end && code[start] != '<'; start++) {}
        if (start >= end) return end;

        if (code[start + 1] == '/') return match ? start - 1 : -1;

        if (code[start + 1] == '!') {
            start = finishData(code, start + 1, end);
            if (start < 0) return -1;
            return finishContent(code, start, end, match);
        }

        String tag = getTag(code, start, end);
        int tagLen = tag.length();
        if (tagLen == 0) return -1;

        int next = finishContent(code, start + tagLen + 2, end, true);
        if (next < 0 || next + tagLen + 3 > end) return -1;
        if (!("</" + tag + ">").equals(
                new String(code, next + 1, tagLen + 3))) return -1;
        return finishContent(code, next + tagLen + 3, end, match);
    }

    // TODO: Regex

    void test(String code, boolean expected) {
        assertEquals(expected, isValid(code));
        assertEquals(expected, isValid2(code));
    }

    @Test
    public void test() {
        test("123", false);
        test("<A>123<</A>", false);
        test("<A></A>", true);
        test(" <A></A>", false);
        test("<AC>  B </AC>", true);
        test("<1>  B </1>", false);
        test("<DIV><></></DIV>", false);
        test("<ABCDEFGHI>A</ABCDEFGHI>", true);
        test("<ABCDEFGHIJ>A</ABCDEFGHIJ>", false);
        test("<A><!A></A>", false);
        test("<A><A></A></A></A>", false);
        test("<AB>  <B> </A>   </B>", false);
        test("<A></A><B></B>", false);
        test("</A></A></A></A>", false);
        test("<DIV>  unmatched <  </DIV>", false);
        test("<A><![CDATA[</A>]]123></A>", false);
        test("<A><![CDATA[</A>]]>123</A>", true);
        test("<DIV>This is the first line <![CDATA[<<<<<<<]]></DIV>", true);
        test("<![CDATA[wahaha]]]><![CDATA[]> wahaha]]>", false);
        test("<DIV>>>  ![cdata[]] <![CDATA[<div>]>]]>]]>>]</DIV>", true);
        test("<DIV>  div tag is not closed  <DIV>", false);
        test("<DIV> closed tags with invalid tag name  <b>123</b> </DIV>",
             false);
        test(
            "<DIV>  unmatched start tag <B>  and unmatched end tag </C>  </DIV>",
            false);
        test(
            "<DIV> unmatched tags with invalid tag name  </1234567890> and <CDATA[[]]>  </DIV>",
            false);
        test(
            "<A><A>456</A>  <A> 123  !!  <![CDATA[]]>  123 </A>   <A>123</A></A>",
            true);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
