package com.ebay.park.service.notification.command;

import com.ebay.park.db.entity.Feed;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.notification.FeedServiceHelper;
import com.ebay.park.service.notification.dto.MarkAsReadRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Unit test {@link MarkAsReadCmd}
 * @author Julieta Salvadó
 */
public class MarkAsReadCmdTest {
    private static final long INVALID_FEED_ID = 1L;
    private static final long VALID_FEED_ID = 2L;
    @InjectMocks
    private MarkAsReadCmd cmd;

    @Mock
    private FeedServiceHelper helper;

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenNullRequestWhenExecutingThenException() {
        cmd.execute(null);
    }

    @Test
    public void givenValidFeedIdWhenExecutingThenMarkAsReas() {
        MarkAsReadRequest request = new MarkAsReadRequest("token", "lang", VALID_FEED_ID);

        cmd.execute(request);

        verify(helper).markAsRead(any(Feed.class));
    }
}