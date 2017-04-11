ALTER TABLE item_group ADD COLUMN ig_date DATETIME NOT NULL DEFAULT NOW();
UPDATE user_group 
SET 
   ug_last_access = NOW();