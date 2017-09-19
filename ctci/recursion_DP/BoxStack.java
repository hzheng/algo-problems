import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Function;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Cracking the Coding Interview(5ed) Problem 9.10:
 * You have a stack of n boxes, with widths, heights and depths. The boxes
 * cannot be rotated and can only be stacked on top of one another if each box
 * in the stack is strictly larger than the box above it in width, height, and
 * depth. Build the tallest stack possible, where the height of a stack is
 * the sum of the heights of each box.
 */
public class BoxStack {
    static class Box {
        int width;
        int height;
        int depth;

        Box(int w, int h, int d) {
            width = w;
            height = h;
            depth = d;
        }

        @Override
        public boolean equals(Object other) {
            if (!(other instanceof Box)) return false;
            Box that = (Box)other;
            return width == that.width && height == that.height
                   && depth == that.depth;
        }

        @Override
        public int hashCode() {
            return Objects.hash(width, height, depth);
        }

        @Override
        public String toString() {
            return "(" + width + "," + height + "," + depth + ")";
        }

        public boolean canBeAbove(Box b) {
            if (b == null) { return true; }
            return (width < b.width && height < b.height && depth < b.depth);
        }
    }

    private static class Stack {
        List<Box> boxes;
        int height;

        Stack(Box box) {
            boxes = new ArrayList<Box>();
            boxes.add(box);
            height = box.height;
        }

        void add(Stack other) {
            height += other.height;
            boxes.addAll(other.boxes);
        }
    }

    public static List<Box> buildStack(Box[] boxes) {
        if (boxes == null || boxes.length == 0) return null;

        // boxes = boxes.clone();
        // Arrays.sort(boxes, (a, b) -> a.height - b.height);
        Stack maxStack = null;
        Stack[] highestStacks = new Stack[boxes.length];
        for (int i = boxes.length - 1; i >= 0; i--) {
            Stack stack = buildStack(boxes, i, highestStacks);
            if (maxStack == null || maxStack.height < stack.height) {
                maxStack = stack;
            }
        }
        return maxStack.boxes;
    }

    private static Stack buildStack(Box[] boxes, int bottom, Stack[] stackMap) {
        if (stackMap[bottom] != null) {
            return stackMap[bottom];
        }

        Box bottomBox = boxes[bottom];
        Stack aboveStack = null;
        for (int i = 0; i < boxes.length; i++) {
            // for (int i = 0; i < bottom; i++) { // if boxes are sorted
            if (boxes[i].canBeAbove(bottomBox)) {
                Stack stack = stackMap[i];
                if (stack == null) {
                    stack = buildStack(boxes, i, stackMap);
                }
                if (aboveStack == null || aboveStack.height < stack.height) {
                    aboveStack = stack;
                }
            }
        }

        Stack result = new Stack(bottomBox);
        if (aboveStack != null) {
            result.add(aboveStack);
        }
        stackMap[bottom] = result;
        return result;
    }

    // from the book
    public static List<Box> createStack(Box[] boxes) {
        return createStack(boxes, null, new HashMap<Box, ArrayList<Box> >());
    }

    private static int stackHeight(List<Box> boxes) {
        if (boxes == null) return 0;
        int h = 0;
        for (Box b : boxes) {
            h += b.height;
        }
        return h;
    }

    // @SuppressWarnings("unchecked")
    private static ArrayList<Box> createStack(Box[] boxes, Box bottom,
                                              Map<Box, ArrayList<Box> > stack_map) {
        if (bottom != null && stack_map.containsKey(bottom)) {
            return stack_map.get(bottom);
        }

        int max_height = 0;
        ArrayList<Box> max_stack = null;
        for (int i = 0; i < boxes.length; i++) {
            if (boxes[i].canBeAbove(bottom)) {
                ArrayList<Box> new_stack = createStack(boxes, boxes[i], stack_map);
                int new_height = stackHeight(new_stack);
                if (new_height > max_height) {
                    max_stack = new_stack;
                    max_height = new_height;
                }
            }
        }
        // there is a BUG in the book
        // if (max_stack == null) {
        //     max_stack = new ArrayList<Box>();
        // }
        // if (bottom != null) {
        //     max_stack.add(0, bottom);  // <--- override shared list!
        // }
        // stack_map.put(bottom, max_stack);
        // return (ArrayList<Box>)max_stack.clone();

        // fixed code
        ArrayList<Box> boxList = new ArrayList<Box>();
        if (bottom != null) {
            boxList.add(bottom);
        }
        if (max_stack != null) {
            boxList.addAll(max_stack);
        }
        stack_map.put(bottom, boxList);
        return boxList;
    }

    private Box[] createBoxes(int[][] data) {
        Box[] boxes = new Box[data.length];
        for (int i = 0; i < data.length; ++i) {
            int[] sizes = data[i];
            boxes[i] = new Box(sizes[0], sizes[1], sizes[2]);
        }
        return boxes;
    }

    private List<Box> test(Function<Box[], List<Box> > buildStack,
                           String name, Box[] boxes) {
        System.out.println("testing " + name);
        long t1 = System.nanoTime();
        List<Box> stack = buildStack.apply(boxes);
        if (boxes.length < 10) {
            System.out.println("===input===");
            for (Box box : boxes) {
                System.out.println(box);
            }
            System.out.println("===output===");
            for (Box box : stack) {
                System.out.println(box);
            }
        }
        System.out.format("%s: %.3f ms\n", name, (System.nanoTime() - t1) * 1e-6);
        return stack;
    }

    private void test(Box[] boxes) {
        List<Box> stack1 = test(BoxStack::buildStack, "buildStack", boxes);
        List<Box> stack2 = test(BoxStack::createStack, "createStack", boxes);
        assertEquals(stack1, stack2);
    }

    private void test(int[][] data) {
        test(createBoxes(data));
    }

    @Test
    public void test1() {
        test(new int[][] {
            {1, 7, 4}, {2, 6, 9}, {4, 9, 6}, {10, 12, 8}, {6, 2, 5},
            {3, 8, 5}, {5, 7, 7}, {2, 10, 16}, {12, 15, 9}, {5, 10, 7}
        });
    }

    @Test
    public void test2() {
        test(new int[][] {
            {8, 7, 5}, {11, 9, 11}, {12, 12, 9},
            {10, 3, 7}, {9, 11, 3}, {1, 3, 9}
        });
    }

    private int ran(int max) {
        return ThreadLocalRandom.current().nextInt(1, max + 1);
    }

    @Test
    public void test3() {
        // int N = 10000; // buildStack is 4+ times faster than createStack
        int N = 1000;
        for (int times = 10; times >= 0; times--) {
            System.out.println("===testing " + N + " boxes===");
            Box[] boxes = new Box[N];
            for (int i = 0; i < N; ++i) {
                int j = 10 + i;
                boxes[i] = new Box(ran(j), ran(j), ran(j));
            }
            test(boxes);
        }
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("BoxStack");
    }
}
