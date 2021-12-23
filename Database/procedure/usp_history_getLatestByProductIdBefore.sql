USE tiki;

DELIMITER $$

DROP PROCEDURE IF EXISTS `usp_history_getLatestByProductIdBefore` $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `usp_history_getLatestByProductIdBefore`(
	IN product_id_in BIGINT,
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
        SELECT h.*
        FROM history AS h JOIN product AS p ON h.product_id = p.id
		WHERE h.product_id = product_id_in AND MONTH(date) < month_in AND YEAR(date) <= year_in
        ORDER BY date DESC LIMIT 1;
	COMMIT;

END $$

DELIMITER ;