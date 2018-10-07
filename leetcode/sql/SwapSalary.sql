-- LC627: https://leetcode.com/problems/swap-salary/

-- Given a table salary, such as the one below, that has m=male and f=female values. Swap all f and m values (i.e., change all f values to m and vice versa) with a single update query and no intermediate temp table.
-- For example:
-- | id | name | sex | salary |
-- |----|------|-----|--------|
-- | 1  | A    | m   | 2500   |
-- | 2  | B    | f   | 1500   |
-- | 3  | C    | m   | 5500   |
-- | 4  | D    | f   | 500    |
-- After running your query, the above salary table should have the following rows:
-- | id | name | sex | salary |
-- |----|------|-----|--------|
-- | 1  | A    | f   | 2500   |
-- | 2  | B    | m   | 1500   |
-- | 3  | C    | f   | 5500   |
-- | 4  | D    | m   | 500    |

-- Solution 1 (309 ms)
UPDATE salary
SET sex = IF(sex='f', "m", "f");

-- Solution 2 (320 ms)
UPDATE salary
SET sex = 
    CASE sex
        WHEN 'm' THEN 'f'
        ELSE 'm'
    END;

-- Solution 3 (323 ms)
UPDATE salary 
SET sex = CHAR(ASCII('f') ^ ASCII('m') ^ ASCII(sex));

-- Solution 3' (344 ms)
UPDATE salary 
SET sex = CHAR(ASCII('f') + ASCII('m') - ASCII(sex));
