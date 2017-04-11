package com.ebay.park.service.blacklist.command;

import com.ebay.park.db.dao.BlackListDao;
import com.ebay.park.db.entity.BlackList;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.moderation.dto.BlacklistedWordRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.ebay.park.service.ServiceException.createServiceException;

@Component
public class RemoveBlacklistedWordImpl implements RemoveBlacklistedWordCmd {

	@Autowired
	private BlackListDao blackListDao;

	@Override
	public ServiceResponse execute(BlacklistedWordRequest request)
			throws ServiceException {

		BlackList blackListedtWord = blackListDao.findOne(request.getId());
		if (blackListedtWord == null) {
			throw createServiceException(ServiceExceptionCode.BLACKLIST_WORD_NOT_FOUND);
		}

		blackListDao.delete(blackListedtWord);
		return ServiceResponse.SUCCESS;
	}

}
