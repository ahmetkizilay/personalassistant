DELIMITER $$

DROP PROCEDURE IF EXISTS `personalassistant_dev`.`SP_INS_TASK`$$
CREATE PROCEDURE `personalassistant_dev`.`SP_INS_TASK` (
IN in_message VARCHAR(200),
IN in_detail VARCHAR(500),
IN in_start_date TIMESTAMP,
IN in_due_date TIMESTAMP,
IN in_priority INT,
OUT out_task_id INT
)
BEGIN
-- DECLARE l_last_updated VARCHAR(19);
-- SELECT CONCAT(CURDATE(), ' ', CURTIME()) INTO l_last_updated;

INSERT INTO `tasks`(`message`, `detail`, `startdate`, `duedate`, `priority`, `status`, `lastupdated`)
VALUES(in_message, in_detail, in_start_date, in_due_date, in_priority, 0, CONCAT(CURDATE(), ' ', CURTIME()));

SELECT MAX(`id`) INTO out_task_id FROM `tasks`;
END$$

DELIMITER ;
