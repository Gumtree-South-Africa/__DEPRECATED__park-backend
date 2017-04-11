package com.ebay.park.service.group.command;

import com.ebay.park.db.dao.GroupDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Group;
import com.ebay.park.db.entity.User;
import com.ebay.park.eps.EPSClient;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.group.dto.UploadGroupPhotoRequest;
import com.ebay.park.util.EPSUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class UploadGroupPhotoCmdTest {

	private static final Long GROUP_ID = 1L;

	private static final String USR_TOKEN = "usrToken";

	@InjectMocks
	private UploadGroupPhotoCmd uploadGroupPhotoCmd = new UploadGroupPhotoCmd();
	
	@Mock
	private GroupDao groupDao;
	@Mock
	private UserDao userDao;
	@Mock
	private EPSUtils EPSUtils;
	@Mock
	EPSClient epsClient;

	UploadGroupPhotoRequest request;
	User user1;

	@Before
	public void setUp(){
		initMocks(this);
		request = new UploadGroupPhotoRequest();
		request.setGroupId(GROUP_ID);
		request.setLanguage("en");
		request.setToken(USR_TOKEN);
		request.setPhoto(new MockMultipartFile("mockfile", new byte[]{}));
		user1 = new User();
		Group group1 = new Group("group name", user1, "Group description");
		group1.setId(GROUP_ID);
		
		when(groupDao.findOne(GROUP_ID)).thenReturn(group1);
		when(userDao.findByToken(USR_TOKEN)).thenReturn(user1);
		when(epsClient.publish((String) any(), (MultipartFile) any())).thenReturn("urlPicture");
		when(EPSUtils.getPrefix()).thenReturn("Prfx");
	}
	
	@Test
	public void uploadGroupPhotoSuccesfully(){
		uploadGroupPhotoCmd.execute(request);
	}
	
	@Test
	public void uploadWithInvalidGroup(){
		request.setGroupId(222L);
		try{
			uploadGroupPhotoCmd.execute(request);
		} catch(ServiceException e) {
			assertEquals(e.getCode(), ServiceExceptionCode.INVALID_GROUP.getCode());
		}
	}
	
	@Test
	public void uploadWithInvalidUsrToken(){
		request.setToken("invalidToken");
		try{
			uploadGroupPhotoCmd.execute(request);
		} catch(ServiceException e) {
			assertEquals(e.getCode(), ServiceExceptionCode.USER_UNAUTHORIZED.getCode());
		}
	}
	
}
