import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Cracking the Coding Interview(5ed) Problem 3.3:
 * In real life, we would likely start a new stack when the previous stack
 * exceeds some threshold. Implement a data structure that mimics this. It
 * should be composed of several stacks and should create a new stack once
 * the previous one exceeds capacity.
 * Follow up: Implement popAt index) which pops on a specific sub-stack.
 */
public class SetOfStacks {
    private List<Stack<Integer>> stacks = new ArrayList<Stack<Integer>>();
    private int capacity;
    private int index = -1;

    public SetOfStacks(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException();
        }
        this.capacity = capacity;
        createStack();
    }

    private Stack<Integer> createStack() {
        Stack<Integer> stack = new Stack<Integer>();
        stacks.add(stack);
        index++;
        return stack;
    }

    private Stack<Integer> lastStack() {
        return stacks.get(index);
    }

    public void push(int value) {
        Stack<Integer> stack = lastStack();
        if (stack.size() >= capacity) {
            // change stack
            if (index < stacks.size() - 1) {
                stack = stacks.get(++index);
            } else {
                stack = createStack();
            }
        }
        stack.push(value);
    }

    public boolean empty() {
        return (lastStack().empty() && index == 0);
    }

    public int pop() {
        Stack<Integer> stack = lastStack();
        if (stack.empty() && index > 0) {
            stack = stacks.get(--index);
        }
        int result = stack.pop();
        adjustIndex();
        return result;
    }

    private void adjustIndex() {
        if (lastStack().empty() && (index > 0)) {
            index--;
        }
    }

    public int popAt(int i) {
        Stack<Integer> stack = stacks.get(i);
        int result = stack.pop();
        shiftAllLeft(i + 1);
        adjustIndex();
        return result;
    }

    private void shiftAllLeft(int i) {
        for (int j = i; j <= index; j++) {
            int removed = shiftLeft(stacks.get(j));
            stacks.get(j - 1).push(removed);
        }
    }

    private int shiftLeft(Stack<Integer> stack) {
        int size = stack.size();
        int removed = stack.get(0);
        for (int i = 1; i < size; i++) {
            stack.set(i - 1, stack.get(i));
        }
        stack.pop();
        return removed;
    }

    void print() {
        for (int i = 0; i <= index; i++) {
            System.out.print("stack " + i + " : ");
            System.out.println(Arrays.toString(stacks.get(i).toArray()));
        }
    }

    void testPopAt(int i, int expected) {
        System.out.println("===popAt " + i + "===");
        assertEquals(expected, popAt(i));
        print();
    }

    public static void main(String[] args) {
        // org.junit.runner.JUnitCore.main("SetOfStacks");
        SetOfStacks stackSet = new SetOfStacks(3);
        int n = 10;
        // test pop
        for (int i = 0; i < n; i++) {
            System.out.println("===push " + i + "===");
            stackSet.push(i);
            stackSet.print();
        }

        // test push
        for (int i = 0; i < n; i++) {
            System.out.println("===pop===");
            stackSet.pop();
            stackSet.print();
        }

        System.out.println("===push again===");
        for (int i = 0; i < n; i++) {
            stackSet.push(i);
        }
        stackSet.print();

        stackSet.testPopAt(0, 2);
        stackSet.testPopAt(1, 6);
        stackSet.testPopAt(2, 9);
    }
}
