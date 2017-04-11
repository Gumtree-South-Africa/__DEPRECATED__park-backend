package com.ebay.park.push.swrve;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.BDDMockito.*;

/**
 * @author gervasio.amy
 * @since 15/12/2016.
 */
public class SwrvePusherTest {

    @InjectMocks
    private SwrvePusher swrvePusher;

    @Mock
    private RestTemplate restTemplate;

    @Captor
    private ArgumentCaptor<MultiValueMap> mapCaptor;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenSwrvePushKeyIsNullWhenSendPushThenIAE() {
        swrvePusher.sendPush(null, "someUserId", "some msg");
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenSwrveUserIdIsNullWhenSendPushThenIAE() {
        swrvePusher.sendPush("1234", null, null);
    }

    @Test
    public void givenHappyPathWhenSendPushThenNoExceptionIsExpectedAndParametersAreWellSent() {
        String pushKey = "123";
        String swrveUserId = "userX";
        SwrvePushResponse successResponse = new SwrvePushResponse();
        successResponse.setCode(200);
        when(restTemplate.postForObject(eq(SwrvePusher.swrveUrl), any(MultiValueMap.class), eq(SwrvePushResponse.class))).thenReturn
                (successResponse);
        swrvePusher.sendPush(pushKey, swrveUserId, null);

        verify(restTemplate).postForObject(eq(SwrvePusher.swrveUrl),  mapCaptor.capture(), eq(SwrvePushResponse.class));

        Assert.assertEquals(pushKey, mapCaptor.getValue().getFirst("push_key"));
        Assert.assertEquals(swrveUserId, mapCaptor.getValue().getFirst("user"));
    }

    @Test
    public void givenPostCallFailsWhenSendPushThenNullIsExpected() {
        String pushKey = "123";
        String swrveUserId = "userX";
        SwrvePushResponse successResponse = new SwrvePushResponse();
        successResponse.setCode(200);
        when(restTemplate.postForObject(eq(SwrvePusher.swrveUrl), any(MultiValueMap.class), eq(SwrvePushResponse.class))).thenThrow
                (HttpClientErrorException.class);

        SwrvePushResponse response = swrvePusher.sendPush(pushKey, swrveUserId, null);

        Assert.assertNull(response);
    }

}


