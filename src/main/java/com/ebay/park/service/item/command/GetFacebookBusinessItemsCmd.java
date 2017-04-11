package com.ebay.park.service.item.command;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.entity.Category;
import com.ebay.park.db.entity.StatusDescription;
import com.ebay.park.service.ParkRequest;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.item.dto.FacebookBusinessItem;
import com.ebay.park.util.TextUtils;

/**
 * Returns the Facebook Business item list.
 * 
 * @author scalderon
 *
 */
@Component
public class GetFacebookBusinessItemsCmd implements ServiceCommand<ParkRequest, List<FacebookBusinessItem>>{
	
	/**
	 * Facebook Catalog does not allowed an empty description. We defined a default value.
	 */
	private static final String NO_DESCRIPTION = "no description";
	
	@Autowired
	private ItemDao itemDao;
	
	@Autowired
	private TextUtils textUtils;
	
	
	@Override
	public List<FacebookBusinessItem> execute(ParkRequest request) throws ServiceException {
		Assert.notNull(request, "The request cannot be null");
		
		List<Object[]> facebookBusinessItems = itemDao.findAllForFacebookBusinessByStatus(StatusDescription.ACTIVE);
		List<FacebookBusinessItem> result = new ArrayList<>();
		if (!CollectionUtils.isEmpty(facebookBusinessItems)) {
			facebookBusinessItems.
				forEach(item -> result.add(createFacebookBusinessItem(item)));
		}
		
		return result;
	}
	
	/**
	 * Creates a new FacebookBusinessItem given an Object array.
	 * 
	 * item[0] -> item.id
	 * item[1] -> item.description
	 * item[2] -> item.picture1Url
     * item[3] -> item.category
     * item[4] -> item.name
	 * item[5] -> item.price
	 * @param item the Object array
	 * @return FacebookBusinessItem
	 */
	private FacebookBusinessItem createFacebookBusinessItem(Object[] item) {
		Long id = (Long)item[0];
		String description = NO_DESCRIPTION;
		if (item[1] != null && !StringUtils.isEmpty((String)item[1])) {
			//Add double quotes just in case the description contains commas.
			//Facebook catalog does not allowed all uppercase letters
			description = textUtils.doubleQuote(textUtils.capitalize((String)item[1]));
		}
		String imageLink = textUtils.doubleQuote((String)item[2]);
		Category category = (Category)item[3];
		//Add double quotes just in case the title contains commas
		//Facebook catalog does not allowed all uppercase letters
		String title = textUtils.doubleQuote(textUtils.capitalize((String)item[4]));
		String link = textUtils.doubleQuote(textUtils.createItemSEOURL(category.getKey(), (String)item[4], id));
		String price = ((Double)item[5]).toString() + " " + Currency.getInstance(Locale.US);
		String googleProductCategory = category.getKey().replace(".", ">");
		return new FacebookBusinessItem(id, description, imageLink, link, title, price, googleProductCategory);
	}
	
}
