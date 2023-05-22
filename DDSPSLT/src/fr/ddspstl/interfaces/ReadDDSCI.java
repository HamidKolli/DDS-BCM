package fr.ddspstl.interfaces;


import org.omg.dds.sub.Sample.Iterator;
import org.omg.dds.topic.TopicDescription;

import fr.ddspstl.addresses.INodeAddress;
import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

/**
 * 
 * @author Hamid KOLLI
 * @author Yanis ALAYOUD
 * 
 * @param <T> : le type de la donnée
 *
 * Interface Implantée par un ConnectorReadDDS
 */
public interface ReadDDSCI<T> extends OfferedCI, RequiredCI {
	
	/**
	 * methode read : effetcue un read
	 * 
	 * @param topic : le topic
	 * @param address : l'adresse du noeud
	 * @param requestID : ID de la requete read
	 * @throws Exception
	 */
	public void read(TopicDescription<T> topic,INodeAddress address , String requestID) throws Exception;

	/**
	 * methode take : effectue un take
	 * 
	 * @param topic : le topic
	 * @param address : l'adresse du noeud
	 * @param requestID : ID de la requete take
	 * @throws Exception
	 */
	public void take(TopicDescription<T> topic,INodeAddress address , String requestID) throws Exception;

	/**
	 * methode acceptResult : methode de retour du read/take
	 * 
	 * @param result : le résultat retourné
	 * @param requestID : ID de la requete
	 * @throws Exception
	 */
	public void acceptResult(Iterator<T> result, String requestID) throws Exception;
}
