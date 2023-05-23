package fr.ddspstl.ports;

import org.omg.dds.sub.Sample.Iterator;
import org.omg.dds.topic.TopicDescription;

import fr.ddspstl.addresses.INodeAddress;
import fr.ddspstl.interfaces.ReadDDSCI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

/**
 * 
 * @author Hamid KOLLI
 * @author Yanis ALAYOUD
 * 
 * @param <T> : type de la donnée
 *
 * Classe OutPortReadDDS
 */
public class OutPortReadDDS<T> extends AbstractOutboundPort implements ReadDDSCI<T>{


	private static final long serialVersionUID = 1L;

	/**
	 * Constructeur
	 * 
	 * @param owner : l'owner du port
	 * @throws Exception
	 */
	public OutPortReadDDS(ComponentI owner) throws Exception {
		super(ReadDDSCI.class, owner);
	}

	/**
	 * @see fr.ddspstl.interfaces.ReadDDSCI#read(TopicDescription, INodeAddress, String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void read(TopicDescription<T> topic,INodeAddress address , String requestID) throws Exception {
		((ReadDDSCI<T>)this.getConnector()).read(topic,address, requestID);
		
	}

	/**
	 * @see fr.ddspstl.interfaces.ReadDDSCI#take(TopicDescription, INodeAddress, String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void take(TopicDescription<T> topic,INodeAddress address , String requestID) throws Exception {
		((ReadDDSCI<T>)this.getConnector()).take(topic,address, requestID);
		
	}

	/**
	 * @see fr.ddspstl.interfaces.ReadDDSCI#acceptResult(Iterator, String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void acceptResult(Iterator<T> result, String requestID) throws Exception {
		((ReadDDSCI<T>)this.getConnector()).acceptResult(result, requestID);
		
	}

}
