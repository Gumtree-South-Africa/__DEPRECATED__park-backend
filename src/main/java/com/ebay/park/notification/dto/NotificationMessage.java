/*
 * Copyright eBay, 2014
 */
package com.ebay.park.notification.dto;

import com.ebay.park.notification.NotificationAction;
import com.ebay.park.notification.NotificationDispatcher;
import com.ebay.park.notification.NotificationType;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A notification message to be queued.
 * <p><i>Note that it must implement {@link Serializable} since its instances will be queued in JMS queues </i></p>
 *
 * @author jpizarro
 * @author gervasio.amy
 */
public abstract class NotificationMessage implements Serializable {

	protected final NotificationAction action;
	protected final NotificationType type;
	protected Map<String, String> props = new HashMap<String, String>();

	protected NotificationMessage(NotificationAction action, NotificationType type, Map<String, String> props) {
		this.action = action;
		this.type = type;
		this.props = props;
	}

	public String get(String key) {
		return props.get(key);
	}

	public NotificationAction getAction() {
		return action;
	}

	public NotificationType getType() {
		return type;
	}

	public Set<String> getKeys() {
		return props.keySet();
	}

	public void setProperty(String key, String value) {
	    props.put(key, value);
	}
	
	@Override
	public String toString(){
		StringBuilder notification = new StringBuilder();
		notification.append("action: ");
		notification.append(action);
		notification.append(" type: ");
		notification.append(type);
		for (String key : props.keySet()){
			notification.append(" key: ");
			notification.append(props.get(key));			
		}
		return notification.toString();
	}

	public abstract NotificationMessage doDispatch(NotificationDispatcher notificationDispatcher);

    public Map<String,String> getParams() {
        return props;
    }
}
