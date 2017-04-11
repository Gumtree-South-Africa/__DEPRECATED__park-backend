package com.ebay.park.service.item.command;


import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.StatusDescription;
import com.ebay.park.service.PaginatedRequest;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.item.ItemUtils;
import com.ebay.park.service.item.dto.ListFollowedItemsResponse;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.session.dto.UserSessionCache;
import com.ebay.park.util.DataCommonUtil;
import com.ebay.park.util.InternationalizationUtil;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class ListItemsInGroupsFollowedByUserCmd implements
		ServiceCommand<PaginatedRequest, ListFollowedItemsResponse> {
	
	private static final String NO_RESULTS_MESSAGE = "emptylist.groups_items";

	@Value("${search.pageSize}")
	private int defaultPageSize;

	@Autowired
	private ItemDao itemDao;

	@Autowired
	private SessionService sessionService;

	@Autowired
	private ItemUtils itemUtils;
	
	@Autowired
	private InternationalizationUtil i18nUtil;

	@Override
	public ListFollowedItemsResponse execute(PaginatedRequest request)
			throws ServiceException {

		int page = request.getPage() != null ? request.getPage() : 0;
		int pageSize = request.getPageSize() != null
				&& request.getPageSize() > 0 ? request.getPageSize()
				: defaultPageSize;

		Pageable pageable = new PageRequest(page, pageSize);
		UserSessionCache session = sessionService.getUserSession(request.getToken());
		StatusDescription[] status = StatusDescription.visibleStatusForFollowedItems;
		Page<Item> list;
		if(request.getRequestTime() != null){
			list = itemDao.listItemsInGroupsFollowedByUser(
				session.getUserId(), status, pageable, DataCommonUtil.parseUnixTime(request.getRequestTime()));
		}
		else{
			list = itemDao.listItemsInGroupsFollowedByUser(
					session.getUserId(), status, pageable);
		}
		
		ListFollowedItemsResponse response = new ListFollowedItemsResponse(
				itemUtils.convertToItemSummaryFromPublisher(list.getContent(), request.getLanguage()),
				list.getNumberOfElements(), list.getTotalElements(), list.getTotalPages(), DataCommonUtil.getDateTimeAsUnixFormat(DateTime.now().toDate())); 
	
		
		i18nUtil.internationalizeListedResponse(response, NO_RESULTS_MESSAGE, 
				request.getLanguage() != null ? request.getLanguage() : session.getLang());

		return response;
	}

}
