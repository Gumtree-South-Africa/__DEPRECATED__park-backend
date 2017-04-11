package com.ebay.park.service.item.validator;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.entity.Item;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.item.dto.UploadPhotosRequest;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Field.class})
public class UploadPhotosValidatorTest {
	
	private static final Long ITEM_ID = 1L;
	private static final int MAX_AMOUNT_PICTURES = 4;

	@Mock
	private ItemDao itemDao;
	
	@Mock
	private UploadPhotosRequest request;
	
	@Mock
	private Item item;
	
	@InjectMocks
	private UploadPhotosValidator validator = new UploadPhotosValidator();
	
	@Before
	public void setUp() {
		initMocks(this);
		Whitebox.setInternalState(validator, "maxAmountPictures", MAX_AMOUNT_PICTURES);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void givenANullRequestWhenvalidateThenException() {
		validator.validate(null);
	}
	
	@Test
	public void givenANullItemWhenValidateThenServiceException() {
		//given
		when(request.getItemId()).thenReturn(ITEM_ID);
		when(itemDao.findOne(ITEM_ID)).thenReturn(null);
		
		try {
			//when
			validator.validate(request);
			fail();
		} catch(ServiceException e) {
			//then
			assertEquals(ServiceExceptionCode.ITEM_NOT_FOUND.getCode(), e.getCode());
		}
	}
	
	@Test
	public void givenAPictureUrlNullWhenValidateThenServiceException() {
		//given
		when(request.getItemId()).thenReturn(ITEM_ID);
		when(itemDao.findOne(ITEM_ID)).thenReturn(item);
		when(item.getPicture1Url()).thenReturn(null);
		
		try {
			//when
			validator.validate(request);
			fail();
		} catch(ServiceException e) {
			//then
			assertEquals(ServiceExceptionCode.MANDATORY_PICTURE_NOT_UPLOADED.getCode(), e.getCode());
		}
	}

}
