package fr.ddspstl.exceptions;

/**
 * 
 * @author Hamid KOLLI
 * @author Yanis ALAYOUD
 * 
 *
 * Classe d'exception pour un topic non trouvé
 */
public class DDSTopicNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public DDSTopicNotFoundException() {
		super();
		// TODO Auto-generated constructor stub
	}



	public DDSTopicNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public DDSTopicNotFoundException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	

}