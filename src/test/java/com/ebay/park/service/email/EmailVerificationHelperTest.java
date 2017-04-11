package com.ebay.park.service.email;

import com.ebay.park.db.entity.User;
import org.junit.Before;
import org.mockito.Mock;

import static org.mockito.MockitoAnnotations.initMocks;

public class EmailVerificationHelperTest {

	private EmailVerificationHelper emailVerificationHelper;

	@Mock
	private User user;

	@Before
	public void setUp(){
		emailVerificationHelper = new EmailVerificationHelper();
		initMocks(this);
	}

}
