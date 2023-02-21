package fr.ddspstl.ports;

import org.omg.dds.sub.Sample.Iterator;

import fr.ddspstl.connectors.ConnectorRead;
import fr.ddspstl.interfaces.OutRead;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

public class OutPortRead extends AbstractOutboundPort implements OutRead {

	private static final long serialVersionUID = 1L;

	public OutPortRead( ComponentI owner) throws Exception {
		super(OutRead.class, owner);
	}

	@Override
	public String getDataReader(String topic) throws Exception {
		return ((ConnectorRead)getConnector()).getDataReader(topic);
	}

	@Override
	public Iterator<?> read(String reader) throws Exception {
		return ((ConnectorRead)getConnector()).read(reader);
	}

}
