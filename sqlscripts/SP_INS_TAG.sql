DELIMITER $$

DROP PROCEDURE IF EXISTS `personalassistant_dev`.`SP_INS_TAG`$$
CREATE PROCEDURE `personalassistant_dev`.`SP_INS_TAG` (
IN in_task_id INT,
IN in_tag_name VARCHAR(50)
)
BEGIN
DECLARE l_tag_id INT;
DECLARE l_tag_exists INT;
DECLARE l_tag_task_exists INT;

SELECT COUNT(*) INTO l_tag_exists FROM `tags` WHERE `name` = in_tag_name;
IF l_tag_exists = 0 THEN
	INSERT INTO `tags`(`name`) VALUES(in_tag_name);
END IF;

SELECT MAX(`id`) INTO l_tag_id FROM `tags`;

SELECT COUNT(*) INTO l_tag_task_exists FROM `tags_tasks` WHERE `tag_id` = l_tag_id AND `task_id` = in_task_id;
IF l_tag_task_exists = 0 THEN
	INSERT INTO `tags_tasks`(`tag_id`, `task_id`) VALUES(l_tag_id, in_task_id);
END IF;

END$$

DELIMITER ;
