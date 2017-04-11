package com.ebay.park.service.moderation.command.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;

import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.moderation.dto.RejectItemRequest;
import com.ebay.park.service.moderation.rejection.RejectItemType;
import com.ebay.park.service.moderation.validator.RejectItemValidator;

public class RejectItemValidatorTest {

	private static final Long VALID_ITEM_ID = 223l;
	private static final int VALID_REASON_ID = 0;
	private static final int INVALID_REASON_ID1 = RejectItemType.getSize();
	private static final int INVALID_REASON_ID2 = -1;
    private static final String MSG = "An exception is expected";

	@Spy
	@InjectMocks
	private RejectItemValidator validator;
	
	@Before
	public void setUp() {
		initMocks(this);
	}

	@Test
	public void testValidate() {
		RejectItemRequest request = new RejectItemRequest(VALID_ITEM_ID, VALID_REASON_ID);

		try {
			validator.validate(request);
		} catch (ServiceException e) {
			fail();
		}
	}

	@Test
	public void testValidateNull() {
		RejectItemRequest request = null;
		try {
			validator.validate(request);
			fail(MSG);
		} catch (ServiceException e) {
			assertEquals(ServiceExceptionCode.INVALID_REJECT_ITEM_REQ.getCode(), e.getCode());
		}
	}

	@Test
	public void testValidateInvalidItemId() {
		RejectItemRequest request = new RejectItemRequest(null, VALID_REASON_ID);
		try {
			validator.validate(request);
			fail(MSG);
		} catch (ServiceException e) {
			assertEquals(ServiceExceptionCode.INVALID_REJECT_ITEM_ID_REQ.getCode(), e.getCode());
		}
	}

	@Test
	public void testValidateInvalidReasonId1() {
		RejectItemRequest request1 = new RejectItemRequest(VALID_ITEM_ID, INVALID_REASON_ID1);
		try {
			validator.validate(request1);
			fail(MSG);
		} catch (ServiceException e1) {
			assertEquals(ServiceExceptionCode.INVALID_REJECT_ITEM_REASON_REQ.getCode(), e1.getCode());
		}
	}

	@Test
    public void testValidateInvalidReasonId2() {
        RejectItemRequest request1 = new RejectItemRequest(VALID_ITEM_ID, INVALID_REASON_ID2);
        try {
            validator.validate(request1);
            fail(MSG);
        } catch (ServiceException e1) {
            assertEquals(ServiceExceptionCode.INVALID_REJECT_ITEM_REASON_REQ.getCode(), e1.getCode());
        }
    }
}
