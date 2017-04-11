package com.ebay.park.service.group.command;

import com.ebay.park.db.dao.GroupDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Group;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.ListedResponse;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.group.dto.GetGroupSubscribersRequest;
import com.ebay.park.service.social.dto.SearchUserResponse;
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

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class GetGroupSubscribersCmdTest {
	private static final String USR_TOKEN = "usrtoken";
	private static final Long GROUP_ID = 1L;

	@InjectMocks
	private GetGroupSubscribersCmd getGroupSubscribersCmd = new GetGroupSubscribersCmd();
	
	@Mock
	GroupDao groupDao;
	@Mock
	UserDao userDao;
	@Mock
	private InternationalizationUtil i18nUtil;
	
	private GetGroupSubscribersRequest request;
	private User user1;
	
	@Before
	public void setUp(){
		initMocks(this);
		
		request = new GetGroupSubscribersRequest(GROUP_ID, USR_TOKEN, "en", 0, 20);
		user1 = new User();
		Group group1 = new Group("group name", user1, "Group description");
		group1.setId(GROUP_ID);
		
		when(groupDao.findOne(GROUP_ID)).thenReturn(group1);
		when(userDao.findByToken(USR_TOKEN)).thenReturn(user1);
		
		ArrayList<User> usersList = new ArrayList<User>();
		User subscriber1 = new User();
		User subscriber2 = new User();
		usersList.add(subscriber1);
		usersList.add(subscriber2);
		
		Page<User> users = new PageImpl<User>(usersList);
		PageRequest pageReq = new PageRequest(0, 20);
		
		when(userDao.findByGroup(GROUP_ID, pageReq)).thenReturn(users);
		
		Mockito.doNothing().when(i18nUtil).internationalizeListedResponse((ListedResponse)any(), (String)any(), (String)any());
		
	}
	
	@Test
	public void succesfullyGetSubscribers(){
		SearchUserResponse response = getGroupSubscribersCmd.execute(request);
		assertEquals(response.getAmountUsersFound(), 2);
	}
	
	@Test
	public void searchWithInvalidGroup(){
		request = new GetGroupSubscribersRequest(123L, USR_TOKEN, "en", 0, 20);
		try{
			getGroupSubscribersCmd.execute(request);
		} catch(ServiceException e) {
			assertEquals(e.getCode(), ServiceExceptionCode.INVALID_GROUP.getCode());
		}
	}
	
	@Test
	public void searchWithInvalidUsrToken(){
		request = new GetGroupSubscribersRequest(GROUP_ID, "invalidToken", "en", 0, 20);
		try{
			getGroupSubscribersCmd.execute(request);
		} catch(ServiceException e) {
			assertEquals(e.getCode(), ServiceExceptionCode.USER_UNAUTHORIZED.getCode());
		}
	}
	
}
