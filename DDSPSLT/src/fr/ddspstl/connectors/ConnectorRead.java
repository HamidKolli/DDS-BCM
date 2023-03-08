package fr.ddspstl.connectors;

import org.omg.dds.sub.Sample.Iterator;
import org.omg.dds.topic.TopicDescription;

import fr.ddspstl.interfaces.ReadCI;
import fr.sorbonne_u.components.connectors.AbstractConnector;

public class ConnectorRead<TYPE> extends AbstractConnector implements ReadCI<TYPE> {


	public  Iterator<TYPE> read(TopicDescription<TYPE> topic) throws Exception {
		return ((ReadCI<TYPE> )this.offering).read(topic);

	}

}
