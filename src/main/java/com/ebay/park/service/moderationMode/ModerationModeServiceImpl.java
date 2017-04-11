package com.ebay.park.service.moderationMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.ServiceValidator;
import com.ebay.park.service.moderationMode.dto.ApplyFiltersForModerationModeRequest;
import com.ebay.park.service.moderationMode.dto.ApplyFiltersForModerationModeResponse;
import com.ebay.park.service.moderationMode.dto.GetItemInformationRequest;
import com.ebay.park.service.moderationMode.dto.GetItemInformationResponse;
import com.ebay.park.service.moderationMode.dto.GetUserItemsForModerationModeRequest;
import com.ebay.park.service.moderationMode.dto.GetUserItemsForModerationModeResponse;
import com.ebay.park.service.moderationMode.dto.UnlockUserRequest;
import com.ebay.park.service.moderationMode.dto.UpdateItemForModerationModeRequest;

@Component
public class ModerationModeServiceImpl implements ModerationModeService {

	@Autowired
	private ServiceCommand<GetUserItemsForModerationModeRequest,
	GetUserItemsForModerationModeResponse> getUserItemsForModerationModeCmd;
	
	@Autowired
	private ServiceCommand<UpdateItemForModerationModeRequest, ServiceResponse> updateItemForModerationModeCmd;
	
	@Autowired
	private ServiceCommand<ApplyFiltersForModerationModeRequest,
	ApplyFiltersForModerationModeResponse> applyFiltersForModerationModeCmd;
	
	@Autowired
	private ServiceCommand<GetItemInformationRequest, GetItemInformationResponse> getItemInformationCmd;
	
	@Autowired 
	private ServiceCommand<UnlockUserRequest, ServiceResponse> unlockUserCmd;
	
	@Autowired
	@Qualifier("applyFiltersForModerationModeValidator")
	private ServiceValidator<ApplyFiltersForModerationModeRequest> applyFiltersForModerationModeValidator;
	
	@Autowired
	private ServiceValidator<UpdateItemForModerationModeRequest> updateItemAttributesValidator;

	
	@Override
	public GetUserItemsForModerationModeResponse getUserItemsForModerationMode(
			GetUserItemsForModerationModeRequest request) {
		return getUserItemsForModerationModeCmd.execute(request);
	}

	@Override
	public ServiceResponse updateItemForModerationMode(
			UpdateItemForModerationModeRequest request) {
		updateItemAttributesValidator.validate(request);
		return updateItemForModerationModeCmd.execute(request);
	}

	@Override
	public ApplyFiltersForModerationModeResponse applyFiltersForModerationMode(
			ApplyFiltersForModerationModeRequest request) {
	    applyFiltersForModerationModeValidator.validate(request);
		return applyFiltersForModerationModeCmd.execute(request);
	}

	@Override
	public GetItemInformationResponse getItemInformation(
			GetItemInformationRequest getItemInformationRequest) {
		return getItemInformationCmd.execute(getItemInformationRequest);
	}

	@Override
	public ServiceResponse unlockItem(UnlockUserRequest request) {
		return unlockUserCmd.execute(request);
	}
	
}
