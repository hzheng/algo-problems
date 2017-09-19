import java.util.Stack;

/**
 * Cracking the Coding Interview(5ed) Problem 3.2:
 * Design a stack which, in addition to push and pop, also has a function min
 * which returns the minimum element. Push, pop and min operate in 0(1) time.
 */
public class StackWithMin {
    Stack<Integer> data = new Stack<Integer>();
    Stack<Integer> mins = new Stack<Integer>();

    public void push(int value) {
        data.push(value);
        if (value <= min()) {
            mins.push(value);
        }
    }

    public int pop() {
        int value = data.pop();
        if (value == mins.peek()) {
            mins.pop();
        }
        return value;
    }

    public int min() {
        if (mins.isEmpty()) {
            return Integer.MAX_VALUE;
        }
        return mins.peek();
    }

    public static void main(String[] args) {
        // org.junit.runner.JUnitCore.main("StackWithMin");
    }
}
