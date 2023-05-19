package fr.ddspstl.ports;

import org.omg.dds.sub.Sample.Iterator;
import org.omg.dds.topic.TopicDescription;

import fr.ddspstl.addresses.INodeAddress;
import fr.ddspstl.interfaces.ReadDDSCI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

public class OutPortReadDDS<T> extends AbstractOutboundPort implements ReadDDSCI<T>{


	private static final long serialVersionUID = 1L;

	public OutPortReadDDS(ComponentI owner) throws Exception {
		super(ReadDDSCI.class, owner);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void read(TopicDescription<T> topic,INodeAddress address , String requestID) throws Exception {
		((ReadDDSCI<T>)this.getConnector()).read(topic,address, requestID);
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public void take(TopicDescription<T> topic,INodeAddress address , String requestID) throws Exception {
		((ReadDDSCI<T>)this.getConnector()).take(topic,address, requestID);
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public void acceptResult(Iterator<T> result, String requestID) throws Exception {
		((ReadDDSCI<T>)this.getConnector()).acceptResult(result, requestID);
		
	}

}
