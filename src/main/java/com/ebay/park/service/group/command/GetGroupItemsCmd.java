package com.ebay.park.service.group.command;

import com.ebay.park.db.dao.GroupDao;
import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Group;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.StatusDescription;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.group.dto.GetGroupItemsRequest;
import com.ebay.park.service.item.ItemUtils;
import com.ebay.park.service.item.dto.ItemSummary;
import com.ebay.park.service.item.dto.SearchItemResponse;
import com.ebay.park.util.DataCommonUtil;
import com.ebay.park.util.InternationalizationUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.ebay.park.service.ServiceException.createServiceException;

@Component
public class GetGroupItemsCmd implements ServiceCommand<GetGroupItemsRequest, SearchItemResponse>  {

		private static final String EMPTY_LIST_GROUP_ITEMS = "emptylist.group_items";

		@Autowired
		GroupDao groupDao;
		
		@Autowired
		UserDao userDao;
		
		@Autowired
		ItemDao itemDao;
		
		@Autowired
		private ItemUtils itemUtils;
		
		@Value("${search.pageSize.mobile}")
		private int defaultPageSizeMobile;
		
		@Autowired
		private InternationalizationUtil i18nUtil;
		
		
		@Override
		public SearchItemResponse execute(GetGroupItemsRequest request) throws ServiceException {
			
			int pageSize = calculatePageSize(request.getPageSize());
			int pageIndex = calculatePageIndex(request.getPage());
			
			Group group = groupDao.findOne(request.getGroupId());
			if (group == null) {
				throw createServiceException(ServiceExceptionCode.INVALID_GROUP);
			}
			
			//FIXME temporary: we made this cmd public in order to make able to use it from web
//			User user = null;
//			if (!StringUtils.isBlank(request.getToken())) {
//				user = userDao.findByToken(request.getToken());
//				if (user == null) {
//					throw createServiceException(ServiceExceptionCode.USER_UNAUTHORIZED);
//				}		
//			}
			
			PageRequest pageReq = new PageRequest(pageIndex, pageSize);
	
			Page<Item> items = itemDao.listItemsFromGroup(group.getId(), StatusDescription.ACTIVE, pageReq);
					
			i18nUtil.internationalizeItems(items.getContent(), request);
			
			List<ItemSummary> itemsDTO = itemUtils.convertToItemSummary(items.getContent(), /*user, FIXME: temporary*/request.getLanguage());
				
			
			SearchItemResponse searchItemResponse = new SearchItemResponse(itemsDTO, items.getTotalElements(),
					DataCommonUtil.getDateTimeAsISO(DateTime.now().toDate()));
			
			i18nUtil.internationalizeListedResponse(searchItemResponse, EMPTY_LIST_GROUP_ITEMS, request.getLanguage());
			
			return searchItemResponse;
		

		}
		
		private int calculatePageIndex(Integer page) {
			if (page == null || page <= 0) {
				return 0;
			}
			return page;
		}

		private int calculatePageSize(Integer pageSize) {
			if (pageSize == null || pageSize <= 0) {
				return defaultPageSizeMobile;
			}

			return pageSize;
		}


}
