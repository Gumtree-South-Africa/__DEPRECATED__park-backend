package com.ebay.park.service.admin.command;

import com.ebay.park.db.dao.UserAdminDao;
import com.ebay.park.db.entity.UserAdmin;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.moderation.dto.UserIdRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Unit Test for {@link RemoveModeratorCmdImpl}.
 * @author Julieta Salvadó
 */
public class RemoveModeratorCmdImplTest {
    private static final long USER_ID = 34;

    @InjectMocks
    @Spy
    private RemoveModeratorCmdImpl cmd;

    @Mock
    private UserAdminDao userAdminDao;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void givenInvalidModeratorWhenExecutingThenException() {
        when(userAdminDao.findById(USER_ID)).thenReturn(null);
        try {
            UserIdRequest request = new UserIdRequest();
            cmd.execute(request);
        } catch (ServiceException e) {
            assertEquals(ServiceExceptionCode.USER_NOT_FOUND.getCode(), e.getCode());
        }
    }

    @Test
    public void givenValidModeratorWhenExecutingThenRemoveModerator() {
        when(userAdminDao.findById(USER_ID)).thenReturn(mock(UserAdmin.class));
        UserIdRequest request = new UserIdRequest();
        request.setUserId(USER_ID);
        ServiceResponse response = cmd.execute(request);
        assertEquals(ServiceResponse.SUCCESS.getStatusCode(), response.getStatusCode());
    }

    @Test
    public void givenSavingErrorWhenExecutingThenException() {
        UserAdmin user = mock(UserAdmin.class);
        when(userAdminDao.findById(USER_ID)).thenReturn(user);
        UserIdRequest request = new UserIdRequest();
        request.setUserId(USER_ID);
        doThrow(Exception.class).when(userAdminDao).delete(user);

        try {
            cmd.execute(request);
        } catch (ServiceException e) {
            assertEquals(ServiceExceptionCode.REMOVE_MODERATOR_ERROR.getCode(), e.getCode());
        }
    }

}