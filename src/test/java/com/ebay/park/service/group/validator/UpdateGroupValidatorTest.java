package com.ebay.park.service.group.validator;

import com.ebay.park.db.dao.GroupDao;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.group.dto.UpdateGroupRequest;
import com.ebay.park.service.item.ItemUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class UpdateGroupValidatorTest {
	private static final String VALID_DESCRIPTION = "This is a valid description";
	private static final String INVALID_DESCRIPTION = "This is an invalid description;";
	private static final String VALID_LOCATION = "33.2,12.3";
	private static final String VALID_LOCATION_NAME = "Tandil";
	private static final String VALID_NAME = "T";
	private static final String INVALID_NAME = "bla&bla";
	private static final String MSG = "An exception was not expected";
	private static final String VALID_ZIPCODE = "90210";
	private static final String VALID_TOKEN = "123456";
	@InjectMocks
	private UpdateGroupValidator validator;
	@Mock
	private ItemUtils itemUtils;
	@Mock
	private UpdateGroupRequest request;
	@Mock
	private GroupDao groupDao;
	
	@Before
	public void setUp() {
		validator = new UpdateGroupValidator();
		initMocks(this);
		Mockito.when(itemUtils.isValidTextTitle(VALID_NAME)).thenReturn(true);
		Mockito.when(itemUtils.isValidTextTitle(INVALID_NAME)).thenReturn(false);
		Mockito.when(itemUtils.isValidTextDescription(VALID_DESCRIPTION)).thenReturn(true);
		Mockito.when(itemUtils.isValidTextDescription(INVALID_DESCRIPTION)).thenReturn(false);
	}
	@Test
	public void validCreation() {	
		request.setDescription(VALID_DESCRIPTION);
		when(request.getLocation()).thenReturn(VALID_LOCATION);
		when(request.getLocationName()).thenReturn(VALID_LOCATION_NAME);
		when(request.getName()).thenReturn(VALID_NAME);
		when(request.getToken()).thenReturn(VALID_TOKEN);
		when(request.getZipCode()).thenReturn(VALID_ZIPCODE);
		when(groupDao.findByName(VALID_NAME)).thenReturn(null);
		
		try {
			validator.validate(request);
		} catch(ServiceException e) {
			fail(MSG + e);
		}
	}

	@Test
	public void specialCharacters() {
		//invalid description
		request.setDescription(INVALID_DESCRIPTION);
		when(request.getLocation()).thenReturn(VALID_LOCATION);
		when(request.getLocationName()).thenReturn(VALID_LOCATION_NAME);
		when(request.getName()).thenReturn(VALID_NAME);
		when(request.getToken()).thenReturn(VALID_TOKEN);
		when(request.getZipCode()).thenReturn(VALID_ZIPCODE);
		when(groupDao.findByName(VALID_NAME)).thenReturn(null);
		
		try {
			validator.validate(request);
		} catch(ServiceException e) {
			assertEquals(ServiceExceptionCode.INVALID_GROUP_DESCRIPTION_CHARACTER.getCode(), e.getCode());
		}
		
		//invalid name
		request.setDescription(VALID_DESCRIPTION);
		when(request.getName()).thenReturn(INVALID_NAME);
		
		try {
			validator.validate(request);
		} catch(ServiceException e) {
			assertEquals(ServiceExceptionCode.INVALID_GROUP_NAME_CHARACTER.getCode(), e.getCode());
		}
	}
}
