import java.util.*;

import java.nio.file.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.Trie;

/**
 * Cracking the Coding Interview(5ed) Problem 18.13:
 * Given a list of millions of words, create the largest possible rectangle of
 * letters s.t. every row forms a word(left to right) and every column forms a
 * word (top to bottom).
 */
public class WordMatrix {
    static class WordGroup {
        private Map<String, Boolean> lookup = new HashMap<String, Boolean>();
        private List<String> group = new ArrayList<String>();

        public boolean containsWord(String s) {
            return lookup.containsKey(s);
        }

        public void addWord(String s) {
            group.add(s);
            lookup.put(s, true);
        }

        public int length() {
            return group.size();
        }

        public String getWord(int i) {
            return group.get(i);
        }

        public List<String> getWords() {
            return group;
        }

        public static WordGroup[] createWordGroups(String[] list) {
            WordGroup[] groupList;
            int maxWordLength = 0;
            for (int i = 0; i < list.length; i++) {
                if (list[i].length() > maxWordLength) {
                    maxWordLength = list[i].length();
                }
            }

            /* Group the words in the dictionary into lists of words of
             * same length.groupList[i] will contain a list of words, each
             * of length (i+1). */
            groupList = new WordGroup[maxWordLength];
            for (int i = 0; i < list.length; i++) {
                int wordLength = list[i].length() - 1;
                if (groupList[wordLength] == null) {
                    groupList[wordLength] = new WordGroup();
                }
                groupList[wordLength].addWord(list[i]);
            }
            return groupList;
        }
    } // class WordGroup

    static class Rectangle {
        public int height;
        public int length;
        public char[][] matrix;

        public Rectangle(int len) {
            this.length = len;
        }

        public Rectangle(int length, int height, char[][] letters) {
            this.height = letters.length;
            this.length = letters[0].length;
            matrix = letters;
        }

        public char getLetter(int i, int j) {
            return matrix[i][j];
        }

        public String getColumn(int i) {
            char[] column = new char[height];
            for (int j = 0; j < height; j++) {
                column[j] = getLetter(j, i);
            }
            return new String(column);
        }

        public boolean isComplete(int l, int h, WordGroup groupList) {
            // Check if we have formed a complete rectangle.
            if (height == h) {
                // Check if each column is a word in the dictionary.
                for (int i = 0; i < l; i++) {
                    String col = getColumn(i);
                    if (!groupList.containsWord(col)) {
                        return false; // Invalid rectangle.
                    }
                }
                return true; // Valid Rectangle!
            }
            return false;
        }

        public boolean isPartialOK(int l, Trie<Boolean> trie) {
            if (height == 0) return true;

            for (int i = 0; i < l; i++) {
                String col = getColumn(i);
                if (!trie.contains(col, false)) {
                    return false; // Invalid rectangle.
                }
            }
            return true;
        }

        /* If the length of the argument s is consistent with that of this
         * Rectangle object, then return a Rectangle whose matrix is constructed by
         * appending s to the underlying matrix. Otherwise, return null. The
         * underlying matrix of this Rectangle object is /not/ modified.
         */
        public Rectangle append(String s) {
            if (s.length() != length) return null;

            char temp[][] = new char[height + 1][length];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < length; j++) {
                    temp[i][j] = matrix[i][j];
                }
            }
            s.getChars(0, length, temp[height], 0);
            return new Rectangle(length, height + 1, temp);
        }

        public String[] toStrings() {
            String[] list = new String[height];
            for (int i = 0; i < height; i++) {
                char[] tmp = new char[length];
                for (int j = 0; j < length; j++) {
                    tmp[j] = matrix[i][j];
                }
                list[i] = new String(tmp);
            }
            return list;
        }

        @Override
        public String toString() {
            return Arrays.toString(toStrings());
        }
    } // class Rectangle

    private static int maxWordLength;
    private static WordGroup[] groupList;
    private static Trie<Boolean> trieList[];

    // TODO: algorithm is too slow
    @SuppressWarnings("unchecked")
    public static Rectangle maxMatrix(String[] list) {
        groupList = WordGroup.createWordGroups(list);
        maxWordLength = groupList.length;
        trieList = new Trie[maxWordLength];

        int maxSize = maxWordLength * maxWordLength;
        for (int z = maxSize; z > 0; z--) {
            // Find out all pairs i,j less than maxWordLength s.t. i * j = z
            for (int i = 1; i <= maxWordLength; i++) {
                if (z % i == 0) {
                    int j = z / i;
                    if (j <= maxWordLength) {
                        Rectangle rectangle = makeRectangle(i,j);
                        if (rectangle != null) return rectangle;
                    }
                }
            }
        }
        return null;
    }

    /* This function takes the length and height of a rectangle as
     * arguments. It tries to form a rectangle of the given length and
     * height using words of the specified length as its rows, in which
     * words whose length is the specified height form the columns.
     */
    private static Rectangle makeRectangle(int length, int height) {
        if (groupList[length - 1] == null || groupList[height - 1] == null) {
            return null;
        }
        if (trieList[height - 1] == null) {
            List<String> words = groupList[height - 1].getWords();
            trieList[height - 1] = new Trie<Boolean>(true, words);
        }
        return makePartialRectangle(length, height, new Rectangle(length));
    }

    /* This function recursively tries to form a rectangle with words
     * of length l from the dictionary as rows and words of length h
     * from the dictionary as columns. To do so, we start with an empty
     * rectangle and add in a word with length l as the first row. We
     * then check the trie of words of length h to see if each partial
     * column is a prefix of a word with length h. If so we branch
     * recursively and check the next word till we've formed a complete
     * rectangle. When we have a complete rectangle check if every
     * column is a word in the dictionary.
     */
    private static Rectangle makePartialRectangle(int l, int h, Rectangle rectangle) {
        // Check if we have formed a complete rectangle
        if (rectangle.height == h) {
            if (rectangle.isComplete(l, h, groupList[h - 1])) {
                return rectangle;
            } else {
                return null;
            }
        }

        // If the rectangle is not empty, validate that each column is a
        // substring of a word of length h in the dictionary using the
        // trie of words of length h.
        if (!rectangle.isPartialOK(l, trieList[h - 1])) {
            return null;
        }

        // For each word of length l, try to make a new rectangle by adding
        // the word to the existing rectangle.
        for (int i = 0; i < groupList[l-1].length(); i++) {
            Rectangle orgPlus = rectangle.append(groupList[l-1].getWord(i));
            Rectangle rect = makePartialRectangle(l, h, orgPlus);
            if (rect != null) return rect;
        }
        return null;
    }

    private void test(String dataFile, String ... expected) throws Exception {
        Rectangle r = maxMatrix(Files.readAllLines(Paths.get(dataFile))
                                .toArray(new String[0]));
        System.out.println(r);
        assertArrayEquals(expected, r.toStrings());
    }

    @Test
    public void test1() throws Exception {
        test("data/words1k", "care", "acid", "ring", "edge");
    }

    @Test
    public void test2() throws Exception {
        test("data/words5k", "staff", "trial", "aisle", "false", "fleet");
    }

    // @Test // (take too long time)
    public void test3() throws Exception {
        test("data/words70k", "staff", "trial", "aisle", "false", "fleet");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("WordMatrix");
    }
}
