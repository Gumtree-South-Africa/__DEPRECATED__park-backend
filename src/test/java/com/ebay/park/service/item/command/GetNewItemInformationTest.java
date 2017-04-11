package com.ebay.park.service.item.command;

import static org.junit.Assert.*;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.ebay.park.db.dao.ItemGroupDao;
import com.ebay.park.db.dao.UserFollowsGroupDao;
import com.ebay.park.db.entity.ItemGroup;
import com.ebay.park.db.entity.UserFollowsGroup;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.item.dto.GetNewItemInformationRequest;
import com.ebay.park.service.item.dto.ItemSummary;

public class GetNewItemInformationTest {
	
	@InjectMocks
	private GetNewItemInformationCmd getNewItemInformationCmd;
	
	@Mock
	private UserFollowsGroupDao userFollowsGroupDao;
	
	@Mock
	private ItemGroupDao itemGroupDao;
	
	@Before
	public void setUp() {
		getNewItemInformationCmd = new GetNewItemInformationCmd();
		initMocks(this);
	}
	
	@Test
	public void testExecuteSuccess(){
		//given
		GetNewItemInformationRequest request = Mockito.mock(GetNewItemInformationRequest.class);
		UserFollowsGroup userFollowsGroup = Mockito.mock(UserFollowsGroup.class);
		ItemSummary itemSummary = new ItemSummary();
		itemSummary.setId(1l);
		List<ItemSummary> itemSummries= new ArrayList<>();
		itemSummries.add(itemSummary);
		ItemGroup itemGroup = Mockito.mock(ItemGroup.class);
		Mockito.when(request.getItemsDTO()).thenReturn(itemSummries);
		Mockito.when(request.getGroupId()).thenReturn(1l);
		Mockito.when(request.getUserId()).thenReturn(2l);
		Mockito.when(userFollowsGroupDao.find(request.getGroupId(), request.getUserId())).thenReturn(userFollowsGroup);
		Mockito.when(itemGroupDao.find(itemSummary.getId(), request.getGroupId())).thenReturn(itemGroup);
		//when
		ServiceResponse response = getNewItemInformationCmd.execute(request);
		//then
		assertNotNull(response);
		assertEquals(ServiceResponse.SUCCESS.getStatusCode(), response.getStatusCode());
		Mockito.verify(itemGroupDao, Mockito.times(1)).find(itemSummary.getId(), request.getGroupId());
		Mockito.verify(userFollowsGroupDao, Mockito.times(1)).find(request.getGroupId(), request.getUserId());
	}
	
	@Test
	public void testExecuteUserFollowsGroupNullSuccess(){
		//given
		GetNewItemInformationRequest request = Mockito.mock(GetNewItemInformationRequest.class);
		ItemSummary itemSummary = new ItemSummary();
		itemSummary.setId(1l);
		List<ItemSummary> itemSummries= new ArrayList<>();
		itemSummries.add(itemSummary);
		ItemGroup itemGroup = Mockito.mock(ItemGroup.class);
		Mockito.when(request.getItemsDTO()).thenReturn(itemSummries);
		Mockito.when(request.getGroupId()).thenReturn(1l);
		Mockito.when(request.getUserId()).thenReturn(2l);
		Mockito.when(userFollowsGroupDao.find(request.getGroupId(), request.getUserId())).thenReturn(null);
		Mockito.when(itemGroupDao.find(itemSummary.getId(), request.getGroupId())).thenReturn(itemGroup);
		//when
		ServiceResponse response = getNewItemInformationCmd.execute(request);
		//then
		assertNotNull(response);
		assertEquals(ServiceResponse.SUCCESS.getStatusCode(), response.getStatusCode());
		Mockito.verify(userFollowsGroupDao, Mockito.times(1)).find(request.getGroupId(), request.getUserId());
	}

}
