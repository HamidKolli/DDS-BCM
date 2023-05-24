package fr.ddspstl.topic.exemple;

public class Temperature  {

	private String lieu;
	private Integer temp;

	public Temperature(String lieu, Integer temp) {
		super();
		this.lieu = lieu;
		this.temp = temp;
	}

	public String getLieu() {
		return lieu;
	}

	public Integer getTemp() {
		return temp;
	}

	@Override
	public String toString() {
		return "Temperature [lieu=" + lieu + ", temp=" + temp + "]";
	}
	
	
}
