package com.ebay.park.service.item.command;

import static com.ebay.park.service.ServiceException.createServiceException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ebay.park.db.dao.CategoryDao;
import com.ebay.park.db.dao.GroupDao;
import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Category;
import com.ebay.park.db.entity.Group;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.item.dto.CreateItemRequest;
import com.ebay.park.service.item.dto.CreateItemResponse;
import com.ebay.park.util.TextUtils;
/**
 * 
 * @author marcos.lambolay
 */
@Component
public class CreateItemCmd implements ServiceCommand<CreateItemRequest, CreateItemResponse> {


	private static Logger logger = LoggerFactory.getLogger(CreateItemCmd.class);

	@Autowired
	private ItemDao itemDao;

	@Autowired
	private CategoryDao categoryDao;

	@Autowired
	private UserDao userDao;
	
	@Autowired
	@Value("${createItem.maxAmountPictures}")
	private Integer maxAmountPictures;

	@Autowired
	private GroupDao groupDao;

	@Autowired
	private TextUtils textUtils;

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public CreateItemResponse execute(CreateItemRequest request)
			throws ServiceException {
		CreateItemResponse response = new CreateItemResponse();
		User user = userDao.findByToken(request.getToken());

		if (user == null) {
			throw createServiceException(ServiceExceptionCode.USER_UNAUTHORIZED);
		}

		// copy request to item
		Item item = new Item(request.getName(), Double.valueOf(request.getPrice()), request.getVersionPublish(), request.getShareOnFacebook(), request.getShareOnTwitter());
		item.setBrandPublish(request.getBrandPublish());
		item.setDescription(request.getDescription());
	
		// load and set category
		Category category = categoryDao.findOne(request.getCategoryId());
		item.setCategory(category);

		item.setPublishedBy(user);

		if (StringUtils.isBlank(request.getLocation())){
			item.setLatitude(user.getLatitude());
			item.setLongitude(user.getLongitude());
			item.setLocationName(user.getLocationName());
			item.setZipCode(user.getZipCode());
			//TODO: Location will be removed in next releases
			item.setLocation("-1");	
		}else{
			item.setLatitude(request.getLatitude());
			item.setLongitude(request.getLongitude());
			item.setLocationName(request.getLocationName());
			item.setZipCode(request.getZipCode());
			//TODO: Location will be removed in next releases
			item.setLocation(request.getLocation());
		}
		
		itemDao.save(item);

		if (request.getGroups() != null && request.getGroups().length > 0) {
			for (String groupId : request.getGroups()) {

				Group group = groupDao.findOne(new Long(groupId));
				if (group == null) {
                    logger.error("invalid group: {}", groupId);
					throw createServiceException(ServiceExceptionCode.INVALID_GROUP);
				}
				item.addGroup(group);
				groupDao.save(group);
			}
		}
				
		response.setId(item.getId());
		response.setURL(textUtils.createItemSEOURL(item.getCategory().getKey(),
				item.getName(), item.getId()));

		return response;
	}
	
}
