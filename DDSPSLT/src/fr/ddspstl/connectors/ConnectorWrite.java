package fr.ddspstl.connectors;

import org.omg.dds.pub.DataWriter;
import org.omg.dds.topic.Topic;

import fr.ddspstl.interfaces.OutWrite;
import fr.ddspstl.ports.InPortWrite;
import fr.sorbonne_u.components.connectors.AbstractConnector;

public class ConnectorWrite extends AbstractConnector implements OutWrite {



	@Override
	public <T> DataWriter<T> getDataWriter(Topic<T> topic) throws Exception {
		
		return ((InPortWrite)this.offering).getDataWriter(topic);
	}

	@Override
	public <T> T write(DataWriter<T> reader) throws Exception {
		
		return ((InPortWrite)this.offering).write(reader);
	}

}
