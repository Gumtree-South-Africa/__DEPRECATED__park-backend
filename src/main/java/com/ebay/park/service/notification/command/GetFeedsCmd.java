package com.ebay.park.service.notification.command;

import com.ebay.park.db.dao.FeedDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.item.dto.SmallItem;
import com.ebay.park.service.item.dto.SmallUser;
import com.ebay.park.service.notification.dto.Feed;
import com.ebay.park.service.notification.dto.GetFeedsRequest;
import com.ebay.park.service.notification.dto.GetFeedsResponse;
import com.ebay.park.util.InternationalizationUtil;
import com.ebay.park.util.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.ebay.park.service.ServiceException.createServiceException;

@Component
public class GetFeedsCmd implements ServiceCommand<GetFeedsRequest, GetFeedsResponse> {

	private static final String NO_RESULTS_MESSAGE = "emptylist.activity_feeds";

	@Autowired
	private UserDao userDao;

	@Autowired
	private FeedDao feedDao;

	@Autowired
	private InternationalizationUtil i18nUtil;

	@Autowired
	protected MessageUtil messageUtil;

	@Override
	public GetFeedsResponse execute(GetFeedsRequest request) throws ServiceException {

		User user = userDao.findByUsername(request.getUsername());
		if (user == null) {
			throw createServiceException(ServiceExceptionCode.USER_NOT_FOUND);
		}
		String lang = request.getLanguage();
		if (lang == null) {
			lang = user.getIdiom().getCode();
		}

		List<com.ebay.park.db.entity.Feed> feeds = feedDao
				.findOrderedFeeds(user.getId());

		List<Feed> resultedFeeds = new ArrayList<Feed>();
		for (com.ebay.park.db.entity.Feed feed : feeds) {

			Feed resultedFeed = new Feed();
			resultedFeed.setCreationDate(feed.getCreationDate());


			String message = messageUtil.formatMessage(feed.getNotificationConfig().getTemplateName(),
					feed.getFeedProperties(), lang);

			resultedFeed.setMessage(message);

			Item item = feed.getItem();
			if (feed.getItem() != null) {
				if (!item.isDeleted()) {
					resultedFeed.setItem(new SmallItem(item));
				} else {
					//when the item is deleted, it just returns the item name 
					resultedFeed.setItem(new SmallItem(item.getName(), item.getPicture1Url()));
				}
			}

			if (feed.getUser() != null) {
				resultedFeed.setUser(new SmallUser(feed.getUser()));
			}

			resultedFeed.setAction(feed.getNotificationConfig().getNotificationAction().toString());
		
			resultedFeed.setFeedId(feed.getId());
			
			resultedFeed.setConversationId(feed.getFeedProperties().get(
					"conversationId"));

			resultedFeed.setGroupName(feed.getFeedProperties().get(
					"groupName"));
			
			resultedFeed.setFollowedByUser(Boolean.valueOf(feed.getFeedProperties()
					.get("followedByUser")));
			
			resultedFeed.setRead(feed.isRead());

			resultedFeeds.add(resultedFeed);
		}

		feedDao.save(feeds);

		GetFeedsResponse response = new GetFeedsResponse(resultedFeeds);

		i18nUtil.internationalizeListedResponse(response, NO_RESULTS_MESSAGE,
				lang);

		return response;
	}

}
