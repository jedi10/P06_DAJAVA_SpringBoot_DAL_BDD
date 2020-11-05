-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : jeu. 05 nov. 2020 à 17:30
-- Version du serveur :  10.4.14-MariaDB
-- Version de PHP : 7.4.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `paymybuddy`
--
CREATE DATABASE IF NOT EXISTS `paymybuddy` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `paymybuddy`;

-- --------------------------------------------------------

--
-- Structure de la table `app_account`
--

CREATE TABLE `app_account` (
  `app_account_id` bigint(20) NOT NULL,
  `app_account_status` varchar(15) DEFAULT NULL,
  `last_connection` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `status_date` date DEFAULT NULL,
  `user_fk` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- RELATIONS POUR LA TABLE `app_account`:
--   `user_fk`
--       `user` -> `user_id`
--

-- --------------------------------------------------------

--
-- Structure de la table `bank_account`
--

CREATE TABLE `bank_account` (
  `bank_account_id` bigint(20) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `name` varchar(55) DEFAULT NULL,
  `tel_number` varchar(55) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- RELATIONS POUR LA TABLE `bank_account`:
--

-- --------------------------------------------------------

--
-- Structure de la table `external_transaction`
--

CREATE TABLE `external_transaction` (
  `ext_transaction_id` bigint(20) NOT NULL,
  `amount` double NOT NULL,
  `description` varchar(350) DEFAULT NULL,
  `status` varchar(15) DEFAULT NULL,
  `status_date` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `int_cash_account_fk` bigint(20) DEFAULT NULL,
  `bank_account_fk` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- RELATIONS POUR LA TABLE `external_transaction`:
--   `bank_account_fk`
--       `bank_account` -> `bank_account_id`
--   `int_cash_account_fk`
--       `int_cash_account` -> `int_cash_account_id`
--

-- --------------------------------------------------------

--
-- Structure de la table `hibernate_sequence`
--

CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- RELATIONS POUR LA TABLE `hibernate_sequence`:
--

-- --------------------------------------------------------

--
-- Structure de la table `internal_transaction`
--

CREATE TABLE `internal_transaction` (
  `int_transaction_id` bigint(20) NOT NULL,
  `amount` double NOT NULL,
  `description` varchar(350) DEFAULT NULL,
  `status` varchar(15) DEFAULT NULL,
  `status_date` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `transaction_message` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- RELATIONS POUR LA TABLE `internal_transaction`:
--

-- --------------------------------------------------------

--
-- Structure de la table `int_cash_account`
--

CREATE TABLE `int_cash_account` (
  `int_cash_account_id` bigint(20) NOT NULL,
  `amount` double NOT NULL,
  `currency` varchar(35) DEFAULT NULL,
  `libelle` varchar(255) DEFAULT NULL,
  `number` varchar(35) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- RELATIONS POUR LA TABLE `int_cash_account`:
--

-- --------------------------------------------------------

--
-- Structure de la table `money_transfer_type`
--

CREATE TABLE `money_transfer_type` (
  `int_cash_account_id` bigint(20) NOT NULL,
  `int_transaction_id` bigint(20) NOT NULL,
  `is_credit` bit(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- RELATIONS POUR LA TABLE `money_transfer_type`:
--   `int_transaction_id`
--       `internal_transaction` -> `int_transaction_id`
--   `int_cash_account_id`
--       `int_cash_account` -> `int_cash_account_id`
--

-- --------------------------------------------------------

--
-- Structure de la table `user`
--

CREATE TABLE `user` (
  `user_id` bigint(20) NOT NULL,
  `email` varchar(100) NOT NULL,
  `first_name` varchar(55) DEFAULT NULL,
  `last_name` varchar(55) DEFAULT NULL,
  `password` varchar(100) NOT NULL,
  `bank_account_fk` bigint(20) DEFAULT NULL,
  `int_cash_account_fk` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- RELATIONS POUR LA TABLE `user`:
--   `bank_account_fk`
--       `bank_account` -> `bank_account_id`
--   `int_cash_account_fk`
--       `int_cash_account` -> `int_cash_account_id`
--

-- --------------------------------------------------------

--
-- Structure de la table `user_friends`
--

CREATE TABLE `user_friends` (
  `user_fk` bigint(20) NOT NULL,
  `friend_fk` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- RELATIONS POUR LA TABLE `user_friends`:
--   `user_fk`
--       `user` -> `user_id`
--   `friend_fk`
--       `user` -> `user_id`
--

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `app_account`
--
ALTER TABLE `app_account`
  ADD PRIMARY KEY (`app_account_id`),
  ADD KEY `FKhq22r3ihi9mrkubvir5t05ftw` (`user_fk`);

--
-- Index pour la table `bank_account`
--
ALTER TABLE `bank_account`
  ADD PRIMARY KEY (`bank_account_id`);

--
-- Index pour la table `external_transaction`
--
ALTER TABLE `external_transaction`
  ADD PRIMARY KEY (`ext_transaction_id`),
  ADD KEY `FK6ej5od3qk6i54cl8d6ehs3q1v` (`int_cash_account_fk`),
  ADD KEY `FK6bfjokk6wyn8dk8yt787vhv3d` (`bank_account_fk`);

--
-- Index pour la table `internal_transaction`
--
ALTER TABLE `internal_transaction`
  ADD PRIMARY KEY (`int_transaction_id`);

--
-- Index pour la table `int_cash_account`
--
ALTER TABLE `int_cash_account`
  ADD PRIMARY KEY (`int_cash_account_id`),
  ADD UNIQUE KEY `UKmywhexpaculaopjxve6ed8jeo` (`number`);

--
-- Index pour la table `money_transfer_type`
--
ALTER TABLE `money_transfer_type`
  ADD PRIMARY KEY (`int_cash_account_id`,`int_transaction_id`),
  ADD KEY `FKbmgetg6nqvmd6lt6wb8ptevul` (`int_transaction_id`);

--
-- Index pour la table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `UKoso07pudw19e66bs4yp8hwpux` (`email`),
  ADD KEY `FK8qcf3py0sxw0ifjhm3jbfbx5r` (`bank_account_fk`),
  ADD KEY `FKgjdjgcxvookjp36ly616u2ftn` (`int_cash_account_fk`);

--
-- Index pour la table `user_friends`
--
ALTER TABLE `user_friends`
  ADD KEY `FKjy5a5kiuwcgp74kre9etoc452` (`friend_fk`),
  ADD KEY `FK5s2lepty4xpqn2gwwaa7qk92v` (`user_fk`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `app_account`
--
ALTER TABLE `app_account`
  MODIFY `app_account_id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `bank_account`
--
ALTER TABLE `bank_account`
  MODIFY `bank_account_id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `external_transaction`
--
ALTER TABLE `external_transaction`
  MODIFY `ext_transaction_id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `internal_transaction`
--
ALTER TABLE `internal_transaction`
  MODIFY `int_transaction_id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `int_cash_account`
--
ALTER TABLE `int_cash_account`
  MODIFY `int_cash_account_id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `user`
--
ALTER TABLE `user`
  MODIFY `user_id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `app_account`
--
ALTER TABLE `app_account`
  ADD CONSTRAINT `FKhq22r3ihi9mrkubvir5t05ftw` FOREIGN KEY (`user_fk`) REFERENCES `user` (`user_id`);

--
-- Contraintes pour la table `external_transaction`
--
ALTER TABLE `external_transaction`
  ADD CONSTRAINT `FK6bfjokk6wyn8dk8yt787vhv3d` FOREIGN KEY (`bank_account_fk`) REFERENCES `bank_account` (`bank_account_id`),
  ADD CONSTRAINT `FK6ej5od3qk6i54cl8d6ehs3q1v` FOREIGN KEY (`int_cash_account_fk`) REFERENCES `int_cash_account` (`int_cash_account_id`);

--
-- Contraintes pour la table `money_transfer_type`
--
ALTER TABLE `money_transfer_type`
  ADD CONSTRAINT `FKbmgetg6nqvmd6lt6wb8ptevul` FOREIGN KEY (`int_transaction_id`) REFERENCES `internal_transaction` (`int_transaction_id`),
  ADD CONSTRAINT `FKgexyp7145p7efe4g9meprgixn` FOREIGN KEY (`int_cash_account_id`) REFERENCES `int_cash_account` (`int_cash_account_id`);

--
-- Contraintes pour la table `user`
--
ALTER TABLE `user`
  ADD CONSTRAINT `FK8qcf3py0sxw0ifjhm3jbfbx5r` FOREIGN KEY (`bank_account_fk`) REFERENCES `bank_account` (`bank_account_id`),
  ADD CONSTRAINT `FKgjdjgcxvookjp36ly616u2ftn` FOREIGN KEY (`int_cash_account_fk`) REFERENCES `int_cash_account` (`int_cash_account_id`);

--
-- Contraintes pour la table `user_friends`
--
ALTER TABLE `user_friends`
  ADD CONSTRAINT `FK5s2lepty4xpqn2gwwaa7qk92v` FOREIGN KEY (`user_fk`) REFERENCES `user` (`user_id`),
  ADD CONSTRAINT `FKjy5a5kiuwcgp74kre9etoc452` FOREIGN KEY (`friend_fk`) REFERENCES `user` (`user_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
