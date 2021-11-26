USE tiki;

DELIMITER $$

DROP PROCEDURE IF EXISTS `usp_configurable_product_update` $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `usp_configurable_product_update`(
	IN child_id_in BIGINT,
	IN image_url_in TEXT,
	IN inventory_status_in TEXT,
	IN name_in TEXT,
	IN option1_in TEXT,
	IN price_in BIGINT,
	IN sku_in TEXT,
	IN thumbnail_url_in TEXT,
	IN product_id_in BIGINT,
	IN id_in BIGINT
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
        UPDATE configurable_product 
        SET child_id = child_id_in, image_url = image_url_in, inventory_status = inventory_status_in, name = name_in, 
			option1 = option1_in, price = price_in, sku = sku_in, thumbnail_url = thumbnail_url_in, product_id = product_id_in 
        WHERE id = id_in;
	COMMIT;

END $$

DELIMITER ;