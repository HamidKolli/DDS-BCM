package fr.ddspstl.topic.exemple;

public class Notification {
	
	private String senderId;
	private String notif;

	public Notification(String senderId, String notif) {
		super();
		this.senderId = senderId;
		this.notif = notif;
	}

	public String getSenderId() {
		return senderId;
	}

	public String getMessage() {
		return notif;
	}
	


	@Override
	public String toString() {
		return "Notification [senderId=" + senderId + ", notif=" + notif + "]";
	}
	
	
	
	
	
	
	
}
