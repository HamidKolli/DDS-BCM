package fr.ddspstl.connectors;

import fr.ddspstl.interfaces.OutWrite;
import fr.ddspstl.ports.InPortWrite;
import fr.sorbonne_u.components.connectors.AbstractConnector;

public class ConnectorWrite extends AbstractConnector implements OutWrite {



	@Override
	public String getDataWriter(String topic) throws Exception {
		
		return ((InPortWrite)this.offering).getDataWriter(topic);
	}

	@Override
	public <T> void write(String reader, T data) throws Exception {
		
		((InPortWrite)this.offering).write(reader,data);
		
	}

}
