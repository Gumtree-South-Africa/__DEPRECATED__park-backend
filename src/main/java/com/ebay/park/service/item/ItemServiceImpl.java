package com.ebay.park.service.item;

import static com.ebay.park.service.ServiceException.createServiceException;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ebay.park.db.entity.Social;
import com.ebay.park.event.user.UserItemToFollowersEvent;
import com.ebay.park.service.PaginatedRequest;
import com.ebay.park.service.ParkRequest;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.ServiceValidator;
import com.ebay.park.service.item.command.BuyItemDirectlyCmd;
import com.ebay.park.service.item.command.GetFacebookBusinessItemsCmd;
import com.ebay.park.service.item.command.GetItemCmd;
import com.ebay.park.service.item.command.ListFollowedItemsCmd;
import com.ebay.park.service.item.command.ListItemsInGroupsFollowedByUserCmd;
import com.ebay.park.service.item.command.ListUserItemsCmd;
import com.ebay.park.service.item.command.RecommendedItemIdsCmd;
import com.ebay.park.service.item.command.RecommendedItemsCmdImpl;
import com.ebay.park.service.item.command.RepublishItemCmd;
import com.ebay.park.service.item.command.RepublishItemV4Cmd;
import com.ebay.park.service.item.command.SearchItemCmd;
import com.ebay.park.service.item.command.SearchItemIdsCmd;
import com.ebay.park.service.item.command.ShareItemCmd;
import com.ebay.park.service.item.command.UploadPhotosCmd;
import com.ebay.park.service.item.command.UserFollowItemCmd;
import com.ebay.park.service.item.command.UserItemCmd;
import com.ebay.park.service.item.command.UserReportItemCmd;
import com.ebay.park.service.item.command.UserUnfollowItemCmd;
import com.ebay.park.service.item.command.UserUnreportItemCmd;
import com.ebay.park.service.item.dto.BuyItemDirectlyResponse;
import com.ebay.park.service.item.dto.CreateItemRequest;
import com.ebay.park.service.item.dto.CreateItemResponse;
import com.ebay.park.service.item.dto.DeletePhotoRequest;
import com.ebay.park.service.item.dto.FacebookBusinessItem;
import com.ebay.park.service.item.dto.GetItemRequest;
import com.ebay.park.service.item.dto.GetItemResponse;
import com.ebay.park.service.item.dto.ListFollowedItemsResponse;
import com.ebay.park.service.item.dto.ListUserItemsRequest;
import com.ebay.park.service.item.dto.ListUserItemsResponse;
import com.ebay.park.service.item.dto.ReportItemRequest;
import com.ebay.park.service.item.dto.RepublishItemRequest;
import com.ebay.park.service.item.dto.RepublishItemResponse;
import com.ebay.park.service.item.dto.SearchItemIdsResponse;
import com.ebay.park.service.item.dto.SearchItemRequest;
import com.ebay.park.service.item.dto.SearchItemResponse;
import com.ebay.park.service.item.dto.ShareItemRequest;
import com.ebay.park.service.item.dto.UpdateItemRequest;
import com.ebay.park.service.item.dto.UpdateItemResponse;
import com.ebay.park.service.item.dto.UploadPhotosRequest;
import com.ebay.park.service.item.dto.UserItemRequest;
import com.ebay.park.service.item.validator.CreateItemValidator;
import com.ebay.park.service.item.validator.GetItemValidator;
import com.ebay.park.service.item.validator.ShareItemValidator;
import com.ebay.park.service.item.validator.UpdateItemValidator;
import com.ebay.park.service.item.validator.UploadPhotosValidator;
import com.ebay.park.util.InternationalizationUtil;

/**
 * @author marcos.lambolay
 */
@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private CreateItemValidator createItemValidator;
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ItemServiceImpl.class);

	@Autowired
	@Qualifier("createItemCmd")
	private ServiceCommand<CreateItemRequest, CreateItemResponse> createItemCmd;

	@Autowired
	@Qualifier("searchItemCmdImpl")
	private SearchItemCmd searchItemCmd;

	@Autowired
	private ListUserItemsCmd listUserItemsCmd;

	@Autowired
	private GetItemCmd getItemCmd;

	@Autowired
	private GetItemValidator getItemValidator;

	@Autowired
	private UpdateItemValidator updateItemValidator;

	@Autowired
	@Qualifier("updateItemCmd")
	private ServiceCommand<UpdateItemRequest, UserItemToFollowersEvent> updateItemCmd;

	@Autowired
	@Qualifier("removeItemCmd")
	private ServiceCommand<UserItemRequest, UserItemToFollowersEvent> removeItemCmd;

	@Autowired
	private UploadPhotosCmd uploadPhotosCmd;

	@Autowired
	private BuyItemDirectlyCmd buyItemDirectlyCmd;

	@Autowired
	@Qualifier("soldItemCmd")
	private ServiceCommand<UserItemRequest, ServiceResponse> soldItemCmd;

	@Autowired
	private ListFollowedItemsCmd listFollowedItemsCmd;

	@Autowired
	private UserReportItemCmd userReportItemCmd;

	@Autowired
	private UserUnreportItemCmd userUnreportItemCmd;

	@Autowired
	private ListItemsInGroupsFollowedByUserCmd listItemsInGroupsFollowedByUserCmd;

	@Autowired
	private UserFollowItemCmd userFollowItemCmd;

	@Autowired
	private UserUnfollowItemCmd userUnfollowItemCmd;

	@Autowired
	private ShareItemCmd shareItemCmd;

	@Autowired
	private ShareItemValidator shareItemValidator;

	@Autowired
	private UploadPhotosValidator uploadPhotosValidator;

	@Autowired
	private UserItemCmd<DeletePhotoRequest, ServiceResponse> deletePhotoCmd;

	@Autowired
	@Qualifier("republishItemCmd")
	private RepublishItemCmd republishItemCmd;
	
	@Autowired
	@Qualifier("republishItemV4Cmd")
	private RepublishItemV4Cmd republishItemV4Cmd;

	@Autowired
	@Qualifier("recommendedItemsCmdImpl")
	private RecommendedItemsCmdImpl recommendItemsCmd;
	
	@Autowired
	private InternationalizationUtil i18nUtil;

	@Autowired
    private ServiceValidator<DeletePhotoRequest> deletePhotoValidator;
	
	@Autowired
	private GetFacebookBusinessItemsCmd getActiveItemsCmd;
	
	@Autowired
	private SearchItemIdsCmd searchItemIdsCmd;
	
	@Autowired
	private RecommendedItemIdsCmd recommendedItemIdsCmd;
	

	@Override
	public CreateItemResponse create(CreateItemRequest request) throws ServiceException {
		createItemValidator.validate(request);
		CreateItemResponse response = createItemCmd.execute(request);
		return response;
	}

	@Override
	public SearchItemResponse search(SearchItemRequest request) {
		return searchItemCmd.execute(request);
	}

	@Override
	public ListUserItemsResponse list(ListUserItemsRequest request) {
		return listUserItemsCmd.execute(request);
	}

	@Override
	public GetItemResponse get(GetItemRequest request) {
		getItemValidator.validate(request);
		return getItemCmd.execute(request);
	}

	@Override
	public UpdateItemResponse update(UpdateItemRequest request) {
		updateItemValidator.validate(request);
		
		UpdateItemResponse response = new UpdateItemResponse();
		
		//update
		updateItemCmd.execute(request);
		
		//share
		if (request.getShareOnFacebook()) {
			try {
				shareItemCmd.execute(new ShareItemRequest(request.getItemId(), request.getToken(),
						Social.FACEBOOK));
			} catch (ServiceException e) {
				response.setFacebookPublishError(i18nUtil
						.internationalizeMessage(e.getMessage(),
								request.getLanguage()));
				LOGGER.error("Item share on Facebook failed", e);
			}
		}
		
		if (request.getShareOnTwitter()) {
			try {
				shareItemCmd.execute(new ShareItemRequest(request.getItemId(), request.getToken(),
						Social.TWITTER));
			} catch (ServiceException e) {
				response.setTwitterPublishError(i18nUtil
						.internationalizeMessage(e.getMessage(),
								request.getLanguage(),
								e.getLocalizableMsgArgs()));
				LOGGER.error("Item share on Twitter failed", e);
			}
		}
		
		response.setSuccess(true);
		
		return response;
		
	}

	@Override
	public Boolean remove(UserItemRequest request) throws ServiceException {
		removeItemCmd.execute(request);
		return true;
	}

	@Override
	public ServiceResponse uploadPhotos(UploadPhotosRequest request) {
		uploadPhotosValidator.validate(request);
		return uploadPhotosCmd.execute(request);
	}

	@Override
	public BuyItemDirectlyResponse buyDirectly(UserItemRequest request) {
		return buyItemDirectlyCmd.execute(request);
	}

	@Override
	public ListFollowedItemsResponse listFollowed(PaginatedRequest request) {
		return listFollowedItemsCmd.execute(request);
	}

	@Override
	public ServiceResponse report(ReportItemRequest request) {

		if (request.getItemId() == null || request.getItemId() < 0)
			throw createServiceException(ServiceExceptionCode.ITEM_NOT_FOUND);

		return userReportItemCmd.execute(request);
	}

	@Override
	public ServiceResponse unreport(UserItemRequest request) {

		if (request.getItemId() == null || request.getItemId() < 0)
			throw createServiceException(ServiceExceptionCode.ITEM_NOT_FOUND);

		return userUnreportItemCmd.execute(request);
	}

	@Override
	public ListFollowedItemsResponse listItemsInGroupsFollowedByUser(PaginatedRequest request) {
		return listItemsInGroupsFollowedByUserCmd.execute(request);
	}

	@Override
	public void follow(UserItemRequest request) {

		if (request.getItemId() == null || request.getItemId() < 0) {
			throw createServiceException(ServiceExceptionCode.ITEM_NOT_FOUND);
		}

		userFollowItemCmd.execute(request);
	}

	@Override
	public void unfollow(UserItemRequest request) {

		if (request.getItemId() == null || request.getItemId() < 0) {
			throw createServiceException(ServiceExceptionCode.ITEM_NOT_FOUND);
		}

		userUnfollowItemCmd.execute(request);
	}

	@Override
	public ServiceResponse share(ShareItemRequest request) {
		shareItemValidator.validate(request);
		shareItemCmd.execute(request);
		return ServiceResponse.SUCCESS;
	}

	@Override
	public RepublishItemResponse republishItem(RepublishItemRequest request) {
		return this.republish(request, republishItemCmd);
	}
	
	@Override
	public RepublishItemResponse republishItemV4(RepublishItemRequest request) {
		return this.republish(request, republishItemV4Cmd);
	}

	@Override
	public ServiceResponse deletePhoto(DeletePhotoRequest request) {
		deletePhotoValidator.validate(request);
		return deletePhotoCmd.execute(request);
	}

	@Override
	public ServiceResponse sold(UserItemRequest request) {
		return soldItemCmd.execute(request);
	}
	
	@Override
	public SearchItemResponse getRecommendedItems(SearchItemRequest request){
		return recommendItemsCmd.execute(request);
	}
	
	@Override
	public List<FacebookBusinessItem> getFacebookBusinessItems(ParkRequest request) {
		return getActiveItemsCmd.execute(request);
	}

	@Override
	public SearchItemIdsResponse searchItemIds(SearchItemRequest request) {
		return searchItemIdsCmd.execute(request);
	}

	@Override
	public SearchItemIdsResponse getRecommendedItemIds(SearchItemRequest request) {
		return recommendedItemIdsCmd.execute(request);
	}
	
	/**
	 * Republish item according to command version.
	 * @param request
	 * @param cmd
	 * @return RepublishItemResponse
	 */
	private RepublishItemResponse republish(RepublishItemRequest request, RepublishItemCmd cmd) {
		Assert.notNull(request, "The request cannot be null");
		RepublishItemResponse response = new RepublishItemResponse();
		response.setRepublishedItemId(cmd.execute(request).getItemId());
		return response;
	}
	
}
