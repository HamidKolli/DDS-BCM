package fr.ddspstl.topic.exemple;

/**
 * 
 * @author Hamid KOLLI
 * @author Yanis ALAYOUD
 * 
 *
 * Classe Temperature pour les tests
 */
public class Temperature  {

	private String lieu;
	private Integer temp;

	/**
	 * Constructeur
	 * 
	 * @param lieu : lieu ou la temperature est prise
	 * @param temp : date a laquelle elle est prise
	 */
	public Temperature(String lieu, Integer temp) {
		super();
		this.lieu = lieu;
		this.temp = temp;
	}

	/**
	 * getter
	 * 
	 * @return String : le lieu
	 */
	public String getLieu() {
		return lieu;
	}

	/**
	 * getter
	 * 
	 * @return Integer : la température
	 */
	public Integer getTemp() {
		return temp;
	}

	@Override
	public String toString() {
		return "Temperature [lieu=" + lieu + ", temp=" + temp + "]";
	}
	
	
}
