import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// Given an array of words and a length L, format the text such that each line
// has exactly L characters and is fully (left and right) justified.
// You should pack your words in a greedy approach; that is, pack as many words
// as you can in each line. Pad extra spaces ' ' when necessary so that each
// line has exactly L characters. Extra spaces between words should be
// distributed as evenly as possible. If the number of spaces on a line do not
// divide evenly between words, the empty slots on the left will be assigned
// more spaces than the slots on the right. For the last line of text, it should
// be left justified and no extra space is inserted between words.
public class TextJustification {
    // beats 13.12%
    public List<String> fullJustify(String[] words, int maxWidth) {
        List<String> res = new ArrayList<>();
        List<String> line = new ArrayList<>();
        int len = 0;
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            int newLen = word.length();
            int oldLen = len + (len > 0 ? 1 : 0);
            if (oldLen + newLen <= maxWidth) {
                len = oldLen + newLen;
            } else {
                append(res, line, maxWidth);
                len = newLen;
                line.clear();
            }
            line.add(word);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(line.get(0));
        for (int i = 1; i < line.size(); i++) {
            sb.append(" ").append(line.get(i));
        }
        for (int i = maxWidth - sb.length(); i > 0; i--) {
            sb.append(" ");
        }
        res.add(sb.toString());
        return res;
    }

    private void append(List<String> res, List<String> line, int width) {
        int minWidth = -1;
        for (String word : line) {
            minWidth += word.length() + 1;
        }
        int extra = width - minWidth;
        int spaces = line.size() - 1;
        StringBuilder sb = new StringBuilder();
        sb.append(line.get(0));
        if (spaces == 0) {
            for (int i = extra; i > 0; i--) {
                sb.append(" ");
            }
            res.add(sb.toString());
            return;
        }

        int pad = extra / spaces + 1;
        int leftPads = extra % spaces;
        for (int i = 1; i < line.size(); i++) {
            for (int j = pad; j > 0; j--) {
                sb.append(" ");
            }
            if (leftPads-- > 0) {
                sb.append(" ");
            }
            sb.append(line.get(i));
        }
        res.add(sb.toString());
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(Function<String[], Integer, List<String> > justify, String name,
              String[] words, int w, String ... expected) {
        List<String> res = justify.apply(words, w);
        assertArrayEquals(expected, res.toArray(new String[0]));
    }

    void test(String[] words, int w, String ... expected) {
        TextJustification t = new TextJustification();
        test(t::fullJustify, "fullJustify", words, w, expected);
    }

    @Test
    public void test1() {
        test(new String[] {"This", "is", "an", "example", "of", "text",
                           "justification."}, 16,
             "This    is    an",
             "example  of text",
             "justification.  ");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("TextJustification");
    }
}
