package com.ebay.park.service.group.command;

import com.ebay.park.db.dao.GroupDao;
import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Group;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.StatusDescription;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.ListedResponse;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.group.dto.GetGroupItemsRequest;
import com.ebay.park.service.item.ItemUtils;
import com.ebay.park.service.item.dto.ItemSummary;
import com.ebay.park.service.item.dto.SearchItemResponse;
import com.ebay.park.util.InternationalizationUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class GetGroupItemsCmdTest {

	private static final Long GROUP_ID = 1L;

	private static final String USR_TOKEN = "usrtok";

	@InjectMocks
	private GetGroupItemsCmd getGroupItemsCmd = new GetGroupItemsCmd();
	
	@Mock
	GroupDao groupDao;
	@Mock
	UserDao userDao;
	@Mock
	ItemDao itemDao;
	@Mock
	private ItemUtils itemUtils;
	
	@Mock
	private InternationalizationUtil i18nUtil;
	
	private GetGroupItemsRequest request;
	private User user1;
	
	@Before
	public void setUp(){
		initMocks(this);
		
		request = new GetGroupItemsRequest(GROUP_ID, USR_TOKEN, "en", 0, 20);
		user1 = new User();
		Group group1 = new Group("group name", user1, "Group description");
		group1.setId(GROUP_ID);
		
		when(groupDao.findOne(GROUP_ID)).thenReturn(group1);
		when(userDao.findByToken(USR_TOKEN)).thenReturn(user1);
		
		ArrayList<Item> itemsList = new ArrayList<Item>();
		Item item1 = new Item("item1", 223D, "v1.0", false, false);
		item1.setPublishedBy(user1);
		Item item2 = new Item("item2", 113D, "v1.0", false, false);
		item2.setPublishedBy(user1);
		itemsList.add(item1);
		itemsList.add(item2);
		
		Page<Item> items = new PageImpl<Item>(itemsList);
		PageRequest pageReq = new PageRequest(0, 20);
		
		when(itemDao.listItemsFromGroup(group1.getId(), StatusDescription.ACTIVE, pageReq)).thenReturn(items);
		
		List<ItemSummary> itemSummaryList = Arrays.asList(
								ItemSummary.fromItem(item1),
								ItemSummary.fromItem(item2));
		when(i18nUtil.internationalizeItems(items.getContent(), request)).thenReturn(itemsList);
		
		when(itemUtils.convertToItemSummary(items.getContent(), /*user1, FIXME temporary*/ request.getLanguage())).thenReturn(itemSummaryList);
		
		Mockito.doNothing().when(i18nUtil).internationalizeListedResponse((ListedResponse)any(), (String)any(), (String)any());
		
		
	}
	
	@Test
	public void succesfullyGetItems(){
		SearchItemResponse response = getGroupItemsCmd.execute(request);
		assertEquals(2, response.getNumberOfElements());
	}
	
	@Test
	public void searchWithInvalidGroup(){
		request = new GetGroupItemsRequest(123L, USR_TOKEN, "en", 0, 20);
		try{
			getGroupItemsCmd.execute(request);
		} catch(ServiceException e) {
			assertEquals(e.getCode(), ServiceExceptionCode.INVALID_GROUP.getCode());
		}
	}
	
	@Test
	public void searchWithInvalidUsrToken(){
		request = new GetGroupItemsRequest(GROUP_ID, "invalidToken", "en", 0, 20);
		try{
			getGroupItemsCmd.execute(request);
		} catch(ServiceException e) {
			assertEquals(e.getCode(), ServiceExceptionCode.USER_UNAUTHORIZED.getCode());
		}
	}
	
}
