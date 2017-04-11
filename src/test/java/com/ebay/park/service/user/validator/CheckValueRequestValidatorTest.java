package com.ebay.park.service.user.validator;

import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceValidator;
import com.ebay.park.service.user.dto.CheckValueRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * 
 * @author lucia.masola
 * 
 */
public class CheckValueRequestValidatorTest {

	@InjectMocks
	private ServiceValidator<CheckValueRequest> validator;

	private static final String FAIL_MESSAGE = "An exception was expected. ";
	private static final String VALID_VALUE = "field";
	private static final String EMAIL_FIELD = "email";
	private static final String NAME_FIELD = "username";
	private static final String EMPTY_FIELD = "";
	private static final String INVALID_FIELD = "invalidField";

	@Before
	public void setUp() {
		validator = new CheckValueRequestValidator();
		initMocks(this);
	}

	@Test
	public void validCheckValueRequestTest() {

		validator.validate(new CheckValueRequest(EMAIL_FIELD, VALID_VALUE));
		validator.validate(new CheckValueRequest(NAME_FIELD, VALID_VALUE));

	}

	@Test
	public void invalidCheckValueRequest() {

		try {

			validator.validate(new CheckValueRequest(VALID_VALUE, EMPTY_FIELD));
			fail(FAIL_MESSAGE);

		} catch (ServiceException re) {

			assertEquals(re.getCode(), ServiceExceptionCode.BAD_REQ_INFO.getCode());

		}

		try {

			validator.validate(new CheckValueRequest(EMPTY_FIELD, EMAIL_FIELD));
			fail(FAIL_MESSAGE);

		} catch (ServiceException re) {

			assertEquals(re.getCode(), ServiceExceptionCode.BAD_REQ_INFO.getCode());

		}

		try {

			validator.validate(new CheckValueRequest(VALID_VALUE, INVALID_FIELD));
			fail(FAIL_MESSAGE);

		} catch (ServiceException re) {

			assertEquals(re.getCode(), ServiceExceptionCode.BAD_REQ_INFO.getCode());

		}

	}

}
