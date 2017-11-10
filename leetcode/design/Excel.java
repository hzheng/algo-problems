import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC631: https://leetcode.com/problems/design-excel-sum-formula/
//
// Design the basic function of Excel and implement the function of sum formula:
//
// Excel(int H, char W): This is the constructor. The inputs represents the
// height and width of the Excel form. H is a positive integer, range from 1 to
// 26. It represents the height. W is a character range from 'A' to 'Z'.
// It represents that the width is the number of characters from 'A' to W. The
//  Excel form content is represented by a height * width 2D integer array C,
// it should be initialized to zero. Assume the first row of C starts from 1,
// and the first column of C starts from 'A'.
//
// void Set(int row, char column, int val): Change the value at C(row, column)
// to be val.
//
// int Get(int row, char column): Return the value at C(row, column).
//
// int Sum(int row, char column, List of Strings : numbers): Calculate and set
// the value at C(row, column), where the value should be the sum of cells
// represented by numbers. This function return the sum result at C(row, column).
// This sum formula should exist until this cell is overlapped by another value
// or another sum formula.
// numbers is a list of strings that each string represent a cell or a range of
// cells. If the string represent a single cell, then it has the following
// format : ColRow. For example, "F7" represents the cell at (7, F).
//
// If the string represent a range of cells, then it has the following format :
// ColRow1:ColRow2. The range will always be a rectangle, and ColRow1 represent
// the position of the top-left cell, and ColRow2 represents the position of the
// bottom-right cell.
// Note:
// Assume there won't be any circular sum reference. For example, A1 = sum(B1)
// and B1 = sum(A1).
// The test cases are using double-quotes to represent a character.
public class Excel {
    static interface IExcel {
        public void set(int r, char c, int v);

        public int get(int r, char c);

        public int sum(int r, char c, String[] strs);
    }

    // Topological Sort + Recursion + Hash Table + Stack
    // beats 63.21%(113 ms for 20 tests)
    static class Excel1 implements IExcel {
        private class Cell {
            private Map<String, Integer> links;
            private int val;

            public Cell(int val, Map<String, Integer> links) {
                this.val = val;
                this.links = links;
            }

            public boolean contains(int r, int c) {
                return links != null && links.containsKey(
                    String.valueOf((char)('A' + c)) + (r + 1));
            }

            public int sum() {
                if (links == null) return 0;

                int sum = 0;
                for (String key : links.keySet()) {
                    int i = Integer.valueOf(key.substring(1)) - 1;
                    int j = key.charAt(0) - 'A';
                    int index = i * W + j;
                    sum += ((cells[index] == null)
                            ? 0 : cells[index].val) * links.get(key);
                }
                return val = sum;
            }
        }

        private final int H;
        private final int W;

        private Cell[] cells;
        private ArrayDeque<Integer> stack = new ArrayDeque<>();

        public Excel1(int H, char W) {
            this.H = H;
            this.W = W - 'A' + 1;
            cells = new Cell[H * W];
        }

        // time complexity: O(1)
        public int get(int r, char c) {
            Cell cell = cells[(r - 1) * W + c - 'A'];
            return (cell == null) ? 0 : cell.val;
        }

        // time complexity: O((r * c) ^ 2)
        public void set(int r, char c, int v) {
            cells[(r - 1) * W + c - 'A'] = new Cell(v, null);
            topologicalSort(r - 1, c - 'A');
            while (!stack.isEmpty()) {
                cells[stack.pop()].sum();
            }
        }

        private void topologicalSort(int r, int c) {
            for (int i = 0; i < H; i++) {
                for (int j = 0; j < W; j++) {
                    int index = i * W + j;
                    if (cells[index] != null && cells[index].contains(r, c)) {
                        topologicalSort(i, j);
                    }
                }
            }
            stack.push(r * W + c);
        }

        // time complexity: O((r * c) ^ 2 + r * c * l)
        public int sum(int r, char c, String[] strs) {
            Cell cell = new Cell(0, convert(strs));
            int index = (r - 1) * W + c - 'A';
            cells[index] = cell;
            int sum = cell.sum();
            set(r, c, sum);
            cells[index] = cell; // restore links
            return sum;
        }

        private Map<String, Integer> convert(String[] strs) {
            Map<String, Integer> links = new HashMap<>();
            for (String s : strs) {
                if (!s.contains(":")) {
                    links.put(s, links.getOrDefault(s, 0) + 1);
                    continue;
                }
                String[] corners = s.split(":");
                int startX = Integer.valueOf(corners[0].substring(1));
                int endX = Integer.valueOf(corners[1].substring(1));
                char startY = corners[0].charAt(0);
                char endY = corners[1].charAt(0);
                for (int i = startX; i <= endX; i++) {
                    for (char j = startY; j <= endY; j++) {
                        String key = String.valueOf(j) + i;
                        links.put(key, links.getOrDefault(key, 0) + 1);
                    }
                }
            }
            return links;
        }
    }

    // DFS + Recursion + Hash Table
    // beats 37.74%(118 ms for 20 tests)
    static class Excel2 implements IExcel {
        private class Cell {
            private int val = 0;
            private Map<Cell, Integer> links = new HashMap<>();

            public Cell(int val) {
                setValue(val);
            }

            public Cell(String[] formulas) {
                setFormula(formulas);
            }

            public void setValue(int val) {
                links.clear();
                this.val = val;
            }

            public void setFormula(String[] formulas) {
                links.clear();
                for (String s : formulas) {
                    if (s.indexOf(":") < 0) {
                        int[] pos = getPos(s);
                        addFormulaCell(pos[0], pos[1]);
                    } else {
                        String[] pos = s.split(":");
                        int[] start = getPos(pos[0]);
                        int[] end = getPos(pos[1]);
                        for (int r = start[0]; r <= end[0]; r++) {
                            for (int c = start[1]; c <= end[1]; c++) {
                                addFormulaCell(r, c);
                            }
                        }
                    }
                }
            }

            private int[] getPos(String str) {
                return new int[] { Integer.valueOf(str.substring(1)),
                                   str.charAt(0) - 'A' };
            }

            private void addFormulaCell(int r, int c) {
                if (table[r][c] == null) {
                    table[r][c] = new Cell(0);
                }
                Cell cell = table[r][c];
                links.put(cell, links.getOrDefault(cell, 0) + 1);
            }

            public int getValue() { // TODO: cache value by adding dirty flag
                if (links.isEmpty()) return val;

                int sum = 0;
                for (Cell cell : links.keySet()) {
                    sum += cell.getValue() * links.get(cell);
                }
                return sum;
            }
        }

        private Cell[][] table;

        public Excel2(int H, char W) {
            table = new Cell[H + 1][W - 'A' + 1];
        }

        // time complexity: O(1)
        public void set(int r, char c, int v) {
            if (table[r][c - 'A'] == null) {
                table[r][c - 'A'] = new Cell(v);
            } else {
                table[r][c - 'A'].setValue(v);
            }
        }

        // time complexity: O((r * c) ^ 2)
        public int get(int r, char c) {
            return (table[r][c - 'A'] == null)
                   ? 0 : table[r][c - 'A'].getValue();
        }

        // time complexity: O((r * c) ^ 2 + r * c * l)
        public int sum(int r, char c, String[] strs) {
            if (table[r][c - 'A'] == null) {
                table[r][c - 'A'] = new Cell(strs);
            } else {
                table[r][c - 'A'].setFormula(strs);
            }
            return table[r][c - 'A'].getValue();
        }
    }

    // TODO: iterative solution

    void test1(IExcel obj) {
        obj.set(1, 'A', 2);
        obj.sum(3, 'C', new String[] {"A1", "A1:B2"});
        obj.set(2, 'B', 2);
        assertEquals(2, obj.get(1, 'A'));
        assertEquals(2, obj.get(2, 'B'));
        assertEquals(6, obj.get(3, 'C'));
    }

    void test2(IExcel obj) {
        obj.set(1, 'A', 1);
        assertEquals(1, obj.sum(2, 'B', new String[] {"A1"}));
        obj.set(2, 'B', 0);
        assertEquals(0, obj.get(2, 'B'));
    }

    void test3(IExcel obj) {
        obj.set(1, 'A', 5);
        obj.set(1, 'B', 3);
        obj.set(1, 'C', 2);
        assertEquals(13, obj.sum(1, 'C', new String[] {"A1", "A1:B1"}));
        assertEquals(13, obj.get(1, 'C'));
        obj.set(1, 'B', 5);
        assertEquals(15, obj.get(1, 'C'));
        assertEquals(5, obj.sum(1, 'B', new String[] {"A1:A5"}));
        obj.set(5, 'A', 10);
        assertEquals(15, obj.get(1, 'B'));
        assertEquals(25, obj.get(1, 'C'));
        assertEquals(60, obj.sum(3, 'C', new String[] {"A1:C1", "A1:A5"}));
        obj.set(3, 'A', 3);
        assertEquals(18, obj.get(1, 'B'));
        assertEquals(28, obj.get(1, 'C'));
        assertEquals(69, obj.get(3, 'C'));
        assertEquals(10, obj.get(5, 'A'));
    }

    @Test
    public void test1() {
        int h = 3;
        char w = 'C';
        test1(new Excel1(h, w));
        test1(new Excel2(h, w));
    }

    @Test
    public void test2() {
        int h = 5;
        char w = 'E';
        test2(new Excel1(h, w));
        test2(new Excel2(h, w));
    }

    @Test
    public void test3() {
        int h = 5;
        char w = 'E';
        test3(new Excel1(h, w));
        test3(new Excel2(h, w));
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
