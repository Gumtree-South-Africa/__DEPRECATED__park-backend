package com.ebay.park.service.user;

import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Unit test for {@link UserServiceHelper}.
 * @author Julieta Salvadó
 */
public class UserServiceHelperTest {

    private static final String AN_EXCEPTION_WAS_EXPECTED = "An exception was expected";
    private static final String TOKEN = "token";
    private static final String EMAIL = "email";
    private static final String UPPERCASE_EMAIL = "EMAIL";

    @InjectMocks
    private UserServiceHelper helper;

    @Mock
    private UserDao userDao;

    @Mock
    private User user;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void givenNullTokenWhenSearchingUserByTokenThenException() {
        try {
            helper.findUserByToken(null);
            fail(AN_EXCEPTION_WAS_EXPECTED);
        } catch (ServiceException e) {
            assertThat(e.getCode(), is(ServiceExceptionCode.USER_NOT_FOUND.getCode()));
        }
    }

    @Test
    public void givenInvalidTokenWhenSearchingUserByTokenThenException() {
        when(userDao.findByToken(TOKEN)).thenReturn(null);
        try {
            helper.findUserByToken(TOKEN);
            fail(AN_EXCEPTION_WAS_EXPECTED);
        } catch (ServiceException e) {
            assertThat(e.getCode(), is(ServiceExceptionCode.USER_NOT_FOUND.getCode()));
        }
    }

    @Test
    public void givenValidTokenWhenSearchingUserByTokenThenReturnUser() {
        when(userDao.findByToken(TOKEN)).thenReturn(user);
        User response = helper.findUserByToken(TOKEN);
        assertThat("The user with the token must be returned", user, is(response));
    }

    @Test
    public void givenNullTokenWhenSearchingAuthorizedUserByTokenThenException() {
        try {
            helper.findAuthorizedUserByToken(null);
            fail(AN_EXCEPTION_WAS_EXPECTED);
        } catch (ServiceException e) {
            assertThat(e.getCode(), is(ServiceExceptionCode.USER_UNAUTHORIZED.getCode()));
        }
    }

    @Test
    public void givenInvalidTokenWhenSearchingAuthorizedUserByTokenThenException() {
        when(userDao.findByToken(TOKEN)).thenReturn(null);
        try {
            helper.findAuthorizedUserByToken(TOKEN);
            fail(AN_EXCEPTION_WAS_EXPECTED);
        } catch (ServiceException e) {
            assertThat(e.getCode(), is(ServiceExceptionCode.USER_UNAUTHORIZED.getCode()));
        }
    }

    @Test
    public void givenValidTokenWhenSearchingAuthorizedUserByTokenThenReturnUser() {
        when(userDao.findByToken(TOKEN)).thenReturn(user);
        User response = helper.findAuthorizedUserByToken(TOKEN);
        assertThat("The user with the token must be returned", user, is(response));
    }


    @Test(expected = IllegalArgumentException.class)
    public void givenNullUserWhenDeleletingUserEmailThenException() {
        helper.deleteUserEmail(null);
    }

    @Test
    public void givenValidUserWhenDeleletingUserEmailThenDeleteEmail() {
        when(user.getEmail()).thenReturn(EMAIL);

        helper.deleteUserEmail(user);

        verify(user).setEmail(null);
    }

    @Test
    public void givenNullEmailWhenSettingUserEmailThenException() {
        try {
            helper.setUserEmail(user, null);
            fail(AN_EXCEPTION_WAS_EXPECTED);
        } catch (ServiceException e) {
            assertThat(e.getCode(), is(ServiceExceptionCode.EMAIL_USER_EMPTY_EMAIL.getCode()));
        }
    }

    @Test
    public void givenUppercaseEmailWhenSettingUserEmailThenSetLowercaseEmail() {
        helper.setUserEmail(user, UPPERCASE_EMAIL);
        verify(user).setEmail(UPPERCASE_EMAIL.toLowerCase());
    }

    @Test
    public void givenValidEmailWhenSettingUserEmailThenSetEmail() {
        helper.setUserEmail(user, EMAIL);
        verify(user).setEmail(EMAIL.toLowerCase());
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenNullUserWhenSettingUserEmailThenException() {
        helper.setUserEmail(null, EMAIL);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenNullUserAndEmailWhenSettingUserEmailThenException() {
        helper.setUserEmail(null, null);
    }

    @Test
    public void givenInvalidUserWhenSearchingUserByTokenThenException() {
        when(userDao.findByToken(TOKEN)).thenReturn(null);
        try {
            helper.findUserByToken(TOKEN);
            fail(AN_EXCEPTION_WAS_EXPECTED);
        } catch (ServiceException e) {
            assertThat(e.getCode(), is(ServiceExceptionCode.USER_NOT_FOUND.getCode()));
        }
    }

}