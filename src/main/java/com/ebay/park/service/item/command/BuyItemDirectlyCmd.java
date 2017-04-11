package com.ebay.park.service.item.command;

import com.ebay.park.db.dao.ConversationDao;
import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Chat;
import com.ebay.park.db.entity.Conversation;
import com.ebay.park.db.entity.ConversationStatus;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.StatusDescription;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.conversation.command.ChatHelper;
import com.ebay.park.service.item.ItemUtils;
import com.ebay.park.service.item.dto.BuyItemDirectlyResponse;
import com.ebay.park.service.item.dto.UserItemRequest;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.session.dto.UserSessionCache;

import com.ebay.park.util.LanguageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.ebay.park.service.ServiceException.createServiceException;

/**
 * 
 * @author marcos.lambolay & Julieta Salvadó
 */
@Component
public class BuyItemDirectlyCmd implements ServiceCommand<UserItemRequest, BuyItemDirectlyResponse> {

	@Autowired
	private ItemDao itemDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private ConversationDao conversationDao;

	@Autowired
	private SessionService sessionService;

	@Autowired
	private ChatHelper chatHelper;

	@Autowired
	private ItemUtils itemUtils;

	@Override
	public BuyItemDirectlyResponse execute(UserItemRequest request) throws ServiceException {

		Item item = itemDao.findOne(request.getItemId());

		if (item == null) {
			throw createServiceException(ServiceExceptionCode.ITEM_NOT_FOUND);
		}

		if (!item.getStatus().equals(StatusDescription.ACTIVE)) {
			throw createServiceException(ServiceExceptionCode.ITEM_NOT_ACTIVE_ERROR);
		}

		UserSessionCache session = sessionService.getUserSession(request.getToken());
		if (item.getPublishedBy().getUserId().equals(session.getUserId())) {
			throw createServiceException(ServiceExceptionCode.SELLER_CANT_BUY_HIS_OWN_ITEM_ERROR);
		}

		//create new conversation with an accepted milestone
		User buyer = userDao.findOne(session.getUserId());
		Conversation conversation = new Conversation(buyer, item.getPublishedBy(), item);
		conversation.setCurrentPriceProposedByBuyer(item.getPrice());
		conversation.setCurrentPriceProposedBySeller(item.getPrice());
		conversation.setStatus(ConversationStatus.ACCEPTED);
		conversation = conversationDao.saveAndFlush(conversation);

		Chat chat = conversation.buildAcceptedMilestone(session.getUserId());
		chatHelper.persistChat(chat);

		chatHelper.notifyOwnerDirectBuy(item, buyer, conversation, LanguageUtil.getLanguageForUserRequest(session, request.getLanguage()));

		// generate ratings
		chatHelper.generateRatings(conversation.getBuyer(), conversation.getSeller(),
				conversation.getItem());

		itemUtils.setAsFollower(item.getId(), buyer.getId());

		itemDao.save(item);

		return new BuyItemDirectlyResponse(conversation.getId());
	}
}