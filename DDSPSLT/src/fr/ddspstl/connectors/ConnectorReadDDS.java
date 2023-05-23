package fr.ddspstl.connectors;

import org.omg.dds.sub.Sample.Iterator;
import org.omg.dds.topic.TopicDescription;

import fr.ddspstl.addresses.INodeAddress;
import fr.ddspstl.interfaces.ReadDDSCI;
import fr.sorbonne_u.components.connectors.AbstractConnector;

/**
 * @author Hamid KOLLI
 * @author Yanis ALAYOUD
 *
 * @param <T> : le type de la donnée
 * 
 *            Classe Connecteur pour les opérations de read
 */
public class ConnectorReadDDS extends AbstractConnector implements ReadDDSCI {

	/**
	 * 
	 * @see fr.ddspstl.interfaces.ReadDDSCI#read(TopicDescription, INodeAddress,
	 *      String)
	 */
	@Override
	public void read(TopicDescription<?> topic, INodeAddress address, String requestID) throws Exception {
		((ReadDDSCI) this.offering).read(topic, address, requestID);

	}

	/**
	 * 
	 * @see fr.ddspstl.interfaces.ReadDDSCI#take(TopicDescription, INodeAddress,
	 *      String)
	 */
	@Override
	public void take(TopicDescription<?> topic, INodeAddress address, String requestID) throws Exception {
		((ReadDDSCI) this.offering).take(topic, address, requestID);

	}

	/**
	 * 
	 * @see fr.ddspstl.interfaces.ReadDDSCI#acceptResult(Iterator, String)
	 */
	@Override
	public void acceptResult(Iterator<?> result, String requestID) throws Exception {
		((ReadDDSCI) this.offering).acceptResult(result, requestID);

	}

}
