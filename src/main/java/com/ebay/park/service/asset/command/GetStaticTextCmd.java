/*
 * Copyright eBay, 2014
 */
package com.ebay.park.service.asset.command;

import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.asset.dto.GetStaticTextRequest;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.session.dto.UserSessionCache;
import com.ebay.park.util.LanguageUtil;
import org.apache.velocity.exception.VelocityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.ui.velocity.VelocityEngineFactoryBean;
import org.springframework.ui.velocity.VelocityEngineUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author marcos.lambolay
 * 
 */
@Component
public class GetStaticTextCmd implements ServiceCommand<GetStaticTextRequest, String> {

	@Autowired
	private SessionService sessionService;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired(required = true)
	private VelocityEngineFactoryBean velocityEngineFactory;
	
	@Override
	public String execute(GetStaticTextRequest request) throws ServiceException {
		UserSessionCache session = sessionService.getUserSession(request.getToken());

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("messages", messageSource); 
		model.put("locale", new Locale(LanguageUtil.getValidLanguage(session.getLang())));
		model.put("params", null);

		String text = null;

		try {
			text = VelocityEngineUtils.mergeTemplateIntoString(
				velocityEngineFactory.createVelocityEngine()
				, request.getTemplate(), "UTF-8", model);
		} catch (VelocityException | IOException e) {
			throw new RuntimeException(e);
		}
				
		return text;
	}
}
