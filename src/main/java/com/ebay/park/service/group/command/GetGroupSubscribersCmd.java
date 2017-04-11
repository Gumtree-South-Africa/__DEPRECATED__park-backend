package com.ebay.park.service.group.command;

import com.ebay.park.db.dao.GroupDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Group;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.group.dto.GetGroupSubscribersRequest;
import com.ebay.park.service.social.dto.BasicUser;
import com.ebay.park.service.social.dto.SearchUserResponse;
import com.ebay.park.util.InternationalizationUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.ebay.park.service.ServiceException.createServiceException;

@Component
public class GetGroupSubscribersCmd implements
		ServiceCommand<GetGroupSubscribersRequest, SearchUserResponse> {

	private static final String EMPTY_LIST_GROUP_SUBSCRIBERS = "emptylist.group_subscribers";

	@Autowired
	GroupDao groupDao;

	@Autowired
	UserDao userDao;

	@Value("${search.pageSize}")
	private int defaultPageSize;

	@Autowired
	private InternationalizationUtil i18nUtil;
	
	@Override
	public SearchUserResponse execute(GetGroupSubscribersRequest request)
			throws ServiceException {

		int pageSize = calculatePageSize(request.getPageSize());
		int pageIndex = calculatePageIndex(request.getPage());

		Group group = groupDao.findOne(request.getGroupId());
		if (group == null) {
			throw createServiceException(ServiceExceptionCode.INVALID_GROUP);
		}

		User user = null;
		if (!StringUtils.isBlank(request.getToken())) {
			user = userDao.findByToken(request.getToken());
			if (user == null) {
				throw createServiceException(ServiceExceptionCode.USER_UNAUTHORIZED);
			}
		}

		PageRequest pageReq = new PageRequest(pageIndex, pageSize);

		Page<User> list = userDao.findByGroup(group.getId(), pageReq);

		List<BasicUser> users = new ArrayList<>();
		for (User u : list.getContent()) {
			BasicUser basicUser = new BasicUser(u);
			if (user != null){
				basicUser.setFollowedByUser(u.isFollowedByUser(user));
			}
			users.add(basicUser);
		}

		SearchUserResponse response = new SearchUserResponse(users,
				list.getTotalElements());

		i18nUtil.internationalizeListedResponse(response, EMPTY_LIST_GROUP_SUBSCRIBERS, request.getLanguage());
		
		return response;

	}

	private int calculatePageIndex(Integer page) {
		if (page == null || page <= 0) {
			return 0;
		}
		return page;
	}

	private int calculatePageSize(Integer pageSize) {
		if (pageSize == null || pageSize <= 0) {
			return defaultPageSize;
		}

		return pageSize;
	}

}
