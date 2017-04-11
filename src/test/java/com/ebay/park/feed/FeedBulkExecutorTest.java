package com.ebay.park.feed;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import com.ebay.park.db.dao.FeedDao;
import com.ebay.park.db.entity.Feed;
import com.ebay.park.util.FeedUtils;

public class FeedBulkExecutorTest {

    private static final String MESSAGE = "some message";
    private static final long ID1 = 1l;
    private static final long ID2 = 2l;
    private static final long ID3 = 3l;
    private static final int MAX = 2;

    @InjectMocks
    FeedBulkExecutor executor; 

    @Mock
    private FeedUtils feedUtils;

    @Mock
    private FeedDao feedDao;

    @Before
    public void setUp(){
        initMocks(this);
        ReflectionTestUtils.setField(executor, "max", MAX);
    }

    @Test
    public void givenValidLongUserListWhenExecutingThenSendFeeds() {
        List<Long> userIds = Arrays.asList(ID1, ID2, ID3);
        Feed f1 = mock(Feed.class), f2 = mock(Feed.class), f3 = mock(Feed.class);
        List<Feed> feedList = new ArrayList<Feed>();
        feedList.add(f1);
        feedList.add(f2);
        feedList.add(f3);
        
        when(feedUtils.createFeedListForBulk(userIds, MESSAGE)).thenReturn(feedList);

        executor.execute(userIds, MESSAGE);

        assertEquals(feedList.size(), 0);
    }
    
    @Test
    public void givenValidShortUserListWhenExecutingThenSendFeeds2() {
        List<Long> userIds = Arrays.asList(ID1, ID2);
        Feed f1 = mock(Feed.class), f2 = mock(Feed.class), f3 = mock(Feed.class);
        List<Feed> feedList = new ArrayList<Feed>();
        feedList.add(f1);
        feedList.add(f2);
        feedList.add(f3);
        
        when(feedUtils.createFeedListForBulk(userIds, MESSAGE)).thenReturn(feedList);

        executor.execute(userIds, MESSAGE);

        assertEquals(feedList.size(), 0);
    }
    
    @Test
    public void givenEmptyUserListWhenExecutingThenDoNothing() {
        List<Long> userIds = new ArrayList<Long>();

        executor.execute(userIds, MESSAGE);

        verify(feedDao, never()).save(any(Feed.class));
    }
}
