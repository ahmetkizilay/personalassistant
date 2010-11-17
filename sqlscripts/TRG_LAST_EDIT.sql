DELIMITER $$

DROP TRIGGER IF EXISTS `personalassistant_dev`.`TRG_LAST_EDIT`$$
CREATE TRIGGER `personalassistant_dev`.`TRG_LAST_EDIT`
	BEFORE UPDATE ON `personalassistant_dev`.`tasks` 
	FOR EACH ROW
BEGIN
  SET NEW.`lastedited` = CONCAT(CURDATE(), ' ', CURTIME());
END$$

DELIMITER ;