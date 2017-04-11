package com.ebay.park.service.moderation.command;

import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.entity.Conversation;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.User;
import com.ebay.park.email.Email;
import com.ebay.park.email.MailSender;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.moderation.dto.ContactPublisherRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author giriarte
 *
 */
public class ContactPublisherCmdImplTest {

	private static final Long ITEM_ID = 1L;

	@InjectMocks
	private ContactPublisherCmdImpl contactPublisherCmdImpl = new ContactPublisherCmdImpl();
	
	@Mock
	private ItemDao itemDao;

	@Mock
	private MailSender mailSender;
	
	private Item item;
	
	ContactPublisherRequest request = new ContactPublisherRequest();
	
	@Before
	public void setUp(){
		initMocks(this);
		item = new Item("itName", 345D, "v1.0", false,false);
		User publisher = new User();
		publisher.setEmail("email@mail.com");
		
		request.setSubject("theSubject");
		request.setBody("theBody");
		request.setLanguage("en");
		request.setItemId(ITEM_ID);
		
		item.setPublishedBy(publisher);	
		item.setOpenConversations(new ArrayList<Conversation>());
		
		when(itemDao.findOne(ITEM_ID)).thenReturn(item);
		Mockito.doNothing().when(mailSender).sendAsync((Email)any());
	}
	
	@Test
	public void contactSuccesfully(){
		assertEquals(ServiceResponse.SUCCESS, contactPublisherCmdImpl.execute(request));
	}
	
	@Test
	public void contactItemNotFound(){
		request.setItemId(111L);
		try{
			contactPublisherCmdImpl.execute(request);
		} catch(ServiceException e) {
			assertEquals(ServiceExceptionCode.ITEM_NOT_FOUND.getCode(), e.getCode());
			return;
		}
		fail();
	}

	@Test
	public void publisherEmptyMail(){
		item.getPublishedBy().setEmail(null);
		try{
			contactPublisherCmdImpl.execute(request);
		} catch(ServiceException e) {
			assertEquals(ServiceExceptionCode.EMAIL_PUBLISHER_EMPTY_EMAIL.getCode(), e.getCode());
			return;
		}
		fail();
	}
	
	@Test
	public void mailSendExceptionCatch(){
		Mockito.doThrow(ServiceException.class).when(mailSender).sendAsync((Email)any());
		try{
			contactPublisherCmdImpl.execute(request);
		} catch(ServiceException e) {
			assertEquals(ServiceExceptionCode.EMAIL_SEND_ERROR.getCode(), e.getCode());
			return;
		}
		fail();
	}
}
