package com.ebay.park.service.conversation.command;

import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;

import com.ebay.park.util.TextUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.ebay.park.db.dao.ChatDao;
import com.ebay.park.db.dao.RatingDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Chat;
import com.ebay.park.db.entity.Chat.ChatActionType;
import com.ebay.park.db.entity.Conversation;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.Rating;
import com.ebay.park.db.entity.User;
import com.ebay.park.event.conversation.ChatSentEvent;
import com.ebay.park.event.conversation.ConversationAcceptedEvent;
import com.ebay.park.event.user.InterestedUserItemToFollowersEvent;
import com.ebay.park.event.user.NotInterestedUserItemToFollowersEvent;
import com.ebay.park.event.user.UsersEvent;
import com.ebay.park.notification.NotificationAction;
import com.ebay.park.notification.aop.Notifiable;
import com.ebay.park.service.conversation.dto.SendChatRequest;
import com.ebay.park.service.conversation.dto.SendOfferRequest;
import com.ebay.park.service.conversation.dto.SmallChat;
import com.ebay.park.util.DataCommonUtil;
import com.ebay.park.util.InternationalizationUtil;

/**
 * Utility class for chat interactions
 * @author jpizarro & marcos.lambolay
 */
@Component
public class ChatHelper {

	public static final String NEGOCIATION_ACCEPTED_KEY = "negociation.accepted";
	public static final String NEGOCIATION_ACCEPTED_HINT_KEY = "negociation.accepted.hint";
	public static final String NEGOCIATION_CANCELLED_KEY = "negociation.cancelled";
	public static final String NEGOCIATION_CANCELLED_NO_REASON_KEY = "negociation.cancelled.no.reason";
	public static final String NEGOCIATION_CANCELLED_AUTOMATICALLY_KEY = "negociation.cancelled.automatically";
	public static final String OFFER_MESSAGE = "negociation.offer.message";
	public static final String OFFER_ACCEPTED_MESSAGE = "negociation.offer.accepted";
	public static final String MARKED_AS_SOLD_MESSAGE = "negociation.marked.as.sold";

	@Autowired
	private UserDao userDao; 

	@Autowired
	private ChatDao chatDao;

	@Autowired
	private RatingDao ratingDao;

	@Autowired
	protected InternationalizationUtil i18nUtil;

	@Autowired
	private ApplicationContext applicationContext;

	private ChatHelper proxyOfMe;

	@Autowired
	private TextUtils textUtils;

	@PostConstruct
	public void initialize()  {
		proxyOfMe = applicationContext.getBean(ChatHelper.class);
	}

	/**
	 * It creates a new instance of Chat. If the request represents an offer, the chat will be a
	 * ChatActionType.OFFER; if not, it will be a ChatActionType.CHAT.
	 * @param request
	 *     incoming SendChatRequest. It <b>mustn't</b> be null and can represent an offer or a plain chat.
	 * @param conversation
	 *     conversation that is involved.
	 * @param item
	 *     item that is involved
	 * @param role
	 *     both roles in the conversation
	 * @param lang
	 *     language for the automatic messages
	 * @return
	 *     resulting chat
	 */
	public Chat buildChat(SendChatRequest request, Conversation conversation, Item item, DoubleRole role, String lang) {
		Chat chat;
		if(request instanceof SendOfferRequest) {
			chat = buildOfferChat((SendOfferRequest)request, lang, role);
		} else {
			chat = buildMessageChat(request);
		}
		chat.setConversation(conversation);
		chat.setItem(item);
		chat.setPostTime(DateTime.now().toDate());
		chat.setReceiver(userDao.getOne(role.getReceiver().getId()));
		chat.setSender(userDao.getOne(role.getSender().getId()));
		chat.setBrandPublish(request.getBrandPublish());
		chat.setVersionPublish(request.getVersionPublish());
		return chat;
	}
	
	private Chat buildMessageChat(SendChatRequest request) {
		Chat chat = new Chat();
		chat.setComment(request.getComment());
		chat.setAction(ChatActionType.CHAT);
		return chat;
	}

	private Chat buildOfferChat(SendOfferRequest request, String userLang, DoubleRole role) {
		Chat chat = new Chat();
		chat.setOfferedPrice(Double.parseDouble(request.getOfferedPrice()));
		chat.setAction(ChatActionType.OFFER);
		role.getMe().setConversationCurrentPrice(Double.parseDouble(request.getOfferedPrice()));
		return chat;
	}

	@Notifiable(action = NotificationAction.CHAT_SENT)
	public ChatSentEvent sendChat(Chat chat, Conversation conversation, Item item, DoubleRole role, String lang) {
		chatDao.saveAndFlush(chat);
		return new ChatSentEvent(
				role.getReceiver().getUsername(),
				conversation.getId(), item.getId(),
				item.getName(),
				role.getReceiver().getId(),
				role.getSender().getId(),
				role.getSender().getUsername(),
				textUtils.createItemSEOURL(item, lang)
				);
	}

	public Chat persistChat(Chat chat) {
		return chatDao.save(chat);
	}

    /**
     * It send notification to the item followers that did not start a conversation on this item.
     * @param item
     *      the item involved
     * @param publishedBy
     *      the user that owns the item
     * @return
     *      resulting event
     */
	@Notifiable(action = NotificationAction.SOLD_AN_ITEM)
	public NotInterestedUserItemToFollowersEvent notifyNotInterestedFollowersItemWasSold(Item item, User publishedBy) {
		return new NotInterestedUserItemToFollowersEvent(item, publishedBy);
	}

	@Notifiable(action = NotificationAction.SOLD_AN_ITEM_FOR_INTERESTED_FOLLOWERS)
	public InterestedUserItemToFollowersEvent notifyInterestedFollowersItemWasSold(Item item,
            User publishedBy, String lang) {
        return new InterestedUserItemToFollowersEvent(item, publishedBy, textUtils.createItemSEOURL(item, lang));
    }

	@Notifiable(action = NotificationAction.CONVERSATION_ACCEPTED)
	public ConversationAcceptedEvent notifyOwnerDirectBuy(Item item, User buyer, Conversation conversation, String language) {
		return new ConversationAcceptedEvent(
				item.getPublishedBy().getUsername(),
				buyer.getId(),
				buyer.getUsername(),
				item.getPublishedBy().getId(),
				item.getId(),
				item.getName(),
				conversation.getId(),
				textUtils.createItemSEOURL(item, language));
	}

	public SmallChat createaSmallChat(Chat chat, String userLang) {
		SmallChat smallChat = new SmallChat(chat);

		if (chat.getAction().equals(ChatActionType.ACCEPTED)) {
			String hint = i18nUtil.internationalize(NEGOCIATION_ACCEPTED_HINT_KEY,
					userLang);
			smallChat.setHint(hint);
			String comment = i18nUtil.internationalize(NEGOCIATION_ACCEPTED_KEY,
					userLang);
			smallChat.setComment(comment);
		}

		if (chat.getAction().equals(ChatActionType.CANCELLED)) {
			if (chat.isAutomaticGeneratedAction()){
				smallChat.setHint(i18nUtil.internationalize(NEGOCIATION_CANCELLED_AUTOMATICALLY_KEY, userLang));
				
			} else if (StringUtils.isNotBlank(chat.getComment())) {
				smallChat.setHint(chat.getComment());
			
			} else {
				String noReason = i18nUtil.internationalize(NEGOCIATION_CANCELLED_NO_REASON_KEY, userLang);
				smallChat.setHint(noReason);
			}
			String comment = i18nUtil.internationalize(NEGOCIATION_CANCELLED_KEY, userLang);
			smallChat.setComment(comment);
		}
		
		if (chat.getAction().equals(ChatActionType.OFFER)) {
			smallChat.setComment(i18nUtil.internationalize(OFFER_MESSAGE, userLang));
		}
		
		if (chat.getAction().equals(ChatActionType.MARKED_AS_SOLD)) {
			smallChat.setComment(String.format(i18nUtil.internationalize(MARKED_AS_SOLD_MESSAGE, userLang),
			        chat.getItem().getName()));
		}

		return smallChat;
	}

	public SmallChat createaSmallChatOfferAccepted(Chat chat,String userLang) {
		SmallChat smallChat = new SmallChat();
		smallChat.setAction(ChatActionType.OFFER.toString());
		smallChat.setPostTime(DataCommonUtil.getDateTimeAsUnixFormat(chat.getPostTime()));
		smallChat.setOfferedPrice(chat.getOfferedPrice());
		smallChat.setReceiverUsername(chat.getReceiver().getUsername());
		smallChat.setSenderUsername(chat.getSender().getUsername());
		String comment = i18nUtil.internationalize(OFFER_ACCEPTED_MESSAGE, userLang);
		smallChat.setComment(comment);
		return smallChat;
	}

	@Notifiable(action = NotificationAction.PENDING_RATE)
	public void generateRatings(User buyer, User seller, Item item) {
		Rating sellerIsRater = new Rating(buyer, seller, item);
		Rating buyerIsRater = new Rating(seller, buyer, item);
		ratingDao.save(sellerIsRater);
		ratingDao.save(buyerIsRater);

		proxyOfMe.notifyPendingRating(sellerIsRater);
		proxyOfMe.notifyPendingRating(buyerIsRater);

	}

	@Notifiable(action = NotificationAction.PENDING_RATE)
	public UsersEvent notifyPendingRating(Rating rating) {
		return new UsersEvent(rating.getUser(), rating.getRater());
	}

	/**
	 * Sort chats by id in descendent order
	 * @param chats
	 *     the list of chats
	 * @return
	 *     the sorted list of chats
	 */
    public static List<Chat> sort(List<Chat> chats) {
        return chats.stream()
            .sorted((c1, c2) -> Long.compare(c2.getChatId(), c1.getChatId()))
            .collect(Collectors.toList());
    }
}
