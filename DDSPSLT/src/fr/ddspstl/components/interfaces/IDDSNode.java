package fr.ddspstl.components.interfaces;

import org.omg.dds.core.Time;
import org.omg.dds.sub.Sample.Iterator;
import org.omg.dds.topic.TopicDescription;

/**
 * 
 * @author Hamid KOLLI
 * @author Yanis ALAYOUD
 * 
 * @param <T> : le type de la donnée
 *
 * Interface implantée par un noeud DDS
 */
public interface IDDSNode<T> {


	/**
	 * methode propager : s'occupe d'effectuer la propagation de la donnée
	 * 
	 * @param newObject : la nouvelle donnée à propager
	 * @param topic : le topic auquel il appartient
	 * @param id : .
	 * @param modifiableTime : le timestamp
	 * @throws Exception
	 */
	public void propager(T newObject, TopicDescription<T> topic, String id, Time modifiableTime) throws Exception;

	/**
	 * methode consommer : s'occupe de commencer le take
	 * 
	 * @param topic : le topic dans lequel on va chercher
	 * @param id : .
	 * @param b : .
	 * @return Iterator<T> : la donnée lue
	 * @throws Exception
	 */
	public Iterator<T> consommer(TopicDescription<T> topic, String id, boolean b) throws Exception;

	/**
	 * methode read : s'occupe de lire la donnée
	 * 
	 * @param topic : le topic dans lequel on va chercher
	 * @return Iterator<T> : la donnée lue
	 * @throws Exception
	 */
	public Iterator<T> read(TopicDescription<T> topic) throws Exception;

}
