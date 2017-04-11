package com.ebay.park.service.item.command;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.StatusDescription;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.PaginatedRequest;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.item.ItemUtils;
import com.ebay.park.service.item.dto.ListFollowedItemsResponse;
import com.ebay.park.util.DataCommonUtil;
import com.ebay.park.util.InternationalizationUtil;

/**
 * 
 * @author federico.jaite
 */
@Component
public class ListFollowedItemsCmd implements
ServiceCommand<PaginatedRequest, ListFollowedItemsResponse> {

	private static final String NO_RESULTS_MESSAGE = "emptylist.following_items";
	
	@Value("${search.pageSize}")
	private int defaultPageSize;

	@Autowired
	private ItemDao itemDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private InternationalizationUtil i18nUtil;
	
	@Autowired
	private ItemUtils itemUtils;

	@Override
	public ListFollowedItemsResponse execute(PaginatedRequest request)
			throws ServiceException {

		int pageSize = request.getPageSize() != null
				&& request.getPageSize() > 0 ? request.getPageSize()
						: defaultPageSize;

				int page = request.getPage() != null ? request.getPage() : 0;
				Sort sort = new Sort(Sort.Direction.DESC, "published");
				Pageable pageable = new PageRequest(page, pageSize, sort);

				User user = userDao.findByToken(request.getToken());

				StatusDescription[] status = StatusDescription.visibleStatusForFollowedItems;
				Page<Item> listItemsFollowed;
				if(request.getRequestTime() != null){
					listItemsFollowed = itemDao.listItemsFollowed(user.getId(),DataCommonUtil.parseUnixTime(request.getRequestTime()),
						status, pageable);
				}
				else{
					listItemsFollowed = itemDao.listItemsFollowed(user.getId(),
							status, pageable);
				}

				ListFollowedItemsResponse response = new ListFollowedItemsResponse(
						
						itemUtils.convertToItemSummary(i18nUtil.internationalizeItems(listItemsFollowed.getContent(), request), user, request.getLanguage())
						, listItemsFollowed.getNumberOfElements()
						, listItemsFollowed.getTotalElements()
						, listItemsFollowed.getTotalPages()
						, DataCommonUtil.getDateTimeAsUnixFormat(DateTime.now().toDate())
		
				);
				
				i18nUtil.internationalizeListedResponse(response, NO_RESULTS_MESSAGE, (request.getLanguage() != null? request.getLanguage() : user.getIdiom().getCode()));

				return response;
	}
}