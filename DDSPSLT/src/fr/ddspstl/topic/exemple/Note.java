package fr.ddspstl.topic.exemple;

/**
 * 
 * @author Hamid KOLLI
 * @author Yanis ALAYOUD
 * 
 *
 * Classe Note pour les tests
 */
public class Note {
	
	private int note;
	private String idStudent;
	private String uE;

	/**
	 * Constructeur
	 * 
	 * @param note : note de l'eleve
	 * @param idStudent : id de l'eleve
	 * @param uE : l'UE
	 */
	public Note(int note, String idStudent, String uE) {
		super();
		this.note = note;
		this.idStudent = idStudent;
		this.uE = uE;
	}

	/**
	 * getter
	 * 
	 * @return int : la note
	 */
	public int getNote() {
		return note;
	}

	/**
	 * getter
	 * 
	 * @return String : l'id de l'eleve
	 */
	public String getIdStudent() {
		return idStudent;
	}

	/**
	 * getter
	 * 
	 * @return String : l'ue concernée
	 */
	public String getuE() {
		return uE;
	}

	@Override
	public String toString() {
		return "Note [note=" + note + ", idStudent=" + idStudent + ", uE=" + uE + "]";
	}
	
	
	

	
}
