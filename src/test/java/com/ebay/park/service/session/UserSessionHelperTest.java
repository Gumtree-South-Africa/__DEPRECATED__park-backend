package com.ebay.park.service.session;

import com.ebay.park.db.dao.UserSessionDao;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserSession;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.session.command.CreateUserSessionCmd;
import com.ebay.park.service.user.UserServiceHelper;
import com.ebay.park.service.user.dto.signin.SignInRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Unit test for {@link UserSessionHelper}.
 * @author Julieta Salvadó
 */
public class UserSessionHelperTest {
    private static final String TOKEN = "token";
    @InjectMocks
    private UserSessionHelper helper;

    @Mock
    private SignInRequest request;

    @Mock
    private User user;

    @Mock
    private CreateUserSessionCmd createUserSessionCmd;

    @Mock
    private UserSession userSession;

    @Mock
    private UserServiceHelper userServiceHelper;

    @Mock
    private SessionService sessionService;
    
    @Mock 
    private UserSessionDao userSessionDao;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test (expected = IllegalArgumentException.class)
    public void givenNullUserWhenCreatingSessionThenException() {
        helper.createSession(null, request);
    }

    @Test (expected = IllegalArgumentException.class)
    public void givenNullRequestWhenCreatingSessionThenException() {
        helper.createSession(user, null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void givenNullRequestAndUserWhenCreatingSessionThenException() {
        helper.createSession(null, null);
    }

    @Test
    public void givenValidEntriesWhenCreatingSessionThenCreateSession() {
        when(createUserSessionCmd.execute(request)).thenReturn(userSession);
        when(userSession.getToken()).thenReturn(TOKEN);
        String response = helper.createSession(user, request);
        assertNotNull(response);
        verify(userServiceHelper).saveUser(user);
        verify(sessionService).createUserSessionCache(userSession);
    }

    @Test
    public void givenValidEntriesWhenExceptionCreatingSessionThenException() {
        doThrow(IOException.class).when(createUserSessionCmd).execute(request);
        try {
            helper.createSession(user, request);
            fail();
        } catch (ServiceException e) {
            assertThat(e.getCode(), is(ServiceExceptionCode.IO_ERROR.getCode()));
        }
    }
}