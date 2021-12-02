USE tiki;

DELIMITER $$

DROP PROCEDURE IF EXISTS `usp_product_filter` $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `usp_product_filter`(
	IN name_in TEXT,
    IN category_id_in BIGINT,
    IN brand_id_in BIGINT,
    IN rating_average_in FLOAT,
    IN min_price_in BIGINT,
    IN max_price_in BIGINT
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
		FROM product
		WHERE name LIKE CONCAT('%', if(name_in IS NULL, '', name_in), '%') AND
				(category_id_in IS NULL OR category_id = category_id_in) AND
				(brand_id_in IS NULL OR brand_id = brand_id_in) AND
				(rating_average_in IS NULL OR rating_average >= rating_average_in) AND
				(min_price_in IS NULL OR price >= min_price_in) AND
				(max_price_in IS NULL OR price <= max_price_in);
	COMMIT;

END $$

DELIMITER ;