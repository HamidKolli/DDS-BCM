package fr.ddspstl.DDS.subscribers.interfaces;

import org.omg.dds.topic.TopicDescription;

/**
 * 
 * @author Hamid KOLLI
 * @author Yanis ALAYOUD
 * 
 *
 * Interface implantée par Subscriber
 */
public interface Subscriber extends org.omg.dds.sub.Subscriber{
	/**
	 * Methode createDataReader : créé un Data Reader
	 * 
	 * @param <TYPE> : le type de la donnée
	 * @param topic : le topic
	 * @param uriPortDDSNode : l'uri du port entrant du noeud DDS
	 * @return
	 */
	public <TYPE> org.omg.dds.sub.DataReader<TYPE> createDataReader(TopicDescription<TYPE> topic,String uriPortDDSNode) ;
}