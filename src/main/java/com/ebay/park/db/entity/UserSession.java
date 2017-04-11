package com.ebay.park.db.entity;

import org.hibernate.annotations.UpdateTimestamp;
import org.joda.time.DateTime;

import javax.persistence.*;

import java.util.Date;

/**
 * @author gabriel.sideri
 */
@Entity
@Table(name = "user_session")
public class UserSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "use_ses_id")
    private Long id;

    // bi-directional many-to-one association to User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "use_id")
    private User user;

    @Column(name = "use_ses_token")
    private String token;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "use_ses_last_successful_login", nullable = false)
    @UpdateTimestamp
    private Date lastSuccessfulLogin;

    @Column(name = "use_ses_active")
    private boolean sessionActive;
    
    @Column(name = "use_ses_unique_device_id")
    private String uniqueDeviceId;

    @OneToOne(mappedBy = "userSession", optional=true, cascade = CascadeType.ALL)
    private Device device;
    
    /**
     * The Swrve user ID
     */
    @Column(name="use_ses_swrve_id")
    private String swrveId;

    public UserSession(){

    }

    public UserSession(String token, String uniqueDeviceId) {
        setToken(token);
        setUniqueDeviceId(uniqueDeviceId);
        setSessionActive(true);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getLastSuccessfulLogin() {
        return lastSuccessfulLogin;
    }

    public void setLastSuccessfulLogin(Date lastSuccessfulLogin) {
        this.lastSuccessfulLogin = lastSuccessfulLogin;
    }


    public void updateLastSuccessfulLogin() {
        this.lastSuccessfulLogin = DateTime.now().toDate();
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public boolean isSessionActive() {
        return sessionActive;
    }

    public void setSessionActive(boolean sessionActive) {
        this.sessionActive = sessionActive;
    }

    public void addDevice(String deviceId, DeviceType platform){
        Device device = new Device(this, deviceId, platform);
        this.setDevice(device);
    }
    
    public void setUniqueDeviceId(String uniqueDeviceId) {
    	this.uniqueDeviceId = uniqueDeviceId;
    }

    public String getUniqueDeviceId() {
    	return uniqueDeviceId;
    }
    
    public String getSwrveId() {
		return swrveId;
	}

	public void setSwrveId(String swrveId) {
		this.swrveId = swrveId;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserSession that = (UserSession) o;

        return id.equals(that.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("UserSession [")
                .append("Id=")
                .append(id)
                .append(", userId=")
                .append(user == null ? "null" : user.getId())
                .append(", token=")
                .append(token)
                .append(", last login=")
                .append(lastSuccessfulLogin == null ? "null" : lastSuccessfulLogin.toString())
                .append(", session active=")
                .append(sessionActive)
                .append(", device=")
                .append(device == null ? "null" : device.toString())
                .append("]");
        return builder.toString();
    }
}
