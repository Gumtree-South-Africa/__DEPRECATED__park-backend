package com.ebay.park.db.dao;

import com.ebay.park.db.entity.Rating;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * Data access interface for {@link Rating} entity.
 * @author marcos.lambolay
 */
public interface RatingDao extends JpaRepository<Rating, Long> {
	public Rating findByUserIdAndRaterIdAndItemId(Long userToRateId, Long raterId, Long itemId);
	
	@Query(value="select r from Rating r where r.comment is null and r.status is null and r.rater.id=:raterId order by r.id desc")
	public List<Rating> findPendingRatingsForRater(@Param("raterId") Long raterId, Pageable pageable);

	@Query(value="select r from Rating r where r.comment is null and r.status is null and r.rater.id=:raterId  and r.rateDate <= :rated order by r.id desc")
	public List<Rating> findPendingRatingsForRater(@Param("raterId") Long raterId, Pageable pageable, @Param("rated") Date rated);
	
	@Query(value="select count(r) from Rating r where r.comment is null and r.status is null and r.rater.id=:raterId")
	public long findPendingRatingsForRaterQty(@Param("raterId") Long raterId);
	
	@Query(value="select r from Rating r where r.comment is not null and r.status is not null and r.user.id=:userId order by r.rateDate desc")
	public List<Rating> findRatingForUser(@Param("userId") Long userId, Pageable pageable);

	@Query(value="select r from Rating r where r.comment is not null and r.status is not null and r.user.id=:userId and r.rateDate <= :rated order by r.rateDate desc")
	public List<Rating> findRatingForUser(@Param("userId") Long userId, Pageable pageable,  @Param("rated") Date rated);
	
	@Query(value="select count(r) from Rating r where r.comment is not null and r.status is not null and r.user.id=:userId")
	public long findRatingForUserQty(@Param("userId") Long userId);
	
	@Query(value="select count(r) from Rating r where r.comment is not null and r.status is not null and r.user.id=:userId and r.rateDate <= :rated")
	public long findRatingForUserQty(@Param("userId") Long userId, @Param("rated") Date rated);
	
	@Query(value="select r from Rating r join r.item i where r.comment is not null and r.status is not null and r.user.id = :userId and i.publishedBy.id = :userId order by r.rateDate desc")
	public List<Rating> findRatingForUserAsSeller(@Param("userId") Long userId, Pageable pageable);
	
	@Query(value="select r from Rating r join r.item i where r.comment is not null and r.status is not null and r.user.id = :userId and i.publishedBy.id = :userId and r.rateDate <= :rated order by r.rateDate desc")
	public List<Rating> findRatingForUserAsSeller(@Param("userId") Long userId, Pageable pageable, @Param("rated") Date rated);

	@Query(value="select count(r) from Rating r join r.item i where r.comment is not null and r.status is not null and r.user.id = :userId and i.publishedBy.id = :userId")
	public long findRatingForUserAsSellerQty(@Param("userId") Long userId);
	
	@Query(value="select count(r) from Rating r join r.item i where r.comment is not null and r.status is not null and r.user.id = :userId and i.publishedBy.id = :userId and r.rateDate <= :rated")
	public long findRatingForUserAsSellerQty(@Param("userId") Long userId, @Param("rated") Date rated);
	
	@Query(value="select r from Rating r join r.item i where r.comment is not null and r.status is not null and r.user.id = :userId and i.publishedBy.id != :userId order by r.rateDate desc")
	public List<Rating> findRatingForUserAsBuyer(@Param("userId") Long userId, Pageable pageable);
	
	@Query(value="select r from Rating r join r.item i where r.comment is not null and r.status is not null and r.user.id = :userId and i.publishedBy.id != :userId and r.rateDate <= :rated order by r.rateDate desc")
	public List<Rating> findRatingForUserAsBuyer(@Param("userId") Long userId, Pageable pageable, @Param("rated") Date rated);
	
	@Query(value="select count(r) from Rating r join r.item i where r.comment is not null and r.status is not null and r.user.id = :userId and i.publishedBy.id != :userId")
	public long findRatingForUserAsBuyerQty(@Param("userId") Long userId);
	
	@Query(value="select count(r) from Rating r join r.item i where r.comment is not null and r.status is not null and r.user.id = :userId and i.publishedBy.id != :userId and r.rateDate <= :rated")
	public long findRatingForUserAsBuyerQty(@Param("userId") Long userId, @Param("rated") Date rated);
	
	@Query(value="select r from Rating r where r.comment is null and r.status is null and r.id=:id and r.rater.id=:raterId")
	public Rating findPendingRatingByIdAndRaterId(@Param("id") Long id, @Param("raterId") Long raterId);
	
}
