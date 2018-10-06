-- LC180: https://leetcode.com/problems/consecutive-numbers/

-- Find all numbers that appear at least three times consecutively.

-- +----+-----+
-- | Id | Num |
-- +----+-----+
-- | 1  |  1  |
-- | 2  |  1  |
-- | 3  |  1  |
-- | 4  |  2  |
-- | 5  |  1  |
-- | 6  |  2  |
-- | 7  |  2  |
-- +----+-----+

-- Solution 1
SELECT DISTINCT l1.Num ConsecutiveNums
FROM
    Logs l1,
    Logs l2,
    Logs l3
WHERE
    l1.Id = l2.Id - 1
    AND l2.Id = l3.Id - 1
    AND l1.Num = l2.Num
    AND l2.Num = l3.Num
;

-- Solution 2
SELECT DISTINCT num ConsecutiveNums
FROM
    (SELECT num,
        CASE 
            WHEN @num = num THEN @count := @count + 1
            WHEN @num <> @num := num THEN @count := 1
        END count
        FROM 
            Logs, (SELECT @count := 0, @num := (SELECT num FROM Logs LIMIT 1)) r
    ) countedNum
WHERE count >= 3;

-- Solution 3
SELECT DISTINCT num ConsecutiveNums
FROM
    (SELECT num,
        CASE
            WHEN @prev = Num THEN @count := @count + 1
            WHEN (@prev := Num) THEN @count := 1
        END count
    FROM Logs, (SELECT @prev := NULL, @count := 0) r
) countedNum 
WHERE count >= 3;