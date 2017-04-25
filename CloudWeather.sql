# --------------------------------------------
# SQL script to create and populate the tables
# for the CloudDrive Database (CloudDriveDB)
# Created by Osman Balci
# --------------------------------------------

/*
Tables to be dropped must be listed in a logical order based on dependency.
UserFile and UserPhoto depend on User. Therefore, they must be dropped before User.
*/
/*DROP TABLE IF EXISTS UserFile, UserPhoto, User;*/
DROP TABLE IF EXISTS User;

/* The User table contains attributes of interest of a User. */
CREATE TABLE User
(
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    username VARCHAR (32) NOT NULL,
    password VARCHAR (32) NOT NULL,
    first_name VARCHAR (32) NOT NULL,
    middle_name VARCHAR (32),
    last_name VARCHAR (32) NOT NULL,
    address1 VARCHAR (128) NOT NULL,
    address2 VARCHAR (128),
    city VARCHAR (64) NOT NULL,
    state VARCHAR (2) NOT NULL,
    zipcode VARCHAR (10) NOT NULL, /* e.g., 24060-1804 */
    security_question INT NOT NULL, /* Refers to the number of the selected security question */
    security_answer VARCHAR (128) NOT NULL,
    email VARCHAR (128) NOT NULL,      
    PRIMARY KEY (id)
);

CREATE TABLE UserEvents
(
    id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT NOT NULL,
    user_id INT UNSIGNED,
    event_name VARCHAR(256) NOT NULL,
    latitude FLOAT NOT NULL,
    longitude FLOAT NOT NULL,   
    start_time BIGINT NOT NULL,
    end_time BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE
);
