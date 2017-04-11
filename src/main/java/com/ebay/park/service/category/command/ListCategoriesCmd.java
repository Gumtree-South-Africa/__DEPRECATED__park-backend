package com.ebay.park.service.category.command;

import com.ebay.park.db.dao.CategoryDao;
import com.ebay.park.db.entity.Category;
import com.ebay.park.service.ParkRequest;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.category.dto.ListCategoriesResponse;
import com.ebay.park.service.category.dto.SmallCategory;
import com.ebay.park.util.InternationalizationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 
 * @author marcos.lambolay
 */
@Component
public class ListCategoriesCmd
implements ServiceCommand<ParkRequest, ListCategoriesResponse>{

	@Autowired
	private CategoryDao categoryDao;

	@Autowired
	private InternationalizationUtil i18nUtil;

	@Override
	public ListCategoriesResponse execute(ParkRequest request) throws ServiceException {

		ListCategoriesResponse response = new ListCategoriesResponse();
		List<Category> categories  = null;
		categories = i18nUtil.internationalize(categoryDao.getAllCategories(), request.getLanguage());
		for(Category c : categories) {
			response.add(new SmallCategory(c.getCategoryId(), c.getName(), c
					.getWebColor(), c.getSelectable()));
		}

		return response;
	}
}
