alter table category add column cat_order integer;
update category set cat_order = 2, cat_web_color='23C5CF', cat_picture = 'http://d9de1c2614c54ebad451-c6ae1bfee52f95c9114af051097d89dd.r53.cf5.rackcdn.com/user_files/category/electronics_2145541729146203425' where cat_id = 5;
update category set cat_order = 3, cat_web_color='E799CC', cat_picture = 'http://d9de1c2614c54ebad451-c6ae1bfee52f95c9114af051097d89dd.r53.cf5.rackcdn.com/user_files/category/quinceanera' where cat_id = 1;
update category set cat_order = 5, cat_web_color='00A4EE', cat_picture = 'http://d9de1c2614c54ebad451-c6ae1bfee52f95c9114af051097d89dd.r53.cf5.rackcdn.com/user_files/category/forHim' where cat_id = 2;
update category set cat_order = 6, cat_web_color='E74587', cat_picture = 'http://d9de1c2614c54ebad451-c6ae1bfee52f95c9114af051097d89dd.r53.cf5.rackcdn.com/user_files/category/forHer' where cat_id = 3;
update category set cat_order = 7, cat_key = 'category.name.photography', cat_web_color='6666CF', cat_picture = 'http://d9de1c2614c54ebad451-c6ae1bfee52f95c9114af051097d89dd.r53.cf5.rackcdn.com/user_files/category/photography' where cat_id = 7;
update category set cat_order = 8, cat_web_color='D82A22', cat_picture = 'http://d9de1c2614c54ebad451-c6ae1bfee52f95c9114af051097d89dd.r53.cf5.rackcdn.com/user_files/category/cars_-1107278882840131214' where cat_id = 8;
update category set cat_order = 9, cat_web_color='009E36', cat_picture = 'http://d9de1c2614c54ebad451-c6ae1bfee52f95c9114af051097d89dd.r53.cf5.rackcdn.com/user_files/category/sports_316478165868917864' where cat_id = 6;
update category set cat_order = 15, cat_web_color='7DA7D6', cat_picture = 'http://d9de1c2614c54ebad451-c6ae1bfee52f95c9114af051097d89dd.r53.cf5.rackcdn.com/user_files/category/music_-4269987503449800416' where cat_id = 4;
update category set cat_order = 17, cat_key = 'category.name.home', cat_web_color='647681', cat_picture = 'http://d9de1c2614c54ebad451-c6ae1bfee52f95c9114af051097d89dd.r53.cf5.rackcdn.com/user_files/category/homeAppliances' where cat_id = 9;
update category set cat_order = 18, cat_web_color='99224C', cat_picture = 'http://d9de1c2614c54ebad451-c6ae1bfee52f95c9114af051097d89dd.r53.cf5.rackcdn.com/user_files/category/others_-2216122686638733927' where cat_id = 10;

INSERT INTO category (cat_id, cat_key, cat_picture, cat_web_color, cat_order) VALUES (11, 'category.name.all', 'http://d9de1c2614c54ebad451-c6ae1bfee52f95c9114af051097d89dd.r53.cf5.rackcdn.com/user_files/category/all', '8EBB36', 1);
INSERT INTO category (cat_id, cat_key, cat_picture, cat_web_color, cat_order) VALUES (12, 'category.name.audio', 'http://d9de1c2614c54ebad451-c6ae1bfee52f95c9114af051097d89dd.r53.cf5.rackcdn.com/user_files/category/audio', 'EC521B', 12);
INSERT INTO category (cat_id, cat_key, cat_picture, cat_web_color, cat_order) VALUES (13, 'category.name.babes', 'http://d9de1c2614c54ebad451-c6ae1bfee52f95c9114af051097d89dd.r53.cf5.rackcdn.com/user_files/category/babies_and_kids', 'BC9EE7', 16);
INSERT INTO category (cat_id, cat_key, cat_picture, cat_web_color, cat_order) VALUES (14, 'category.name.books', 'http://d9de1c2614c54ebad451-c6ae1bfee52f95c9114af051097d89dd.r53.cf5.rackcdn.com/user_files/category/books', 'FFCC00', 14);
INSERT INTO category (cat_id, cat_key, cat_picture, cat_web_color, cat_order) VALUES (15, 'category.name.furniture', 'http://d9de1c2614c54ebad451-c6ae1bfee52f95c9114af051097d89dd.r53.cf5.rackcdn.com/user_files/category/furniture', 'C9AC7A', 4);
INSERT INTO category (cat_id, cat_key, cat_picture, cat_web_color, cat_order) VALUES (16, 'category.name.instruments', 'http://d9de1c2614c54ebad451-c6ae1bfee52f95c9114af051097d89dd.r53.cf5.rackcdn.com/user_files/category/instruments', 'FFAA00', 11);
INSERT INTO category (cat_id, cat_key, cat_picture, cat_web_color, cat_order) VALUES (17, 'category.name.tv', 'http://d9de1c2614c54ebad451-c6ae1bfee52f95c9114af051097d89dd.r53.cf5.rackcdn.com/user_files/category/tv01', '873C8A', 13);
INSERT INTO category (cat_id, cat_key, cat_picture, cat_web_color, cat_order) VALUES (18, 'category.name.videogames', 'http://d9de1c2614c54ebad451-c6ae1bfee52f95c9114af051097d89dd.r53.cf5.rackcdn.com/user_files/category/videogames', '006EAC', 10);

update banner set banner_priority='GENERAL_MESSAGES' where banner_id = 3 or banner_id = 4 or banner_id = 5 or banner_id = 6;
update banner set banner_type = 'FACEBOOK' where banner_id = 3 or banner_id = 4;
update banner set banner_type = 'TWITTER' where banner_id = 5 or banner_id = 6; 
update banner set banner_msg='¡Recuerde verificar su correo electrónico! No podrá publicar ítems hasta que lo haya hecho' where banner_id = 2;
update banner set banner_msg='Do you know you can invite your friends with Facebook? Check that feature in your preferences' where banner_id=3;
update banner set banner_msg='¿Sabía que puede invitar a sus amigos en Facebook? Encuentre esa opción en sus preferencias' where banner_id=4;
update banner set banner_msg='Do you know you can invite your friends with Twitter? Check that feature in your preferences' where banner_id=5;
update banner set banner_msg='¿Sabía que puede invitar a sus amigos en Twitter? Encuentre esa opción en sus preferencias' where banner_id=6;
update banner set banner_msg='Do you know you can invite your friends through e-mail? Check that feature in the main menu' where banner_id=7;
update banner set banner_msg='¿Sabía que puede invitar a sus amigos por email? Encuentre esa opción en el menú principal' where banner_id=8;

alter table category add column cat_selectable bit not null default true;
update category set cat_selectable = false where cat_id = 11;