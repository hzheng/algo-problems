import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC604: https://leetcode.com/problems/design-compressed-string-iterator/
//
// Design and implement a data structure for a compressed string iterator.
// The given compressed string will be in the form of each letter followed by a
// positive integer representing the number of this letter existing in the
// original uncompressed string.
// next() - if the original string still has uncompressed characters, return the
// next letter; Otherwise return a white space.
// hasNext() - Judge whether there is any letter needs to be uncompressed.
public class StringIterator {
    static interface IStringIterator {
        public char next();
        public boolean hasNext();
    }

    // 2 Lists
    // beats 74.66%(121 ms for 169 tests)
    static class StringIterator1 implements IStringIterator {
        private List<Character> chars = new ArrayList<>();
        private List<Integer> counts = new ArrayList<>();
        private int cursor = 0;

        public StringIterator1(String compressedString) {
            int n = compressedString.length();
            for (int i = 0; i < n; ) {
                chars.add(compressedString.charAt(i));
                int count = 0;
                while (++i < n) {
                    char c = compressedString.charAt(i);
                    if (Character.isDigit(c)) {
                        count *= 10;
                        count += c - '0';
                    } else break;
                }
                counts.add(count);
            }
        }

        public char next() {
            if (!hasNext()) return ' ';

            int count = counts.get(cursor) - 1;
            counts.set(cursor, count);
            char res = chars.get(cursor);
            if (count == 0) {
                cursor++;
            }
            return res;
        }

        public boolean hasNext() {
            return cursor < chars.size();
        }
    }

    // beats 89.46%(116 ms for 169 tests)
    static class StringIterator2 implements IStringIterator {
        private String str;
        private char ch = ' ';
        private int count = 0;
        private int cursor = 0;

        public StringIterator2(String compressedString) {
            str = compressedString;
        }

        public char next() {
            if (!hasNext()) return ' ';

            if (count == 0) {
                ch = str.charAt(cursor++);
                while (cursor < str.length() &&
                       Character.isDigit(str.charAt(cursor))) {
                    count = count * 10 + str.charAt(cursor++) - '0';
                }
            }
            count--;
            return ch;
        }

        public boolean hasNext() {
            return cursor != str.length() || count != 0;
        }
    }

    // 2 Arrays
    // beats 12.11%(145 ms for 169 tests)
    static class StringIterator3 implements IStringIterator {
        private String[] chars;
        private int[] counts;
        private int cursor = 0;

        public StringIterator3(String compressedString) {
            counts = Arrays.stream(compressedString.substring(1)
                                   .split("[a-zA-Z]+")).mapToInt(
                Integer::parseInt).toArray();
            chars = compressedString.split("[0-9]+");
        }

        public char next() {
            if (!hasNext()) return ' ';

            char res = chars[cursor].charAt(0);
            if (--counts[cursor] == 0) {
                cursor++;
            }
            return res;
        }

        public boolean hasNext() {
            return cursor != chars.length;
        }
    }

    // Queue
    // beats 68.83%(123 ms for 169 tests)
    static class StringIterator4 implements IStringIterator {
        private Queue<int[]> queue = new LinkedList<>();

        public StringIterator4(String s) {
            for (int i = 0, n = s.length(), j; i < n; i = j) {
                j = i + 1;
                while (j < n && s.charAt(j) - 'A' < 0) {
                    j++;
                }
                queue.add(new int[] {s.charAt(i) - 'A',
                                     Integer.parseInt(s.substring(i + 1, j))});
            }
        }

        public char next() {
            if (!hasNext()) return ' ';

            int[] top = queue.peek();
            if (--top[1] == 0) {
                queue.poll();
            }
            return (char)('A' + top[0]);
        }

        public boolean hasNext() {
            return !queue.isEmpty();
        }
    }

    void test1(IStringIterator iterator) {
        assertEquals('L', iterator.next());
        assertEquals('e', iterator.next());
        assertEquals('e', iterator.next());
        assertEquals('t', iterator.next());
        assertEquals('C', iterator.next());
        assertEquals('o', iterator.next());
        assertEquals('d', iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals('e', iterator.next());
        assertFalse(iterator.hasNext());
        assertEquals(' ', iterator.next());
    }

    @Test
    public void test1() {
        String str = "L1e2t1C1o1d1e1";
        test1(new StringIterator1(str));
        test1(new StringIterator2(str));
        test1(new StringIterator3(str));
        test1(new StringIterator4(str));
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
