package com.ebay.park.service.social.command;

import com.ebay.park.db.dao.FollowerDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Follower;
import com.ebay.park.db.entity.FollowerPK;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.social.dto.UnfollowUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.MockitoAnnotations.initMocks;

public class RemoveFollowerCmdTest {

	private static final String FOLLOWER = "mike_wazowski";

	private static final String FOLLOWED = "sullivan";

	private static final Long KEY_FOLLOWER = 12345l;
	private static final Long KEY_FOLLOWED = 67899l;

	@InjectMocks
	private RemoveFollowerCmd removeFollowersCmd;

	@Mock
	private UserDao userDao;

	@Mock
	private FollowerDao followerDao;

	@Before
	public void setUp() {
		removeFollowersCmd = new RemoveFollowerCmd();
		initMocks(this);
	}

	@Test
	public void executeUnfollowSuccessTest(){

		User userFollowed = new User();
		userFollowed.setUserId(KEY_FOLLOWED);
		Mockito.when(userDao.findByUsername(FOLLOWED)).thenReturn(userFollowed);

		User userFollower = new User();
		userFollower.setUserId(KEY_FOLLOWER);
		Mockito.when(userDao.findByUsername(FOLLOWER)).thenReturn(userFollower);


		FollowerPK id = new FollowerPK(KEY_FOLLOWER, KEY_FOLLOWED);
		Follower follower = new Follower(id , userFollowed);
		Mockito.when(followerDao.findFollower(userFollower.getId(), userFollowed.getUserId())).thenReturn(follower);

		UnfollowUserRequest request = new UnfollowUserRequest();
		request.setFollower(FOLLOWER);
		request.setUserToUnfollow(FOLLOWED);

		removeFollowersCmd.execute(request);


		Mockito.verify(userDao, Mockito.times(1)).findByUsername(FOLLOWED);
		Mockito.verify(userDao, Mockito.times(1)).findByUsername(FOLLOWER);
		Mockito.verify(followerDao, Mockito.times(1)).findFollower(userFollower.getId(), userFollowed.getUserId());

	}

	@Test
	public void executeEmptyFollowerTest(){

		UnfollowUserRequest request = new UnfollowUserRequest();
		request.setUserToUnfollow(FOLLOWED);

		try{
			removeFollowersCmd.execute(request);
			fail("A service exception was expected. ");
		} catch(ServiceException se){
			assertEquals(ServiceExceptionCode.USER_NOT_FOUND.getCode(), se.getCode());
		}
	}

	@Test
	public void executeEmptyUserToUnfollowTest(){

		UnfollowUserRequest request = new UnfollowUserRequest();
		request.setFollower(FOLLOWER);

		try{
			removeFollowersCmd.execute(request);
			fail("A service exception was expected. ");
		} catch(ServiceException se){
			assertEquals(ServiceExceptionCode.USER_NOT_FOUND.getCode(), se.getCode());
		}
	}

	@Test
	public void executeNotFollowingBetweenUsersTest(){

		User userFollowed = new User();
		userFollowed.setUserId(KEY_FOLLOWED);
		Mockito.when(userDao.findByUsername(FOLLOWED)).thenReturn(userFollowed);

		User userFollower = new User();
		userFollower.setUserId(KEY_FOLLOWER);
		Mockito.when(userDao.findByUsername(FOLLOWER)).thenReturn(userFollower);

		UnfollowUserRequest request = new UnfollowUserRequest();
		request.setFollower(FOLLOWER);
		request.setUserToUnfollow(FOLLOWED);

		Mockito.when(followerDao.findFollower(userFollower.getId(), userFollowed.getUserId())).thenReturn(null);

		try{
			removeFollowersCmd.execute(request);
			fail("A service exception was expected. ");
		} catch(ServiceException se){
			assertEquals(ServiceExceptionCode.FOLLOWING_NOT_FOUND.getCode(), se.getCode());
		}

		Mockito.verify(userDao, Mockito.times(1)).findByUsername(FOLLOWED);
		Mockito.verify(userDao, Mockito.times(1)).findByUsername(FOLLOWER);
		Mockito.verify(followerDao, Mockito.times(1)).findFollower(userFollower.getId(), userFollowed.getUserId());
	}

}
