/*
 * Copyright eBay, 2014
 */
package com.ebay.park.controller;

import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ebay.park.service.PaginatedRequest;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.ServiceResponseStatus;
import com.ebay.park.service.item.ItemService;
import com.ebay.park.service.item.dto.CreateItemRequest;
import com.ebay.park.service.item.dto.DeletePhotoRequest;
import com.ebay.park.service.item.dto.GetItemRequest;
import com.ebay.park.service.item.dto.ListUserItemsRequest;
import com.ebay.park.service.item.dto.ReportItemRequest;
import com.ebay.park.service.item.dto.RepublishItemRequest;
import com.ebay.park.service.item.dto.SearchItemRequest;
import com.ebay.park.service.item.dto.SearchItemRequest.SearchItemRequestBuilder;
import com.ebay.park.service.item.dto.ShareItemRequest;
import com.ebay.park.service.item.dto.UpdateItemRequest;
import com.ebay.park.service.item.dto.UploadPhotosRequest;
import com.ebay.park.service.item.dto.UserItemRequest;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.session.dto.UserSessionCache;
import com.ebay.park.util.GenericBuilder;
import com.ebay.park.util.ParkConstants;

/**
 * @author marcos.lambolay
 */
@RestController
public class ItemController implements ParkConstants {

	private static Logger logger = LoggerFactory.getLogger(ItemController.class);

	@Autowired
	private ItemService itemService;

	@Autowired
	private SessionService sessionService;
	
	
	@RequestMapping(value = {"/items/v3","/items/v3.0"}, method = RequestMethod.POST, consumes = "application/json")
	@ResponseStatus(value = HttpStatus.CREATED)
	public ServiceResponse createItem(@RequestHeader(PARK_TOKEN_HEADER) String parkToken,
			@RequestBody CreateItemRequest request) {
		try {
			request.setToken(parkToken);
			return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY,
					itemService.create(request));
		} catch (ServiceException e) {
			logger.error("error trying to create item. Token: {}", parkToken);
			e.setRequestToContext(request);
			throw e;
		} 
	}
 	
	@RequestMapping(value = {"/public/items/v3/search", "/public/items/v3.0/search"}, method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public ServiceResponse searchPublicItems(
			@RequestHeader(value = LANGUAGE_HEADER, required = false) String lang,
			@RequestParam(value = "latitude", required = false) Double latitude,
			@RequestParam(value = "longitude", required = false) Double longitude,
			@RequestParam(value = "radius", required = false) Integer radius,
			@RequestParam(value = "q", required = false) String criteria,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "priceFrom", required = false) Double priceFrom,
			@RequestParam(value = "priceTo", required = false) Double priceTo,
			@RequestParam(value = "categoryId", required = false) Long categoryId,
			@RequestParam(value = "groupId", required = false) Long groupId,
			@RequestParam(value = "publisherName", required = false) String publisherName,
			@RequestParam(value = "order", required = false) String order,
			@RequestParam(value = "requestTime", required = false) String requestTime) {
		//@formatter:off
		SearchItemRequest request = new SearchItemRequestBuilder(criteria)
		.setLatitude(latitude)
		.setLongitude(longitude)
		.setRadius(radius)
		.setToken(null)
		.setPage(page)
		.setPageSize(pageSize)
		.setPriceFrom(priceFrom)
		.setPriceTo(priceTo)
		.setCategoryId(categoryId)
		.setGroupId(groupId)
		.setOrder(order)
		.setLanguage(lang)
		.setFromFollowedUsers(false)
		.setFromUserWishlist(false)
		.setFromFollowedGroups(false)
		.setPublisherName(publisherName)
		.setRequestTime(requestTime)
		.build();
		//@formatter:on

		if (page != null) {
			request.setPage(page);
		}

		if (pageSize != null) {
			request.setPageSize(pageSize);
		}

		try {
			logger.debug("searching item....");
			return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY,
					itemService.search(request));
		} catch (ServiceException e) {
			logger.error("error trying to search items.");
			e.setRequestToContext(request);
			throw e;
		} 
	}

	@RequestMapping(value = {"/items/v3/search", "/items/v3.0/search"}, method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public ServiceResponse searchItem(
			@RequestHeader(value = PARK_TOKEN_HEADER, required = true) String parkToken,
			@RequestHeader(value = LANGUAGE_HEADER, required = false) String lang,
			@RequestParam(value = "latitude", required = false) Double latitude,
			@RequestParam(value = "longitude", required = false) Double longitude,
			@RequestParam(value = "radius", required = false) Integer radius,
			@RequestParam(value = "q", required = false) String criteria,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "priceFrom", required = false) Double priceFrom,
			@RequestParam(value = "priceTo", required = false) Double priceTo,
			@RequestParam(value = "categoryId", required = false) Long categoryId,
			@RequestParam(value = "groupId", required = false) Long groupId,
			@RequestParam(value = "fromFollowedUsers", required = false) boolean fromFollowedUsers,
			@RequestParam(value = "fromUserWishlist", required = false) boolean fromUserWishlist,
			@RequestParam(value = "fromFollowedGroups", required = false) boolean fromFollowedGroups,
			@RequestParam(value = "publisherName", required = false) String publisherName,
			@RequestParam(value = "order", required = false) String order,
			@RequestParam(value = "tagNewItem", required = false) boolean tagNewItem,
			@RequestParam(value = "requestTime", required = false) String requestTime) {
		//@formatter:off
		SearchItemRequest request = new SearchItemRequestBuilder(criteria)
		.setLatitude(latitude)
		.setLongitude(longitude)
		.setRadius(radius)
		.setToken(parkToken)
		.setPage(page)
		.setPageSize(pageSize)
		.setPriceFrom(priceFrom)
		.setPriceTo(priceTo)
		.setCategoryId(categoryId)
		.setGroupId(groupId)
		.setOrder(order)
		.setLanguage(lang)
		.setFromFollowedUsers(fromFollowedUsers)
		.setFromUserWishlist(fromUserWishlist)
		.setFromFollowedGroups(fromFollowedGroups)
		.setPublisherName(publisherName)
		.setTagNewItem(tagNewItem)
		.setRequestTime(requestTime)
		.build();
		//@formatter:on

		if (page != null) {
			request.setPage(page);
		}

		if (pageSize != null) {
			request.setPageSize(pageSize);
		}

		try {
			logger.debug("searching item....");
			return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY,
					itemService.search(request));
		} catch (ServiceException e) {
			logger.error("error trying to search items.");
			e.setRequestToContext(request);
			throw e;
		} 
	}
	
	/**
	 * This method was added in order to improve the item's transition from VIP
	 * @see <a href="https://jira.globant.com/browse/EPA001-10000"</a>
	 * @since v2.1.0
	 * 
	 * @param lang
	 * @param latitude
	 * @param longitude
	 * @param radius
	 * @param criteria
	 * @param page
	 * @param pageSize
	 * @param priceFrom
	 * @param priceTo
	 * @param categoryId
	 * @param groupId
	 * @param publisherName
	 * @param order
	 * @param requestTime
	 * @return ServiceResponse
	 * 				includes the item id list
	 */
	@RequestMapping(value = {"/public/items/v3/searchIds", "/public/items/v3.0/searchIds"}, method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public ServiceResponse searchPublicItemIds(
			@RequestHeader(value = LANGUAGE_HEADER, required = false) String lang,
			@RequestParam(value = "latitude", required = false) Double latitude,
			@RequestParam(value = "longitude", required = false) Double longitude,
			@RequestParam(value = "radius", required = false) Integer radius,
			@RequestParam(value = "q", required = false) String criteria,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "priceFrom", required = false) Double priceFrom,
			@RequestParam(value = "priceTo", required = false) Double priceTo,
			@RequestParam(value = "categoryId", required = false) Long categoryId,
			@RequestParam(value = "groupId", required = false) Long groupId,
			@RequestParam(value = "publisherName", required = false) String publisherName,
			@RequestParam(value = "order", required = false) String order,
			@RequestParam(value = "requestTime", required = false) String requestTime) {
		//@formatter:off
		SearchItemRequest request = new SearchItemRequestBuilder(criteria)
		.setLatitude(latitude)
		.setLongitude(longitude)
		.setRadius(radius)
		.setToken(null)
		.setPage(page)
		.setPageSize(pageSize)
		.setPriceFrom(priceFrom)
		.setPriceTo(priceTo)
		.setCategoryId(categoryId)
		.setGroupId(groupId)
		.setOrder(order)
		.setLanguage(lang)
		.setFromFollowedUsers(false)
		.setFromUserWishlist(false)
		.setFromFollowedGroups(false)
		.setPublisherName(publisherName)
		.setRequestTime(requestTime)
		.build();
		//@formatter:on

		if (page != null) {
			request.setPage(page);
		}

		if (pageSize != null) {
			request.setPageSize(pageSize);
		}

		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY,
					itemService.searchItemIds(request));
		} catch (ServiceException e) {
			logger.error("error trying to search item ids.");
			e.setRequestToContext(request);
			throw e;
		} 
	}
	
	/**
	 * This method was added in order to improve the item's transition from VIP
	 * @see <a href="https://jira.globant.com/browse/EPA001-10000"</a>
	 * @since v2.1.0 
	 * @param parkToken
	 * @param lang
	 * @param latitude
	 * @param longitude
	 * @param radius
	 * @param criteria
	 * @param page
	 * @param pageSize
	 * @param priceFrom
	 * @param priceTo
	 * @param categoryId
	 * @param groupId
	 * @param fromFollowedUsers
	 * @param fromUserWishlist
	 * @param fromFollowedGroups
	 * @param publisherName
	 * @param order
	 * @param tagNewItem
	 * @param requestTime
	 * @return ServiceResponse
	 * 				includes the item id list
	 */
	@RequestMapping(value = {"/items/v3/searchIds", "/items/v3.0/searchIds"}, method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public ServiceResponse searchItemIds(
			@RequestHeader(value = PARK_TOKEN_HEADER, required = true) String parkToken,
			@RequestHeader(value = LANGUAGE_HEADER, required = false) String lang,
			@RequestParam(value = "latitude", required = false) Double latitude,
			@RequestParam(value = "longitude", required = false) Double longitude,
			@RequestParam(value = "radius", required = false) Integer radius,
			@RequestParam(value = "q", required = false) String criteria,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "priceFrom", required = false) Double priceFrom,
			@RequestParam(value = "priceTo", required = false) Double priceTo,
			@RequestParam(value = "categoryId", required = false) Long categoryId,
			@RequestParam(value = "groupId", required = false) Long groupId,
			@RequestParam(value = "fromFollowedUsers", required = false) boolean fromFollowedUsers,
			@RequestParam(value = "fromUserWishlist", required = false) boolean fromUserWishlist,
			@RequestParam(value = "fromFollowedGroups", required = false) boolean fromFollowedGroups,
			@RequestParam(value = "publisherName", required = false) String publisherName,
			@RequestParam(value = "order", required = false) String order,
			@RequestParam(value = "tagNewItem", required = false) boolean tagNewItem,
			@RequestParam(value = "requestTime", required = false) String requestTime) {
		//@formatter:off
		SearchItemRequest request = new SearchItemRequestBuilder(criteria)
		.setLatitude(latitude)
		.setLongitude(longitude)
		.setRadius(radius)
		.setToken(parkToken)
		.setPage(page)
		.setPageSize(pageSize)
		.setPriceFrom(priceFrom)
		.setPriceTo(priceTo)
		.setCategoryId(categoryId)
		.setGroupId(groupId)
		.setOrder(order)
		.setLanguage(lang)
		.setFromFollowedUsers(fromFollowedUsers)
		.setFromUserWishlist(fromUserWishlist)
		.setFromFollowedGroups(fromFollowedGroups)
		.setPublisherName(publisherName)
		.setTagNewItem(tagNewItem)
		.setRequestTime(requestTime)
		.build();
		//@formatter:on

		if (page != null) {
			request.setPage(page);
		}

		if (pageSize != null) {
			request.setPageSize(pageSize);
		}

		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY,
					itemService.searchItemIds(request));
		} catch (ServiceException e) {
			logger.error("error trying to search item ids.");
			e.setRequestToContext(request);
			throw e;
		} 
	}
	
	/**
	 * This method was added in order to improve the item's transition from VIP
	 * @see <a href="https://jira.globant.com/browse/EPA001-10323"</a>
	 * @since v2.1.1
	 * @param parkToken
	 * @param lang
	 * @param latitude
	 * @param longitude
	 * @return ServiceResponse
	 */
	@RequestMapping(value = {"/items/v3/recommendedIds","/items/v3.0/recommendedIds"}, method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public ServiceResponse recommendedItemIds(
			@RequestHeader(value = PARK_TOKEN_HEADER, required = true) String parkToken,
			@RequestHeader(value = LANGUAGE_HEADER, required = false) String lang,
			@RequestParam(value = "latitude", required = false) Double latitude,
			@RequestParam(value = "longitude", required = false) Double longitude) {
		SearchItemRequest request = buildSearchItemRequestRecommendedItems(parkToken, latitude, longitude, lang);

		return getRecommendedItemIds(request); 
	}
	
	/**
	 * This method was added in order to improve the item's transition from VIP.
	 * Public endpoint: parkToken is not required
	 * @see <a href="https://jira.globant.com/browse/EPA001-10323"</a>
	 * @since v2.1.1
	 * @param lang
	 * @param latitude
	 * @param longitude
	 * @return ServiceResponse
	 */
	@RequestMapping(value = {"/public/items/v3/recommendedIds", "/public/items/v3.0/recommendedIds"}, method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public ServiceResponse publicRecommendedItemIds(
			@RequestHeader(value = LANGUAGE_HEADER, required = false) String lang,
			@RequestParam(value = "latitude", required = false) Double latitude,
			@RequestParam(value = "longitude", required = false) Double longitude) {
		SearchItemRequest request = buildSearchItemRequestRecommendedItems(null, latitude, longitude, lang);

		return getRecommendedItemIds(request); 
	}
	
	@RequestMapping(value = {"/public/items/v3/{id}", "/public/items/v3.0/{id}"}, method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse getItem(
			@RequestHeader(value = PARK_TOKEN_HEADER, required = false) String parkToken,
			@RequestHeader(value = LANGUAGE_HEADER, required = false) String lang,
			@PathVariable String id) {
		GetItemRequest request = new GetItemRequest(id, parkToken, lang);
		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY,
					itemService.get(request));
		} catch (ServiceException e) {
			logger.error("error trying to get item id: {}", id);
			e.setRequestToContext(request);
			throw e;
		} 
	}


	@RequestMapping(value = {"/items/v3/{id}","/items/v3.0/{id}"}, method = RequestMethod.PUT, consumes = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse updateItem(@PathVariable Long id,
			@RequestHeader(PARK_TOKEN_HEADER) String parkToken,
			@RequestParam(value = "feedWhenItemBanned", required = false) Boolean feedWhenItemBanned,
			@RequestBody UpdateItemRequest request) {
		try {
			request.setItemId(id);
			request.setToken(parkToken);
			request.setFeedWhenItemBanned(feedWhenItemBanned == null ? true : feedWhenItemBanned);

			return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY,
					itemService.update(request));
		} catch (ServiceException e) {
			logger.error("error trying to update item. Token: {}", parkToken);
			e.setRequestToContext(request);
			throw e;
		} 
	}

	@RequestMapping(value = {"/items/v3/{id}","/items/v3.0/{id}"}, method = RequestMethod.DELETE, consumes = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse removeItem(@PathVariable Long id,
			@RequestHeader(PARK_TOKEN_HEADER) String parkToken) {
		UserItemRequest request = new UserItemRequest(parkToken);
		request.setItemId(id);
		try {
			request.setToken(parkToken);
			return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY,
					itemService.remove(request));
		} catch (ServiceException e) {
			logger.error("Error trying to remove a item. Token: {}", parkToken);
			e.setRequestToContext(request);
			throw e;
		} 
	}

	@RequestMapping(value = {"/items/v3/{id}/pictures","/items/v3.0/{id}/pictures"}, method = RequestMethod.POST, consumes = { "multipart/form-data" })
	@ResponseStatus(value = HttpStatus.OK)
	// @formatter:off
	public ServiceResponse uploadPhotosItem(
			@RequestHeader(PARK_TOKEN_HEADER) String parkToken,
			@PathVariable Long id,
			@RequestParam(value = "photo1", required = false) MultipartFile photo1,
			@RequestParam(value = "photo2", required = false) MultipartFile photo2,
			@RequestParam(value = "photo3", required = false) MultipartFile photo3,
			@RequestParam(value = "photo4", required = false) MultipartFile photo4) {

		UploadPhotosRequest request = new UploadPhotosRequest.Builder()
		.itemId(id)
		.photo1(photo1)
		.photo2(photo2)
		.photo3(photo3)
		.photo4(photo4)
		.build();
		// @formatter:on
		try {
			// TODO validate the request
			request.setToken(parkToken);

			return itemService.uploadPhotos(request);

		} catch (ServiceException e) {
            logger.error("error trying to upload item photos. Token: {}", parkToken);
			throw e;
		} 
	}

	@RequestMapping(value = {"/items/v3/{itemId}/pictures/{photoIdList}", "/items/v3.0/{itemId}/pictures/{photoIdList}"}, method = RequestMethod.DELETE, consumes = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse deletePhotoItem(@RequestHeader(PARK_TOKEN_HEADER) String parkToken,
			@PathVariable("itemId") Long itemId, @PathVariable("photoIdList") Long[] photoIdList) {
		DeletePhotoRequest request = new DeletePhotoRequest(parkToken);
		try {

			request.setToken(parkToken);
			request.setItemId(itemId);
			request.setPictureId(photoIdList);

			return itemService.deletePhoto(request);

		} catch (ServiceException e) {
			logger.error("error trying to delete an item photo or a list of photos. Token: {}", parkToken);
			e.setRequestToContext(request);
			throw e;
		} 
	}

	/**
	 * The item is bought inside our app, but without a negotiation (conversation), the original price was accepted
	 * */
	@RequestMapping(value = {"/items/v3/{id}/buy", "/items/v3.0/{id}/buy"}, method = RequestMethod.PUT, consumes = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse buyDirectlyItem(@RequestHeader(PARK_TOKEN_HEADER) String parkToken,
			@PathVariable Long id) {
		UserItemRequest request = new UserItemRequest(parkToken);
		try {
			request.setToken(parkToken);
			request.setItemId(id);
			return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY,
					itemService.buyDirectly(request));
		} catch (ServiceException e) {
			logger.error("error trying to buy a item directly. Token: {}", parkToken);
			e.setRequestToContext(request);
			throw e;
		} 
	}

	/**
	 * The seller decides to mark this item as SOLD, but the transaction was realized outside our application
	 * */
	@RequestMapping(value = {"/items/v3/{id}/sold", "/items/v3.0/{id}/sold"}, method = RequestMethod.PUT, consumes = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse soldItem(@RequestHeader(PARK_TOKEN_HEADER) String parkToken, @PathVariable Long id) {
		UserItemRequest request = new UserItemRequest(parkToken);
		try {
			request.setToken(parkToken);
			request.setItemId(id);
			return itemService.sold(request);
		} catch (ServiceException e) {
			logger.error("error trying to mark item as sold. Token: {}", parkToken);
			e.setRequestToContext(request);
			throw e;
		} 
	}

	@RequestMapping(value = {"/items/v3/followed","/items/v3.0/followed"}, method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public ServiceResponse listFollowedItem(@RequestHeader(PARK_TOKEN_HEADER) String parkToken,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestHeader(value = LANGUAGE_HEADER, required = false) String language,
			@RequestParam(value = "requestTime", required = false) String requestTime) {
		PaginatedRequest request = new PaginatedRequest(parkToken, language, page, pageSize);
		request.setToken(parkToken);
		request.setPage(page);
		request.setPageSize(pageSize);
		request.setLanguage(language);
		request.setRequestTime(requestTime);
		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY,
					itemService.listFollowed(request));
		} catch (ServiceException e) {
			logger.error("error listing followed items. Token: {}", parkToken);
			e.setRequestToContext(request);
			throw e;
		} 
	}

	@RequestMapping(value = {"/items/v3/user/{username}", "/items/v3/user/{username}/{item_id_excluded}", "/items/v3.0/user/{username}", "/items/v3.0/user/{username}/{item_id_excluded}"}, method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public ServiceResponse listItems(@RequestHeader(value = PARK_TOKEN_HEADER, required = false) String parkToken,
			@RequestHeader(value = LANGUAGE_HEADER, required = false) String language,
			@PathVariable String username,
			@PathVariable Optional<Long> item_id_excluded,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "requestTime", required = false) String requestTime) {
	    boolean isOwnItems = false;
        if (!StringUtils.isEmpty(parkToken)) {
            UserSessionCache session = sessionService.getUserSession(parkToken);
            if (session != null && session.getUsername().equals(username)) {
                isOwnItems = true;
            }
        }

        ListUserItemsRequest request = GenericBuilder.of(
                () -> new ListUserItemsRequest(parkToken, language, page, pageSize))
                .with(ListUserItemsRequest::setUsername, username)
                .with(ListUserItemsRequest::setOwnItems, isOwnItems)
                .with(ListUserItemsRequest::setRequestTime, requestTime)
                .with(ListUserItemsRequest::setItemIdExcluded, item_id_excluded.isPresent() ? item_id_excluded.get() : null)
                .build();

        try {
            return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY,
                    itemService.list(request));
        } catch (ServiceException e) {
            logger.error("Error listing user's items. Token: {}", parkToken);
            e.setRequestToContext(request);
            throw e;
        } 
    }

    @RequestMapping(value = {"/items/v3/{itemId}/report", "/items/v3.0/{itemId}/report"}, method = RequestMethod.POST, consumes = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse reportItem(@RequestHeader(PARK_TOKEN_HEADER) String parkToken,
			@PathVariable Long itemId, @RequestBody ReportItemRequest request) {
		try {
			request.setToken(parkToken);
			request.setItemId(itemId);
			return itemService.report(request);
		} catch (ServiceException e) {
			logger.error("Error trying to report a item. Token: {}", parkToken);
			e.setRequestToContext(request);
			throw e;
		} 
	}

	@RequestMapping(value = {"/items/v3/{itemId}/report", "/items/v3.0/{itemId}/report"}, method = RequestMethod.DELETE, consumes = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse unreportItem(@RequestHeader(PARK_TOKEN_HEADER) String parkToken,
			@PathVariable Long itemId) {

		UserItemRequest request = new UserItemRequest(parkToken);
		try {
			request.setToken(parkToken);
			request.setItemId(itemId);
			return itemService.unreport(request);
		} catch (ServiceException e) {
			logger.error("Error trying to unreport a item. Token: {}", parkToken);
			e.setRequestToContext(request);
			throw e;
		} 
	}

	@RequestMapping(value = {"/items/v3/groups/user","/items/v3.0/groups/user"}, method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse listItemsInGroupsFollowedByUser(
			@RequestHeader(PARK_TOKEN_HEADER) String parkToken,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestHeader(value = LANGUAGE_HEADER, required = false) String language,
			@RequestParam(value = "requestTime", required = false) String requestTime) {
		PaginatedRequest request = new PaginatedRequest(parkToken, language, page, pageSize);
		request.setToken(parkToken);
		request.setPage(page);
		request.setPageSize(pageSize);
		request.setLanguage(language);
		request.setRequestTime(requestTime);
		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY,
					itemService.listItemsInGroupsFollowedByUser(request));
		} catch (ServiceException e) {
			logger.error("Error trying to list items in groups followed by user. Token: {}", parkToken);
			e.setRequestToContext(request);
			throw e;
		} 
	}

	@RequestMapping(value = {"/items/v3/{id}/follow", "/items/v3.0/{id}/follow"}, method = RequestMethod.PUT, consumes = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse followItem(@RequestHeader(PARK_TOKEN_HEADER) String parkToken,
			@PathVariable("id") Long id, @RequestBody UserItemRequest request) {
		try {
			request.setToken(parkToken);
			request.setItemId(id);
			itemService.follow(request);
			return ServiceResponse.SUCCESS;
		} catch (ServiceException e) {
			logger.error("Error trying to follow an item. Token: {}", parkToken);
			e.setRequestToContext(request);
			throw e;
		} 
	}

	@RequestMapping(value = {"/items/v3/{id}/follow", "/items/v3.0/{id}/follow"}, method = RequestMethod.DELETE, consumes = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse unfollowItem(@RequestHeader(PARK_TOKEN_HEADER) String parkToken,
			@PathVariable("id") Long id) {

		UserItemRequest request = new UserItemRequest(parkToken);

		try {
			request.setToken(parkToken);
			request.setItemId(id);
			itemService.unfollow(request);
			return ServiceResponse.SUCCESS;
		} catch (ServiceException e) {
			logger.error("Error trying to unfollow an item. Token: {}", parkToken);
			e.setRequestToContext(request);
			throw e;
		} 
	}

	@RequestMapping(value = {"/items/v3/{id}/share", "/items/v3.0/{id}/share"}, method = RequestMethod.POST, consumes = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse shareItem(@RequestHeader(PARK_TOKEN_HEADER) String parkToken,
			@PathVariable("id") Long itemId, @RequestBody ShareItemRequest request) {
		try {
			request.setItemId(itemId);
			request.setToken(parkToken);
			return itemService.share(request);
		} catch (ServiceException e) {
			logger.error("error trying to share item. Token: {}", parkToken);
			e.setRequestToContext(request);
			throw e;
		} 
	}

	/**
	 * Endpoint to republish a SOLD or EXPIRED item.
	 * @param parkToken
	 * @param itemId
	 * @return ServiceResponse
	 */
	@RequestMapping(value = {"/items/v3/{id}/republish","/items/v3.0/{id}/republish"}, method = RequestMethod.POST, consumes = "application/json")
	@ResponseStatus(value = HttpStatus.CREATED)
	public ServiceResponse republishItem(@RequestHeader(PARK_TOKEN_HEADER) String parkToken,
			@PathVariable("id") Long itemId) {
		RepublishItemRequest request = new RepublishItemRequest(parkToken);
		try {
			request.setItemId(itemId);
			return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY,
					itemService.republishItem(request));
		} catch (ServiceException e) {
			e.setRequestToContext(request);
			throw e;
		} 
	}
	
	/**
	 * Endpoint to republish a SOLD, EXPIRED and ACTIVE item.
	 * @see <a href=https://jira.globant.com/browse/EPA001-10504 />
	 * @param parkToken
	 * @param itemId
	 * @return ServiceResponse
	 */
	@RequestMapping(value = {"/items/v4/{id}/republish","/items/v4.0/{id}/republish"}, method = RequestMethod.POST, consumes = "application/json")
	@ResponseStatus(value = HttpStatus.CREATED)
	public ServiceResponse republishItemV4(@RequestHeader(PARK_TOKEN_HEADER) String parkToken,
			@PathVariable("id") Long itemId, @RequestBody RepublishItemRequest request) {
		try {
			request.setToken(parkToken);
			request.setItemId(itemId);
			return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY,
					itemService.republishItemV4(request));
		} catch (ServiceException e) {
			e.setRequestToContext(request);
			throw e;
		} 
	}
	
	@RequestMapping(value = {"/items/v3/recommended","/items/v3.0/recommended"}, method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public ServiceResponse itemsRecommended(
			@RequestHeader(value = PARK_TOKEN_HEADER, required = true) String parkToken,
			@RequestHeader(value = LANGUAGE_HEADER, required = false) String lang,
			@RequestParam(value = "latitude", required = false) Double latitude,
			@RequestParam(value = "longitude", required = false) Double longitude) {
		SearchItemRequest request = buildSearchItemRequestRecommendedItems(parkToken, latitude, longitude, lang);
		
		return getRecommendedItems(request); 
	}
	
	@RequestMapping(value = {"/public/items/v3/recommended", "/public/items/v3.0/recommended"}, method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public ServiceResponse itemsPublicRecommended(
			@RequestHeader(value = LANGUAGE_HEADER, required = false) String lang,
			@RequestParam(value = "latitude", required = false) Double latitude,
			@RequestParam(value = "longitude", required = false) Double longitude) {
		SearchItemRequest request = buildSearchItemRequestRecommendedItems(null, latitude, longitude, lang);

		return getRecommendedItems(request); 
	}
	
	/**
	 * Builds a {@link SearchItemRequest} for recommended items search
	 * @param parkToken (if request is public then parkToken is null)
	 * @param latitude
	 * @param longitude
	 * @param lang
	 * @return the SearchItemRequest
	 */
	private SearchItemRequest buildSearchItemRequestRecommendedItems(String parkToken, Double latitude, 
			Double longitude, String lang) {
		return new SearchItemRequestBuilder(null)
				.setToken(parkToken).setLatitude(latitude)
				.setLongitude(longitude).setLanguage(lang).build();
	}
	
	/**
	 * Returns a {@link ServiceResponse} for recommended item ids
	 * @param request
	 * 			the SearchItemRequest
	 * @return ServiceResponse
	 */
	private ServiceResponse getRecommendedItemIds(SearchItemRequest request) {
		Assert.notNull(request, "SearchItemRequest cannot be null");
		
		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS,
					StringUtils.EMPTY, itemService.getRecommendedItemIds(request));
		} catch (ServiceException e) {
			e.setRequestToContext(request);
			throw e;
		}
	}
	
	/**
	 * Returns a {@link ServiceResponse} for recommended items
	 * @param request
	 * 			the SearchItemRequest
	 * @return ServiceResponse
	 */
	private ServiceResponse getRecommendedItems(SearchItemRequest request) {
		Assert.notNull(request, "SearchItemRequest cannot be null");
		
		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS,
					StringUtils.EMPTY, itemService.getRecommendedItems(request));
		} catch (ServiceException e) {
			e.setRequestToContext(request);
			throw e;
		} 
	}
}
