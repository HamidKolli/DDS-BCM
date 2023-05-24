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
 *
 * Classe OutPortReadDDS
 */
public class OutPortReadDDS extends AbstractOutboundPort implements ReadDDSCI{


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
	@Override
	public void read(TopicDescription<?> topic,INodeAddress address , String requestID) throws Exception {
		((ReadDDSCI)this.getConnector()).read(topic,address, requestID);
		
	}

	/**
	 * @see fr.ddspstl.interfaces.ReadDDSCI#take(TopicDescription, INodeAddress, String)
	 */
	@Override
	public void take(TopicDescription<?> topic,INodeAddress address , String requestID) throws Exception {
		((ReadDDSCI)this.getConnector()).take(topic,address, requestID);
		
	}

	/**
	 * @see fr.ddspstl.interfaces.ReadDDSCI#acceptResult(Iterator, String)
	 */
	@Override
	public void acceptResult(Iterator<?> result, String requestID) throws Exception {
		((ReadDDSCI)this.getConnector()).acceptResult(result, requestID);
		
	}

}
