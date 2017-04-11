package com.ebay.park.service.admin.command;

import com.ebay.park.db.dao.UserAdminDao;
import com.ebay.park.db.entity.UserAdmin;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.admin.dto.SmallUserAdmin;
import com.ebay.park.service.admin.dto.UpdateModeratorRequest;
import com.ebay.park.util.PasswdUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class UpdateModeratorCmdImplTest {

	private static final Long USR_ID = 1L;

	private static final String USR_NAME = "usrName";

	private static final String USR_EMAIL = "usrEmail";
    private static final String USR_PASS = "p4ss";
    private static final String USR_LASTNAME = "lastname";

    @InjectMocks
	private UpdateModeratorCmdImpl cmd = new UpdateModeratorCmdImpl();

	@Mock
	private UserAdminDao userAdminDao;

	@Mock
	private PasswdUtil passwdUtil;
	
	UpdateModeratorRequest request = new UpdateModeratorRequest();
	
	@Before
	public void setUp(){
		initMocks(this);
		UserAdmin moderator = new UserAdmin();
		when(userAdminDao.save(moderator)).thenReturn(moderator);
		when(userAdminDao.findById(USR_ID)).thenReturn(moderator);
		when(userAdminDao.findByUsername(USR_NAME)).thenReturn(null);
		when(userAdminDao.findByEmail(USR_EMAIL)).thenReturn(null);
		
	}
	
	@Test
	public void givenValidModeratorWhenExecutingThenUpdateModerator(){
		request.setEmail(USR_EMAIL);
		request.setLanguage("en");
		request.setUsername(USR_NAME);
		request.setId(USR_ID);
        request.setPassword(USR_PASS);
        request.setName(USR_NAME);
        request.setLastname(USR_LASTNAME);
		
		SmallUserAdmin response = cmd.execute(request);
		assertEquals(response.getEmail(), USR_EMAIL);
	}

    @Test
    public void givenInvalidModeratorWhenExecutingThenException() {
        when(userAdminDao.findById(USR_ID)).thenReturn(null);
        try {
            cmd.execute(request);
        } catch (ServiceException e) {
            assertEquals(ServiceExceptionCode.USER_NOT_FOUND.getCode(), e.getCode());
        }
    }

    @Test
    public void givenDuplicatedUsernameWhenExecutingThenException() {
        when(userAdminDao.findByUsername(USR_NAME)).thenReturn(mock(UserAdmin.class));
        request.setId(USR_ID);
        request.setUsername(USR_NAME);
        try {
            cmd.execute(request);
        } catch (ServiceException e) {
            assertEquals(ServiceExceptionCode.USERNAME_DUPLICATED.getCode(), e.getCode());
        }
    }

    @Test
    public void givenDuplicatedEMailWhenExecutingThenException() {
        when(userAdminDao.findByEmail(USR_EMAIL)).thenReturn(mock(UserAdmin.class));
        request.setId(USR_ID);
        request.setUsername(USR_NAME);
        request.setEmail(USR_EMAIL);
        try {
            cmd.execute(request);
        } catch (ServiceException e) {
            assertEquals(ServiceExceptionCode.EMAIL_DUPLICATED.getCode(), e.getCode());
        }
    }

    @Test
    public void givenSavingErrorWhenExecutingThenException() {
        doThrow(Exception.class).when(userAdminDao).save(any(UserAdmin.class));
        request.setEmail(USR_EMAIL);
        request.setLanguage("en");
        request.setUsername(USR_NAME);
        request.setId(USR_ID);
        request.setPassword(USR_PASS);
        request.setName(USR_NAME);
        request.setLastname(USR_LASTNAME);

        try {
            cmd.execute(request);
        } catch (ServiceException e) {
            assertEquals(ServiceExceptionCode.UPDATE_MODERATOR_ERROR.getCode(), e.getCode());
        }
    }
}
