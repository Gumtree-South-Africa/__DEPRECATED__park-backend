package com.ebay.park.service.item.command;

import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.StatusDescription;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.item.ItemUtils;
import com.ebay.park.service.item.dto.ItemSummary;
import com.ebay.park.service.item.dto.SearchItemRequest;
import com.ebay.park.service.item.dto.SearchItemResponse;
import com.ebay.park.util.DataCommonUtil;
import com.ebay.park.util.InternationalizationUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.ebay.park.service.ServiceException.createServiceException;

@Component
public class RecommendedItemsCmdImpl implements SearchItemCmd {

	private static final String NO_RESULTS_MESSAGE = "emptylist.recommended_items";

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private ItemDao itemDao;
	
	@Autowired
	private ItemUtils itemUtils;

	@Autowired
	private InternationalizationUtil i18nUtil;
	
	@Autowired
	private SearchItemCmdHelper searchCmdHelper;
	
	@Value("${list.recommended_items_max}")
	private int LIST_RECOMMENDED_ITEMS_MAX;
	
	@Value("${list.recommended_items_radius_miles}")
	private Double LIST_RECOMMENDED_ITEMS_RADIUS_MILES;
	
	@Override
	public SearchItemResponse execute(SearchItemRequest request) {

		User user = null;
		if (request.getToken() != null) {
			user = userDao.findByToken(request.getToken());
			if (user == null) {
				throw createServiceException(ServiceExceptionCode.USER_UNAUTHORIZED);
			}
		}
		
		searchCmdHelper.setDefaultDistance(request, user);

		List<Item> items;
		if (user != null) {
			items = itemDao.getRecommendedItems(user.getId(),
					StatusDescription.ACTIVE, request.getLatitude(),
					request.getLongitude(), LIST_RECOMMENDED_ITEMS_RADIUS_MILES,
					new PageRequest(0, LIST_RECOMMENDED_ITEMS_MAX));
		} else {
			items = itemDao.getPublicRecommendedItems(StatusDescription.ACTIVE,
					request.getLatitude(), request.getLongitude(),
					LIST_RECOMMENDED_ITEMS_RADIUS_MILES,
					new PageRequest(0, LIST_RECOMMENDED_ITEMS_MAX));
		}

		//language definition
		String language = request.getLanguage();
		if ((language == null) && (user != null)) {
			language = user.getIdiom().getCode();
		}
		
		List<ItemSummary> itemsDTO = itemUtils.convertToItemSummary(items,
				user, language);

		SearchItemResponse searchItemResponse = new SearchItemResponse(
				itemsDTO, itemsDTO.size(),
				DataCommonUtil.getDateTimeAsISO(DateTime.now().toDate()));

		i18nUtil.internationalizeListedResponse(searchItemResponse,
				NO_RESULTS_MESSAGE, request.getLanguage());

		return searchItemResponse;
	}

}