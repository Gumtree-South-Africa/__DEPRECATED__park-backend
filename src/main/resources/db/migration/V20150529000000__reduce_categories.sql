
-- Add Fashion Categories
INSERT INTO `category`(`cat_id`,`cat_key`,`cat_picture`,`cat_web_color`,`cat_order`) VALUES
(19, "category.name.fashionAccesories","","e74587",2),
(20, "category.name.juegosPeliculas","","ec521b",9);

-- Merge items from fashionForHim & fashionForHer to fashionAccesories
UPDATE item SET ite_cat_id = 19 WHERE ite_cat_id IN (2,3);

-- Merge items from Audio, TV and Fotografía to electronics
UPDATE item SET ite_cat_id = 5 WHERE ite_cat_id IN (12,17,7);

-- Merge items from Intruments to sports
UPDATE item SET ite_cat_id = 6 WHERE ite_cat_id = 16;

-- Merge items from  Videojuegos, Música & Películas to Juegos y películas
UPDATE item SET ite_cat_id = 20 WHERE ite_cat_id in (18,4);

-- Merge items from books to others
UPDATE item SET ite_cat_id = 10 WHERE ite_cat_id = 14;

-- Delete Old Categories
DELETE FROM category WHERE cat_id in (2,3,12,17,7,16,18,4,14);

--Update Colors & order
UPDATE category SET cat_web_color = "647681"  WHERE cat_id = 11;
UPDATE category SET cat_order = 3, cat_web_color = "006eac"  WHERE cat_id = 5;
UPDATE category SET cat_order = 4, cat_web_color = "d82a22"  WHERE cat_id = 8;
UPDATE category SET cat_order = 5, cat_web_color = "bc9ee7"  WHERE cat_id = 13;
UPDATE category SET cat_order = 6, cat_web_color = "8ebb36"  WHERE cat_id = 6;
UPDATE category SET cat_order = 7, cat_web_color = "c9ac7a"  WHERE cat_id = 15;
UPDATE category SET cat_order = 8, cat_web_color = "23c5cf"  WHERE cat_id = 9;
UPDATE category SET cat_order = 10, cat_web_color = "873c8a"  WHERE cat_id = 1;
UPDATE category SET cat_order = 11, cat_web_color = "ffcc00"  WHERE cat_id = 10;