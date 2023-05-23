package fr.ddspstl.interfaces;

import org.omg.dds.core.Time;
import org.omg.dds.sub.Sample.Iterator;
import org.omg.dds.topic.TopicDescription;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

/**
 * 
 * @author Hamid KOLLI
 * @author Yanis ALAYOUD
 * 
 * @param <T> : le type de la donnée
 *
 * Interface Implantée par un ConnectorPropagation
 */
public interface Propagation extends OfferedCI, RequiredCI {
	
	
	/**
	 * methode propager : s'occupe d'effectuer la propagation de la donnée
	 * 
	 * @param newObject : la nouvelle donnée à propager
	 * @param topic : le topic auquel il appartient
	 * @param id : .
	 * @param time : le timestamp
	 * @throws Exception
	 */
	public <T> void propager(T newObject, TopicDescription<T> topic, String id, Time time) throws Exception;

	/**
	 * methode consommer : consomme la donnée
	 * 
	 * @param topic : le topic dans lequel on va chercher
	 * @param id : .
	 * @param isFirst : booleen verifiant si cette demande de consommer la donnée est la première
	 * @return Iterator<T> : la donnée lue
	 * @throws Exception
	 */
	public <T> Iterator<T> consommer(TopicDescription<T> topic, String id,boolean isFirst) throws Exception;
}
