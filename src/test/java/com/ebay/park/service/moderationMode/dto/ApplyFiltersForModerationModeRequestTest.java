package com.ebay.park.service.moderationMode.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;

public class ApplyFiltersForModerationModeRequestTest {
    
    private static final String INVALID_DATE = "April 20 1985";
    private static final String MSG = "An exception was expected";

    @Test
    public void givenInvalidDateFromWhenValidatingThenException() {
        try {
            ApplyFiltersForModerationModeRequest request = new ApplyFiltersForModerationModeRequest();
            request.setItemLastUpdatedFrom(INVALID_DATE);
            fail(MSG);
        } catch (ServiceException e) {
            assertEquals(ServiceExceptionCode.WRONG_DATE_FORMAT.getCode(), e.getCode());
        }
    }
    
    @Test
    public void givenInvalidDateToWhenValidatingThenException() {
        try {  
            ApplyFiltersForModerationModeRequest request = new ApplyFiltersForModerationModeRequest();
            request.setItemLastUpdatedTo(INVALID_DATE);
            fail(MSG);
        } catch (ServiceException e) {
            assertEquals(ServiceExceptionCode.WRONG_DATE_FORMAT.getCode(), e.getCode());
        }
    }
}
