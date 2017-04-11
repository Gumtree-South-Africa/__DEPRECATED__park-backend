package com.ebay.park.service.user.schedule;

import com.ebay.park.db.dao.UserSessionDao;
import com.ebay.park.push.swrve.SwrvePusher;
import org.apache.commons.collections.ListUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * @author gervasio.amy
 * @since 26/12/2016.
 */
public class UserEngagerJobTest {

    @InjectMocks
    private UserEngagerJob userEngagerJob;

    @Mock
    private UserSessionDao userSessionDao;

    @Mock
    private SwrvePusher swrvePusher;

    private static int DAYS = 3;
    private static String ANDROID_KEY = "android_key";
    private static String IOS_KEY = "ios_key";

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(userEngagerJob, "swrveApikeyAndroid", ANDROID_KEY);
        ReflectionTestUtils.setField(userEngagerJob, "swrveApikeyIos", IOS_KEY);
    }

    @Test
    public void givenNoUsersInTheQueryWhenExecuteJobNoCallsToSwrvePusherExpected() {
        when(userSessionDao.findSwrveIdsWithItemsWithoutContact(DAYS)).thenReturn(ListUtils.EMPTY_LIST);
        userEngagerJob.sendPushToUsers(DAYS);
        // no exception expected
        //no calls to swrvePusher expected
        verify(swrvePusher, never()).sendPush(anyString(), anyString());
    }

    @Test
    public void givenOnlyOneAndoirdUserInTheQueryWhenExecuteJobOnlyOneCallToSwrvePusherExpected() {
        List<Object[]> users = new ArrayList<Object[]>();
        users.add(createTestUser("sw123", 1L, "ANDROID"));
        when(userSessionDao.findSwrveIdsWithItemsWithoutContact(DAYS)).thenReturn(users);
        userEngagerJob.sendPushToUsers(DAYS);
        // no exception expected
        // only one call to swrvePusher expected
        verify(swrvePusher).sendPush(ANDROID_KEY, "sw123");
    }

    @Test
    public void givenOnlyOneIosUserInTheQueryWhenExecuteJobOnlyOneCallToSwrvePusherExpected() {
        List<Object[]> users = new ArrayList<Object[]>();
        users.add(createTestUser("sw123", 1L, "IOS"));
        when(userSessionDao.findSwrveIdsWithItemsWithoutContact(DAYS)).thenReturn(users);
        userEngagerJob.sendPushToUsers(DAYS);
        // no exception expected
        // only one call to swrvePusher expected
        verify(swrvePusher).sendPush(IOS_KEY, "sw123");
    }

    @Test
    public void givenOnlyOneUserWithWrongPaltformInTheQueryWhenExecuteJobNoCallsToSwrvePusherExpected() {
        List<Object[]> users = new ArrayList<Object[]>();
        users.add(createTestUser("sw123", 1L, "WRONG"));
        when(userSessionDao.findSwrveIdsWithItemsWithoutContact(DAYS)).thenReturn(users);
        userEngagerJob.sendPushToUsers(DAYS);
        verify(swrvePusher, never()).sendPush(anyString(), anyString());
    }

    @Test
    public void givenOneUserWithWrongPaltformAndOneOkInTheQueryWhenExecuteJobOnlyOneCallToSwrvePusherExpected() {
        List<Object[]> users = new ArrayList<Object[]>();
        users.add(createTestUser("sw123", 1L, "WRONG"));
        users.add(createTestUser("sw123", 1L, "IOS"));
        when(userSessionDao.findSwrveIdsWithItemsWithoutContact(DAYS)).thenReturn(users);
        userEngagerJob.sendPushToUsers(DAYS);
        verify(swrvePusher).sendPush(IOS_KEY, "sw123");
    }

    @Test
    public void givenOneUserWithWrongPaltformAndTwoOkInTheQueryWhenExecuteJobOnlyOneCallToSwrvePusherExpected() {
        List<Object[]> users = new ArrayList<Object[]>();
        users.add(createTestUser("sw123", 1L, "WRONG"));
        users.add(createTestUser("sw345", 2L, "IOS"));
        users.add(createTestUser("sw789", 3L, "ANDROID"));
        when(userSessionDao.findSwrveIdsWithItemsWithoutContact(DAYS)).thenReturn(users);
        userEngagerJob.sendPushToUsers(DAYS);
        verify(swrvePusher, times(2)).sendPush(anyString(), anyString());
    }


    private Object[] createTestUser(String swrveUserId, Long itemId, String platform) {
        Object[] user = {swrveUserId, itemId, platform };
        return user;
    }
}
