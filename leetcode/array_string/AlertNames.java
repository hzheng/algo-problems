import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1604: https://leetcode.com/problems/alert-using-same-key-card-three-or-more-times-in-a-one-hour-period/
//
// LeetCode company workers use key-cards to unlock office doors. Each time a worker uses their
// key-card, the security system saves the worker's name and the time when it was used. The system
// emits an alert if any worker uses the key-card three or more times in a one-hour period.
// You are given a list of strings keyName and keyTime where [keyName[i], keyTime[i]] corresponds
// to a person's name and the time when their key-card was used in a single day.
// Access times are given in the 24-hour time format "HH:MM", such as "23:51" and "09:49".
// Return a list of unique worker names who received an alert for frequent keycard use. Sort the
// names in ascending order alphabetically.
// Notice that "10:00" - "11:00" is considered to be within a one-hour period, while
// "22:51" - "23:52" is not considered to be within a one-hour period.
//
// Constraints:
// 1 <= keyName.length, keyTime.length <= 10^5
// keyName.length == keyTime.length
// keyTime[i] is in the format "HH:MM".
// [keyName[i], keyTime[i]] is unique.
// 1 <= keyName[i].length <= 10
// keyName[i] contains only lowercase English letters.
public class AlertNames {
    // Hash Table + Sort
    // time complexity: O(N*log(N)), space complexity: O(N)
    // 75 ms(71.86%), 60.9 MB(61.38%) for 77 tests
    public List<String> alertNames(String[] keyName, String[] keyTime) {
        Map<String, List<Integer>> map = new HashMap<>();
        for (int i = 0, n = keyName.length; i < n; i++) {
            String name = keyName[i];
            String time = keyTime[i];
            map.computeIfAbsent(name, x -> new ArrayList<>()).add(toTime(time));
        }
        List<String> res = new ArrayList<>();
        int max = 3;
        for (String name : map.keySet()) {
            List<Integer> times = map.get(name);
            Collections.sort(times);
            for (int i = max - 1, n = times.size(); i < n; i++) {
                int cur = times.get(i);
                int prev = times.get(i - max + 1);
                if (cur - prev <= 60) {
                    res.add(name);
                    break;
                }
            }
        }
        Collections.sort(res);
        return res;
    }

    private int toTime(String s) {
        String[] time = s.split(":");
        int h = Integer.parseInt(time[0]);
        int m = Integer.parseInt(time[1]);
        return h * 60 + m;
    }

    // SortedSet + Hash Table + Deque
    // time complexity: O(N*log(N)), space complexity: O(N)
    // 109 ms(31.44%), 65.6 MB(8.08%) for 77 tests
    public List<String> alertNames2(String[] keyName, String[] keyTime) {
        Map<String, TreeSet<Integer>> map = new HashMap<>();
        for (int i = 0, n = keyName.length; i < n; i++) {
            String name = keyName[i];
            String time = keyTime[i];
            map.computeIfAbsent(name, x -> new TreeSet<>()).add(toTime(time));
        }
        TreeSet<String> res = new TreeSet<>();
        for (var entry : map.entrySet()) {
            Deque<Integer> queue = new ArrayDeque<>();
            for (int time : new ArrayList<>(entry.getValue())) {
                queue.offer(time);
                if (queue.peekLast() - queue.peekFirst() > 60) {
                    queue.pollFirst();
                }
                if (queue.size() >= 3) {
                    res.add(entry.getKey());
                    break;
                }
            }
        }
        return new ArrayList<>(res);
    }

    private void test(String[] keyName, String[] keyTime, String[] expected) {
        List<String> expectedList = Arrays.asList(expected);
        assertEquals(expectedList, alertNames(keyName, keyTime));
        assertEquals(expectedList, alertNames2(keyName, keyTime));
    }

    @Test public void test() {
        test(new String[] {"daniel", "daniel", "daniel", "luis", "luis", "luis", "luis"},
             new String[] {"10:00", "10:40", "11:00", "09:00", "11:00", "13:00", "15:00"},
             new String[] {"daniel"});
        test(new String[] {"alice", "alice", "alice", "bob", "bob", "bob", "bob"},
             new String[] {"12:01", "12:00", "18:00", "21:00", "21:20", "21:30", "23:00"},
             new String[] {"bob"});
        test(new String[] {"john", "john", "john"}, new String[] {"23:58", "23:59", "00:01"},
             new String[] {});
        test(new String[] {"leslie", "leslie", "leslie", "clare", "clare", "clare", "clare"},
             new String[] {"13:00", "13:20", "14:00", "18:00", "18:51", "19:30", "19:49"},
             new String[] {"clare", "leslie"});
        test(new String[] {"aa", "aa", "aa", "aa", "aa", "aa", "aa", "aa", "ba", "ba", "ba", "ba",
                           "ba", "ba", "ba", "ba", "ca", "ca", "ca", "ca", "ca", "ca", "ca", "ca",
                           "da", "da", "da", "da", "da", "da", "da", "da", "ea", "ea", "ea", "ea",
                           "ea", "ea", "ea", "ea", "fa", "fa", "fa", "fa", "fa", "fa", "fa", "fa",
                           "ga", "ga", "ga", "ga", "ga", "ga", "ga", "ga", "ha", "ha", "ha", "ha",
                           "ha", "ha", "ha", "ha", "ia", "ia", "ia", "ia", "ia", "ia", "ia", "ia",
                           "ja", "ja", "ja", "ja", "ja", "ja", "ja", "ja", "ka", "ka", "ka", "ka",
                           "ka", "ka", "ka", "ka", "la", "la", "la", "la", "la", "la", "la", "la",
                           "ma", "ma", "ma", "ma", "ma", "ma", "ma", "ma", "na", "na", "na", "na",
                           "na", "na", "na", "na", "oa", "oa", "oa", "oa", "oa", "oa", "oa", "oa",
                           "pa", "pa", "pa", "pa", "pa", "pa", "pa", "pa", "qa", "qa", "qa", "qa",
                           "qa", "qa", "qa", "qa", "ra", "ra", "ra", "ra", "ra", "ra", "ra", "ra",
                           "sa", "sa", "sa", "sa", "sa", "sa", "sa", "sa", "ta", "ta", "ta", "ta",
                           "ta", "ta", "ta", "ta", "ua", "ua", "ua", "ua", "ua", "ua", "ua", "ua",
                           "va", "va", "va", "va", "va", "va", "va", "va", "wa", "wa", "wa", "wa",
                           "wa", "wa", "wa", "wa", "xa", "xa", "xa", "xa", "xa", "xa", "xa", "xa",
                           "ya", "ya", "ya", "ya", "ya", "ya", "ya", "ya", "za", "za", "za", "za",
                           "za", "za", "za", "za", "ab", "ab", "ab", "ab", "ab", "ab", "ab", "ab",
                           "bb", "bb", "bb", "bb", "bb", "bb", "bb", "bb", "cb", "cb", "cb", "cb",
                           "cb", "cb", "cb", "cb", "db", "db", "db", "db", "db", "db", "db", "db"},
             new String[] {"15:47", "07:06", "23:32", "03:44", "16:02", "18:04", "13:06", "06:28",
                           "12:39", "21:09", "05:07", "03:43", "19:54", "15:44", "23:45", "06:42",
                           "15:11", "11:01", "05:11", "05:46", "20:14", "09:44", "09:06", "15:43",
                           "15:12", "21:45", "18:39", "14:49", "03:54", "22:03", "15:28", "06:15",
                           "12:49", "23:31", "13:43", "16:14", "16:56", "10:40", "13:26", "02:08",
                           "04:39", "00:18", "23:52", "19:04", "13:48", "07:41", "15:19", "20:15",
                           "16:30", "11:44", "03:40", "08:06", "17:10", "17:47", "19:35", "14:27",
                           "21:55", "19:48", "22:06", "01:46", "23:23", "11:36", "01:33", "17:48",
                           "16:20", "05:01", "10:12", "16:48", "18:44", "23:31", "18:36", "21:50",
                           "22:05", "20:26", "22:49", "12:34", "20:15", "15:56", "00:28", "00:23",
                           "00:28", "08:19", "18:52", "07:44", "12:12", "00:33", "22:14", "13:10",
                           "02:12", "15:32", "01:04", "07:23", "00:35", "21:07", "00:30", "19:48",
                           "17:14", "21:22", "16:54", "12:58", "11:11", "17:54", "12:10", "14:27",
                           "08:07", "04:21", "22:43", "11:06", "04:33", "23:45", "03:54", "16:12",
                           "22:47", "00:26", "01:04", "20:53", "20:50", "02:18", "10:25", "06:39",
                           "22:07", "21:09", "04:20", "13:38", "09:43", "11:29", "19:42", "06:55",
                           "00:07", "18:29", "08:26", "00:59", "07:29", "04:52", "21:11", "14:33",
                           "12:19", "16:48", "09:46", "13:47", "13:26", "06:56", "19:19", "23:18",
                           "20:47", "21:55", "07:44", "10:38", "21:11", "04:07", "09:24", "04:26",
                           "10:19", "12:30", "02:36", "10:53", "13:43", "20:48", "04:22", "17:59",
                           "16:50", "23:21", "21:02", "04:18", "23:11", "13:46", "02:37", "16:55",
                           "21:14", "22:35", "18:09", "15:41", "13:49", "07:08", "11:48", "19:03",
                           "21:17", "05:20", "19:52", "22:02", "19:16", "09:00", "03:24", "14:55",
                           "23:16", "02:57", "02:23", "13:40", "17:47", "12:13", "03:23", "02:38",
                           "18:29", "15:32", "20:48", "20:27", "14:38", "12:16", "10:25", "11:59",
                           "08:09", "18:31", "21:53", "03:11", "09:19", "17:39", "14:41", "19:29",
                           "00:59", "00:39", "02:32", "18:24", "17:17", "17:59", "20:41", "17:19",
                           "04:18", "03:44", "22:26", "13:43", "02:17", "18:46", "18:40", "05:46",
                           "20:57", "12:50", "15:57", "08:34", "01:17", "17:47", "14:34", "14:56",
                           "03:09", "09:38", "19:14", "13:02", "20:54", "20:11", "11:55", "18:19"},
             new String[] {"ab", "da", "ea", "la", "ma", "na", "xa"});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
