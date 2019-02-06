import java.util.*;
import java.io.*;

// http://www.usaco.org/index.php?page=viewproblem2&cpid=862
// USACO 2018 December Contest, Gold Problem 2. - Cowpatibility
//
// It turns out there is one factor that matters far more than any other when
// determining whether two cows are compatible as potential friends: whether 
// they like similar flavors of ice cream!
// Farmer John's N cows (2≤N≤50,000) have each listed their five favorite
// flavors of ice cream. To make this list concise, each possible flavor is 
// represented by a positive integer ID at most 106. Two cows are compatible if 
// their lists contain at least one common flavor of ice cream.
// Please determine the number of pairs of cows that are NOT compatible.
// INPUT FORMAT (file cowpatibility.in):
// The first line of input contains N. Each of the following N lines 
// contain 5 integers (all different) representing the favorite ice cream
// flavors of one cow.
// OUTPUT FORMAT (file cowpatibility.out):
// Please output the number of pairs of cows that are not compatible.
public class Cowpatibility {
    // Principle of Inclusion and Exclusion
    // time complexity: O(N), space complexity: O(N)
    public static long solve(int[][] flavors) {
        long compatibles = 0;
        int n = flavors.length;
        for (int cnt = 1; cnt <= 5; cnt++) {
            Map<List<Integer>, Integer> count = new HashMap<>();
            for (int mask = 1; mask < 32; mask++) {
                if (Integer.bitCount(mask) != cnt) continue;

                for (int i = 0; i < n; i++) {
                    Integer[] tmp = new Integer[5];
                    for (int j = 0; j < 5; j++) {
                        tmp[j] = ((mask & (1 << j)) != 0) ? flavors[i][j] : 0;
                    }
                    Arrays.sort(tmp); // could alternatively use HashSet
                    List<Integer> list = Arrays.asList(tmp);
                    count.put(list, count.getOrDefault(list, 0) + 1);
                }
            }
            long total = 0;
            for (int i : count.values()) {
                total += i * (i - 1L) / 2;
            }
            compatibles += total * (cnt % 2 == 1 ? 1 : -1);
        }
        return n * (n - 1L) / 2 - compatibles;
    }

    public static void main0(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader("cowpatibility.in"));
        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("cowpatibility.out")));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.valueOf(st.nextToken());
        int[][] flavors = new int[n][5];
        for (int i = 0; i < n; i++) {
            flavors[i] = new int[5];
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < 5; j++) {
                flavors[i][j] = Integer.valueOf(st.nextToken());
            }
        }
        br.close();
        pw.println(solve(flavors));
        pw.close();
    }

    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        PrintStream out = System.out;
        int n = in.nextInt();
        int[][] flavors = new int[n][5];
        for (int i = 0; i < n; i++) {
            flavors[i] = new int[5];
            for (int j = 0; j < 5; j++) {
                flavors[i][j] = in.nextInt();
            }
        }
        out.println(solve(flavors));
        in.close();
    }
}
