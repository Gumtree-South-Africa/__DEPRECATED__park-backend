package com.ebay.park.service.notification;

import com.ebay.park.service.notification.command.MarkAsReadCmd;
import com.ebay.park.service.notification.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ebay.park.service.ServiceCommand;

@Service
public class FeedServiceImpl implements FeedService {

	@Autowired
	@Qualifier("getFeedsCmd")
	private ServiceCommand<GetFeedsRequest, GetFeedsResponse> getFeedsV5Cmd;

	@Autowired
    @Qualifier("getFeedsV4Cmd")
    private ServiceCommand<GetFeedsRequest, GetFeedsResponse> getFeedsV4Cmd;
	
	@Autowired
    @Qualifier("getFeedsV3Cmd")
    private ServiceCommand<GetFeedsRequest, GetFeedsResponse> getFeedsV3Cmd;
	
	@Autowired
	private FeedServiceHelper feedServiceHelper;

	@Autowired
	private MarkAsReadCmd markFeedAsReadCmd;

	@Override
	public GetFeedsResponse getFeedsV3(GetFeedsRequest request) {
		Assert.notNull(request, "The request cannot be null");
		return getFeedsV3Cmd.execute(request);
	}

	@Override
    public GetFeedsResponse getFeedsV4(GetFeedsRequest request) {
		Assert.notNull(request, "The request cannot be null");
        return getFeedsV4Cmd.execute(request);
    }
	
	@Override
    public GetFeedsResponse getFeedsV5(GetFeedsRequest request) {
		Assert.notNull(request, "The request cannot be null");
        return getFeedsV5Cmd.execute(request);
    }

	@Override
	public GetUnreadFeedsCounterResponse countUnreadFeeds(GetUnreadFeedsCounterRequest request) {
		Assert.notNull(request, "The request cannot be null");
		GetUnreadFeedsCounterResponse response = new GetUnreadFeedsCounterResponse();
		response.setUnreadFeedsCounter(feedServiceHelper
				.countUnreadFeeds(request.getToken()));
		return response;
	}

	@Override
	public void readFeed(MarkAsReadRequest request) {
		Assert.notNull(request, "The request cannot be null");
		markFeedAsReadCmd.execute(request);
	}

}
