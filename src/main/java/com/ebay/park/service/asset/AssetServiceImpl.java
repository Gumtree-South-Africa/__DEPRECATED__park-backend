/*
 * Copyright eBay, 2014
 */
package com.ebay.park.service.asset;

import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceValidator;
import com.ebay.park.service.asset.command.GetStaticTextCmd;
import com.ebay.park.service.asset.command.GetTutorialCmd;
import com.ebay.park.service.asset.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author jpizarro
 * 
 */
@Service
public class AssetServiceImpl implements AssetService {

	@Autowired
	ServiceCommand<AssetUploadRequest, AssetUploadResponse> uploadCmd;
	
	@Autowired
	ServiceValidator<AssetUploadRequest> uploadRequestValidator;
	
	@Autowired
	private GetStaticTextCmd getStaticTextCmd;
	
	@Autowired
	private GetTutorialCmd getTutorialCmd;
	
	@Value("${legalDisclosures.template}")
	private String legalDisclosuresTemplate;
	
	@Value("${termsAndConditions.template}")
	private String termsAndConditionsTemplate;
	
	@Value("${communityRules.template}")
	private String communityRulesTemplate;
	
	@Override
	public AssetUploadResponse upload(AssetUploadRequest request) throws ServiceException {
		uploadRequestValidator.validate(request);
		return uploadCmd.execute(request);
	}

	@Override
	public String listTerms(GetStaticTextRequest request) throws ServiceException {
		request.setTemplate(termsAndConditionsTemplate);
		return getStaticTextCmd.execute(request);
	}

	@Override
	public String listCommunityRules(GetStaticTextRequest request) throws ServiceException {
		request.setTemplate(communityRulesTemplate);
		return getStaticTextCmd.execute(request);
	}

	@Override
	public String listLegalDisclosures(GetStaticTextRequest request) throws ServiceException {
		request.setTemplate(legalDisclosuresTemplate);
		return getStaticTextCmd.execute(request);
	}

	@Override
	public GetTutorialResponse getTutorial(GetTutorialRequest request) throws ServiceException {
		return getTutorialCmd.execute(request); 		
	}
}
