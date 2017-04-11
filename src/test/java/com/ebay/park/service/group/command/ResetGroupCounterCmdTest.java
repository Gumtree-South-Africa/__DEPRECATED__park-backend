package com.ebay.park.service.group.command;

import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.dao.UserFollowsGroupDao;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserFollowsGroup;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.group.dto.ResetGroupCounterRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Date;
import java.util.HashMap;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Unit test for {@link ResetGroupCounterCmd}.
 * @author Julieta Salvadó
 */
public class ResetGroupCounterCmdTest {

    private static final String TOKEN = "token";
    private static final long USER_ID = 1L;
    private static final long GROUP_ID = 2L;

    @InjectMocks
    private ResetGroupCounterCmd cmd;

    @Mock
    private UserDao userDao;

    @Mock
    private ResetGroupCounterRequest request;

    @Mock
    private UserFollowsGroupDao userFollowsGroupDao;

    @Before
    public void setUp() {
        initMocks(this);
        when(request.getToken()).thenReturn(TOKEN);
        when(request.getGroupId()).thenReturn(GROUP_ID);
    }

    @Test (expected = IllegalArgumentException.class)
    public void givenNullRequestWhenExecutingThenException() {
        cmd.execute(null);
    }

    @Test
    public void givenInvalidTokenWhenExecutingThenException() {
        when(userDao.findByToken(TOKEN)).thenReturn(null);

        try {
            cmd.execute(request);
            fail();
        } catch (ServiceException e) {
            assertEquals(e.getCode(), ServiceExceptionCode.USER_UNAUTHORIZED.getCode());
        }
    }

    @Test
    public void givenUserInGroupWhenExecutingThenResetCounterValue() {
        User user = mock(User.class);
        UserFollowsGroup userFollowsGroup = mock(UserFollowsGroup.class);
        when(user.getId()).thenReturn(USER_ID);
        when(userDao.findByToken(TOKEN)).thenReturn(user);
        when(userFollowsGroupDao.find(GROUP_ID, USER_ID)).thenReturn(userFollowsGroup);

        cmd.execute(request);

        verify(userFollowsGroup).setLastAccess(any(Date.class));
        verify(userFollowsGroupDao).save(userFollowsGroup);
    }

    @Test
    public void givenUserInGroupWhenExecutingThenSuccessfulResponse() {
        User user = mock(User.class);
        UserFollowsGroup userFollowsGroup = mock(UserFollowsGroup.class);
        when(user.getId()).thenReturn(USER_ID);
        when(userDao.findByToken(TOKEN)).thenReturn(user);
        when(userFollowsGroupDao.find(GROUP_ID, USER_ID)).thenReturn(userFollowsGroup);

        ServiceResponse response = cmd.execute(request);

        assertTrue(isSuccessful(response));
    }

    private boolean isSuccessful(ServiceResponse response) {
        return ((HashMap)response.getData()).get("success").equals("true");
    }

    @Test
    public void givenUserNotInGroupWhenExecutingThenDoNotSaveChanges() {
        User user = mock(User.class);
        when(user.getId()).thenReturn(USER_ID);
        when(userDao.findByToken(TOKEN)).thenReturn(user);
        when(userFollowsGroupDao.find(GROUP_ID, USER_ID)).thenReturn(null);

        cmd.execute(request);

        verify(userFollowsGroupDao, never()).save(any(UserFollowsGroup.class));
    }

    @Test
    public void givenUserNotInGroupWhenExecutingThenFailResponse() {
        User user = mock(User.class);
        when(user.getId()).thenReturn(USER_ID);
        when(userDao.findByToken(TOKEN)).thenReturn(user);
        when(userFollowsGroupDao.find(GROUP_ID, USER_ID)).thenReturn(null);

        ServiceResponse response = cmd.execute(request);

        assertFalse(isSuccessful(response));
    }
}