package fr.ddspstl.DDS.publishers.interfaces;


import org.omg.dds.topic.Topic;

import fr.ddspstl.DDS.publishers.data.DataWriter;

/**
 * 
 * @author Hamid KOLLI
 * @author Yanis ALAYOUD
 * 
 *
 * Interface implantée par Publisher
 */
public interface Publisher extends org.omg.dds.pub.Publisher{
	

	/**
	 * Methode createDatawriter : créé un Data Writer
	 * 
	 * @param <TYPE> : le type de la donnée
	 * @param topic : le topic
	 * @param uriPortSortant : l'uri du port sortant
	 * @return DataWriter(TYPE) : le Data Writer créé
	 */
	public <TYPE> DataWriter<TYPE> createDataWriter(Topic<TYPE> topic, String uriPortSortant) ;
}