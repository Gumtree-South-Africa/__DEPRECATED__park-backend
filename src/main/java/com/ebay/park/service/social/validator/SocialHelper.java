package com.ebay.park.service.social.validator;

import com.ebay.park.db.dao.SocialDao;
import com.ebay.park.db.entity.Social;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import static com.ebay.park.service.ServiceException.createServiceException;

/**
 * Helper for social related processing.
 * @author Julieta Salvadó
 */
@Component
public class SocialHelper {

    @Autowired
    private SocialDao socialDao;

    /**
     * It searches for Social.
     * @param socialNetwork social network name to search for
     * @return  the social
     * @throws ServiceException with code INVALID_SOCIAL_NETWORK
     */
    public Social findSocialByDescription(String socialNetwork) {
        Social social = socialDao.findByDescription(socialNetwork);

        if (social == null) {
            throw createServiceException(ServiceExceptionCode.INVALID_SOCIAL_NETWORK);
        }
        return social;
    }
}
