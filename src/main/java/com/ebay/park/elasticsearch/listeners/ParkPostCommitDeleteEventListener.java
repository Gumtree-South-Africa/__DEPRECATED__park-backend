package com.ebay.park.elasticsearch.listeners;

import com.ebay.park.db.entity.*;
import org.hibernate.event.spi.PostCommitDeleteEventListener;
import org.hibernate.event.spi.PostDeleteEvent;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.stereotype.Component;

/**
 * @author l.marino on 6/3/15.
 */
@Component
public class ParkPostCommitDeleteEventListener extends AbstractPostcommitEventListener  implements PostCommitDeleteEventListener{

	private static final long serialVersionUID = 1L;

	@Override
    public void onPostDelete(PostDeleteEvent postDeleteEvent) {
        if (postDeleteEvent == null) return;
        Object entity = postDeleteEvent.getEntity();
        if(entity instanceof Item){
            removeItemFromIndex((Item) entity);
        }else if (entity instanceof BlackList){
            removeBlacklistFromIndex((BlackList) entity);
        }else if (entity instanceof Group){
            removeGroupFromIndex((Group) entity);
        }else if (entity instanceof User){
            removeUserFromIndex((User) entity);
        }else if (entity instanceof UserFollowsGroup){
            UserFollowsGroup userFollowGroup = (UserFollowsGroup) entity;
            indexAsUser(userFollowGroup.getUser());
            indexAsGroup(userFollowGroup.getGroup());
        }else if (entity instanceof ItemGroup){
            //if the item is already deleted, do not reindex it again!
            Item item = ((ItemGroup) entity).getItem();
			if (item != null && !item.isDeleted()) {
				indexAsItem(item);
			}
        } else if (entity instanceof UserFollowsItem) {
        	UserFollowsItem userFollowsItem = (UserFollowsItem) entity;
        	indexAsUser(userFollowsItem.getUser());
        	//if the item is already deleted, do not reindex it again!
        	Item item = userFollowsItem.getItem();
        	if (item != null && !item.isDeleted()) {
				indexAsItem(item);
			}
        }
    }

    @Override
    public boolean requiresPostCommitHanding(EntityPersister entityPersister) {
        return true;
    }

	@Override
	public void onPostDeleteCommitFailed(PostDeleteEvent arg0) {
		throw new RuntimeException("we failed on the post commit delete!");
	}
}
