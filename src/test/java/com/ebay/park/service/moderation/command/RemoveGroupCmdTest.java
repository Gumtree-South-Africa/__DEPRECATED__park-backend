package com.ebay.park.service.moderation.command;

import com.ebay.park.db.dao.GroupDao;
import com.ebay.park.db.entity.Group;
import com.ebay.park.db.entity.ItemGroup;
import com.ebay.park.db.entity.UserFollowsGroup;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.moderation.dto.GroupIdRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class RemoveGroupCmdTest {
	@Spy
	@InjectMocks
	private final RemoveGroupCmd cmd = new RemoveGroupCmd();

	@Mock
	private GroupIdRequest request;

	@Mock
	private GroupDao groupDao;

	@Mock
	private UserFollowsGroup userFollowsGroup1;
	@Mock
	private UserFollowsGroup userFollowsGroup2;
	@Mock
	private UserFollowsGroup userFollowsGroup3;

	@Mock
	private ItemGroup itemGroup1;
	@Mock
	private ItemGroup itemGroup2;
	@Mock
	private ItemGroup itemGroup3;

	@Mock
	private Group group;

	@Before
	public void setUp() {
		initMocks(this);
		when(request.getGroupId()).thenReturn(1l);

		when(group.getFollowers()).thenReturn(Arrays.asList(userFollowsGroup1, userFollowsGroup2, userFollowsGroup3));
		when(group.getItems()).thenReturn(Arrays.asList(itemGroup1, itemGroup2, itemGroup3));

		when(groupDao.findOne(1l)).thenReturn(group);

	}

	@Test
	public void testExecuteShouldSucceed() {
		ServiceResponse response = cmd.execute(request);
		verify(groupDao).delete(group);
		assertEquals(ServiceResponse.SUCCESS, response);
	}

	@Test
	public void testExecuteShouldFailOnInvalidGroup() {

		when(groupDao.findOne(1l)).thenReturn(null);

		try {
			cmd.execute(request);
			fail();
		} catch (ServiceException e) {
			assertEquals(ServiceExceptionCode.INVALID_GROUP.getCode(), e.getCode());
		}
	}

}
