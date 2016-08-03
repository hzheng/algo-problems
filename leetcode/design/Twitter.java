import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.Interval;

// https://leetcode.com/problems/design-twitter/
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

    // beats 46.34%(189 ms)
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
            List<Tweet> tweets = new ArrayList<>();

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
                tweets.add(new Tweet(tweetId, tweetNo));
            }

            public List<Integer> getNewsFeed(int n) {
                List<Tweet> topFeeds = new ArrayList<>();
                for (User followee : followees) {
                    List<Tweet> feeds = followee.tweets;
                    for (int i = feeds.size() - 1, count = n;
                         i >= 0 && count > 0; count--, i--) {
                        topFeeds.add(feeds.get(i));
                    }
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

        public Twitter1() {
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
            return getUser(userId).getNewsFeed(3 /*10*/);
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
            System.out.println(i+"===="+Arrays.toString(expected));
            assertArrayEquals(feeds, twitter.getNewsFeed(i++).toArray(new Integer[0]));
            System.out.println("passed "+(i-1));
        }
    }

    void test(int[][] posts, int[][] follows, Integer[] ... expected) {
        test(new Twitter1(), posts, follows, expected);
    }

    @Test
    public void test1() {
        test(new int[][] {{1, 1}, {2, 2}, {3, 5}, {2, 6}, {4, 7}, {5, 8}, {3, 9}},
             new int[][] {{1, 2}, {2, 1}, {3, 1}, {5, 2}, {3, 5}, {1, 5}},
             new Integer[] {8, 6, 2}, new Integer[] {6, 2, 1},
             new Integer[] {9, 8, 5}, new Integer[] {7}, new Integer[] {8, 6, 2});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("Twitter");
    }
}
