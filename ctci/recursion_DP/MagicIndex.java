/**
 * Cracking the Coding Interview(5ed) Problem 9.3:
 * A magic index in an array A[1...n-1] is defined to be an index such that
 * A[i] = i. Given a sorted array of distinct integers, write a method to find
 * a magic index, if one exists, in array A.
 *  FOLLOW UP: What if the values are not distinct?
 */
public class MagicIndex {
    public static int getIndexDistinct(int[] array) {
        return getIndex(array, 0, array.length - 1);
    }

    // distinct: time complexity: O(log(N)), space complexity: O(log(N))
    private static int getIndexDistinct(int[] array, int start, int end) {
        if (start > end) return -1;

        int mid = (start + end) / 2;
        if (array[mid] == mid) return mid;

        if (array[mid] < mid) {
            return getIndexDistinct(array, mid + 1, end);
        } else {
            return getIndexDistinct(array, start, mid - 1);
        }
    }

    // non-distinct(not good)
    private static int getIndex(int[] array, int start, int end) {
        if (start > end) return -1;

        int mid = (start + end) / 2;
        int indexDiff = (end - start) / 2;
        int diff = (array[mid] - mid);
        if (diff == 0) return mid;

        if (diff > indexDiff) {
            return getIndexDistinct(array, mid + 1, end);
        }
        if (diff < -indexDiff) {
            return getIndexDistinct(array, start, mid - 1);
        }
        int index = getIndexDistinct(array, start, mid -1);
        return (index < 0) ? getIndexDistinct(array, mid + 1, end) : index;
    }

    // non-distinct(adapt from the book)
    private static int getIndex2(int[] array, int start, int end) {
        if (start > end) return -1;

        int mid = (start + end) / 2;
        int midVal = array[mid];
        if (mid == midVal) return mid;

        int leftEnd = Math.min(mid - 1, midVal);
        int index = getIndexDistinct(array, start, leftEnd);
        if (index >= 0) return index;

        int rightStart = Math.max(mid + 1, midVal);
        return getIndexDistinct(array, rightStart, end);
    }

    public static void main(String[] args) {
        // org.junit.runner.JUnitCore.main("MagicIndex");
    }
}
