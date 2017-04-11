package com.ebay.park.service.item.validator;

import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.item.ItemUtils;
import com.ebay.park.service.item.dto.CreateItemRequest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class CreateItemValidatorTest {
	private static final String VALID_BRAND = "android";
	private static final String VALID_DESCRIPTION = "This is a valid description";
	private static final String INVALID_DESCRIPTION = "This is an invalid description;";
	private static final String INVALID_LENGTH_DESCRIPTION = "This is an invalid description This is an invalid description This is an invalid description This is an invalid description This is an invalid description This is an invalid description This is an invalid description This is an invalid description This is an invalid description";
	private static final String VALID_LOCATION = "5374fa22498e33ddadb073b3";
	private static final String VALID_LOCATION_NAME = "Tandil";
	private static final double VALID_LATITUDE = -42.0998;
	private static final double VALID_LONGITUDE = -120.413;
	private static final String VALID_PRICE = "10";
	private static final String VALID_VERSION = "1";
	private static final long VALID_CATEGORY_ID = 5;
	private static final String VALID_NAME = "T";
	private static final String INVALID_NAME = "bla&bla";
	private static final String MSG1 = "An exception was not expected";
	private static final String MSG2 = "An exception was expected";
	private static final String VALID_ZIPCODE = "90210";
    private static final double LONGITUDE = 90;
    private static final double LATITUDE = 90;
    private static final long INVALID_CATEGORY_ID = -1;

	@InjectMocks
	private CreateItemValidator validator;
	@Mock
	private ItemUtils itemUtils;
	
	@Before
	public void setUp() {
		validator = new CreateItemValidator();
		initMocks(this);
		Mockito.when(itemUtils.isValidTextTitle(VALID_NAME)).thenReturn(true);
		Mockito.when(itemUtils.isValidTextTitle(INVALID_NAME)).thenReturn(false);
		Mockito.when(itemUtils.isValidTextDescription(VALID_DESCRIPTION)).thenReturn(true);
		Mockito.when(itemUtils.isValidTextDescription(INVALID_DESCRIPTION)).thenReturn(false);
		Mockito.when(itemUtils.isValidTextDescription(INVALID_LENGTH_DESCRIPTION)).thenReturn(true);
	}
	@Test
	public void validCreation() {
		CreateItemRequest request = new CreateItemRequest();
		request.setName(VALID_NAME);	
		request.setBrandPublish(VALID_BRAND);
		request.setDescription(VALID_DESCRIPTION);
		request.setLocation(VALID_LOCATION);
		request.setLocationName(VALID_LOCATION_NAME);
		request.setLatitude(VALID_LATITUDE);
		request.setLongitude(VALID_LONGITUDE);
		request.setPrice(VALID_PRICE);
		request.setVersionPublish(VALID_VERSION);
		request.setCategoryId(VALID_CATEGORY_ID);
		request.setZipCode(VALID_ZIPCODE);
		
		try {
			validator.validate(request);
			verify(itemUtils).isValidTextTitle(VALID_NAME);
			verify(itemUtils).isValidTextDescription(VALID_DESCRIPTION);
		} catch(ServiceException e) {
			fail(MSG1 + e);
		}
		
		request.setDescription(null);
		try {
			validator.validate(request);
		} catch(ServiceException e) {
			fail(MSG1 + e);
		}
	}
	
	@Test
	public void givenEmptyDataThenException() {
	    CreateItemRequest request = new CreateItemRequest();
        request.setName(INVALID_NAME);
        try {
            validator.validate(request);
        } catch(ServiceException e) {
            assertEquals(ServiceExceptionCode.EMPTY_DATA_CREATEITEM.getCode(), e.getCode());
        }
	}

	@Test
	public void givenSpecialCharacterInNameThenException(){
		CreateItemRequest request = new CreateItemRequest();
		request.setName(INVALID_NAME);	
		request.setBrandPublish(VALID_BRAND);
		request.setDescription(VALID_DESCRIPTION);
		request.setLocation(VALID_LOCATION);
		request.setLocationName(VALID_LOCATION_NAME);
		request.setLatitude(VALID_LATITUDE);
		request.setLongitude(VALID_LONGITUDE);
		request.setPrice(VALID_PRICE);
		request.setVersionPublish(VALID_VERSION);
		request.setCategoryId(VALID_CATEGORY_ID);
		request.setZipCode(VALID_ZIPCODE);
		try {
			validator.validate(request);
		} catch(ServiceException e) {
			assertEquals(ServiceExceptionCode.INVALID_ITEM_NAME_CHARACTER.getCode(), e.getCode());
		}
		
		
	}
	
	@Test
    public void givenSpecialCharacterInDescriptionThenException(){
	    CreateItemRequest request = new CreateItemRequest();
        request.setBrandPublish(VALID_BRAND);
        request.setDescription(VALID_DESCRIPTION);
        request.setLocation(VALID_LOCATION);
        request.setLocationName(VALID_LOCATION_NAME);
        request.setLatitude(VALID_LATITUDE);
        request.setLongitude(VALID_LONGITUDE);
        request.setPrice(VALID_PRICE);
        request.setVersionPublish(VALID_VERSION);
        request.setCategoryId(VALID_CATEGORY_ID);
        request.setZipCode(VALID_ZIPCODE);
	    request.setDescription(INVALID_DESCRIPTION);
        request.setName(VALID_NAME);
        try {
            validator.validate(request);
        } catch(ServiceException e) {
            assertEquals(ServiceExceptionCode.INVALID_ITEM_DESCRIPTION_CHARACTER.getCode(), e.getCode());
        }
	}
	
	@Test
    public void givenNullDescriptionThenSuccessfulCreation() {
	    CreateItemRequest request = new CreateItemRequest();
        request.setName(VALID_NAME);    
        request.setBrandPublish(VALID_BRAND);
        request.setDescription(null);
        request.setLocation(VALID_LOCATION);
        request.setLocationName(VALID_LOCATION_NAME);
        request.setLatitude(VALID_LATITUDE);
        request.setLongitude(VALID_LONGITUDE);
        request.setPrice(VALID_PRICE);
        request.setVersionPublish(VALID_VERSION);
        request.setCategoryId(VALID_CATEGORY_ID);
        request.setZipCode(VALID_ZIPCODE);
        
        try {
            validator.validate(request);
            verify(itemUtils).isValidTextTitle(VALID_NAME);
        } catch(ServiceException e) {
            fail(MSG1 + e);
        }
    }
	
	@Test
	public void givenInvalidLatitudeThenException() {
	    CreateItemRequest request = new CreateItemRequest();
        request.setLatitude(null);
        request.setLongitude(LONGITUDE);
        try {
            validator.validate(request);
            fail(MSG2);
        } catch(ServiceException e) {
            assertEquals(ServiceExceptionCode.INVALID_LATITUDE_LONGITUDE.getCode(), e.getCode());
        }
	}
	
	@Test
    public void givenInvalidLongitudeThenException() {
        CreateItemRequest request = new CreateItemRequest();
        request.setLatitude(LATITUDE);
        request.setLongitude(null);
        try {
            validator.validate(request);
            fail(MSG2);
        } catch(ServiceException e) {
            assertEquals(ServiceExceptionCode.INVALID_LATITUDE_LONGITUDE.getCode(), e.getCode());
        }
    }
	
	@Test
    public void givenInvalidDescriptionLengthThenException() {
        CreateItemRequest request = new CreateItemRequest();
        request.setDescription(INVALID_LENGTH_DESCRIPTION);
        request.setName(VALID_NAME);    
        request.setBrandPublish(VALID_BRAND);
        request.setLocation(VALID_LOCATION);
        request.setLocationName(VALID_LOCATION_NAME);
        request.setLatitude(VALID_LATITUDE);
        request.setLongitude(VALID_LONGITUDE);
        request.setPrice(VALID_PRICE);
        request.setVersionPublish(VALID_VERSION);
        request.setCategoryId(VALID_CATEGORY_ID);
        request.setZipCode(VALID_ZIPCODE);

        try {
            validator.validate(request);
            fail(MSG2);
        } catch(ServiceException e) {
            assertEquals(ServiceExceptionCode.INVALID_ITEM_DESCRIPTION_LONG.getCode(), e.getCode());
            verify(itemUtils).isValidTextTitle(VALID_NAME);
            verify(itemUtils).isValidTextDescription(INVALID_LENGTH_DESCRIPTION);
        }
    }
	
	@Test
    public void givenNullLocationThenValidate() {
        CreateItemRequest request = new CreateItemRequest();
        request.setDescription(VALID_DESCRIPTION);
        request.setName(VALID_NAME);    
        request.setBrandPublish(VALID_BRAND);
        request.setLocation(null);
        request.setLocationName(VALID_LOCATION_NAME);
        request.setLatitude(VALID_LATITUDE);
        request.setLongitude(VALID_LONGITUDE);
        request.setPrice(VALID_PRICE);
        request.setVersionPublish(VALID_VERSION);
        request.setCategoryId(VALID_CATEGORY_ID);
        request.setZipCode(VALID_ZIPCODE);

        try {
            validator.validate(request);
            verify(itemUtils).isValidTextTitle(VALID_NAME);
            verify(itemUtils).isValidTextDescription(VALID_DESCRIPTION);
        } catch(ServiceException e) {
            fail(MSG1);
        }
    }
	
	@Test
    public void givenInvalidCategoryThenException() {
        CreateItemRequest request = new CreateItemRequest();
        request.setDescription(VALID_DESCRIPTION);
        request.setName(VALID_NAME);    
        request.setBrandPublish(VALID_BRAND);
        request.setLocation(null);
        request.setLocationName(VALID_LOCATION_NAME);
        request.setLatitude(VALID_LATITUDE);
        request.setLongitude(VALID_LONGITUDE);
        request.setPrice(VALID_PRICE);
        request.setVersionPublish(VALID_VERSION);
        request.setCategoryId(INVALID_CATEGORY_ID);
        request.setZipCode(VALID_ZIPCODE);

        try {
            validator.validate(request);
            fail(MSG2);
        } catch(ServiceException e) {
            assertEquals(ServiceExceptionCode.EMPTY_DATA_CREATEITEM.getCode(), e.getCode());
        }
    }
}