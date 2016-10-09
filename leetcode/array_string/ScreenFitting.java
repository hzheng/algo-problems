import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC418: https://leetcode.com/problems/sentence-screen-fitting
//
// Given a rows x cols screen and a sentence represented by a list of words,
// find how many times the given sentence can be fitted on the screen.
// Note:
// A word cannot be split into two lines.
// The order of words in the sentence must remain unchanged.
// Two consecutive words in a line must be separated by a single space.
// Total words in the sentence won't exceed 100.
// Length of each word won't exceed 10.
// 1 ≤ rows, cols ≤ 20,000.
public class ScreenFitting {
    // Time Limit Exceeded
    public int wordsTyping(String[] sentence, int rows, int cols) {
        int count = sentence.length;
        int[] lens = new int[count];
        for (int i = 0; i < count; i++) {
            lens[i] = sentence[i].length();
        }
        int colLeft = cols;
        int index = 0;
        int times = 0;
        for (int rowLeft = rows; rowLeft > 0; ) {
            if (colLeft >= lens[index]) {
                colLeft -= lens[index];
                if (colLeft > 0) {
                    colLeft--; // space
                }
                if (++index == count) {
                    index = 0;
                    times++;
                }
            } else {
                rowLeft--;
                colLeft = cols;
            }
        }
        return times;
    }

    // beats N/A(88 ms for 51 tests)
    public int wordsTyping2(String[] sentence, int rows, int cols) {
        int count = sentence.length;
        int[] lens = new int[count];
        int sentenceLen = 0;
        for (int i = 0; i < count; i++) {
            lens[i] = sentence[i].length();
            sentenceLen += lens[i] + 1;
        }
        int times = 0;
        for (int colLeft = cols, rowLeft = rows, index = 0; rowLeft > 0; ) {
            times += colLeft / sentenceLen;
            colLeft %= sentenceLen;
            if (colLeft < lens[index]) {
                rowLeft--;
                colLeft = cols;
            } else {
                colLeft -= lens[index] + 1;
                if (++index == count) {
                    index = 0;
                    times++;
                }
            }
        }
        return times;
    }

    void test(String[] sentence, int rows, int cols, int expected) {
        assertEquals(expected, wordsTyping(sentence, rows, cols));
        assertEquals(expected, wordsTyping2(sentence, rows, cols));
    }

    @Test
    public void test() {
        test(new String[] {"f", "p", "a"}, 1, 7, 1);
        test(new String[] {"f", "p", "a"}, 8, 7, 10);
        test(new String[] {"a", "bcd", "e"}, 3, 6, 2);
        test(new String[] {"hello", "world"}, 2, 8, 1);
        test(new String[] {"I", "had", "apple", "pie"}, 4, 5, 1);
        test(new String[] {"a", "bcd"}, 20000, 20000, 66660000);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ScreenFitting");
    }
}
