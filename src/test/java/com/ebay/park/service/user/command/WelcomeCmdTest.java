package com.ebay.park.service.user.command;

import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Access;
import com.ebay.park.db.entity.User;
import com.ebay.park.email.PasswordEmailSender;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.user.dto.EmailRequest;
import com.ebay.park.util.Password;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Unit test for {@link WelcomeCmd}.
 * @author Julieta Salvadó
 */
public class WelcomeCmdTest {

    private static final String EMAIL = "john@globant.com";
    private static final String AN_EXCEPTION_WAS_EXPECTED = "An exception was expected";
    private static final String PASSWORD = "password";

    @InjectMocks
    private WelcomeCmd cmd;

    @Mock
    private EmailRequest request;

    @Mock
    private UserDao userDao;

    @Mock
    private User user;

    @Mock
    private Password password;

    @Mock
    @Qualifier("welcomePasswordEmailSender")
    private PasswordEmailSender sender;

    @Mock
    ApplicationContext context;

    @Mock
    private Access access;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test (expected = IllegalArgumentException.class)
    public void givenNullRequestWhenExecutingThenException() {
        cmd.execute(null);
    }

    @Test
    public void givenInvalidUserWhenExecutingThenException() {
        when(userDao.findByEmail(EMAIL)).thenReturn(null);
        try {
            cmd.execute(request);
            fail(AN_EXCEPTION_WAS_EXPECTED);
        } catch (ServiceException e) {
            assertThat("Invalid user", e.getCode(), is(ServiceExceptionCode.USER_NOT_FOUND.getCode()));
        }
    }

    @Test
    public void givenValidValuesWhenExecutingThenSendEmail() {
        when(request.getEmail()).thenReturn(EMAIL);
        when(userDao.findByEmail(EMAIL)).thenReturn(user);
        when(user.getAccess()).thenReturn(access);
        when(password.getSimplePassword()).thenReturn(PASSWORD);
        when(context.getBean(anyString())).thenReturn(sender);
        cmd.execute(request);
        verify(sender).sendEmail(any(User.class), anyString());
    }
}