package common;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Utils {
    public static int[][] clone(int[][] nums) {
        int[][] cloned = nums.clone();
        for (int i = 0; i < nums.length; i++) {
            cloned[i] = nums[i].clone();
        }
        return cloned;
    }

    public static char[][] clone(char[][] nums) {
        char[][] cloned = nums.clone();
        for (int i = 0; i < nums.length; i++) {
            cloned[i] = nums[i].clone();
        }
        return cloned;
    }

    public static <T> T[][] clone(T[][] items) {
        T[][] cloned = items.clone();
        for (int i = 0; i < items.length; i++) {
            cloned[i] = items[i].clone();
        }
        return cloned;
    }

    public static <T> List<List<T>> toList(T[][] items) {
        List<List<T>> res = new ArrayList<>();
        for (T[] item : items) {
            res.add(Arrays.asList(item));
        }
        return res;
    }

    public static class IntArrayComparator implements Comparator<int[]> {
        public int compare(int[] a, int[] b) {
            int len = Math.min(a.length, b.length);
            int i = 0;
            for (; i < len && (a[i] == b[i]); i++) {}
            return (i == len) ? a.length - b.length : a[i] - b[i];
        }
    }

    public static class StrListComparator implements Comparator<List<String>> {
        public int compare(List<String> a, List<String> b) {
            int len = Math.min(a.size(), b.size());
            int i = 0;
            for (; i < len && a.get(i).equals(b.get(i)); i++) {}
            return (i == len) ? a.size() - b.size() : a.get(i).compareTo(b.get(i));
        }
    }

    public static class StrArrayComparator implements Comparator<String[]> {
        public int compare(String[] a, String[] b) {
            int len = Math.min(a.length, b.length);
            int i = 0;
            for (; i < len && a[i].equals(b[i]); i++) {}
            return (i == len) ? a.length - b.length : a[i].compareTo(b[i]);
        }
    }

    public static String[][] toStrArray(List<List<String>> listOfList) {
        return toArray(listOfList, String.class);
    }

    public static Integer[][] toIntArray(List<List<Integer>> listOfList) {
        return toArray(listOfList, Integer.class);
    }

    public static int[] toArray(List<Integer> list) {
        return list.stream().mapToInt(i -> i).toArray();
    }

    public static int[][] toInts(List<List<Integer>> listOfList) {
        return listOfList.stream().map(i -> toArray(i)).toArray(int[][]::new);
    }

    public static Integer[] toIntegerArray(int[] arr) {
        return IntStream.of(arr).boxed().toArray(Integer[]::new);
    }

    public static Integer[][] toIntegerArray(int[][] arr) {
        return Stream.of(arr).map(Utils::toIntegerArray).toArray(Integer[][]::new);
    }

    public static int[][] sort(int[][] arrays) {
        Arrays.sort(arrays, new IntArrayComparator());
        return arrays;
    }

    public static int[][] toSortedInts(List<List<Integer>> listOfList) {
        int[][] res = listOfList.stream().map(i -> toArray(i)).toArray(int[][]::new);
        return sort(res);
    }

    public static String[][] sort(String[][] arrays) {
        Arrays.sort(arrays, new StrArrayComparator());
        return arrays;
    }

    public static String[][] toSortedStrs(List<List<String>> listOfList) {
        String[][] res = toStrArray(listOfList);
        return sort(res);
    }

    @SuppressWarnings("unchecked") public static <T> T[][] toArray(List<List<T>> listOfList,
                                                                   Class<T> clazz) {
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

    public static int[] readIntArray(String str) {
        String[] arr = str.strip().split("[\\[,\\]]");
        int[] res = new int[arr.length];
        int i = 0;
        for (String a : arr) {
            a = a.strip();
            if (!a.isEmpty()) {
                res[i++] = Integer.parseInt(a);
            }
        }
        return Arrays.copyOfRange(res, 0, i);
    }

    public static int[][] readInt2Array(String str) {
        str = str.strip();
        if (str.equals("[]")) { return new int[0][0]; }

        String[] arr = str.split(",(?= *\\[)");
        int[][] res = new int[arr.length][];
        int i = 0;
        for (String a : arr) {
            res[i++] = readIntArray(a);
        }
        return res;
    }

    public static String[] readStrArray(String str) {
        str = str.strip();
        if (str.equals("[]")) { return new String[0]; }

        String[] res = str.split("\",\"");
        res[0] = res[0].substring(2);
        int last = res.length - 1;
        if (last > 0) {
            res[last] = res[last].substring(0, res[last].length() - 2);
        }
        return res;
    }

    public static Object[][] readObj2Array(String str) {
        str = str.strip();
        if (str.equals("[]")) { return new Object[0][0]; }

        String[] arr = str.split(",(?= *\\[)");
        Object[][] res = new Object[arr.length][];
        int i = 0;
        for (String a : arr) {
            res[i++] = readObjArray(a);
        }
        return res;
    }

    public static Object[] readObjArray(String str) {
        String[] arr = str.strip().split("[\\[,\\]]");
        Object[] res = new Object[arr.length];
        int i = 0;
        String NULL = "" + null;
        String TRUE = Boolean.TRUE.toString();
        String FALSE = Boolean.FALSE.toString();
        for (String a : arr) {
            a = a.strip();
            if (a.isEmpty()) { continue; }

            if (a.startsWith("\"")) {
                res[i++] = a.substring(1, a.length() - 1);
            } else if (a.startsWith("'")) {
                res[i++] = a.charAt(1);
            } else if (TRUE.equals(a)) {
                res[i++] = true;
            } else if (FALSE.equals(a)) {
                res[i++] = false;
            } else if (NULL.equals(a)) {
                res[i++] = null;
            } else if (a.contains(".")) {
                res[i++] = Double.parseDouble(a);
            } else {
                res[i++] = Integer.parseInt(a);
            }
        }
        return Arrays.copyOfRange(res, 0, i);
    }
}
