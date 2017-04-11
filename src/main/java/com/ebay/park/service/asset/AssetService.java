/**
 * 
 */
package com.ebay.park.service.asset;

import com.ebay.park.service.ServiceException;
import com.ebay.park.service.asset.dto.*;

/**
 * @author jpizarro
 * 
 */
public interface AssetService {

	/**
	 * Upload an asset to the asset repository.
	 * 
	 * @param request Asset upload request
	 * @return response Instance of AssetUploadResponse
	 * @throws ServiceException
	 */
	public AssetUploadResponse upload(AssetUploadRequest request) throws ServiceException;

	public String listTerms(GetStaticTextRequest request) throws ServiceException;
	public String listCommunityRules(GetStaticTextRequest request) throws ServiceException;
	public String listLegalDisclosures(GetStaticTextRequest request) throws ServiceException;
	public GetTutorialResponse getTutorial(GetTutorialRequest request) throws ServiceException;

}
