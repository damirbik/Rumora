DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS roles CASCADE;
DROP FUNCTION IF EXISTS generate_10digit_id() CASCADE;

CREATE TABLE roles (
    role_id SERIAL PRIMARY KEY,
    role_name VARCHAR(50) UNIQUE NOT NULL
);

CREATE OR REPLACE FUNCTION generate_10digit_id()
RETURNS BIGINT
LANGUAGE plpgsql
AS $$
DECLARE
  new_id BIGINT;
BEGIN
  LOOP
    new_id := floor(random() * (9999999999 - 1000000000 + 1) + 1000000000)::BIGINT;
    IF NOT EXISTS (SELECT 1 FROM users WHERE user_id = new_id) THEN
      RETURN new_id;
    END IF;
  END LOOP;
END;
$$;

CREATE TABLE users (
    user_id BIGINT PRIMARY KEY DEFAULT generate_10digit_id(),
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(100) NOT NULL CHECK (length(password_hash) >= 8),
    role_id INTEGER NOT NULL,
    FOREIGN KEY (role_id) REFERENCES roles(role_id)
);

INSERT INTO roles (role_name) VALUES
('пользователь'),
('модератор'),
('администратор')
ON CONFLICT DO NOTHING;
