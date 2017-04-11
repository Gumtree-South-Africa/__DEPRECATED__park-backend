package com.ebay.park.service.moderationMode.validator;

import static com.ebay.park.service.ServiceException.createServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ebay.park.db.dao.CategoryDao;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceValidator;
import com.ebay.park.service.moderationMode.dto.ApplyFiltersForModerationModeRequest;

@Component
public class ApplyFiltersForModerationModeValidator implements ServiceValidator<ApplyFiltersForModerationModeRequest> {

	@Autowired
	private CategoryDao categoryDao;

	@Override
	public void validate(ApplyFiltersForModerationModeRequest request) {
		if (request.getCategoryId() != null && categoryDao.findOne(request.getCategoryId()) == null) {
			throw createServiceException(ServiceExceptionCode.CATEGORY_NOT_FOUND);
		}
	}

}
