package com.ebay.park.service.moderation.command;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projection;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.ebay.park.db.entity.DeviceType;
import com.ebay.park.db.entity.OrphanedDevice;
import com.ebay.park.db.entity.UserSession;
import com.ebay.park.service.moderation.UserReceiverPush;
import com.ebay.park.service.moderation.dto.FilterForModerationRequest;
import com.ebay.park.service.moderation.dto.SendNotificationsForModerationRequest;
import com.ebay.park.service.social.SocialIds;

public class AbstractFiltererCmdTest {
private static final Boolean IS_GROUP_OWNER = true;
private static final Boolean IS_NOT_GROUP_OWNER = false;
private static final Boolean IS_GROUP_FOLLOWER = true;
private static final Boolean IS_NOT_GROUP_FOLLOWER = false;
protected static final String MSG = "An exception was not expected";
protected static final String MSG2 = "An exception was expected";
private static final Long FACEBOOK = 1L;
private static final Long TWITTER = 2L;
private static final Boolean HAS_FACEBOOK = true;
private static final Boolean DOES_NOT_HAVE_FACEBOOK = false;
private static final Boolean HAS_TWITTER = true;
private static final Boolean DOES_NOT_HAVE_TWITTER = false;
private static final String ALL_PLATFORM = "all";
private static final String ANDROID_PLATFORM = "android";
private static final String IOS_PLATFORM = "ios";
private static final Boolean IS_VERIFIED = true;
private static final Boolean IS_NOT_VERIFIED = false;
private static final String DATE_FROM = "1431907200";
private static final String DATE_TO = "1432252800";
private static final Integer ZIPCODE1 = new Integer(90201);
private static final Integer ZIPCODE2 = new Integer(77002);
private static final Boolean HAS_ACTIVE_ITEMS = true;
private static final Boolean DOES_NOT_HAVE_ACTIVE_ITEMS = false;
private static final Long CATEGORY = 5L;
private static final Long USER1 = 1L;
private static final Long USER2 = 2L;
private static final String DEVICE1 = null;
private static final String DEVICE2 = null;
private static final Boolean NOT_ONLY_PUSH = false;
private static final String ACTIVE_SESSION = "active_session";
private static final String NO_SESSION = "no_session";
private static final Long GROUP_ID = 1L;

@Mock
protected EntityManager entityManager;

@Mock
protected SendNotificationsForModerationRequest request1;

@Mock
protected FilterForModerationRequest request2;

@Mock
protected FilterForModerationRequest request3;

@Mock
protected SendNotificationsForModerationRequest request4;

@Mock
protected SendNotificationsForModerationRequest request5;

protected Session session= Mockito.mock(Session.class);
protected Criteria userCriteria = Mockito.mock(Criteria.class);

@Mock
protected Criteria sessionCriteria;

@Mock
protected Criteria noSessionCriteria;

@Mock
protected Criteria groupCriteria;

@Mock
private SocialIds socialIds;

	public void setUp() {
		initMocks(this);
		
		//request 1
		request1 = new SendNotificationsForModerationRequest();
		
		request1.setIsGroupOwner(IS_GROUP_OWNER);
		request1.setPlatform(ALL_PLATFORM);
		request1.setIsGroupFollower(IS_GROUP_FOLLOWER);
		request1.setHasFacebook(HAS_FACEBOOK);
		request1.setHasTwitter(HAS_TWITTER);
		request1.setIsVerified(IS_VERIFIED);
		request1.setAccountCreationFrom(DATE_FROM);
		request1.setAccountCreationTo(DATE_TO);
		List<Integer> list = new ArrayList<Integer>(
			    Arrays.asList(ZIPCODE1, ZIPCODE2));
		request1.setZipCode(list);
		request1.setHasActiveItems(null);
		request1.setOnlyPush(NOT_ONLY_PUSH);
		request1.setSessionStatus(ACTIVE_SESSION);
		
		//request 2
		request2 = new FilterForModerationRequest();
		request2.setIsGroupOwner(IS_NOT_GROUP_OWNER);
		request2.setPlatform(ANDROID_PLATFORM);
		request2.setIsGroupFollower(IS_NOT_GROUP_FOLLOWER);
		request2.setHasFacebook(DOES_NOT_HAVE_FACEBOOK);
		request2.setHasTwitter(DOES_NOT_HAVE_TWITTER);
		request2.setIsVerified(IS_NOT_VERIFIED);
		request2.setHasActiveItems(HAS_ACTIVE_ITEMS);
		request2.setCategoryActiveItems(CATEGORY);
		request2.setSessionStatus(ACTIVE_SESSION);
		
		//request 3
		request3 = new FilterForModerationRequest();
		request3.setPlatform(IOS_PLATFORM);
		request3.setHasActiveItems(DOES_NOT_HAVE_ACTIVE_ITEMS);
		request3.setSessionStatus(ACTIVE_SESSION);
		
		//request 4
		request4 = new SendNotificationsForModerationRequest();
		request4.setPlatform(IOS_PLATFORM);
		request4.setOnlyPush(NOT_ONLY_PUSH);
		request4.setSessionStatus(ACTIVE_SESSION);
		request4.setIsMemberOfGroup(GROUP_ID);
		
		//request 5
		request5 = new SendNotificationsForModerationRequest();
		request5.setPlatform(IOS_PLATFORM);
		request5.setOnlyPush(NOT_ONLY_PUSH);
		request5.setSessionStatus(NO_SESSION);
		request5.setIsMemberOfGroup(GROUP_ID);
		
		
		when(entityManager.unwrap(Session.class)).thenReturn(session);
		when(session.createCriteria(UserSession.class, "sessionCriteria")).thenReturn(sessionCriteria);
		when(sessionCriteria.createCriteria("user")).thenReturn(userCriteria);
		
		when(session.createCriteria(OrphanedDevice.class,"orphanedDeviceCriteria")).thenReturn(noSessionCriteria);
				
		when(socialIds.getFacebookId()).thenReturn(FACEBOOK);
		when(socialIds.getTwitterId()).thenReturn(TWITTER);
		
		when(userCriteria.createAlias(Mockito.anyString(), Mockito.anyString())).thenReturn(userCriteria);
		when(userCriteria.setProjection((Projection) Mockito.any())).thenReturn(userCriteria);
		
		when(groupCriteria.createAlias(Mockito.anyString(), Mockito.anyString())).thenReturn(groupCriteria);
		when(groupCriteria.setProjection((Projection) Mockito.any())).thenReturn(groupCriteria);
		
		when(noSessionCriteria.createAlias(Mockito.anyString(), Mockito.anyString())).thenReturn(noSessionCriteria);
		when(noSessionCriteria.setProjection((Projection) Mockito.any())).thenReturn(noSessionCriteria);
		
		//results
		UserReceiverPush result1 = new UserReceiverPush();
		result1.setUserId(USER1);
		result1.setDeviceId(DEVICE1);
		result1.setPlatform(DeviceType.IOS);
		
		UserReceiverPush result2 = new UserReceiverPush();
		result2.setUserId(USER2);
		result2.setDeviceId(DEVICE2);
		result2.setPlatform(DeviceType.ANDROID);
		
		when(userCriteria.list()).thenReturn(new ArrayList<UserReceiverPush> (Arrays.asList(
				result1, result2)));
		when(groupCriteria.list()).thenReturn(new ArrayList<UserReceiverPush> (Arrays.asList(
				result1, result2)));
		when(noSessionCriteria.list()).thenReturn(new ArrayList<UserReceiverPush> (Arrays.asList(
				result1, result2)));
	}
}
