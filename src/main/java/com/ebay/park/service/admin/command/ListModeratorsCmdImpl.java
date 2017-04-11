package com.ebay.park.service.admin.command;

import com.ebay.park.db.dao.UserAdminDao;
import com.ebay.park.db.entity.UserAdmin;
import com.ebay.park.service.PaginatedRequest;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.admin.dto.ListModeratorsResponse;
import com.ebay.park.service.admin.dto.SmallUserAdmin;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.session.dto.UserSessionCache;
import com.ebay.park.util.InternationalizationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ListModeratorsCmdImpl implements ListModeratorsCmd {

	@Value("${search.pageSize}")
	private int defaultPageSize;

	@Autowired
	private UserAdminDao userAdminDao;

	@Autowired
	private SessionService sessionService;

	@Autowired
	private InternationalizationUtil i18nUtil;

	@Override
	public ListModeratorsResponse execute(PaginatedRequest request) throws ServiceException {

		ListModeratorsResponse response = null;

		if (request.getPage() != null || request.getPageSize() != null) {
            int page = request.getPage() != null ? request.getPage() : 0;
            int pageSize = request.getPageSize() != null  ? request.getPageSize() : defaultPageSize;

			Sort sort = new Sort(Sort.Direction.DESC, "username");
			Pageable pageable = new PageRequest(page, pageSize, sort);

			Page<UserAdmin> adminPage = userAdminDao.findAll(pageable);
		    response = new ListModeratorsResponse(convertToDTO(adminPage.getContent()), adminPage.getTotalElements());

		} else {
			List<UserAdmin> adminList = userAdminDao.findAll();
			response = new ListModeratorsResponse(convertToDTO(adminList), adminList.size());
		}

		if (response.listIsEmpty()) {
			UserSessionCache session = sessionService.getUserSession(request.getToken());
			i18nUtil.internationalizeListedResponse(response, "emptylist.moderator_list",
					session.getLang());
		}
		return response;
	}

	private List<SmallUserAdmin> convertToDTO(List<UserAdmin> moderators) {
		List<SmallUserAdmin> result = new ArrayList<>();
		if (moderators != null) {
			for (UserAdmin moderator : moderators) {
				result.add(new SmallUserAdmin(moderator));
			}
		}
		return result;
	}
}