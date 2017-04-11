package com.ebay.park.elasticsearch.listeners;

import com.ebay.park.db.entity.*;
import org.hibernate.event.spi.PostCommitInsertEventListener;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.stereotype.Component;

/**
 * @author l.marino on 6/3/15.
 */
@Component
public class ParkPostCommitInsertEventListener extends AbstractPostcommitEventListener implements PostCommitInsertEventListener {

	private static final long serialVersionUID = 1L;
	
	@Override
    public void onPostInsert(PostInsertEvent postInsertEvent) {
        if (postInsertEvent == null) return;
        Object entity = postInsertEvent.getEntity();
        if(entity instanceof Item){
            indexAsItem((Item) entity);
        }else if (entity instanceof BlackList){
            indexAsBlacklist((BlackList) entity);
        }else if (entity instanceof Group){
            indexAsGroup((Group) entity);
        }else if (entity instanceof User){
            indexAsUser((User) entity);
        }else if (entity instanceof UserFollowsGroup){
            indexUserFollowingGroup((UserFollowsGroup) entity);
        }else if (entity instanceof ItemGroup){
            indexItemInGroup((ItemGroup) entity);
        }
    }

    @Override
    public boolean requiresPostCommitHanding(EntityPersister entityPersister) {
        return true;
    }

	@Override
	public void onPostInsertCommitFailed(PostInsertEvent arg0) {
		throw new RuntimeException("we failed on the post commit insert!");
	}

}
