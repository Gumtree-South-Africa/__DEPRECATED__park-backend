package com.ebay.park.service.email.interceptor;

import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.email.EmailVerificationAspect;
import com.ebay.park.service.email.EmailVerificationHelper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class EmailVerificationAspectTest {

	@InjectMocks
	private EmailVerificationAspect emailVerificationInterceptor;

	@Mock
	private UserDao userDao;

	@Mock
	private EmailVerificationHelper emailNotificationHelper;
	;

	@Mock
	private Object handler;

	@Mock
	private User user;

	@Before
	public void setUp(){

		emailVerificationInterceptor = new EmailVerificationAspect();
		initMocks(this);

	}

	@Test
	public void testPreHandleHappyPath() throws Exception{

		when(userDao.findByToken("token")).thenReturn(user);
		when(user.isEmailVerified()).thenReturn(true);

		emailVerificationInterceptor.preHandle("token");


		verify(userDao, Mockito.times(1)).findByToken("token");
		verify(user, Mockito.times(1)).isEmailVerified();

	}

	@Test
	public void testPreHandleUserNotFound() throws Exception{

		when(userDao.findByToken("token")).thenThrow(new RuntimeException());

		try{
			emailVerificationInterceptor.preHandle("token");
			fail("A service exception was expected.");
		} catch(ServiceException e){
			assertEquals(e.getCode(), ServiceExceptionCode.USER_UNAUTHORIZED.getCode());
		} finally{
			verify(userDao, Mockito.times(1)).findByToken("token");
		}

	}

	@Test
	public void testPreHandleEmailNotVerify() throws Exception{

		when(userDao.findByToken("token")).thenReturn(user);
		when(user.isEmailVerified()).thenReturn(false);

		try{
			emailVerificationInterceptor.preHandle("token");
			fail("A service exception was expected.");
		} catch(ServiceException e){
			assertEquals(e.getCode(), ServiceExceptionCode.ACCOUNT_NOT_VERIFIED.getCode());
		} finally{
			verify(userDao, Mockito.times(1)).findByToken("token");
			verify(user, Mockito.times(1)).isEmailVerified();
		}

	}

}
