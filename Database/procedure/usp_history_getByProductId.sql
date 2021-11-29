USE tiki;

DELIMITER $$

DROP PROCEDURE IF EXISTS `usp_history_getByProductId` $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `usp_history_getByProductId`(
	IN product_id_in BIGINT
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
		WHERE h.product_id = product_id_in;
	COMMIT;

END $$

DELIMITER ;