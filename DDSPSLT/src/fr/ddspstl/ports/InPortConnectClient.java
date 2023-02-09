package fr.ddspstl.ports;

import java.util.Set;

import org.omg.dds.topic.Topic;

import fr.ddspstl.components.DDSNode;
import fr.ddspstl.interfaces.ConnectInClient;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

public class InPortConnectClient extends AbstractInboundPort implements ConnectInClient {

	private static final long serialVersionUID = 1L;

	public InPortConnectClient(String uri, ComponentI owner) throws Exception {
		super(uri, ConnectInClient.class, owner);
	}

	@Override
	public Set<Topic<Object>> connect() throws Exception {
		return getOwner().handleRequest(new AbstractComponent.AbstractService<Set<Topic<Object>>>() {
			@Override
			public Set<Topic<Object>> call() throws Exception {
				return ((DDSNode)getOwner()).connect();
			}
		});
	}

	@Override
	public String getReaderURI() throws Exception{
		return getOwner().handleRequest(new AbstractComponent.AbstractService<String>() {
			@Override
			public String call() throws Exception {
				return ((DDSNode)getOwner()).getReaderURI();
			}
		});
	}

	@Override
	public String getWriterURI() throws Exception{
		return getOwner().handleRequest(new AbstractComponent.AbstractService<String>() {
			@Override
			public String call() throws Exception {
				return ((DDSNode)getOwner()).getWriterURI();
			}
		});
	}

	@Override
	public void disconnectClient() throws Exception{
		getOwner().handleRequest(new AbstractComponent.AbstractService<Void>() {
			@Override
			public Void call() throws Exception {
				((DDSNode)getOwner()).disconnectClient();
				return null;
			}
		});

	}

}
