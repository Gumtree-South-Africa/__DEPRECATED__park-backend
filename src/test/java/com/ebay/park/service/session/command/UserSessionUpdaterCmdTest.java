package com.ebay.park.service.session.command;

import com.ebay.park.db.dao.UserSessionDao;
import com.ebay.park.db.entity.UserSession;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.session.dto.UserSessionCache;
import com.ebay.park.service.session.dto.UserSessionUpdaterRequest;
import com.ebay.park.util.DataCommonUtil;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Date;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class UserSessionUpdaterCmdTest {

    private static final String USER_TOKEN                                    = "2234";

    private static final int YESTERDAY = -1;

    private static final int TODAY = 0;

    @InjectMocks
    private UserSessionUpdaterCmd cmd = new UserSessionUpdaterCmd();

    @Mock
    private UserSessionCache userSessionCache;

    @Mock
    private SessionService sessionService;

    @Mock
    private UserSessionDao userSessionDao;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void givenTheSameDayWhenExecutingThenNothing() {
        UserSessionUpdaterRequest request = mock(UserSessionUpdaterRequest.class); 
                new UserSessionUpdaterRequest(userSessionCache, USER_TOKEN);
        Date date = DataCommonUtil.addDays(DateTime.now().toDate(), TODAY);
        when(request.getUserSessionCache()).thenReturn(userSessionCache);
        when(userSessionCache.getLastActivityDay()).thenReturn(date);      
        
        cmd.execute(request);
        verify(request, times(1)).getUserSessionCache();
        verify(userSessionCache, times(1)).getLastActivityDay();
    }
    
    @Test
    public void givenDifferentDaysWhenExecutingThenUpdate() {
        UserSessionUpdaterRequest request = mock(UserSessionUpdaterRequest.class); 
        new UserSessionUpdaterRequest(userSessionCache, USER_TOKEN);
        UserSession userSession = mock(UserSession.class);
        
        Date date = DataCommonUtil.addDays(DateTime.now().toDate(), YESTERDAY);
        when(request.getUserSessionCache()).thenReturn(userSessionCache);
        when(userSessionCache.getLastActivityDay()).thenReturn(date);
        when(request.getToken()).thenReturn(USER_TOKEN);
        
        when(userSessionDao.findUserSessionByToken(USER_TOKEN)).thenReturn(userSession);
        
        cmd.execute(request);
        verify(request, times(1)).getUserSessionCache();
        verify(request, times(2)).getToken();
        verify(userSessionCache, times(1)).getLastActivityDay();
        verify(userSessionCache, times(1)).setLastActivityDay(Mockito.any());
        verify(sessionService, times(1)).saveUserSession(USER_TOKEN, userSessionCache);
        verify(userSessionDao, times(1)).findUserSessionByToken(USER_TOKEN);
        verify(userSession, times(1)).setLastSuccessfulLogin(Mockito.any());  
    }
}

