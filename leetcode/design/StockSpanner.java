import java.util.*;

import java.lang.reflect.Constructor;

import org.junit.Test;
import static org.junit.Assert.*;

// LC901: https://leetcode.com/problems/online-stock-span/
//
// Write a class StockSpanner which collects daily price quotes for some stock,
// and returns the span of that stock's price for the current day.
// The span of the stock's price today is defined as the maximum number of
// consecutive days (starting from today and going backwards) for which the
// price of the stock was less than or equal to today's price.
public class StockSpanner {
    // beats %(123 ms for 99 tests)
    class StockSpanner1 {
        private List<Integer> prices = new ArrayList<>();
        private List<Integer> spans = new ArrayList<>();

        public StockSpanner1() {}

        public int next(int price) {
            int res = 1;
            for (int i = prices.size() - 1; i >= 0 && price >= prices.get(i); ) {
                res += spans.get(i);
                i -= spans.get(i);
            }
            prices.add(price);
            spans.add(res);
            return res;
        }
    }

    // Stack
    // beats %(132 ms for 99 tests)
    class StockSpanner2 {
        private Stack<int[]> prices = new Stack<>();
        private int index;
     
        public StockSpanner2() {
            prices.push(new int[] {Integer.MAX_VALUE, -1});
        }
        
        public int next(int price) {
            for (; price >= prices.peek()[0]; prices.pop()) {}
            int res = index - prices.peek()[1]; 
            prices.push(new int[] {price, index++});
            return res;
        }
    }

    // Stack
    // beats %(136 ms for 99 tests)
    class StockSpanner3 {
        private Stack<int[]> prices = new Stack<>();
    
        public StockSpanner3() {
            prices.push(new int[] {Integer.MAX_VALUE, 0});
        }
    
        public int next(int price) {
            int res = 1;
            for (; prices.peek()[0] <= price; res += prices.pop()[1]) {}
            prices.push(new int[]{price, res});
            return res;
        }
    }
    
    void test1(String className) throws Exception {
        Object outerObj =
            new Object() {}.getClass().getEnclosingClass().newInstance();
        test(new String[] { className, "next", "next", "next", "next", "next",
                            "next", "next" },
             new Object[][] { new Object[] { outerObj }, { 100 }, { 80 },
                              { 60 }, { 70 }, { 60 }, { 75 }, { 85 } },
             new Integer[] { null, 1, 1, 1, 2, 1, 4, 6 });
    }

    void test(String[] methods, Object[][] args,
              Integer[] expected) throws Exception {
        final String name =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        final Object[] VOID = new Object[] {};
        Class<?> clazz = Class.forName(name + "$" + methods[0]);
        Constructor<?> ctor = clazz.getConstructors()[0];
        Object obj = ctor.newInstance(args[0]);
        for (int i = 1; i < methods.length; i++) {
            Object[] arg = args[i];
            Object res = null;
            if (arg.length == 0) {
                res = clazz.getMethod(methods[i]).invoke(obj);
            } else if (arg.length == 1) {
                res = clazz.getMethod(methods[i], int.class).invoke(obj, arg);
            } else if (arg.length == 2) {
                res = clazz.getMethod(methods[i], int.class, int.class).invoke(
                    obj, arg);
            }
            if (expected[i] != null) {
                assertEquals(expected[i], res);
            }
        }
    }

    @Test
    public void test1() {
        try {
            test1("StockSpanner1");
            test1("StockSpanner2");
            test1("StockSpanner3");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
