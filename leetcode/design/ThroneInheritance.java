import java.util.*;
import java.lang.reflect.Constructor;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1600: https://leetcode.com/problems/throne-inheritance/
//
// A kingdom consists of a king, his children, his grandchildren, and so on. Every once in a while,
// someone in the family dies or a child is born. The kingdom has a well-defined order of
// inheritance that consists of the king as the first member. Let's define the recursive function
// Successor(x, curOrder), which given a person x and the inheritance order so far, returns who
// should be the next person after x in the order of inheritance.
// Successor(x, curOrder):
//    if x has no children or all of x's children are in curOrder:
//        if x is the king return null
//        else return Successor(x's parent, curOrder)
//    else return x's oldest child who's not in curOrder
// Using the above function, we can always obtain a unique order of inheritance.
// Implement the ThroneInheritance class:
// ThroneInheritance(string kingName) Initializes an object of the ThroneInheritance class. The name
// of the king is given as part of the constructor.
// void birth(string parentName, string childName) Indicates that parentName gave birth to childName.
// void death(string name) Indicates the death of name. The death of the person doesn't affect the
// Successor function nor the current inheritance order.
// string[] getInheritanceOrder() Returns a list representing the current order of inheritance
// excluding dead people.
// Constraints:
// 1 <= kingName.length, parentName.length, childName.length, name.length <= 15
// kingName, parentName, childName, and name consist of lowercase English letters only.
// All arguments childName and kingName are distinct.
// All name arguments of death will be passed to either the constructor or as childName to birth first.
// For each call to birth(parentName, childName), it is guaranteed that parentName is alive.
// At most 10^5 calls will be made to birth and death.
// At most 10 calls will be made to getInheritanceOrder.
public class ThroneInheritance {
    // Recursion + DFS + Tree + Map + Set
    // 246 ms(77.48%), 104.2 MB(57.54%) for 49 tests
    static class ThroneInheritance1 {
        class Node {
            String name;
            List<Node> children = new ArrayList<>();

            Node(String name) {
                this.name = name;
                familyTree.put(name, this);
            }
        }

        private Set<String> dead = new HashSet<>();
        private Map<String, Node> familyTree = new HashMap<>();
        private Node root;

        public ThroneInheritance1(String kingName) {
            root = new Node(kingName);
        }

        public void birth(String parentName, String childName) {
            Node parent = familyTree.get(parentName);
            Node child = new Node(childName);
            parent.children.add(child);
        }

        public void death(String name) {
            dead.add(name);
        }

        public List<String> getInheritanceOrder() {
            List<String> res = new ArrayList<>();
            dfs(root, res);
            return res;
        }

        private void dfs(Node node, List<String> res) {
            if (!dead.contains(node.name)) {
                res.add(node.name);
            }
            for (Node child : node.children) {
                dfs(child, res);
            }
        }
    }

    // Recursion + DFS + Map + Set
    // 244 ms(77.48%), 104.2 MB(57.54%) for 49 tests
    static class ThroneInheritance2 {
        private Map<String, List<String>> familyTree = new HashMap<>();
        private Set<String> dead = new HashSet<>();
        private String root;

        public ThroneInheritance2(String kingName) {
            root = kingName;
        }

        public void birth(String parentName, String childName) {
            familyTree.computeIfAbsent(parentName, x -> new ArrayList<>()).add(childName);
        }

        public void death(String name) {
            dead.add(name);
        }

        public List<String> getInheritanceOrder() {
            List<String> res = new ArrayList<>();
            dfs(root, res);
            return res;
        }

        private void dfs(String name, List<String> res) {
            if (!dead.contains(name)) {
                res.add(name);
            }
            for (String child : familyTree.getOrDefault(name, Collections.emptyList())) {
                dfs(child, res);
            }
        }
    }

    void test1(String className) throws Exception {
        test(new String[] {className, "birth", "birth", "birth", "birth", "birth", "birth",
                           "getInheritanceOrder", "death", "getInheritanceOrder"},
             new Object[][] {{"king"}, {"king", "andy"}, {"king", "bob"}, {"king", "catherine"},
                             {"andy", "matthew"}, {"bob", "alex"}, {"bob", "asha"}, {}, {"bob"},
                             {}}, new String[][] {null, null, null, null, null, null, null,
                                                  {"king", "andy", "matthew", "bob", "alex", "asha",
                                                   "catherine"}, null,
                                                  {"king", "andy", "matthew", "alex", "asha",
                                                   "catherine"}});
    }

    void test(String[] methods, Object[][] args, String[][] expected) throws Exception {
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
                res = clazz.getMethod(methods[i], String.class).invoke(obj, arg);
            } else if (arg.length == 2) {
                res = clazz.getMethod(methods[i], String.class, String.class).invoke(obj, arg);
            }
            if (expected[i] != null) {
                assertEquals(Arrays.asList(expected[i]), res);
            }
        }
    }

    @Test public void test1() {
        try {
            test1("ThroneInheritance1");
            test1("ThroneInheritance2");
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
