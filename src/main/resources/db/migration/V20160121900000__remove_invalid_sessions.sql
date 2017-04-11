-- On user_session
DELETE FROM user_session WHERE use_ses_id IN (SELECT
  use_ses_id
FROM (SELECT
  *
FROM user_session AS u1,
     session_device AS s1
WHERE u1.use_ses_id = s1.dev_session_id
HAVING u1.use_ses_last_successful_login < (SELECT
  MAX(use_ses_last_successful_login)
FROM (SELECT
  *
FROM session_device s,
     user_session u
WHERE s.dev_session_id = u.use_ses_id
AND dev_device_id IN (SELECT
  dev_device_id
FROM (SELECT
  *
FROM (SELECT
  dev_device_id,
  COUNT(*) AS numb
FROM session_device
WHERE dev_device_id IS NOT NULL
GROUP BY dev_device_id) AS t
HAVING numb > 1) AS m)
ORDER BY s.dev_device_id) AS e
WHERE e.dev_device_id = s1.dev_device_id)) AS p);

-- On session_device
DELETE FROM session_device
WHERE dev_session_id NOT IN (SELECT
    use_ses_id
  FROM user_session);