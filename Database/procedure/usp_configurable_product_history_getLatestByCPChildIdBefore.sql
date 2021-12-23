USE tiki;

DELIMITER $$

DROP PROCEDURE IF EXISTS `usp_configurable_product_history_getLatestByCPChildIdBefore` $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `usp_configurable_product_history_getLatestByCPChildIdBefore`(
	IN cp_child_id_in BIGINT,
    IN month_in SMALLINT,
    IN year_in SMALLINT
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
        SELECT *
        FROM configurable_product_history AS cph
		WHERE cph.configurable_product_child_id = cp_child_id_in AND 
				MONTH(date) < month_in AND YEAR(date) <= year_in
		ORDER BY date DESC LIMIT 1;
	COMMIT;

END $$

DELIMITER ;