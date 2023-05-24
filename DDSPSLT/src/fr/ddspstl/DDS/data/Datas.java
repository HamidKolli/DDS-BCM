package fr.ddspstl.DDS.data;

import org.omg.dds.core.Time;
import org.omg.dds.sub.InstanceState;
import org.omg.dds.sub.Sample;
import org.omg.dds.sub.SampleState;
import org.omg.dds.sub.ViewState;
import org.omg.dds.topic.TopicDescription;


/**
 * 
 * @author Hamid KOLLI
 * @author Yanis ALAYOUD
 * 
 * @param <T> : le type de la donnée
 * 
 * Classe Datas représentant la donnée
 */

public class Datas<T>  {
	
	private fr.ddspstl.DDS.samples.Sample.Iterator<T> samplesData;
	private TopicDescription<T> topic;

	
	/**
	 * @param topic : le topic auquel les données appartiennent
	 */
	public Datas(TopicDescription<T> topic) {
		super();
	
		this.topic = topic;
		this.samplesData = new fr.ddspstl.DDS.samples.Sample.Iterator<T>();
	}

	
	/**
	 * methode read : effectue un read sur la donnée
	 * 
	 * @return Sample.Iterator(T) : la donnée retournée
	 * @throws CloneNotSupportedException 
	 */
	public Sample.Iterator<T> read() throws CloneNotSupportedException {
		return samplesData.clone();
	}

	/**
	 * methode take : effectue un take sur la donnée
	 * 
	 * @return Sample.Iterator(T) : la donnée retournée
	 */
	public Sample.Iterator<T> take() {
		Sample.Iterator<T> tmp = samplesData;
		samplesData =  new fr.ddspstl.DDS.samples.Sample.Iterator<T>();
		return tmp;
	}
	
	/**
	 * methode write : effectue un write
	 * 
	 * @param data : la donnée à écrire
	 * @param time : le timestamp de l'ecriture
	 */
	@SuppressWarnings("unchecked")
	public void write(Object data, Time time) {
		samplesData.add(new fr.ddspstl.DDS.samples.Sample<T>(topic.getEnvironment(),(T) data, SampleState.READ, ViewState.NEW,
				InstanceState.ALIVE, time));
	}

	
}
