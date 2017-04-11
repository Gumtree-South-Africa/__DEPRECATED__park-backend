package com.ebay.park.db.dao;

import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.StatusDescription;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * Data access interface for the {@link Item} entity.
 *
 * @author marcos.lambolay
 */
public interface ItemDao extends JpaRepository<Item, Long> {

	@Query(value = "select it from Item it where it.publishedBy.id = :userId and it.status in "
			+ "(:status) and it.deleted = false and it.id <> :itemExcludedId order by it.lastModificationDate desc ")
	public Page<Item> listItemsFromUserId(@Param("userId") Long userId, @Param("itemExcludedId") Long itemId,
			@Param("status") StatusDescription[] itemStatus, Pageable pageable);

	@Query(value = "select it from Item it where it.publishedBy.id = :userId and it.status in "
			+ "(:status) and it.deleted = false and it.id <> :itemExcludedId order by it.lastModificationDate desc ")
	public List<Item> listItemsFromUserId(@Param("userId") Long userId, @Param("itemExcludedId") Long itemId,
			@Param("status") StatusDescription[] itemStatus);

	@Query(value = "select it from Item it where it.publishedBy.username = :username and it.status in (:status) and it.deleted = false")
	public Page<Item> listItemsFromUser(@Param("username") String username,
			@Param("status") StatusDescription[] itemStatus, Pageable pageable);

	@Query(value = "select it from Item it where it.publishedBy.username = :username and it.status in (:status) and it.deleted = false and it.id <> :itemIdExcluded")
    public Page<Item> listItemsFromUser(@Param("username") String username,
            @Param("status") StatusDescription[] itemStatus, Pageable pageable,
            @Param("itemIdExcluded") Long itemIdExcluded);

	@Query(value = "select it from Item it where it.publishedBy.username = :username and it.status in (:status) and it.deleted = false and it.created <= :created")
	public Page<Item> listItemsFromUser(@Param("username") String username,@Param("created") Date created,
			@Param("status") StatusDescription[] itemStatus, Pageable pageable);

	@Query(value = "select it from Item it where it.publishedBy.username = :username and it.status in (:status) and it.deleted = false and it.created <= :created and it.id <> :itemIdExcluded")
    public Page<Item> listItemsFromUser(@Param("username") String username,@Param("created") Date created,
            @Param("status") StatusDescription[] itemStatus, Pageable pageable,
            @Param("itemIdExcluded") Long itemIdExcluded);

	@Query(value = "select it from Item it where it.publishedBy.username = :username and it.status in (:status) and it.deleted = false")
	public List<Item> listItemsFromUser(@Param("username") String username,
			@Param("status") StatusDescription[] itemsStatus);

	@Query(value = "select it from Item it where it.publishedBy.username = :username and it.status in (:status) and it.deleted = false and it.id <> :itemIdExcluded")
    public List<Item> listItemsFromUser(@Param("username") String username,
            @Param("status") StatusDescription[] itemsInStatus,
            @Param("itemIdExcluded") Long itemIdExcluded);

	@Query(value = "select it from Item it where it.category.id = :categoryId and it.status in (:status) and it.deleted = false")
	public List<Item> listItemsByCategory(@Param("categoryId") Long categoryId,
			@Param("status") StatusDescription[] itemsStatus, Pageable pageable);

	@Query(value = "select distinct it from Item it LEFT JOIN it.followers fol_ite LEFT JOIN it.publishedBy.followers fol_use where it.status in (:status) and (fol_ite.user.id = :userId or fol_use.id.followerId = :userId) and it.deleted = false")
	public Page<Item> listItemsFollowed(@Param("userId") Long userId,
			@Param("status") StatusDescription[] itemStatus, Pageable pageable);
	
	@Query(value = "select distinct it from Item it LEFT JOIN it.followers fol_ite LEFT JOIN it.publishedBy.followers fol_use where it.status in (:status) and (fol_ite.user.id = :userId or fol_use.id.followerId = :userId) and it.deleted = false and it.created <= :created")
	public Page<Item> listItemsFollowed(@Param("userId") Long userId, @Param("created") Date created,
			@Param("status") StatusDescription[] itemStatus, Pageable pageable);

	@Query(value = "select distinct i from Item i "
            + "join i.itemGroups itemGroup "
            + "join itemGroup.group g "
            + "join g.followers follower "
            + "where follower.user.id = :userId and i.status in (:status) and i.deleted = false")
	public Page<Item> listItemsInGroupsFollowedByUser(
			@Param("userId") Long userId,
			@Param("status") StatusDescription[] status, Pageable pageable);

	@Query(value = "select distinct i from Item i "
            + "join i.itemGroups itemGroup "
            + "join itemGroup.group g "
            + "join g.followers follower "
            + "where follower.user.id = :userId and i.status in (:status) and i.deleted = false and i.created <= :created")
	public Page<Item> listItemsInGroupsFollowedByUser(
			@Param("userId") Long userId,
			@Param("status") StatusDescription[] status, Pageable pageable, @Param("created") Date created);
	
	@Override
	@Query(value = "select it from Item it "
			+ "where it.id = :itemId and it.deleted = false")
	public Item findOne(@Param("itemId") Long id);

	@Override
	@Query(value = "select it from Item it " + "where it.deleted = false")
	public List<Item> findAll();

	@Query(value = "select i from Item i where status = :activeStatus and deleted = false and lastModificationDate < :limitDate")
	public Page<Item> listElementsToExpire(
			@Param("activeStatus") StatusDescription activeStatus,
			@Param("limitDate") Date limitDate, Pageable pageable);

    @Query(value = "select i from Item i where status = :activeStatus and deleted = false and lastModificationDate < :upperDate and lastModificationDate > :lowerDate")
    public Page<Item> listElementsCloseToExpire(
            @Param("activeStatus") StatusDescription activeStatus,
            @Param("lowerDate") Date lowerDate,
            @Param("upperDate") Date upperDate, Pageable pageable);

	@Query(value = "select i from Item i where deleted = false and lastModificationDate < :limitDate and status = :deletionStatus")
	public Page<Item> listElementsToDelete(
			@Param("deletionStatus") StatusDescription deletionStatus,
			@Param("limitDate") Date limitDate, Pageable pageable);
	
	
	@Query(value =   "SELECT it FROM Item it left outer join it.followers ui left outer join ui.user itemFollower "
					+ "WHERE (SQRT(POW(69.1 * (it.latitude - (:userLatitude)), 2) + POW(69.1 * ((:userLongitude) - it.longitude) * COS(it.latitude / 57.3), 2))) < :radius "
					+ "AND it.publishedBy.id != :userId "
					+ "AND it.status = :activeStatus and it.deleted = false "
					+ "GROUP BY it.id "
					+ "ORDER BY COUNT(itemFollower.id) DESC")
	public List<Item> getRecommendedItems(
            @Param("userId") Long userId,
			@Param("activeStatus") StatusDescription activeStatus,
			@Param("userLatitude") Double latitude,
			@Param("userLongitude") Double longitude, 
			@Param("radius") Double radius,
			Pageable pageable);
	
	@Query(value = "SELECT it.id FROM Item it left outer join it.followers ui left outer join ui.user itemFollower "
			+ "WHERE (SQRT(POW(69.1 * (it.latitude - (:userLatitude)), 2) + POW(69.1 * ((:userLongitude) - it.longitude) * COS(it.latitude / 57.3), 2))) < :radius "
			+ "AND it.publishedBy.id != :userId " + "AND it.status = :activeStatus and it.deleted = false "
			+ "GROUP BY it.id " + "ORDER BY COUNT(itemFollower.id) DESC")
	public List<Long> getRecommendedItemIds(@Param("userId") Long userId,
			@Param("activeStatus") StatusDescription activeStatus, @Param("userLatitude") Double latitude,
			@Param("userLongitude") Double longitude, @Param("radius") Double radius, Pageable pageable);

	@Query(value = "SELECT it FROM Item it left outer join it.followers ui left outer join ui.user itemFollower "
			+ "WHERE (SQRT(POW(69.1 * (it.latitude - (:userLatitude)), 2) + POW(69.1 * ((:userLongitude) - it.longitude) * COS(it.latitude / 57.3), 2))) < :radius "
			+ "AND it.status = :activeStatus and it.deleted = false " + "GROUP BY it.id "
			+ "ORDER BY COUNT(itemFollower.id) DESC")
	public List<Item> getPublicRecommendedItems(@Param("activeStatus") StatusDescription activeStatus,
			@Param("userLatitude") Double latitude, @Param("userLongitude") Double longitude,
			@Param("radius") Double radius, Pageable pageable);
	
	@Query(value = "SELECT it.id FROM Item it left outer join it.followers ui left outer join ui.user itemFollower "
			+ "WHERE (SQRT(POW(69.1 * (it.latitude - (:userLatitude)), 2) + POW(69.1 * ((:userLongitude) - it.longitude) * COS(it.latitude / 57.3), 2))) < :radius "
			+ "AND it.status = :activeStatus and it.deleted = false " + "GROUP BY it.id "
			+ "ORDER BY COUNT(itemFollower.id) DESC")
	public List<Long> getPublicRecommendedItemIds(@Param("activeStatus") StatusDescription activeStatus,
			@Param("userLatitude") Double latitude, @Param("userLongitude") Double longitude,
			@Param("radius") Double radius, Pageable pageable);


	@Query(value = "select it from Item it join it.itemGroups tg where tg.id.groupId = :groupId and it.status = :activeStatus and it.deleted = false")
	public Page<Item> listItemsFromGroup(@Param("groupId") long groupId,
			@Param("activeStatus") StatusDescription activeStatus, Pageable pageable);
	
	
	@Query("select it from Item it join it.itemGroups tg where tg.id.groupId = :groupId and it.id IN :itemsIds and it.status = :activeStatus and it.deleted = false")
	public Page<Item> getItemsById(@Param("groupId") Long groupId,
			@Param("activeStatus") StatusDescription activeStatus,
			@Param("itemsIds") List<Long> itemsIds, Pageable pageable);
	
	
	@Query("select it from Item it join it.itemGroups tg where tg.id.groupId = :groupId and it.publishedBy.id IN :usersIds and it.status = :activeStatus and it.deleted = false")
	public List<Item> getItemsByGroupIdAndUsersIds(@Param("groupId") Long groupId,
			@Param("activeStatus") StatusDescription activeStatus,
			@Param("usersIds") List<Long> usersIds);

	@Query(value = "select itm from Item itm where itm.id >= :itemId")
	public List<Item> getItemsFromIdRange(@Param("itemId") Long itemId, Pageable pageable);

	@Query(value = "select it from Item it where it.id = :itemId")
	public Item findOneDeletedOrNot(@Param("itemId") Long itemId);

	@Query(value = "select it from Item it where it.deleted = false and it.status = :status")
    public Page<Item> findAllByStatus(@Param("status") StatusDescription status, Pageable pageable);
	
	@Query(value = "select it.id, it.description, it.picture1Url, it.category,"
			+ " it.name, it.price from Item it where it.deleted = false and it.status = :status"
			+ " and it.picture1Url is not null")
    public List<Object[]> findAllForFacebookBusinessByStatus(@Param("status") StatusDescription status);

}
