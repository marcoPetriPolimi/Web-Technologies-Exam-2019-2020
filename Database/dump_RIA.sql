CREATE SCHEMA `ria` DEFAULT CHARACTER SET utf8 COLLATE utf8_bin;

CREATE TABLE IF NOT EXISTS `User` (
	`Id` INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	`Name` VARCHAR(30) UNIQUE NOT NULL,
	`Email` VARCHAR(100) NOT NULL,
	`Password` VARCHAR(30) NOT NULL
) AUTO_INCREMENT = 1, CHARSET = utf8;

CREATE TABLE IF NOT EXISTS `Account` (
	`Id` INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	`Owner` INT UNSIGNED NOT NULL,
	`Balance` INT NOT NULL DEFAULT 0,
	FOREIGN KEY (Owner) REFERENCES User(Id) ON UPDATE CASCADE ON DELETE CASCADE
) AUTO_INCREMENT = 1, CHARSET = utf8;

CREATE TABLE IF NOT EXISTS `Transfer` (
	`Id` INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	`OutgoingAccount` INT UNSIGNED NOT NULL,
	`IngoingAccount` INT UNSIGNED NOT NULL,
	`Date` DATETIME NOT NULL,
	`Amount` INT UNSIGNED NOT NULL,
	`Reason` VARCHAR(200),
	FOREIGN KEY (OutgoingAccount) REFERENCES Account(Id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (IngoingAccount) REFERENCES Account(Id) ON UPDATE CASCADE ON DELETE CASCADE
) AUTO_INCREMENT = 1, CHARSET = utf8;

CREATE TABLE IF NOT EXISTS `AddressBook` (
	`Id` INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	`Owner` INT UNSIGNED UNIQUE NOT NULL,
	FOREIGN KEY (Owner) REFERENCES User(Id) ON UPDATE CASCADE ON DELETE CASCADE
) AUTO_INCREMENT = 1, CHARSET = utf8;

CREATE TABLE IF NOT EXISTS `Recipient` (
	`Id` INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	`AddressBook` INT UNSIGNED NOT NULL,
	`RecipientId` INT UNSIGNED NOT NULL,
	`AccountId` INT UNSIGNED NOT NULL,
	FOREIGN KEY (AddressBook) REFERENCES AddressBook(Id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (RecipientId) REFERENCES User(Id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (AccountId) REFERENCES Account(Id) ON UPDATE CASCADE ON DELETE CASCADE
) AUTO_INCREMENT = 1, CHARSET = utf8;