package com.ebay.park.service.support.command;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.IOException;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.ebay.park.email.Email;
import com.ebay.park.email.MailSender;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.session.dto.UserSessionCache;
import com.ebay.park.service.support.dto.SendUserFeedbackRequest;

public class SendUserFeedbackCmdTest {

	@InjectMocks
	private SendUserFeedbackCmd cmd = new SendUserFeedbackCmd();	
	
	@Mock
	private MailSender mailSender;
	
	private String supportEmailAddress = "supportEmailAddress";
	private String parkEmailAddress = "parkEmailAddress";
	
	@Mock
	private SessionService sessionService;
	
	@Mock
	private SendUserFeedbackRequest request;

	private static final String VERSION_FILE = "version.properties";
	private static final String VERSION_PROPERTY = "version";

    private static final String DEFAULT_VERSION = "rel-1.5.7";
	
	@Before
	public void setUp() {
		initMocks(this);

		Whitebox.setInternalState(cmd, "parkEmailAddress", parkEmailAddress);
		Whitebox.setInternalState(cmd, "supportEmailAddress", supportEmailAddress);
	}
	
	@Test
	public void givenValidValuesWhenExecutingThenSend() {
	    Properties properties = mock(Properties.class);
        when(properties.getProperty("version")).thenReturn(DEFAULT_VERSION);
        cmd.setProperties(properties);
		when(request.getToken()).thenReturn("token");
		UserSessionCache session = mock(UserSessionCache.class);
		when(sessionService.getUserSession("token")).thenReturn(session);
		
		when(request.getMessage()).thenReturn("message");
		when(request.getDeviceModel()).thenReturn("deviceModel");
		when(request.getAppVersion()).thenReturn("appVersion");
		when(request.getCountryCode()).thenReturn("countryCode");
		when(session.getEmail()).thenReturn("email");
		when(session.getUsername()).thenReturn("username");
		
		AnswerEmail resultEmail = new AnswerEmail();
		
		doAnswer(resultEmail).when(mailSender).sendAsync(any(Email.class));
		
		cmd.execute(request);
		
		assertEquals(getExpectedEmail(DEFAULT_VERSION), resultEmail.resultEmail.getRawBody() );
		
		verify(request, atLeastOnce()).getToken();
		verify(sessionService).getUserSession("token");
		verify(request).getMessage();
		verify(request).getDeviceModel();
		verify(request).getAppVersion();
		verify(request).getCountryCode();
		verify(session).getEmail();
		verify(session).getUsername();
		verify(mailSender).sendAsync(any(Email.class));
		
	}

	private String getExpectedEmail(String version) {
	    return "message: message;\ndeviceModel: deviceModel;\nappVersion: appVersion;\ncountryCode: countryCode;\n"
	            +"buildVersion: " + version + ";\nuserEmail: email;\nuserName: username;\n";
    }

    @Test
    public void givenNullPropertiesWhenExecutingThenCreatePropertiesAndSend() throws IOException {
        Properties properties = new Properties();
        properties.load(this.getClass().getClassLoader().getResourceAsStream(VERSION_FILE));
        String currentVersion = properties.getProperty(VERSION_PROPERTY);

        when(request.getToken()).thenReturn("token");
        UserSessionCache session = mock(UserSessionCache.class);
        when(sessionService.getUserSession("token")).thenReturn(session);
        
        when(request.getMessage()).thenReturn("message");
        when(request.getDeviceModel()).thenReturn("deviceModel");
        when(request.getAppVersion()).thenReturn("appVersion");
        when(request.getCountryCode()).thenReturn("countryCode");
        when(session.getEmail()).thenReturn("email");
        when(session.getUsername()).thenReturn("username");
        
        AnswerEmail resultEmail = new AnswerEmail();
        
        doAnswer(resultEmail).when(mailSender).sendAsync(any(Email.class));
        
        cmd.execute(request);
        
        assertEquals(getExpectedEmail(currentVersion), resultEmail.resultEmail.getRawBody() );        
    }

	private static class AnswerEmail implements Answer<Void> {
		public Email resultEmail;
		@Override
		public Void answer(InvocationOnMock invocation) throws Throwable {
			Object[] arg = invocation.getArguments();
			resultEmail = (Email) arg[0];
			return null;
		}
	}

}
