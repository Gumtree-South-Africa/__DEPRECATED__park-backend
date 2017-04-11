/**
 * Copyright eBay, 2014
 */
package com.ebay.park.service;

/**
 * @author diana.gazquez
 * 
 */
public enum ServiceExceptionCode {

	// @formatter:off
	USER_NOT_FOUND(100, "error.user_not_found", false),
	WRONG_SIGNIN_DATA(101, "error.wrong_signin_data", false),
	INVALID_FB_TOKEN(102, "error.invalid_facebook_token"),
	BAD_REQ_INFO(103, "error.bad_request_information"),
	INVALID_PWD(104, "error.invalid_password"),
	USER_NOT_CONNECTED(105, "error.user_not_connected"),
	INVALID_TWITTER_TOKEN(106, "error.invalid_twitter_token"),
	WRONG_SIGNIN_DATA_ONE_TO_BLOCK(107, "error.one_try_to_block", false),
	INCOMPATIBLE_VERSION_MOBILE(108, "error.incompatible_version_mobile"),
	INCOMPATIBLE_VERSION_WEB(109, "error.incompatible_version_web"),
	GENERIC_ERROR(110, "error.generic_error"),
	NON_EXISTENT_EMAIL(112, "error.non_existent_email"),


	EMPTY_USERNAME(200, "error.empty_username", false),
	EMPTY_PASSWORD(201, "error.empty_password", false),
	EMPTY_EMAIL(202, "error.empty_email", false),
	EMPTY_LOCATION(203, "error.empty_location", false),
	DUPLICATED_SIGNUP_DATA(204, "error.duplicated_data", false),
	INVALID_LANGUAGE(205, "error.invalid_language", false),

	EMPTY_DEVICE_ID(206, "error.empty_device_id", false),
	EMPTY_DEVICE_TYPE(207, "error.empty_device_type", false),
	INVALID_DEVICE_TYPE(208, "error.invalid_device_type", false),

	INVALID_SIGNUP_REQ(209, "error.invalid_signup_req"),
	INVALID_FACEBOOK_INFO(210, "error.invalid_facebook_info"),
	INVALID_FACEBOOK_ID(211, "error.invalid_facebook_id"),
	EMPTY_USER_ID(212, "error.empty_user_id"),
	EMPTY_TOKEN(213, "error.empty_token"),
	INVALID_TEMP_TOKEN(214, "error.invalid_token"),
	ACCOUNT_LOCKED(215, "error.account_locked"),
	NO_PREVIOUS_FORGET_PWD(216, "error.no_previous_forget_pwd"),
	INVALID_LOCATION(217, "error.invalid_location"),
	EMAIL_ALREADY_VERIFIED(218, "error.user_email_already_verified"),

	EMPTY_LOCATION_NAME(219, "error.empty_location_name"),
	USER_BANNED_ERROR(220, "error.user_banned"),
	ACCOUNT_NOT_VERIFIED(221, "error.account_not_verified"),
	INVALID_USERNAME_PATTERN(224,"error.invalid_username_pattern"),
	INVALID_USERNAME_LONG(225,"error.invalid_username_long"),
	ERROR_FORGOT_PASSWORD_WHEN_BANNED(226, "error.forgot_password_when_banned"),
	EMPTY_UNIQUE_DEVICE_ID(227, "error.empty_unique_device_id", false),
	EMPTY_PHONE_NUMBER(228, "error.empty_phone_number"),
	EMPTY_ACCOUNT_KIT_TOKEN(229, "error.empty_account_kit_token"),
	INVALID_PHONE_NUMBER(230, "error.invalid_phone_number"),
	

	SESSION_NOT_STORED(300, "error.session_not_stored"),
	EMPTY_PARK_TOKEN(301, "error.empty_park_token", false),
	USER_UNAUTHORIZED(302, "error.user_unauthorized", false),
	INVALID_ENVIRONMENT(303, "error.invalid_environment"),
	USER_UNAUTHORIZED_SUPER_ADMIN(304, "error.user_unauthorized_super_admin", false),
	USER_UNAUTHORIZED_ADMIN(305, "error.user_unauthorized_admin", false),

	UPLOAD_EMPTY_FILE(400, "error.upload_empty_file"),
	INVALID_UPLOAD_DATA(401, "error.invalid_upload_data"),

	INVALID_TIMESTAMP(450, "error.invalid_timestamp"),

	// Profile Management
	INVALID_USERINFO_UPDATE_REQ(500, "error.invalid_userinfo_update_req"),
	NO_PICTURES(503, "error.no_pictures"),
	NO_GROUPS(504, "error.no_groups"),
	INVALID_URL(505, "error.invalid_url"),
	FOLLOWING_NOT_FOUND(506, "error.following_not_found"),
	FOLLOW_SELF_REFERENTIAL(507, "error.follow_self_referential"),
	FOLLOW_ALREADY_EXISTS(508, "error.follow_already_exists"),

	FOURSQUARE_API_ERROR(600, "error.foursquare_api"),

	// Item Management
	EMPTY_DATA_CREATEITEM(700, "error.empty_data_createitem"),
	ITEM_NOT_FOUND(701, "error.item_not_found", false),
	ITEM_DELETION_ERROR(702, "error.item_deletion_error"),
	ITEM_ALREADY_DELETED(703, "error.item_already_deleted"),
	ITEM_ALREADY_REPORTED_FOR_USER(704, "error.item_already_reported_for_user"),
	ITEM_REPORTED_BELONGS_USER(706, "error.item_reported_belongs_user"),
	ITEM_REPORT_NOT_FOUND(707, "error.item_report_not_found"),
	CATEGORY_NOT_FOUND(708, "error.category_not_found"),
	INVALID_GROUP(709, "error.invalid_group"),
	ERROR_DELETING_COMMENT(710, "error.error_deleting_comment"),
	ERROR_UPDATING_ITEM(711, "error.error_updating_item"),
	EMPTY_DATA_UPDATE(712, "error.empty_data_update"),
	ITEM_ALREADY_FOLLOWED_BY_USER(714, "error.item_already_followed_by_user"),
	ITEM_NOT_FOLLOWED_BY_USER(715, "error.item_not_followed_by_user"),
	ITEM_DOESNT_BELONG_TO_USER(716, "error.item_doesnt_belong_to_user"),
	ERROR_DELETING_PICTURE(717, "error.error_deleting_picture"),
	PICTURE_NOT_FOUND(718, "error.picture_not_found"),
	CANNOT_DELETE_PICTURE(719, "error.cannot_delete_picture"),
	MANDATORY_PICTURE_NOT_UPLOADED(720, "error.mandatory_picture_not_uploaded"),
	INVALID_PICTURE_FORMAT(721, "error.invalid_picture_format"),
	INVALID_LATITUDE_LONGITUDE(722, "error.invalid_latitude_longitude"),
	ITEM_EXPIRED(723, "error.item_expired"),
	UNAUTHORIZED_TO_ACCESS_ITEM(724, "error.unauthorized_to_access_item"),
	INVALID_ITEM_DESCRIPTION_LONG(725,"error.invalid_item_description_long"),
	ITEMS_ARE_NOT_IN_THE_GROUP(726,"error.items_are_not_in_the_group"),
	INVALID_ITEM_NAME_CHARACTER(727,"error.invalid_item_name_character"),
	INVALID_ITEM_DESCRIPTION_CHARACTER(728,"error.invalid_item_description_character"),
	ITEM_PICTURE_UPLOAD_FAILED(729, "error.item_picture_upload_failed"),
	ITEM_NOT_AVAILABLE_TO_MODERATE(730, "error.item_not_available_to_moderate"),
	ITEM_ALREADY_MODERATED(731, "error.item_already_moderated"),
	INVALID_ITEM_NAME_LONG(732,"error.invalid_item_name_long"),
	ITEM_UPLOAD_PHOTO_ERROR(733, "error.item_upload_photo_error"),

	INVALID_PICTURE_ID(740, "error.invalid_item_picture_id"),

	ITEM_SEARCH_EMPTY_CRITERIA(701, "error.empty_criteria"),

	// social services
	INVALID_USER_RATES_REQ(800, "error.invalid_user_rates_req"),
	INVALID_USER_RATES_STATUS(801, "error.invalid_user_rates_status"),
	INVALID_FB_POST_PERMISSION(802, "error.invalid_facebook_post_permission"),
	DUPLICATED_FB_POST(803, "error.duplicated_facebook_post"),
	RATE_LIMIT_FB_POST_EXCEED(804, "error.rate_limit_facebook_post"),
	ERROR_FB_POST_COMMUNICATION(805, "error.error_facebook_post_communication"),
	INVALID_SOCIAL_NETWORK(806, "error.invalid_social_network"),
	INVALID_SOCIAL_USERID(807, "error.invalid_social_userid"),
	INVALID_USER_SOCIAL(808, "error.social_network_not_linked"),
	USERS_SEARCH_ERROR(809, "error.users_search_error"),
	UNAVAILABLE_TWITTER_SERVICE(810, "error.twitter.service_unavailable"),
	STATUS_DUPLICATED(811, "error.twitter.duplicated_status"),
	USER_SOCIAL_ALREADY_REGISTERED_FB(812,"error.socialNetwork.user_already_registered_fb"),
	USER_SOCIAL_ALREADY_REGISTERED_TW(813,"error.socialNetwork.user_already_registered_tw"),	
	INVALID_FACEBOOK_EMAIL(814,"error.invalid_facebook_email"),
	INVALID_ACCOUNT_KIT_TOKEN(815, "error.invalid_account_kit_token"),
	INVALID_ACCOUNT_KIT_URI(816, "error.invalid_account_kit_uri"),
	ERROR_ACCOUNT_KIT_COMMUNICATION(817, "error.account_kit_communication"),
	DUPLICATED_MOBILE_PHONE(818, "error.duplicated_mobile_phone"),
	INSUFFICIENT_FB_PERMISSION(819, "error.insufficient_facebook_permission"),
	ERROR_FB_COMMUNICATION(820, "error.error_facebook_communication"),
	FB_REVOKED_AUTHORIZATION(821, "error.fb_revoked_authorization"),
	
	
	// notification services
	INVALID_NOT_REQ(900, "error.invalid_notification_request"),
	INVALID_NOTIFICATION_PROPERTIES(901, "error.invalid_notification_prop"),
	EMPTY_NOTIFICATION_TYPE(902, "error.empty_notification_type"),
	EMPTY_NOTIFICATION(903, "error.empty_notification"),
	NOTIFICATION_TYPE_NOT_FOUND(904, "error.notification_type_not_found"),
	NOTIFICATION_ACTION_NOT_FOUND(905, "error.notification_action_not_found"),
	NOTIFICATION_TEMPLATE_NOT_FOUND(906, "error.notification_template_not_found"),
	INVALID_NOT_TEMPLATE_REQ(907, "error.invalid_not_template_request"),
	EMPTY_NOTIFICATION_ACTION(908, "error.empty_notification_action"),
	INVALID_FEEDS_CONFIG_REQ(909, "error.invalid_feeds_config_req"),
	EMPTY_FEEDS_CONFIG_FIELD(910, "error.empty_feeds_config_field"),
	INVALID_FEEDS_CONFIG_FIELD (911, "error.invalid_feeds_config_field"),
	NO_BANNER_TO_SHOW(912, "error.no_banners_to_show"),
	NOTIFICATION_NOT_FOUND(913, "error.notification_id_not_found"),

	// location service
	INVALID_KEYWORD(1000, "error.invalid_keyword"),

	// email service
	EMAIL_SEND_ERROR(1100, "error.email_sender"),
	EMAIL_EMPTY_SUBJECT_ERROR(1101, "error.empty_email_subject"),
	EMAIL_EMPTY_BODY_ERROR(1102, "error.empty_email_body"),
	EMAIL_PUBLISHER_EMPTY_EMAIL(1103, "error.publisher_empty_email"),
	EMAIL_USER_EMPTY_EMAIL(1104, "error.user_empty_email"),

	NOTIFICATION_ERROR(1200, "error.general_notification_exception"),

	// password validation
	EMPTY_DATA_CHANGE_PWD(1300, "error.empty_data_changepwd"),
	PWD_TOO_SHORT(1301, "error.password_too_short", false),
	PWD_MUST_HAVE_SPEC_CHAR(1302, "error.password_must_have_spec_char", false),
	PWD_MUST_HAVE_DIGIT(1303, "error.password_must_have_digit", false),
	PWD_MUST_HAVE_MIXED_CASE(1304, "error.password_must_have_mixed_case", false),
	PWD_TOO_SIMILAR(1305, "error.new_password_too_similar", false),
	EMPTY_DATA_VERIFY_EMAIL(1306, "error.empty_data_verifyemail"),
	INVALID_ACCESS_DATA(1307, "error.invalid_access_data"),
	PWD_TOO_LONG(1308, "error.password_too_long", false),

	// chat and conversation management
	USER_NOT_BUYER_NOR_SELLER_ERROR(1401, "error.user_not_buyer_nor_seller"),
	SELLER_CANT_START_CONVERSATION_ERROR(1403, "error.seller_cant_start_conversation"),
	ITEM_NOT_ACTIVE_NOR_SOLD_ERROR(1404, "error.item_not_active_nor_sold"),
	CHAT_SENDER_SAME_AS_RECEIVER_ERROR(1406, "error.chat_sender_same_as_receiver"),
	CONVERSATION_NOT_FOUND_ERROR(1407, "error.conversation_not_found"),
	ALREADY_ACCEPTED_CONVERSATION_ERROR(1408, "error.already_accepted_conversation"),
	ALREADY_CANCELLED_CONVERSATION_ERROR(1409, "error.already_cancelled_conversation"),
	ALREADY_SOLD_ITEM_ERROR(1410, "error.already_sold_item"),
	BAD_PRICE_FORMAT(1411, "error.bad_price_format"),
	SELLER_CANT_BUY_HIS_OWN_ITEM_ERROR(1412, "error.seller_cant_buy_his_own_item"),

	ITEM_SEARCH_EMPTY_QUERY_EXCEPTION(1500, "error.search_item_empty_query"),

	// Moderation
	MODERATION_ITEM_NOT_PENDING(1600, "error.moderation_item_not_pending"),
	ITEM_ACTIVATION_ERROR(1601, "error.item_activation_error"),
	MODERATION_SEARCH_ITEM_ERROR(1602, "error.moderation_search_item_error"),
	BLACKLIST_SEARCH_ERROR(1603, "error.blacklist_search_error"),
	MODERATION_USER_NOT_BANNED(1604, "error.moderation_user_not_banned"),
	MODERATION_USER_ALREADY_BANNED(1605, "error.moderation_user_already_banned"),
	USER_ACTIVATION_ERROR(1606, "error.user_activation_error"),
	USER_BAN_ERROR(1607, "error.user_ban_error"),
	MODERATION_SEARCH_USER_ERROR(1608, "error.moderation_search_user_error"),
	BLACKLIST_VALIDATION_ERROR(1609, "error.blacklist_validation_error"),
	BLACKLIST_WORD_NOT_FOUND(1610, "error.blacklist_word_not_found"),
	BLACKLIST_WORD_EMPTY(1611, "error.blacklist_word_empty"),
	BLACKLIST_WORD_ALREADY_EXISTS(1612, "error.blacklist_word_already_exists"),
	USERNAME_DUPLICATED (1613, "error.username_duplicated"),
	EMAIL_DUPLICATED(1614, "error.email_duplicated"),
	ADD_MODERATOR_ERROR(1615, "error.add_moderator_error"),
	UPDATE_MODERATOR_ERROR(1616, "error.update_moderator_error"),
	REMOVE_MODERATOR_ERROR(1617, "error.remove_moderator_error"),
	EMPTY_SEND_MODERATION_NOTIF_ONLYPUSH(1618, "error.empty_onlypush_value_moderation"),
	EMPTY_SEND_MODERATION_NOTIF_MESSAGE(1619, "error.empty_message_moderation"),
	INVALID_SEND_MODERATION_NOTIF_REQ(1620, "error.invalid_send_notif_req_moderation"),
	INVALID_SEND_MODERATION_NOTIF_MESSAGE_LENGTH(1621, "error.invalid_send_notif_message_length"),
	ERROR_SENDING_PUSH_NOTIFICATIONS(1622, "error.sending_push_notifications"),
	BLACKLIST_CREATION_ERROR (1623, "error.blacklist_creation"),
	WRONG_CODING(1624, "error.codification"),
	INVALID_REJECT_ITEM_ID_REQ(1625, "error.invalid_reject_item_id_request"),
	INVALID_REJECT_ITEM_REASON_REQ(1626, "error.invalid_reject_item_reason_request"),
	INVALID_REJECT_ITEM_REQ(1627, "error.invalid_reject_item_request"),
	INVALID_FILTER_REQUEST(1628, "error.invalid_filter_request"),
	WRONG_DATE_FORMAT(1629, "error.wrong_date_format"),

	// groups management
	EMPTY_DATA_CREATEGROUP(1700, "error.empty_data_create_group"),
	DUPLICATED_GROUP_DATA(1701, "error.duplicated_group_data"),
	USER_ALREADY_SUBSCRIBED(1702, "error.user_already_subscribed"),
	USER_NOT_SUBSCRIBED(1703, "error.user_not_subscribed"),
	EMPTY_PICTURE(1704, "error.empty_picture"),
	PICTURE_ALREADY_UPLOADED(1705, "error.picture_already_uploaded"),
	INVALID_SEARCH_LOCATION(1706, "error.invalid_search_location"),
	SEARCH_TERM_IS_NOT_SPECIFIC_ENOUGH(1707, "error.search_term_is_not_specific_enough"),
	INVALID_GROUP_OWNER(1708, "error.invalid_owner_group"),
	GROUP_ALREADY_EXISTS(1709, "error.group_already_exists"),
	USERS_NOT_SUBSCRIBED(1710, "error.users_not_subscribed"),
	INVALID_USER_IDS(1711, "error.group_invalid_user_ids"),
	ERROR_UPDATING_GROUP(1712, "error.updating_group"),
	EMPTY_DATA_UPDATE_GROUP(1713, "error.empty_data_update_group"),
	INVALID_ZIP_CODE(1714, "error.empty_zip_code"),
	ERROR_CREATING_GROUP(1715, "error.updating_group"),
	ERROR_DELETING_GROUP(1716, "error.deleting_group"),
	INVALID_ITEM_IDS(1717, "error.group_invalid_items_ids"),
	OWNER_CAN_NOT_BE_UNSUBSCRIBED(1718, "error.owner_can_not_unsubscribed"),
	INVALID_GROUP_NAME_CHARACTER(1719,"error.invalid_group_name_character"),
	INVALID_GROUP_DESCRIPTION_CHARACTER(1720,"error.invalid_group_description_character"),

	// Rating
	RATING_COMMENT_BLANK_ERROR(1800, "error.rating_comment_blank_error"),
	USER_ALREADY_RATED_ERROR(1801, "error.user_already_rated_error"),
	RATING_INVALID_ROLE(1802, "error.rating_invalid_role"),
	INVALID_PENDING_RATING(1803, "error.invalid_pending_rating"),

	// Item republish
	ITEM_NOT_SOLD_OR_EXPIRED_ERROR(1900, "error.item_not_sold_or_expired"),
	ITEM_NOT_SOLD_EXPIRED_OR_ACTIVE_ERROR(1901, "error.item_not_sold_expired_or_active"),
	ITEM_NOT_ABLE_TO_REPUBLISH(1902, "error.item_not_able_to_republish"),

	// conversation and chat bad iso date format
	LAST_REQUEST_BAD_FORMAT_ERROR(2000, "error.last_request_bad_format"),

	ITEM_NOT_ACTIVE_ERROR(2100, "error.item_not_active"),
	CANT_SEND_CHAT_ERROR(2101, "error.cant_send_chat"),
	CONVERSATION_EXIST(2102, "error.cant_create_new_conversation"),

	// send user feedback exceptions:
	APP_VERSION_BLANK_ERROR(2200, "error.app_version_blank"),
	COUNTRY_CODE_BLANK_ERROR(2201, "error.country_code_blank"),
	DEVICE_MODEL_BLANK_ERROR(2202, "error.device_model_blank"),
	FEEDBACK_MSG_BLANK_ERROR(2203, "error.feedback_msg_blank"),

	// tutorial
	TUTORIAL_STEP_NOT_FOUND(2300, "error.tutorial_step_not_found"),

	// DataAccessException
	IO_ERROR(2400, "error.io_error"),
	
	WRONG_PLATFORM_HEADER(2500, "error.missing_apiversion_header"),
	TIMEOUT(2600, "error.timeout");

	// @formatter:on

	private final int code;
	private final String message;
	private final boolean error;

	ServiceExceptionCode(int code, String message) {
		this.message = message;
		this.code = code;
		this.error = true;
	}

	ServiceExceptionCode(int code, String message, boolean error) {
		this.message = message;
		this.code = code;
		this.error = error;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public boolean isError() {
		return error;
	}

}
