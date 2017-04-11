package com.ebay.park.service.item;

import java.util.List;

import com.ebay.park.service.PaginatedRequest;
import com.ebay.park.service.ParkRequest;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.item.dto.*;

/**
 * @author marcos.lambolay
 */
public interface ItemService {

	public CreateItemResponse create(CreateItemRequest request) throws ServiceException;

	public SearchItemResponse search(SearchItemRequest request);

	public ListUserItemsResponse list(ListUserItemsRequest request);

	public GetItemResponse get(GetItemRequest request);

	public UpdateItemResponse update(UpdateItemRequest request);

	public Boolean remove(UserItemRequest request) throws ServiceException;

	public ServiceResponse uploadPhotos(UploadPhotosRequest request);

	public BuyItemDirectlyResponse buyDirectly(UserItemRequest request);

	public ServiceResponse sold(UserItemRequest request);

	public ListFollowedItemsResponse listFollowed(PaginatedRequest request);

	public ServiceResponse report(ReportItemRequest request);

	public ServiceResponse unreport(UserItemRequest request);

	public void follow(UserItemRequest request);

	public void unfollow(UserItemRequest request);

	public ListFollowedItemsResponse listItemsInGroupsFollowedByUser(PaginatedRequest request);

	public ServiceResponse share(ShareItemRequest request);

	public ServiceResponse deletePhoto(DeletePhotoRequest request);

	/**
	 * Republishes a SOLD or EXPIRED item
	 * @param request
	 * @return RepublishItemResponse
	 */
	public RepublishItemResponse republishItem(RepublishItemRequest request);
	
	/**
	 * Republishes a SOLD, EXPIRED or ACTIVE item
	 * @see <a href=https://jira.globant.com/browse/EPA001-10504 />
	 * @param request
	 * @return RepublishItemResponse
	 */
	public RepublishItemResponse republishItemV4(RepublishItemRequest request);
	
	public SearchItemResponse getRecommendedItems(SearchItemRequest request);
	
	/**
	 * Returns a {@link FacebookBusinessItem} list
	 * @param request
	 * @return active items in csv format
	 */
	public List<FacebookBusinessItem> getFacebookBusinessItems(ParkRequest request);
	
	/**
	 * Returns an item id list to improve the item's transitions from V.I.P.
	 * @param request
	 * 			the search parameters
	 * @return SearchItemIdsResponse
	 * 				the response that contains the item ids
	 */
	public SearchItemIdsResponse searchItemIds(SearchItemRequest request);
	
	/**
	 * Returns a recommended item id list to improve the item's transitions from V.I.P.
	 * @param request
	 * 			the search parameters
	 * @return SearchItemIdsResponse
	 * 				the response that contains the item ids
	 */
	public SearchItemIdsResponse getRecommendedItemIds(SearchItemRequest request);

}
