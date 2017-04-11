package com.ebay.park.service.item.validator;

import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.item.ItemUtils;
import com.ebay.park.service.item.dto.UpdateItemRequest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class UpdateItemValidatorTest {
	private static final String VALID_DESCRIPTION = "This is a valid description";
	private static final String INVALID_DESCRIPTION = "This is an invalid description;";
	
	private static final String VALID_NAME = "T";
	private static final String INVALID_NAME = "bla&bla";
	private static final String MSG1 = "An exception was not expected";
    private static final String MSG2 = "An exception was expected";

	@InjectMocks
	private UpdateItemValidator validator;

	@Mock
	private ItemUtils itemUtils;

	@Before
	public void setUp() {
		validator = new UpdateItemValidator();
		initMocks(this);
		when(itemUtils.isValidTextTitle(VALID_NAME)).thenReturn(true);
		when(itemUtils.isValidTextTitle(INVALID_NAME)).thenReturn(false);
		when(itemUtils.isValidTextDescription(VALID_DESCRIPTION)).thenReturn(true);
		when(itemUtils.isValidTextDescription(INVALID_DESCRIPTION)).thenReturn(false);
	}

	@Test
	public void validCreation() {
		UpdateItemRequest request = new UpdateItemRequest();
		request.setName(VALID_NAME);	
		request.setDescription(VALID_DESCRIPTION);

		try {
			validator.validate(request);
			verify(itemUtils).isValidTextTitle(VALID_NAME);
			verify(itemUtils).isValidTextDescription(VALID_DESCRIPTION);
		} catch(ServiceException e) {
			fail(MSG1 + e);
		}
	}

	@Test
	public void givenNullDescriptionThenValidCreation()
	{
	    UpdateItemRequest request = new UpdateItemRequest();
        request.setName(VALID_NAME);    
		request.setDescription(null);
		try {
			validator.validate(request);
			verify(itemUtils).isValidTextTitle(VALID_NAME);
		} catch(ServiceException e) {
			fail(MSG1 + e);
		}
	}

	@Test
	public void giveSpecialCharactersInNameThenException() {
		UpdateItemRequest request = new UpdateItemRequest();
		request.setName(INVALID_NAME);	
		request.setDescription(VALID_DESCRIPTION);

		try {
			validator.validate(request);
			fail(MSG2);
		} catch(ServiceException e) {
			assertEquals(ServiceExceptionCode.INVALID_ITEM_NAME_CHARACTER.getCode(), e.getCode());
		}
	}

	@Test
    public void giveSpecialCharactersInDescriptionThenException() {
        UpdateItemRequest request = new UpdateItemRequest();
		request.setDescription(INVALID_DESCRIPTION);
		request.setName(VALID_NAME);
		try {
			validator.validate(request);
			fail(MSG2);
		} catch(ServiceException e) {
			assertEquals(ServiceExceptionCode.INVALID_ITEM_DESCRIPTION_CHARACTER.getCode(), e.getCode());
		}
	}

	@Test
    public void givenEmptyDataThenException() {
	    UpdateItemRequest request = new UpdateItemRequest();
	    request.setName("");
	    
	    try {
            validator.validate(request);
            fail(MSG2);
        } catch(ServiceException e) {
            assertEquals(ServiceExceptionCode.EMPTY_DATA_UPDATE.getCode(), e.getCode());
        }
	}
}
