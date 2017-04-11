package com.ebay.park.service.item.command;

import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.dao.UserFollowsItemDao;
import com.ebay.park.db.dao.UserReportItemDao;
import com.ebay.park.db.entity.Conversation;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.ItemGroup;
import com.ebay.park.db.entity.StatusDescription;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserReportItem;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.category.dto.SmallCategory;
import com.ebay.park.service.item.dto.GetItemRequest;
import com.ebay.park.service.item.dto.GetItemResponse;
import com.ebay.park.service.item.dto.SmallGroupDTO;
import com.ebay.park.service.item.dto.SmallUser;
import com.ebay.park.service.picture.ResetEPSPictureExpireDateService;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.session.dto.UserSessionCache;
import com.ebay.park.util.DataCommonUtil;
import com.ebay.park.util.EPSUtils;
import com.ebay.park.util.InternationalizationUtil;
import com.ebay.park.util.TextUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.ebay.park.service.ServiceException.createServiceException;


@Component
public class GetItemCmd implements ServiceCommand<GetItemRequest, GetItemResponse> {

	@Autowired
	protected ItemDao itemDao;

	@Autowired
	protected UserDao userDao;

	@Autowired
	protected UserFollowsItemDao userFollowsItemDao;

	@Autowired
	protected InternationalizationUtil i18nUtil;

	@Autowired
	protected UserReportItemDao userReportItemDao;

	@Autowired
	private SessionService sessionService;
	
	@Autowired
	private TextUtils textUtils;
	
	@Autowired
	private EPSUtils epsUtils;
	
	@Autowired 
	private ResetEPSPictureExpireDateService resetEPSExpirateDate;

	private static Logger logger = LoggerFactory.getLogger(GetItemCmd.class);
	
	/**
	 * EPS picture resolution for this cmd.
	 */
	@Value("${picture.full_item_size}")
	private String resolution;

	@Override
	public GetItemResponse execute(GetItemRequest request) throws ServiceException {
		GetItemResponse response = new GetItemResponse();

		Item item = itemDao.findOne(Long.parseLong(request.getId()));

		if (item == null) {
			throw createServiceException(ServiceExceptionCode.ITEM_NOT_FOUND);
		}

		User user = null;
		UserSessionCache session = null;
		if (!StringUtils.isBlank(request.getToken())) {
			user = userDao.findByToken(request.getToken());
			if (user == null) {
				throw createServiceException(ServiceExceptionCode.USER_UNAUTHORIZED);
			}
			session = sessionService.getUserSession(request.getToken());
		}
		
		String lang = request.getLanguage();

		if (lang == null && user != null) {
			lang = user.getIdiom().getCode();
		}

		//An item is not available to a user when:
		//	1- The item is in status "Deleted"
		//	2- The item is in status "Pending" and the user is not the owner of that item
		//	3- The item is in status "Expired" and the user is not the owner of that item
		//  4- The item does not have an image (creation error), should not happen often
		
		// They can see everything
		boolean isAdminOrModerator = session != null && CollectionUtils.isNotEmpty(session.getRoles());
		if (!isAdminOrModerator) {
			
			if (item.isDeleted() || item.is(StatusDescription.IMAGE_PENDING)) {
				throw createServiceException(ServiceExceptionCode.UNAUTHORIZED_TO_ACCESS_ITEM);
			
			} else if (item.is(StatusDescription.PENDING) || item.is(StatusDescription.EXPIRED)) {
				boolean isOwner = false;
				if (user != null){
					isOwner = item.getPublishedBy().getId().equals(user.getId());
				}
				if (!isOwner) {
					throw createServiceException(ServiceExceptionCode.UNAUTHORIZED_TO_ACCESS_ITEM);
				}
			}
		}
		

		response.setId(item.getId());
		response.setDescription(item.getDescription());
		response.setLocation(item.getLocation());
		response.setName(item.getName());
		response.setPrice(item.getPrice().toString());
		response.setLocationName(item.getLocationName());

		
		List <String> pictures = getItemPictures(item);
		
		response.setPictures(pictures);
        logger.info("Try to Reset Item's Picture Expire Date into EPS: {}ItemId:{}", pictures, item.getId());
		resetEPSExpirateDate.resetEPSExpireDate(pictures);
		
		response.setStatus(item.getStatus().toString());
		response.setLocalizedStatus(i18nUtil.internationalizeMessage("item." + item.getStatus().toString().toLowerCase(), request.getLanguage()));
		response.setURL(textUtils.createItemSEOURL(
				i18nUtil.internationalizeCategoryName(item.getCategory().getKey(), lang), 
				item.getName(),
				item.getId()));

		if (item.getPublished() != null) {
			response.setPublished(DataCommonUtil.getDateTimeAsISO(item.getPublished()));
		}

		response.setTotalOfFollowers(userFollowsItemDao.totalOfFollowersOfItem(item.getId()));
		response.setUser(new SmallUser(item));
		response.setLatitude(item.getLatitude());
		response.setLongitude(item.getLongitude());
		response.setZipCode(item.getZipCode());

		if (user != null) {
			response =  fillUserData(request, response, item, user, lang);
		}else{
			response.setCategory(SmallCategory.fromCategory(i18nUtil.internationalize(item.getCategory(), lang)));
		}

		List<SmallGroupDTO> smallGroups = new ArrayList<SmallGroupDTO>();
		for (ItemGroup g : item.getItemGroups()) {
			SmallGroupDTO smallGroupDTO = SmallGroupDTO.fromGroup(g.getGroup());
			smallGroupDTO.setSubscribed((user!=null) ? user.isSubscribedToGroup(g.getGroup()): false);
			smallGroups.add(smallGroupDTO);
		}
		response.setGroups(smallGroups);

		return response;

	}

	protected GetItemResponse fillUserData(GetItemRequest request, GetItemResponse response,
			Item item, User user, String lang) {
		response.setCategory(SmallCategory.fromCategory(i18nUtil.internationalize(item.getCategory(), lang)));

		UserReportItem report = userReportItemDao.findUserReportForItem(user.getId(), item.getId());
		response.setReported(report != null);

		response.setFollowedByUser(item.isFollowedByUser(user));
		
		boolean conversations = false; 
		for (Conversation c :item.getConversations()){
			if (user.equals(c.getBuyer()) || user.equals(c.getSeller())){
				conversations = true;
				break;
			}
		}
		response.setHasConversations(conversations);

		return response;
	}

	private List<String> getItemPictures(Item item) {
		List<String> pictures = new ArrayList<>();
		if (StringUtils.isNotEmpty(item.getPicture1Url())) {
			pictures.add(epsUtils.getPictureResolution(item.getPicture1Url(), resolution));
		}
		if (StringUtils.isNotEmpty(item.getPicture2Url())) {
			pictures.add(epsUtils.getPictureResolution(item.getPicture2Url(), resolution));
		}
		if (StringUtils.isNotEmpty(item.getPicture3Url())) {
			pictures.add(epsUtils.getPictureResolution(item.getPicture3Url(), resolution));
		}
		if (StringUtils.isNotEmpty(item.getPicture4Url())) {
			pictures.add(epsUtils.getPictureResolution(item.getPicture4Url(), resolution));
		}
		return pictures;
	}

}