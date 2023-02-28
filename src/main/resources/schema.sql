DROP TABLE IF EXISTS User;

CREATE TABLE User
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(20)  NOT NULL UNIQUE,
    email    VARCHAR(100) NOT NULL
);

DROP TABLE IF EXISTS Course;

CREATE TABLE Course
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    code        VARCHAR(10) NOT NULL UNIQUE,
    name        VARCHAR(20) NOT NULL UNIQUE,
    description VARCHAR(500)
);

DROP TABLE IF EXISTS Enrollment;

CREATE TABLE Enrollment
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    username     VARCHAR(20) NOT NULL,
    registerDate TIMESTAMP,
    courseCode   VARCHAR(10)
);
