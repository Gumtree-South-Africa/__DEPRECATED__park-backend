package com.ebay.park.service.item.validator;

import static com.ebay.park.service.ServiceException.createServiceException;

import java.lang.reflect.Field;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.entity.Item;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceValidator;
import com.ebay.park.service.item.dto.UploadPhotosRequest;

@Component
public class UploadPhotosValidator implements
		ServiceValidator<UploadPhotosRequest> {

	@Autowired
	private ItemDao itemDao;

	@Value("${createItem.maxAmountPictures}")
	private Integer maxAmountPictures;

	@Override
	public void validate(UploadPhotosRequest toValidate) {
		Assert.notNull(toValidate, "The request cannot be null");
		
		Item item = itemDao.findOne(toValidate.getItemId());

		try {
			for (int i = 1; i <= maxAmountPictures; i++) {
				Field field = UploadPhotosRequest.class
						.getDeclaredField("photo" + i);
				field.setAccessible(true);
				MultipartFile photoFile = (MultipartFile) field.get(toValidate);
				if (photoFile != null) {
					validateIsImage(photoFile);
				}
			}
		} catch (NoSuchFieldException | SecurityException
				| IllegalArgumentException | IllegalAccessException e) {
			throw createServiceException(ServiceExceptionCode.ITEM_UPLOAD_PHOTO_ERROR, e);
		}

		if (item == null) {
			throw createServiceException(ServiceExceptionCode.ITEM_NOT_FOUND);
		}

		if (item.getPicture1Url() == null && toValidate.getPhoto1() == null) {
			throw createServiceException(ServiceExceptionCode.MANDATORY_PICTURE_NOT_UPLOADED);
		}
	}

	private void validateIsImage(MultipartFile photoFile) {
		String contentType = photoFile.getContentType();
		if (!contentType.matches("image/.*")) {
			ServiceException se = createServiceException(ServiceExceptionCode.INVALID_PICTURE_FORMAT, new String[] { contentType });
			throw se;
		}
	}

}
