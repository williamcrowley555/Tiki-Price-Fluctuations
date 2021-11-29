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
        SELECT cph.*
        FROM configurable_product_history AS cph JOIN configurable_product AS cp ON cph.configurable_product_child_id = cp.child_id
		WHERE cp.product_id = product_id_in;
	COMMIT;

END $$

DELIMITER ;