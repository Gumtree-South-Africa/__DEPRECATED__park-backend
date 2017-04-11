INSERT INTO user_group (ug_grp_id, ug_use_id)
Select A.grp_id, A.grp_creator_id
From park.group As A
Where Not Exists    (
                    Select 1
                    From user_group As G
                    Where G.ug_use_id = A.grp_creator_id
                        And G.ug_grp_id = A.grp_id
                    );