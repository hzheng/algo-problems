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

    // beats 9.88%(87 ms for 51 tests)
    public int wordsTyping2(String[] sentence, int rows, int cols) {
        int count = sentence.length;
        int sentenceLen = 0;
        for (int i = 0; i < count; i++) {
            sentenceLen += sentence[i].length() + 1;
        }
        int times = 0;
        for (int colLeft = cols, rowLeft = rows, index = 0; rowLeft > 0; ) {
            times += colLeft / sentenceLen;
            colLeft %= sentenceLen;
            if (colLeft < sentence[index].length()) {
                rowLeft--;
                colLeft = cols;
            } else {
                colLeft -= sentence[index].length() + 1;
                if (++index == count) {
                    index = 0;
                    times++;
                }
            }
        }
        return times;
    }

    // beats 44.01%(22 ms for 51 tests)
    public int wordsTyping3(String[] sentence, int rows, int cols) {
        String s = String.join(" ", sentence) + " ";
        int len = s.length();
        int cur = 0;
        for (int i = 0; i < rows; i++) {
            cur += cols;
            if (s.charAt(cur % len) == ' ') {
                cur++;
            } else {
                for (; cur > 0 && s.charAt((cur - 1) % len) != ' '; cur--) {}
            }
        }
        return cur / len;
    }

    // Solution of Choice
    // Dynamic Programming
    // beats 91.59%(13 ms for 51 tests)
    public int wordsTyping4(String[] sentence, int rows, int cols) {
        String s = String.join(" ", sentence) + " ";
        int len = s.length();
        int[] shift = new int[len];
        for (int i = 1; i < len; ++i) {
            shift[i] = (s.charAt(i) == ' ') ? 1 : shift[i - 1] - 1;
        }
        int cur = 0;
        for (int i = 0; i < rows; ++i) {
            cur += cols;
            cur += shift[cur % len];
        }
        return cur / len;
    }

    // Memoization
    // beats 82.02%(16 ms for 51 tests)
    public int wordsTyping5(String[] sentence, int rows, int cols) {
        int n = sentence.length;
        int[] next = new int[n];
        int[] times = new int[n];
        for (int i = 0; i < n; i++) {
            int cur = i;
            int time = 0;
            for (int len = 0; len + sentence[cur].length() <= cols; ) {
                len += sentence[cur++].length() + 1;
                if (cur == n) {
                    cur = 0;
                    time++;
                }
            }
            next[i] = cur;
            times[i] = time;
        }
        int res = 0;
        for (int i = 0, cur = 0; i < rows; i++, cur = next[cur]) {
            res += times[cur];
        }
        return res;
    }

    // Memoization
    // https://discuss.leetcode.com/topic/65173/12ms-java-solution-using-dp
    // beats 91.59%(13 ms for 51 tests)
    public int wordsTyping6(String[] sentence, int rows, int cols) {
        int n = sentence.length;
        int[] next = new int[n];
        for (int i = 0, cur = 0, len = 0; i < n; i++) {
            if (i > 0 && len > 0) { // adjust length
                len -= sentence[i - 1].length() + 1;
            }
            while (len + sentence[cur % n].length() <= cols) {
                len += sentence[cur++ % n].length() + 1;
            }
            next[i] = cur;
        }
        int words = 0;
        for(int i = 0, cur = 0; i < rows; i++, cur = next[cur] % n) {
            // if(next[cur] == cur) return 0;
            words += next[cur] - cur;
        }
        return words / n;
    }

    void test(String[] sentence, int rows, int cols, int expected) {
        assertEquals(expected, wordsTyping(sentence, rows, cols));
        assertEquals(expected, wordsTyping2(sentence, rows, cols));
        assertEquals(expected, wordsTyping3(sentence, rows, cols));
        assertEquals(expected, wordsTyping4(sentence, rows, cols));
        assertEquals(expected, wordsTyping5(sentence, rows, cols));
        assertEquals(expected, wordsTyping6(sentence, rows, cols));
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
