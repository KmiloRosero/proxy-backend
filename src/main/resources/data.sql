INSERT INTO user_quota (user_id, plan, tokens_used, requests_this_minute,
last_minute_reset, monthly_reset_date, created_at)
VALUES ('user-001', 'FREE', 0, 0, CURRENT_TIMESTAMP,
DATEADD(MONTH,1,CURRENT_DATE), CURRENT_TIMESTAMP);

