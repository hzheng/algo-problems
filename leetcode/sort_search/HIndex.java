import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC274: https://leetcode.com/problems/h-index/
//
// Given an array of citations (each citation is a non-negative integer) of a
// researcher, write a function to compute the researcher's h-index.
// A scientist has index h if h of his/her N papers have at least h citations
// each, and the other N - h papers have no more than h citations each.
public class HIndex {
    // Sort
    // time complexity: O(N * log(N)), space complexity: O(1)
    // beats 46.31%(2 ms for 81 tests)
    public int hIndex(int[] citations) {
        Arrays.sort(citations);
        int n = citations.length;
        for (int i = n - 1; i >= 0; i--) {
            int citation = citations[i];
            if (citation < (n - i)) return n - i - 1;
        }
        return n;
    }

    // Sort + Binary Search
    // time complexity: O(N * log(N)), space complexity: O(1)
    // beats 46.31%(2 ms for 81 tests)
    public int hIndex1(int[] citations) {
        Arrays.sort(citations);
        int n = citations.length;
        int low = 0;
        for (int high = n - 1; low <= high; ) {
            int mid = (low + high) >>> 1;
            if (citations[mid] < (n - mid)) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return n - low;
    }

    // SortedMap
    // time complexity: O(N * log(N)), space complexity: O(N)
    // beats 4.77%(10 ms for 81 tests)
    public int hIndex2(int[] citations) {
        SortedMap<Integer, Integer> map = new TreeMap<>();
        for (int citation : citations) {
            map.put(citation, map.getOrDefault(citation, 0) + 1);
        }
        for (int topCitedPapers = 0;; ) {
            int citation = map.isEmpty() ? -1 : map.lastKey();
            if (citation < topCitedPapers) return topCitedPapers;

            topCitedPapers += map.remove(citation);
            if (citation < topCitedPapers) return citation;
        }
    }

    // Hash Table
    // time complexity: O(N), space complexity: O(N)
    // beats 5.54%(8 ms for 81 tests)
    public int hIndex3(int[] citations) {
        Map<Integer, Integer> map = new HashMap<>();
        int n = citations.length;
        for (int citation : citations) {
            int key = Math.min(citation, n);
            map.put(key, map.getOrDefault(key, 0) + 1);
        }
        for (int topCitedPapers = 0; ; n--) {
            topCitedPapers += map.getOrDefault(n, 0);
            if (n <= topCitedPapers) return n;
        }
    }

    // Solution of Choice
    // Counting Sort
    // time complexity: O(N), space complexity: O(N)
    // beats 61.34%(1 ms for 81 tests)
    public int hIndex4(int[] citations) {
        int n = citations.length;
        int[] count = new int[n + 1];
        for (int citation : citations) {
            count[Math.min(citation, n)]++;
        }
        for (int topCitedPapers = 0; ; n--) {
            topCitedPapers += count[n];
            if (n <= topCitedPapers) return n;
        }
    }

    // Counting Sort
    // https://discuss.leetcode.com/topic/33102/o-n-time-o-1-space-solution/
    // time complexity: O(N), space complexity: O(1) (modified input)
    // beats 61.34%(1 ms for 81 tests)
    public int hIndex5(int[] citations) {
        int n = citations.length;
        // fill the input array with counts, where (-citations[i] - 1) is the
        // number of papers having i publications.
        for (int i = 0; i < n; ++i) {
            int count = citations[i];
            if (count < 0) continue; // already processed

            citations[i] = -1; // the count starts with 0
            for (int nextCount; count < n &&
                 (nextCount = citations[count]) >= 0; count = nextCount) {
                citations[count] = -2;
            }
            if (count < n) {
                citations[count]--;
            }
        }
        for (int i = 0, less = 0; i < n; i++) {
            less += (-citations[i] - 1);
            if (less >= n - i) return i;
        }
        return n;
    }

    void test(int expected, int ... citations) {
        assertEquals(expected, hIndex(citations));
        assertEquals(expected, hIndex1(citations));
        assertEquals(expected, hIndex2(citations));
        assertEquals(expected, hIndex3(citations));
        assertEquals(expected, hIndex4(citations));
        assertEquals(expected, hIndex5(citations));
    }

    @Test
    public void test1() {
        test(3, 0, 6, 1, 5, 3);
        test(4, 5, 5, 5, 5);
        test(0, 0, 0);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("HIndex");
    }
}
