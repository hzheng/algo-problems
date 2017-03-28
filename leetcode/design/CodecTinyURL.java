import java.util.*;
import java.util.concurrent.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC535: https://leetcode.com/problems/encode-and-decode-tinyurl/
//
// Design the encode and decode methods for the TinyURL service. There is no
// restriction on how your encode/decode algorithm should work. You just need to
// ensure that a URL can be encoded to a tiny URL and the tiny URL can be decoded
// to the original URL.
public class CodecTinyURL {
    interface Codec {
        // Encodes a URL to a shortened URL.
        public String encode(String longUrl);

        // Decodes a shortened URL to its original URL.
        public String decode(String shortUrl);
    }

    private static final String PREFIX= "http://tinyurl.com/";
    private static final char[] BASE_CHARS =
        "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
    private static final int BASE = BASE_CHARS.length;
    private static final int LEN = 6;

    // beats 35.35%(8 ms for 739 tests)
    static class Codec1 implements Codec {
        private static final long MAX_NUM = (long)Math.pow(BASE, LEN);

        private Map<String, String> map = new HashMap<>();

        public String encode(String longUrl) {
            while (true) {
                long n = ThreadLocalRandom.current().nextLong(MAX_NUM);
                char[] s = new char[LEN];
                for (int i = s.length - 1; i >= 0; i--, n /= BASE) {
                    s[i] = BASE_CHARS[(int)(n % BASE)];
                }
                String key = String.valueOf(s);
                if (!map.containsKey(key)) {
                    map.put(key, longUrl);
                    return PREFIX + key;
                }
            }
        }

        public String decode(String shortUrl) {
            if (shortUrl == null || !shortUrl.startsWith(PREFIX)) return null;

            return map.get(shortUrl.substring(PREFIX.length()));
        }
    }

    // Hash Table
    // beats 35.35%(8 ms for 739 tests)
    static class Codec2 implements Codec {
        private Map<String, String> map = new HashMap<>();

        public String encode(String longUrl) {
            while (true) {
                char[] s = new char[LEN];
                for (int i = s.length - 1; i >= 0; i--) {
                    s[i] = BASE_CHARS[ThreadLocalRandom.current().nextInt(BASE)];
                }
                String key = String.valueOf(s);
                if (!map.containsKey(key)) {
                    map.put(key, longUrl);
                    return PREFIX + key;
                }
            }
        }

        public String decode(String shortUrl) {
            if (shortUrl == null || !shortUrl.startsWith(PREFIX)) return null;

            return map.get(shortUrl.substring(PREFIX.length()));
        }
    }

    // Hash Table
    // beats 5.86%(26 ms for 739 tests)
    static class Codec3 implements Codec {
        private Map<Integer, String> map = new HashMap<>();
        private Random r = new Random();
        private int key = r.nextInt(Integer.MAX_VALUE);

        public String encode(String longUrl) {
            while (map.containsKey(key)) {
                key = r.nextInt(Integer.MAX_VALUE);
            }
            map.put(key, longUrl);
            return PREFIX + key;
        }

        public String decode(String shortUrl) {
            return map.get(Integer.parseInt(shortUrl.replace(PREFIX, "")));
        }
    }

    void test(Codec obj, String url, String name) {
        String encoded = obj.encode(url);
        System.out.println(name + " shorten from " + url + " to " + encoded);
        // assertEquals(PREFIX.length() + LEN, encoded.length());
        assertEquals(url, obj.decode(encoded));
    }

    public void test(Codec obj, String name) {
        test(obj, "https://leetcode.com/problems/design-tinyurl", name);
        test(obj, "https://google.com", name);
    }

    @Test
    public void test() {
        test(new Codec1(), "Codec1");
        test(new Codec2(), "Codec2");
        test(new Codec3(), "Codec3");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("CodecTinyURL");
    }
}
