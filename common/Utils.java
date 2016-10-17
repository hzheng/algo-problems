package common;

import java.lang.reflect.*;
import java.util.*;

public class Utils {
    public static int[][] clone(int[][] nums) {
        int[][] cloned = nums.clone();
        for (int i = 0; i < nums.length; i++) {
            cloned[i] = nums[i].clone();
        }
        return cloned;
    }

    public static class IntArrayComparator implements Comparator<int[]> {
        public int compare(int[] a, int[] b) {
            int len = Math.min(a.length, b.length);
            int i = 0;
            for (; i < len && (a[i] == b[i]); i++);
            return (i == len) ? a.length - b.length : a[i] - b[i];
        }
    }

    public static class StrListComparator implements Comparator<List<String>> {
        public int compare(List<String> a, List<String> b) {
            int len = Math.min(a.size(), b.size());
            int i = 0;
            for (; i < len && a.get(i).equals(b.get(i)); i++);
            return (i == len) ? a.size() - b.size() : a.get(i).compareTo(b.get(i));
        }
    }

    public static class StrArrayComparator implements Comparator<String[]> {
        public int compare(String[] a, String[] b) {
            int len = Math.min(a.length, b.length);
            int i = 0;
            for (; i < len && a[i].equals(b[i]); i++);
            return (i == len) ? a.length - b.length : a[i].compareTo(b[i]);
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
