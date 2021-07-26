import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

import static java.util.stream.Collectors.toList;

import org.junit.Test;

import static org.junit.Assert.*;

// Given lists of starting points, ending points and styles, we apply it in order to a text.
// Each selection or change style (bold, italic, underline) is counted 1 operation.
// Find the minimum operations to get the same effect.
// All operations are idempotent.
//
// Constraints:
// 1 <= starting, ending <= 10^9
// 1 <= starting.length == ending.length == style.length <= 10^5
class Result {
    /*
     * Complete the 'textFormatting' function below.
     *
     * The function is expected to return an INTEGER.
     * The function accepts following parameters:
     *  1. INTEGER_ARRAY starting
     *  2. INTEGER_ARRAY ending
     *  3. CHARACTER_ARRAY style
     */
    public static int textFormatting(List<Integer> starting, List<Integer> ending,
                                     List<Character> style) {
        int n = starting.size();
        TreeSet<Integer> points = new TreeSet<>();
        for (int i = 0; i < n; i++) {
            points.add(starting.get(i));
            points.add(ending.get(i));
        }
        // map endpoints to their orders (to compact the intervals) without changing result
        Map<Integer, Integer> map = new HashMap<>();
        int pointCount = points.size();
        int prev = points.pollFirst();
        map.put(prev, 0);
        int size = 1;
        for (int i = 1; i < pointCount; i++, size++) {
            int cur = points.pollFirst();
            // if there is gap between current and previous, increase order to reflect this gap
            if (cur - prev > 1) {
                size++;
            }
            map.put(cur, size);
            prev = cur;
        }
        Map<Integer, List<Integer>> events = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int start = map.get(starting.get(i));
            int end = map.get(ending.get(i));
            int s = convertStyleIndex(style.get(i));
            addEvent(start, s, events);
            addEvent(end + 1, -s, events);
        }
        int[] text = new int[++size];
        int[] cumStyle = new int[3]; // cumulative b, i, u styles
        for (int i = 0; i < text.length; i++) {
            List<Integer> event = events.get(i);
            if (event == null) {
                if (i > 0) {
                    text[i] = text[i - 1];
                }
                continue;
            }
            for (int s : event) {
                if (s > 0) {
                    cumStyle[s - 1]++;
                } else {
                    cumStyle[-s - 1]--;
                }
            }
            int val = 0;
            for (int k = 0; k < cumStyle.length; k++) {
                if (cumStyle[k] > 0) { // has a style
                    val |= (1 << k); // bitmask of a given style
                }
            }
            text[i] = val;
        }
        //System.out.println(Arrays.toString(text));
        int res = 0;
        for (int i = 0; i < text.length; i++) {
            int commonStyles = 7; // mask of styles
            int j = i;
            for (; j < text.length; j++) {
                if ((commonStyles & text[j]) == 0) { break; }
                commonStyles &= text[j];
            }
            if (j == i) { continue; }

            res += 1 + Integer.bitCount(commonStyles); // 1 selection + style changes
            for (int k = i; k < j; k++) {
                text[k] &= ~commonStyles; // remove styles
            }
            i--;
        }
        return res;
    }

    private static void addEvent(int point, int style, Map<Integer, List<Integer>> events) {
        // postive style means adding a style, negative means removing a style
        events.computeIfAbsent(point, x -> new ArrayList<>()).add(style);
    }

    private static int convertStyleIndex(char c) {
        switch (c) {
        case 'b':
            return 1;
        case 'i':
            return 2;
        case 'u':
            return 3;
        default:
            return 0;
        }
    }
}

public class TextFormatting {
    void test(Integer[] starts, Integer[] ends, String styles, int expected) {
        List<Integer> starting = Arrays.asList(starts);
        List<Integer> ending = Arrays.asList(ends);
        List<Character> style = styles.chars().mapToObj(x -> (char)x).collect(Collectors.toList());
        assertEquals(expected, Result.textFormatting(starting, ending, style));
    }

    @Test public void test() {
        test(new Integer[] {3}, new Integer[] {4}, "b", 2);
        test(new Integer[] {3, 2, 1}, new Integer[] {4, 5, 2}, "biu", 6);
        test(new Integer[] {3, 2, 1, 1}, new Integer[] {4, 5, 2, 3}, "biui", 6);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }

    @Test public void test2() throws IOException {
        //    public static void main(String[] args) throws IOException {
        //        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        //        BufferedWriter bufferedWriter =
        //                new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getName();
        BufferedReader bufferedReader = Files.newBufferedReader(Paths.get("data/" + clazz));

        int startingCount = Integer.parseInt(bufferedReader.readLine().trim());

        List<Integer> starting = IntStream.range(0, startingCount).mapToObj(i -> {
            try {
                return bufferedReader.readLine().replaceAll("\\s+$", "");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }).map(String::trim).map(Integer::parseInt).collect(toList());

        int endingCount = Integer.parseInt(bufferedReader.readLine().trim());

        List<Integer> ending = IntStream.range(0, endingCount).mapToObj(i -> {
            try {
                return bufferedReader.readLine().replaceAll("\\s+$", "");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }).map(String::trim).map(Integer::parseInt).collect(toList());

        int styleCount = Integer.parseInt(bufferedReader.readLine().trim());

        List<Character> style = IntStream.range(0, styleCount).mapToObj(i -> {
            try {
                return bufferedReader.readLine();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }).map(e -> e.charAt(0)).collect(toList());

        int expected = Integer.parseInt(bufferedReader.readLine());
        int result = Result.textFormatting(starting, ending, style);
        assertEquals(expected, result);

        //        bufferedWriter.write(String.valueOf(result));
        //        bufferedWriter.newLine();

        bufferedReader.close();
        //        bufferedWriter.close();
    }
}

