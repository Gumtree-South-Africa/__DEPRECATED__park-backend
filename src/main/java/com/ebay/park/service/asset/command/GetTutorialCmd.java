package com.ebay.park.service.asset.command;

import com.ebay.park.db.dao.TutorialDao;
import com.ebay.park.db.entity.Tutorial;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.asset.dto.GetTutorialRequest;
import com.ebay.park.service.asset.dto.GetTutorialResponse;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.session.dto.UserSessionCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.ebay.park.service.ServiceException.createServiceException;

/**
 * 
 * @author marcos.lambolay
 *
 */
@Component
public class GetTutorialCmd implements
		ServiceCommand<GetTutorialRequest, GetTutorialResponse> {

	@Autowired
	private TutorialDao tutorialDao;

	@Autowired
	private SessionService sessionService;

	@Override
	public GetTutorialResponse execute(GetTutorialRequest request)
			throws ServiceException {

		UserSessionCache userSession = sessionService.getUserSession(request
				.getToken());

		Tutorial tutorial = tutorialDao.findByStepAndLang(request.getStep(),
				userSession.getLang());

		if (tutorial == null) {
			throw createServiceException(ServiceExceptionCode.TUTORIAL_STEP_NOT_FOUND);
		}

		return new GetTutorialResponse(tutorial.getPicture(),
				tutorial.getStep());
	}

}
