package fr.ddspstl.topic.exemple;

public class Note {
	
	private int note;
	private String idStudent;
	private String uE;

	public Note(int note, String idStudent, String uE) {
		super();
		this.note = note;
		this.idStudent = idStudent;
		this.uE = uE;
	}

	public int getNote() {
		return note;
	}

	public String getIdStudent() {
		return idStudent;
	}

	public String getuE() {
		return uE;
	}

	@Override
	public String toString() {
		return "Note [note=" + note + ", idStudent=" + idStudent + ", uE=" + uE + "]";
	}
	
	
	

	
}
