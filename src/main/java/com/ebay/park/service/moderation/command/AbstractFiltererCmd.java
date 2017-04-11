package com.ebay.park.service.moderation.command;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;

import com.ebay.park.db.entity.DeviceType;
import com.ebay.park.db.entity.Group;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.OrphanedDevice;
import com.ebay.park.db.entity.SessionStatusDescription;
import com.ebay.park.db.entity.UserFollowsGroup;
import com.ebay.park.db.entity.UserSession;
import com.ebay.park.db.entity.UserSocial;
import com.ebay.park.db.entity.UserStatusDescription;
import com.ebay.park.service.moderation.UserReceiverPush;
import com.ebay.park.service.moderation.dto.FilterForModerationRequest;
import com.ebay.park.service.moderation.dto.SendNotificationsForModerationResponse;
import com.ebay.park.service.social.SocialIds;
import com.ebay.park.util.DataCommonUtil;

public abstract class AbstractFiltererCmd<T extends FilterForModerationRequest> {

	@PersistenceContext
	protected EntityManager entityManager;

	@Autowired
	protected SocialIds socialIds;

	public abstract SendNotificationsForModerationResponse execute(T request);

	@SuppressWarnings("unchecked")
	protected List<UserReceiverPush> getFilteredUsers(FilterForModerationRequest request) {
		Criteria sessionCriteria;
		Session session = entityManager.unwrap(Session.class);
		if (filteringUserWithSessions(request)) {
			sessionCriteria = session.createCriteria(UserSession.class, "sessionCriteria").createCriteria("user");
			sessionCriteria.createAlias("sessionCriteria.device", "device");
			addFiltersWithSession(request, sessionCriteria);

			sessionCriteria
					.setProjection(Projections.projectionList().add(Projections.property("id"), "userId")
							.add(Projections.property("device.deviceId"), "deviceId")
							.add(Projections.property("device.platform"), "platform"))
					.setResultTransformer(Transformers.aliasToBean(UserReceiverPush.class));
		} else {
			sessionCriteria = session.createCriteria(OrphanedDevice.class, "orphanedDeviceCriteria");
			addFiltersNoSession(request, sessionCriteria);

			sessionCriteria
					.setProjection(Projections.projectionList().add(Projections.property("deviceId"), "deviceId")
							.add(Projections.property("platform"), "platform"))
					.setResultTransformer(Transformers.aliasToBean(UserReceiverPush.class));
		}

		return sessionCriteria.list();
	}

	private void addFiltersNoSession(FilterForModerationRequest request, Criteria sessionCriteria) {
		addPlatformFiltersNoSession(request, sessionCriteria);
	}

	private void addPlatformFiltersNoSession(FilterForModerationRequest request, Criteria sessionCriteria) {
		if (StringUtils.isNotEmpty(request.getPlatform())) {

			Criterion deviceTypeSelection;

			if (request.getPlatform().toLowerCase().equals(DeviceType.ANDROID.getValue())) {
				deviceTypeSelection = Restrictions.eq("platform", DeviceType.ANDROID);
			} else if (request.getPlatform().toLowerCase().equals(DeviceType.IOS.getValue())) {
				deviceTypeSelection = Restrictions.eq("platform", DeviceType.IOS);
			} else {
				deviceTypeSelection = Restrictions.or(Restrictions.eq("platform", DeviceType.ANDROID),
						Restrictions.eq("platform", DeviceType.IOS));
			}

			sessionCriteria.add(deviceTypeSelection);

		}
	}

	private void addFiltersWithSession(FilterForModerationRequest request, Criteria userCriteria) {
		addGroupFilters(request, userCriteria);
		addSocialFilters(request, userCriteria);
		addItemFilters(request, userCriteria);
		addLocationFilters(request, userCriteria);
		addUserFilters(request, userCriteria);
		addPlatformFiltersWithSession(request, userCriteria);
	}

	/**
	 * Completes the criteria in order to include no session users.
	 * 
	 * @param request
	 */
	private boolean filteringUserWithSessions(FilterForModerationRequest request) {
		SessionStatusDescription filter = request.getSessionStatus();
		return (filter == null) ? true : !filter.equals(SessionStatusDescription.NO_SESSION);
	}

	/**
	 * Completes the criteria in order to include platform filter.
	 * 
	 * @param request
	 * @param userCriteria
	 */
	private void addPlatformFiltersWithSession(FilterForModerationRequest request, Criteria userCriteria) {

		if (StringUtils.isNotEmpty(request.getPlatform())) {

			Criterion deviceTypeSelection;

			if (request.getPlatform().toLowerCase().equals(DeviceType.ANDROID.getValue())) {
				deviceTypeSelection = Restrictions.eq("device.platform", DeviceType.ANDROID);
			} else if (request.getPlatform().toLowerCase().equals(DeviceType.IOS.getValue())) {
				deviceTypeSelection = Restrictions.eq("device.platform", DeviceType.IOS);
			} else {
				deviceTypeSelection = Restrictions.or(Restrictions.eq("device.platform", DeviceType.ANDROID),
						Restrictions.eq("device.platform", DeviceType.IOS));
			}

			userCriteria.add(deviceTypeSelection);

		}
	}

	/**
	 * Completes the criteria in order to include user filters.
	 * 
	 * @param request
	 * @param userCriteria
	 */
	private void addUserFilters(FilterForModerationRequest request, Criteria userCriteria) {
		// users logged with device
		if (request.getSessionStatus().equals(SessionStatusDescription.ACTIVE_SESSION)) {
			userCriteria.add(Restrictions.eq("sessionCriteria.sessionActive", true));
		} else if (request.getSessionStatus().equals(SessionStatusDescription.INACTIVE_SESSION)) {
			userCriteria.add(Restrictions.eq("sessionCriteria.sessionActive", false));
		}

		// active users
		userCriteria.add(Restrictions.eq("status", UserStatusDescription.ACTIVE));

		// verified
		if (request.getIsVerified() != null) {
			userCriteria.add(Restrictions.eq("emailVerified", request.getIsVerified()));
		}

		// creation date: from
		if (request.getAccountCreationFrom() != null) {
			Date dateFrom = DataCommonUtil.parseUnixTime(request.getAccountCreationFrom());
			userCriteria.add(Restrictions.ge("creation", dateFrom));
		}

		// creation date: to
		if (request.getAccountCreationTo() != null) {
			Date dateTo = DataCommonUtil.parseUnixTime(request.getAccountCreationTo());
			userCriteria.add(Restrictions.le("creation", DataCommonUtil.addDays(dateTo, 1)));
		}
	}

	/**
	 * Completes the criteria in order to include location filter.
	 * 
	 * @param request
	 * @param userCriteria
	 */
	private void addLocationFilters(FilterForModerationRequest request, Criteria userCriteria) {
		// zipcodes
		if (request.getZipCode() != null) {
			Disjunction or = Restrictions.disjunction();
			for (Integer zipcode : request.getZipCode()) {
				or.add(Restrictions.eq("zipcode", String.valueOf(zipcode)));
			}
			userCriteria.add(or);
		}
	}

	/**
	 * Completes the criteria in order to include item filters.
	 * 
	 * @param request
	 * @param userCriteria
	 */
	private void addItemFilters(FilterForModerationRequest request, Criteria userCriteria) {
		// has the user active items?
		if (request.getHasActiveItems() != null || request.getCategoryActiveItems() != null) {
			DetachedCriteria subqueryItemesActive = DetachedCriteria.forClass(Item.class, "item")
					.add(Property.forName("item.publishedBy.id").eqProperty("sessionCriteria.user.id"))
					.add(Restrictions.eq("item.deleted", false));
			if (request.getHasActiveItems() != null) {
				if (request.getHasActiveItems()) {
					userCriteria.add(Subqueries
							.exists(subqueryItemesActive.setProjection(Projections.property("item.publishedBy.id"))));
				} else {
					userCriteria.add(Subqueries.notExists(
							subqueryItemesActive.setProjection(Projections.property("item.publishedBy.id"))));
				}
			}

			// items category
			if (request.getCategoryActiveItems() != null) {
				subqueryItemesActive.add(Restrictions.eq("item.category.id", request.getCategoryActiveItems()));
				userCriteria.add(Subqueries
						.exists(subqueryItemesActive.setProjection(Projections.property("item.publishedBy.id"))));
			}
		}
	}

	/**
	 * Completes the criteria in order to include social filters.
	 * 
	 * @param request
	 * @param userCriteria
	 */
	private void addSocialFilters(FilterForModerationRequest request, Criteria userCriteria) {
		// facebook
		if (request.getHasFacebook() != null) {
			DetachedCriteria facebookCriteria = DetachedCriteria.forClass(UserSocial.class, "facebookCriteria")
					.add(Property.forName("facebookCriteria.user.id").eqProperty("sessionCriteria.user.id"))
					.add(Property.forName("facebookCriteria.social.id").eq(socialIds.getFacebookId()));

			if (request.getHasFacebook()) {
				userCriteria.add(Subqueries
						.exists(facebookCriteria.setProjection(Projections.property("facebookCriteria.user.id"))));
			} else {
				userCriteria.add(Subqueries
						.notExists(facebookCriteria.setProjection(Projections.property("facebookCriteria.user.id"))));
			}
		}

		if (request.getHasTwitter() != null) {
			// twitter
			DetachedCriteria twitterCriteria = DetachedCriteria.forClass(UserSocial.class, "twitterCriteria")
					.add(Property.forName("twitterCriteria.user.id").eqProperty("sessionCriteria.user.id"))
					.add(Property.forName("twitterCriteria.social.id").eq(socialIds.getTwitterId()));

			if (request.getHasTwitter()) {
				userCriteria.add(Subqueries
						.exists(twitterCriteria.setProjection(Projections.property("twitterCriteria.user.id"))));
			} else {
				userCriteria.add(Subqueries
						.notExists(twitterCriteria.setProjection(Projections.property("twitterCriteria.user.id"))));
			}
		}
	}

	/**
	 * Completes the criteria in order to include group filters.
	 * 
	 * @param request
	 * @param userCriteria
	 */
	private void addGroupFilters(FilterForModerationRequest request, Criteria userCriteria) {
		// group owner
		if (request.getIsGroupOwner() != null) {
			DetachedCriteria subquery = DetachedCriteria.forClass(Group.class, "group")
					.add(Property.forName("group.creator.id").eqProperty("sessionCriteria.user.id"));
			if (request.getIsGroupOwner()) {

				userCriteria.add(Subqueries.exists(subquery.setProjection(Projections.property("group.creator.id"))));
			} else {
				userCriteria
						.add(Subqueries.notExists(subquery.setProjection(Projections.property("group.creator.id"))));
			}
		}

		// is the user a group follower?
		if (request.getIsGroupFollower() != null) {
			DetachedCriteria followGroup = DetachedCriteria.forClass(UserFollowsGroup.class, "followGroup")
					.add(Property.forName("followGroup.user.id").eqProperty("sessionCriteria.user.id"));
			if (request.getIsGroupFollower()) {
				userCriteria
						.add(Subqueries.exists(followGroup.setProjection(Projections.property("followGroup.user.id"))));
			} else {
				userCriteria.add(
						Subqueries.notExists(followGroup.setProjection(Projections.property("followGroup.user.id"))));
			}
		}
		
		// is the user a member of a the group?
		if (request.getIsMemberOfGroup() != null) {
			DetachedCriteria memberGroup = DetachedCriteria.forClass(UserFollowsGroup.class, "followGroup")
					.add(Property.forName("followGroup.user.id").eqProperty("sessionCriteria.user.id"));
			// group id
			memberGroup.add(Restrictions.eq("followGroup.group.id", request.getIsMemberOfGroup()));
			userCriteria.add(Subqueries.exists(memberGroup.setProjection(Projections.property("followGroup.user.id"))));
			
		}
	}

}
