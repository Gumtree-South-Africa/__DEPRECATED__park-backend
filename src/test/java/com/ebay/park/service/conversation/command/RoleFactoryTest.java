package com.ebay.park.service.conversation.command;

import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.dao.RatingDao;
import com.ebay.park.db.entity.Conversation;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.ApplicationContext;

import javax.persistence.EntityManager;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class RoleFactoryTest {

	private static final Long BUYER_ID = 1L;

	private static final Long SELLER_ID = 2L;

	@InjectMocks
	private RoleFactory roleFactory = new RoleFactory();
	
	@Mock
	ApplicationContext context;
	
	@Mock
	private ItemDao itemDao;
	
	@Mock
	private RatingDao ratingDao;
	
	@Mock
	private EntityManager entityManager;
	
	@Mock
	private ChatHelper chatHelper;
	
	private User buyer;
	
	private User seller;
	
	private Conversation conversation;
	
	private Item item;
	
	@Before
	public void setUp(){
		initMocks(this);
		buyer = new User();
		buyer.setId(BUYER_ID);
		seller = new User();
		seller.setId(SELLER_ID);
		conversation = new Conversation();
		conversation.setSeller(seller);
		conversation.setBuyer(buyer);
		when(context.getBean(SellerRole.class)).thenReturn(new SellerRole());
		when(context.getBean(BuyerRole.class)).thenReturn(new BuyerRole());
		item = new Item("itName", 43D, "versPublish", false, false);
		item.setPublishedBy(seller);
		conversation.setItem(item);
	}
	
	@Test(expected=ServiceException.class)
	public void testCreateRoleWhenUserIsNotBuyerNorSeller(){
		Long invalidUserId = 3L;
		User user = new User();
		user.setId(invalidUserId);
		roleFactory.createRole(conversation, user.getId());
	}
	
	@Test
	public void testCreateRoleForSeller(){
		Role role = roleFactory.createRole(conversation, seller.getId());
		assertEquals(role.getClass(), SellerRole.class);
	}
	
	@Test
	public void testCreateRoleForBuyer(){
		Role role = roleFactory.createRole(conversation, buyer.getId());
		assertEquals(role.getClass(), BuyerRole.class);
	}
	
}
