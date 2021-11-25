# Rambot

Build with JAVA, API JDA and MySql

prerequisite :

A web server like WAMP
An IDE like Eclipse
A bot token ready to use

Installation :

Add a .env file in Rambot/assets/ and fill it with :

TOKEN=[your bot token]
URL=[url DataBase]
USER=[user Database]
PWD=[pwd Database]
PREFIX=[prefix to use(like "-"]
OWNER=(ID discord of admin user)

Add a database in your wamp server in the format : {Read bellow}

Launch your bot with your IDE (or snapshot if everything is ready)

--------------------------------------------------DATABASE TO ADD--------------------------------------------
-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le : mer. 24 nov. 2021 à 11:05
-- Version du serveur :  5.7.31
-- Version de PHP : 7.3.21

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `rambot`
--

-- --------------------------------------------------------

--
-- Structure de la table `inventaire`
--

DROP TABLE IF EXISTS `inventaire`;
CREATE TABLE IF NOT EXISTS `inventaire` (
  `id` int(11) NOT NULL,
  `Army` text NOT NULL,
  `Objets` text NOT NULL,
  KEY `inventaire_ibfk_2` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `langues`
--

DROP TABLE IF EXISTS `langues`;
CREATE TABLE IF NOT EXISTS `langues` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `libelle` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `personnage`
--

DROP TABLE IF EXISTS `personnage`;
CREATE TABLE IF NOT EXISTS `personnage` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `Nom` varchar(30) NOT NULL,
  `StarRate` int(5) NOT NULL,
  `img` varchar(40) NOT NULL,
  `imgIcone` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `produits`
--

DROP TABLE IF EXISTS `produits`;
CREATE TABLE IF NOT EXISTS `produits` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nom` text,
  `nomFr` varchar(30) NOT NULL,
  `Description` text,
  `Prix` int(11) DEFAULT NULL,
  `icone` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `server`
--

DROP TABLE IF EXISTS `server`;
CREATE TABLE IF NOT EXISTS `server` (
  `id` varchar(30) NOT NULL,
  `name` varchar(40) NOT NULL,
  `detention` varchar(30) DEFAULT NULL,
  `idLangue` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `idLangue` (`idLangue`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user` (
  `idBot` int(11) NOT NULL AUTO_INCREMENT,
  `idUser` varchar(30) NOT NULL,
  `idServeur` varchar(30) NOT NULL,
  `nameUser` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `bourse` int(11) NOT NULL,
  `affection` int(11) NOT NULL,
  `teamComp` varchar(20) NOT NULL DEFAULT ' ',
  PRIMARY KEY (`idBot`),
  KEY `idServeur` (`idServeur`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déclencheurs `user`
--
DROP TRIGGER IF EXISTS `insertInvetaire`;
DELIMITER $$
CREATE TRIGGER `insertInvetaire` AFTER INSERT ON `user` FOR EACH ROW BEGIN
  INSERT INTO Inventaire (id)
  VALUES(new.idBot);
END
$$
DELIMITER ;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `inventaire`
--
ALTER TABLE `inventaire`
  ADD CONSTRAINT `inventaire_ibfk_2` FOREIGN KEY (`id`) REFERENCES `user` (`idBot`);

--
-- Contraintes pour la table `server`
--
ALTER TABLE `server`
  ADD CONSTRAINT `server_ibfk_1` FOREIGN KEY (`idLangue`) REFERENCES `langues` (`id`);

--
-- Contraintes pour la table `user`
--
ALTER TABLE `user`
  ADD CONSTRAINT `user_ibfk_1` FOREIGN KEY (`idServeur`) REFERENCES `server` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
