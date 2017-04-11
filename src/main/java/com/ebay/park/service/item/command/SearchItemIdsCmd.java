package com.ebay.park.service.item.command;

import static com.ebay.park.service.ServiceException.createServiceException;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.User;
import com.ebay.park.elasticsearch.document.ItemDocument;
import com.ebay.park.elasticsearch.document.converter.DocumentConverter;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.item.dto.SearchItemIdsResponse;
import com.ebay.park.service.item.dto.SearchItemRequest;
import com.ebay.park.util.DataCommonUtil;
import com.ebay.park.util.InternationalizationUtil;

/**
 * This command was added in order to improve the item's transition from VIP.
 * Returns a list of item ids for given search parameters.
 * 
 * @author scalderon
 *
 */
@Component
public class SearchItemIdsCmd implements ServiceCommand <SearchItemRequest, SearchItemIdsResponse>{

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private DocumentConverter documentConverter;
	
	@Autowired
	private SearchItemCmdHelper searchCmdHelper;
	

	@Autowired
	private InternationalizationUtil i18nUtil;


	@Override
	public SearchItemIdsResponse execute(SearchItemRequest request) throws ServiceException {
		Assert.notNull(request, "The request cannot be null");
		User user = null;
		
		if (!StringUtils.isBlank(request.getToken())) {
			user = userDao.findByToken(request.getToken());
			if (user == null) {
				throw createServiceException(ServiceExceptionCode.USER_UNAUTHORIZED);
			}		
		}

		try {

			Page<ItemDocument> page = searchCmdHelper.search(request);
			if (page != null) {
				List<ItemDocument> result = page.getContent();
				List<Long> itemIds = documentConverter.itemIdsfromItemDocument(result);
	
				return new SearchItemIdsResponse(itemIds, page.getTotalElements(),
						DataCommonUtil.getDateTimeAsUnixFormat(DateTime.now().toDate()));
			}
			
			return buildEmptyQueryResponse(request, user);
		} catch (Exception e){
			e.printStackTrace();
			return buildEmptyQueryResponse(request, user);
		}
	}
	
	private SearchItemIdsResponse buildEmptyQueryResponse(SearchItemRequest request, User user) {
		SearchItemIdsResponse searchItemIdsResponse = new SearchItemIdsResponse(new ArrayList<Long>(), 0,
				DataCommonUtil.getDateTimeAsISO(DateTime.now().toDate()));
		
		String lang = request.getLanguage() != null ? request.getLanguage() : user.getIdiom().getCode();
		i18nUtil.internationalizeListedResponse(searchItemIdsResponse, searchCmdHelper.getNoResultMessageKey(request), lang);
		return searchItemIdsResponse;
	}

}
