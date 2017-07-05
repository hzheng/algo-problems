import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.Utils;

// LC635: https://leetcode.com/problems/design-log-storage-system/
//
// Each log contains a unique id and timestamp. Timestamp is a string that has the
// following format: Year:Month:Day:Hour:Minute:Second. All domains are zero-padded
// decimal numbers. Design a log storage system to implement the following functions:
// void Put(int id, string timestamp): Given a log's unique id and timestamp, store
// the log in your storage system.
// int[] Retrieve(String start, String end, String granularity): Return the id of
// logs whose timestamps are within the range from start to end. Start and end all
// have the same format as timestamp. Granularity means the time level for consideration.
// Note:
// There will be at most 300 operations of Put or Retrieve.
// Year ranges from [2000,2017]. Hour ranges from [00,23].
// Output for Retrieve has no order required.
public class LogSystem {

    static interface Loggable {
        public void put(int id, String timestamp);

        public List<Integer> retrieve(String s, String e, String gra);
    }

    private static final List<String> GRANULARITY = Arrays.asList(
        "Year", "Month", "Day", "Hour", "Minute", "Second");

    // SortedMap
    // beats 77.60%(176 ms for 63 tests)
    class LogSystem1 implements Loggable {
        @SuppressWarnings("unchecked")
        private NavigableMap<String, List<Integer> >[] logs = new NavigableMap[6];

        public LogSystem1() {
            for (int i = GRANULARITY.size() - 1; i >= 0; i--) {
                logs[i] = new TreeMap<>();
            }
        }

        public void put(int id, String timestamp) {
            int index = 4;
            for (NavigableMap<String, List<Integer> > log : logs) {
                String key = timestamp.substring(0, index);
                List<Integer> list = log.get(key);
                if (list == null) {
                    log.put(key, list = new ArrayList<>());
                }
                list.add(id);
                index += 3;
            }
        }

        public List<Integer> retrieve(String s, String e, String gra) {
            int index = GRANULARITY.indexOf(gra);
            s = s.substring(0, 4 + 3 * index);
            e = e.substring(0, 4 + 3 * index);
            NavigableMap<String, List<Integer> > log = logs[index];
            NavigableMap<String, List<Integer> > subMap = log.subMap(s, true, e, true);
            Set<Integer> set = new HashSet<>();
            for (List<Integer> list : subMap.values()) {
                set.addAll(list);
            }
            return new ArrayList<>(set);
        }
    }

    // List
    // https://leetcode.com/articles/design-log-storage/#approach-1-converting-timestamp-into-a-number-accepted
    // beats 52.19%(195 ms for 63 tests)
    class LogSystem2 implements Loggable {
        private List<long[]> logs = new ArrayList<>();

        public void put(int id, String timestamp) {
            int[] st = Arrays.stream(timestamp.split(":")).mapToInt(Integer::parseInt).toArray();
            logs.add(new long[] {convert(st), id});
        }

        public List <Integer> retrieve(String s, String e, String gra) {
            List <Integer> res = new ArrayList<>();
            long start = granularity(s, gra, 0);
            long end = granularity(e, gra, 1);
            for (long[] log : logs) {
                if (log[0] >= start && log[0] < end) {
                    res.add((int)log[1]);
                }
            }
            return res;
        }

        private long granularity(String s, String gra, int endFlag) {
            String[] res = new String[] {"1999", "00", "00", "00", "00", "00"};
            String[] st = s.split(":");
            int index = GRANULARITY.indexOf(gra);
            for (int i = 0; i <= index; i++) {
                res[i] = st[i];
            }
            int[] t = Arrays.stream(res).mapToInt(Integer::parseInt).toArray();
            t[index] += endFlag;
            return convert(t);
        }

        private long convert(int[] st) {
            st[1] -= (st[1] == 0 ? 0 : 1);
            st[2] -= (st[2] == 0 ? 0 : 1);
            return (st[0] - 1999L) * 365 * 24 * 60 * 60 + st[1] * 30 * 24 * 60 * 60
                   + st[2] * 24 * 60 * 60 + st[3] * 60 * 60 + st[4] * 60 + st[5];
        }
    }

    // SortedMap
    // https://leetcode.com/articles/design-log-storage/#approach-1-converting-timestamp-into-a-number-accepted
    // beats 34.87%(209 ms for 63 tests)
    class LogSystem3 implements Loggable {
        private SortedMap<Long, Integer> logs = new TreeMap<>();

        public void put(int id, String timestamp) {
            int[] st = Arrays.stream(timestamp.split(":")).mapToInt(Integer::parseInt).toArray();
            logs.put(convert(st), id);
        }

        public List <Integer> retrieve(String s, String e, String gra) {
            List <Integer> res = new ArrayList<>();
            long start = granularity(s, gra, 0);
            long end = granularity(e, gra, 1);
            for (int val : logs.subMap(start, end).values()) {
                res.add(val);
            }
            return res;
        }

        private long granularity(String s, String gra, int endFlag) {
            String[] res = new String[] {"1999", "00", "00", "00", "00", "00"};
            String[] st = s.split(":");
            int index = GRANULARITY.indexOf(gra);
            for (int i = 0; i <= index; i++) {
                res[i] = st[i];
            }
            int[] t = Arrays.stream(res).mapToInt(Integer::parseInt).toArray();
            t[index] += endFlag;
            return convert(t);
        }

        private long convert(int[] st) {
            st[1] -= (st[1] == 0 ? 0 : 1);
            st[2] -= (st[2] == 0 ? 0 : 1);
            return (st[0] - 1999L) * 365 * 24 * 60 * 60 + st[1] * 30 * 24 * 60 * 60
                   + st[2] * 24 * 60 * 60 + st[3] * 60 * 60 + st[4] * 60 + st[5];
        }
    }

    // List
    // https://discuss.leetcode.com/topic/94449/concise-java-solution
    // beats 70.21%(181 ms for 63 tests)
    class LogSystem4 implements Loggable {
        private final int[] indices = new int[] {4, 7, 10, 13, 16, 19};
        private List<String[]> timestamps = new LinkedList<>();

        public void put(int id, String timestamp) {
            timestamps.add(new String[] {Integer.toString(id), timestamp});
        }

        public List<Integer> retrieve(String s, String e, String gra) {
            List<Integer> res = new LinkedList<>();
            int i = indices[GRANULARITY.indexOf(gra)];
            for (String[] timestamp : timestamps) {
                if (timestamp[1].substring(0, i).compareTo(s.substring(0, i)) >= 0
                    && timestamp[1].substring(0, i).compareTo(e.substring(0, i)) <= 0) {
                    res.add(Integer.parseInt(timestamp[0]));
                }
            }
            return res;
        }
    }

    void test1(Loggable obj) {
        obj.put(1, "2017:01:01:23:59:59");
        obj.put(2, "2017:01:01:22:59:59");
        obj.put(3, "2016:01:01:00:00:00");
        List<Integer> res = obj.retrieve("2016:01:01:01:01:01", "2017:01:01:23:00:00", "Year");
        Collections.sort(res);
        assertArrayEquals(new int[] {1, 2, 3}, Utils.toArray(res));
        res = obj.retrieve("2016:01:01:01:01:01", "2017:01:01:23:00:00", "Hour");
        Collections.sort(res);
        assertArrayEquals(new int[] {1, 2}, Utils.toArray(res));
    }

    @Test
    public void test() {
        test1(new LogSystem1());
        test1(new LogSystem2());
        test1(new LogSystem3());
        test1(new LogSystem4());
    }

    public static void main(String[] args) {
        String clazz = new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
