import org.junit.Test;
import static org.junit.Assert.*;

// LC158: https://leetcode.com/problems/read-n-characters-given-read4-ii-call-multiple-times/
//
// The API: int read4(char *buf) reads 4 characters at a time from a file.
// The return value is the actual number of characters read.
// By using the read4 API, implement the function int read(char *buf, int n) that
// reads n characters from the file.
// Note:
// The read function may be called multiple times
public class ReadNChars2 {
    private static String input;
    private static int offset = 0;

    public static void setInput(String input) {
        ReadNChars2.input = input;
        offset = 0;
    }

    public int read4(char[] buf) {
        int len = input.length();
        for (int i = 0; i < 4; i++, offset++) {
            if (offset < len) {
                buf[i] = input.charAt(offset);
            } else return i;
        }
        return 4;
    }

    /////////////////////////////////////////////

    private char[] buf4 = new char[4];
    private int remaining;

    // beats 7.94%(3 ms for 81 tests)
    public int read(char[] buf, int n) {
        int i = 0;
        for (; remaining > 0 && i < n; i++, remaining--) {
            buf[i] = buf4[i];
        }
        for (int nRead = 0; i < n; i += nRead) {
            nRead = read4(buf4);
            if (nRead == 0) return i;

            System.arraycopy(buf4, 0, buf, i, Math.min(nRead, n - i));
            if ((remaining = nRead - (n - i)) > 0) {
                i = n - i;
                break;
            }
        }
        if (remaining > 0) {
            System.arraycopy(buf4, i, buf4, 0, remaining);
        }
        return n;
    }

    private int bufCount = 0;
    private int bufPos = 0;

    // beats 32.42%(2 ms for 81 tests)
    public int read2(char[] buf, int n) {
        int total = Math.min(n, bufCount);
        System.arraycopy(buf4, bufPos, buf, 0, total);
        bufCount -= total;
        bufPos += total;
        for (int nRead = 4; total < n && nRead == 4; total += bufPos) {
            nRead = read4(buf4);
            bufPos = Math.min(n - total, nRead);
            bufCount = nRead - bufPos;
            System.arraycopy(buf4, 0, buf, total, bufPos);
        }
        return total;
    }

    // beats 32.42%(2 ms for 81 tests)
    public int read3(char[] buf, int n) {
        int total = 0;
        while (total < n) {
            if (bufPos < bufCount) {
                buf[total++] = buf4[bufPos++];
            } else {
                bufCount = read4(buf4);
                bufPos = 0;
                if (bufCount == 0) break;
            }
        }
        return total;
    }

    private int bufCnt = 0;
    private int bufIndex = 0;

    // beats 32.42%(2 ms for 81 tests)
    public int read4(char[] buf, int n) {
        int total = 0;
        while (total < n) {
            if (bufIndex == 0) {
                bufCnt = read4(buf4);
            }
            while (total < n && bufIndex < bufCnt) {
                buf[total++] = buf4[bufIndex++];
            }
            if (bufIndex == bufCnt) {
                bufIndex = 0;
            }
            if (bufCnt < 4) break;
        }
        return total;
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(Function<char[], Integer, Integer> read, String input, int[] n, int[] expected) {
        setInput(input);
        for (int i = 0, j = 0; i < n.length; j += n[i], i++) {
            char[] buf = new char[n[i]];
            assertEquals(expected[i], (int)read.apply(buf, n[i]));
            assertEquals(input.substring(j, j + expected[i]), new String(buf, 0, expected[i]));
        }
    }

    void test(String input, int[] n, int[] expected) {
        ReadNChars2 r = new ReadNChars2();
        test(r::read, input, n, expected);
        test(r::read2, input, n, expected);
        test(r::read3, input, n, expected);
        test(r::read4, input, n, expected);
    }

    @Test
    public void test() {
        test("1234567", new int[] {6}, new int[] {6});
        test("abc", new int[] {1, 1, 1, 1}, new int[] {1, 1, 1, 0});
        test("ab", new int[] {1, 2}, new int[] {1, 1});
        test("abc", new int[] {1, 2}, new int[] {1, 2});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ReadNChars2");
    }
}
