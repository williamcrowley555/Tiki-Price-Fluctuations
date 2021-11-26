USE tiki;

DELIMITER $$

DROP PROCEDURE IF EXISTS `usp_product_update` $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `usp_product_update`(
	IN all_time_quantity_sold_in BIGINT,
    IN discount_in BIGINT,
    IN discount_rate_in BIGINT,
    IN favourite_count_in BIGINT,
    IN image_url_in TEXT,
    IN list_price_in BIGINT,
    IN name_in TEXT,
    IN original_price_in BIGINT,
    IN price_in BIGINT,
    IN rating_average_in BIGINT,
    IN review_count_in BIGINT,
    IN description_in LONGTEXT,
    IN short_description_in TEXT,
    IN sku_in TEXT,
    IN url_key_in TEXT,
    IN url_path_in TEXT,
    IN category_id_in BIGINT,
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
        UPDATE product 
        SET all_time_quantity_sold = all_time_quantity_sold_in, discount = discount_in, discount_rate = discount_rate_in, 
			favourite_count = favourite_count_in, image_url = image_url_in, list_price = list_price_in, name = name_in, 
			original_price = original_price_in, price = price_in, rating_average = rating_average_in, review_count = review_count_in, 
			description = description_in, short_description = short_description_in, sku = sku_in, url_key = url_key_in, 
			url_path = url_path_in, category_id = category_id_in 
		WHERE id = id_in;
	COMMIT;

END $$

DELIMITER ;