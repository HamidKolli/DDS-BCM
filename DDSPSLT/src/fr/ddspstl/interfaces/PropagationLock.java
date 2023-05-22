package fr.ddspstl.interfaces;

import org.omg.dds.core.Time;
import org.omg.dds.topic.TopicDescription;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;


/**
 * 
 * @author Hamid KOLLI
 * @author Yanis ALAYOUD
 * 
 *
 * Interface Implantée par un ConnectorLock
 */
public interface PropagationLock extends OfferedCI, RequiredCI {
	
	/**
	 * @param <T> : le type de la donnée
	 * @param topic : le topic
	 * @param idPropagation : l'id de la propagation
	 * @param timestamp : timestamp de la propagation
	 * @return boolean : booleen de retour du lock
	 * @throws Exception
	 */
	public <T> boolean lock(TopicDescription<T> topic, String idPropagation, Time timestamp) throws Exception;

	/**
	 * methode unlock : unlock à la fin de la propagation
	 * 
	 * @param <T> : le type de la donnée
	 * @param topic : le topic
	 * @param idPropagation : l'id de la propagation
	 * @param idPropagationUnlock : l'id de l'unlock de la propagation
	 * @throws Exception
	 */
	public <T> void unlock(TopicDescription<T> topic, String idPropagation,String idPropagationUnlock) throws Exception;
}
