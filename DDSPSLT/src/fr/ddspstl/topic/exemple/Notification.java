package fr.ddspstl.topic.exemple;

/**
 * 
 * @author Hamid KOLLI
 * @author Yanis ALAYOUD
 * 
 *
 * Classe Notification pour les tests
 */
public class Notification {
	
	private String senderId;
	private String notif;

	/**
	 * Constructeur
	 * 
	 * @param senderId : l'ID de la personne envoyant la notif
	 * @param notif : le contenu de la notif
	 */
	public Notification(String senderId, String notif) {
		super();
		this.senderId = senderId;
		this.notif = notif;
	}

	/**
	 * getter
	 * 
	 * @return String : l'id du sender
	 */
	public String getSenderId() {
		return senderId;
	}

	/**
	 * getter
	 * 
	 * @return String : le contenu de la notif / le message
	 */
	public String getMessage() {
		return notif;
	}
	


	@Override
	public String toString() {
		return "Notification [senderId=" + senderId + ", notif=" + notif + "]";
	}
	
	
	
	
	
	
	
}
