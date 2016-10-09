package common;

import java.util.Comparator;

public class Utils {
    public static class IntArrayComparator implements Comparator<int[]> {
        public int compare(int[] a, int[] b) {
            int len = Math.min(a.length, b.length);
            int i = 0;
            for (; i < len && (a[i] == b[i]); i++);
            return (i == len) ? a.length - b.length : a[i] - b[i];
        }
    }
}
