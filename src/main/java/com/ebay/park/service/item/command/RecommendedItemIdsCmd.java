package com.ebay.park.service.item.command;

import static com.ebay.park.service.ServiceException.createServiceException;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.StatusDescription;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.item.dto.SearchItemIdsResponse;
import com.ebay.park.service.item.dto.SearchItemRequest;
import com.ebay.park.util.DataCommonUtil;
import com.ebay.park.util.InternationalizationUtil;

@Component
public class RecommendedItemIdsCmd implements ServiceCommand<SearchItemRequest, SearchItemIdsResponse>{
	private static final String NO_RESULTS_MESSAGE = "emptylist.recommended_items";
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private ItemDao itemDao;
	
	@Autowired
	private InternationalizationUtil i18nUtil;
	
	@Value("${list.recommended_items_max}")
	private int LIST_RECOMMENDED_ITEMS_MAX;
	
	@Value("${list.recommended_items_radius_miles}")
	private Double LIST_RECOMMENDED_ITEMS_RADIUS_MILES;
	
	@Autowired
	private SearchItemCmdHelper searchCmdHelper;

	@Override
	public SearchItemIdsResponse execute(SearchItemRequest request) {
		
		User user = null;
		if (request.getToken() != null) {
			user = userDao.findByToken(request.getToken());
			if (user == null) {
				throw createServiceException(ServiceExceptionCode.USER_UNAUTHORIZED);
			}
		}

		searchCmdHelper.setDefaultDistance(request, user);

		List<Long> items;
		if (user != null) {
			items = itemDao.getRecommendedItemIds(user.getId(), StatusDescription.ACTIVE, request.getLatitude(),
					request.getLongitude(), LIST_RECOMMENDED_ITEMS_RADIUS_MILES,
					new PageRequest(0, LIST_RECOMMENDED_ITEMS_MAX));
		} else {
			items = itemDao.getPublicRecommendedItemIds(StatusDescription.ACTIVE, request.getLatitude(),
					request.getLongitude(), LIST_RECOMMENDED_ITEMS_RADIUS_MILES,
					new PageRequest(0, LIST_RECOMMENDED_ITEMS_MAX));
		}

		SearchItemIdsResponse searchItemIdsResponse = new SearchItemIdsResponse(items, items.size(),
				DataCommonUtil.getDateTimeAsISO(DateTime.now().toDate()));

		i18nUtil.internationalizeListedResponse(searchItemIdsResponse, NO_RESULTS_MESSAGE, request.getLanguage());

		return searchItemIdsResponse;
	}

}
