package com.ebay.park.service.item.command;

import static com.ebay.park.service.ServiceException.createServiceException;

import java.util.List;

import com.ebay.park.util.LanguageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.entity.Conversation;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.StatusDescription;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.conversation.command.ChatHelper;
import com.ebay.park.service.item.dto.UserItemRequest;
import com.ebay.park.util.UserUtils;
import org.springframework.util.Assert;

/**
 * 
 * @author marcos.lambolay & Julieta Salvadó
 */
@Component
public class SoldItemCmd implements ServiceCommand<UserItemRequest, ServiceResponse> {

	@Autowired
	private ItemDao itemDao;

	@Autowired
	private ChatHelper chatHelper;
	
	@Autowired
	private UserUtils userUtils;

	/**
	 * It changes the item state into SOLD, notifies followers and adds a milestone
	 * to the chat.
	 * @param request
	 *     the incoming request. It <b>mustn't</b> be null.
     * @throws ServiceException with code ITEM_NOT_FOUND when the item is deleted or cannot be found.
     * @throws ServiceException with code ITEM_DOESNT_BELONG_TO_USER when the item does not belong to the requester.
     * @throws ServiceException with code ITEM_NOT_ACTIVE_ERROR when the item is not in the active state.
	 */
	@Override
    public ServiceResponse execute(UserItemRequest request) throws ServiceException {
		Assert.notNull(request, "Request must be not null");
		Item item = userUtils.getItemUser(request);

		if (!item.getStatus().equals(StatusDescription.ACTIVE)) {
			throw createServiceException(ServiceExceptionCode.ITEM_NOT_ACTIVE_ERROR);
		}

		item.sold();
		itemDao.save(item);

		//notification to followers with/without started conversation
		chatHelper.notifyNotInterestedFollowersItemWasSold(item, item.getPublishedBy());
		chatHelper.notifyInterestedFollowersItemWasSold(
				item,
				item.getPublishedBy(),
				LanguageUtil.getLanguageForUserRequest(item.getPublishedBy(), request.getLanguage()));

		//automatic chat to opened & accepted conversations
		List<Conversation> conversations = item.getAcceptedConversations();
		conversations.addAll(item.getOpenConversations());
		for (Conversation conversation : conversations) {
		    chatHelper.persistChat(conversation.buildSoldMilestone());
		}

		return ServiceResponse.SUCCESS;
	}
}