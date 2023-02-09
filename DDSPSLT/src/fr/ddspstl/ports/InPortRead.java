package fr.ddspstl.ports;

import org.omg.dds.sub.DataReader;
import org.omg.dds.topic.Topic;

import fr.ddspstl.components.DDSNode;
import fr.ddspstl.interfaces.InRead;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

public class InPortRead extends AbstractInboundPort implements InRead{

	private static final long serialVersionUID = 1L;

	public InPortRead(ComponentI owner) throws Exception {
		super(InRead.class, owner);
		
	}

	@Override
	public <T> DataReader<T> getDataReader(Topic<T> topic) throws Exception{
		return getOwner().handleRequest(new AbstractComponent.AbstractService<DataReader<T>>() {
			@Override
			public DataReader<T> call() throws Exception {
				return ((DDSNode)getOwner()).getDataReader(topic);
			}
		});
	}

	@Override
	public <T> T read(DataReader<T> reader) throws Exception{
		return getOwner().handleRequest(new AbstractComponent.AbstractService<T>() {
			@Override
			public T call() throws Exception {
				return ((DDSNode)getOwner()).read(reader);
			}
		});
	}

}
