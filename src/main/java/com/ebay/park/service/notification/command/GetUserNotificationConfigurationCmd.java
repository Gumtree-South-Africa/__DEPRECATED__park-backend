/**
 * Copyright eBay, 2014
 */
package com.ebay.park.service.notification.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ebay.park.db.dao.NotificationConfigDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.NotificationConfig;
import com.ebay.park.db.entity.User;
import com.ebay.park.notification.NotificationAction;
import com.ebay.park.notification.NotificationGroup;
import com.ebay.park.notification.NotificationType;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.notification.dto.ActionConfigurationDTO;
import com.ebay.park.service.notification.dto.ConfigurationValue;
import com.ebay.park.service.notification.dto.GetUserNotificationRequest;
import com.ebay.park.service.notification.dto.NotificationConfigRequest;
import com.ebay.park.util.InternationalizationUtil;


/**
 * Finds the notification configuration for the current user.
 * This configuration stores the user selection regarding he/she wants/doens't want to receive a notification type.
 * Feeds are  compulsory, but pushes and emails are optional. 
 * @author diana.gazquez & Julieta Salvadó
 *
 */
@Component
public class GetUserNotificationConfigurationCmd implements ServiceCommand<GetUserNotificationRequest, NotificationConfigRequest>{

    @Autowired
	private UserDao userDao;

	@Autowired
	private NotificationConfigDao notificationConfigDao;

	@Autowired
	protected InternationalizationUtil i18nUtil;

	private static final long CURRENT_VERSION = 3;

	/* (non-Javadoc)
	 * @see com.ebay.park.service.ServiceCommand#execute(java.lang.Object)
	 */
	//TODO Refactor this when adding full integration with the retrocompatibility solution
	@Override
	public NotificationConfigRequest execute(GetUserNotificationRequest request) throws ServiceException {
	      Map<NotificationAction, ActionConfigurationDTO> userConfiguration = new HashMap<NotificationAction, ActionConfigurationDTO>();

	        User user = userDao.findByUsername(request.getUsername());

	        String lang = request.getLanguage();

	        if (lang == null) {
	            lang = user.getIdiom().getCode();
	        }
	        
	        List<NotificationConfig> availableNotifications = getAvailableNotifications();

	        for (NotificationAction action : availableNotifications.stream().map(NotificationConfig::getNotificationAction).collect(Collectors.toList()) ) {
	            ActionConfigurationDTO dto = new ActionConfigurationDTO(i18nUtil.internationalize(action.getMessageKey(),
	                    lang), action, ConfigurationValue.DISABLED, ConfigurationValue.DISABLED);
	            userConfiguration.put(action, dto);
	        }

	        for (NotificationConfig availiableConfig : availableNotifications) {

	            ActionConfigurationDTO dto = userConfiguration.get(availiableConfig.getNotificationAction());

	            if (NotificationType.EMAIL.equals(availiableConfig.getNotificationType())) {
	                dto.setEmailConfig(ConfigurationValue.FALSE);

	            } else if (NotificationType.PUSH.equals(availiableConfig.getNotificationType())) {
	                dto.setPushConfig(ConfigurationValue.FALSE);
	            }
	            // Ignore Feed, they are always enabled

	        }

	        for( NotificationConfig userConfig : user.getNotificationConfigs()){
	            if (availableNotifications.contains(userConfig)) {
    	            if (NotificationType.EMAIL.equals(userConfig.getNotificationType())){
    	                userConfiguration.get(userConfig.getNotificationAction()).setEmailConfig(ConfigurationValue.TRUE);
    
    	            } else if (NotificationType.PUSH.equals(userConfig.getNotificationType())){
    	                userConfiguration.get(userConfig.getNotificationAction()).setPushConfig(ConfigurationValue.TRUE);
    	            }
    	            // Ignore Feed, they are always enabled
	            }
	        }

	        return new NotificationConfigRequest(request.getUsername(), getUserConfigurationPerGroup(userConfiguration, lang));
	    }

    /**
     * Gets the notification available in the corresponding version
     * @return  notification list
     */
	private List<NotificationConfig> getAvailableNotifications() {
        return notificationConfigDao.findAll(getCmdVersion());
    }


	protected Long getCmdVersion() {
        return CURRENT_VERSION;
    }

    private Map<String, SortedMap<NotificationAction, ActionConfigurationDTO>> getUserConfigurationPerGroup(
			Map<NotificationAction, ActionConfigurationDTO> userConfiguration, String lang) {
		Map<String, SortedMap<NotificationAction, ActionConfigurationDTO>> userConfigurationPerGroup
		= new HashMap<String, SortedMap<NotificationAction, ActionConfigurationDTO>>();

		for (NotificationGroup group : NotificationGroup.values()) {
			userConfigurationPerGroup.put(group.toString(), new  TreeMap<NotificationAction, ActionConfigurationDTO>());
		}

		for (Entry<NotificationAction, ActionConfigurationDTO> config : userConfiguration.entrySet()) {
			if (   !config.getKey().isHideInConfiguration()
					&& !(config.getValue().getPushConfig().isDisabled()  && config.getValue().getEmailConfig().isDisabled()) ) {

				userConfigurationPerGroup.get(config.getValue().getNotificationAction().getGroup().toString()).
				put(config.getValue().getNotificationAction(),
						config.getValue());
			}
		}

		return userConfigurationPerGroup;
	}

}
