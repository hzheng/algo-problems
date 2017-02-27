import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC271: https://leetcode.com/problems/encode-and-decode-strings
//
// Design an algorithm to encode a list of strings to a string. The encoded string
// is then sent over the network and is decoded back to the original list of strings.
public class CodecStr {
    interface Codec {
        public String encode(List<String> strs);

        public List<String> decode(String s);
    }

    // beats 27.23%(40 ms for 316 tests)
    static class Codec1 implements Codec {
        public String encode(List<String> strs) {
            StringBuilder sb = new StringBuilder();
            for (String s : strs) {
                for (char c : s.toCharArray()) {
                    if (c == '\\' || c == '+') {
                        sb.append("\\");
                    }
                    sb.append(c);
                }
                sb.append("+");
            }
            return sb.toString();
        }

        public List<String> decode(String s) {
            List<String> res = new ArrayList<>();
            StringBuilder sb = new StringBuilder();
            for (int i = 0, len = s.length(); i < len; i++) {
                char c = s.charAt(i);
                if (c == '\\') {
                    sb.append(s.charAt(++i));
                } else if (c == '+') {
                    res.add(sb.toString());
                    sb.setLength(0);
                } else {
                    sb.append(c);
                }
            }
            return res;
        }
    }

    // beats 15.99%(66 ms for 316 tests)
    static class Codec1_2 implements Codec {
        public String encode(List<String> strs) {
            StringBuilder sb = new StringBuilder();
            for (String s : strs) {
                sb.append(s.replace("+", "++")).append(" + ");
            }
            return sb.toString();
        }

        public List<String> decode(String s) {
            List<String> res = new ArrayList<>();
            String[] strs = s.split(" \\+ ", -1);
            for (int i = 0; i < strs.length - 1; i++) {
                res.add(strs[i].replace("++", "+"));
            }
            return res;
        }
    }

    // beats 96.96%(10 ms for 316 tests)
    static class Codec2 implements Codec {
        public String encode(List<String> strs) {
            StringBuilder sb = new StringBuilder();
            for (String s : strs) {
                sb.append(s.length()).append(" ").append(s);
            }
            return sb.toString();
        }

        public List<String> decode(String s) {
            List<String> res = new ArrayList<>();
            char[] cs = s.toCharArray();
            for (int i = 0, count; i < cs.length; i += count) {
                count = 0;
                while (true) {
                    char c = s.charAt(i++);
                    if (c == ' ') break;

                    count *= 10;
                    count += c - '0';
                }
                res.add(new String(cs, i, count));
            }
            return res;
        }
    }

    // beats 35.12%(20 ms for 316 tests)
    static class Codec2_2 implements Codec {
        public String encode(List<String> strs) {
            StringBuilder sb = new StringBuilder();
            for (String s : strs) {
                sb.append(s.length()).append(" ").append(s);
            }
            return sb.toString();
        }

        public List<String> decode(String s) {
            List<String> res = new ArrayList<>();
            for (int i = 0; i < s.length(); ) {
                int space = s.indexOf(' ', i);
                int size = Integer.valueOf(s.substring(i, space));
                res.add(s.substring(space + 1, space + size + 1));
                i = space + size + 1;
            }
            return res;
        }
    }

    void test(Codec obj, String[] strArray) {
        List<String> strs = Arrays.asList(strArray);
        String encoded = obj.encode(strs);
        assertEquals(strs, obj.decode(encoded));
    }

    public void test(Codec obj) {
        test(obj, new String[] {});
        test(obj, new String[] {""});
        test(obj, new String[] {"", ""});
        test(obj, new String[] {"a"});
        test(obj, new String[] {"a", "", ""});
        test(obj, new String[] {"a", ""});
        test(obj, new String[] {"", "b"});
        test(obj, new String[] {"a", "", "b"});
        test(obj, new String[] {"++"});
        test(obj, new String[] {"\\", "++", "\\"});
        test(obj, new String[] {"a", "++"});
        test(obj, new String[] {"a", "++", "bb"});
        test(obj, new String[] {"a+", "++", "+bb"});
        test(obj, new String[] {"a\\+", "++", "+bb"});
        test(obj, new String[] {"a\\+", "\\\\++", "++", "+bb"});
        test(obj, new String[] {"this", "is", "a", "test"});
        test(obj, new String[] {"this", "is", "1+1", "test"});
        test(obj, new String[] {"this", "i\\s", "1+1", "test"});
        test(obj, new String[] {"32"});
        test(obj, new String[] {"32", "14", "1+1", "test"});
        test(obj, new String[] {"a # b"});
    }

    @Test
    public void test() {
        test(new Codec1());
        test(new Codec1_2());
        test(new Codec2());
        test(new Codec2_2());
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("CodecStr");
    }
}
