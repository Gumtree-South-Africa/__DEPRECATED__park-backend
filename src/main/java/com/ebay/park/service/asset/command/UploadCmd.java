/*
 * Copyright eBay, 2014
 */
package com.ebay.park.service.asset.command;

import com.ebay.park.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ebay.park.eps.EPSClient;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.asset.dto.AssetUploadRequest;
import com.ebay.park.service.asset.dto.AssetUploadResponse;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.session.dto.UserSessionCache;
import com.ebay.park.util.EPSUtils;

/**
 * @author jpizarro
 * 
 */
@Component
public class UploadCmd implements ServiceCommand<AssetUploadRequest, AssetUploadResponse> {

	@Autowired
	private SessionService sessionService;
	@Autowired
	private EPSUtils EPSUtils;
	@Autowired
	EPSClient epsClient;

	private static Logger logger = LoggerFactory.getLogger(UploadCmd.class);

	@Override
	public AssetUploadResponse execute(AssetUploadRequest request) throws ServiceException {
		try {
			UserSessionCache userSession = sessionService.getUserSession(request.getToken());
			String fileName = createFileName(EPSUtils.getPrefix(), request, userSession);
            logger.info("Try to Upload Profile's Picture into EPS. Original Picture Name: {}", fileName);
			String url = epsClient.publish(fileName, request.getFile());
			return new AssetUploadResponse(request.getName(), url);
		} catch (Exception e){
			throw new RuntimeException(e);
		}
	}

	private String createFileName(String prefix, AssetUploadRequest request, UserSessionCache userSession) {
		return prefix + userSession.getUsername() + "/" + request.getName();
	}

}
