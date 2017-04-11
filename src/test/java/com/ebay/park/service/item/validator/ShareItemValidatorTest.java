package com.ebay.park.service.item.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import com.ebay.park.db.entity.Social;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.item.dto.ShareItemRequest;

public class ShareItemValidatorTest {
    private static final String TW = Social.TWITTER;
    private static final String FB = Social.FACEBOOK;
    private static final String MSG1 = "None exception is expected";
    private static final String MSG2 = "An exception is expected";
    private static final String OTHER = "other";
    
    @InjectMocks
    private ShareItemValidator validator;
    
    @Before
    public void setUp() throws Exception{
        initMocks(this);
    }
    
    @Test
    public void testTwitterValidate() {
        ShareItemRequest request = new ShareItemRequest();
        request.setSocialNetwork(TW);
        try {
            validator.validate(request);
        } catch (Exception e){
            fail(MSG1);
        }
    }
    
    @Test
    public void testFacebookValidate() {
        ShareItemRequest request = new ShareItemRequest();
        request.setSocialNetwork(FB);
        try {
            validator.validate(request);
        } catch (Exception e){
            fail(MSG1);
        }
    }
    
    @Test
    public void testInvalidSocialNetworkValidate() {
        ShareItemRequest request = new ShareItemRequest();
        request.setSocialNetwork(OTHER);
        try {
            validator.validate(request);
            fail(MSG2);
        } catch (ServiceException e){
            assertEquals(e.getCode(), ServiceExceptionCode.INVALID_SOCIAL_NETWORK.getCode());
        }
    }
}
