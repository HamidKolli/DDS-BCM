package fr.ddspstl.ports;

import org.omg.dds.pub.DataWriter;
import org.omg.dds.topic.Topic;

import fr.ddspstl.connectors.ConnectorWrite;
import fr.ddspstl.interfaces.OutWrite;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

public class OutPortWrite extends AbstractOutboundPort implements OutWrite {

	private static final long serialVersionUID = 1L;

	public OutPortWrite( ComponentI owner) throws Exception {
		super(OutWrite.class, owner);
	}

	@Override
	public <T> DataWriter<T> getDataWriter(Topic<T> topic) throws Exception {
		return ((ConnectorWrite)getConnector()).getDataWriter(topic);
	}

	@Override
	public <T> T write(DataWriter<T> reader) throws Exception  {
		return ((ConnectorWrite)getConnector()).write(reader);
	}

}
