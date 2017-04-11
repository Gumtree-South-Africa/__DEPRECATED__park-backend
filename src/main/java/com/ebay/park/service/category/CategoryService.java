package com.ebay.park.service.category;

import com.ebay.park.service.ParkRequest;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.category.dto.ListCategoriesResponse;

/**
 * 
 * @author marcos.lambolay
 */
public interface CategoryService {
	public ListCategoriesResponse list(ParkRequest request) throws ServiceException;
}
