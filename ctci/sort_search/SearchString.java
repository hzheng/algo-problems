import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Cracking the Coding Interview(5ed) Problem 11.5:
 * Given a sorted array of strings which is interspersed with empty strings,
 * find the location of a given string.
 */
public class SearchString {
    public static int search(String tgt, String[] arr) {
        if (arr == null || tgt == null) return -1;
        if (tgt.isEmpty()) {
            for (int i = 0; i < arr.length; i++) {
                if (arr[i].length() == 0) return i;
            }
            return -1;
        }

        return search(tgt, arr, 0, arr.length - 1);
    }

    private static int search(String tgt, String[] arr, int start, int end) {
        if (start > end) return -1;

        int mid = (start + end) / 2;
        int m;
        for (m = mid; m >= start && arr[m].isEmpty(); --m) {}
        if (m < start) return -1;

        int compared = tgt.compareTo(arr[m]);
        if (compared == 0) return m;

        return (compared < 0) ? search(tgt, arr, start, m - 1)
               : search(tgt, arr, mid + 1, end);
    }

    public static int search2(String tgt, String[] arr) {
        return search2(arr, tgt, 0, arr.length - 1);
    }

    private static int search2(String[] strings, String str, int first, int last) {
        if (first > last) { return -1; }

        int mid = (last + first) / 2;
        // NOTE: the following is uncessarily complex.
        // find closest non-empty string has no advantage in statistics
        /* If mid is empty, find closest non-empty string. */
        if (strings[mid].isEmpty()) {
            int left = mid - 1;
            int right = mid + 1;
            while (true) {
                if (left < first && right > last) {
                    return -1;
                } else if (right <= last && !strings[right].isEmpty()) {
                    mid = right;
                    break;
                } else if (left >= first && !strings[left].isEmpty()) {
                    mid = left;
                    break;
                }
                right++;
                left--;
            }
        }

        /* Check for string, and recurse if necessary */
        if (str.equals(strings[mid])) { // Found it!
            return mid;
        } else if (strings[mid].compareTo(str) < 0) { // Search right
            return search2(strings, str, mid + 1, last);
        } else { // Search left
            return search2(strings, str, first, mid - 1);
        }
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    private int test(Function<String, String[], Integer> search, String name,
                     String x, String[] arr) {
        long t1 = System.nanoTime();
        int n = search.apply(x, arr);
        System.out.format("%s: %.3f ms\n", name, (System.nanoTime() - t1) * 1e-6);
        return n;
    }

    private void test(String[] arr, String[] tgts) {
        for (String x : tgts) {
            int i1 = test(SearchString::search, "search", x, arr);
            int i2 = test(SearchString::search2, "search2", x, arr);

            if (i1 != i2) { // in case of multi-solutions
                assertTrue(i1 >= 0 && i2 >= 0);
                assertEquals(x, arr[i1]);
                assertEquals(x, arr[i2]);
            }
        }
    }

    @Test
    public void test1() {
        test(new String[] {
            "apple", "", "", "banana", "", "", "", "carrot", "duck", "", "",
            "eel", "", "flower"
        },
             new String[] {"ac", "apple", "flower", "duck", "eel"});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("SearchString");
    }
}
