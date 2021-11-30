USE tiki;

DELIMITER $$

DROP PROCEDURE IF EXISTS `usp_configurable_product_history_getByProductIdAndCPChildId` $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `usp_configurable_product_history_getByProductIdAndCPChildId`(
	IN product_id_in BIGINT,
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
        SELECT cph.*
        FROM configurable_product_history AS cph JOIN configurable_product AS cp ON cph.configurable_product_child_id = cp.child_id
		WHERE cp.product_id = product_id_in AND cp.child_id = cp_child_id_in AND 
				MONTH(date) = month_in AND YEAR(date) = year_in;
	COMMIT;

END $$

DELIMITER ;