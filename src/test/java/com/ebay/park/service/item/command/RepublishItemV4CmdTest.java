package com.ebay.park.service.item.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.StatusDescription;
import com.ebay.park.db.entity.User;
import com.ebay.park.event.user.UserItemToFollowersEvent;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.item.ItemUtils;
import com.ebay.park.service.item.dto.RepublishItemRequest;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.session.dto.UserSessionCache;
/**
 * Test class for {@link RepublishItemV4Cmd}
 * @author scalderon
 *
 */
public class RepublishItemV4CmdTest {
	
	private static final Long ITEM_ID = 1L;
	private static final Double ITEM_PRICE = 20.0;
	private static final Long USER_ID = 1L;
	
	@InjectMocks
	private RepublishItemV4Cmd cmd = new RepublishItemV4Cmd();
	
	@Mock
	private ItemDao itemDao;
	
	@Mock
	private SessionService sessionService;
	
	@Mock
	private ItemUtils itemUtils;
	
	@Mock
	private Item item;
	
	@Mock
	private User user;
	
	@Mock
	private RepublishItemRequest request;
	
	@Before
	public void setUp() {
		initMocks(this);
		
		when(request.getItemId()).thenReturn(ITEM_ID);
		when(user.getUserId()).thenReturn(USER_ID);
//		when(user.getUsername()).thenReturn(USERNAME);

		UserSessionCache userSession = mock(UserSessionCache.class);
		when(userSession.getUserId()).thenReturn(USER_ID);
		when(sessionService.getUserSession(any(String.class))).thenReturn(userSession);

		when(user.getUserId()).thenReturn(USER_ID);
		when(item.getPublishedBy()).thenReturn(user);
		when(itemDao.findOne(any(Long.class))).thenReturn(item);
		when(item.getId()).thenReturn(ITEM_ID);
		when(item.getPrice()).thenReturn(ITEM_PRICE);
		when(itemDao.save(item)).thenReturn(item);
		
	}
	
	@Test
	public void givenASoldItemWhenRepublishThenSuccess() {
		//given
		Mockito.doCallRealMethod().when(item).republish();
		when(item.getStatus()).thenReturn(StatusDescription.SOLD);
		when(itemUtils.isAbleToRepublish(item)).thenReturn(true);
		
		//when
		UserItemToFollowersEvent event = cmd.execute(request);
		
		//then
		assertNotNull(event);
		assertNotNull(event.getItem());
		
		verify(item).republish();
		verify(itemDao).save(item);
	}
	
	@Test
	public void givenAnExpiredItemWhenRepublishThenSuccess() {
		//given
		Mockito.doCallRealMethod().when(item).republish();
		when(item.getStatus()).thenReturn(StatusDescription.EXPIRED);
		when(itemUtils.isAbleToRepublish(item)).thenReturn(true);
		
		//when
		UserItemToFollowersEvent event = cmd.execute(request);
		
		//then
		assertNotNull(event);
		assertNotNull(event.getItem());
		
		verify(item).republish();
		verify(itemDao).save(item);
	}
	
	@Test
	public void givenAnActiveItemWhenRepublishThenSuccess() {
		//given
		Mockito.doCallRealMethod().when(item).republish();
		when(item.getStatus()).thenReturn(StatusDescription.ACTIVE);
		when(itemUtils.isAbleToRepublish(item)).thenReturn(true);
		
		//when
		UserItemToFollowersEvent event = cmd.execute(request);
		
		//then
		assertNotNull(event);
		assertNotNull(event.getItem());
		
		verify(item).republish();
		verify(itemDao).save(item);
	}
	
	@Test
	public void givenAnImagePendingItemWhenRepublishThenFails() {
		//given
		Mockito.doCallRealMethod().when(item).republish();
		when(item.getStatus()).thenReturn(StatusDescription.IMAGE_PENDING);

		//when
		try {
			cmd.execute(request);
		} catch (ServiceException e) {
			assertEquals(ServiceExceptionCode.ITEM_NOT_SOLD_EXPIRED_OR_ACTIVE_ERROR.getCode(), e.getCode());
		}
		
		//then
		verify(item,never()).republish();
		verify(itemDao, never()).save(item);
	}
	
	@Test
	public void givenAPendingItemWhenRepublishThenFails() {
		//given
		Mockito.doCallRealMethod().when(item).republish();
		when(item.getStatus()).thenReturn(StatusDescription.PENDING);

		//when
		try {
			cmd.execute(request);
		} catch (ServiceException e) {
			assertEquals(ServiceExceptionCode.ITEM_NOT_SOLD_EXPIRED_OR_ACTIVE_ERROR.getCode(), e.getCode());
		}
		
		//then
		verify(item,never()).republish();
		verify(itemDao, never()).save(item);
	}
	
	@Test
	public void givenAnItemNotAbleToRepublishThenFails() {
		//given
		Mockito.doCallRealMethod().when(item).republish();
		when(item.getStatus()).thenReturn(StatusDescription.ACTIVE);
		when(itemUtils.isAbleToRepublish(item)).thenReturn(false);
		
		//when
		try {
			cmd.execute(request);
		} catch (ServiceException e) {
			assertEquals(ServiceExceptionCode.ITEM_NOT_ABLE_TO_REPUBLISH.getCode(), e.getCode());
		}
		
		//then
		verify(item,never()).republish();
		verify(itemDao, never()).save(item);
	}


}
