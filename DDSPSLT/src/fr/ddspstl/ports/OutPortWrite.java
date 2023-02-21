package fr.ddspstl.ports;

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
	public String getDataWriter(String topic) throws Exception {
		return ((ConnectorWrite)getConnector()).getDataWriter(topic);
	}

	@Override
	public <T> void write(String writer,T data) throws Exception  {
		 ((ConnectorWrite)getConnector()).write(writer,data);
	}

}
