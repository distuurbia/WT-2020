CREATE DATABASE IF NOT EXISTS epam;
USE epam;
CREATE TABLE IF NOT EXISTS
    users
(
    user_id            INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    user_name          VARCHAR(255) UNIQUE NOT NULL,
    user_password_hash VARCHAR(255),
    user_bank          INT,
    user_authority_lvl INT
);
CREATE TABLE IF NOT EXISTS
    horses
(
    horse_id           INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    horse_name         VARCHAR(32) UNIQUE NOT NULL,
    horse_wins_counter INT
);
CREATE TABLE IF NOT EXISTS
    races
(
    race_id        INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    race_date      BIGINT,
    race_status    INT,
    race_winner    VARCHAR(32)
);
CREATE TABLE IF NOT EXISTS
    racehorses
(
    racehorse_race_id   INT NOT NULL,
    racehorse_horse_id  INT NOT NULL,
    FOREIGN KEY (racehorse_race_id) REFERENCES races (race_id) ON DELETE CASCADE,
    FOREIGN KEY (racehorse_horse_id) REFERENCES horses (horse_id) ON DELETE RESTRICT
);
CREATE TABLE IF NOT EXISTS
    bets
(
    bet_id          INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    bet_amount      FLOAT,
    bet_race_id     INT,
    bet_horse_id    INT,
    bet_user_id     INT,
    FOREIGN KEY (bet_race_id) REFERENCES races (race_id) ON DELETE RESTRICT,
    FOREIGN KEY (bet_horse_id) REFERENCES horses (horse_id) ON DELETE RESTRICT,
    FOREIGN KEY (bet_user_id) REFERENCES users (user_id) ON DELETE CASCADE
);