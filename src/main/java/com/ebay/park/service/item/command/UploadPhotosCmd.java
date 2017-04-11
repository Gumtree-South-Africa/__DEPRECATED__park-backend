package com.ebay.park.service.item.command;

import static com.ebay.park.service.ServiceException.createServiceException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import com.ebay.park.util.LanguageUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.StatusDescription;
import com.ebay.park.db.entity.User;
import com.ebay.park.eps.EPSClient;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.blacklist.BlacklistService;
import com.ebay.park.service.item.ItemUtils;
import com.ebay.park.service.item.dto.UploadPhotosRequest;
import com.ebay.park.util.EPSUtils;

/**
 * @author marcos.lambolay
 */
@Component
public class UploadPhotosCmd extends
		UserItemCmd<UploadPhotosRequest, ServiceResponse>{

	@Autowired
	private ItemDao itemDao;

	@Autowired
	private EPSUtils EPSUtils;

	@Autowired
	private ItemUtils itemUtils;

	@Value("${createItem.maxAmountPictures}")
	private Integer maxAmountPictures;

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private BlacklistService blackListService;

	@Autowired
	EPSClient epsClient;
	
	private final DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ");

	
	private static final Logger LOGGER = LoggerFactory.getLogger(UploadPhotosCmd.class);
	
	@Override
	public ServiceResponse execute(UploadPhotosRequest request)
			throws ServiceException {
	
		
		User user = userDao.findByToken(request.getToken());
		
		String username = user.getUsername();
		
		Item item = getItemUser(request);

		Map<Integer, String> photos = new HashMap<>(maxAmountPictures);

		String url;
		int photoId;
		MultipartFile photoFile;
		
			for (int i = 1; i <= maxAmountPictures; i++) {
				photoId = i;
				
				try {
					Field field = UploadPhotosRequest.class
							.getDeclaredField("photo" + i);
					field.setAccessible(true);
					photoFile = (MultipartFile) field.get(request);
	
					url = null;
					if (photoFile != null) {
						photoId = calculatePhotoIndex(item, photos, i);
	
						String fileName = createPhotoFileName(username, item, photoId);
                        LOGGER.info("Trying to Upload Item's Picture into ePS. Original Picture Name: {}", fileName);
						url = epsClient.publish(fileName,photoFile);
					}
				} catch (IllegalAccessException | InvocationTargetException
						| NoSuchFieldException | SecurityException
						| NoSuchMethodException e) {
					throw new RuntimeException(e);
				}
				
				if (url != null && !StringUtils.isEmpty(url)) {
					photos.put(photoId, url);
				} else if (photoFile != null) {
					throw createServiceException(ServiceExceptionCode.ITEM_PICTURE_UPLOAD_FAILED);
				}
			}

			try {
				itemUtils.setPictures(photos, item);
				itemDao.save(item);
			} catch (IllegalAccessException | InvocationTargetException e) {
				throw new RuntimeException(e);
		}
		
		if (item.is(StatusDescription.IMAGE_PENDING)) {
			if (blackListService.isBlackListed(item)) {
				LOGGER.debug(
						"Sending a NotificationAction.ITEM_BANNED, Item: {}",
						request.getItemId());
				blackListService.bannedItem(item, getLanguage(request, user));
			} else {
				LOGGER.debug("Activating Item id: {}", request.getItemId());
				itemUtils.activateItem(item);	
				itemUtils.shareNewItem(item, request.getToken());
			}
		}
		return ServiceResponse.SUCCESS;
	}

	private String getLanguage(UploadPhotosRequest request, User user) {
		return LanguageUtil.getLanguageForUserRequest(user, request.getLanguage());
	}

	private int calculatePhotoIndex(Item item, Map<Integer, String> photos,
			int currentIndex) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		int photoId;
		photoId = currentIndex - 1;
		if (photoId > 0) {
			String previous = BeanUtils.getProperty(item, "picture" + photoId
					+ "Url");
			while (photoId > 0 && previous == null
					&& !photos.containsKey(photoId)) {
				previous = BeanUtils.getProperty(item, "picture" + photoId
						+ "Url");
				photoId--;
			}
		}

		photoId++;
		return photoId;
	}

	private String createPhotoFileName(String username, Item item,int i) {
		return EPSUtils.getPrefix() + username + "/item/"+ item.getId() +"/picture" + i + "_"
				+ DateTime.now().toString(fmt);
	}

}