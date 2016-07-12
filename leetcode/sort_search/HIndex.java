import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/h-index/
//
// Given an array of citations (each citation is a non-negative integer) of a
// researcher, write a function to compute the researcher's h-index.
// A scientist has index h if h of his/her N papers have at least h citations
// each, and the other N - h papers have no more than h citations each.
public class HIndex {
    // time complexity: O(N * log(N)), space complexity: O(1)
    // beats 32.05%(3 ms)
    public int hIndex(int[] citations) {
        Arrays.sort(citations);
        int n = citations.length;
        for (int i = n - 1; i >= 0; i--) {
            int citation = citations[i];
            if (citation < (n - i)) return n - i - 1;
        }
        return n;
    }

    // time complexity: O(N * log(N)), space complexity: O(N)
    // beats 4.29%(12 ms)
    public int hIndex2(int[] citations) {
        SortedMap<Integer, Integer> map = new TreeMap<>();
        for (int citation : citations) {
            map.put(citation, map.getOrDefault(citation, 0) + 1);
        }

        int papers = 0;
        while (!map.isEmpty()) {
            int citation = map.lastKey();
            int citationCount = map.get(citation);
            if (citation < papers) return papers;

            if (citation < papers + citationCount) return citation;

            papers += citationCount;
            map.remove(citation);
        }
        return papers;
    }

    // time complexity: O(N), space complexity: O(N)
    // beats 5.34%(8 ms)
    public int hIndex3(int[] citations) {
        Map<Integer, Integer> map = new HashMap<>();
        int n = citations.length;
        int max = 0;
        for (int citation : citations) {
            int key = Math.min(citation, n);
            map.put(key, map.getOrDefault(key, 0) + 1);
            max = Math.max(max, key);
        }

        int papers = 0;
        for (int i = max; i >= papers; i--) {
            if (map.containsKey(i)) {
                int citationCount = map.get(i);
                if (i < papers + citationCount) return i;

                papers += citationCount;
            }
        }
        return papers;
    }

    // counting sort
    // time complexity: O(N), space complexity: O(N)
    // beats 58.47%(1 ms)
    public int hIndex4(int[] citations) {
        int n = citations.length;
        int[] count = new int[n + 1];
        for (int citation : citations) {
            count[Math.min(citation, n)]++;
        }
        int papers = 0;
        for (int i = n; i >= papers; i--) {
            papers += count[i];
            if (i < papers) return i;
        }
        return papers;
    }

    // https://discuss.leetcode.com/topic/33102/o-n-time-o-1-space-solution/2
    // time complexity: O(N), space complexity: O(1)
    public int hIndex5(int[] citations) {
        int n = citations.length;
        // Here, we fill the input array with counts, where
        // (-citations[i] - 1) is exactly the number
        // of papers having i publications.
        // Negative because we need to distinguish it from
        // the citation counts that we haven't processed yet.
        // Note that we'll just throw away any counts >= citations.length
        for (int i = 0; i < n; ++i) {
            int count = citations[i];
            if (count < 0) continue; // already processed

            citations[i] = -1; // the count starts with 0
            for (int nextCount; count < n &&
                 (nextCount = citations[count]) >= 0; ) {
                // We haven't got enough space to count those
                // >= citations.length, but neither we need them.
                citations[count] = -2; // we've just seen one
                count = nextCount;
            }
            // The loop above could have terminated either
            // 1) because count >= citations.length (we don't count those) or
            // 2) because we hit an element that already stores a count.
            // In the second case we need to increment that count since
            // we've just encountered another element with the same value.
            if (count < n) {
                --citations[count];
            }
        }
        for (int h = 0, less = 0; h < n; ++h) {
            int count = -citations[h] - 1;
            // Logically, the loop below must have this condition:
            // citations.length - less >= h && less + count >= citations.length - h,
            // but the first of these is really redundant. Indeed, it is obviously
            // true on the first iteration, and it follows that if it was true for
            // some "h", then it would be true for "h + 1". Indeed, the "less" variable
            // on this iteration is what "less + count" was on the previous one, so
            // the (citations.length - less >= h) condition, in terms of
            // the previous-iteration values, is nothing but really
            // (citations.length - (less + count) >= h + 1),
            // which is exactly the same as (citations.length - (h + 1) >= (less + count))
            // or (citations.length - h > (less + count)), but if that was false, then
            // (less + count >= citations.length - h) would be true on the previous
            // iteration, and the whole thing would have terminated earlier.
            if (less + count >= n - h) return h;

            less += count;
        }
        return n;
    }

    void test(int expected, int ... citations) {
        assertEquals(expected, hIndex(citations));
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
