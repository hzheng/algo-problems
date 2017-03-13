import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC359: https://leetcode.com/problems/logger-rate-limiter
//
// Design a logger system that receive stream of messages along with its
// timestamps, each message should be printed if and only if it is not printed
// in the last 10 seconds.
// Given a message and a timestamp, return true if the message should be
// printed in the given timestamp, otherwise returns false.
// It is possible that several messages arrive roughly at the same time.
public class Logger {
    interface ILogger {

        /** Returns true if the message should be printed in the given timestamp, otherwise returns false.
            If this method returns false, the message will not be printed. */
        public boolean shouldPrintMessage(int timestamp, String message);
    }

    // Hash Table
    // beats 69.60%(169 ms for 20 tests)
    static class Logger1 implements ILogger {
        private Map<String, Integer> printed = new HashMap<>();

        public boolean shouldPrintMessage(int timestamp, String message) {
            Integer time = printed.get(message);
            if (time != null && timestamp - time < 10) return false;
            // or:
            // if (timestamp - printed.getOrDefault(message, timestamp - 10) < 10) return false;

            printed.put(message, timestamp);
            return true;
        }
    }

    // Hash Table
    // beats 23.53%(205 ms for 20 tests)
    static class Logger2 implements ILogger {
        private Map<String, Integer> printed = new HashMap<>();

        public boolean shouldPrintMessage(int timestamp, String message) {
            if (timestamp < printed.getOrDefault(message, 0)) return false;

            printed.put(message, timestamp + 10);
            return true;
        }
    }

    // Queue + Set
    // beats 51.09%(180 ms for 20 tests)
    static class Logger3 implements ILogger {
        private static class TimedMessage {
            int time;
            String msg;

            public TimedMessage(int time, String msg) {
                this.time = time;
                this.msg = msg;
            }
        }

        private Queue<TimedMessage> queue = new LinkedList<>();
        private Set<String> printed = new HashSet<>();

        public boolean shouldPrintMessage(int timestamp, String message) {
            while (!queue.isEmpty() && queue.peek().time <= timestamp - 10) {
                printed.remove(queue.poll().msg);
            }
            if (printed.contains(message)) return false;

            queue.offer(new TimedMessage(timestamp, message));
            printed.add(message);
            return true;
        }
    }

    private void test(ILogger logger) {
        assertTrue(logger.shouldPrintMessage(1, "foo"));
        assertTrue(logger.shouldPrintMessage(2, "bar"));
        assertFalse(logger.shouldPrintMessage(3, "foo"));
        assertFalse(logger.shouldPrintMessage(8, "bar"));
        assertFalse(logger.shouldPrintMessage(10, "foo"));
        assertTrue(logger.shouldPrintMessage(11, "foo"));
    }

    @Test
    public void test() {
        test(new Logger1());
        test(new Logger2());
        test(new Logger3());
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("Logger");
    }
}
