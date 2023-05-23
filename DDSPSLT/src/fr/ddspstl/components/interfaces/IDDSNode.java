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
public interface IDDSNode {


	/**
	 * methode propager : s'occupe d'effectuer la propagation de la donnée
	 * 
	 * @param newObject : la nouvelle donnée à propager
	 * @param topic : le topic auquel il appartient
	 * @param id : l'identifiant de la requete
	 * @param modifiableTime : le timestamp
	 * @throws Exception
	 */
	public <T> void propager(T newObject, TopicDescription<T> topic, String id, Time modifiableTime) throws Exception;

	/**
	 * methode consommer : s'occupe de commencer le take
	 * 
	 * @param topic : le topic dans lequel on va chercher
	 * @param id : l'identifiant de la requete
	 * @param isFirst : true si c'est le noeud qui lance la requete, false sinon
	 * @return Iterator<T> : la donnée lue
	 * @throws Exception
	 */
	public <T> Iterator<T> consommer(TopicDescription<T> topic, String id, boolean isFirst) throws Exception;

	/**
	 * methode read : s'occupe de lire la donnée
	 * 
	 * @param topic : le topic dans lequel on va chercher
	 * @return Iterator<T> : la donnée lue
	 * @throws Exception
	 */
	public <T> Iterator<T> read(TopicDescription<T> topic) throws Exception;

}
