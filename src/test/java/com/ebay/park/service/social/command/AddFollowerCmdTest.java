package com.ebay.park.service.social.command;

import com.ebay.park.db.dao.FollowerDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Follower;
import com.ebay.park.db.entity.FollowerPK;
import com.ebay.park.db.entity.User;
import com.ebay.park.event.user.UsersEvent;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.social.dto.FollowUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class AddFollowerCmdTest {

	private static final String FOLLOWER = "timMartins";
	
	private static final String USERNAME1 = "thomasjhon";

	private static final Long KEY_FOLLOWER1 = 12345l;
	private static final Long KEY_USER1 = 67899l;
	
	@InjectMocks
	private AddFollowerCmd addFollowersCmd;
	
	@Mock
	private UserDao userDao;
	
	@Mock
	private FollowerDao followerDao;
	
	@Before
	public void setUp() {
		addFollowersCmd = new AddFollowerCmd();
		initMocks(this);
	}
	
	@Test
	public void executeTest(){
		
		FollowUserRequest request = new FollowUserRequest();
		request.setFollower(FOLLOWER);
		request.setUserToFollow(USERNAME1);
		
		User userToFollow = new User();
		userToFollow.setUserId(KEY_USER1);
		Mockito.when(userDao.findByUsername(USERNAME1)).thenReturn(userToFollow);
		
		User userFollower = new User();
		userFollower.setUserId(KEY_FOLLOWER1);
		Mockito.when(userDao.findByUsername(FOLLOWER)).thenReturn(userFollower);
		
		FollowerPK id = new FollowerPK(KEY_FOLLOWER1, KEY_USER1);
		Follower follower = new Follower(id , userToFollow);
		Mockito.when(followerDao.save(follower)).thenReturn(follower);
		
		UsersEvent response = addFollowersCmd.execute(request);
		
		assertNotNull(response);
		
		Mockito.verify(userDao, Mockito.times(1)).findByUsername(USERNAME1);
		Mockito.verify(userDao, Mockito.times(1)).findByUsername(FOLLOWER);
		Mockito.verify(followerDao, Mockito.times(1)).save(follower);

	}
	
	@Test
	public void executeEmptyUserTest1(){
		
		UsersEvent response = null;
		try{
			response = addFollowersCmd.execute(new FollowUserRequest());
			fail("A service exception was expected. ");
		} catch(ServiceException se){
			assertNull(response);
			assertEquals(ServiceExceptionCode.USER_NOT_FOUND.getCode(), se.getCode());
		}

	}
	
	@Test
	public void executeEmptyUserTest2(){
		
		FollowUserRequest request = new FollowUserRequest();
		request.setUserToFollow(USERNAME1);
		UsersEvent response = null;
		try{
			response = addFollowersCmd.execute(request);
			fail("A service exception was expected. ");
		} catch(ServiceException se){
			assertNull(response);
			assertEquals(ServiceExceptionCode.USER_NOT_FOUND.getCode(), se.getCode());
		}

	}

	@Test
	public void addFollowThatAlreadyExistsTest(){
		
		FollowUserRequest request = new FollowUserRequest();
		request.setFollower(FOLLOWER);
		request.setUserToFollow(USERNAME1);
		
		User userToFollow = new User();
		userToFollow.setUserId(KEY_USER1);
		Mockito.when(userDao.findByUsername(USERNAME1)).thenReturn(userToFollow);
		
		User userFollower = new User();
		userFollower.setUserId(KEY_FOLLOWER1);
		Mockito.when(userDao.findByUsername(FOLLOWER)).thenReturn(userFollower);
		
		FollowerPK id = new FollowerPK(KEY_FOLLOWER1, KEY_USER1);
		Follower follower = new Follower(id , userToFollow);
		
		Mockito.when(followerDao.findFollower(userFollower.getId(), userToFollow.getId())).thenReturn(follower);
		
		UsersEvent response = null;
		try {
			response = addFollowersCmd.execute(request);
		} catch (ServiceException e) {
			assertEquals(ServiceExceptionCode.FOLLOW_ALREADY_EXISTS.getCode(), e.getCode());			
		}
		assertNull(response);
		
		
		Mockito.verify(userDao, Mockito.times(1)).findByUsername(USERNAME1);
		Mockito.verify(userDao, Mockito.times(1)).findByUsername(FOLLOWER);
		Mockito.verify(followerDao, Mockito.times(1)).findFollower(userFollower.getId(), userToFollow.getId());
		
	}

}
