package fr.ddspstl.connectors;

import org.omg.dds.topic.TopicDescription;

import org.omg.dds.sub.Sample.Iterator;

import fr.ddspstl.addresses.INodeAddress;
import fr.ddspstl.interfaces.ReadDDSCI;
import fr.sorbonne_u.components.connectors.AbstractConnector;


/**
 * @author Hamid KOLLI
 * @author Yanis ALAYOUD
 *
 * @param <T> : le type de la donnée
 * 
 * Classe Connecteur pour les opérations de read
 */
public class ConnectorReadDDS<T> extends AbstractConnector implements ReadDDSCI<T> {

	
	/**
	 * 
	 * @see fr.ddspstl.interfaces.ReadDDSCI#read(TopicDescription, INodeAddress, String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void read(TopicDescription<T> topic,INodeAddress address , String requestID) throws Exception {
		((ReadDDSCI<T>) this.offering).read(topic,address, requestID);

	}

	/**
	 * 
	 * @see fr.ddspstl.interfaces.ReadDDSCI#take(TopicDescription, INodeAddress, String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void take(TopicDescription<T> topic,INodeAddress address , String requestID) throws Exception {
		((ReadDDSCI<T>) this.offering).take(topic, address,requestID);

	}

	/**
	 * 
	 * @see fr.ddspstl.interfaces.ReadDDSCI#acceptResult(Iterator, String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void acceptResult(Iterator<T> result, String requestID) throws Exception {
		((ReadDDSCI<T>) this.offering).acceptResult(result, requestID);

	}

}
