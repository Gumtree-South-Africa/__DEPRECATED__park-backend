package com.ebay.park.service.group.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.data.domain.Sort;

import com.ebay.park.db.dao.GroupDao;
import com.ebay.park.db.entity.Group;
import com.ebay.park.service.ParkRequest;
import com.ebay.park.service.group.dto.ListGroupsNamesResponse;
import com.ebay.park.service.group.dto.SmallGroupNameDTO;

/**
 * It tests istGroupsNamesCmd.
 * @author Julieta Salvadó
 *
 */
public class ListGroupsNamesCmdTest {
    
    private static final String LANG = "es";

    private static final String GROUP1_NAME = "g1";
    private static final String GROUP2_NAME = "g2";
    private static final String GROUP3_NAME = "g3";

    @InjectMocks
    @Spy
    private ListGroupsNamesCmd cmd;

    @Mock
    private GroupDao groupDao;
    
    @Before
    public void setUp(){
        initMocks(this);
    }
    
    @Test
    public void givenExistingGroupsWhenExecutingThenGetList() {
        Group group1 = mock(Group.class);
        Group group2 = mock(Group.class);
        Group group3 = mock(Group.class);

        when(group1.getName()).thenReturn(GROUP1_NAME);
        when(group2.getName()).thenReturn(GROUP2_NAME);
        when(group3.getName()).thenReturn(GROUP3_NAME);
        when(groupDao.findAll(new Sort(Sort.Direction.ASC, "name"))).thenReturn(Arrays.asList(group1, group2, group3));

        ListGroupsNamesResponse response = cmd.execute(new ParkRequest(null, LANG));
        assertEquals(3, response.getNumberOfElements());

        Optional<SmallGroupNameDTO> group1Exists = response.getGroups().stream()
            .filter(dto -> dto.getName() == GROUP1_NAME)
            .findFirst();
        assertTrue(group1Exists.isPresent());
        
        Optional<SmallGroupNameDTO> group2Exists = response.getGroups().stream()
                .filter(dto -> dto.getName() == GROUP2_NAME)
                .findFirst();
        assertTrue(group2Exists.isPresent());
            
        Optional<SmallGroupNameDTO> group3Exists = response.getGroups().stream()
                .filter(dto -> dto.getName() == GROUP3_NAME)
                .findFirst();
        assertTrue(group3Exists.isPresent()); 
    }
    
    @Test
    public void givenNoneGroupWhenExecutingThenGetEmptyList() {
        List<Group> list = new ArrayList<Group>();
        when(groupDao.findAll()).thenReturn(list);
        ListGroupsNamesResponse response = cmd.execute(new ParkRequest(null, LANG));
        assertEquals(0, response.getNumberOfElements());
    }
}
