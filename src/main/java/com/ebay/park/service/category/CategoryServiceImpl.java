package com.ebay.park.service.category;

import com.ebay.park.service.ParkRequest;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.category.command.ListCategoriesCmd;
import com.ebay.park.service.category.dto.ListCategoriesResponse;
import com.ebay.park.service.category.validator.ListCategoriesValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * @author marcos.lambolay
 */
@Service
public class CategoryServiceImpl implements CategoryService {
	@Autowired
	private ListCategoriesValidator listCategoriesValidator;

	@Autowired
	private ListCategoriesCmd listCategoriesCmd;

	@Override
	public ListCategoriesResponse list(ParkRequest request) throws ServiceException{
		listCategoriesValidator.validate(request);
		return listCategoriesCmd.execute(request);

	}
}
