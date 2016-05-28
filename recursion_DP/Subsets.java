import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Cracking the Coding Interview(5ed) Problem 9.4:
 * Write a method to return all subsets of a set.
 */
public class Subsets {
    public static List<List<Integer> > getSubsets(List<Integer> set) {
        return getSubsets(set, set.size() - 1);
    }

    private static List<List<Integer> > getSubsets(List<Integer> set, int index) {
        if (index == -1) {
            List<List<Integer> > emptySet = new ArrayList<>();
            emptySet.add(new ArrayList<Integer>());
            return emptySet;
        }

        List<List<Integer> > subsets = getSubsets(set, index - 1);
        List<List<Integer> > moreSubsets = new ArrayList<>();
        Integer newElement = set.get(index);
        for (List<Integer> subset : subsets) {
            List<Integer> cloned = new ArrayList<>(subset);
            cloned.add(newElement);
            moreSubsets.add(cloned);
        }
        subsets.addAll(moreSubsets);
        return subsets;
    }

    // From the book
    public static List<List<Integer> > getSubsets2(List<Integer> set) {
        return getSubsets2(set, 0);
    }

    private static List<List<Integer> > getSubsets2(List<Integer> set, int index) {
        List<List<Integer> > allsubsets;
        if (set.size() == index) { // Base case - add empty set
            allsubsets = new ArrayList<>();
            allsubsets.add(new ArrayList<>());
        } else {
            allsubsets = getSubsets2(set, index + 1);
            int item = set.get(index);
            List<List<Integer> > moresubsets = new ArrayList<>();
            for (List<Integer> subset : allsubsets) {
                List<Integer> newsubset = new ArrayList<>();
                newsubset.addAll(subset); //
                newsubset.add(item);
                moresubsets.add(newsubset);
            }
            allsubsets.addAll(moresubsets);
        }
        return allsubsets;
    }

    public static List<List<Integer> > getSubsets3(List<Integer> set) {
        List<List<Integer> > subsets = new ArrayList<>();
        int n = set.size();
        for (int i = ((1 << n) - 1); i >= 0; i--) {
            List<Integer> subset = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                if ((i & (1 << j)) > 0) {
                    subset.add(set.get(j));
                }
            }
            subsets.add(subset);
        }
        return subsets;
    }

    // From the book
    public static List<Integer> convertIntToSet(int x, List<Integer> set) {
        List<Integer> subset = new ArrayList<Integer>();
        int index = 0;
        for (int k = x; k > 0; k >>= 1) {
            if ((k & 1) == 1) {
                subset.add(set.get(index));
            }
            index++;
        }
        return subset;
    }

    public static List<List<Integer> > getSubsets4(List<Integer> set) {
        List<List<Integer> > allsubsets = new ArrayList<>();
        int max = 1 << set.size();
        for (int k = 0; k < max; k++) {
            List<Integer> subset = convertIntToSet(k, set);
            allsubsets.add(subset);
        }
        return allsubsets;
    }

    private void test(int n,
                      Function<List<Integer>, List<List<Integer>>> getSubset) {
        List<Integer> set = new ArrayList<Integer>();
        for (int i = 1; i <= n; i++) {
            set.add(i);
        }

        long t = System.nanoTime();
        List<List<Integer> > subsets = getSubset.apply(set);
        System.out.format("%.03f ms\n", (System.nanoTime() - t) * 1e-6);
        if (n < 5) {
            for (List<Integer> subset : subsets) {
                System.out.println(subset);
            }
        }
        assertEquals((1 << n), subsets.size());
    }

    @Test
    public void test() {
        // int n = 18;
        int n = 3;
        System.out.println("test getSubsets");
        test(n, Subsets::getSubsets);
        System.out.println("test getSubsets2");
        test(n, Subsets::getSubsets2);
        System.out.println("test getSubsets3");
        test(n, Subsets::getSubsets3);
        System.out.println("test getSubsets4");
        test(n, Subsets::getSubsets4);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("Subsets");
    }
}
