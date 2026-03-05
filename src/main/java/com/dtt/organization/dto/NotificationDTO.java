/**
 * 
 */
package com.dtt.organization.dto;

import java.io.Serializable;

/**
 * @author Raxit Dubey
 *
 */
public class NotificationDTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String to;

    private String priority;

    private NotificationDataDTO data;

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public NotificationDataDTO getData() {
		return data;
	}

	public void setData(NotificationDataDTO data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "NotificationDTO [to=" + to + ", priority=" + priority + ", data=" + data + "]";
	}   

}
