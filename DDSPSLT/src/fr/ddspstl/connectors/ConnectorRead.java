package fr.ddspstl.connectors;

import org.omg.dds.sub.DataReader;
import org.omg.dds.topic.Topic;

import fr.ddspstl.interfaces.InRead;
import fr.ddspstl.interfaces.OutRead;
import fr.sorbonne_u.components.connectors.AbstractConnector;

public class ConnectorRead extends AbstractConnector implements OutRead {



	@Override
	public <T> DataReader<T> getDataReader(Topic<T> topic)throws Exception {
		return ((InRead)this.offering).getDataReader(topic);
	}

	@Override
	public <T> T read(DataReader<T> reader) throws Exception{
		return ((InRead)this.offering).read(reader);
	}

}
