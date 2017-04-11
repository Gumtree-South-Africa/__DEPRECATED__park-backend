package com.ebay.park.service.item.command;

import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.dao.UserReportItemDao;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.StatusDescription;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserReportItem;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.item.dto.UserItemRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.ebay.park.service.ServiceException.createServiceException;


/**
 * Command to remove a Report created by the user on a item
 * 
 * @author cbirge
 * 
 */
@Component
public class UserUnreportItemCmd implements
		ServiceCommand<UserItemRequest, ServiceResponse> {

	@Autowired
	private UserDao userDao;

	@Autowired
	private ItemDao itemDao;

	@Autowired
	private UserReportItemDao userReportItemDao;

	@Override
	public ServiceResponse execute(UserItemRequest request)
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
		if (report == null) {
			throw createServiceException(ServiceExceptionCode.ITEM_REPORT_NOT_FOUND);
		}

		userReportItemDao.delete(report);

		if (itemReported.getCountOfReports() != null
				&& itemReported.getCountOfReports() > 0) {
			itemReported
					.setCountOfReports(itemReported.getCountOfReports() - 1);

		} else {
			itemReported.setCountOfReports(0);
		}
		itemDao.save(itemReported);

		return ServiceResponse.SUCCESS;
	}
}