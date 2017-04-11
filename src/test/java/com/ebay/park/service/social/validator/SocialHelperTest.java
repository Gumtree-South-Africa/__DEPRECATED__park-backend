package com.ebay.park.service.social.validator;

import com.ebay.park.db.dao.SocialDao;
import com.ebay.park.db.entity.Social;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.notification.NotificationServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Unit test for {@link SocialHelper}.
 * @author Julieta Salvadó
 */
public class SocialHelperTest {

    private static final String SOCIAL_NETWORK = "social network";
    @InjectMocks
    private SocialHelper helper;

    @Mock
    private SocialDao socialDao;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void givenInvalidSocialNetworkWhenFindingSocialByDescriptionThenException() {
        when(socialDao.findByDescription(SOCIAL_NETWORK)).thenReturn(null);
        try {
            helper.findSocialByDescription(SOCIAL_NETWORK);
        } catch (ServiceException e) {
            assertThat(e.getCode(), is(ServiceExceptionCode.INVALID_SOCIAL_NETWORK.getCode()));
        }
    }

    @Test
    public void givenValidSocialNetworkWhenFindingSocialByDescriptionThenException() {
        Social social = mock(Social.class);
        when(socialDao.findByDescription(SOCIAL_NETWORK)).thenReturn(social);
        Social response = helper.findSocialByDescription(SOCIAL_NETWORK);
        assertThat(response, is(social));
    }

}