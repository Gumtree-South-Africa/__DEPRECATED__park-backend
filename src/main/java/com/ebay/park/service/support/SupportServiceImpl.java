package com.ebay.park.service.support;

import com.ebay.park.service.support.command.SendUserFeedbackCmd;
import com.ebay.park.service.support.dto.SendUserFeedbackRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SupportServiceImpl implements SupportService {

	@Autowired
	private SendUserFeedbackCmd sendUserFeedbackCmd;

	@Override
	public Void sendUserFeedback(SendUserFeedbackRequest request) {
		return sendUserFeedbackCmd.execute(request);
	}

}
