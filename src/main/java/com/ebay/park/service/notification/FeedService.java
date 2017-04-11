package com.ebay.park.service.notification;

import com.ebay.park.service.notification.dto.*;

public interface FeedService {

	/**
	 * Version 3.
	 * Returns a list of feeds for the given user. The feeds will be return in descendant order using the creation date.
	 * @param request
     *      the incoming request
	 * @return return a GetFeedsResponse with the list of feeds.
	 */
	public GetFeedsResponse getFeedsV3(GetFeedsRequest request);

	/**
     * Version 4.
     * Returns a list of feeds for the given user. The feeds will be return in descendant order using the creation date.
     * @param request
     *      the incoming request
     * @return return a GetFeedsResponse with the list of feeds.
     */
    public GetFeedsResponse getFeedsV4(GetFeedsRequest request);
    
    /**
     * Version 5.
     * Returns a list of feeds for the given user. The feeds will be return in descendant order using the creation date.
     * @param request
     *      the incoming request
     * @return return a GetFeedsResponse with the list of feeds.
     */
    public GetFeedsResponse getFeedsV5(GetFeedsRequest request);

	/**
	 * Returns the number of unread feeds for the given user.
	 * @param request the request with the token
	 * @return return a {@link GetUnreadFeedsCounterResponse} with 
	 * the number of unread feeds.
	 */
	public GetUnreadFeedsCounterResponse countUnreadFeeds(GetUnreadFeedsCounterRequest request);

	/**
	 * It marks a feed as read.
	 * @param request the incoming request
	 */
    public void readFeed(MarkAsReadRequest request);
}
