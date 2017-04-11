package com.ebay.park.service.notification.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ebay.park.notification.NotificationAction;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.notification.dto.Feed;
import com.ebay.park.service.notification.dto.GetFeedsRequest;
import com.ebay.park.service.notification.dto.GetFeedsResponse;

@Component
public class GetFeedsV4Cmd implements ServiceCommand<GetFeedsRequest, GetFeedsResponse> {

    @Autowired
    private GetFeedsCmd commonBehaviourCmd;

	@Override
	public GetFeedsResponse execute(GetFeedsRequest request) throws ServiceException {
		GetFeedsResponse response = commonBehaviourCmd.execute(request);

        for (Feed feed : response.getFeeds()) {
            //FB_FRIEND_USING_THE_APP is not a valid action in version 4
            if (NotificationAction.FB_FRIEND_USING_THE_APP.toString().equals(feed.getAction())) {
                feed.setAction(NotificationAction.FOLLOW_USER.toString());
            }
        }
        return response;

	}


}
