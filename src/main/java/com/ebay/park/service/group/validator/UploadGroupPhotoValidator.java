package com.ebay.park.service.group.validator;

import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceValidator;
import com.ebay.park.service.group.dto.UploadGroupPhotoRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import static com.ebay.park.service.ServiceException.createServiceException;
@Component
public class UploadGroupPhotoValidator implements
		ServiceValidator<UploadGroupPhotoRequest> {

	@Override
	public void validate(UploadGroupPhotoRequest toValidate) {
		if (toValidate.getPhoto() == null) {
			throw createServiceException(ServiceExceptionCode.EMPTY_PICTURE);
		}
		validateIsImage(toValidate.getPhoto());
	}

	private void validateIsImage(MultipartFile photoFile) {
		String contentType = photoFile.getContentType();
		if (!contentType.matches("image/.*")) {
			ServiceException se = createServiceException(ServiceExceptionCode.INVALID_PICTURE_FORMAT, new String[] { contentType });
			throw se;
		}
	}

}
