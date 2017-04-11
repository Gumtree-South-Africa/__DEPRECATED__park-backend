package com.ebay.park.service.moderation.rejection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doThrow;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.entity.Item;
import com.ebay.park.event.item.ItemNotificationEvent;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.moderation.dto.RejectItemRequest;

public class RejectDoNotSendItemExecutorTest {

    private static final long ITEM_ID = 127l;
    private static final int REASON = 0;
    private static final String MSG = "An exception is expected";

    @Spy
    @InjectMocks
    private RejectDoNotSendItemExecutor executor;
    
    @Mock
    private Item item;

    @Mock
    private ItemDao itemDao;

    @Before
    public void setUp() {
        
        initMocks(this);
    }
    
    @Test
    public void givenInvalidItemThenException() {
        RejectItemRequest request = new RejectItemRequest(ITEM_ID, REASON);
        when(itemDao.findOneDeletedOrNot(ITEM_ID)).thenReturn(null);
    
        try {
            executor.execute(request);
            fail(MSG);
        } catch (ServiceException e) {
            assertEquals(ServiceExceptionCode.ITEM_NOT_FOUND.getCode(), e.getCode());
        }
     }
    
    @Test
    public void givenAlreadyDeletedItemThenException() {
        RejectItemRequest request = new RejectItemRequest(ITEM_ID, REASON);
        when(itemDao.findOneDeletedOrNot(ITEM_ID)).thenReturn(item);
        when(item.isDeleted()).thenReturn(true);
        
        try {
            executor.execute(request);
            fail(MSG);
        } catch (ServiceException e) {
            assertEquals(ServiceExceptionCode.ITEM_ALREADY_DELETED.getCode(), e.getCode());
        }
     }
    
    @Test
    public void givenValidItemThenRejectItem() {
        RejectItemRequest request = new RejectItemRequest(ITEM_ID, REASON);
        when(itemDao.findOneDeletedOrNot(ITEM_ID)).thenReturn(item);
        when(item.isDeleted()).thenReturn(false);
        
        ItemNotificationEvent returnedItem = executor.execute(request);
        
        verify(item).delete();
        assertNotNull(returnedItem);
        assertEquals(item, returnedItem.getItem());
     }
    
    @Test
    public void givenValidItemWhenErrorThenRejectItem() {
        RejectItemRequest request = new RejectItemRequest(ITEM_ID, REASON);
        when(itemDao.findOneDeletedOrNot(ITEM_ID)).thenReturn(item);
        when(item.isDeleted()).thenReturn(false);
        doThrow(RuntimeException.class).when(itemDao).save(item);
        
        try {
            executor.execute(request);
            fail(MSG);
        } catch (ServiceException e) {
            assertEquals(ServiceExceptionCode.ITEM_DELETION_ERROR.getCode(), e.getCode());
        }
     }
}
