package com.ebay.park.service.asset.command;

import com.ebay.park.service.asset.dto.GetStaticTextRequest;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.session.dto.UserSessionCache;
import com.ebay.park.util.LanguageUtil;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.context.MessageSource;
import org.springframework.ui.velocity.VelocityEngineFactoryBean;
import org.springframework.ui.velocity.VelocityEngineUtils;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({GetStaticTextCmdTest.class, LanguageUtil.class, VelocityEngineUtils.class})
public class GetStaticTextCmdTest {

	@Spy
	@InjectMocks
	private GetStaticTextCmd cmd = new GetStaticTextCmd();
	
	@Mock
	private SessionService sessionService;
	
	@Mock
	private MessageSource messageSource;
	
	@Mock
	private VelocityEngineFactoryBean velocityEngineFactory;
	
	@SuppressWarnings("unchecked")
	//@Test //FIXME see how to fix it
	public void testExecuteShouldSucceed() throws VelocityException, IOException {
		PowerMockito.mockStatic(LanguageUtil.class);
		GetStaticTextRequest request = mock(GetStaticTextRequest.class);
		when(request.getToken()).thenReturn("token");
		UserSessionCache userSession = mock(UserSessionCache.class);
		when(sessionService.getUserSession("token")).thenReturn(userSession);
		when(userSession.getLang()).thenReturn("en");
		PowerMockito.mockStatic(LanguageUtil.class);
		when(LanguageUtil.getValidLanguage("en")).thenReturn("en");
		VelocityEngine velocityEngine = mock(VelocityEngine.class);
		when(velocityEngineFactory.createVelocityEngine()).thenReturn(velocityEngine);
		when(request.getTemplate()).thenReturn("template");
		PowerMockito.mockStatic(VelocityEngineUtils.class);
		when(VelocityEngineUtils.mergeTemplateIntoString(eq(velocityEngine), eq("template"), eq("UTF-8"), any(Map.class))).thenReturn("text");
		
		String result = cmd.execute(request);
		
		assertEquals("text", result);
		
		verify(request, atLeastOnce()).getToken();
		verify(sessionService).getUserSession("token");
		verify(userSession).getLang();
		PowerMockito.verifyStatic();
		LanguageUtil.getValidLanguage("en");
		verify(velocityEngineFactory).createVelocityEngine();
		verify(request).getTemplate();
		VelocityEngineUtils.mergeTemplateIntoString(eq(velocityEngine), eq("template"), eq("UTF-8"), any(Map.class));
	}
}
