import java.util.*;

import java.lang.reflect.Constructor;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1797: https://leetcode.com/problems/design-authentication-manager/
//
// There is an authentication system that works with authentication tokens. For each session, the
// user will receive a new authentication token that will expire timeToLive seconds after the
// currentTime. If the token is renewed, the expiry time will be extended to expire timeToLive
// seconds after the (potentially different) currentTime.
// Implement the AuthenticationManager class:
// AuthenticationManager(int timeToLive) constructs the AuthenticationManager and sets the
// timeToLive.
// generate(string tokenId, int currentTime) generates a new token with the given tokenId at the
// given currentTime in seconds.
// renew(string tokenId, int currentTime) renews the unexpired token with the given tokenId at the
// given currentTime in seconds. If there are no unexpired tokens with the given tokenId, the
// request is ignored, and nothing happens.
// countUnexpiredTokens(int currentTime) returns the number of unexpired tokens at the given
// currentTime.
// Note that if a token expires at time t, and another action happens on time t (renew or
// countUnexpiredTokens), the expiration takes place before the other actions.
//
// Constraints:
// 1 <= timeToLive <= 10^8
// 1 <= currentTime <= 10^8
// 1 <= tokenId.length <= 5
// tokenId consists only of lowercase letters.
// All calls to generate will contain unique values of tokenId.
// The values of currentTime across all the function calls will be strictly increasing.
// At most 2000 calls will be made to all functions combined.
public class AuthenticationManager {
    // Hash Table
    // 54 ms(%), 40.7 MB(%) for 90 tests
    static class AuthenticationManager1 {
        private final int ttl;
        private final Map<String, Integer> map = new HashMap<>();

        public AuthenticationManager1(int timeToLive) {
            ttl = timeToLive;
        }

        public void generate(String tokenId, int currentTime) {
            map.put(tokenId, currentTime + ttl);
        }

        public void renew(String tokenId, int currentTime) {
            int t = map.getOrDefault(tokenId, -1);
            if (t > currentTime) {
                map.put(tokenId, currentTime + ttl);
            }
        }

        public int countUnexpiredTokens(int currentTime) {
            int res = 0;
            for (int v : map.values()) {
                if (v > currentTime) {
                    res++;
                }
            }
            return res;
        }
    }

    // Hash Table
    // 54 ms(%), 40.7 MB(%) for 90 tests
    static class AuthenticationManager2 {
        private final int ttl;
        private final Map<String, Integer> map = new HashMap<>();

        public AuthenticationManager2(int timeToLive) {
            ttl = timeToLive;
        }

        public void generate(String tokenId, int currentTime) {
            map.put(tokenId, currentTime + ttl);
        }

        public void renew(String tokenId, int currentTime) {
            int t = map.getOrDefault(tokenId, -1);
            if (t > currentTime) {
                map.put(tokenId, currentTime + ttl);
            }
        }

        public int countUnexpiredTokens(int currentTime) {
            map.entrySet().removeIf(e -> e.getValue() <= currentTime);
            return map.size();
        }
    }

    void test1(String className) throws Exception {
        test(new String[] {className, "renew", "generate", "countUnexpiredTokens", "generate",
                           "renew", "renew", "countUnexpiredTokens"},
             new Object[][] {{5}, {"aaa", 1}, {"aaa", 2}, {6}, {"bbb", 7}, {"aaa", 8}, {"bbb", 10},
                             {15}}, new Object[] {null, null, null, 1, null, null, null, 0});
        test(new String[] {className, "renew", "countUnexpiredTokens", "countUnexpiredTokens",
                           "generate", "generate", "renew", "generate", "generate",
                           "countUnexpiredTokens", "countUnexpiredTokens", "countUnexpiredTokens",
                           "renew", "countUnexpiredTokens", "countUnexpiredTokens",
                           "countUnexpiredTokens", "generate", "countUnexpiredTokens", "renew"},
             new Object[][] {{13}, {"ajvy", 1}, {3}, {4}, {"fuzxq", 5}, {"izmry", 7}, {"puv", 12},
                             {"ybiqb", 13}, {"gm", 14}, {15}, {18}, {19}, {"ybiqb", 21}, {23}, {25},
                             {26}, {"aqdm", 28}, {29}, {"puv", 30}},
             new Object[] {null, null, 0, 0, null, null, null, null, null, 4, 3, 3, null, 2, 2, 2,
                           null, 2, null});
    }

    void test(String[] methods, Object[][] args, Object[] expected) throws Exception {
        final String name = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
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
                res = clazz.getMethod(methods[i], String.class, int.class).invoke(obj, arg);
            }
            if (expected[i] != null) {
                assertEquals(expected[i], res);
            }
        }
    }

    @Test public void test1() {
        try {
            test1("AuthenticationManager1");
            test1("AuthenticationManager2");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
