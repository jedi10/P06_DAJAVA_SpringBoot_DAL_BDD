#DROP DATABASE IF EXISTS transfer;
#CREATE DATABASE transfer CHARACTER SET utf8 COLLATE utf8_general_ci;
#CREATE DATABASE IF NOT EXISTS `transfer` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
#USE `transfer`;

#CREATE TABLE country (
#        id   INTEGER      NOT NULL AUTO_INCREMENT,
#        name VARCHAR(128) NOT NULL,
#        PRIMARY KEY (id)
#);

CREATE TABLE `user_friends` (`user_fk` bigint(20) NOT NULL,
                            `friend_fk` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `user_friends`
    ADD KEY `FKjy5a5kiuwcgp74kre9etoc452` (`friend_fk`),
    ADD KEY `FK5s2lepty4xpqn2gwwaa7qk92v` (`user_fk`);
#https://www.baeldung.com/spring-boot-data-sql-and-schema-sql
#https://dev.mysql.com/doc/refman/8.0/en/charset-database.html