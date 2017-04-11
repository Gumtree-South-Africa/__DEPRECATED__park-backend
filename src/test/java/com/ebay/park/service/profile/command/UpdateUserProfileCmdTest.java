package com.ebay.park.service.profile.command;

import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.profile.dto.ProfilePictureRequest;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.session.dto.UserSessionCache;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Unit test for {@link UpdateUserProfileCmd}.
 * Created by jsalvado on 03/08/16.
 */
public class UpdateUserProfileCmdTest {
    private static final String INVALID_URL = "invalid .. url";
    private static final String VALID_URL = "http://www.validurl.com";
    private static final String AN_EXCEPTION_WAS_EXPECTED = "An exception was expected";
    private static final String INVALID_USERNAME = "invalid";
    private static final String VALID_USERNAME = "valid";
    private static final String TOKEN_1 = "token1";
    private static final String TOKEN_2 = "token2";

    @InjectMocks
    @Spy
    private UpdateUserProfileCmd cmd;

    @Mock
    private UserDao userDao;

    @Mock
    private SessionService sessionService;


    @Before
    public void setUp() {
        initMocks(this);
        when(userDao.findByUsername(VALID_USERNAME)).thenReturn(mock(User.class));
        when(userDao.findByUsername(INVALID_USERNAME)).thenReturn(null);
        UserSessionCache valid_session = mock(UserSessionCache.class);
        when(sessionService.getUserSession(TOKEN_2)).thenReturn(valid_session);
        when(valid_session.getUsername()).thenReturn(VALID_USERNAME);

        UserSessionCache invalid_session = mock(UserSessionCache.class);
        when(sessionService.getUserSession(TOKEN_1)).thenReturn(invalid_session);
        when(invalid_session.getUsername()).thenReturn(INVALID_USERNAME);
    }

    @Test
    public void givenInvalidURLWhenExecutingThenException() {
        ProfilePictureRequest request = new ProfilePictureRequest();
        request.setUrl(INVALID_URL);
        try {
            cmd.execute(request);
            fail(AN_EXCEPTION_WAS_EXPECTED);
        } catch (ServiceException e) {
            assertEquals(ServiceExceptionCode.INVALID_URL.getCode(), e.getCode());
        }
    }

    @Test
    public void givenInvalidUserWhenExecutingThenException() {
        ProfilePictureRequest request = new ProfilePictureRequest();
        request.setUrl(VALID_URL);
        request.setUsername(INVALID_USERNAME);

        try {
            cmd.execute(request);
            fail(AN_EXCEPTION_WAS_EXPECTED);
        } catch (ServiceException e) {
            assertEquals(ServiceExceptionCode.USER_NOT_FOUND.getCode(), e.getCode());
        }
    }

    @Test
    public void givenInvalidTokenWhenExecutingThenException() {
        ProfilePictureRequest request = new ProfilePictureRequest();
        request.setUrl(VALID_URL);
        request.setUsername(VALID_USERNAME);
        request.setToken(TOKEN_1);

        try {
            cmd.execute(request);
            fail(AN_EXCEPTION_WAS_EXPECTED);
        } catch (ServiceException e) {
            assertEquals(ServiceExceptionCode.USER_UNAUTHORIZED.getCode(), e.getCode());
        }
    }

    @Test
    public void givenValidValuesWhenExecutingThenE() {
        ProfilePictureRequest request = new ProfilePictureRequest();
        request.setUrl(VALID_URL);
        request.setUsername(VALID_USERNAME);
        request.setToken(TOKEN_2);

        cmd.execute(request);
    }

}