/*
 * Copyright eBay, 2014
 */
package com.ebay.park.service.asset;

import com.ebay.park.eps.EPSClient;
import com.ebay.park.service.asset.command.UploadCmd;
import com.ebay.park.service.asset.dto.AssetUploadRequest;
import com.ebay.park.service.asset.dto.AssetUploadResponse;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.session.dto.UserSessionCache;
import com.ebay.park.util.EPSUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author jpizarro
 * 
 */
public class UploadCmdTest {

	@InjectMocks
	UploadCmd uploadCmd;

	@Mock
	EPSClient epsClient;

	@Mock
	SessionService sessionService;
	
	@Mock
	private EPSUtils EPSUtils;

	@Before
	public void setUp(){
		initMocks(this);
	}
	
	@Test
	public void givenValidEntriesWhenExecutingThenUploadPictureSuccessfully() {
		// given
		String expectedUsername = "my-username";
		String expectedFileName = "file-name";
		String expectedCDNUrl = "http://theurl.com/1";
		String expectedToken = "my-token";
		AssetUploadRequest requestMock = mock(AssetUploadRequest.class);
		MultipartFile fileMock = mock(MultipartFile.class);
		UserSessionCache userSessionMock = mock(UserSessionCache.class);
		when(EPSUtils.getPrefix()).thenReturn("");

		when(fileMock.isEmpty()).thenReturn(false);
		when(requestMock.getName()).thenReturn(expectedFileName);
		when(requestMock.getFile()).thenReturn(fileMock);
		when(requestMock.getToken()).thenReturn(expectedToken);
		when(epsClient.publish(eq(expectedUsername + "/" + expectedFileName), eq(fileMock))).thenReturn(expectedCDNUrl);
		when(sessionService.getUserSession(expectedToken)).thenReturn(userSessionMock);
		when(userSessionMock.getUsername()).thenReturn(expectedUsername);

		// when
		AssetUploadResponse actualResponse = uploadCmd.execute(requestMock);

		// then
		assertNotNull(actualResponse);
		assertNotNull(actualResponse.getURL());
		assertEquals(expectedCDNUrl, actualResponse.getURL());
		assertEquals(expectedFileName, actualResponse.getName());
		verify(userSessionMock).getUsername();
	}

}
