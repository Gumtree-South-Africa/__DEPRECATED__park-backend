/**
 * Copyright eBay, 2014
 */
package com.ebay.park.service.group.command;

import com.ebay.park.db.dao.GroupDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Group;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.group.dto.GetGroupRequest;
import com.ebay.park.service.group.dto.GroupDTO;
import com.ebay.park.util.TextUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author diana.gazquez
 *
 */
public class GetGroupCmdTest {
	

	private static final String URL = "url";

	@InjectMocks
	private final GetGroupCmd cmd = new GetGroupCmd();

	@Mock
	private GetGroupRequest request;

	@Mock
	private GroupDao groupDao;

	@Mock
	private UserDao userDao;
	
	@Mock
	private User user;
	
	private long userId = 24535l;

	@Mock
	private Group group;
	
	@Mock
	private TextUtils textUtils;
	
	private long groupId = 5465l;
	
	
	@Before
	public void setUp() {
		initMocks(this);
		when(request.getId()).thenReturn((new Long(groupId)).toString());
		when(user.getId()).thenReturn(userId);
		when(user.getUserId()).thenReturn(userId);
		when(group.getId()).thenReturn(groupId);
		when(group.getGroupId()).thenReturn(groupId);
		when(textUtils.createGroupSEOURL(group.getName(), group.getId())).thenReturn(URL);
	}

	@Test
	public void execute_nonAnonimousCall_groupReturned() {
		when(request.getToken()).thenReturn("123456");
		when(userDao.findByToken("123456")).thenReturn(user);
		when(groupDao.findOne(groupId)).thenReturn(group);
//		when(groupDao.getGroupFollowersWithPicture(eq(groupId),eq(userId),any(Pageable.class))).thenReturn(Collections.<User>emptyList());
//		
//		when(groupDao.save(any(Group.class))).thenReturn(group);
//
//		GroupDTO groupResponse = cmd.execute(request);
//
//		verify(groupDao).getGroupFollowersWithPicture(eq(groupId),eq(userId),any(Pageable.class));
//		assertNotNull(groupResponse);

	}
	
	@Test
	public void execute_anonimousCall_groupReturned() {
		when(request.getToken()).thenReturn(null);
		when(groupDao.findOne(groupId)).thenReturn(group);	
		when(groupDao.save(any(Group.class))).thenReturn(group);

		GroupDTO groupResponse = cmd.execute(request);
		assertNotNull(groupResponse);

	}

}
