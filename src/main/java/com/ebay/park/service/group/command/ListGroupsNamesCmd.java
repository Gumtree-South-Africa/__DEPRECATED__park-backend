package com.ebay.park.service.group.command;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.ebay.park.db.dao.GroupDao;
import com.ebay.park.db.entity.Group;
import com.ebay.park.service.ParkRequest;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.group.dto.ListGroupsNamesResponse;
import com.ebay.park.service.group.dto.SmallGroupNameDTO;

/**
 * Creates a list of groups names and ids.
 * @author Julieta Salvadó
 *
 */
@Component
public class ListGroupsNamesCmd implements ServiceCommand<ParkRequest, ListGroupsNamesResponse> {

    @Autowired
    private GroupDao groupDao;

    @Override
    public ListGroupsNamesResponse execute(ParkRequest param)
            throws ServiceException {
        List<Group> groupList = groupDao.findAll(new Sort(Sort.Direction.ASC, "name"));
        return new ListGroupsNamesResponse(groupList.stream()
            .map(group -> SmallGroupNameDTO.fromGroup(group))
            .collect(Collectors.toList()));
    }

}
