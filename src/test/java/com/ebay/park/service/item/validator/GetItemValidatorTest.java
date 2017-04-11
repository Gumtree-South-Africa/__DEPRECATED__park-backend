package com.ebay.park.service.item.validator;

import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import com.ebay.park.service.item.dto.GetItemRequest;
import com.ebay.park.util.ParkConstants;

public class GetItemValidatorTest {
    
    private static final String USER_TOKEN = "2234";
    private static final String LANG = ParkConstants.DEFAULT_LANGUAGE;
    private static final String ITEM_ID = "1";
    
    @InjectMocks
    private GetItemValidator validator;
    
    @Before
    public void setUp() throws Exception{
        initMocks(this);
    }
    
    @Test
    public void testValidate() {
        GetItemRequest request = new GetItemRequest(ITEM_ID, USER_TOKEN, LANG);
        validator.validate(request);
    }
}
