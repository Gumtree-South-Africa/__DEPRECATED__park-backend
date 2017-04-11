package com.ebay.park.service.user.validator;

import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.user.dto.ChangePwdRequest;
import com.ebay.park.util.PasswdUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ChangePwdRequestValidatorTest {

	private static final String NEW_PWD = "pwd";
    private static final String CURRENT_PWD = "pwdold";
    private static final String PWD_MIXED_CASE = "6a@b4971";
    private static final String PWD_WITHOUT_DIGIT = "Ga@bAbCd";
    private static final String PWD_WITHOUT_SPECIAL = "6a5b4971";
    private static final String PWD_VALID = "Ac_321zz";
    private static final String PWD_TOO_SHORT = "AA";
    private static final String PWD_TOO_SIMILAR = "6547@AlL";

	@InjectMocks
	private ChangePwdRequestValidator validator;

	@Mock
	private UserDao userDao;

	@Mock
	private PasswdUtil passwdUtil;

	@Mock
	private ChangePwdRequest request;

	@Before
	public void setUp() {
		validator = new ChangePwdRequestValidator();
		initMocks(this);
		when(request.getCurrentPassword()).thenReturn("ABC654789");
		User user = mock(User.class);
		when(userDao.findByToken(any(String.class))).thenReturn(user);
		when(
				passwdUtil.equalsToHashedPassword(any(String.class),
						any(byte[].class))).thenReturn(true);
	}

	@Test
	public void givenValidValuesWhenValidatingThenAcceptPassword() {
		when(request.getNewPassword()).thenReturn(PWD_VALID);
		validator.validate(request);
	}

	@Test
	public void givenTooShortPwdWhenValidatingThenException() {
		try {
			when(request.getNewPassword()).thenReturn(PWD_TOO_SHORT);
			validator.validate(request);
			fail();

		} catch (ServiceException e) {
			assertEquals(ServiceExceptionCode.PWD_TOO_SHORT.getCode(), e.getCode());
		}
	}

	@Test
	public void givenTooSimilarPwdWhenValidatingThenException() {
		try {
			when(request.getNewPassword()).thenReturn(PWD_TOO_SIMILAR);
			validator.validate(request);
			fail();
		} catch (ServiceException e) {
			assertEquals(ServiceExceptionCode.PWD_TOO_SIMILAR.getCode(), e.getCode());
		}
	}

	@Test
	public void givenPasswordWithoutSpecialWhenValidatingThenAcceptPassword() {
		when(request.getNewPassword()).thenReturn(PWD_WITHOUT_SPECIAL);
		validator.validate(request);
	}

	@Test
	public void givenPasswordWithoutMixedCaseWhenValidatingThenAcceptPassword() {
		when(request.getNewPassword()).thenReturn(PWD_MIXED_CASE);
		validator.validate(request);
	}

	@Test
	public void givenPasswordWithoutDigitWhenValidatingThenAcceptPassword() {
		when(request.getNewPassword()).thenReturn(PWD_WITHOUT_DIGIT);
		validator.validate(request);
	}

	@Test
	public void givenEmptyCurrentPasswordWhenValidationThenException() {
	    ChangePwdRequest request = new ChangePwdRequest();
	    request.setCurrentPassword(null);
	    request.setNewPassword(NEW_PWD);
	    try {
	        validator.validate(request);
	        fail();
	    } catch (ServiceException e) {
	        assertEquals(ServiceExceptionCode.EMPTY_DATA_CHANGE_PWD.getCode(), e.getCode());
	    }
	}

	@Test
    public void givenEmptyNewPasswordWhenValidationThenException() {
        ChangePwdRequest request = new ChangePwdRequest();
        request.setCurrentPassword(CURRENT_PWD);
        request.setNewPassword(null);
        try {
            validator.validate(request);
            fail();
        } catch (ServiceException e) {
            assertEquals(ServiceExceptionCode.EMPTY_DATA_CHANGE_PWD.getCode(), e.getCode());
        }
    }

	@Test
    public void givenEmDFDFwPasswordWhenValidationThenException() {
        ChangePwdRequest request = new ChangePwdRequest();
        request.setCurrentPassword(CURRENT_PWD);
        request.setNewPassword(NEW_PWD);
        when(passwdUtil.equalsToHashedPassword(anyString(), any())).thenReturn(false);

        try {
            validator.validate(request);
            fail();
        } catch (ServiceException e) {
            assertEquals(ServiceExceptionCode.INVALID_PWD.getCode(), e.getCode());
        }
    }
}
