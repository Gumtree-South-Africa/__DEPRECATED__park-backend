package com.ebay.park.service.item.command;

import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.dao.UserReportItemDao;
import com.ebay.park.db.entity.*;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.item.dto.ReportItemRequest;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.ebay.park.service.ServiceException.createServiceException;

/**
 * Command to create a Report by the user on a item
 * 
 * @author cbirge
 * 
 */
@Component
public class UserReportItemCmd implements
		ServiceCommand<ReportItemRequest, ServiceResponse> {

	@Autowired
	private UserDao userDao;

	@Autowired
	private ItemDao itemDao;

	@Autowired
	private UserReportItemDao userReportItemDao;

	@Override
	public ServiceResponse execute(ReportItemRequest request)
			throws ServiceException {

		User userReporter = userDao.findByToken(request.getToken());
		if (userReporter == null) {
			throw createServiceException(ServiceExceptionCode.USER_UNAUTHORIZED);
		}

		Item itemReported = itemDao.findOne(request.getItemId());
		if (itemReported == null) {
			throw createServiceException(ServiceExceptionCode.ITEM_NOT_FOUND);
		}

		if (itemReported.is(StatusDescription.EXPIRED)) {
			throw createServiceException(ServiceExceptionCode.ITEM_EXPIRED);
		}

		UserReportItem report = userReportItemDao.findUserReportForItem(
				userReporter.getId(), itemReported.getId());
		if (report != null) {
			throw createServiceException(ServiceExceptionCode.ITEM_ALREADY_REPORTED_FOR_USER);
		}

		if (itemReported.getPublishedBy().getId().equals(userReporter.getId())) {
			throw createServiceException(ServiceExceptionCode.ITEM_REPORTED_BELONGS_USER);
		}

		UserReportItemPK reportId = new UserReportItemPK();
		reportId.setUserReporterId(userReporter.getId());
		reportId.setItemReportedId(itemReported.getId());

		UserReportItem userReportItem = new UserReportItem();
		userReportItem.setId(reportId);
		userReportItem.setUserComment(request.getComment());
		userReportItem.setReportDate(DateTime.now().toDate());

		userReportItemDao.save(userReportItem);

		if (itemReported.getCountOfReports() != null
				&& itemReported.getCountOfReports() > 0) {
			itemReported
					.setCountOfReports(itemReported.getCountOfReports() + 1);

		} else {
			itemReported.setCountOfReports(1);
		}
		itemDao.save(itemReported);

		return ServiceResponse.SUCCESS;
	}
}