-- LC197: https://leetcode.com/problems/rising-temperature/

-- Given a Weather table, write a SQL query to find all dates' Ids with higher 
-- temperature compared to its previous (yesterday's) dates.

-- +---------+------------------+------------------+
-- | Id(INT) | RecordDate(DATE) | Temperature(INT) |
-- +---------+------------------+------------------+
-- |       1 |       2015-01-01 |               10 |
-- |       2 |       2015-01-02 |               25 |
-- |       3 |       2015-01-03 |               20 |
-- |       4 |       2015-01-04 |               30 |
-- +---------+------------------+------------------+
-- For example, return the following Ids for the above Weather table:

-- +----+
-- | Id |
-- +----+
-- |  2 |
-- |  4 |
-- +----+

-- Solution 1 (557 ms)
SELECT a.Id FROM Weather a, Weather b
WHERE DATEDIFF(a.RecordDate, b.RecordDate) = 1 AND a.Temperature > b.Temperature;

-- Solution 2 (332 ms)
SELECT
    a.id
FROM weather a JOIN weather b 
    ON DATEDIFF(a.RecordDate, b.RecordDate) = 1
       AND a.Temperature > b.Temperature;

-- Solution 3 (195 ms)
SELECT Id FROM (
    SELECT CASE
        WHEN Temperature > @prevTemp AND DATEDIFF(RecordDate, @prevDate) = 1 
        THEN Id 
        ELSE NULL 
    END Id,
    @prevtemp:=Temperature, @prevdate:=RecordDate
    FROM Weather, (SELECT @prevTemp:=NULL, @prevDate:=NULL) A ORDER BY RecordDate ASC
) AS D WHERE Id IS NOT NULL;