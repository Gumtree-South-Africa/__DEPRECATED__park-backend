DELETE FROM device WHERE dv_id IN (
 SELECT * FROM 
              (SELECT d.dv_id FROM device d HAVING d.dv_id < 
					(SELECT max(x.dv_id) FROM device x
					WHERE x.dv_use_id = d.dv_use_id)
				)
AS t);