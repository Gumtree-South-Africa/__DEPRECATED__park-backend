package com.ebay.park.db.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ebay.park.db.entity.Group;
import com.ebay.park.db.entity.StatusDescription;
import com.ebay.park.db.entity.User;

/**
 * Data access interface for the {@link Group} entity.
 * 
 * @author marcos.lambolay
 */
public interface GroupDao extends JpaRepository<Group, Long> {

	@Query(value = "select grp from Group grp join grp.followers ug where ug.user.id = :userId and grp.id = :groupId")
	public Group getGroupFollowedByUserAndId(@Param("userId") Long userId, @Param("groupId") Long groupId);

	@Query(value = "select userFollowingGroup from Group grp join grp.followers followersSet join followersSet.user userFollowingGroup  "
			+ "join userFollowingGroup.followers userFollowingUser where grp.id = :groupId and userFollowingGroup.picture is not null and "
			+ "userFollowingUser.id.followerId = :userId")
	public List<User> getGroupFollowersAndUserFollowingsWithPicture(@Param("groupId") long groupId, @Param("userId") long userId, Pageable pageable);
	
	@Query(value = "SELECT grp FROM Group grp join grp.followers ug join ug.user groupFollower GROUP BY grp.id ORDER BY COUNT(groupFollower.id) DESC")
	public List<Group> getRecommendedGroup(Pageable pageable);
	
	@Query(value = "select ug.user from Group grp join grp.followers ug where grp.id = :groupId")
	public List<User> getGroupFollowers(@Param("groupId") Long groupId);
	
	@Query(value = "select userFollowingGroup from Group grp join grp.followers followersSet join followersSet.user userFollowingGroup  "
			+ "join userFollowingGroup.followers userFollowingUser where grp.id = :groupId and "
			+ "userFollowingUser.id.followerId = :userId")
	public List<User> getGroupFollowersAndUserFollowings(@Param("groupId") Long groupId, @Param("userId") long userId);

	public Group findByName(String name);

	@Query(value = "select grp from Group grp where grp.id >= :groupId")
	public List<Group> getGroupsFromIdRange(@Param("groupId") Long groupId, Pageable pageable);

	@Query(value = "select count (it) from Group grp join grp.items it where it.item.status = :activeStatus and it.item.deleted = false and grp.id = :groupId and it.date > :lastAccess ")
	public Long getNewItemsCount(
			@Param("lastAccess") Date lastAccess,
			@Param("groupId") Long groupId,
			@Param("activeStatus") StatusDescription activeStatus);
}
