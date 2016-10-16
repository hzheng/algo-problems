package common;

import java.lang.reflect.*;
import java.util.*;

public class Utils {
    public static class IntArrayComparator implements Comparator<int[]> {
        public int compare(int[] a, int[] b) {
            int len = Math.min(a.length, b.length);
            int i = 0;
            for (; i < len && (a[i] == b[i]); i++);
            return (i == len) ? a.length - b.length : a[i] - b[i];
        }
    }

    public static String[][] toStrArray(List<List<String> > listOfList) {
        return toArray(listOfList, String.class);
    }

    public static Integer[][] toIntArray(List<List<Integer> > listOfList) {
        return toArray(listOfList, Integer.class);
    }

    @SuppressWarnings("unchecked")
    public static <T> T[][] toArray(List<List<T>> listOfList, Class<T> clazz) {
        int size = listOfList.size();
        T[][] array = (T[][])Array.newInstance(clazz, size, 0);
        for (int i = 0; i < size; i++) {
            List<T> list = listOfList.get(i);
            if (list != null) {
                array[i] = list.toArray((T[])Array.newInstance(clazz, list.size()));
            }
        }
        return array;
    }
}
