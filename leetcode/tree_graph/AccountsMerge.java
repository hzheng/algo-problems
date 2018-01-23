import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.function.Function;

import common.Utils;

// LC721: https://leetcode.com/problems/accounts-merge/
//
// Given a list accounts, each element accounts[i] is a list of strings, where
//  the first element accounts[i][0] is a name, and the rest of the elements are
//  emails representing emails of the account.
// Now, we would like to merge these accounts. Two accounts definitely belong to
// the same person if there is some email that is common to both accounts. Note
// that even if two accounts have the same name, they may belong to different
// people as people could have the same name. A person can have any number of
// accounts initially, but all of their accounts definitely have the same name.
// After merging the accounts, return the accounts in the following format: the
// first element of each account is the name, and the rest of the elements are
// emails in sorted order. The accounts themselves can be returned in any order.
public class AccountsMerge {
    // Union Find + Hash Table
    // time complexity: O(A * log A), space complexity: O(A)
    // where A is total account number
    // beats 94.78%(69 ms for 49 tests)
    public List<List<String> > accountsMerge(List<List<String> > accounts) {
        Map<String, String> emailNames = new HashMap<>();
        Map<String, Integer> emailIDs = new HashMap<>();
        int id = 0;
        for (List<String> account : accounts) {
            String name = account.get(0);
            for (int i = account.size() - 1; i > 0; i--) {
                String email = account.get(i);
                if (!emailIDs.containsKey(email)) {
                    emailIDs.put(email, id++);
                }
                emailNames.put(email, name);
            }
        }
        int[] parents = new int[id];
        for (int i = 0; i < id; i++) {
            parents[i] = i;
        }
        for (List<String> account : accounts) {
            int p = emailIDs.get(account.get(1));
            for (; parents[p] != p; p = parents[p]) {}
            for (int i = account.size() - 1; i > 1; i--) {
                int q = emailIDs.get(account.get(i));
                for (; parents[q] != q; q = parents[q] = parents[parents[q]]) {}
                parents[q] = p;
            }
        }
        List<List<String> > res = new ArrayList<>();
        Map<Integer, Integer> places = new HashMap<>();
        int i = 0;
        for (String email : emailIDs.keySet()) {
            int root = emailIDs.get(email);
            for (; parents[root] != root; root = parents[root]) {}
            Integer index = places.get(root);
            if (index != null) {
                res.get(index).add(email);
            } else {
                List<String> list = new ArrayList<>();
                list.add(emailNames.get(email));
                list.add(email);
                res.add(list);
                places.put(root, i++);
            }
        }
        for (List<String> list : res) {
            Collections.sort(list.subList(1, list.size()));
        }
        return res;
    }

    // Union Find + Hash Table
    // time complexity: O(A * log A), space complexity: O(A)
    // beats 27.63%(160 ms for 49 tests)
    public List<List<String> > accountsMerge2(List<List<String> > accounts) {
        DSU dsu = new DSU();
        Map<String, String> emailNames = new HashMap<>();
        Map<String, Integer> emailIDs = new HashMap<>();
        int id = 0;
        for (List<String> account : accounts) {
            String name = null;
            for (String email : account) {
                if (name == null) {
                    name = email;
                    continue;
                }
                emailNames.put(email, name);
                if (!emailIDs.containsKey(email)) {
                    emailIDs.put(email, id++);
                }
                dsu.union(emailIDs.get(account.get(1)), emailIDs.get(email));
            }
        }
        Map<Integer, List<String> > res = new HashMap<>();
        for (String email : emailNames.keySet()) {
            int index = dsu.find(emailIDs.get(email));
            res.computeIfAbsent(index, x -> new ArrayList<>()).add(email);
        }
        for (List<String> component : res.values()) {
            Collections.sort(component);
            component.add(0, emailNames.get(component.get(0)));
        }
        return new ArrayList<>(res.values());
    }

    class DSU {
        int[] parent;

        public DSU() {
            parent = new int[10001];
            for (int i = 0; i < parent.length; ++i) {
                parent[i] = i;
            }
        }

        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        public void union(int x, int y) {
            parent[find(x)] = find(y);
        }
    }

    // Union Find + Hash Table
    // time complexity: O(A * log A), space complexity: O(A)
    // beats 48.71%(112 ms for 49 tests)
    public List<List<String> > accountsMerge3(List<List<String> > accounts) {
        Map<String, String> emailNames = new HashMap<>();
        Map<String, String> parents = new HashMap<>();
        Map<String, TreeSet<String>> unions = new HashMap<>();
        for (List<String> account : accounts) {
            String name = account.get(0);
            for (int i = account.size() - 1; i > 0; i--) {
                String email = account.get(i);
                parents.put(email, email);
                emailNames.put(email, name);
            }
        }
        for (List<String> account : accounts) {
            String root = find(account.get(1), parents);
            for (int i = account.size() - 1; i > 1; i--) {
                parents.put(find(account.get(i), parents), root);
            }
        }
        for (List<String> account : accounts) {
            String root = find(account.get(1), parents);
            TreeSet<String> union = unions.get(root);
            if (union == null) {
                unions.put(root, union = new TreeSet<>());
            }
            for (int i = account.size() - 1; i > 0; i--) {
                union.add(account.get(i));
            }
        }
        List<List<String>> res = new ArrayList<>();
        for (String email : unions.keySet()) {
            List<String> emails = new ArrayList<>(unions.get(email));
            emails.add(0, emailNames.get(email));
            res.add(emails);
        }
        return res;
    }

    private String find(String x, Map<String, String> parents) {
        return parents.get(x) == x ? x : find(parents.get(x), parents);
    }

    // DFS + Stack
    // time complexity: O(A * log A), space complexity: O(A)
    // beats 23.79%(169 ms for 49 tests)
    public List<List<String>> accountsMerge4(List<List<String>> accounts) {
        Map<String, String> emailNames = new HashMap<>();
        Map<String, List<String>> graph = new HashMap<>();
        for (List<String> account : accounts) {
            String name = null;
            for (String email : account) {
                if (name == null) {
                    name = email;
                    continue;
                }
                graph.computeIfAbsent(email, x -> new ArrayList<>()).add(account.get(1));
                graph.computeIfAbsent(account.get(1), x -> new ArrayList<>()).add(email);
                emailNames.put(email, name);
            }
        }
        Set<String> visited = new HashSet<>();
        List<List<String>> res = new ArrayList<>();
        for (String email : graph.keySet()) {
            if (!visited.add(email)) continue;

            Stack<String> stack = new Stack<>();
            stack.push(email);
            List<String> component = new ArrayList<>();
            while (!stack.empty()) {
                String node = stack.pop();
                component.add(node);
                for (String adjacency : graph.get(node)) {
                    if (visited.add(adjacency)) {
                        stack.push(adjacency);
                    }
                }
            }
            Collections.sort(component);
            component.add(0, emailNames.get(email));
            res.add(component);
        }
        return res;
    }

    void test(String[][] accounts, String[][] expected) {
        AccountsMerge a = new AccountsMerge();
        test(accounts, expected, a::accountsMerge);
        test(accounts, expected, a::accountsMerge2);
        test(accounts, expected, a::accountsMerge3);
        test(accounts, expected, a::accountsMerge4);
    }

    void test(String[][] accounts, String[][] expected,
              Function<List<List<String>>,
              List <List<String>>> accountsMerge) {
        List<List<String> > accts = Utils.toList(accounts);
        List<List<String> > res = accountsMerge.apply(accts);
        List<List<String> > expectedList = Utils.toList(expected);
        Collections.sort(res, (a, b) -> a.get(0).compareTo(b.get(0)) * 100
                         + a.get(1).compareTo(b.get(1)));
        assertEquals(expectedList, res);
    }

    @Test
    public void test() {
        test(new String[][] {{"John", "johnsmith@mail.com", "john00@mail.com"},
                             {"John", "johnnybravo@mail.com"},
                             {"John", "johnsmith@mail.com",
                              "john_newyork@mail.com"},
                               {"Mary", "mary@mail.com"}},
             new String[][] {{"John", "john00@mail.com",
                              "john_newyork@mail.com", "johnsmith@mail.com"},
                             {"John", "johnnybravo@mail.com"},
                             {"Mary", "mary@mail.com"}});
        test(new String[][] {{"David", "David0@m.co", "David4@m.co",
                              "David3@m.co"},
                             {"David", "David5@m.co", "David5@m.co",
             "David0@m.co"},
                             {"David", "David1@m.co", "David4@m.co", "David0@m.co"},
                             {"David", "David0@m.co", "David1@m.co", "David3@m.co"},
                             {"David", "David4@m.co", "David1@m.co", "David3@m.co"}},
             new String[][] {{"David", "David0@m.co", "David1@m.co",
                              "David3@m.co", "David4@m.co", "David5@m.co"}});
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
