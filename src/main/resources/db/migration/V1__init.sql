CREATE TABLE notification_log (
      id BIGINT AUTO_INCREMENT PRIMARY KEY,
      user_id BIGINT NOT NULL,
      user_name VARCHAR(100) NOT NULL,
      message TEXT NOT NULL,
      channel VARCHAR(30) NOT NULL,
      category VARCHAR(30) NOT NULL,
      status VARCHAR(20) NOT NULL,
      timestamp TIMESTAMP NOT NULL
);