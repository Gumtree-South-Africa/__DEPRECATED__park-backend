package com.ebay.park.service.item.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.springframework.test.util.ReflectionTestUtils;

import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.item.dto.DeletePhotoRequest;

public class DeletePhotoValidatorTest {

    private static final String TOKEN = "token";

    private static final Long[] INVALID_PICTURE_IDS1 = {0l};
    private static final Long[] INVALID_PICTURE_IDS2 = {5l};
    private static final Long[] INVALID_PICTURE_IDS3 = {1l};
    private static final Long[] INVALID_PICTURE_IDS4 = {2l, 5l};
    private static final Long[] VALID_PICTURE_IDS1 = {4l, 2l};
    private static final Long[] VALID_PICTURE_IDS2 = {2l};

    private static final Long VALID_ID = null;

    private static final String MSG = "An exception is expected";;

    @InjectMocks
    @Spy
    private DeletePhotoValidator validator;
    
    @Before
    public void setUp() throws Exception{
        initMocks(this);
        ReflectionTestUtils.setField(validator, "max", 4);
    }

    @Test
    public void givenBeforeFirstPictureWhenValidatingThenException() {
        DeletePhotoRequest request = new DeletePhotoRequest(TOKEN);
        request.setItemId(VALID_ID);
        request.setPictureId(INVALID_PICTURE_IDS1);
        try {
            validator.validate(request);
            fail(MSG);
        } catch (ServiceException e) {
            assertEquals(e.getCode(), ServiceExceptionCode.INVALID_PICTURE_ID.getCode());
        }
    }

    @Test
    public void givenAfterLastPictureWhenValidatingThenException() {
        DeletePhotoRequest request = new DeletePhotoRequest(TOKEN);
        request.setItemId(VALID_ID);
        request.setPictureId(INVALID_PICTURE_IDS2);
        try {
            validator.validate(request);
            fail(MSG);
        } catch (ServiceException e) {
            assertEquals(e.getCode(), ServiceExceptionCode.INVALID_PICTURE_ID.getCode());
        }
    }

    @Test
    public void givenFirstPictureWhenValidatingThenException() {
        DeletePhotoRequest request = new DeletePhotoRequest(TOKEN);
        request.setItemId(VALID_ID);
        request.setPictureId(INVALID_PICTURE_IDS3);
        try {
            validator.validate(request);
            fail(MSG);
        } catch (ServiceException e) {
            assertEquals(e.getCode(), ServiceExceptionCode.CANNOT_DELETE_PICTURE.getCode());
        }
    }

    @Test
    public void givenInvalidPictureListWhenValidatingThenException() {
        DeletePhotoRequest request = new DeletePhotoRequest(TOKEN);
        request.setItemId(VALID_ID);
        request.setPictureId(INVALID_PICTURE_IDS4);
        try {
            validator.validate(request);
            fail(MSG);
        } catch (ServiceException e) {
            assertEquals(e.getCode(), ServiceExceptionCode.INVALID_PICTURE_ID.getCode());
        }
    }

    @Test
    public void givenValidPictureListWhenValidatingThenSuccess() {
        DeletePhotoRequest request = new DeletePhotoRequest(TOKEN);
        request.setItemId(VALID_ID);
        request.setPictureId(VALID_PICTURE_IDS1);
        validator.validate(request);
    }

    @Test
    public void givenValidUniquePictureWhenValidatingThenSuccess() {
        DeletePhotoRequest request = new DeletePhotoRequest(TOKEN);
        request.setItemId(VALID_ID);
        request.setPictureId(VALID_PICTURE_IDS2);
        validator.validate(request);
    }
}
