USE tiki;

DELIMITER $$

DROP PROCEDURE IF EXISTS `usp_configurable_product_deleteByIdNotIn` $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `usp_configurable_product_deleteByIdNotIn`(
	IN ids_in VARCHAR(255)
)
BEGIN

	DECLARE EXIT HANDLER FOR SQLEXCEPTION
		BEGIN
			ROLLBACK;
		END;
        
	DECLARE EXIT HANDLER FOR SQLWARNING
		BEGIN
			ROLLBACK;
		END;
        
	START TRANSACTION;
        SET @sql = CONCAT('DELETE FROM configurable_product WHERE id NOT IN (', ids_in, ')');
        PREPARE stmt FROM @sql;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
	COMMIT;

END $$

DELIMITER ;