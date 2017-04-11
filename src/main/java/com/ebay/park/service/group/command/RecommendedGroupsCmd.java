package com.ebay.park.service.group.command;

import com.ebay.park.db.dao.GroupDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Group;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.ParkRequest;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.group.dto.SearchGroupResponse;
import com.ebay.park.service.item.dto.SmallGroupDTO;
import com.ebay.park.util.InternationalizationUtil;
import com.ebay.park.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.ebay.park.service.ServiceException.createServiceException;
/**
 * 
 * @author gabriel.sideri
 */
@Component
public class RecommendedGroupsCmd implements
		ServiceCommand<ParkRequest, SearchGroupResponse> {

	private static final String NO_RESULTS_MESSAGE = "emptylist.recommended_groups";

	@Value("${list.recommended_groups_max}")
	private int LIST_RECOMMENDED_GROUPS_MAX;

	@Autowired
	private UserDao userDao;

	@Autowired
	private InternationalizationUtil i18nUtil;

	@Autowired
	private GroupDao groupDao;
	
	@Autowired
	private TextUtils textUtils;

	@Override
	public SearchGroupResponse execute(ParkRequest request)
			throws ServiceException {
		User user = null;
		if (request.getToken() != null) {
			user = userDao.findByToken(request.getToken());
			if (user == null) {
				throw createServiceException(ServiceExceptionCode.USER_UNAUTHORIZED);
			}
		}

		List<Group> groups = groupDao.getRecommendedGroup(new PageRequest(0,
				LIST_RECOMMENDED_GROUPS_MAX));
		
		ArrayList<SmallGroupDTO> groupsDTO = new ArrayList<SmallGroupDTO>(groups
				.size());
		if (user != null) {
			for (Group group : groups) {
				SmallGroupDTO smallGroup = SmallGroupDTO.fromGroup(group);
				smallGroup.setSubscribed(user.isSubscribedToGroup(group));
				smallGroup.setURL(textUtils.createGroupSEOURL(
						smallGroup.getName(), smallGroup.getId()));
				groupsDTO.add(smallGroup);
			}
		} else {
			for (Group group : groups) {
				SmallGroupDTO smallGroup = SmallGroupDTO.fromGroup(group);
				smallGroup.setURL(textUtils.createGroupSEOURL(
						smallGroup.getName(), smallGroup.getId()));
				groupsDTO.add(smallGroup);
			}
		}
		

		SearchGroupResponse response = new SearchGroupResponse(groupsDTO,
				groupsDTO.size());
		
		//language definition
		String language = request.getLanguage();
		if ((language == null) && (user != null)) {
			language = user.getIdiom().getCode();
		} 
	
		i18nUtil.internationalizeListedResponse(response, NO_RESULTS_MESSAGE,
				language);
			
		return response;
	}

}