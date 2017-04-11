package com.ebay.park.service.item.command;

import static com.ebay.park.service.ServiceException.createServiceException;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.entity.Item;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.item.ItemUtils;
import com.ebay.park.service.item.dto.DeletePhotoRequest;

@Component
public class DeletePhotoCmd extends
		UserItemCmd<DeletePhotoRequest, ServiceResponse> {

	@Autowired
	private ItemDao itemDao;

	@Autowired
	private ItemUtils itemUtils;

	@Override
	public ServiceResponse execute(DeletePhotoRequest request)
			throws ServiceException {

		Item item = getItemUser(request);

		for(Long pictureId: request.getPictureIdList()) {

    		try {
    			String pictureField = "picture" + pictureId + "Url";

    			String url = BeanUtils.getProperty(item, pictureField);
    
    			if (StringUtils.isEmpty(url)) {
    				throw createServiceException(ServiceExceptionCode.PICTURE_NOT_FOUND);
    			}

    			BeanUtils.setProperty(item, pictureField, null);
    		} catch (IllegalAccessException | InvocationTargetException
    				| NoSuchMethodException e) {
    			throw createServiceException(ServiceExceptionCode.ERROR_DELETING_PICTURE);
    		}
		}
		itemUtils.rearrangeItemPictures(item);
		itemDao.save(item);

		return ServiceResponse.SUCCESS;
	}

}