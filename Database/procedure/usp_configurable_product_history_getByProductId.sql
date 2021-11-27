USE tiki;

DELIMITER $$

DROP PROCEDURE IF EXISTS `usp_configurable_product_history_getByProductId` $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `usp_configurable_product_history_getByProductId`(
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
        SELECT cfh.*
        FROM configurable_product_history AS cfh JOIN configurable_product AS cf ON cfh.configurable_product_id = cf.id
		WHERE cf.product_id = product_id_in;
	COMMIT;

END $$

DELIMITER ;