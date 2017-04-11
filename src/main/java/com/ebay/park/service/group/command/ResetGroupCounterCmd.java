package com.ebay.park.service.group.command;

import static com.ebay.park.service.ServiceException.createServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.dao.UserFollowsGroupDao;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserFollowsGroup;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.group.dto.ResetGroupCounterRequest;
import com.ebay.park.util.DataCommonUtil;
import org.springframework.util.Assert;

@Component
public class ResetGroupCounterCmd implements ServiceCommand<ResetGroupCounterRequest, ServiceResponse> {

    @Autowired
    private UserFollowsGroupDao userFollowsGroupDao;

    @Autowired
    private UserDao userDao;

    @Override
    public ServiceResponse execute(ResetGroupCounterRequest request)
            throws ServiceException {
        Assert.notNull(request, "'request' must be not null");
        User user = userDao.findByToken(request.getToken());

        if (user == null) {
            throw createServiceException(ServiceExceptionCode.USER_UNAUTHORIZED);
        }

        UserFollowsGroup element = userFollowsGroupDao.find(request.getGroupId(), user.getId());
        if (element != null) {
            element.setLastAccess(DataCommonUtil.getCurrentTime());
            userFollowsGroupDao.save(element);

            return ServiceResponse.SUCCESS;
        }

        return ServiceResponse.FAIL;
    }
}
