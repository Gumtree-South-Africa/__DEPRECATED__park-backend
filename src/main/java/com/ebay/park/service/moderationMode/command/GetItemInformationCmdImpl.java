package com.ebay.park.service.moderationMode.command;

import static com.ebay.park.service.ServiceException.createServiceException;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.dao.UserFollowsItemDao;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.ItemGroup;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.category.dto.SmallCategory;
import com.ebay.park.service.item.dto.SmallGroupDTO;
import com.ebay.park.service.item.dto.SmallUser;
import com.ebay.park.service.moderationMode.ModerationCacheHelper;
import com.ebay.park.service.moderationMode.dto.GetItemInformationRequest;
import com.ebay.park.service.moderationMode.dto.GetItemInformationResponse;
import com.ebay.park.util.DataCommonUtil;
import com.ebay.park.util.InternationalizationUtil;

@Component
public class GetItemInformationCmdImpl implements GetItemInformationCmd {
	
	@Autowired
	private UserFollowsItemDao userFollowsItemDao;
	
	@Autowired
	private InternationalizationUtil i18nUtil;
	
	@Autowired
	private ItemDao itemDao;
	
	@Autowired
	private ModerationCacheHelper moderationCacheHelper;

	@Override
	public GetItemInformationResponse execute(GetItemInformationRequest request) throws ServiceException {

		Item item = itemDao.findOne(request.getItemId());

		if (item == null) {
			throw createServiceException(ServiceExceptionCode.ITEM_NOT_FOUND);
		}

		Long publisherId = item.getPublishedBy().getId();
		if (!moderationCacheHelper.isItemAvailableToModerate(publisherId, request.getToken())) {
			throw createServiceException(ServiceExceptionCode.ITEM_NOT_AVAILABLE_TO_MODERATE);
		}

		//unlocked previous locked user
		moderationCacheHelper.unlockUser(request.getToken());

		//lock user
		moderationCacheHelper.lockUser(publisherId, request.getToken());

		return getItemInformationResponse(item, request.getLanguage());
	}

	private GetItemInformationResponse getItemInformationResponse(Item item, String language) {
		GetItemInformationResponse response = new GetItemInformationResponse();
		
		response.setId(item.getId());
		response.setDescription(item.getDescription());
		response.setLocation(item.getLocation());
		response.setName(item.getName());
		response.setPrice(item.getPrice().toString());
		response.setLocationName(item.getLocationName());
		response.setPicture1(item.getPicture1Url());
		response.setPicture2(item.getPicture2Url());
		response.setPicture3(item.getPicture3Url());
		response.setPicture4(item.getPicture4Url());

		if (item.getLastModificationDate() != null) {
			response.setLastModificationDate(DataCommonUtil.getDateTimeAsISO(item.getLastModificationDate()));
		}

		response.setTotalOfFollowers(userFollowsItemDao.totalOfFollowersOfItem(item.getId()));
		response.setUser(new SmallUser(item));
		response.setZipCode(item.getZipCode());

		response.setCategory(SmallCategory.fromCategory(i18nUtil.internationalize(item.getCategory(), language)));
		
		List<SmallGroupDTO> smallGroups = new ArrayList<SmallGroupDTO>();
		for (ItemGroup g : item.getItemGroups()) {
			SmallGroupDTO smallGroupDTO = SmallGroupDTO.fromGroup(g.getGroup());
			smallGroups.add(smallGroupDTO);
		}
		response.setGroups(smallGroups);
		response.setPendingModeration(item.isPendingModeration());

		return response;
	}

}
