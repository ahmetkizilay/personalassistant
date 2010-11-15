CREATE TABLE  `personalassistant`.`tasks` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `message` varchar(200) NOT NULL,
  `detail` varchar(500) DEFAULT NULL,
  `startdate` datetime DEFAULT NULL,
  `duedate` datetime DEFAULT NULL,
  `priority` int(10) unsigned DEFAULT '0',
  `status` int(10) unsigned DEFAULT '0',
  `lastupdated` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1