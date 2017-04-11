package com.ebay.park.push;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to send massive Push Messages.
 */
public abstract class PushBatchExecutor {

    protected int successfulPushes;
    protected List<String> devicesIds = new ArrayList<String>();
    protected int maxDevicesToSend;

    /**
     * Adds devices ids that are desired to send the notification. If max limit
     * is reached, the push notification will be sent it automatically.
     * 
     * @param deviceId
     */
    public void addDevice(String deviceId){
        if (deviceId != null){

            devicesIds.add(deviceId);

            if(devicesIds.size() >= maxDevicesToSend){
                sendPushNotification();
                devicesIds.clear();
            } 
        }
    }

    /**
     * Send Push NotificationMessage if addDevices() did not reach the limit.
     */
    public void flushPush(){
        if(!devicesIds.isEmpty()){
            sendPushNotification();
            devicesIds.clear();
        }
    }

    /**
     * Quantity of Successful Pushes
     *  
     * @return
     */
    public int getSuccessfulPushes(){
        return this.successfulPushes;
    }
    
    /**
     * Clear values from previous executions.
     */
    public void clear() {
        this.successfulPushes = 0;
    }

    /**
     * Sets the message to be pushed.
     * @param message the message to be sent
     */
    public abstract void setPushMessage(String message);

    /**
     * Sends Push Notifications to the Devices Ids.
     */
    protected abstract void sendPushNotification();
}
