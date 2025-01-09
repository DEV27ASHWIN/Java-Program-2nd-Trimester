CREATE DATABASE IF NOT EXISTS medicine_db;
USE medicine_db;

-- 1) Users table
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    salt VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2) Medicines table
CREATE TABLE IF NOT EXISTS medicines (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    dosage VARCHAR(50) DEFAULT '',
    quantity INT DEFAULT 0,
    threshold INT DEFAULT 5,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 3) Schedules table
CREATE TABLE IF NOT EXISTS schedules (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    medicine_id INT NOT NULL,
    time_of_day ENUM('MORNING','AFTERNOON','EVENING') NOT NULL,
    meal_timing ENUM('BEFORE_MEAL','AFTER_MEAL') NOT NULL,
    is_taken TINYINT(1) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (medicine_id) REFERENCES medicines(id) ON DELETE CASCADE
);

-- Optional test user (plaintext password "testpass", salt "plaintextsalt")
INSERT INTO users (username, email, password, salt)
VALUES ('testuser', 'test@example.com', 'testpass', 'plaintextsalt');

-- Optional sample medicine
INSERT INTO medicines (user_id, name, dosage, quantity, threshold)
VALUES (1, 'Vitamin C', '500mg', 10, 3);

-- Optional schedule
INSERT INTO schedules (user_id, medicine_id, time_of_day, meal_timing)
VALUES (1, 1, 'MORNING', 'BEFORE_MEAL');
