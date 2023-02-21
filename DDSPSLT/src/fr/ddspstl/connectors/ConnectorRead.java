package fr.ddspstl.connectors;

import org.omg.dds.sub.Sample.Iterator;

import fr.ddspstl.interfaces.InRead;
import fr.ddspstl.interfaces.OutRead;
import fr.sorbonne_u.components.connectors.AbstractConnector;

public class ConnectorRead extends AbstractConnector implements OutRead {



	@Override
	public String getDataReader(String topic)throws Exception {
		return ((InRead)this.offering).getDataReader(topic);
	}

	@Override
	public Iterator<?> read(String reader) throws Exception{
		return ((InRead)this.offering).read(reader);
	}

}
