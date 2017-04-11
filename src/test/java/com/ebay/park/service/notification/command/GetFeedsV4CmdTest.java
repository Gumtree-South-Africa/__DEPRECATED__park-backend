package com.ebay.park.service.notification.command;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import com.ebay.park.notification.NotificationAction;
import com.ebay.park.service.notification.dto.Feed;
import com.ebay.park.service.notification.dto.GetFeedsRequest;
import com.ebay.park.service.notification.dto.GetFeedsResponse;

public class GetFeedsV4CmdTest {
	
	private static final String LANG = "es";
    private static final String TOKEN = "token";
    private static final String USERNAME = "username";

    @InjectMocks
    @Spy
    private GetFeedsV4Cmd cmd = new GetFeedsV4Cmd();

    @Mock
    private GetFeedsCmd commonBehaviourCmd;

    @Before
    public void setUp() {
        initMocks(this);
    }
    @Test
    public void givenNewFeedThenReturnKnownFeedActions() {
    	//given
        GetFeedsRequest request = new GetFeedsRequest(USERNAME, TOKEN, LANG);
        
        Feed fbFriendUsingTheApp = new Feed();
        fbFriendUsingTheApp.setAction(NotificationAction.FB_FRIEND_USING_THE_APP.toString());
        
        //when
        List<Feed> feeds = Arrays.asList(fbFriendUsingTheApp);
        when(commonBehaviourCmd.execute(request)).thenReturn(new GetFeedsResponse(feeds));
        
        //then
        GetFeedsResponse response = cmd.execute(request);
        assertEquals(NotificationAction.FOLLOW_USER.toString(), response.getFeeds().get(0).getAction());
    }


}
