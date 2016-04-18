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
    public static List<ArrayList<Integer> > getSubsets(List<Integer> set) {
        return getSubsets(set, set.size() - 1);
    }

    private static List<ArrayList<Integer> > getSubsets(List<Integer> set, int index) {
        if (index == -1) {
            List<ArrayList<Integer> > emptySet = new ArrayList<ArrayList<Integer> >();
            emptySet.add(new ArrayList<Integer>());
            return emptySet;
        }

        List<ArrayList<Integer> > subsets = getSubsets(set, index - 1);
        List<ArrayList<Integer> > moreSubsets = new ArrayList<ArrayList<Integer> >();
        Integer newElement = set.get(index);
        for (ArrayList<Integer> subset : subsets) {
            ArrayList<Integer> cloned =  new ArrayList<Integer>(subset);
            cloned.add(newElement);
            moreSubsets.add(cloned);
        }
        subsets.addAll(moreSubsets);
        return subsets;
    }

    // From the book
    public static List<ArrayList<Integer> > getSubsets2(List<Integer> set) {
        return getSubsets2(set, 0);
    }

    private static List<ArrayList<Integer> > getSubsets2(List<Integer> set, int index) {
        List<ArrayList<Integer> > allsubsets;
        if (set.size() == index) { // Base case - add empty set
            allsubsets = new ArrayList<ArrayList<Integer> >();
            allsubsets.add(new ArrayList<Integer>());
        } else {
            allsubsets = getSubsets2(set, index + 1);
            int item = set.get(index);
            List<ArrayList<Integer> > moresubsets = new ArrayList<ArrayList<Integer> >();
            for (ArrayList<Integer> subset : allsubsets) {
                ArrayList<Integer> newsubset = new ArrayList<Integer>();
                newsubset.addAll(subset); //
                newsubset.add(item);
                moresubsets.add(newsubset);
            }
            allsubsets.addAll(moresubsets);
        }
        return allsubsets;
    }

    public static List<ArrayList<Integer> > getSubsets3(List<Integer> set) {
        List<ArrayList<Integer> > subsets = new ArrayList<ArrayList<Integer> >();
        int n = set.size();
        for (int i = ((1 << n) - 1); i >= 0; i--) {
            ArrayList<Integer> subset = new ArrayList<Integer>();
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
    public static ArrayList<Integer> convertIntToSet(int x, List<Integer> set) {
        ArrayList<Integer> subset = new ArrayList<Integer>();
        int index = 0;
        for (int k = x; k > 0; k >>= 1) {
            if ((k & 1) == 1) {
                subset.add(set.get(index));
            }
            index++;
        }
        return subset;
    }

    public static List<ArrayList<Integer> > getSubsets4(List<Integer> set) {
        List<ArrayList<Integer> > allsubsets = new ArrayList<ArrayList<Integer> >();
        int max = 1 << set.size();
        for (int k = 0; k < max; k++) {
            ArrayList<Integer> subset = convertIntToSet(k, set);
            allsubsets.add(subset);
        }
        return allsubsets;
    }

    private void test(int n,
                      Function<List<Integer>, List<ArrayList<Integer>>> getSubset) {
        List<Integer> set = new ArrayList<Integer>();
        for (int i = 1; i <= n; i++) {
            set.add(i);
        }

        long t = System.nanoTime();
        List<ArrayList<Integer> > subsets = getSubset.apply(set);
        System.out.format("%.03f ms\n", (System.nanoTime() - t) * 1e-6);
        if (n < 5) {
            for (ArrayList<Integer> subset : subsets) {
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
