package com.ebay.park.service.group.command;

import com.ebay.park.db.dao.GroupDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Group;
import com.ebay.park.db.entity.User;
import com.ebay.park.eps.EPSClient;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.group.dto.UploadGroupPhotoRequest;
import com.ebay.park.util.EPSUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.ebay.park.service.ServiceException.createServiceException;

@Component
public class UploadGroupPhotoCmd implements
		ServiceCommand<UploadGroupPhotoRequest, ServiceResponse> {

	@Autowired
	private GroupDao groupDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private EPSUtils EPSUtils;

	@Autowired
	EPSClient epsClient;

	private static Logger logger = LoggerFactory.getLogger(UploadGroupPhotoCmd.class);

	private final DateTimeFormatter fmt = DateTimeFormat
			.forPattern("yyyy-MM-dd'T'HH:mm:ssZ");

	@Override
	public ServiceResponse execute(UploadGroupPhotoRequest request)
			throws ServiceException {

		User user = userDao.findByToken(request.getToken());

		if (user == null) {
			throw createServiceException(ServiceExceptionCode.USER_UNAUTHORIZED);
		}

		Group group = groupDao.findOne(request.getGroupId());

		if (group == null) {
			throw createServiceException(ServiceExceptionCode.INVALID_GROUP);
		}

		try {
			String fileName = createPhotoFileName(user.getUsername(), group.getId());
            logger.info("Try to Upload Group's Picture into EPS. Original Picture Name: {}", fileName);
			String url = epsClient.publish(fileName,request.getPhoto());
			group.setPicture(url);
			groupDao.save(group);
		}  catch (Exception e) {
			throw new RuntimeException(e);
		}

		return ServiceResponse.SUCCESS;
	}

	private String createPhotoFileName(String username, Long id) {
		return EPSUtils.getPrefix() + username + "/group/" + id + "/picture_"
				+ DateTime.now().toString(fmt);
	}

}
