package com.ebay.park.service.blacklist.command;

import com.ebay.park.db.dao.BlackListDao;
import com.ebay.park.db.entity.BlackList;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.blacklist.dto.BlacklistedWord;
import com.ebay.park.service.moderation.dto.BlacklistedWordRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.ebay.park.service.ServiceException.createServiceException;

@Component
public class AddBlacklistedWordImpl implements AddBlacklistedWordCmd {

	@Autowired
	private BlackListDao blackListDao;

	@Override
	public BlacklistedWord execute(BlacklistedWordRequest request)
			throws ServiceException {

		BlackList checkIfExists = blackListDao.findByDescription(request
				.getWord());
		if (checkIfExists != null) {
			throw createServiceException(ServiceExceptionCode.BLACKLIST_WORD_ALREADY_EXISTS);
		}

		BlackList blackListedtWord = new BlackList();
		blackListedtWord.setDescription(request.getWord());
		blackListDao.save(blackListedtWord);

		return new BlacklistedWord(blackListedtWord.getId(),
				blackListedtWord.getDescription());
	}

}
