import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC296: https://leetcode.com/problems/best-meeting-point/
//
// A group of two or more people wants to meet and minimize the total travel
// distance. You are given a 2D grid of values 0 or 1, where each 1 marks the
// home of someone in the group. The distance is calculated using Manhattan Distance,
// where distance(p1, p2) = |p2.x - p1.x| + |p2.y - p1.y|.
public class BestMeetingPoint {
    // Binary Search
    // time complexity: O(M ^ 2 * N * log(N), space complexity: O(M * N)
    // beats 23.04%(17 ms for 57 tests)
    public int minTotalDistance(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int[] totalPerRow = new int[m];
        int[] totalPerCol = new int[n];
        int[][] rowPeople = new int[m][n];
        int[][] colPeople = new int[n][m];
        int[] colEnds = new int[n];
        int min = 0;
        for (int i = 0, col = 0; i < m; i++) {
            col = 0;
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    totalPerRow[i]++;
                    totalPerCol[j]++;
                    rowPeople[i][col++] = j;
                    colPeople[j][colEnds[j]++] = i;
                    min += i + j;
                }
            }
        }
        for (int i = 0, firstCol = min; i < m; i++) {
            int cur = firstCol;
            if (i > 0) {
                int diff = 0;
                for (int j = 0; j < n; j++) {
                    diff += totalPerCol[j] - countBigs(colPeople[j], totalPerCol[j], i) * 2;
                }
                if (diff >= 0) break;

                min = Math.min(cur += diff, min);
                firstCol = cur;
            }
            for (int j = 1; j < n; j++) {
                int diff = 0;
                for (int k = 0; k < m; k++) {
                    diff += totalPerRow[k] - countBigs(rowPeople[k], totalPerRow[k], j) * 2;
                }
                if (diff >= 0) break;

                min = Math.min(cur += diff, min);
            }
        }
        return min;
    }

    private int countBigs(int[] nums, int end, int target) {
        int index = Arrays.binarySearch(nums, 0, end, target);
        return end - (index >= 0 ? index : (-index - 1));
    }

    // time complexity: O(M * N), space complexity: O(1)
    // beats 84.56%(2 ms for 57 tests)
    public int minTotalDistance2(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int total = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    total++;
                }
            }
        }
        int minRow = 0;
        for (int count = 0; minRow < m; minRow++) {
            for (int j = 0; j < n; j++) {
                if (grid[minRow][j] == 1) {
                    count++;
                }
            }
            if (count * 2 > total) break;
        }
        int minCol = 0;
        for (int count = 0; minCol < n; minCol++) {
            for (int j = 0; j < m; j++) {
                if (grid[j][minCol] == 1) {
                    count++;
                }
            }
            if (count * 2 > total) break;
        }
        int min = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    min += Math.abs(i - minRow) + Math.abs(j - minCol);
                }
            }
        }
        return min;
    }

    // Sort
    // time complexity: O(M * N * log(M * N)), space complexity: O(M * N)
    // beats 40.27%(14 ms for 57 tests)
    public int minTotalDistance3(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        List<Integer> rows = new ArrayList<>();
        List<Integer> cols = new ArrayList<>();
        for (int row = 0; row < m; row++) {
            for (int col = 0; col < n; col++) {
                if (grid[row][col] == 1) {
                    rows.add(row);
                    cols.add(col);
                }
            }
        }
        int row = rows.get(rows.size() / 2);
        Collections.sort(cols);
        int col = cols.get(cols.size() / 2);
        return measure(rows, row) + measure(cols, col);
    }

    private int measure(List<Integer> points, int origin) {
        int distance = 0;
        for (int point : points) {
            distance += Math.abs(point - origin);
        }
        return distance;
    }

    // time complexity: O(M * N), space complexity: O(M * N)
    // beats 69.57%(8 ms for 57 tests)
    public int minTotalDistance4(int[][] grid) {
        List<Integer> rows = new ArrayList<>();
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                if (grid[row][col] == 1) {
                    rows.add(row);
                }
            }
        }
        List<Integer> cols = new ArrayList<>();
        for (int col = 0; col < grid[0].length; col++) {
            for (int row = 0; row < grid.length; row++) {
                if (grid[row][col] == 1) {
                    cols.add(col);
                }
            }
        }
        int row = rows.get(rows.size() / 2);
        int col = cols.get(cols.size() / 2);
        return measure(rows, row) + measure(cols, col);
    }

    // Sort + Two Pointers
    // time complexity: O(M * N * log(M * N)), space complexity: O(M * N)
    // beats 32.44%(15 ms for 57 tests)
    public int minTotalDistance5(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        List<Integer> rows = new ArrayList<>(m);
        List<Integer> cols = new ArrayList<>(n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    rows.add(i);
                    cols.add(j);
                }
            }
        }
        return minDistance(rows) + minDistance(cols);
    }

    private int minDistance(List<Integer> list) {
        int res = 0;
        Collections.sort(list);
        for (int i = 0, j = list.size() - 1; i < j; i++, j--) {
            res += list.get(j) - list.get(i);
        }
        return res;
    }

    // time complexity: O(M * N), space complexity: O(M + N)
    // beats 84.56%(2 ms for 57 tests)
    public int minTotalDistance6(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int[] rows = new int[m];
        int[] cols = new int[n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    rows[i]++;
                    cols[j]++;
                }
            }
        }
        int total = 0;
        for (int[] lines : new int[][] {rows, cols}) {
            for (int i = 0, j = lines.length - 1; i < j; ) {
                int pairs = Math.min(lines[i], lines[j]);
                total += pairs * (j - i);
                if ((lines[i] -= pairs) == 0) {
                    i++;
                }
                if ((lines[j] -= pairs) == 0) {
                    j--;
                }
            }
        }
        return total;
    }

    void test(int[][] grid, int expected) {
        assertEquals(expected, minTotalDistance(grid));
        assertEquals(expected, minTotalDistance2(grid));
        assertEquals(expected, minTotalDistance3(grid));
        assertEquals(expected, minTotalDistance4(grid));
        assertEquals(expected, minTotalDistance5(grid));
        assertEquals(expected, minTotalDistance6(grid));
    }

    @Test
    public void test() {
        test(new int[][] {{1, 1}}, 1);
        test(new int[][] {{1}, {1}}, 1);
        test(new int[][] {{0, 0, 0}, {1, 1, 1}}, 2);
        test(new int[][] {{0,0,0,1,1,0,0}, {0,1,0,0,1,0,0},
                          {1,0,0,0,1,0,0}, {0,1,0,0,1,0,0}, {1,0,0,0,1,1,0}, {0,0,1,1,0,0,0},
                          {0,1,0,1,0,0,1}, {0,0,1,0,1,1,0}, {0,0,0,1,1,1,1}}, 84);
        test(new int[][] {{1, 0, 0, 0, 1}, {0, 0, 0, 0, 0}, {0, 0, 1, 0, 0}}, 6);
        test(new int[][] {{0,0,1,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0},
                          {1,0,0,0,0,0,0,0,0,1,0,0,1,1,0,0,0,0,0},
                          {0,0,0,1,0,0,0,1,0,0,0,0,0,0,1,1,1,0,0},
                          {1,0,0,0,1,1,1,1,0,0,1,0,0,1,0,0,0,0,0},
                          {1,1,1,0,1,1,0,0,1,0,0,0,1,0,0,0,1,0,0},
                          {0,0,0,1,1,1,0,1,0,0,0,0,1,1,0,0,1,0,0}}, 193);
        test(new int[][] {{0, 0, 0, 0, 1, 0, 1, 0}, {0, 0, 0, 0, 1, 0, 0, 1}}, 7);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("BestMeetingPoint");
    }
}
