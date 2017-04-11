package com.ebay.park.db.dao;


import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserFollowsItem;
import com.ebay.park.db.entity.UserFollowsItemPK;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * 
 * @author marcos.lambolay
 */
public interface UserFollowsItemDao extends JpaRepository<UserFollowsItem, UserFollowsItemPK> {

	@Query(value = "select count(ufi.user.id) from UserFollowsItem ufi where ufi.item.id = :itemId")
	public Long totalOfFollowersOfItem(@Param("itemId") Long itemId);
	
	public Page<UserFollowsItem> findByUser(User user, Pageable pageable);
}
