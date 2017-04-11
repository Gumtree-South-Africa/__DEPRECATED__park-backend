package com.ebay.park.db.dao;

import com.ebay.park.db.entity.ItemGroup;
import com.ebay.park.db.entity.ItemGroupPK;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * 
 * @author marcos.lambolay
 *
 */
public interface ItemGroupDao 
	extends JpaRepository<ItemGroup, ItemGroupPK>{

	@Query("select it_gr from ItemGroup it_gr where it_gr.item.id = :itemId and it_gr.group.id = :groupId ")
	public ItemGroup find(@Param("itemId") Long itemId, @Param("groupId") Long groupId);	
}
