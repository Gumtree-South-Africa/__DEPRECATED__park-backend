package com.ebay.park.db.dao;

import com.ebay.park.db.entity.Conversation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


/**
 * Data access interface for {@link Conversation} entity.
 * @author marcos.lambolay
 */
public interface ConversationDao extends JpaRepository<Conversation, Long> {

	static final String STATUS_FILTER = "(c.item.status = 1 or c.item.status = 2 or c.item.status = 3)";
	
	@Query(value="select c from Conversation c where c.seller.id = :userId "
			+ " and "+STATUS_FILTER+"  and c.item.deleted = false order by c.id DESC")
	public List<Conversation> findConversationsForSeller(@Param("userId") Long userId, Pageable p);

	@Query(value="select count(c) from Conversation c where c.seller.id = :userId "
			+ " and "+STATUS_FILTER+" and c.item.deleted = false ")
	public int findConversationsForSellerCount(@Param("userId") Long userId);
	
	@Query(value="select c from Conversation c where c.buyer.id = :userId "
			+ " and "+STATUS_FILTER+" and c.item.deleted = false order by c.id DESC")
	public List<Conversation> findConversationsForBuyer(@Param("userId") Long userId, Pageable p);

	@Query(value="select count(c) from Conversation c where c.buyer.id = :userId "
			+ " and "+STATUS_FILTER+" and c.item.deleted = false ")
	public int findConversationsForBuyerCount(@Param("userId") Long userId);
	
	@Query(value="select c from Conversation c where c.item.id = :itemId"
			+ " and "+STATUS_FILTER+" and c.item.deleted = false ")
	public List<Conversation> findConversationsByItemId(@Param("itemId") Long itemId, Pageable p);

	@Query(value="select count(c) from Conversation c where c.item.id = :itemId"
			+ " and "+STATUS_FILTER+" and c.item.deleted = false ")
	public int findConversationsByItemIdCount(@Param("itemId") Long itemId);
	
	@Query(value="select c from Conversation c where c.seller.id = :userId and c.item.id = :itemId "
			+ " and c.item.status <> 0 and c.item.deleted = false ")
	public List<Conversation> findConversationsByItemIdForSeller(@Param("userId") Long userId, @Param("itemId") Long itemId, Pageable p);
	
	@Query(value="select count(c) from Conversation c where c.seller.id = :userId and c.item.id = :itemId "
			+ " and c.item.status <> 0 and c.item.deleted = false ")
	public int findConversationsByItemIdForSellerCount(@Param("userId") Long userId, @Param("itemId") Long itemId);
	
	@Query(value="select c from Conversation c where c.buyer.id = :userId and c.item.id = :itemId "
			+ " and "+STATUS_FILTER+" and c.item.deleted = false ")
	public Conversation findConversationByItemIdForBuyer(@Param("userId") Long userId, @Param("itemId") Long itemId);
	
	@Query(value="select c from Conversation c where c.buyer.id = :userId and c.item.id = :itemId "
			+ " and "+STATUS_FILTER+" and c.item.deleted = false ")
	public List<Conversation> findConversationsByItemIdForBuyer(@Param("userId") Long userId, @Param("itemId") Long itemId, Pageable p);
	
	@Query(value="select count(c) from Conversation c where c.buyer.id = :userId and c.item.id = :itemId "
			+ " and "+STATUS_FILTER+" and c.item.deleted = false ")
	public int findConversationsByItemIdForBuyerCount(@Param("userId") Long userId, @Param("itemId") Long itemId);

}
