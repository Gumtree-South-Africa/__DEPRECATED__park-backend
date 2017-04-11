package com.ebay.park.db.dao;

import com.ebay.park.db.entity.Chat;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * Data access interface for {@link Chat} entity.
 * 
 * @author marcos.lambolay
 */
public interface ChatDao extends JpaRepository<Chat, Long> {

	@Query(value = "select c from Chat c where c.conversation.id = :conversationId order by c.postTime DESC")
	public List<Chat> findLastChatsOfConversation(
			@Param("conversationId") Long conversationId, Pageable p);

	@Query(value = "select count(c.id) from Chat c join c.conversation conv join conv.item it where conv.id = :conversationId and it.deleted = false and (it.status = 1 or it.status = 2) and c.postTime > :d")
	public Long findForConversationChatsMoreRecentThan(
			@Param("conversationId") Long conversationId, @Param("d") Date d);
	
	@Deprecated
	@Query(value = "select count(c.id) from Chat c join c.conversation conv join conv.item it where conv.seller.id = :userId and c.receiver.id = :userId and it.deleted = false and (it.status = 1 or it.status = 2) and c.postTime > :d")
	public Long findForSellerChatsMoreRecentThan(@Param("userId") Long userId,
			@Param("d") Date d);
	
}
