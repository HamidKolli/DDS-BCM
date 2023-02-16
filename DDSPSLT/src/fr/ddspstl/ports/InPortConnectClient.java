package fr.ddspstl.ports;

import org.omg.dds.topic.Topic;

import fr.ddspstl.components.DDSNode;
import fr.ddspstl.interfaces.ConnectInClient;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

public class InPortConnectClient<T> extends AbstractInboundPort implements ConnectInClient<T> {

	private static final long serialVersionUID = 1L;

	public InPortConnectClient(String uri, ComponentI owner) throws Exception {
		super(uri, ConnectInClient.class, owner);
	}

	@Override
	public Topic<T> connect(int domainID, String topicName) throws Exception {
		return getOwner().handleRequest(new AbstractComponent.AbstractService<Topic<T>>() {
			@Override
			public Topic<T> call() throws Exception {
				return ((DDSNode)getOwner()).connect(domainID,topicName);
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
