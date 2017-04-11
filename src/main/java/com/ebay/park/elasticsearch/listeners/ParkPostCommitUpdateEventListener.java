package com.ebay.park.elasticsearch.listeners;

import com.ebay.park.db.entity.*;
import org.hibernate.event.spi.PostCommitUpdateEventListener;
import org.hibernate.event.spi.PostUpdateEvent;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.stereotype.Component;

/**
 * @author l.marino on 6/3/15.
 */
@Component
public class ParkPostCommitUpdateEventListener extends AbstractPostcommitEventListener implements PostCommitUpdateEventListener {

	private static final long serialVersionUID = 1L;

	@Override
    public void onPostUpdate(PostUpdateEvent postUpdateEvent) {
        if (postUpdateEvent == null) return;
        Object entity = postUpdateEvent.getEntity();
        if(entity instanceof Item){
        	Item item = (Item) entity;
        	if (item.isDeleted()){
        		removeItemFromIndex(item);
        	} else {
        		indexAsItem(item);
        	}
        }else if (entity instanceof BlackList){
            indexAsBlacklist((BlackList) entity);
        }else if (entity instanceof Group){
            indexAsGroup((Group) entity);
        }else if (entity instanceof User){
			indexAsUserAndGroups((User) entity);
        }else if (entity instanceof ItemGroup){
			indexAsItem(((ItemGroup) entity).getItem());
		}
    }

	@Override
    public boolean requiresPostCommitHanding(EntityPersister entityPersister) {
        return true;
    }

	@Override
	public void onPostUpdateCommitFailed(PostUpdateEvent arg0) {
		throw new RuntimeException("we failed on the post commit update!");
	}
}
