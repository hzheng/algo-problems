--  LC178: https://leetcode.com/problems/rank-scores/

-- Write a SQL query to rank scores. If there is a tie between two scores, both should have the same ranking. Note that after a tie, the next ranking number should be the next consecutive integer value. In other words, there should be no "holes" between ranks.

-- +----+-------+
-- | Id | Score |
-- +----+-------+
-- | 1  | 3.50  |
-- | 2  | 3.65  |
-- | 3  | 4.00  |
-- | 4  | 3.85  |
-- | 5  | 4.00  |
-- | 6  | 3.65  |
-- +----+-------+
-- For example, given the above Scores table, your query should generate the following report (order by highest score):

-- +-------+------+
-- | Score | Rank |
-- +-------+------+
-- | 4.00  | 1    |
-- | 4.00  | 1    |
-- | 3.85  | 2    |
-- | 3.65  | 3    |
-- | 3.65  | 3    |
-- | 3.50  | 4    |
-- +-------+------+

-- Solution 1
 SELECT Scores.Score, COUNT(Distinct_Score.Score) RANK
 FROM Scores
     , (
       SELECT DISTINCT Score
         FROM Scores
       ) Distinct_Score
 WHERE Scores.Score <= Distinct_Score.Score
 GROUP BY Scores.Id, Scores.Score
 ORDER BY Scores.Score DESC;

-- Solution 2
SELECT s.Score, COUNT(DISTINCT t.score) Rank
FROM Scores s JOIN Scores t ON s.Score <= t.score
GROUP BY s.Id
ORDER BY s.Score DESC;

-- Solution 3
SELECT Score, (
	SELECT COUNT(DISTINCT s2.Score) + 1
	FROM Scores s2
	WHERE s1.Score < s2.Score
) Rank
FROM Scores s1
ORDER BY Score DESC;

-- Solution 3'
SELECT Score, (
  SELECT COUNT(DISTINCT Score)
  FROM Scores WHERE s.Score <= Score
) Rank
FROM Scores s
ORDER BY Score DESC;

-- Solution 4
SELECT Score,
  (SELECT COUNT(*) FROM (SELECT DISTINCT Score s FROM Scores) tmp WHERE s >= Score) Rank
FROM Scores
ORDER BY Score DESC;

-- Solution 5
SELECT
  Score,
  @rank := @rank + (@prev <> (@prev := Score)) Rank
FROM
  Scores,
  (SELECT @rank := 0, @prev := -1) init
ORDER BY Score DESC;