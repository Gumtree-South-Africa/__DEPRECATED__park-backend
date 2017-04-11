package com.ebay.park.service.conversation.validator;

import static org.junit.Assert.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;

import com.ebay.park.db.entity.Conversation;
import com.ebay.park.db.entity.ConversationStatus;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.StatusDescription;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;

public class ConversationValidatorTest {

    private static final long BUYER_ID = 1l;
    private static final long SELLER_ID = 2l;
    private static final long ANOTHER_ID = 3l;

    @InjectMocks
    @Spy
    private ConversationValidator validator;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void givenDeletedItemWhenValidatingDeletedThenException() {
        Item item = mock(Item.class);
        when(item.isDeleted()).thenReturn(true);

        try {
            validator.isNotDeleted(item);
            fail();
        } catch (ServiceException e) {
            assertEquals(e.getCode(), ServiceExceptionCode.ITEM_ALREADY_DELETED.getCode());
        }
    }

    @Test
    public void givenNotDeletedItemWhenValidatingDeletedThenSucess() {
        Item item = mock(Item.class);
        when(item.isDeleted()).thenReturn(false);

        validator.isNotDeleted(item);
    }

    @Test
    public void givenActiveItemWhenValidatingActiveOrSoldThenSucess() {
        Item item = mock(Item.class);
        when(item.is(StatusDescription.ACTIVE)).thenReturn(true);

        validator.isActiveOrSold(item);
    }

    @Test
    public void givenSoldItemWhenValidatingActiveOrSoldThenSucess() {
        Item item = mock(Item.class);
        when(item.is(StatusDescription.ACTIVE)).thenReturn(false);
        when(item.is(StatusDescription.SOLD)).thenReturn(true);

        validator.isActiveOrSold(item);
    }

    @Test
    public void givenSoldItemWhenValidatingActiveOrSoldThenException() {
        Item item = mock(Item.class);
        when(item.is(StatusDescription.ACTIVE)).thenReturn(false);
        when(item.is(StatusDescription.SOLD)).thenReturn(false);
        try {
            validator.isActiveOrSold(item);
            fail();
        } catch (ServiceException e) {
            assertEquals(e.getCode(), ServiceExceptionCode.ITEM_NOT_ACTIVE_NOR_SOLD_ERROR.getCode());
        }
    }

    @Test
    public void givenAcceptedConversationWhenValidatingNotClosedConversationThenException() {
        Conversation conversation = mock(Conversation.class);
        when(conversation.is(ConversationStatus.ACCEPTED)).thenReturn(true);
        try {
            validator.isNotClosed(conversation);
            fail();
        } catch (ServiceException e) {
            assertEquals(e.getCode(), ServiceExceptionCode.ALREADY_ACCEPTED_CONVERSATION_ERROR.getCode());
        }
    }

    @Test
    public void givenCancelledConversationWhenValidatingNotClosedConversationThenException() {
        Conversation conversation = mock(Conversation.class);
        when(conversation.is(ConversationStatus.ACCEPTED)).thenReturn(false);
        when(conversation.is(ConversationStatus.CANCELLED)).thenReturn(true);
        try {
            validator.isNotClosed(conversation);
            fail();
        } catch (ServiceException e) {
            assertEquals(e.getCode(), ServiceExceptionCode.ALREADY_CANCELLED_CONVERSATION_ERROR.getCode());
        }
    }

    @Test
    public void givenNotAccepterNorCancelledConversationWhenValidatingNotClosedConversationThenSuccess() {
        Conversation conversation = mock(Conversation.class);
        when(conversation.is(ConversationStatus.ACCEPTED)).thenReturn(false);
        when(conversation.is(ConversationStatus.CANCELLED)).thenReturn(false);

        validator.isNotClosed(conversation);
    }

    @Test
    public void givenUnexistingConversationWhenValidatingExistenceThenException() {
        try {
            validator.validateFound(null);
            fail();
        } catch (ServiceException e) {
            assertEquals(e.getCode(), ServiceExceptionCode.CONVERSATION_NOT_FOUND_ERROR.getCode());
        }
    }
    
    @Test
    public void givenValidConversationWhenValidatingExistenceThenSucess() {
        Conversation conversation = mock(Conversation.class);
        validator.validateFound(conversation);
    }

    @Test
    public void givenBuyerWhenValidatingBuyerOrSellerThenReturnTrue() {
        User user = mock(User.class);
        when(user.getId()).thenReturn(BUYER_ID);
        Conversation conversation = mock(Conversation.class);
        when(conversation.getBuyer()).thenReturn(user);

        assertTrue(validator.verifyUserBuyerOrSeller(conversation, BUYER_ID));
    }

    @Test
    public void givenSellerWhenValidatingBuyerOrSellerThenReturnTrue() {
        User sellerUser = mock(User.class), buyerUser = mock(User.class);
        when(sellerUser.getId()).thenReturn(SELLER_ID);
        when(buyerUser.getId()).thenReturn(BUYER_ID);
        Conversation conversation = mock(Conversation.class);
        when(conversation.getSeller()).thenReturn(sellerUser);
        when(conversation.getBuyer()).thenReturn(buyerUser);

        assertTrue(validator.verifyUserBuyerOrSeller(conversation, SELLER_ID));
    }

    @Test
    public void givenNeitherSellerOrBuyerWhenValidatingBuyerOrSellerThenReturnFalse() {
        User sellerUser = mock(User.class), buyerUser = mock(User.class);
        when(sellerUser.getId()).thenReturn(SELLER_ID);
        when(buyerUser.getId()).thenReturn(BUYER_ID);
        Conversation conversation = mock(Conversation.class);
        when(conversation.getSeller()).thenReturn(sellerUser);
        when(conversation.getBuyer()).thenReturn(buyerUser);

        assertFalse(validator.verifyUserBuyerOrSeller(conversation, ANOTHER_ID));
    }
}
