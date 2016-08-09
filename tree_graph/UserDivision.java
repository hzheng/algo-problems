import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// http://mp.weixin.qq.com/s?__biz=MjM5ODIzNDQ3Mw==&mid=2649965685&idx=1&sn=b3d5a26775364f23a077edf91b45a74e&scene=0#wechat_redirect
// 
// Users of Facebook are mutual. Given a user list, test if all the users can
// be divided into two groups, s.t. any two members in each group are not friends.
public class UserDivision {
    // Bipartite
    public List<List<Integer> > divide(int[] users, int[][] friends) {
        Map<Integer, Set<Integer> > friendMap = new HashMap<>();
        for (int[] friend : friends) {
            int a = friend[0];
            int b = friend[1];
            if (!friendMap.containsKey(a)) {
                friendMap.put(a, new HashSet<>());
            }
            friendMap.get(a).add(b);
            if (!friendMap.containsKey(b)) {
                friendMap.put(b, new HashSet<>());
            }
            friendMap.get(b).add(a);
        }

        Map<Integer, Boolean> colors = new HashMap<>();
        List<Integer> group1 = new ArrayList<>();
        List<Integer> group2 = new ArrayList<>();
        for (int user : users) {
            if (colors.get(user) == null && !bfs(user, friendMap, colors)) {
                return null;
            }
            if (colors.get(user)) {
                group1.add(user);
            } else {
                group2.add(user);
            }
        }
        return Arrays.asList(group1, group2);
    }

    private boolean bfs(int user, Map<Integer, Set<Integer> > friends,
                        Map<Integer, Boolean> colors) {
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(user);
        colors.put(user, true);
        while (!queue.isEmpty()) {
            int first = queue.poll();
            boolean firstColor = colors.get(first);
            for (int friend : friends.get(first)) {
                Boolean color = colors.get(friend);
                if (color == null) {
                    colors.put(friend, !firstColor);
                    queue.offer(friend);
                } else if (color == firstColor) return false;
            }
        }
        return true;
    }

    void test(int[] users, int[][] friends, int[][] expected) {
        List<List<Integer> > res = divide(users, friends);
        if (expected == null) {
            assertNull(res);
            return;
        }

        Integer[][] resArray = res.stream().map(
            a -> a.toArray(new Integer[0])).toArray(Integer[][]::new);
        assertArrayEquals(expected, resArray);
    }

    @Test
    public void test1() {
        test(new int[] {1, 2, 3}, new int[][] {{1, 2}, {2, 3}},
             new int[][] {{1, 3}, {2}});
        test(new int[] {1, 2, 3, 4}, new int[][] {{1, 2}, {2, 3}, {3, 4}, {4, 1}},
             new int[][] {{1, 3}, {2, 4}});
        test(new int[] {1, 2, 3, 4}, new int[][] {{1, 2}, {1, 3}, {1, 4},
                                                  {2, 3}, {3, 4}}, null);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("UserDivision");
    }
}
