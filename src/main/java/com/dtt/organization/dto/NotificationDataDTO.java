/**
 * 
 */
package com.dtt.organization.dto;

import java.io.Serializable;

/**
 * @author Raxit Dubey
 *
 */
public class NotificationDataDTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String body;

    private String title;

    private NotificationContextDTO notificationContext;

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public NotificationContextDTO getNotificationContext() {
		return notificationContext;
	}

	public void setNotificationContext(NotificationContextDTO notificationContext) {
		this.notificationContext = notificationContext;
	}

	@Override
	public String toString() {
		return "NotificationDataDTO [body=" + body + ", title=" + title + ", notificationContext=" + notificationContext
				+ "]";
	}
}
