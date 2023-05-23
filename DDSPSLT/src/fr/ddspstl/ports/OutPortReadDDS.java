package fr.ddspstl.ports;

import org.omg.dds.sub.Sample.Iterator;
import org.omg.dds.topic.TopicDescription;

import fr.ddspstl.addresses.INodeAddress;
import fr.ddspstl.interfaces.ReadDDSCI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

public class OutPortReadDDS extends AbstractOutboundPort implements ReadDDSCI{


	private static final long serialVersionUID = 1L;

	public OutPortReadDDS(ComponentI owner) throws Exception {
		super(ReadDDSCI.class, owner);
	}

	@Override
	public void read(TopicDescription<?> topic,INodeAddress address , String requestID) throws Exception {
		((ReadDDSCI)this.getConnector()).read(topic,address, requestID);
		
	}

	@Override
	public void take(TopicDescription<?> topic,INodeAddress address , String requestID) throws Exception {
		((ReadDDSCI)this.getConnector()).take(topic,address, requestID);
		
	}

	@Override
	public void acceptResult(Iterator<?> result, String requestID) throws Exception {
		((ReadDDSCI)this.getConnector()).acceptResult(result, requestID);
		
	}

}
