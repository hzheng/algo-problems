import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.Interval;

// LC355: https://leetcode.com/problems/design-twitter/
//
// Design a simplified version of Twitter where users can post tweets,
// follow/unfollow another user and is able to see the 10 most recent tweets in
// the user's news feed. Your design should support the following methods:
// postTweet(userId, tweetId): Compose a new tweet.
// getNewsFeed(userId): Retrieve the 10 most recent tweet ids in the user's news
// feed. Each item in the news feed must be posted by users who the user
// followed or by the user herself. Tweets must be ordered from most recent to least recent.
// follow(followerId, followeeId): Follower follows a followee.
// unfollow(followerId, followeeId): Follower unfollows a followee.
public class Twitter {
    static interface ITwitter {
        /** Compose a new tweet. */
        public void postTweet(int userId, int tweetId);

        /** Retrieve the 10 most recent tweet ids in the user's news feed. Each item in the news feed must be posted by users who the user followed or by the user herself. Tweets must be ordered from most recent to least recent. */
        public List<Integer> getNewsFeed(int userId);

        /** Follower follows a followee. If the operation is invalid, it should be a no-op. */
        public void follow(int followerId, int followeeId);

        /** Follower unfollows a followee. If the operation is invalid, it should be a no-op. */
        public void unfollow(int followerId, int followeeId);
    }

    // Hash Table
    // beats 75.85%(159 ms for 23 tests)
    static class Twitter1 implements ITwitter {
        static class Tweet implements Comparable<Tweet> {
            int id;
            int timestamp;

            public Tweet(int id, int timestamp) {
                this.id = id;
                this.timestamp = timestamp;
            }

            public int compareTo(Tweet other) {
                return other.timestamp - timestamp;
            }
        }

        static class User {
            int id;
            Set<User> followees = new HashSet<>();
            List<Tweet> tweets = new LinkedList<>();

            public User(int id) {
                this.id = id;
                followees.add(this);
            }

            public void follow(User followee) {
                followees.add(followee);
            }

            public void unfollow(User followee) {
                if (followee != this) {
                    followees.remove(followee);
                }
            }

            public void postTweet(int tweetId, int tweetNo) {
                tweets.add(0, new Tweet(tweetId, tweetNo));
                if (tweets.size() > 10) { // better change to feedCount
                    tweets.remove(tweets.size() - 1);
                }
            }

            public List<Integer> getNewsFeed(int n) {
                List<Tweet> topFeeds = new ArrayList<>(n);
                for (User followee : followees) {
                    topFeeds.addAll(followee.tweets);
                }
                Collections.sort(topFeeds);
                List<Integer> feeds = new ArrayList<>();
                int i = 1;
                for (Tweet feed : topFeeds) {
                    feeds.add(feed.id);
                    if (++i > n) break;
                }
                return feeds;
            }
        }

        private Map<Integer, User> users = new HashMap<>();

        private int tweetNo = 0;

        private int feedCount = 10;

        public Twitter1() {
        }

        public Twitter1(int feedCount) {
            this.feedCount = feedCount;
        }

        private User getUser(int userId) {
            if (!users.containsKey(userId)) {
                users.put(userId, new User(userId));
            }
            return users.get(userId);
        }

        public void postTweet(int userId, int tweetId) {
            getUser(userId).postTweet(tweetId, tweetNo++);
        }

        public List<Integer> getNewsFeed(int userId) {
            return getUser(userId).getNewsFeed(feedCount);
        }

        public void follow(int followerId, int followeeId) {
            getUser(followerId).follow(getUser(followeeId));
        }

        public void unfollow(int followerId, int followeeId) {
            getUser(followerId).unfollow(getUser(followeeId));
        }
    }

    // Solution of Choice
    // Hash Table + Heap
    // beats 77.91%(158 ms for 23 tests)
    static class Twitter2 implements ITwitter {
        static class Tweet implements Comparable<Tweet> {
            int id;
            int timestamp;
            Tweet next;

            public Tweet(int id, int timestamp) {
                this.id = id;
                this.timestamp = timestamp;
            }

            public int compareTo(Tweet other) {
                return other.timestamp - timestamp;
            }
        }

        static class User {
            int id;
            Set<User> followees = new HashSet<>();
            Tweet tweet;

            public User(int id) {
                this.id = id;
                follow(this);
            }

            public void follow(User followee) {
                followees.add(followee);
            }

            public void unfollow(User followee) {
                if (followee != this) {
                    followees.remove(followee);
                }
            }

            public void postTweet(int tweetId, int tweetNo) {
                Tweet newTweet = new Tweet(tweetId, tweetNo);
                newTweet.next = tweet;
                tweet = newTweet;
            }

            public List<Integer> getNewsFeed(int n) {
                PriorityQueue<Tweet> pq = new PriorityQueue<>();
                for (User followee : followees) {
                    Tweet tweet = followee.tweet;
                    if (tweet != null) {
                        pq.add(tweet);
                    }
                }
                List<Integer> feeds = new ArrayList<>(n); // change to feedCount
                for (int i = n; i > 0 && !pq.isEmpty(); i--) {
                    Tweet tweet = pq.poll();
                    feeds.add(tweet.id);
                    if (tweet.next != null) {
                        pq.add(tweet.next);
                    }
                }
                return feeds;
            }
        }

        private Map<Integer, User> users = new HashMap<>();

        private int tweetNo = 0;

        private int feedCount = 10;

        public Twitter2() {
        }

        public Twitter2(int feedCount) {
            this.feedCount = feedCount;
        }

        private User getUser(int userId) {
            if (!users.containsKey(userId)) {
                users.put(userId, new User(userId));
            }
            return users.get(userId);
        }

        public void postTweet(int userId, int tweetId) {
            getUser(userId).postTweet(tweetId, tweetNo++);
        }

        public List<Integer> getNewsFeed(int userId) {
            return getUser(userId).getNewsFeed(feedCount);
        }

        public void follow(int followerId, int followeeId) {
            getUser(followerId).follow(getUser(followeeId));
        }

        public void unfollow(int followerId, int followeeId) {
            getUser(followerId).unfollow(getUser(followeeId));
        }
    }

    void test(ITwitter twitter, int[][] posts, int[][] follows,
              Integer[] ... expected) {
        for (int[] post : posts) {
            twitter.postTweet(post[0], post[1]);
        }
        for (int[] follow : follows) {
            twitter.follow(follow[0], follow[1]);
        }
        int i = 1;
        for (Integer[] feeds : expected) {
            assertArrayEquals(feeds, twitter.getNewsFeed(i++).toArray(new Integer[0]));
        }
    }

    void test(int[][] posts, int[][] follows, Integer[] ... expected) {
        test(new Twitter1(3), posts, follows, expected);
        test(new Twitter2(3), posts, follows, expected);
    }

    void test2(ITwitter twitter, int[][] posts, int[][] unfollows,
               Integer[] ... expected) {
        for (int[] post : posts) {
            twitter.postTweet(post[0], post[1]);
        }
        for (int[] unfollow : unfollows) {
            twitter.unfollow(unfollow[0], unfollow[1]);
        }
        int i = 1;
        for (Integer[] feeds : expected) {
            assertArrayEquals(feeds, twitter.getNewsFeed(i++).toArray(new Integer[0]));
        }
    }

    void test2(int[][] posts, int[][] unfollows, Integer[] ... expected) {
        test2(new Twitter1(3), posts, unfollows, expected);
        test2(new Twitter2(3), posts, unfollows, expected);
    }

    @Test
    public void test1() {
        test(new int[][] {{1, 1}, {2, 2}, {3, 5}, {2, 6}, {4, 7}, {5, 8}, {3, 9}},
             new int[][] {{1, 2}, {2, 1}, {3, 1}, {5, 2}, {3, 5}, {1, 5}},
             new Integer[] {8, 6, 2}, new Integer[] {6, 2, 1},
             new Integer[] {9, 8, 5}, new Integer[] {7}, new Integer[] {8, 6, 2});
        test2(new int[][] {{1, 5}}, new int[][] {{1, 1}}, new Integer[] {5});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("Twitter");
    }
}
